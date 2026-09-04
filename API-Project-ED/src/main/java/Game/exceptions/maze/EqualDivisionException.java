package Game.exceptions.maze;


/**
 * EqualDivisionException is an exception thrown when an operation
 * requires two different divisions but receives the same division
 * for both parameters.
 */
public class EqualDivisionException extends Exception {

  /**
   * Default message used when the two divisions are equal.
   */
  private static final String DEFAULT_MESSAGE = "The divisions cannot be equal!";

  /**
   * Creates an {@code EqualDivisionException} with the default message.
   */
  public EqualDivisionException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates an {@code EqualDivisionException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public EqualDivisionException(String message) { super(message); }
}
