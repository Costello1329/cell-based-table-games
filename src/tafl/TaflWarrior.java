package tafl;

import core.AbstractFigure;
import core.Player;

public class TaflWarrior extends AbstractFigure {
    public TaflWarrior(final Player player) { super(player); }
    public String getPresentation() { return player == Player.White ? "◆" : "◇"; }
}
