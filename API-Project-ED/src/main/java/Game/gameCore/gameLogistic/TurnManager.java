package Game.gameCore.gameLogistic;

import Collections.exceptions.EmptyCollectionException;
import Collections.queue.CircularArrayQueue;
import Collections.queue.QueueADT;
import Collections.list.ListADT;
import Game.api.player.IPlayerState;
import Game.api.engine.ITurnManager;


/**
 * {@code TurnManager} is the concrete implementation of {@link ITurnManager}
 * responsible for managing the order of player turns in the game.
 * <p>
 * It keeps an internal queue of {@link IPlayerState} objects and applies
 * turn rules such as blocked turns and extra turns when deciding which
 * player should act next.
 * </p>
 */
public class TurnManager implements ITurnManager {

    /** Queue that stores the turn order of players. */
    private QueueADT<IPlayerState> turnQueue;

    /**
     * Creates a new {@code TurnManager} with an empty turn queue.
     */
    public TurnManager(){
        this.turnQueue = new CircularArrayQueue<>();
    }

    /**
     * Loads the given player states into the internal turn queue.
     * Players are enqueued in the order provided by the {@link ListADT}.
     *
     * @param states list of player states to be added to the turn queue
     */
    @Override
    public void loadPlayers(ListADT<IPlayerState> states){
        for (IPlayerState player : states) {
            turnQueue.enqueue(player);
        }
    }

    /**
     * Resets the turn manager, clearing the current turn queue.
     * After calling this method, the turn manager has no players
     * until {@link #loadPlayers(ListADT)} is invoked again.
     */
    @Override
    public void reset (){
        turnQueue = new CircularArrayQueue<>();
    }

    /**
     * Returns the player whose turn it is, applying blocked and extra turn rules.
     * <p>
     * If the queue is empty, this method returns {@code null}.
     * If the current player (at the front of the queue) is blocked, one blocked
     * turn is consumed using {@link IPlayerState#tickBlock()}, the player is moved
     * to the end of the queue and the next player at the front is returned.
     * If the current player has extra turns, one extra turn is consumed using
     * {@link IPlayerState#useExtraTurn()} and the same player returned without
     * rotating the queue. Otherwise, the current player moved to the end of
     * the queue and that player returned as the one who just played.
     * </p>
     *
     * @return the {@link IPlayerState} whose turn it is, or {@code null} if no players are available
     */
    @Override
    public IPlayerState thisTurn() {

        try {
            IPlayerState current = turnQueue.first();

            if(current.isBlocked()) {
                current.tickBlock();
                turnQueue.enqueue(turnQueue.dequeue());
                return turnQueue.first();
            }

            if(current.hasExtraTurns()) {
                current.useExtraTurn();
                return current;
            }

            turnQueue.enqueue(turnQueue.dequeue());
            return current;

        } catch (EmptyCollectionException e) {
            return null;
        }
    }

}
