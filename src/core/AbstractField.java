package core;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractField {
    public AbstractField (final int size) {
        this.size = size;
        field = new ArrayList<>();

        for (int i = 0; i < size * size; i ++)
            field.add(Optional.empty());
    }

    public String getPresentation () {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = size - 1; y >= 0; y --)
            for (int x = 0; x < size; x ++) {
                final Vector position = new Vector(x, y);
                final Optional<AbstractFigure> cell = getCell(position);
                stringBuilder.append(
                    cell.isPresent() ?
                    cell.get().getPresentation() :
                    getCellPresentation(position)
                );
                stringBuilder.append(x == size - 1 ? "\n" : " ");
            }

        return stringBuilder.toString();
    }

    public Optional<AbstractFigure> getCell (final Vector cell) { return field.get(cell.y() * size + cell.x()); }

    public void placeFigure (final Vector cell, final AbstractFigure figure) {
        field.set(cell.y() * size + cell.x(), Optional.of(figure));
    }

    public void removeFigure (final Vector cell) { field.set(cell.y() * size + cell.x(), Optional.empty()); }

    public boolean isCellValid (final Vector cell) {
        return cell.y() >= 0 && cell.y() < size && cell.x() >= 0 && cell.x() < size;
    }

    public int getSize () { return size; }

    protected abstract String getCellPresentation (final Vector position);

    private final int size;
    private final ArrayList<Optional<AbstractFigure>> field;
}
