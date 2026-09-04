package Game.api.division;

import Game.api.player.IPlayerState;
import Game.exceptions.division.NullMiniGameException;
import org.json.simple.JSONObject;


/**
 * Contract for divisions in the maze.
 * <p>
 * A division can contain a mini-game, be marked as an entry point or as
 * the treasure room, and can be serialized to JSON for export/replay.
 * </p>
 */
public interface IDivision {

    /**
     * Returns the unique identifier of this division.
     *
     * @return the division id
     */
    int getId();

    /**
     * Starts the mini-game associated with this division for the given player.
     * <p>
     * The mini-game implementation decides how the player state is affected and whether the game is solved.
     * </p>
     *
     * @param playerState the player state entering the division
     * @throws NullMiniGameException if no mini-game is associated with this division
     */
    void startMiniGame(IPlayerState playerState) throws NullMiniGameException;

    /**
     * Returns the mini-game associated with this division.
     *
     * @return the {@link IMiniGame} instance, or {@code null} if none is set
     */
    IMiniGame getMiniGame();

    /**
     * Associates a mini-game with this division.
     *
     * @param miniGame the {@link IMiniGame} to set, or {@code null} to remove it
     */
    void setMiniGame(IMiniGame miniGame);

    /**
     * Returns the display name of this division.
     *
     * @return the division name
     */
    String getName();

    /**
     * Indicates whether this division is the treasure room.
     *
     * @return {@code true} if this is the treasure room, {@code false} otherwise
     */
    boolean isTreasureRoom();

    /**
     * Indicates whether this division can be used as an entry point for players.
     *
     * @return {@code true} if this is an entry point, {@code false} otherwise
     */
    boolean isEntryPoint();

    /**
     * Converts this division into its JSON representation.
     * <p>
     * The JSON is used when exporting maze configuration, saving state or building replays.
     * </p>
     *
     * @return a {@link JSONObject} containing the serialized data of this division
     */
    JSONObject toJson();
}
