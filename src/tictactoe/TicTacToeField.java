package tictactoe;

import core.AbstractField;
import core.AbstractFigure;
import core.Vector;

import java.util.Optional;

public class TicTacToeField extends AbstractField {
    TicTacToeField (final int size) { super(size, size); }

    @Override
    public String getPresentation () {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = height - 1; y >= 0; y --)
            for (int x = 0; x < width; x ++) {
                final Vector position = new Vector(x, y);
                final Optional<AbstractFigure> cell = getCell(position);
                stringBuilder.append(cell.isPresent() ? cell.get().getPresentation() : "▫");
                stringBuilder.append(x == width - 1 ? "\n" : " ");
            }

        return stringBuilder.toString();
    }
}
