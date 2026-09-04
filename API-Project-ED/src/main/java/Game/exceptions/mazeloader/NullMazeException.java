package Game.exceptions.mazeloader;


/**
 * NullMazeException is an exception thrown when a maze reference is null
 * and a valid maze instance is required.
 */
public class NullMazeException extends Exception {

    /**
     * Default message used when the maze is null.
     */
    private static final String DEFAULT_MESSAGE = "The maze is null!";

    /**
     * Creates a {@code NullMazeException} with the default message.
     */
    public NullMazeException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code NullMazeException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public NullMazeException(String message) {
        super(message);
    }
}
