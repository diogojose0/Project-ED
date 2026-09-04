package Game.exceptions.replays;


/**
 * NullReplayEventException is an exception thrown when a replay event
 * is null and a valid event instance is required.
 */
public class NullReplayEventException extends Exception {

  /**
   * Default message used when the replay event is null.
   */
  private static final String DEFAULT_MESSAGE = "The replay event is null";

  /**
   * Creates a {@code NullReplayEventException} with the default message.
   */
  public NullReplayEventException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NullReplayEventException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NullReplayEventException(String message) {super(message);}

}
