package customCollections;

import Collections.graphs.NetworkADT;
import Game.api.corridor.ICorridor;

import java.util.Iterator;


/**
 * Contract for maze networks where vertices represent divisions and edges represent corridors.
 * <p>
 * This interface extends {@link NetworkADT} with maze-specific operations used to:
 * iterate over divisions,
 * check adjacency between divisions,
 * associate {@link ICorridor} instances with edges,
 * retrieve corridors for event generation.
 *
 * @param <T> the type used to represent divisions (maze rooms)
 */
public interface MazeNetworkADT<T> extends NetworkADT<T> {

    /**
     * Returns an iterator over all divisions using the internal default order
     * of the underlying network.
     *
     * @return an iterator over all divisions in their default internal order
     */
    Iterator<T> defaultIteratorDivisions();

    /**
     * Returns an iterator over all divisions currently stored in the maze.
     *
     * @return an iterator over all divisions present in the maze
     */
    Iterator<T> iteratorDivisions();

    /**
     * Adds a corridor (edge) between two divisions in the maze.
     * <p>
     * The corridor is associated with the connection and can later be retrieved
     * to generate events when a player moves between these divisions.
     * </p>
     *
     * @param startVertex the source division
     * @param endVertex the target division
     * @param corridor the corridor that connects both divisions
     */
    void addEdge(T startVertex, T endVertex, ICorridor corridor);

    /**
     * Checks whether two divisions are directly connected by a corridor.
     *
     * @param vertex1 the first division
     * @param vertex2 the second division
     * @return {@code true} if there is a corridor between both divisions, {@code false} otherwise
     */
    boolean areNeighbours(T vertex1, T vertex2);

    /**
     * Returns the corridor that connects two divisions.
     * <p>
     * The returned corridor is used by the game engine to generate and execute
     * events associated with movement between the two divisions.
     * </p>
     *
     * @param vertex1 the first division
     * @param vertex2 the second division
     * @return the {@link ICorridor} connecting the divisions, or {@code null} if no corridor exists between them
     */
    ICorridor getEdge(T vertex1, T vertex2);

    /**
     * Checks whether a division exists in the maze network.
     *
     * @param vertex the division to check
     * @return {@code true} if the division is present, {@code false} otherwise
     */
    boolean containsVertex(T vertex);
}
