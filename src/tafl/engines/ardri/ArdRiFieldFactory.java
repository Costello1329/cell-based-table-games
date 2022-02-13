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

        for (final Vector base : Vector.DIRECTIONS) {
            field.placeFigure(field.center.add(base.multiply(1)), new TaflWarrior(Player.WHITE));
            field.placeFigure(field.center.add(base.multiply(3)), new TaflWarrior(Player.BLACK));

            for (final Vector shift : Vector.DIRECTIONS) {
                final Vector candidate = field.center.add(base.multiply(3)).add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.BLACK));
            }
        }

        for (int i = 0; i < Vector.DIRECTIONS.length; i ++)
            field.placeFigure(
                field.center.add(Vector.DIRECTIONS[i]).add(Vector.DIRECTIONS[(i + 1) % Vector.DIRECTIONS.length]),
                new TaflWarrior(Player.WHITE)
            );

        return field;
    }
}
