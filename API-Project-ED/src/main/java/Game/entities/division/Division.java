package Game.entities.division;

import Game.api.division.IDivision;
import Game.api.division.enigmaGame.IEnigmaGame;
import Game.api.division.leverGame.ILever;
import Game.api.division.leverGame.ILeverGame;
import Game.api.player.IPlayerState;
import Game.api.division.IMiniGame;
import Game.exceptions.division.NullMiniGameException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;


/**
 * Default implementation of {@link IDivision} representing a room or cell
 * in the maze, optionally associated with a mini-game.
 */
public class Division implements IDivision {

    /** Static counter used to assign incremental ids to divisions. */
    private static int nextID = 1;
    /** Unique identifier of this division. */
    private int id;
    /** Name of the division. */
    private String name;
    /** Indicates if this division is the treasure room. */
    private final boolean isTreasureRoom;
    /** Indicates if this division can be used as an entry point. */
    private final boolean isEntryPoint;
    /** Mini-game associated with this division, if any. */
    private IMiniGame miniGame;

    /**
     * Creates a new {@code Division} with a fixed identifier.
     *
     * @param name the division name
     * @param isTreasureRoom {@code true} if this division is the treasure room
     * @param isEntryPoint {@code true} if this division is an entry point
     * @param id the division id
     */
    public Division (String name, boolean isTreasureRoom, boolean isEntryPoint, int id) {
        this.id = id;
        this.name = name;
        this.isTreasureRoom = isTreasureRoom;
        this.isEntryPoint = isEntryPoint;
    }

    /**
     * Creates a new {@code Division} with an automatically generated identifier.
     *
     * @param name the division name
     * @param isTreasureRoom {@code true} if this division is the treasure room
     * @param isEntryPoint {@code true} if this division is an entry point
     */
    public Division (String name, boolean isTreasureRoom, boolean isEntryPoint) {
        this(name, isTreasureRoom, isEntryPoint, nextID++);
    }

    /**
     * Starts the mini-game associated with this division for the given player.
     *
     * @param playerState the player attempting the mini-game
     * @throws NullMiniGameException if no mini-game is associated with this division
     */
    @Override
    public void startMiniGame(IPlayerState playerState) throws NullMiniGameException {
        if (miniGame == null) {
            throw new NullMiniGameException();
        }
        miniGame.start(playerState);
    }

    /**
     * Returns the next available division id that will be assigned.
     *
     * @return next division id
     */
    public static int getNextID() {
        return nextID;
    }

    /**
     * Returns the mini-game associated with this division.
     *
     * @return the mini-game, or {@code null} if none exists
     */
    @Override
    public IMiniGame getMiniGame() {
        return miniGame;
    }

    /**
     * Sets the mini-game for this division.
     *
     * @param miniGame the mini-game to associate
     */
    @Override
    public void setMiniGame(IMiniGame miniGame) {
        this.miniGame = miniGame;
    }

    /**
     * Returns the name of this division.
     *
     * @return the division name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Indicates whether this division is the treasure room.
     *
     * @return {@code true} if this is the treasure room, {@code false} otherwise
     */
    @Override
    public boolean isTreasureRoom() {
        return isTreasureRoom;
    }

    /**
     * Indicates whether this division is an entry point.
     *
     * @return {@code true} if this division is an entry point, {@code false} otherwise
     */
    @Override
    public boolean isEntryPoint() {
        return isEntryPoint;
    }

    /**
     * Returns the unique id of this division.
     *
     * @return division id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Returns a string representation of the division, including its name
     * and, when applicable, the type of mini-game attached.
     *
     * @return formatted division description
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        if(this.miniGame instanceof ILeverGame) {
            builder.append("(Lever Game)");
        } else if (this.miniGame instanceof IEnigmaGame) {
            builder.append("(Enigma Game)");
        }
        return builder.toString();
    }

    /**
     * Compares this division with another object for equality.
     * Two divisions are considered equal if they share the same id and name.
     *
     * @param object the object to compare with
     * @return {@code true} if both represent the same division, {@code false} otherwise
     */
    public boolean equals (Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Division)) {
            return false;
        }

        Division division = (Division) object;
        return this.id == division.id && this.name.equals(division.name);
    }

    /**
     * Converts this division into its JSON representation, including:
     * {@code id}, {@code name}, {@code hasTreasure} and {@code isEntryPoint};
     * for lever-game divisions, a {@code "levers"} array with each lever serialized via {@code toJson()}.
     *
     * @return a {@link JSONObject} describing this division
     */
    @Override
    public JSONObject toJson() {
        JSONObject divisionJson = new JSONObject();

        divisionJson.put("id", this.id);
        divisionJson.put("name", this.name);
        divisionJson.put("hasTreasure", this.isTreasureRoom);
        divisionJson.put("isEntryPoint", this.isEntryPoint);

        if (miniGame instanceof ILeverGame leverGame) {

            JSONArray leversArray = new JSONArray();

            Iterator<ILever> iterator = leverGame.getLevers().iterator();
            while(iterator.hasNext()) {
                ILever lever = iterator.next();
                leversArray.add(lever.toJson());
            }

            divisionJson.put("levers", leversArray);
        }

        return divisionJson;
    }

}
