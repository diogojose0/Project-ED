package Game.entities.corridor.events;

import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;
import Game.gameCore.gameDesign.GameUtilPrint;

import java.util.Random;


/**
 * Event that grants extra turns to a player.
 * <p>
 * When executed, the player receives a random number of extra turns
 * between 1 and {@value #MAX_TURNS}.
 * </p>
 */
public class GainTurnEvent extends Event {

    private static final String DEFAULT_MESSAGE = " - gain extra turns: ";
    private static final int MAX_TURNS = 2;
    private static final Random rand = new Random();

    /**
     * Executes the event, granting the player extra turns and updating
     * the event description.
     *
     * @param statePlayer the player state affected by this event
     * @param engine the game engine
     */
    @Override
    public void execute(IPlayerState statePlayer, IGameEngine engine) {
        GameUtilPrint.printEventGenerated();

        int amount = rand.nextInt(MAX_TURNS) + 1;
        this.setPlayer(statePlayer);
        statePlayer.addExtraTurns(amount);

        this.setDescription(statePlayer.getPlayer().getName() + DEFAULT_MESSAGE + amount);
    }

}
