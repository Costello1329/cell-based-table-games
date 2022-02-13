package tafl.engines.tablut;

import core.FieldFactory;
import core.Player;
import tafl.TaflField;
import core.Vector;
import tafl.TaflKing;
import tafl.TaflWarrior;

public class TablutFieldFactory implements FieldFactory<TaflField> {
    @Override
    public TaflField createField () {
        final TaflField field = new TaflField(9);
        field.placeFigure(field.center, new TaflKing());

        for (final Vector base : Vector.DIRECTIONS) {
            field.placeFigure(field.center.add(base), new TaflWarrior(Player.WHITE));
            field.placeFigure(field.center.add(base.multiply(2)), new TaflWarrior(Player.WHITE));
            field.placeFigure(field.center.add(base.multiply(4)), new TaflWarrior(Player.BLACK));

            for (final Vector shift : Vector.DIRECTIONS) {
                final Vector candidate = field.center.add(base.multiply(4)).add(shift);

                if (field.isCellValid(candidate))
                    field.placeFigure(candidate, new TaflWarrior(Player.BLACK));
            }
        }

        return field;
    }
}
