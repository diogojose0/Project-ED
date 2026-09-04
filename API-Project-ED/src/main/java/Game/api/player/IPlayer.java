package Game.api.player;

import org.json.simple.JSONObject;


/**
 * Contract for player data.
 * <p>
 * A player has a name, a flag indicating whether it is controlled by a bot,
 * and can be serialized to JSON.
 * </p>
 */
public interface IPlayer {

    /**
     * Returns the player's display name.
     *
     * @return the player name
     */
    String getName();

    /**
     * Indicates whether this player is controlled by a bot.
     *
     * @return {@code true} if the player is a bot, {@code false} if human
     */
    boolean isBot();

    /**
     * Converts this player into its JSON representation.
     *
     * @return a {@link JSONObject} containing the player data
     */
    JSONObject toJson();
}

