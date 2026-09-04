package Game.entities.corridor;

import Game.api.corridor.ICorridor;
import Game.api.corridor.IEventFactory;
import Game.api.corridor.IEventStrategy;
import Game.entities.corridor.events.EventFactory;
import org.json.simple.JSONObject;

import java.util.Random;


/**
 * Default implementation of a corridor in the maze.
 * <p>
 * A {@code Corridor} has a name and can optionally generate a random
 * event when traversed, based on a probability.
 * </p>
 */
public class Corridor implements ICorridor {

    /** Shared event factory used to create random events. */
    private static final IEventFactory eventFactory = new EventFactory();
    /** Name of this corridor. */
    private String name;

    /**
     * Creates a new corridor with the given name.
     *
     * @param name the corridor name
     */
    public Corridor(String name) {
        this.name = name;
    }

    /**
     * Generates a random event for this corridor with a fixed probability.
     * <p>
     * Currently there is a 30% chance to generate an event; otherwise,
     * this method returns {@code null}.
     * </p>
     *
     * @return a generated {@link IEventStrategy} or {@code null} if no event occurs
     */
    @Override
    public IEventStrategy generateEvent() {
        Random rand = new Random();
        IEventStrategy generatedEvent = null;

        int probability = rand.nextInt(101);
        if(probability < 30) {
            generatedEvent = eventFactory.generateEvent();
        }

        return generatedEvent;
    }

    /**
     * Returns the name of this corridor.
     *
     * @return the corridor name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of this corridor, which is its name.
     *
     * @return the corridor name
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Converts this corridor into its JSON representation.
     *
     * @return a {@link JSONObject} containing the corridor name
     */
    @Override
    public JSONObject toJson() {
        JSONObject corridorJson = new JSONObject();

        corridorJson.put("name", this.name);

        return corridorJson;
    }

}
