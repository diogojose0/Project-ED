package Game.exceptions.maze;


/**
 * DivisionNotFoundException is an exception thrown when a requested
 * division cannot be found in the maze.
 */
public class DivisionNotFoundException extends Exception {

  /**
   * Default message used when a division is not found.
   */
  private static final String DEFAULT_MESSAGE = "Division not found!";

  /**
   * Creates a {@code DivisionNotFoundException} with the default message.
   */
  public DivisionNotFoundException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code DivisionNotFoundException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public DivisionNotFoundException(String message) { super(message); }
}
