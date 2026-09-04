import Game.api.maze.IMaze;
import Game.api.maze.IMazeLoader;
import Game.entities.maze.Maze;
import Game.entities.maze.MazeLoader;
import Game.exceptions.mazeloader.MazeAlreadyExistsException;
import Game.exceptions.mazeloader.MazeDoesntExistException;
import Game.exceptions.mazeloader.NullMazeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link MazeLoader} implementation of {@link IMazeLoader}.
 * <p>
 * These tests verify the behaviour for adding mazes, retrieving them by id,
 * handling invalid/duplicate mazes and exposing the internal collection.
 * </p>
 */
public class MazeLoaderTest {

    /** Loader instance used in the tests. */
    private IMazeLoader loader;

    /**
     * Initializes a fresh {@link MazeLoader} before each test.
     */
    @BeforeEach
    void setUp() {
        loader = new MazeLoader();
    }

    /**
     * Verifies that a maze can be added and then retrieved by its id
     * using {@link MazeLoader#add(IMaze)} and {@link MazeLoader#getMaze(int)}.
     */
    @Test
    void testAddAndRetrieveMazeSuccessfully() throws Exception {
        IMaze maze = new Maze("AncientMaze");
        loader.add(maze);

        IMaze retrieved = loader.getMaze(maze.getId());
        assertEquals(maze, retrieved);
    }

    /**
     * Verifies that adding a {@code null} maze results in a {@link NullMazeException}.
     */
    @Test
    void testAddNullMazeThrows() {
        assertThrows(NullMazeException.class, () -> loader.add(null));
    }

    /**
     * Verifies that adding the same maze instance twice results in a
     * {@link MazeAlreadyExistsException}.
     */
    @Test
    void testAddDuplicateMazeThrows() throws Exception {
        IMaze maze = new Maze("MazeA");
        loader.add(maze);
        assertThrows(MazeAlreadyExistsException.class, () -> loader.add(maze));
    }

    /**
     * Verifies that requesting a maze with a non-existent id
     * throws {@link MazeDoesntExistException}.
     */
    @Test
    void testGetMazeThrowsWhenNotFound() {
        assertThrows(MazeDoesntExistException.class, () -> loader.getMaze(999));
    }

    /**
     * Verifies that {@link MazeLoader#toString()} includes the string
     * representation of all stored mazes.
     */
    @Test
    void testToStringIncludesMazes() throws Exception {
        Maze maze1 = new Maze("Maze1");
        Maze maze2 = new Maze("Maze2");
        loader.add(maze1);
        loader.add(maze2);

        String output = loader.toString();
        assertTrue(output.contains("Maze1"));
        assertTrue(output.contains("Maze2"));
    }

    /**
     * Verifies that {@link MazeLoader#getMazes()} returns a collection
     * containing all mazes that were added.
     */
    @Test
    void testGetMazesReturnsAll() throws Exception {
        Maze m1 = new Maze("Maze1");
        Maze m2 = new Maze("Maze2");
        loader.add(m1);
        loader.add(m2);

        assertEquals(2, loader.getMazes().size());
    }
}
