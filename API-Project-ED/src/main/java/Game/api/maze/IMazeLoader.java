package Game.api.maze;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.data.JsonExportable;
import Game.exceptions.mazeloader.MazeAlreadyExistsException;
import Game.exceptions.mazeloader.MazeDoesntExistException;
import Game.exceptions.mazeloader.NullMazeException;


/**
 * Contract for components that manage available mazes.
 * <p>
 * A maze loader stores multiple {@link IMaze} instances and provides
 * access to them by id or as a collection.
 * </p>
 */
public interface IMazeLoader extends JsonExportable {

    /**
     * Adds a new maze to the loader.
     *
     * @param maze the maze to add
     * @throws MazeAlreadyExistsException if a maze with the same id already exists
     * @throws NullMazeException if {@code maze} is {@code null}
     */
    void add(IMaze maze) throws MazeAlreadyExistsException, NullMazeException;

    /**
     * Retrieves a maze by its identifier.
     *
     * @param id the maze id
     * @return the matching maze
     * @throws MazeDoesntExistException if no maze with the given id exists
     */
    IMaze getMaze(int id) throws MazeDoesntExistException;

    /**
     * Returns the collection of all loaded mazes.
     *
     * @return a list containing all {@link IMaze} instances
     */
    DoublyLinkedUnorderedList<IMaze> getMazes();
}
