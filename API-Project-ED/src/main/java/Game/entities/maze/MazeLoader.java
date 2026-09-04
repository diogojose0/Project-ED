package Game.entities.maze;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.maze.IMaze;
import Game.api.maze.IMazeLoader;
import Game.exceptions.mazeloader.MazeAlreadyExistsException;
import Game.exceptions.mazeloader.MazeDoesntExistException;
import Game.exceptions.mazeloader.NullMazeException;
import org.json.simple.JSONArray;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


/**
 * Implementation of {@link IMazeLoader} that stores multiple mazes in memory.
 * <p>
 * This class allows adding new mazes and retrieving them by id, as well as
 * listing all stored mazes.
 * </p>
 */
public class MazeLoader implements IMazeLoader {


    /** Path to the mazes JSON file. */
    private static Path path = Paths.get("files/mazes.json");

    /** List of mazes in the game. */
    private DoublyLinkedUnorderedList<IMaze> mazes;

    /**
     * Creates a new {@code MazeLoader} with an empty list of mazes.
     */
    public MazeLoader() {
        mazes = new DoublyLinkedUnorderedList<>();
    }

    /**
     * Adds a maze to the internal collection.
     *
     * @param maze the maze to add
     * @throws MazeAlreadyExistsException if the maze is already stored
     * @throws NullMazeException if the maze is null
     */
    public void add(IMaze maze) throws MazeAlreadyExistsException, NullMazeException {
        if(maze == null) {
            throw new NullMazeException();
        }

        if(mazes.contains(maze)) {
            throw new MazeAlreadyExistsException();
        }

        mazes.addToRear(maze);
    }

    /**
     * Retrieves a maze by its id.
     *
     * @param id the maze id
     * @return the maze with the given id
     * @throws MazeDoesntExistException if no maze with that id exists
     */
    public IMaze getMaze(int id) throws MazeDoesntExistException {
        Iterator<IMaze> iterator = mazes.iterator();
        while(iterator.hasNext()) {
            IMaze maze = iterator.next();
            if(maze.getId() == id) {
                return maze;
            }
        }
        throw new MazeDoesntExistException();
    }

    /**
     * Returns the list of all stored mazes.
     *
     * @return a list of {@link IMaze} instances
     */
    @Override
    public DoublyLinkedUnorderedList<IMaze> getMazes() {
        return mazes;
    }

    /**
     * Returns a string representation of all mazes stored in this loader.
     *
     * @return concatenated string of all maze descriptions
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(IMaze maze : mazes) {
            builder.append(maze);
        }

        return builder.toString();
    }

    /**
     * Exports the mazes to a JSON array.
     *
     * @return a {@link JSONArray} representing all mazes
     */
    @Override
    public JSONArray toJson() {
        JSONArray mazesArray = new JSONArray();

        for (IMaze maze : this.mazes) {
            mazesArray.add(maze.toJson());
        }

        return mazesArray;
    }

}
