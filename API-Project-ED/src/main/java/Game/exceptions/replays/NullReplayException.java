package Game.exceptions.replays;


/**
 * NullReplayException is an exception thrown when a replay reference
 * is null and a valid replay instance is required.
 */
public class NullReplayException extends Exception {

  /**
   * Default message used when the replay is null.
   */
  private static final String DEFAULT_MESSAGE = "The replay is null";

  /**
   * Creates a {@code NullReplayException} with the default message.
   */
  public NullReplayException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NullReplayException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NullReplayException(String message) { super(message); }
}
