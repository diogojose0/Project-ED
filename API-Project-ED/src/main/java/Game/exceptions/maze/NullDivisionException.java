package Game.exceptions.maze;


/**
 * NullDivisionException is an exception thrown when a division is null.
 */
public class NullDivisionException extends Exception {
    /**
     * Default message used when a division is null.
     */
    private static final String DEFAULT_MESSAGE = "The division is null!";

    /**
     * Creates a {@code NullDivisionException} with no message.
     */
    public NullDivisionException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Creates an {@code NullDivisionException} with the specified message.
     *
     * @param message the message to be displayed with the exception.
     */
    public NullDivisionException(String message) {
        super(message);
    }
}
