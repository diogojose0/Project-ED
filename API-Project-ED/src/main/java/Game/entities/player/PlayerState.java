package Game.entities.player;

import Game.api.player.IMovementPlayer;
import Game.api.player.IPlayer;
import Game.api.player.IPlayerState;


/**
 * Implementation of {@link IPlayerState} that stores the dynamic state of a player
 * during the game, including movement, blocked turns, extra turns and winner status.
 */
public class PlayerState implements IPlayerState {

    /** Underlying player whose state is being tracked. */
    private IPlayer player;

    /** Number of turns the player is currently blocked from playing. */
    private int blockedTurns = 0;

    /** Number of extra turns the player has available. */
    private int extraTurns = 0;

    /** Flag indicating whether this player has won the game. */
    private boolean winner = false;

    /** Component responsible for tracking the player's movements through the maze. */
    private IMovementPlayer movementPlayer;


    /**
     * Creates a new {@code PlayerState} for the given player with a fresh movement tracker.
     *
     * @param player the player whose state is represented
     */
    public PlayerState(IPlayer player) {
        this.player = player;
        this.movementPlayer = new MovementPlayer();
    }

    /**
     * Returns the underlying player.
     *
     * @return the player instance
     */
    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    /**
     * Returns the movement tracker associated with this player state.
     *
     * @return the movement player component
     */
    @Override
    public IMovementPlayer getMovementPlayer() {
        return this.movementPlayer;
    }

    /**
     * Indicates whether the player is currently blocked.
     *
     * @return {@code true} if the player has blocked turns remaining, {@code false} otherwise
     */
    @Override
    public boolean isBlocked() {
        return this.blockedTurns > 0;
    }

    /**
     * Increases the number of blocked turns for this player.
     *
     * @param turns the number of turns to block
     */
    @Override
    public void blockFor(int turns) {
        this.blockedTurns += turns;
    }

    /**
     * Decreases the count of blocked turns by one, if any remain.
     */
    @Override
    public void tickBlock() {
        if (this.blockedTurns > 0) {
            this.blockedTurns--;
        }
    }

    /**
     * Adds extra turns to this player.
     *
     * @param turns the number of extra turns to add
     */
    @Override
    public void addExtraTurns(int turns) {
        this.extraTurns += turns;
    }

    /**
     * Consumes one extra turn if available.
     */
    @Override
    public void useExtraTurn() {
        if (this.extraTurns > 0) {
            this.extraTurns--;
        }
    }

    /**
     * Indicates whether the player has extra turns remaining.
     *
     * @return {@code true} if extra turns are available, {@code false} otherwise
     */
    @Override
    public boolean hasExtraTurns() {
        return this.extraTurns > 0;
    }

    /**
     * Marks this player as the winner of the game.
     */
    @Override
    public void markAsWinner(){
        this.winner = true;
    }

    /**
     * Indicates whether this player has been marked as winner.
     *
     * @return {@code true} if winner, {@code false} otherwise
     */
    @Override
    public boolean isWinner(){
        return this.winner;
    }

    /**
     * Resets the state of the player for a new game.
     * <p>
     * Blocked turns, extra turns and winner flag are cleared, and
     * movement information is reinitialized.
     * </p>
     */
    @Override
    public void reset() {
        this.blockedTurns = 0;
        this.extraTurns = 0;
        this.winner = false;
        this.movementPlayer = new MovementPlayer();
    }

    /**
     * Returns a string representation of the player state, including the
     * underlying player information and, if present, blocked and extra turn counters.
     *
     * @return formatted player state description
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.player);

        if (this.blockedTurns > 0) {
            builder.append(" - ").append(this.blockedTurns).append(" blocked");
        }
        if (this.extraTurns > 0) {
            builder.append(" - ").append(this.extraTurns).append(" extra turns");
        }

        return builder.toString();
    }

}
