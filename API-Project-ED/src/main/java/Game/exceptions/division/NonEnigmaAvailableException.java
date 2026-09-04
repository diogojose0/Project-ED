package Game.exceptions.division;


/**
 * NonEnigmaAvailableException is an exception thrown when no enigma
 * is available to be retrieved by the enigma strategy.
 */
public class NonEnigmaAvailableException extends Exception {

  /**
   * Default message used when there is no enigma available.
   */
  private static final String DEFAULT_MESSAGE = "Non Enigma Available!";

  /**
   * Creates a {@code NonEnigmaAvailableException} with the default message.
   */
  public NonEnigmaAvailableException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NonEnigmaAvailableException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NonEnigmaAvailableException(String message) { super(message); }
}
