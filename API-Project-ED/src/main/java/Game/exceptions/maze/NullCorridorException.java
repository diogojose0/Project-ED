package Game.exceptions.maze;


/**
 * NullCorridorException is an exception thrown when a corridor
 * reference is null and a valid corridor is required.
 */
public class NullCorridorException extends Exception {

    /**
     * Default message used when a corridor is null.
     */
    private static final String DEFAULT_MESSAGE = "The corridor is null!";

    /**
     * Creates a {@code NullCorridorException} with the default message.
     */
    public NullCorridorException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code NullCorridorException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public NullCorridorException(String message) {
        super(message);
    }
}
