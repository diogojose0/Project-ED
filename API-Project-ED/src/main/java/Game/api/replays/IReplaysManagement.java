package Game.api.replays;

import Collections.list.DoublyLinkedUnorderedList;
import Game.api.data.JsonExportable;
import Game.exceptions.replays.NullReplayException;
import Game.exceptions.replays.ReplayNotFoundException;


/**
 * Contract for managing multiple game replays.
 * <p>
 * This component stores a collection of {@link IReplay} instances,
 * provides access by id and exports them to JSON.
 * </p>
 */
public interface IReplaysManagement extends JsonExportable {

    /**
     * Adds a replay to the collection.
     *
     * @param replay the replay to add
     * @throws NullReplayException if {@code replay} is {@code null}
     */
    void add(IReplay replay) throws NullReplayException;

    /**
     * Retrieves a replay by its identifier.
     *
     * @param id the replay id
     * @return the matching replay
     * @throws ReplayNotFoundException if no replay with the given id exists
     */
    IReplay get(int id) throws ReplayNotFoundException;

    /**
     * Returns the list of all stored replays.
     *
     * @return a {@link DoublyLinkedUnorderedList} of replays
     */
    DoublyLinkedUnorderedList<IReplay> getReplays();
}

