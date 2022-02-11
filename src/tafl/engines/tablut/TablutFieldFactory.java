package tafl.engines.tablut;

import core.FieldFactory;
import core.Player;
import core.TaflField;
import core.Vector;
import tafl.TaflKing;
import tafl.TaflWarrior;

public class TablutFieldFactory implements FieldFactory {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(9);
        field.placeFigure(field.center, new TaflKing());

        for (final Vector base : Vector.directions) {
            field.placeFigure(field.center.add(base), new TaflWarrior(Player.White));
            field.placeFigure(field.center.add(base.multiply(2)), new TaflWarrior(Player.White));
            field.placeFigure(field.center.add(base.multiply(4)), new TaflWarrior(Player.Black));

            for (final Vector shift : Vector.directions) {
                final Vector candidate = field.center.add(base.multiply(4)).add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.Black));
            }
        }

        return field;
    }
}
