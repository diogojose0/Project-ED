package Game.exceptions.maze;


/**
 * AlreadyHaveTreasureRoomException is an exception thrown when
 * an operation tries to add a new treasure room to a maze that
 * already has one defined.
 */
public class AlreadyHaveTreasureRoomException extends Exception {

  /**
   * Default message used when the maze already has a treasure room.
   */
  private static final String DEFAULT_MESSAGE = "Already Have Treasure Room in the maze!";

  /**
   * Creates an {@code AlreadyHaveTreasureRoomException} with the default message.
   */
  public AlreadyHaveTreasureRoomException() { super(DEFAULT_MESSAGE); }

  /**
   * Creates an {@code AlreadyHaveTreasureRoomException} with the specified message.
   *
   * @param message the message to be displayed with the exception
   */
  public AlreadyHaveTreasureRoomException(String message) { super(message); }
}
