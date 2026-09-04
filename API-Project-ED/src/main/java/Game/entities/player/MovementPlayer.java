package Game.entities.player;

import Collections.exceptions.EmptyCollectionException;
import Collections.list.DoublyLinkedUnorderedList;
import Collections.stack.LinkedStack;
import Game.api.division.IDivision;
import Game.api.player.IMovementPlayer;


/**
 * Implementation of {@link IMovementPlayer} that tracks a player's position,
 * movement history and path through the maze.
 */
public class MovementPlayer implements IMovementPlayer {

    /** Current division (room) where the player is located. */
    private IDivision currentDivision;

    /** Ordered list of all divisions the player has passed through. */
    private DoublyLinkedUnorderedList<IDivision> movements;

    /** Stack that stores the movement history, used for moving back through previous divisions. */
    private LinkedStack<IDivision> history;


    /**
     * Creates a new {@code MovementPlayer} with empty movement history.
     */
    public MovementPlayer() {
        movements = new DoublyLinkedUnorderedList<>();
        history = new LinkedStack<>();
    }

    /**
     * Updates the current division of the player and records the movement
     * in both history and movements list.
     *
     * @param division the new current division
     */
    @Override
    public void setCurrentDivision(IDivision division) {
        this.currentDivision = division;
        this.history.push(currentDivision);
        this.movements.addToRear(currentDivision);
    }

    /**
     * Returns the current division of the player.
     *
     * @return the current division
     */
    @Override
    public IDivision getDivision() {
        return this.currentDivision;
    }

    /**
     * Clears the movement history stack and is reinitialized with the current division.
     */
    @Override
    public void clearHistory(){
        this.history = new LinkedStack<>();
        this.history.push(this.currentDivision);
    }

    /**
     * Returns the list of all movements performed by the player.
     *
     * @return list of divisions representing the path
     */
    @Override
    public DoublyLinkedUnorderedList<IDivision> getMovements() {
        return this.movements;
    }

    /**
     * Moves the player back along the history a given number of steps.
     * The current division is updated accordingly and each popped division
     * is appended to the movements list.
     *
     * @param amount the number of steps to move back
     */
    public void moveBack (int amount) {
        do {
            try {
                IDivision division = this.history.pop();
                this.movements.addToRear(division);
                this.currentDivision = history.peek();
            } catch (EmptyCollectionException e) {
            }

        } while (history.size() > 1 && amount > 0);
    }

}
