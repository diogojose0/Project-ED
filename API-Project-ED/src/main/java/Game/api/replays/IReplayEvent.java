package Game.api.replays;

import org.json.simple.JSONObject;


/**
 * Contract for a single event recorded in a replay.
 * <p>
 * Each event captures a game action or state change that should be
 * persisted and later reconstructed when the replay is played back.
 * </p>
 */
public interface IReplayEvent {

    /**
     * Converts this replay event into its JSON representation.
     *
     * @return a {@link JSONObject} containing the event data
     */
    JSONObject toJson();
}

