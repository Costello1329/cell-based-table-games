package core;

import java.util.ArrayList;
import java.util.Optional;

public abstract class AbstractField {
    protected AbstractField (final int height, final int width) {
        this.width = width;
        this.height = height;
        field = new ArrayList<>();

        for (int i = 0; i < width * height; i ++)
            field.add(Optional.empty());
    }

    public abstract String getPresentation ();

    public Optional<AbstractFigure> getCell (final Vector position) {
        return field.get(position.y() * width + position.x());
    }

    public void placeFigure (final Vector position, final AbstractFigure figure) {
        field.set(position.y() * width + position.x(), Optional.of(figure));
    }

    public void removeFigure (final Vector position) {
        field.set(position.y() * width + position.x(), Optional.empty());
    }

    public boolean isCellValid (final Vector position) {
        return position.y() >= 0 && position.y() < height && position.x() >= 0 && position.x() < width;
    }

    public int getWidth () { return width; }
    public int getHeight () { return height; }

    protected final int width;
    protected final int height;
    protected final ArrayList<Optional<AbstractFigure>> field;
}
