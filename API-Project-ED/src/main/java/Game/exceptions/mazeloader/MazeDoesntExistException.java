package Game.exceptions.mazeloader;


/**
 * MazeDoesntExistException is an exception thrown when an operation
 * requests a maze that is not registered in the loader.
 */
public class MazeDoesntExistException extends Exception {

  /**
   * Default message used when the maze chosen does not exist.
   */
  private static final String DEFAULT_MESSAGE = "The maze chosen doesn't exist!";

  /**
   * Creates a {@code MazeDoesntExistException} with the default message.
   */
  public MazeDoesntExistException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code MazeDoesntExistException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public MazeDoesntExistException(String message) { super(message); }
}
