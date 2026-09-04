package Game.exceptions.maze;


/**
 * DivisionAlreadyExistsException is an exception thrown when an operation
 * attempts to add a division that already exists in the maze.
 */
public class DivisionAlreadyExistsException extends Exception {

    /**
     * Default message used when a division (vertex) already exists.
     */
    private static final String DEFAULT_MESSAGE = "Division already exists!";

    /**
     * Creates a {@code DivisionAlreadyExistsException} with the default message.
     */
    public DivisionAlreadyExistsException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code DivisionAlreadyExistsException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public DivisionAlreadyExistsException(String message) {
        super(message);
    }
}
