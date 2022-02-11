package tafl;

import core.AbstractFigure;
import core.Player;

public class TaflKing extends AbstractFigure {
    public TaflKing () { super(Player.White); }
    public String getPresentation() { return "◎"; }
}
