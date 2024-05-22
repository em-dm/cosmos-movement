package emdm.otus.cosmos.movement.exceptions;

public class CommandException extends RuntimeException {
    public CommandException() {
        super();
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Exception exception) {
        super(exception);
    }
}
