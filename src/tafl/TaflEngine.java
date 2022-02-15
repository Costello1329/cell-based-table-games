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
        final AbstractFigure figure = validateMoveAndGetFigure(move);
        field.placeFigure(move.getTo(), figure);
        field.removeFigure(move.getFrom());

        // check captures
        for (final Vector direction : Vector.DIRECTIONS_L_1) {
            final Vector candidate = move.getTo().add(direction);
            final Vector helper = move.getTo().add(direction.multiply(2));

            if (isWarriorCaptured(candidate, helper))
                field.removeFigure(candidate);

            if (isKingCaptured(candidate)) {
                setStatus(Status.WIN);
                return;
            }
        }

        if (
            // win by moving the king to the corner
            field.isCornerCell(move.getTo()) && figure instanceof TaflKing ||
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
                for (final Vector direction : Vector.DIRECTIONS_L_1) {
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

    private boolean isWarriorCaptured (final Vector candidate, final Vector helper) {
        return
            field.isCellValid(candidate) &&
            field.getCell(candidate).isPresent() &&
            field.getCell(candidate).get() instanceof final TaflWarrior warrior &&
            warrior.getPlayer() != getCurrentPlayer() &&
            field.isCellValid(helper) && (
                field.isCornerCell(helper) ||
                field.getCell(helper).isPresent() && field.getCell(helper).get().getPlayer() != warrior.getPlayer()
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

        for (final Vector direction : Vector.DIRECTIONS_L_1) {
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

    private AbstractFigure validateMoveAndGetFigure (final TaflMove move) throws InvalidMoveException {
        if (!field.isCellValid(move.getFrom()))
            throw new InvalidMoveException("initial cell is invalid");

        if (field.getCell(move.getFrom()).isEmpty())
            throw new InvalidMoveException("initial cell is empty");

        if (field.getCell(move.getFrom()).get().getPlayer() != getCurrentPlayer())
            throw new InvalidMoveException("figure in the initial cell is not owned by the current user");

        if (!field.isCellValid(move.getTo()))
            throw new InvalidMoveException("target cell is invalid");

        if (field.getCell(move.getTo()).isPresent())
            throw new InvalidMoveException("target cell is occupied");

        final AbstractFigure figure = field.getCell(move.getFrom()).get();

        if (move.getFrom().x() != move.getTo().x() && move.getFrom().y() != move.getTo().y())
            throw new InvalidMoveException("cells should be connected by a straight (vertical or horizontal) line");

        final int moveDistance = Vector.distanceL1(move.getFrom(), move.getTo());

        if (moveDistance == 0)
            throw new InvalidMoveException("cells should be different");

        if (settings.isFigureMoveDistanceRestricted() && moveDistance > 1)
            throw new InvalidMoveException("move distance should be equal to one, because move distance is restricted");

        final Vector direction = move.getTo().subtract(move.getFrom()).divide(moveDistance);

        if (!isCellAvailableForTheFigure(move.getTo(), figure))
            throw new InvalidMoveException("target cell should be available to the figure");

        for (
            Vector current = move.getFrom().add(direction);
            current.equals(move.getTo());
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

        return field.getCell(move.getFrom()).get();
    }

    private final TaflSettings settings;
}
