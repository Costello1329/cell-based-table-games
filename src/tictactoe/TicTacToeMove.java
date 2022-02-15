package tictactoe;

import core.AbstractMove;
import core.Vector;

public class TicTacToeMove extends AbstractMove {
    public TicTacToeMove (final Vector cell) { this.cell = cell; }
    public Vector getCell () { return cell; }
    private final Vector cell;
}
