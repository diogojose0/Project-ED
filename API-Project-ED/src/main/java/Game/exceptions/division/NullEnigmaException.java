package Game.exceptions.division;


/**
 * NullEnigmaException is an exception thrown when an enigma reference is null.
 */
public class NullEnigmaException extends Exception {

  /**
   * Default message used when an enigma is null.
   */
  private static final String DEFAULT_MESSAGE = "The enigma is null!";

  /**
   * Creates a {@code NullEnigmaException} with the default message.
   */
  public NullEnigmaException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates a {@code NullEnigmaException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public NullEnigmaException(String message) { super(message); }
}
