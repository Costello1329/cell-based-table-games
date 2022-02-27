package core;

public class StatusException extends Exception {
    public StatusException () { super("Operation is prohibited, the game is finished"); }
}
