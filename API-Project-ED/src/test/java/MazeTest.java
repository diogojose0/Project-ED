import Collections.list.ArrayUnorderedList;
import Game.api.corridor.ICorridor;
import Game.api.division.IDivision;
import Game.api.maze.IMaze;
import Game.entities.maze.Maze;
import Game.exceptions.maze.*;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Maze} implementation of {@link IMaze}.
 * <p>
 * These tests verify division management, corridor creation, treasure room rules,
 * entry points, neighbor detection and shortest path behaviour.
 * </p>
 */
public class MazeTest {

    /** Maze instance under test. */
    private IMaze maze;

    /**
     * Simple fake implementation of {@link ICorridor} used for testing.
     */
    private static class FakeCorridor implements ICorridor {
        private final String name;

        public FakeCorridor(String name) { this.name = name; }

        @Override
        public Game.api.corridor.IEventStrategy generateEvent() { return null; }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public JSONObject toJson() {
            return null;
        }

        @Override
        public String toString() { return name; }
    }

    /**
     * Simple fake implementation of {@link IDivision} used for testing.
     */
    private static class FakeDivision implements IDivision {
        private final String name;
        private final boolean treasure;
        private final boolean entry;
        private final int id;

        public FakeDivision(String name, boolean treasure, boolean entry, int id) {
            this.name = name;
            this.treasure = treasure;
            this.entry = entry;
            this.id = id;
        }

        @Override public int getId() { return id; }
        @Override public void startMiniGame(Game.api.player.IPlayerState playerState) {}
        @Override public Game.api.division.IMiniGame getMiniGame() { return null; }
        @Override public void setMiniGame(Game.api.division.IMiniGame miniGame) {}
        @Override public String getName() { return name; }
        @Override public boolean isTreasureRoom() { return treasure; }
        @Override public boolean isEntryPoint() { return entry; }
        @Override public JSONObject toJson() { return null; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof FakeDivision other)) return false;
            return this.id == other.id;
        }

        @Override
        public int hashCode() { return id; }
    }

    /**
     * Creates a fresh {@link Maze} instance before each test.
     */
    @BeforeEach
    void setUp() {
        maze = new Maze("MysticMaze");
    }

    /**
     * Verifies that the constructor sets the name and that {@link Maze#toString()}
     * contains both the id and the maze name.
     */
    @Test
    void testConstructorAndToString() {
        String result = maze.toString();
        assertTrue(result.contains("MysticMaze"));
        assertTrue(result.contains("id:"));
    }

    /**
     * Verifies that a valid division can be added and later retrieved by name.
     */
    @Test
    void testAddDivisionSuccessfully() throws Exception {
        FakeDivision division = new FakeDivision("A", false, false, 1);
        maze.addDivision(division);
        assertEquals(division, maze.getDivision("A"));
    }

    /**
     * Verifies that adding a {@code null} division throws {@link NullDivisionException}.
     */
    @Test
    void testAddNullDivisionThrows() {
        assertThrows(NullDivisionException.class, () -> maze.addDivision(null));
    }

    /**
     * Verifies that adding the same division twice throws
     * {@link DivisionAlreadyExistsException}.
     */
    @Test
    void testAddDuplicateDivisionThrows() throws Exception {
        FakeDivision division = new FakeDivision("B", false, false, 2);
        maze.addDivision(division);
        assertThrows(DivisionAlreadyExistsException.class, () -> maze.addDivision(division));
    }

    /**
     * Verifies that only a single treasure room is allowed per maze and that
     * adding a second treasure division throws {@link AlreadyHaveTreasureRoomException}.
     */
    @Test
    void testOnlyOneTreasureRoomAllowed() throws Exception {
        FakeDivision treasure1 = new FakeDivision("Treasure1", true, false, 3);
        FakeDivision treasure2 = new FakeDivision("Treasure2", true, false, 4);

        maze.addDivision(treasure1);
        assertThrows(AlreadyHaveTreasureRoomException.class, () -> maze.addDivision(treasure2));
    }

    /**
     * Verifies that a corridor can be successfully added between two divisions
     * and later retrieved via {@link Maze#getCorridor(IDivision, IDivision)}.
     */
    @Test
    void testAddCorridorSuccessfully() throws Exception {
        FakeDivision d1 = new FakeDivision("A", false, false, 1);
        FakeDivision d2 = new FakeDivision("B", false, false, 2);
        maze.addDivision(d1);
        maze.addDivision(d2);

        FakeCorridor corridor = new FakeCorridor("Path");
        assertDoesNotThrow(() -> maze.addCorridor(d1, d2, corridor));
        assertEquals(corridor, maze.getCorridor(d1, d2));
    }

    /**
     * Verifies that adding a corridor with null divisions throws
     * {@link NullDivisionException}.
     */
    @Test
    void testAddCorridorThrowsForNullDivisions() {
        FakeCorridor corridor = new FakeCorridor("NullCorridor");
        assertThrows(NullDivisionException.class, () -> maze.addCorridor(null, null, corridor));
    }

    /**
     * Verifies that adding a corridor with a null corridor object throws
     * {@link NullCorridorException}.
     */
    @Test
    void testAddCorridorThrowsForNullCorridor() {
        FakeDivision d1 = new FakeDivision("X", false, false, 1);
        FakeDivision d2 = new FakeDivision("Y", false, false, 2);
        assertThrows(NullCorridorException.class, () -> maze.addCorridor(d1, d2, null));
    }

    /**
     * Verifies that adding a corridor between the same division throws
     * {@link EqualDivisionException}.
     */
    @Test
    void testAddCorridorThrowsForSameDivision() {
        FakeDivision d = new FakeDivision("Same", false, false, 3);
        assertThrows(EqualDivisionException.class, () -> maze.addCorridor(d, d, new FakeCorridor("Loop")));
    }

    /**
     * Verifies that {@link Maze#getTreasureRoom()} returns the correct treasure division
     * when one is present in the maze.
     */
    @Test
    void testGetTreasureRoomReturnsCorrectDivision() throws Exception {
        FakeDivision d1 = new FakeDivision("Normal", false, false, 1);
        FakeDivision treasure = new FakeDivision("Treasure", true, false, 2);
        maze.addDivision(d1);
        maze.addDivision(treasure);

        assertEquals("Treasure", maze.getTreasureRoom().getName());
    }

    /**
     * Verifies that {@link Maze#getEntryPoints()} returns only the divisions
     * marked as entry points.
     */
    @Test
    void testGetEntryPointsIterator() throws Exception {
        FakeDivision entry = new FakeDivision("Entry", false, true, 1);
        FakeDivision normal = new FakeDivision("Normal", false, false, 2);
        maze.addDivision(entry);
        maze.addDivision(normal);

        Iterator<IDivision> entryPoints = maze.getEntryPoints();
        assertTrue(entryPoints.hasNext());
        assertEquals("Entry", entryPoints.next().getName());
    }

    /**
     * Verifies that a division can be retrieved by both id and name
     * after being added to the maze.
     */
    @Test
    void testGetDivisionByIdAndName() throws Exception {
        FakeDivision div = new FakeDivision("Bridge", false, false, 10);
        maze.addDivision(div);
        assertEquals(div, maze.getDivision("Bridge"));
        assertEquals(div, maze.getDivision(10));
    }

    /**
     * Verifies that requesting a division by name that does not exist
     * throws {@link DivisionNotFoundException}.
     */
    @Test
    void testGetDivisionByNameThrowsWhenNotFound() {
        assertThrows(DivisionNotFoundException.class, () -> maze.getDivision("Missing"));
    }

    /**
     * Verifies that {@link Maze#getNeighbors(IDivision)} returns all divisions
     * directly connected to the given division.
     */
    @Test
    void testGetNeighborsReturnsCorrectDivisions() throws Exception {
        FakeDivision d1 = new FakeDivision("A", false, false, 1);
        FakeDivision d2 = new FakeDivision("B", false, false, 2);
        FakeDivision d3 = new FakeDivision("C", false, false, 3);
        maze.addDivision(d1);
        maze.addDivision(d2);
        maze.addDivision(d3);

        FakeCorridor c1 = new FakeCorridor("AB");
        FakeCorridor c2 = new FakeCorridor("AC");
        maze.addCorridor(d1, d2, c1);
        maze.addCorridor(d1, d3, c2);

        Iterator<IDivision> neighbors = maze.getNeighbors(d1);
        assertTrue(neighbors.hasNext());
        ArrayUnorderedList<String> names = new ArrayUnorderedList<>();
        while(neighbors.hasNext()) {
            names.addToRear(neighbors.next().getName());
        }
        assertTrue(names.contains("B"));
        assertTrue(names.contains("C"));
    }

    /**
     * Verifies that {@link Maze#getTreasureRoom()} returns {@code null}
     * when no treasure division has been added.
     */
    @Test
    void testGetTreasureRoomReturnsNullWhenNone() {
        assertNull(maze.getTreasureRoom());
    }

    /**
     * Verifies that {@link Maze#getShortestPathIterator(IDivision, IDivision)} returns
     * the expected shortest path between two connected divisions.
     */
    @Test
    void testGetShortestPathIterator() throws Exception {
        FakeDivision d1 = new FakeDivision("A", false, false, 1);
        FakeDivision d2 = new FakeDivision("B", false, false, 2);
        FakeDivision d3 = new FakeDivision("C", false, false, 3);
        maze.addDivision(d1);
        maze.addDivision(d2);
        maze.addDivision(d3);

        maze.addCorridor(d1, d2, new FakeCorridor("AB"));
        maze.addCorridor(d2, d3, new FakeCorridor("BC"));

        Iterator<IDivision> path = maze.getShortestPathIterator(d1, d3);
        assertTrue(path.hasNext());
        assertEquals("A", path.next().getName());
        assertEquals("B", path.next().getName());
        assertEquals("C", path.next().getName());
    }

    /**
     * Verifies that requesting a division by a non-existent id throws
     * {@link DivisionNotFoundException}.
     */
    @Test
    void testGetDivisionByIdThrowsWhenNotFound() {
        assertThrows(DivisionNotFoundException.class, () -> maze.getDivision(999));
    }
}
