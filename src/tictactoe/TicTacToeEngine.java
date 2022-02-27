package tictactoe;

import core.*;

public class TicTacToeEngine extends AbstractEngine<TicTacToeField, TicTacToeMove> {
    public TicTacToeEngine (final int size) { super(new TicTacToeFieldFactory(size), Player.WHITE); }

    @Override
    public void performMove (final TicTacToeMove move) throws InvalidMoveException {
        if (!field.isCellValid(move.getCell()))
            throw new InvalidMoveException("target cell should be valid");

        if (field.getCell(move.getCell()).isPresent())
            throw new InvalidMoveException("target cell should be empty");

        field.placeFigure(move.getCell(), new TicTacToeFigure(getCurrentPlayer()));

        if (checkWin(move.getCell()))
            setStatus(Status.WIN);

        else if (checkDraw())
            setStatus(Status.DRAW);
    }

    private boolean checkWin (final Vector cell) {
        for (int i = 0; i < Vector.DIRECTIONS_L_INF.length / 2; i ++) {
            final Vector direction = Vector.DIRECTIONS_L_INF[i];
            final Vector oppositeDirection =
                Vector.DIRECTIONS_L_INF[(i + Vector.DIRECTIONS_L_INF.length / 2) % Vector.DIRECTIONS_L_INF.length];

            Vector current = cell.copy();

            while (field.isCellValid(current.add(direction)))
                current = current.add(direction);

            if (checkLine(current, oppositeDirection))
                return true;
        }

        return false;
    }

    private boolean checkLine (final Vector start, final Vector direction) {
        int lineLength = 0;
        for (Vector current = start.copy(); field.isCellValid(current); current = current.add(direction)) {
            lineLength ++;

            if (field.getCell(current).isEmpty() || field.getCell(current).get().getPlayer() != getCurrentPlayer())
                return false;
        }

        return lineLength == field.getSize();
    }

    private boolean checkDraw () {
        for (int i = 0; i < field.getSize() * field.getSize(); i ++)
            if (field.getCell(new Vector(i % field.getSize(), i / field.getSize())).isEmpty())
                return false;

        return true;
    }
}
