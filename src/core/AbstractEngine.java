package core;

public abstract class AbstractEngine<Field extends AbstractField, Move> {
    protected AbstractEngine (final FieldFactory<Field> fieldFactory, final Player initialPlayer) {
        field = fieldFactory.createField();
        currentPlayer = initialPlayer;
        status = Status.ACTIVE;
    }

    public void makeMove (final Move move) throws InvalidMoveException {
        if (status != Status.ACTIVE)
            return;

        performMove(move);
        currentPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    public String getPresentation () { return field.getPresentation(); }
    public Player getCurrentPlayer () { return currentPlayer; }
    public Status getStatus () { return status; }
    protected void setStatus (final Status status) { this.status = status; }

    protected abstract void performMove (final Move move) throws InvalidMoveException;

    protected final Field field;
    private Player currentPlayer;
    private Status status;
}
