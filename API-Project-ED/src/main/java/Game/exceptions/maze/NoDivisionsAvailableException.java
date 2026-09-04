package Game.exceptions.maze;


/**
 * NoDivisionsAvailableException is an exception thrown when didn't exist divisions
 * available to choose.
 */
public class NoDivisionsAvailableException extends Exception {

    /**
     * Default message used when the two divisions are equal.
     */
    private static final String DEFAULT_MESSAGE = "Didn't find any divisions!";

    /**
     * Creates an {@code NoDivisionsAvailableException} with the default message.
     */
    public NoDivisionsAvailableException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates an {@code NoDivisionsAvailableException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public NoDivisionsAvailableException(String message) { super(message); }
}
