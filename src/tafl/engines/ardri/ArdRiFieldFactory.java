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

        for (final Vector base : Vector.directions) {
            field.placeFigure(field.center.add(base.multiply(1)), new TaflWarrior(Player.White));
            field.placeFigure(field.center.add(base.multiply(3)), new TaflWarrior(Player.Black));

            for (final Vector shift : Vector.directions) {
                final Vector candidate = field.center.add(base.multiply(3)).add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.Black));
            }
        }

        for (int i = 0; i < Vector.directions.length; i ++)
            field.placeFigure(
                field.center.add(Vector.directions[i]).add(Vector.directions[(i + 1) % Vector.directions.length]),
                new TaflWarrior(Player.White)
            );

        return field;
    }
}
