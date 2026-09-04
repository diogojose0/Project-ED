package customCollections;

import Collections.exceptions.EmptyCollectionException;
import Game.api.corridor.ICorridor;
import Game.api.corridor.IEventStrategy;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link MazeNetwork}.
 * <p>
 * These tests validate basic graph operations such as:
 * vertex insertion and containment;
 * edge creation and removal (corridor connections);
 * neighbor checks and edge retrieval;
 * graph traversals using BFS and DFS;
 * shortest path calculation;
 * capacity expansion;
 * graph connectivity.
 * </p>
 */
public class MazeNetworkTest {

    private MazeNetwork<String> network;
    private ICorridor corridor;

    /**
     * Simple mock implementation of {@link ICorridor} for testing purposes.
     * <p>
     * It records whether {@link #generateEvent()} was called,
     * and exposes a name for identification in assertions or logs.
     * </p>
     */
    private static class MockCorridor implements ICorridor {
        private final String name;
        private boolean eventGenerated = false;

        public MockCorridor(String name) {
            this.name = name;
        }

        @Override
        public IEventStrategy generateEvent() {
            eventGenerated = true;
            return null;
        }

        @Override
        public JSONObject toJson() {
            return null;
        }

        /**
         * Indicates whether {@link #generateEvent()} has been invoked.
         *
         * @return {@code true} if the event was generated, {@code false} otherwise
         */
        public boolean isEventGenerated() {
            return eventGenerated;
        }

        /**
         * Returns the corridor name.
         *
         * @return corridor name
         */
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "MockCorridor{" +
                    "name='" + name + '\'' +
                    ", eventGenerated=" + eventGenerated +
                    '}';
        }
    }

    /**
     * Initializes a fresh {@link MazeNetwork} and a shared mock corridor
     * before each test. Also inserts four vertices: A, B, C and D.
     */
    @BeforeEach
    void setUp() {
        network = new MazeNetwork<>();
        corridor = new MockCorridor("A-B Corridor");

        network.addVertex("A");
        network.addVertex("B");
        network.addVertex("C");
        network.addVertex("D");
    }

    /**
     * Verifies that a new vertex can be successfully added to the network.
     */
    @Test
    void addVertex_shouldAddVertexSuccessfully() {
        network.addVertex("E");
        assertTrue(network.containsVertex("E"));
    }

    /**
     * Verifies that {@link MazeNetwork#containsVertex(Object)} returns
     * {@code true} for an existing vertex and {@code false} for a missing one.
     */
    @Test
    void containsVertex_shouldReturnTrueIfExists() {
        assertTrue(network.containsVertex("A"));
        assertFalse(network.containsVertex("Z"));
    }

    /**
     * Verifies that {@link MazeNetwork#addEdge(Object, Object, ICorridor)} creates
     * a bidirectional connection between two vertices.
     */
    @Test
    void addEdge_shouldConnectVerticesBidirectionally() {
        network.addEdge("A", "B", corridor);
        assertTrue(network.areNeighbours("A", "B"));
        assertTrue(network.areNeighbours("B", "A"));
    }

    /**
     * Verifies that {@link MazeNetwork#removeEdge(int, int)} removes an existing connection.
     */
    @Test
    void removeEdge_shouldRemoveConnections() {
        network.addEdge("A", "B", corridor);
        network.removeEdge(0, 1);
        assertFalse(network.areNeighbours("A", "B"));
    }

    /**
     * Verifies that {@link MazeNetwork#getEdge(Object, Object)} returns
     * the correct corridor for a given pair of vertices.
     */
    @Test
    void getEdge_shouldReturnCorrectCorridor() {
        network.addEdge("A", "B", corridor);
        assertEquals(corridor, network.getEdge("A", "B"));
    }

    /**
     * Verifies that {@link MazeNetwork#areNeighbours(Object, Object)} throws
     * an {@link IllegalArgumentException} when one of the vertices is invalid.
     */
    @Test
    void areNeighbours_shouldThrowForInvalidVertices() {
        assertThrows(IllegalArgumentException.class, () -> network.areNeighbours("A", "Z"));
    }

    /**
     * Verifies that {@link MazeNetwork iteratorDFS(int)} returns vertices
     * in a valid depth-first traversal order starting from index 0.
     */
    @Test
    void iteratorDFS_shouldReturnCorrectOrder() throws EmptyCollectionException {
        network.addEdge("A", "B", corridor);
        network.addEdge("B", "C", corridor);
        Iterator<String> it = network.iteratorDFS(0);

        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
    }

    /**
     * Verifies that {@link MazeNetwork iteratorBFS(int)} returns at least
     * one element when the graph contains edges.
     */
    @Test
    void iteratorBFS_shouldReturnSomeValidTraversal() throws EmptyCollectionException {
        network.addEdge("A", "B", corridor);
        network.addEdge("A", "C", corridor);
        network.addEdge("B", "D", corridor);

        Iterator<String> it = network.iteratorBFS(0);
        assertTrue(it.hasNext());
    }

    /**
     * Verifies that {@link MazeNetwork iteratorShortestPath(int, int)} returns
     * the shortest path between two vertices when multiple routes exist.
     */
    @Test
    void iteratorShortestPath_shouldReturnShortestPath() throws EmptyCollectionException {
        network.addEdge("A", "B", corridor);
        network.addEdge("B", "C", corridor);
        network.addEdge("A", "D", corridor);
        network.addEdge("D", "C", corridor);

        Iterator<String> path = network.iteratorShortestPath(0, 2);
        assertEquals("A", path.next());
        assertEquals("B", path.next());
        assertEquals("C", path.next());
    }

    /**
     * Verifies that the internal storage expansion logic
     * does not lose already inserted vertices.
     */
    @Test
    void expandCapacity_shouldIncreaseSizeWithoutLosingData() {
        for (int i = 0; i < 15; i++) {
            network.addVertex("V" + i);
        }
        assertTrue(network.containsVertex("V14"));
    }

    /**
     * Verifies that {@link MazeNetwork#defaultIteratorDivisions()} returns a
     * non-empty iterator when the graph has at least one connected vertex.
     */
    @Test
    void defaultIteratorDivisions_shouldReturnDFSIterator() {
        network.addEdge("A", "B", corridor);
        Iterator<String> it = network.defaultIteratorDivisions();
        assertTrue(it.hasNext());
    }

    /**
     * Verifies that the mock corridor correctly records when an event
     * is generated through {@link MockCorridor#generateEvent()}.
     */
    @Test
    void mockCorridor_shouldTrackEventGeneration() {
        MockCorridor mock = new MockCorridor("TestCorridor");
        assertFalse(mock.isEventGenerated());
        mock.generateEvent();
        assertTrue(mock.isEventGenerated());
    }

    /**
     * Verifies that {@link MazeNetwork#isConnected()} returns {@code true}
     * when all vertices are reachable from the first one.
     */
    @Test
    void isConnected_shouldReturnTrueForConnectedGraph() {
        network.addEdge("A", "B", corridor);
        network.addEdge("B", "C", corridor);
        network.addEdge("C", "D", corridor);

        assertTrue(network.isConnected());
    }

    /**
     * Verifies that {@link MazeNetwork#isConnected()} returns {@code false}
     * when the graph is not fully connected (there is an isolated vertex).
     */
    @Test
    void isConnected_shouldReturnFalseForDisconnectedGraph() {
        network.addEdge("A", "B", corridor);
        assertFalse(network.isConnected());
    }

    /**
     * Verifies that {@link MazeNetwork iteratorShortestPath(int, int)}
     * returns an empty iterator when no path exists between the two vertices.
     */
    @Test
    void iteratorShortestPath_shouldReturnEmptyWhenNoPath() throws EmptyCollectionException {
        network.addEdge("A", "B", corridor);

        Iterator<String> path = network.iteratorShortestPath(0, 2);
        assertFalse(path.hasNext());
    }
}
