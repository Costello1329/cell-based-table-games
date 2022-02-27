package tafl;

import core.AbstractField;
import core.Vector;

public class TaflField extends AbstractField {
    public TaflField (final int size) {
        super(size);
        this.center = new Vector(size / 2, size / 2);
    }

    @Override
    public String getCellPresentation (final Vector cell) {
        return isCornerCell(cell) || isCenterCell(cell) ? "▪" : "▫";
    }

    public boolean isCenterCell (final Vector cell) { return Vector.distanceL1(cell, center) == 0; }
    public boolean isCornerCell (final Vector cell) { return Vector.distanceL1(cell, center) == getSize() - 1; }

    public Vector getCenter () { return center; }

    private final Vector center;
}
