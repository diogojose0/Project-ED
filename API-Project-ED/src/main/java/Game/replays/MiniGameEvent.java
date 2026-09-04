package Game.replays;

import Game.api.division.IDivision;
import Game.api.division.enigmaGame.IEnigmaGame;
import Game.api.division.leverGame.ILeverGame;
import Game.api.player.IPlayer;
import org.json.simple.JSONObject;


/**
 * Represents a replay event related to a mini-game.
 * <p>
 * A {@code MiniGameEvent} records that a player has won a mini-game
 * in a specific division (either a lever game or an enigma game).
 * </p>
 */
public class MiniGameEvent extends ReplayEvent {

    /** Division where the mini-game took place. */
    private IDivision divisionOfMiniGame;

    /**
     * Creates a {@code MiniGameEvent} with the given player.
     *
     * @param player the player associated with this mini-game event
     */
    public MiniGameEvent(IPlayer player) {
        super(player);
    }

    /**
     * Creates a {@code MiniGameEvent} with the given player and division.
     *
     * @param player the player associated with this mini-game event
     * @param divisionOfMiniGame the division where the mini-game was won
     */
    public MiniGameEvent(IPlayer player, IDivision divisionOfMiniGame) {
        this(player);
        this.divisionOfMiniGame = divisionOfMiniGame;
    }

    /**
     * Sets the division where the mini-game took place.
     *
     * @param divisionOfMiniGame the division to associate with this event
     */
    public void setDivisionOfMiniGame(IDivision divisionOfMiniGame) {
        this.divisionOfMiniGame = divisionOfMiniGame;
    }

    /**
     * Builds a description of this mini-game event.
     * <p>
     * The message indicates whether the player won a lever game or an enigma game,
     * followed by the division information.
     * </p>
     *
     * @return a formatted string describing the mini-game event
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (divisionOfMiniGame.getMiniGame() instanceof ILeverGame) {
            builder.append("Won the lever game!");
        } else if (divisionOfMiniGame.getMiniGame() instanceof IEnigmaGame) {
            builder.append("Won the enigma game!");
        }
        builder.append(" - Division -> ").append(divisionOfMiniGame.toString());
        builder.append("\n------------------------------------------------\n");
        return builder.toString();
    }

    /**
     * Converts this mini-game event into its JSON representation.
     * <p>
     * The JSON includes the base replay event data and the division where
     * the mini-game was won.
     * </p>
     *
     * @return a {@link JSONObject} containing this mini-game event data
     */
    public JSONObject toJson() {
        JSONObject miniGameEventJson;

        miniGameEventJson = super.toJson();
        miniGameEventJson.put("division", this.divisionOfMiniGame.toJson());

        return miniGameEventJson;
    }

}
