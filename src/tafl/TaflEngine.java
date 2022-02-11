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

        // player wins by destroying all the enemies
        if (!enemyExists(currentPlayer)) {
            status = Status.Win;
            return true;
        }

        // TODO: support draw here

        switchPlayer();
        return true;
    }

    private boolean enemyExists (final Player player) {
        for (int y = 0; y < field.height; y ++)
            for (int x = 0; x < field.width; x ++) {
                final Optional<AbstractFigure> cell = field.getCell(new Vector(x, y));

                if (cell.isPresent() && cell.get().player != player)
                    return true;
            }

        return false;
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

            if (!(
                !field.isCellValid(supporter) ||
                ((TaflField)field).isCornerCell(supporter) ||
                ((TaflField)field).isCenterCell(supporter) && !settings.isKingAllowedToReturnToTheThrone() ||
                field.getCell(supporter).isPresent() && field.getCell(supporter).get().player != king.player
            ))
                return false;
        }

        return true;
    }

    // TODO: introduce an example of avoiding casts to TaflField by using generics
    /// TODO: Use custom exceptions for all the errors
    private boolean isMoveValid (final Move move) {
        if (
            // validate initial cell
            !field.isCellValid(move.from()) || field.getCell(move.from()).isEmpty() ||
            // validate target cell
            !field.isCellValid(move.to()) || field.getCell(move.to()).isPresent() ||
            // initial cell be occupied with the current player's figure
            field.getCell(move.from()).get().player != currentPlayer
        )
            return false;

        final AbstractFigure figure = field.getCell(move.from()).get();

        if (
            figure instanceof TaflWarrior ?
            // throne and corner cells are inaccessible for warriors
            ((TaflField)field).isCenterCell(move.to()) || ((TaflField)field).isCornerCell(move.to()) :
            // throne is inaccessible even for the king if he is not allowed to return to it
            ((TaflField)field).isCenterCell(move.to()) && !settings.isKingAllowedToReturnToTheThrone()
        )
            return false;

        final int moveDistance = Vector.distance(move.from(), move.to());

        if (
            // cells should be different and connected by a straight (vertical or horizontal) line
            (move.from().x() == move.to().x()) == (move.from().y() == move.to().y()) ||
            // move distance should be equal to one if figure move distance is restricted
            settings.isFigureMoveDistanceRestricted() && Vector.distance(move.from(), move.to()) > 1
        )
            return false;

        final Vector direction = move.to().subtract(move.from()).divide(moveDistance);

        for (
            Vector current = move.from().add(direction);
            !current.equals(move.to());
            current = current.add(direction)
        )
            if (
                // Figures can't collide or hop over each other
                field.getCell(current).isPresent() || (
                    // Warriors can't step over the throne (as well as the king, in the case when he isn't allowed to)
                    ((TaflField)field).isCenterCell(current) &&
                    (figure instanceof TaflWarrior || settings.isKingAllowedToReturnToTheThrone())
                )
            )
                return false;

        return true;
    }

    private final TaflSettings settings;
}
