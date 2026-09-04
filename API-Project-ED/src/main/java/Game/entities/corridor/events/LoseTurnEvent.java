package Game.entities.corridor.events;

import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;
import Game.gameCore.gameDesign.GameUtilPrint;

import java.util.Random;


/**
 * Event that blocks a player for a number of turns.
 * <p>
 * When executed, the player is blocked for a random number of turns
 * between 1 and {@value #MAX_TURNS}.
 * </p>
 */
public class LoseTurnEvent extends Event {

    private static final String DEFAULT_MESSAGE = " - blocked turns: ";
    private static final int MAX_TURNS = 2;
    private static final Random rand = new Random();

    /**
     * Executes the event, blocking the player for a number of turns
     * and updating the event description.
     *
     * @param statePlayer the player state affected by this event
     * @param engine the game engine
     */
    @Override
    public void execute(IPlayerState statePlayer, IGameEngine engine) {
        GameUtilPrint.printEventGenerated();

        int quantity = rand.nextInt(MAX_TURNS) + 1;
        this.setPlayer(statePlayer);
        statePlayer.blockFor(quantity);

        this.setDescription(
                statePlayer.getPlayer().getName() + DEFAULT_MESSAGE + quantity
        );
    }

}
