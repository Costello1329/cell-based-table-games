package core;

public abstract class AbstractFigure {
    protected AbstractFigure (Player player) { this.player = player; }

    public abstract String getPresentation();

    public final Player player;
}
