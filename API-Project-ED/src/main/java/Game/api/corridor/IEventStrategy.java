package Game.api.corridor;

import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;


/**
 * Contract for strategies that implement corridor events.
 * <p>
 * Implementations of this interface encapsulate the behavior of a single
 * event triggered when a player traverses a corridor, such as gaining extra
 * turns, losing turns, swapping positions or changing player state in any
 * other way. The game engine invokes the event strategy to apply its effect.
 * </p>
 */
public interface IEventStrategy {

    /**
     * Applies the effect of this event to the given player within the provided game engine.
     * <p>
     * This method is called by the game engine when the event is triggered on a corridor.
     * The implementation may update the {@link IPlayerState}, interact with the {@link IGameEngine}, or both.
     * </p>
     *
     * @param state the player state affected by the event
     * @param engine the current game engine context
     */
    void execute(IPlayerState state, IGameEngine engine);

    /**
     * Returns a description of this event.
     * <p>
     * The description is used for logging and for displaying messages in the game UI after the event is executed.
     * </p>
     *
     * @return the event description
     */
    String getDescription();

    /**
     * Associates this event with a specific player state.
     * <p>
     * Implementations use this reference to keep track of the affected player
     * during and after execution.
     * </p>
     *
     * @param player the player state to associate with this event
     */
    void setPlayer(IPlayerState player);

    /**
     * Returns the player state currently associated with this event.
     *
     * @return the player affected by this event, or {@code null} if none is set
     */
    IPlayerState getPlayerState();

    /**
     * Sets the description for this event.
     * <p>
     * Implementations typically call this inside {@link #execute(IPlayerState, IGameEngine)}
     * after determining the concrete effect of the event.
     * </p>
     *
     * @param description the description to assign to this event
     */
    void setDescription(String description);
}
