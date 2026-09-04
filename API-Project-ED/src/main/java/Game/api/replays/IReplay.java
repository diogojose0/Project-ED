package Game.api.replays;

import Game.exceptions.replays.NullReplayEventException;
import org.json.simple.JSONObject;


/**
 * Contract for a single game replay.
 * <p>
 * A replay records the sequence of events for one game session, along with
 * data such as game id, win moment and timestamp.
 * </p>
 */
public interface IReplay {

    /**
     * Adds a new event to this replay.
     *
     * @param event the replay event to add
     * @throws NullReplayEventException if {@code event} is {@code null}
     */
    void add(IReplayEvent event) throws NullReplayEventException;

    /**
     * Returns the id of the game associated with this replay.
     *
     * @return the game id
     */
    int getGameId();

    /**
     * Sets the win moment description
     *
     * @param winMoment the win moment description
     */
    void setWinMoment(String winMoment);

    /**
     * Updates the timestamp of this replay to the current local date and time.
     */
    void updateLocalTimestamp();

    /**
     * Returns the timestamp associated with this replay.
     *
     * @return the timestamp as a string
     */
    String getTimestamp();

    /**
     * Converts this replay into its JSON representation.
     *
     * @return a {@link JSONObject} containing replay data and events
     */
    JSONObject toJson();
}

