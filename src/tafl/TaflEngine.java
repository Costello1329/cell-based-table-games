package tafl;

import core.*;

import java.util.Optional;

public class TaflEngine extends AbstractEngine {
    public TaflEngine (
        final FieldFactory factory,
        final TaflSettings settings
    ) {
        super(factory, Player.Black);
        this.settings = settings;
    }

    @Override
    public boolean makeMove (final Move move) {
        if (status != Status.Active)
            return false;

        if (!isMoveValid(move))
            return false;

        final AbstractFigure figure = field.getCell(move.from()).get();
        field.placeFigure(move.to(), figure);
        field.removeFigure(move.from());

        // player wins by moving the king to the corner
        if (((TaflField)field).isCornerCell(move.to()) && figure instanceof TaflKing) {
            status = Status.Win;
            return true;
        }

        // check captures
        for (final Vector direction : Vector.directions) {
            final Vector candidate = move.to().add(direction);
            final Vector supporter = move.to().add(direction.multiply(2));

            if (isWarriorCaptured(candidate, supporter))
                field.removeFigure(candidate);

            if (isKingCaptured(candidate)) {
                status = Status.Win;
                return true;
            }
        }

        // current player wins if the enemy has no moves
        if (enemyHasNoMoves(currentPlayer)) {
            status = Status.Win;
            return true;
        }

        switchPlayer();
        return true;
    }

    private boolean enemyHasNoMoves (final Player player) {
        for (int y = 0; y < field.height; y ++)
            for (int x = 0; x < field.width; x ++) {
                final Vector position = new Vector(x, y);
                final Optional<AbstractFigure> cell = field.getCell(position);

                if (cell.isEmpty() || cell.get().player == player)
                    continue;

                final AbstractFigure figure = cell.get();

                for (final Vector direction : Vector.directions) {
                    final Vector candidate = position.add(direction);

                    if (
                        field.isCellValid(candidate) &&
                        field.getCell(candidate).isEmpty() &&
                        isCellAccessibleForTheFigure(candidate, figure)
                    )
                        return false;
                }
            }

        return true;
    }

    private boolean isWarriorCaptured (final Vector candidate, final Vector supporter) {
        return
            field.isCellValid(candidate) &&
            field.getCell(candidate).isPresent() &&
            field.getCell(candidate).get() instanceof final TaflWarrior warrior &&
            warrior.player != currentPlayer &&
            field.isCellValid(supporter) && (
                ((TaflField)field).isCornerCell(supporter) ||
                field.getCell(supporter).isPresent() && field.getCell(supporter).get().player != warrior.player
            );
    }

    private boolean isKingCaptured (final Vector candidate) {
        if (
            !field.isCellValid(candidate) ||
            field.getCell(candidate).isEmpty() ||
            !(field.getCell(candidate).get() instanceof final TaflKing king) ||
            king.player == currentPlayer
        )
            return false;

        for (final Vector direction : Vector.directions) {
            final Vector supporter = candidate.add(direction);

            if (
                field.isCellValid(supporter) &&
                !((TaflField)field).isCornerCell(supporter) &&
                !((TaflField)field).isCenterCell(supporter) &&
                (field.getCell(supporter).isEmpty() || field.getCell(supporter).get().player == king.player)
            )
                return false;
        }

        return true;
    }

    private boolean isCellAccessibleForTheFigure(final Vector cell, final AbstractFigure figure) {
        return
            figure instanceof TaflWarrior ?
            // throne and corner cells are inaccessible for warriors
            !((TaflField)field).isCenterCell(cell) && !((TaflField)field).isCornerCell(cell) :
            // throne is inaccessible even for the king if he is not allowed to return to it
            !((TaflField)field).isCenterCell(cell) || settings.isKingAllowedToReturnToTheThrone();
    }

    // TODO: introduce an example of avoiding casts to TaflField by using generics
    // TODO: Use custom exceptions for all the errors
    private boolean isMoveValid (final Move move) {
        if (
            // validate initial cell
            !field.isCellValid(move.from()) || field.getCell(move.from()).isEmpty() ||
            // validate target cell
            !field.isCellValid(move.to()) || field.getCell(move.to()).isPresent() ||
            // initial cell should be occupied with the current player's figure
            field.getCell(move.from()).get().player != currentPlayer
        )
            return false;

        final AbstractFigure figure = field.getCell(move.from()).get();

        if (!isCellAccessibleForTheFigure(move.to(), figure))
            return false;

        final int moveDistance = Vector.distance(move.from(), move.to());

        if (
            // cells should be different and connected by a straight (vertical or horizontal) line
            (move.from().x() == move.to().x()) == (move.from().y() == move.to().y()) ||
            // move distance should be equal to one if the figure's move distance is restricted
            settings.isFigureMoveDistanceRestricted() && moveDistance > 1
        )
            return false;

        final Vector direction = move.to().subtract(move.from()).divide(moveDistance);

        // Line should be clear of any obstacles
        for (Vector current = move.from().add(direction); !current.equals(move.to()); current = current.add(direction))
            if (field.getCell(current).isPresent() || !isCellAccessibleForTheFigure(current, figure))
                return false;

        return true;
    }

    private final TaflSettings settings;
}
