package Game.api.maze;


/**
 * Contract for creating maze instances.
 * <p>
 * An IMazeCreator is responsible for generating new maze objects
 * with specified attributes, such as a name.
 * </p>
 */
public interface IMazeCreator {

    /**
     * Creates a new maze instance with the specified name.
     *
     * @param name the name of the maze
     * @return a new IMaze instance
     */
    IMaze createMaze(String name);
}
