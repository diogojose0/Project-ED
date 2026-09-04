package Game.exceptions.mazeloader;


/**
 * MazeAlreadyExistsException is an exception thrown when an operation
 * attempts to add a maze that is already registered in the loader.
 */
public class MazeAlreadyExistsException extends Exception {

  /**
   * Default message used when the maze already exists.
   */
  private static final String DEFAULT_MESSAGE = "The maze already exists!";

  /**
   * Creates a {@code MazeAlreadyExistsException} with the default message.
   */
  public MazeAlreadyExistsException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code MazeAlreadyExistsException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public MazeAlreadyExistsException(String message) { super(message); }
}
