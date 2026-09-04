package Game.exceptions.gameEngine;


/**
 * GameStartedException is an exception thrown when an operation
 * is attempted after the game has already started.
 */
public class GameStartedException extends Exception {

  /**
   * Default message used when the game has already started.
   */
  private static final String DEFAULT_MESSAGE = "The game has already started!";

  /**
   * Creates a {@code GameStartedException} with the default message.
   */
  public GameStartedException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code GameStartedException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public GameStartedException(String message) { super(message); }
}
