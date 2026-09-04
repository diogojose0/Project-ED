package Game.api.division.leverGame;

import Game.api.division.IDivision;
import org.json.simple.JSONObject;


/**
 * Contract for levers used in lever-based mini-games.
 * <p>
 * A lever can be pushed, remembers its state, and may be linked to a target
 * division that becomes relevant when the lever is activated.
 * </p>
 */
public interface ILever {

    /**
     * Activates this lever.
     * <p>
     * The concrete effect of pushing the lever is defined by the implementation.
     * </p>
     */
    void push();

    /**
     * Indicates whether this lever has already been pushed.
     *
     * @return {@code true} if the lever is in the pushed state,
     *         {@code false} otherwise
     */
    boolean isPushed();

    /**
     * Returns the target division associated with this lever.
     * <p>
     * The target represents a division that is made accessible when the lever is pushed.
     * </p>
     *
     * @return the target {@link IDivision}, or {@code null} if none is set
     */
    IDivision getTarget();

    /**
     * Sets the target division for this lever.
     * <p>
     * The target represents a division that is made accessible when the lever is pushed.
     * </p>
     *
     * @param target the target {@link IDivision} to associate with this lever
     */
    void setTarget(IDivision target);

    /**
     * Converts this lever into its JSON representation.
     * <p>
     * The resulting JSON is used when exporting the maze layout or building replays.
     * </p>
     *
     * @return a {@link JSONObject} containing the serialized data for this lever
     */
    JSONObject toJson();
}

