package Game.api.maze;

import Game.api.corridor.ICorridor;
import Game.api.division.IDivision;
import Game.exceptions.maze.AlreadyHaveTreasureRoomException;
import Game.exceptions.maze.DivisionAlreadyExistsException;
import Game.exceptions.maze.DivisionNotFoundException;
import Game.exceptions.maze.EqualDivisionException;
import Game.exceptions.maze.NullCorridorException;
import Game.exceptions.maze.NullDivisionException;
import org.json.simple.JSONObject;

import java.util.Iterator;


/**
 * Contract for maze structures used by the game.
 * <p>
 * A maze manages divisions, corridors between them,
 * entry points and the treasure room, and provides graph-related
 * operations such as neighbor and shortest path retrieval.
 * </p>
 */
public interface IMaze {

    /**
     * Returns the unique identifier of this maze.
     *
     * @return the maze id
     */
    int getId();

    /**
     * Adds a new division to the maze.
     *
     * @param division the division to add
     * @throws NullDivisionException if {@code division} is {@code null}
     * @throws AlreadyHaveTreasureRoomException if the maze already has a treasure room
     * and the new division is also marked as treasure
     * @throws DivisionAlreadyExistsException if a division with the same id already exists
     */
    void addDivision(IDivision division) throws NullDivisionException, AlreadyHaveTreasureRoomException, DivisionAlreadyExistsException;

    /**
     * Adds a corridor between two divisions.
     *
     * @param startDivision starting division
     * @param targetDivision target division
     * @param corridor corridor connecting the two divisions
     * @throws NullDivisionException if any division is {@code null}
     * @throws NullCorridorException if {@code corridor} is {@code null}
     * @throws EqualDivisionException if both divisions are the same
     */
    void addCorridor(IDivision startDivision, IDivision targetDivision, ICorridor corridor) throws NullDivisionException, NullCorridorException, EqualDivisionException;

    /**
     * Returns an iterator over the neighbors of the given division.
     *
     * @param division the division whose neighbors are requested
     * @return an iterator over neighboring divisions
     */
    Iterator<IDivision> getNeighbors(IDivision division);

    /**
     * Returns an iterator over all entry point divisions in the maze.
     *
     * @return an iterator over entry point divisions
     */
    Iterator<IDivision> getEntryPoints();

    /**
     * Returns the corridor that connects two divisions.
     *
     * @param firstDivision one division
     * @param secondDivision the other division
     * @return the corridor connecting both divisions
     * @throws NullDivisionException if any division is {@code null}
     */
    ICorridor getCorridor(IDivision firstDivision, IDivision secondDivision) throws NullDivisionException;

    /**
     * Returns the treasure room division.
     *
     * @return the treasure room, or {@code null} if none is configured
     */
    IDivision getTreasureRoom();

    /**
     * Returns an iterator over the divisions in the shortest path between two divisions.
     *
     * @param firstDivision starting division
     * @param lastDivision target division
     * @return an iterator over the divisions that form the shortest path
     */
    Iterator<IDivision> getShortestPathIterator(IDivision firstDivision, IDivision lastDivision);

    /**
     * Retrieves a division by its identifier.
     *
     * @param id the division id
     * @return the matching division
     * @throws DivisionNotFoundException if no division with the given id exists
     */
    IDivision getDivision(int id) throws DivisionNotFoundException;

    /**
     * Retrieves a division by its name.
     *
     * @param name the division name
     * @return the matching division
     * @throws DivisionNotFoundException if no division with the given name exists
     */
    IDivision getDivision(String name) throws DivisionNotFoundException;

    /**
     * Checks whether two divisions are directly connected.
     *
     * @param division a division
     * @param neighbor another division
     * @return {@code true} if there is a corridor between them, {@code false} otherwise
     */
    boolean areNeighbours(IDivision division, IDivision neighbor);

    /**
     * Checks whether the maze is fully connected.
     *
     * @return {@code true} if all divisions are reachable from any other division, {@code false} otherwise
     */
    boolean isMazeConnected();

    /**
     * Returns an iterator over all divisions in the maze.
     *
     * @return an iterator over all divisions
     */
    Iterator<IDivision> iteratorDivisions();

    /**
     * Converts this maze into its JSON representation.
     * <p>
     * The resulting JSON is used when exporting the maze layout or building replays.
     * </p>
     *
     * @return a {@link JSONObject} containing the serialized data for this maze
     */
    JSONObject toJson();
}
