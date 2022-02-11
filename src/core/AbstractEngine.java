package core;

public abstract class AbstractEngine {
    protected AbstractEngine (final FieldFactory fieldFactory, final Player initialPlayer) {
        field = fieldFactory.createField();
        currentPlayer = initialPlayer;
        status = Status.Active;
    }

    public abstract boolean makeMove (final Move move);
    public String getPresentation () { return field.getPresentation(); }
    public Player getCurrentPlayer () { return currentPlayer; }
    public Status getStatus () { return status; }

    protected void switchPlayer () { currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White; }

    protected final AbstractField field;
    protected Player currentPlayer;
    protected Status status;
}
