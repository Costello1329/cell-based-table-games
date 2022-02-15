package tafl.engines.ardri;

import core.*;
import tafl.TaflKing;
import tafl.TaflWarrior;
import tafl.TaflField;

public class ArdRiFieldFactory implements FieldFactory<TaflField> {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(7);
        field.placeFigure(field.center, new TaflKing());

        for (final Vector base : Vector.DIRECTIONS_L_1) {
            field.placeFigure(field.center.add(base.multiply(1)), new TaflWarrior(Player.WHITE));
            field.placeFigure(field.center.add(base.multiply(3)), new TaflWarrior(Player.BLACK));

            for (final Vector shift : Vector.DIRECTIONS_L_1) {
                final Vector candidate = field.center.add(base.multiply(3)).add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.BLACK));
            }
        }

        for (final Vector shift : Vector.DIRECTIONS_L_INF)
            field.placeFigure(field.center.add(shift), new TaflWarrior(Player.WHITE));

        return field;
    }
}
