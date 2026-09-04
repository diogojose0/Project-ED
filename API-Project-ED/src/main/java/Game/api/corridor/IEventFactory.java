package Game.api.corridor;


/**
 * Contract for factories that create corridor events.
 * <p>
 * Implementations of this interface are used by corridors to obtain new
 * {@link IEventStrategy} instances whenever a player traverses a corridor.
 * The factory decides whether an event is created and which concrete event
 * should be generated according to the game rules.
 * </p>
 */
public interface IEventFactory {

    /**
     * Creates a new event to be triggered on a corridor traversal.
     * <p>
     * This method is called by the corridor when a player moves through it.
     * The returned event is then executed by the game engine.
     * </p>
     *
     * @return an {@link IEventStrategy} representing the event to apply, or {@code null} if no event should occur
     */
    IEventStrategy generateEvent();
}
