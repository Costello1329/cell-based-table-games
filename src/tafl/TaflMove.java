package tafl;

import core.AbstractMove;
import core.Vector;

public class TaflMove extends AbstractMove {
    public TaflMove (final Vector from, final Vector to) {
        this.from = from;
        this.to = to;
    }

    public Vector getFrom () { return from; }
    public Vector getTo () { return to; }

    private final Vector from;
    private final Vector to;
}
