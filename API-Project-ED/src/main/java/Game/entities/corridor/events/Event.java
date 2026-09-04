package Game.entities.corridor.events;

import Game.api.corridor.IEventStrategy;
import Game.api.player.IPlayerState;


/**
 * Abstract base implementation of {@link IEventStrategy} for corridor events.
 * <p>
 * Stores a description of the event and the affected player state.
 * Concrete subclasses only need to implement the {@code execute} method.
 * </p>
 */
public abstract class Event implements IEventStrategy {

    /** Description of the event outcome. */
    private String description;
    /** Player affected by this event. */
    private IPlayerState playerState;

    /**
     * Returns the description of this event.
     *
     * @return a description of the event
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this event.
     *
     * @param description the description to assign
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Associates a player state with this event.
     *
     * @param player the player state affected by this event
     */
    @Override
    public void setPlayer(IPlayerState player) {
        this.playerState = player;
    }

    /**
     * Returns the player state associated with this event.
     *
     * @return the affected player state
     */
    @Override
    public IPlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Returns the string representation of the event, which is its description.
     *
     * @return the event description as a string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.description);
        return builder.toString();
    }

}
