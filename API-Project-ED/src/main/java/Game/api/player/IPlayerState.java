package Game.api.player;


/**
 * Contract for the dynamic state of a player within a game.
 * <p>
 * The player state combines the underlying {@link IPlayer} data with
 * movement, turn-related flags and winner status.
 * </p>
 */
public interface IPlayerState {

    /**
     * Returns the player data associated with this state.
     *
     * @return the {@link IPlayer} instance
     */
    IPlayer getPlayer();

    /**
     * Returns the movement tracker for this player.
     *
     * @return the {@link IMovementPlayer} instance
     */
    IMovementPlayer getMovementPlayer();

    /**
     * Indicates whether the player is currently blocked from playing.
     *
     * @return {@code true} if the player should lose this turn, {@code false} otherwise
     */
    boolean isBlocked();

    /**
     * Adds a number of turns during which this player will be blocked.
     *
     * @param turns number of future turns to block
     */
    void blockFor(int turns);

    /**
     * Consumes one blocked turn, decreasing the remaining number blocked turns.
     */
    void tickBlock();

    /**
     * Adds a number of extra turns for this player.
     *
     * @param turns number of extra turns to grant
     */
    void addExtraTurns(int turns);

    /**
     * Consumes one extra turn, decreasing the remaining number of extra turns.
     */
    void useExtraTurn();

    /**
     * Marks this player as the winner of the game.
     */
    void markAsWinner();

    /**
     * Indicates whether this player is marked as winner.
     *
     * @return {@code true} if winner, {@code false} otherwise
     */
    boolean isWinner();

    /**
     * Resets the state of the player for a new game.
     * <p>
     * This typically clears movement history, blocked and extra turns, and winner status.
     * </p>
     */
    void reset();

    /**
     * Indicates whether the player still has extra turns available.
     *
     * @return {@code true} if there are extra turns left, {@code false} otherwise
     */
    boolean hasExtraTurns();
}
