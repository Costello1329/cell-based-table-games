package tafl.engines.brandubh;

import core.*;
import tafl.TaflKing;
import tafl.TaflWarrior;

public class BrandubhFieldFactory implements FieldFactory {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(7);
        field.placeFigure(field.center, new TaflKing());

        for (final Vector vector : Vector.directions) {
            field.placeFigure(field.center.add(vector), new TaflWarrior(Player.White));
            field.placeFigure(field.center.add(vector.multiply(2)), new TaflWarrior(Player.Black));
            field.placeFigure(field.center.add(vector.multiply(3)), new TaflWarrior(Player.Black));
        }

        return field;
    }
}
