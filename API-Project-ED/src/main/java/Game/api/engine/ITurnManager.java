package Game.api.engine;

import Collections.list.ListADT;
import Game.api.player.IPlayerState;



/**
 * Contract for components that manage player turns.
 * <p>
 * The turn manager keeps the internal turn order and returns the next
 * player to act, applying turn-related effects such as blocked turns and extra turns.
 * </p>
 */
public interface ITurnManager {

    /**
     * Loads the players into the internal turn sequence.
     *
     * @param states list of player states to be added to the turn queue
     */
    void loadPlayers(ListADT<IPlayerState> states);

    /**
     * Resets the internal state of the turn manager,
     * clearing any existing turn sequence.
     */
    void reset();

    /**
     * Returns the player whose turn it is, applying turn rules.
     * <p>
     * Implementations may skip blocked players, give extra turns,
     * and rotate the order according to game rules.
     * </p>
     *
     * @return the {@link IPlayerState} whose turn it is, or {@code null} if no players are available
     */
    IPlayerState thisTurn();
}

