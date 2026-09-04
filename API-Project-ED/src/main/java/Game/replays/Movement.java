package Game.replays;

import Game.api.division.IDivision;
import Game.api.player.IPlayer;
import org.json.simple.JSONObject;


/**
 * Represents a replay event for a player movement between two divisions.
 * <p>
 * A {@code Movement} stores the player involved and the origin and
 * destination divisions of a single move in the maze.
 * </p>
 */
public class Movement extends ReplayEvent {

    /** Division where the movement started. */
    private IDivision fromDivision;
    /** Division where the movement ended. */
    private IDivision toDivision;

    /**
     * Creates a {@code Movement} event associated with the given player.
     * <p>
     *
     * @param player the player who performed the movement
     */
    public Movement(IPlayer player) {
        super(player);
    }

    /**
     * Creates a {@code Movement} event with player, origin and destination.
     *
     * @param player the player who performed the movement
     * @param fromDivision the division the player moved from
     * @param toDivision the division the player moved to
     */
    public Movement(IPlayer player, IDivision fromDivision, IDivision toDivision) {
        this(player);
        this.fromDivision = fromDivision;
        this.toDivision = toDivision;
    }

    /**
     * Sets the origin division of this movement.
     *
     * @param fromDivision the division where the movement started
     */
    public void setFromDivision(IDivision fromDivision) {
        this.fromDivision = fromDivision;
    }

    /**
     * Sets the destination division of this movement.
     *
     * @param toDivision the division where the movement ended
     */
    public void setToDivision(IDivision toDivision) {
        this.toDivision = toDivision;
    }

    /**
     * Builds a description of this movement event,
     * including the base {@link ReplayEvent} information and the
     * from/to divisions.
     *
     * @return a formatted string describing the movement
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("Movement -> from: ").append(fromDivision).append(" to: ").append(toDivision);
        builder.append("\n------------------------------------------------\n");
        return builder.toString();
    }

    /**
     * Converts this movement event into its JSON representation.
     * <p>
     * The JSON extends the base replay event data with:
     * {@code "fromDivision"} – origin division serialized to JSON;
     * {@code "toDivision"} – destination division serialized to JSON.
     * </p>
     *
     * @return a {@link JSONObject} containing this movement event data
     */
    public JSONObject toJson() {
        JSONObject movementJson;

        movementJson = super.toJson();
        movementJson.put("fromDivision", this.fromDivision.toJson());
        movementJson.put("toDivision", this.toDivision.toJson());

        return movementJson;
    }

}
