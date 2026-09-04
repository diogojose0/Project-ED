package Game.api.engine;

import Collections.list.ArrayUnorderedList;
import Game.api.maze.IMaze;
import Game.api.player.IPlayerState;
import Game.exceptions.gameEngine.GameStartedException;
import Game.exceptions.gameEngine.NonePlayersException;
import Game.exceptions.mazeloader.NullMazeException;
import Game.replays.Replay;
import customCollections.ExtendedArrayUnorderedList;


/**
 * Contract for the core game engine that controls the maze game.
 * <p>
 * The game engine manages the maze, the players, the game loop,
 * end-game detection and replay recording.
 * </p>
 */
public interface IGameEngine {

    /**
     * Sets the maze to be used by this game engine.
     *
     * @param maze the maze instance to associate with the engine
     * @throws NullMazeException if {@code maze} is {@code null}
     * @throws GameStartedException if the game has already started
     */
    void setMaze(IMaze maze) throws NullMazeException, GameStartedException;

    /**
     * Starts the game loop.
     *
     * @throws GameStartedException if the game is already running
     * @throws NonePlayersException if there are no players registered in the engine
     */
    void start() throws GameStartedException, NonePlayersException;

    /**
     * Verifies whether the given player has reached the end-game condition
     *
     * @param player the player to check
     * @return {@code true} if the game should end because this player has won, {@code false} otherwise
     */
    boolean verifyEndGame(IPlayerState player);

    /**
     * Returns a list of valid target players for actions that require choosing another player.
     *
     * @param player the player that is initiating the action
     * @return a list containing all players except the given one
     */
    ExtendedArrayUnorderedList<IPlayerState> getValidPlayerTargets(IPlayerState player);

    /**
     * Returns the list of players states managed by this game engine.
     *
     * @return an {@link ArrayUnorderedList} of player states
     */
    ArrayUnorderedList<IPlayerState> getPlayers();

    /**
     * Returns the replay associated with the game session.
     *
     * @return the {@link Replay} instance with recorded events
     */
    Replay getReplay();

    /**
     * Applies end-game configuration and cleanup.
     * <p>
     * This includes resetting players states and preparing the engine for a possible new game.
     * </p>
     */
    void endGameConfig();
}
