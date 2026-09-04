package Game.api.division;

import Game.api.player.IPlayerState;


/**
 * Contract for mini-games associated with a division.
 * <p>
 * A mini-game receives a player, executes its internal logic and indicates whether it has been solved.
 * </p>
 */
public interface IMiniGame {

    /**
     * Starts the mini-game for the given player.
     *
     * @param playerState the player state that is interacting with the mini-game
     * @return {@code true} if the mini-game is solved after this execution, {@code false} otherwise
     */
    boolean start(IPlayerState playerState);

    /**
     * Indicates whether the mini-game is currently marked as solved.
     *
     * @return {@code true} if solved, {@code false} otherwise
     */
    boolean isSolved();

    /**
     * Updates the solved state of this mini-game.
     *
     * @param solved {@code true} to mark the mini-game as solved, {@code false} to mark it as unsolved
     */
    void setSolved(boolean solved);
}
