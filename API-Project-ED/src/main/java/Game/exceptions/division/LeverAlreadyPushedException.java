package Game.exceptions.division;


/**
 * LeverAlreadyPushedException is an exception thrown when an operation
 * attempts to push a lever that has already been pushed.
 */
public class LeverAlreadyPushedException extends Exception {

  /**
   * Default message used when a lever has already been pushed.
   */
  private static final String DEFAULT_MESSAGE = "Lever already pushed! Choose another one!";

  /**
   * Creates a {@code LeverAlreadyPushedException} with the default message.
   */
  public LeverAlreadyPushedException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code LeverAlreadyPushedException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public LeverAlreadyPushedException(String message) { super(message); }
}
