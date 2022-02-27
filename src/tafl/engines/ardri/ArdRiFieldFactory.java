package tafl.engines.ardri;

import core.*;
import tafl.TaflKing;
import tafl.TaflWarrior;
import tafl.TaflField;

public class ArdRiFieldFactory implements FieldFactory<TaflField> {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(7);

        field.placeFigure(field.getCenter(), new TaflKing());

        for (final Vector shift : Vector.DIRECTIONS_L_INF)
            field.placeFigure(field.getCenter().add(shift), new TaflWarrior(Player.WHITE));

        for (final Vector direction : Vector.DIRECTIONS_L_1) {
            final Vector base = field.getCenter().add(direction.multiply(3));
            field.placeFigure(base, new TaflWarrior(Player.BLACK));

            for (final Vector shift : Vector.DIRECTIONS_L_1) {
                final Vector candidate = base.add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.BLACK));
            }
        }

        return field;
    }
}
