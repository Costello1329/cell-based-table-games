package core;

import java.util.Optional;

public class TaflField extends AbstractField {
    public TaflField (final int size) { super(size, size); }

    @Override
    public String getPresentation () {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = height - 1; y >= 0; y --)
            for (int x = 0; x < width; x ++) {
                final Vector position = new Vector(x, y);
                final Optional<AbstractFigure> cell = getCell(position);
                stringBuilder.append(
                    cell.isPresent() ?
                    cell.get().getPresentation() :
                    (isCornerCell(position) || isCenterCell(position) ? "▪" : "▫")
                );
                stringBuilder.append(x == width - 1 ? "\n" : " ");
            }

        return stringBuilder.toString();
    }

    public boolean isCornerCell (final Vector position) { return Vector.distance(position, center) == width + height; }
    public boolean isCenterCell (final Vector position) { return Vector.distance(position, center) == 0; }

    public final Vector center = new Vector(height / 2, width / 2);
}
