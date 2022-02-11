package core;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractField {
    public AbstractField (final int height, final int width) {
        this.width = width;
        this.height = height;
        field = new ArrayList<>();

        for (int i = 0; i < width * height; i ++)
            field.add(Optional.empty());
    }

    public Optional<AbstractFigure> getCell (final Vector position) {
        return field.get(position.y() * height + position.x());
    }

    public void placeFigure (final Vector position, final AbstractFigure figure) {
        field.set(position.y() * height + position.x(), Optional.of(figure));
    }

    public void removeFigure (final Vector position) {
        field.set(position.y() * height + position.x(), Optional.empty());
    }

    public abstract String getPresentation ();

    public boolean isCellValid (final Vector position) {
        return position.y() >= 0 && position.y() < height && position.x() >= 0 && position.x() < width;
    }

    public final int width;
    public final int height;
    public final ArrayList<Optional<AbstractFigure>> field;
}
