package Game.entities.corridor.events;

import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;
import Game.gameCore.gameDesign.GameUtilPrint;

import java.util.Random;


/**
 * Event that moves a player back along their movement history.
 * <p>
 * When executed, the player moves back a random number of steps
 * between 1 and {@value #MAX_MOVES}.
 * </p>
 */
public class MoveBackEvent extends Event {

    private static final String DEFAULT_MESSAGE = " - move back: ";
    private static final int MAX_MOVES = 2;
    private static final Random rand = new Random();

    /**
     * Executes the event, moving the player back in their movement history
     * and updating the event description.
     *
     * @param statePlayer the player state affected by this event
     * @param engine the game engine
     */
    @Override
    public void execute(IPlayerState statePlayer, IGameEngine engine) {
        GameUtilPrint.printEventGenerated();

        int times = rand.nextInt(MAX_MOVES) + 1;
        this.setPlayer(statePlayer);
        statePlayer.getMovementPlayer().moveBack(times);

        this.setDescription(statePlayer.getPlayer().getName() + DEFAULT_MESSAGE + times);
    }

}
