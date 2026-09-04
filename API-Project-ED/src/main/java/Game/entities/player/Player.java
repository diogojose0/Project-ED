package Game.entities.player;

import Game.api.player.IPlayer;
import org.json.simple.JSONObject;


/**
 * Implementation of {@link IPlayer} that represents a human or bot player.
 * <p>
 * Each player has a unique id, a name and a flag indicating whether it is a bot.
 * </p>
 */
public class Player implements IPlayer {

    /** Static counter used to assign unique identifiers to players. */
    private static int nextID = 1;

    /** Unique identifier of this player instance. */
    private int id;

    /** Display name of the player. */
    private String name;

    /** Flag indicating whether this player is controlled by a bot. */
    private boolean isBot;


    /**
     * Creates a new {@code Player} with a predefined id.
     *
     * @param name the player name
     * @param bot {@code true} if the player is a bot
     * @param id the unique identifier of the player
     */
    public Player(String name, boolean bot, int id) {
        this.name = name;
        this.isBot = bot;
        this.id = id;
    }

    /**
     * Creates a new {@code Player} with an automatically generated id.
     *
     * @param name the player name
     * @param bot  {@code true} if the player is a bot
     */
    public Player(String name, boolean bot) {
        this(name, bot, nextID++);
    }

    /**
     * Returns the name of the player.
     *
     * @return player name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Indicates whether this player is controlled by a bot.
     *
     * @return {@code true} if this player is a bot, {@code false} otherwise
     */
    @Override
    public boolean isBot() {
        return isBot;
    }

    /**
     * Returns a string representation of the player, indicating whether it is a bot
     * or a human player and showing its name.
     *
     * @return formatted player description
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(isBot ? " BOT - " : " Player - ").append(this.name);
        return builder.toString();
    }

    /**
     * Converts this player into its JSON representation, including id, name and bot flag.
     *
     * @return a {@link JSONObject} describing this player
     */
    @Override
    public JSONObject toJson() {
        JSONObject playerJson = new JSONObject();

        playerJson.put("id", this.id);
        playerJson.put("name", this.name);
        playerJson.put("isBot", this.isBot);

        return playerJson;
    }

}
