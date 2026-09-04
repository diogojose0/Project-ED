package Game.exceptions.division;


/**
 * LeverNotFoundException is an exception thrown when a requested lever
 * cannot be found in the current lever game or division.
 */
public class LeverNotFoundException extends Exception {

    /**
     * Default message used when a lever cannot be found.
     */
    private static final String DEFAULT_MESSAGE = "Lever not found! Try again.";

    /**
     * Creates a {@code LeverNotFoundException} with the default message.
     */
    public LeverNotFoundException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code LeverNotFoundException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public LeverNotFoundException(String message) {
        super(message);
    }
}
