package tictactoe;

import core.AbstractField;
import core.Vector;

public class TicTacToeField extends AbstractField {
    public TicTacToeField (final int size) { super(size); }

    @Override
    public String getCellPresentation (final Vector cell) { return "▫"; }
}
