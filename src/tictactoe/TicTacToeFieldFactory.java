package tictactoe;

import core.FieldFactory;

public record TicTacToeFieldFactory (int size) implements FieldFactory<TicTacToeField> {
    @Override
    public TicTacToeField createField () { return new TicTacToeField(size); }
}
