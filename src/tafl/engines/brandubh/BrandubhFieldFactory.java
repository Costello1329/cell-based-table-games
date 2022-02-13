package tafl.engines.brandubh;

import core.*;
import tafl.TaflKing;
import tafl.TaflWarrior;
import tafl.TaflField;

public class BrandubhFieldFactory implements FieldFactory<TaflField> {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(7);
        field.placeFigure(field.center, new TaflKing());

        for (final Vector vector : Vector.DIRECTIONS) {
            field.placeFigure(field.center.add(vector), new TaflWarrior(Player.WHITE));
            field.placeFigure(field.center.add(vector.multiply(2)), new TaflWarrior(Player.BLACK));
            field.placeFigure(field.center.add(vector.multiply(3)), new TaflWarrior(Player.BLACK));
        }

        return field;
    }
}
