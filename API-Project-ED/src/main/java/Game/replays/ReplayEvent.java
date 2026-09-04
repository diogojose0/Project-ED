package Game.replays;

import Game.api.player.IPlayer;
import Game.api.replays.IReplayEvent;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Abstract base class for all replay events.
 * <p>
 * A {@code ReplayEvent} stores the player involved in the event and the
 * timestamp indicating when the event occurred. Concrete subclasses add
 * specific data such as movements, corridor events or mini-game results.
 * </p>
 */
public abstract class ReplayEvent implements IReplayEvent {

    /** Player associated with this replay event. */
    private IPlayer player;
    /** Timestamp of when the event occurred. */
    private String timestamp;

    /**
     * Creates a new {@code ReplayEvent} for the given player and sets
     * the timestamp to the current local date and time.
     * <p>
     * The timestamp is formatted as {@code "dd/MM/yy HH:mm:ss"}.
     * </p>
     *
     * @param player the player associated with this event
     */
    public ReplayEvent(IPlayer player) {
        this.player = player;
        LocalDateTime now = LocalDateTime.now();
        this.timestamp = now.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
    }

    /**
     * Builds a description of the base replay event,
     * including the player and the timestamp.
     *
     * @return a formatted string with player and date information
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(player).append(" - Date: ").append(timestamp).append("\n");
        return builder.toString();
    }

    /**
     * Converts the base replay event data into its JSON representation.
     * <p>
     * The JSON object contains the serialized player and the timestamp.
     * </p>
     *
     * @return a {@link JSONObject} containing the base event data
     */
    @Override
    public JSONObject toJson() {
        JSONObject replayEventJson = new JSONObject();
        replayEventJson.put("player", this.player.toJson());
        replayEventJson.put("timestamp", this.timestamp);
        return replayEventJson;
    }

}
