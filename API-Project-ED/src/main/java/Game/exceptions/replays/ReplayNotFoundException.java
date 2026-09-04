package Game.exceptions.replays;


/**
 * ReplayNotFoundException is an exception thrown when a requested replay
 * cannot be found in the replays management component.
 */
public class ReplayNotFoundException extends Exception {

    /**
     * Default message used when the replay cannot be found.
     */
    private static final String DEFAULT_MESSAGE = "The replay could not be found!";

    /**
     * Creates a {@code ReplayNotFoundException} with the default message.
     */
    public ReplayNotFoundException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code ReplayNotFoundException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public ReplayNotFoundException(String message) {
        super(message);
    }
}
