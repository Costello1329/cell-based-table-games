package core;

public abstract class AbstractFigure {
    public AbstractFigure (final Player player) { this.player = player; }
    public abstract String getPresentation ();
    public Player getPlayer () { return player; }
    private final Player player;
}
