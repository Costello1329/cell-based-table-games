package tafl;

import core.AbstractFigure;
import core.Player;

public class TaflWarrior extends AbstractFigure {
    public TaflWarrior (final Player player) { super(player); }

    @Override
    public String getPresentation () { return player == Player.WHITE ? "◆" : "◇"; }
}
