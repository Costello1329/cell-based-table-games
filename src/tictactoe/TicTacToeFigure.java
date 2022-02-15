package tictactoe;

import core.AbstractFigure;
import core.Player;

public class TicTacToeFigure extends AbstractFigure {
    protected TicTacToeFigure (final Player player) { super(player); }

    @Override
    public String getPresentation () { return player == Player.WHITE ? "◆" : "◇"; }
}
