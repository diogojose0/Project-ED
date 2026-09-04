package Game.replays;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.replays.IReplay;
import Game.api.replays.IReplayEvent;
import Game.exceptions.replays.NullReplayEventException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * {@code Replay} is the default implementation of {@link IReplay}.
 * <p>
 * It stores all events that occurred during a single game session,
 * along with metadata such as a game id, timestamp and win description.
 * The replay can be printed in a human-readable format or exported as JSON.
 * </p>
 */
public class Replay implements IReplay {

    /** Static counter used to assign incremental ids to replays. */
    private static int nextID = 1;
    /** Unique identifier of this game replay. */
    private int gameId;
    /** Ordered list of events that occurred during the game. */
    private DoublyLinkedUnorderedList<IReplayEvent> history;
    /** Description of how the game was won (who/where). */
    private String winMoment;
    /** Timestamp indicating when the game finished. */
    private String timestamp;

    /**
     * Creates a new replay with an empty history and a generated game id.
     */
    public Replay() {
        this.history = new DoublyLinkedUnorderedList<>();
        this.gameId = nextID++;
    }

    /**
     * Creates a new replay with an empty history, a generated game id
     * and the provided timestamp and win moment.
     *
     * @param timestamp the timestamp to associate with this replay
     * @param winMoment the description of the winning moment
     */
    public Replay(String timestamp, String winMoment) {
        this.history = new DoublyLinkedUnorderedList<>();
        this.gameId = nextID++;
        this.winMoment = winMoment;
        this.timestamp = timestamp;
    }

    /**
     * Returns the timestamp associated with this replay.
     *
     * @return the timestamp string
     */
    @Override
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Updates the timestamp of this replay to the current local date and time.
     * <p>
     * The timestamp is formatted as {@code "dd/MM/yy HH:mm:ss"}.
     * </p>
     */
    @Override
    public void updateLocalTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        this.timestamp = now.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
    }

    /**
     * Sets the win moment description for this replay.
     *
     * @param winMoment a human-readable description of how the game was won
     */
    @Override
    public void setWinMoment(String winMoment) {
        this.winMoment = winMoment;
    }

    /**
     * Adds a new event to the replay history.
     *
     * @param event the event to add
     * @throws NullReplayEventException if the given event is {@code null}
     */
    @Override
    public void add(IReplayEvent event) throws NullReplayEventException {
        if (event == null) {
            throw new NullReplayEventException();
        }
        history.addToRear(event);
    }

    /**
     * Returns the unique game id for this replay.
     *
     * @return the game id
     */
    @Override
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Converts this replay into its JSON representation.
     * <p>
     * The JSON object contains:
     * gameId, timestamp, winMoment and an array of serialized events.
     * </p>
     *
     * @return a {@link JSONObject} representing this replay
     */
    public JSONObject toJson() {
        JSONObject replayJson = new JSONObject();

        replayJson.put("gameId", this.gameId);
        replayJson.put("timestamp", this.timestamp);
        replayJson.put("winMoment", this.winMoment);

        JSONArray historyArray = new JSONArray();
        for (IReplayEvent event : this.history) {
            historyArray.add(event.toJson());
        }

        replayJson.put("history", historyArray);

        return replayJson;
    }

    /**
     * Builds a representation of the replay, including all events and the win moment.
     *
     * @return a formatted string describing the replay
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Replay - ").append(gameId);
        builder.append("\n------------------------------------------------\n");
        for (IReplayEvent event : history) {
            builder.append(event.toString());
        }
        builder.append(winMoment);
        builder.append("\n------------------------------------------------");
        return builder.toString();
    }

}
