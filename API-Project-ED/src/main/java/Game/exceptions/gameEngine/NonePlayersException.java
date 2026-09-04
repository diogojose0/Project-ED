package Game.exceptions.gameEngine;


/**
 * NonePlayersException is an exception thrown when an operation
 * requires at least one player but the game has no players configured.
 */
public class NonePlayersException extends Exception {

  /**
   * Default message used when the game has no players.
   */
  private static final String DEFAULT_MESSAGE = "The game has no players!";

  /**
   * Creates a {@code NonePlayersException} with the default message.
   */
  public NonePlayersException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NonePlayersException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NonePlayersException(String message) { super(message); }
}
