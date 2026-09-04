package customCollections;

import Collections.exceptions.EmptyCollectionException;
import Collections.graphs.MatrixNetwork;
import Collections.list.ArrayUnorderedList;
import Collections.queue.LinkedQueue;
import Collections.stack.LinkedStack;
import Game.api.corridor.ICorridor;

import java.util.Iterator;


/**
 * Specialized network implementation for representing a maze, where vertices are divisions
 * and edges are corridors.
 * <p>
 * This class extends {@link MatrixNetwork} but stores {@link ICorridor} instances in its
 * adjacency matrix instead of simple weights or booleans.
 * </p>
 *
 * @param <T> the type of vertices stored in the network
 */
public class MazeNetwork<T> extends MatrixNetwork<T> implements MazeNetworkADT<T> {

    /** Adjacency matrix storing the corridors between vertices. */
    private ICorridor[][] adjMatrix;

    /**
     * Creates an empty {@code MazeNetwork} with an initial capacity of 10 vertices.
     */
    public MazeNetwork() {
        this.numVertices = 0;
        this.adjMatrix = new ICorridor[10][10];
        this.vertices = (T[]) new Object[10];
    }

    /**
     * Returns a default iterator over the divisions, starting a depth-first traversal from the first vertex.
     *
     * @return an iterator over the divisions in DFS order starting from index 0
     */
    @Override
    public Iterator<T> defaultIteratorDivisions() {
        return this.iteratorDFS(this.vertices[0]);
    }

    /**
     * Returns an iterator over all divisions currently stored in the graph.
     * <p>
     * The iterator goes through all non-null vertices.
     * </p>
     *
     * @return an iterator over all divisions
     */
    @Override
    public Iterator<T> iteratorDivisions() {
        ArrayUnorderedList<T> list = new ArrayUnorderedList<>();

        for (int i = 0; i < numVertices; i++) {
            if (vertices[i] != null) {
                list.addToRear(vertices[i]);
            }
        }

        return list.iterator();
    }

    /**
     * Adds a new vertex to the network.
     * <p>
     * If the internal arrays are full, the capacity is expanded before insertion.
     * New rows and columns in the adjacency matrix are initialized with {@code null}
     * </p>
     *
     * @param vertex the vertex to be added
     */
    @Override
    public void addVertex(T vertex) {
        if (this.numVertices == this.vertices.length) {
            this.expandCapacity();
        }

        this.vertices[this.numVertices] = vertex;

        for(int i = 0; i <= this.numVertices; ++i) {
            this.adjMatrix[this.numVertices][i] = null;
            this.adjMatrix[i][this.numVertices] = null;
        }

        this.numVertices++;
    }

    /**
     * Doubles the capacity of the internal storage arrays, copying
     * existing vertices and corridors into the new structures.
     */
    @Override
    protected void expandCapacity() {
        T[] tempVertices = (T[])(new Object[this.vertices.length * 2]);
        ICorridor[][] tempAdjMatrix = new ICorridor[this.vertices.length * 2][this.vertices.length * 2];

        for (int i = 0; i < this.numVertices; ++i) {
            for (int j = 0; j < this.numVertices; ++j) {
                tempAdjMatrix[i][j] = this.adjMatrix[i][j];
            }
            tempVertices[i] = this.vertices[i];
        }

        this.vertices = tempVertices;
        this.adjMatrix = tempAdjMatrix;
    }

    /**
     * Checks if the given vertex exists in the network.
     *
     * @param vertex the vertex to search for
     * @return {@code true} if the vertex is present, {@code false} otherwise
     */
    @Override
    public boolean containsVertex(T vertex) {
        for (int i = 0; i < this.numVertices; i++) {
            if (this.vertices[i].equals(vertex)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds an edge between two vertices.
     *
     * @param startVertex the starting vertex
     * @param endVertex the ending vertex
     * @param corridor the corridor connecting both vertices
     */
    @Override
    public void addEdge(T startVertex, T endVertex, ICorridor corridor) {
        this.addEdge(this.getIndex(startVertex), this.getIndex(endVertex), corridor);
    }

    /**
     * Adds an edge between two vertices identified by their indices.
     * The corridor is stored symmetrically to represent an undirected connection.
     *
     * @param index1 index of the first vertex
     * @param index2 index of the second vertex
     * @param corridor the corridor connecting both vertices
     */
    private void addEdge(int index1, int index2, ICorridor corridor) {
        if (this.indexIsValid(index1) && this.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = corridor;
            this.adjMatrix[index2][index1] = corridor;
        }
    }

    /**
     * Removes the edge between two vertices identified by their indices.
     *
     * @param index1 index of the first vertex
     * @param index2 index of the second vertex
     */
    @Override
    public void removeEdge(int index1, int index2) {
        if (this.indexIsValid(index1) && this.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = null;
            this.adjMatrix[index2][index1] = null;
        }
    }

    /**
     * Checks if two vertices are neighbors (directly connected by a corridor).
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return {@code true} if there is a corridor between the two vertices, {@code false} otherwise
     * @throws IllegalArgumentException if either vertex is invalid
     */
    @Override
    public boolean areNeighbours(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            return adjMatrix[index1][index2] != null;
        } else {
            throw new IllegalArgumentException("Invalid vertexes!");
        }
    }

    /**
     * Performs a breadth-first traversal starting from the given index and returns an iterator over the visited vertices.
     *
     * @param startIndex the index of the starting vertex
     * @return an iterator over the vertices visited from the starting vertex
     * @throws EmptyCollectionException if the internal queue operations fail
     */
    @Override
    protected Iterator<T> iteratorBFS(int startIndex) throws EmptyCollectionException {
        LinkedQueue<Integer> traversalQueue = new LinkedQueue();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList();
        if (!this.indexIsValid(startIndex)) {
            return resultList.iterator();
        } else {

            boolean[] visited = new boolean[this.numVertices];

            for(int i = 0; i < this.numVertices; ++i) {
                visited[i] = false;
            }

            traversalQueue.enqueue(startIndex);
            visited[startIndex] = true;

            while(!traversalQueue.isEmpty()) {
                Integer x = traversalQueue.dequeue();
                resultList.addToRear(this.vertices[x]);

                for(int i = 0; i < this.numVertices; ++i) {
                    if (this.adjMatrix[x][i] != null && !visited[i]) {
                        traversalQueue.enqueue(i);
                        visited[i] = true;
                    }
                }
            }

            return resultList.iterator();
        }
    }

    /**
     * Performs a depth-first traversal starting from the given index and returns an iterator over the visited vertices.
     *
     * @param startIndex the index of the starting vertex
     * @return an iterator over the vertices visited from the starting vertex
     * @throws EmptyCollectionException if the internal stack operations fail
     */
    @Override
    protected Iterator<T> iteratorDFS(int startIndex) throws EmptyCollectionException {
        LinkedStack<Integer> traversalStack = new LinkedStack();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList();
        boolean[] visited = new boolean[this.numVertices];
        if (!this.indexIsValid(startIndex)) {
            return resultList.iterator();
        } else {

            for(int i = 0; i < this.numVertices; ++i) {
                visited[i] = false;
            }

            traversalStack.push(startIndex);
            resultList.addToRear(this.vertices[startIndex]);
            visited[startIndex] = true;

            while(!traversalStack.isEmpty()) {
                Integer x = traversalStack.peek();
                boolean found = false;

                for(int i = 0; i < this.numVertices && !found; ++i) {
                    if (this.adjMatrix[x][i] != null && !visited[i]) {
                        traversalStack.push(i);
                        resultList.addToRear(this.vertices[i]);
                        visited[i] = true;
                        found = true;
                    }
                }

                if (!found && !traversalStack.isEmpty()) {
                    traversalStack.pop();
                }
            }

            return resultList.iterator();
        }
    }

    /**
     * Returns the corridor (edge) that connects two given vertices.
     *
     * @param vertex the first vertex
     * @param vertex2 the second vertex
     * @return the corridor between the two vertices, or {@code null} if none exists
     */
    @Override
    public ICorridor getEdge(T vertex, T vertex2) {
        int index1 = getIndex(vertex);
        int index2 = getIndex(vertex2);

        return this.adjMatrix[index1][index2];
    }

    /**
     * Computes the shortest path between two vertices using BFS and returns an iterator over the vertices in this path.
     *
     * @param startIndex index of the starting vertex
     * @param targetIndex index of the target vertex
     * @return an iterator over the vertices in the shortest path, or an empty iterator if no path exists or indices are invalid
     * @throws EmptyCollectionException if internal queue/stack operations fail
     */
    @Override
    protected Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) throws EmptyCollectionException {
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex)) {
            boolean[] visited = new boolean[this.numVertices];
            int[] predecessor = new int[this.numVertices];

            for (int i = 0; i < this.numVertices; ++i) {
                predecessor[i] = -1;
                visited[i] = false;
            }

            LinkedQueue<Integer> queue = new LinkedQueue<>();
            visited[startIndex] = true;
            queue.enqueue(startIndex);
            boolean found = false;

            while (!queue.isEmpty() && !found) {
                int current = queue.dequeue();

                for (int vertex = 0; vertex < this.numVertices; ++vertex) {
                    ICorridor edge = this.adjMatrix[current][vertex];
                    if (edge != null && !visited[vertex]) {
                        visited[vertex] = true;
                        predecessor[vertex] = current;
                        queue.enqueue(vertex);
                        if (vertex == targetIndex) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                return (new ArrayUnorderedList<T>()).iterator();
            }

            LinkedStack<Integer> stack = new LinkedStack<>();
            for (int step = targetIndex; step != -1; step = predecessor[step]) {
                stack.push(step);
            }

            ArrayUnorderedList<T> path = new ArrayUnorderedList<>();
            while (!stack.isEmpty()) {
                path.addToRear(this.vertices[stack.pop()]);
            }

            return path.iterator();
        } else {
            return (new ArrayUnorderedList<T>()).iterator();
        }
    }

    /**
     * Checks whether the graph is connected.
     * <p>
     * A graph is considered connected if every vertex is reachable from
     * the first vertex. The method performs a BFS starting from the first vertex, marking visited
     * vertices. If, after the traversal, any vertex remains unvisited,
     * the graph is not connected.
     * </p>
     *
     * @return {@code true} if all vertices are reachable from  the first vertex
     * and the graph is not empty; {@code false} otherwise
     */
    @Override
    public boolean isConnected() {
        if (this.isEmpty()) {
            return false;
        } else {
            boolean[] visited = new boolean[this.numVertices];
            LinkedQueue<Integer> queue = new LinkedQueue();
            queue.enqueue(0);
            visited[0] = true;
            int current = -1;

            while(!queue.isEmpty()) {
                try {
                    current = queue.dequeue();
                } catch (EmptyCollectionException var5) {
                }

                for(int i = 0; i < this.numVertices; ++i) {
                    if (this.adjMatrix[current][i] != null && !visited[i]) {
                        visited[i] = true;
                        queue.enqueue(i);
                    }
                }
            }

            for(int i = 0; i < this.numVertices; ++i) {
                if (!visited[i]) {
                    return false;
                }
            }

            return true;
        }
    }

}
