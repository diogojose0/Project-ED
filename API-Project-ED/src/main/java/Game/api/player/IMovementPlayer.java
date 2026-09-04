package Game.api.player;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.division.IDivision;


/**
 * Contract for tracking a player's movement within the maze.
 * <p>
 * The movement component stores the current division, movement history
 * and supports operations like moving back along the visited path.
 * </p>
 */
public interface IMovementPlayer {

    /**
     * Sets the current division of the player.
     *
     * @param division the division where the player is now located
     */
    void setCurrentDivision(IDivision division);

    /**
     * Returns the current division of the player.
     *
     * @return the current division
     */
    IDivision getDivision();

    /**
     * Clears the entire movement history.
     */
    void clearHistory();

    /**
     * Returns the list of movements performed by the player.
     *
     * @return a {@link DoublyLinkedUnorderedList} with the visited divisions
     */
    DoublyLinkedUnorderedList<IDivision> getMovements();

    /**
     * Moves the player backwards along the movement history.
     *
     * @param amount the number of steps to move back
     */
    void moveBack(int amount);
}

