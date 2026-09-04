package Game.entities.corridor.events;

import Game.api.corridor.IEventFactory;
import Game.api.corridor.IEventStrategy;

import java.util.Random;


/**
 * Factory for randomly generating corridor events.
 * <p>
 * The {@code EventFactory} keeps a static list of available event strategies
 * and returns one at random when requested.
 * </p>
 */
public class EventFactory implements IEventFactory {

    /** List of available event strategies that can be generated. */
    private static final IEventStrategy[] AVAILABLE_STRATEGIES = {
            new LoseTurnEvent(),
            new MoveBackEvent(),
            new GainTurnEvent(),
            new ShufflePositionsEvent(),
            new SwapEvent()
    };

    /** Random number generator used to select events. */
    private final Random randomGenerator;

    /**
     * Creates a new {@code EventFactory} with its own random generator.
     */
    public EventFactory() {
        this.randomGenerator = new Random();
    }

    /**
     * Generates a random event strategy from the available set.
     *
     * @return a randomly chosen {@link IEventStrategy}
     */
    @Override
    public IEventStrategy generateEvent() {
        int randomIndex = this.randomGenerator.nextInt(AVAILABLE_STRATEGIES.length);
        IEventStrategy strategy = AVAILABLE_STRATEGIES[randomIndex];

        return strategy;
    }

}
