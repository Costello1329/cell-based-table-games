package tictactoe;

import core.AbstractFigure;
import core.Player;

public class TicTacToeFigure extends AbstractFigure {
    public TicTacToeFigure (final Player player) { super(player); }

    @Override
    public String getPresentation () { return getPlayer() == Player.WHITE ? "◆" : "◇"; }
}
