package Game.api.corridor;

import org.json.simple.JSONObject;


/**
 * Contract for corridors connecting two divisions in the maze.
 * <p>
 * A corridor can generate an event when a player moves through it and can be
 * serialized to JSON as part of the maze and replay/export structures.
 * </p>
 */
public interface ICorridor {

    /**
     * Generates the event associated with traversing this corridor.
     * <p>
     * This method is invoked by the game engine when a player moves between
     * the two divisions connected by this corridor. The implementation decides
     * whether an event occurs and which event strategy is returned.
     * </p>
     *
     * @return an {@link IEventStrategy} representing the event to be applied to
     * the player, or {@code null} if no event occurs for this traversal
     */
    IEventStrategy generateEvent();

    /**
     * Returns the corridor's display name.
     *
     * @return the corridor name
     */
    String getName();

    /**
     * Converts this corridor into its JSON representation.
     * <p>
     * The resulting JSON is used when exporting the maze layout or building replays.
     * </p>
     *
     * @return a {@link JSONObject} containing the serialized data for this corridor
     */
    JSONObject toJson();
}
