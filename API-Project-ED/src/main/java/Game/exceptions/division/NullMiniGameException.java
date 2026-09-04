package Game.exceptions.division;


/**
 * NullMiniGameException is an exception thrown when a mini-game
 * reference is null and an operation requires a valid mini-game.
 */
public class NullMiniGameException extends Exception {

  /**
   * Default message used when a mini-game is null.
   */
  private static final String DEFAULT_MESSAGE = "Mini game is null!";

  /**
   * Creates a {@code NullMiniGameException} with the default message.
   */
  public NullMiniGameException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NullMiniGameException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NullMiniGameException(String message) { super(message); }
}
