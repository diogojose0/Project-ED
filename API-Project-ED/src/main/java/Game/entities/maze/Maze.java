package Game.entities.maze;

import Collections.exceptions.ElementNotFoundException;
import Collections.list.ArrayUnorderedList;
import Game.api.corridor.ICorridor;
import Game.api.division.IDivision;
import Game.api.maze.IMaze;
import Game.exceptions.maze.*;
import customCollections.ExtendedArrayUnorderedList;
import customCollections.MazeNetwork;
import customCollections.MazeNetworkADT;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;


/**
 * Implementation of {@link IMaze} that represents a maze composed of divisions and corridors.
 * <p>
 * Internally it uses a {@link MazeNetworkADT} graph to store divisions as vertices and
 * corridors as edges. Each maze has a unique id, a name and can contain at most one
 * treasure room division.
 * </p>
 */
public class Maze implements IMaze {

    /** Static counter to assign unique ids to mazes. */
    private static int nextID = 1;

    /** Unique identifier of this maze. */
    private int id;

    /** Name of the maze. */
    private String name;

    /** Graph representing the maze structure. */
    private MazeNetworkADT<IDivision> graph;

    /** Flag indicating if the maze already has a treasure room. */
    private boolean hasTreasureRoom;

    /**
     * Creates a new {@code Maze} with the given name and an empty graph.
     * A unique id is automatically assigned.
     *
     * @param name the name of the maze
     */
    public Maze(String name) {
        this.graph = new MazeNetwork<>();
        this.hasTreasureRoom = false;
        this.name = name;
        this.id = nextID++;
    }

    /**
     * Returns the unique identifier of this maze.
     *
     * @return maze id
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Adds a division to the maze.
     * <p>
     * The method checks for null references, ensures that only one treasure room
     * is present per maze and avoids inserting duplicated divisions.
     * </p>
     *
     * @param division the division to add
     * @throws NullDivisionException if the division is null
     * @throws AlreadyHaveTreasureRoomException if this maze already contains a treasure room
     * @throws DivisionAlreadyExistsException if the division is already in the maze
     */
    @Override
    public void addDivision(IDivision division) throws NullDivisionException, AlreadyHaveTreasureRoomException, DivisionAlreadyExistsException {
        if (division == null) {
            throw new NullDivisionException();
        }

        if(division.isTreasureRoom()) {
            if(!hasTreasureRoom) {
                hasTreasureRoom = true;
            } else {
                throw new AlreadyHaveTreasureRoomException();
            }
        }

        if(graph.containsVertex(division)) {
            throw new DivisionAlreadyExistsException();
        }

        graph.addVertex(division);
    }

    /**
     * Adds a corridor between two divisions in the maze.
     * <p>
     * Both divisions must be non-null, distinct and the corridor must be non-null.
     * </p>
     *
     * @param startDivision division where the corridor starts
     * @param targetDivision division where the corridor ends
     * @param corridor the corridor that connects both divisions
     * @throws NullDivisionException if any of the divisions is null
     * @throws NullCorridorException if the corridor is null
     * @throws EqualDivisionException if the start and target divisions are the same
     */
    @Override
    public void addCorridor(IDivision startDivision, IDivision targetDivision, ICorridor corridor) throws NullDivisionException, NullCorridorException, EqualDivisionException {
        if(startDivision == null || targetDivision == null) {
            throw new NullDivisionException();
        }

        if(corridor == null) {
            throw new NullCorridorException();
        }

        if(startDivision.equals(targetDivision)) {
            throw new EqualDivisionException();
        }

        graph.addEdge(startDivision, targetDivision, corridor);
    }

    /**
     * Returns an iterator over the neighbors of the given division using BFS order.
     *
     * @param division the reference division
     * @return iterator over divisions reachable from the given division
     */
    @Override
    public Iterator<IDivision> getNeighbors(IDivision division) {
        ArrayUnorderedList<IDivision> neighbors = new ArrayUnorderedList<>();
        Iterator<IDivision> iterator = graph.iteratorBFS(division);

        while(iterator.hasNext()) {
            IDivision next = iterator.next();
            if(this.areNeighbours(next, division)) {
                neighbors.addToRear(next);
            }
        }

        return neighbors.iterator();
    }

    /**
     * Returns an iterator with all divisions that are marked as entry points.
     *
     * @return iterator over entry point divisions
     */
    @Override
    public Iterator<IDivision> getEntryPoints() {
        ArrayUnorderedList<IDivision> entryPoints = new ArrayUnorderedList<>();
        Iterator<IDivision> iterator = graph.defaultIteratorDivisions();

        while(iterator.hasNext()) {
            IDivision division = iterator.next();
            if(division.isEntryPoint()) {
                entryPoints.addToRear(division);
            }
        }

        return entryPoints.iterator();
    }

    /**
     * Returns the corridor that connects two divisions.
     *
     * @param firstDivision the first division
     * @param secondDivision the second division
     * @return the corridor that connects the two divisions, or {@code null} if none exists
     * @throws NullDivisionException if any of the divisions is null
     */
    @Override
    public ICorridor getCorridor(IDivision firstDivision, IDivision secondDivision) throws NullDivisionException {
        if(firstDivision == null || secondDivision == null) {
            throw new NullDivisionException();
        }
        return graph.getEdge(firstDivision, secondDivision);
    }

    /**
     * Returns the treasure room division of this maze, if any.
     *
     * @return the treasure room division or {@code null} if none exists
     */
    @Override
    public IDivision getTreasureRoom() {
        Iterator<IDivision> iterator = this.iteratorDivisions();
        while(iterator.hasNext()) {
            IDivision division = iterator.next();
            if(division.isTreasureRoom()) {
                return division;
            }
        }

        return null;
    }

    /**
     * Returns an iterator over the divisions that form the shortest path between the two given divisions.
     *
     * @param firstDivision the starting division
     * @param lastDivision the target division
     * @return iterator over the shortest path divisions
     */
    @Override
    public Iterator<IDivision> getShortestPathIterator(IDivision firstDivision, IDivision lastDivision) {
        return this.graph.iteratorShortestPath(firstDivision, lastDivision);
    }

    /**
     * Retrieves a division by its id.
     *
     * @param id the division id
     * @return the division with the given id
     * @throws DivisionNotFoundException if no division with that id exists in the maze
     */
    @Override
    public IDivision getDivision(int id) throws DivisionNotFoundException {
        Iterator<IDivision> iterator = this.iteratorDivisions();
        while(iterator.hasNext()) {
            IDivision division = iterator.next();
            if(division.getId() == id) {
                return division;
            }
        }

        throw new DivisionNotFoundException();
    }

    /**
     * Retrieves a division by its name.
     *
     * @param name the division name
     * @return the division with the given name
     * @throws DivisionNotFoundException if no division with that name exists in the maze
     */
    @Override
    public IDivision getDivision(String name) throws DivisionNotFoundException {
        Iterator<IDivision> iterator = this.iteratorDivisions();
        while(iterator.hasNext()) {
            IDivision division = iterator.next();
            if(division.getName().equals(name)) {
                return division;
            }
        }

        throw new DivisionNotFoundException();
    }

    /**
     * Returns a short string representation of the maze, with id and name.
     *
     * @return formatted maze description
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: " + id + "| name: " + name);
        return builder.toString();
    }

    /**
     * Checks if two divisions are neighbors in this maze.
     *
     * @param division the first division
     * @param neighbor the second division
     * @return {@code true} if they are neighbors, {@code false} otherwise
     */
    @Override
    public boolean areNeighbours(IDivision division, IDivision neighbor) {
        return this.graph.areNeighbours(division, neighbor);
    }

    /**
     * Checks whether the maze graph is fully connected.
     * <p>
     * A maze is considered connected if all its divisions are reachable
     * within the underlying graph structure.
     * </p>
     *
     * @return {@code true} if the internal graph is connected;
     *         {@code false} otherwise
     */
    @Override
    public boolean isMazeConnected() {
        return graph.isConnected();
    }

    /**
     * Returns an iterator over all divisions in the maze.
     *
     * @return iterator over all divisions
     */
    @Override
    public Iterator<IDivision> iteratorDivisions() {
        return graph.iteratorDivisions();
    }

    /**
     * Constructs a JSON array representing all corridors in the maze.
     *
     * @return a {@link JSONArray} containing corridor data
     */
    private JSONArray getAllCorridorsJson() {
        JSONArray corridorsArray = new JSONArray();

        ExtendedArrayUnorderedList<IDivision> divisionsList = new ExtendedArrayUnorderedList<>();
        Iterator<IDivision> it = this.iteratorDivisions();
        while (it.hasNext()) {
            divisionsList.addToRear(it.next());
        }

        for (int i = 0; i < divisionsList.size(); i++) {
            IDivision div1;
            try {
                div1 = divisionsList.getByIndex(i);
            } catch (ElementNotFoundException e) {continue;}

            for (int j = i + 1; j < divisionsList.size(); j++) {
                IDivision div2;
                try {
                    div2 = divisionsList.getByIndex(j);
                } catch (ElementNotFoundException e) {continue;}

                this.checkNeighboursCorridorJson(corridorsArray, div1, div2);
            }
        }

        return corridorsArray;
    }

    /**
     * Checks if two divisions are neighbors and, if so, adds their corridor information to the JSON array.
     *
     * @param corridorsArray the JSON array to populate
     * @param div1 the first division
     * @param div2 the second division
     */
    private void checkNeighboursCorridorJson(JSONArray corridorsArray, IDivision div1, IDivision div2) {
        if (this.areNeighbours(div1, div2)) {
            ICorridor corridor = null;
            try {
                corridor = this.getCorridor(div1, div2);
            } catch (NullDivisionException e) {}

            if (corridor != null) {
                JSONObject corrJson = new JSONObject();
                corrJson.put("from", div1.getName());
                corrJson.put("to", div2.getName());
                corrJson.put("name", corridor.getName());
                corridorsArray.add(corrJson);
            }
        }
    }

    /**
     * Converts this maze into its JSON representation.
     *
     * @return a {@link JSONObject} containing maze data
     */
    @Override
    public JSONObject toJson() {
        JSONObject mazeJson = new JSONObject();

        mazeJson.put("maze_name", this.name);

        JSONArray divisionsArray = new JSONArray();
        Iterator<IDivision> iterator = this.iteratorDivisions();
        while(iterator.hasNext()) {
            IDivision division = iterator.next();
            divisionsArray.add(division.toJson());
        }
        mazeJson.put("divisions", divisionsArray);

        JSONArray corridorsArray = this.getAllCorridorsJson();
        mazeJson.put("corridors", corridorsArray);

        return mazeJson;
    }

}
