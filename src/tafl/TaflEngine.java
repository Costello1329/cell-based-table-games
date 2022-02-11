package tafl;

import core.*;

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

        field.placeFigure(move.to(), field.getCell(move.from()).get());
        field.removeFigure(move.from());

        /// TODO: support capture, win and draw here...

        switchPlayer();
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
            (move.from().x() == move.to().x() ^ move.from().y() == move.to().y()) ||
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
