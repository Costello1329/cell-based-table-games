package core;

public abstract class AbstractEngine<Field extends AbstractField> {
    protected AbstractEngine (final FieldFactory<Field> fieldFactory, final Player initialPlayer) {
        field = fieldFactory.createField();
        currentPlayer = initialPlayer;
        status = Status.Active;
    }

    public abstract boolean makeMove (final Move move);
    public String getPresentation () { return field.getPresentation(); }
    public Player getCurrentPlayer () { return currentPlayer; }
    public Status getStatus () { return status; }

    protected void switchPlayer () { currentPlayer = currentPlayer == Player.White ? Player.Black : Player.White; }

    protected final Field field;
    protected Player currentPlayer;
    protected Status status;
}
