package Game.replays;

import Game.api.corridor.ICorridor;
import Game.api.player.IPlayer;
import org.json.simple.JSONObject;


/**
 * Represents a replay event related to a corridor traversal.
 * <p>
 * A {@code CorridorEvent} stores the player involved, the corridor
 * that was used and a textual description of the event that occurred
 * when the player passed through that corridor.
 * </p>
 */
public class CorridorEvent extends ReplayEvent {

    /** Corridor where the event took place. */
    private ICorridor corridor;
    /** Description of the corridor event. */
    private String stringEvent;

    /**
     * Creates a {@code CorridorEvent} with the given player.
     *
     * @param player the player associated with this event
     */
    public CorridorEvent(IPlayer player) {
        super(player);
    }

    /**
     * Creates a {@code CorridorEvent} with the given player, description and corridor.
     *
     * @param player the player associated with this event
     * @param event the textual description of the event
     * @param corridor the corridor where the event occurred
     */
    public CorridorEvent(IPlayer player, String event, ICorridor corridor) {
        this(player);
        this.stringEvent = event;
        this.corridor = corridor;
    }

    /**
     * Sets the corridor associated with this event.
     *
     * @param corridor the corridor to assign
     */
    public void setCorridor(ICorridor corridor) {
        this.corridor = corridor;
    }

    /**
     * Sets the textual description of this event.
     *
     * @param stringEvent the description to assign
     */
    public void setStringEvent(String stringEvent) {
        this.stringEvent = stringEvent;
    }

    /**
     * Builds a representation of this corridor event,
     * including the base {@link ReplayEvent} information, the event
     * description and the corridor used.
     *
     * @return a formatted string describing the corridor event
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(stringEvent).append(" -> ").append(corridor.toString());
        builder.append("\n------------------------------------------------\n");
        return builder.toString();
    }

    /**
     * Converts this corridor event into its JSON representation.
     * <p>
     * {@code "corridor"} – the corridor serialized to JSON;
     * {@code "event"} – the textual event description.
     * </p>
     *
     * @return a {@link JSONObject} containing this corridor event data
     */
    public JSONObject toJson() {
        JSONObject corridorEventJson;

        corridorEventJson = super.toJson();
        corridorEventJson.put("corridor", this.corridor.toJson());
        corridorEventJson.put("event", this.stringEvent);

        return corridorEventJson;
    }

}
