package Game.replays;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.replays.IReplay;
import Game.api.replays.IReplaysManagement;
import Game.exceptions.replays.NullReplayException;
import Game.exceptions.replays.ReplayNotFoundException;
import org.json.simple.JSONArray;

import java.util.Iterator;


/**
 * {@code ReplaysManagement} is the default implementation of {@link IReplaysManagement}.
 * <p>
 * It stores a collection of replays, allows accessing them by id and can
 * export all stored replays to a JSON array.
 * </p>
 */
public class ReplaysManagement implements IReplaysManagement {

    /**
     * Internal list that holds all registered replays.
     */
    private DoublyLinkedUnorderedList<IReplay> replays;

    /**
     * Creates a new {@code ReplaysManagement} instance with an empty replay list.
     */
    public ReplaysManagement() {
        this.replays = new DoublyLinkedUnorderedList<>();
    }

    /**
     * Adds a replay to the internal collection.
     *
     * @param replay the replay to add
     * @throws NullReplayException if {@code replay} is {@code null}
     */
    @Override
    public void add(IReplay replay) throws NullReplayException {
        if (replay == null) {
            throw new NullReplayException();
        }
        replays.addToRear(replay);
    }

    /**
     * Returns the list of all stored replays.
     *
     * @return a {@link DoublyLinkedUnorderedList} containing all replays
     */
    @Override
    public DoublyLinkedUnorderedList<IReplay> getReplays() {
        return replays;
    }

    /**
     * Retrieves a replay by its game id.
     *
     * @param id the game id of the replay to retrieve
     * @return the replay with the given id
     * @throws ReplayNotFoundException if no replay with the given id exists
     */
    @Override
    public IReplay get(int id) throws ReplayNotFoundException {
        Iterator<IReplay> it = replays.iterator();
        while (it.hasNext()) {
            IReplay replay = it.next();
            if(replay.getGameId() == id) {
                return replay;
            }
        }
        throw new ReplayNotFoundException();
    }

    /**
     * Converts all stored replays into a JSON array.
     *
     * @return a {@link JSONArray} containing the JSON representation of all replays
     */
    @Override
    public JSONArray toJson() {
        JSONArray replaysArray = new JSONArray();

        for (IReplay replay : replays) {
            replaysArray.add(replay.toJson());
        }

        return replaysArray;
    }

    /**
     * Builds a list of replays, showing each replay id
     * and its timestamp on a separate line.
     *
     * @return a formatted string listing all replays
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (IReplay replay : replays) {
            builder.append(replay.getGameId()).append(" - ").append(replay.getTimestamp()).append("\n");
        }
        return builder.toString();
    }

}
