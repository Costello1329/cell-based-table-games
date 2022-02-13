package tafl;

import core.*;

import java.util.Optional;

public class TaflEngine extends AbstractEngine<TaflField, TaflMove> {
    public TaflEngine (final FieldFactory<TaflField> factory, final TaflSettings settings) {
        super(factory, Player.BLACK);
        this.settings = settings;
    }

    @Override
    protected void performMove (final TaflMove move) throws InvalidMoveException {
        validateMove(move);

        final AbstractFigure figure = field.getCell(move.from()).get();
        field.placeFigure(move.to(), figure);
        field.removeFigure(move.from());

        // check captures
        for (final Vector direction : Vector.DIRECTIONS) {
            final Vector candidate = move.to().add(direction);
            final Vector supporter = move.to().add(direction.multiply(2));

            if (isWarriorCaptured(candidate, supporter))
                field.removeFigure(candidate);

            if (isKingCaptured(candidate)) {
                setStatus(Status.WIN);
                return;
            }
        }

        if (
            // win by moving the king to the corner
            field.isCornerCell(move.to()) && figure instanceof TaflKing ||
            // win if the enemy has no moves
            enemyHasNoMoves(getCurrentPlayer())
        )
            setStatus(Status.WIN);
    }

    private boolean enemyHasNoMoves (final Player player) {
        for (int y = 0; y < field.getHeight(); y ++)
            for (int x = 0; x < field.getWidth(); x ++) {
                final Vector position = new Vector(x, y);
                final Optional<AbstractFigure> cell = field.getCell(position);

                if (cell.isEmpty() || cell.get().getPlayer() == player)
                    continue;

                final AbstractFigure figure = cell.get();

                // check that the figure has at least one possible move
                for (final Vector direction : Vector.DIRECTIONS) {
                    final Vector candidate = position.add(direction);

                    if (
                        field.isCellValid(candidate) &&
                        field.getCell(candidate).isEmpty() &&
                        isCellAvailableForTheFigure(candidate, figure)
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
            warrior.getPlayer() != getCurrentPlayer() &&
            field.isCellValid(supporter) && (
                field.isCornerCell(supporter) ||
                field.getCell(supporter).isPresent() && field.getCell(supporter).get().getPlayer() != warrior.getPlayer()
            );
    }

    private boolean isKingCaptured (final Vector candidate) {
        if (
            !field.isCellValid(candidate) ||
            field.getCell(candidate).isEmpty() ||
            !(field.getCell(candidate).get() instanceof final TaflKing king) ||
            king.getPlayer() == getCurrentPlayer()
        )
            return false;

        for (final Vector direction : Vector.DIRECTIONS) {
            final Vector supporter = candidate.add(direction);

            if (
                field.isCellValid(supporter) &&
                !field.isCornerCell(supporter) &&
                !field.isCenterCell(supporter) &&
                (field.getCell(supporter).isEmpty() || field.getCell(supporter).get().getPlayer() == king.getPlayer())
            )
                return false;
        }

        return true;
    }

    private boolean isCellAvailableForTheFigure (final Vector cell, final AbstractFigure figure) {
        return
            figure instanceof TaflWarrior ?
            // throne and corner cells are inaccessible for warriors
            !field.isCenterCell(cell) && !field.isCornerCell(cell) :
            // throne is inaccessible even for the king if he is not allowed to return to it
            !field.isCenterCell(cell) || settings.isKingAllowedToReturnToTheThrone();
    }

    private void validateMove (final TaflMove move) throws InvalidMoveException {
        if (!field.isCellValid(move.from()))
            throw new InvalidMoveException("initial cell is invalid");

        if (field.getCell(move.from()).isEmpty())
            throw new InvalidMoveException("initial cell is empty");

        if (field.getCell(move.from()).get().getPlayer() != getCurrentPlayer())
            throw new InvalidMoveException("figure in the initial cell is not owned by the current user");

        if (!field.isCellValid(move.to()))
            throw new InvalidMoveException("target cell is invalid");

        if (field.getCell(move.to()).isPresent())
            throw new InvalidMoveException("target cell is occupied");

        final AbstractFigure figure = field.getCell(move.from()).get();

        if (move.from().x() != move.to().x() && move.from().y() != move.to().y())
            throw new InvalidMoveException("cells should be connected by a straight (vertical or horizontal) line");

        final int moveDistance = Vector.distance(move.from(), move.to());

        if (moveDistance == 0)
            throw new InvalidMoveException("cells should be different");

        if (settings.isFigureMoveDistanceRestricted() && moveDistance > 1)
            throw new InvalidMoveException("move distance should be equal to one, because move distance is restricted");

        final Vector direction = move.to().subtract(move.from()).divide(moveDistance);

        if (!isCellAvailableForTheFigure(move.to(), figure))
            throw new InvalidMoveException("target cell should be available to the figure");

        for (
            Vector current = move.from().add(direction);
            current.equals(move.to());
            current = current.add(direction)
        ) {
            if (field.getCell(current).isPresent())
                throw new InvalidMoveException(
                    "there should be no other figures on the pass from the initial cell to the target cell");

            if (!isCellAvailableForTheFigure(current, figure))
                throw new InvalidMoveException(
                    "All cells on the path from the initial cell to the final cell must be available to the current" +
                    "figure, but the cell " + current + " – isn't");
        }
    }

    private final TaflSettings settings;
}
