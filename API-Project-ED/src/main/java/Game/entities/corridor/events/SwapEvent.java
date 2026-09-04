package Game.entities.corridor.events;

import Collections.exceptions.ElementNotFoundException;
import Game.api.division.IDivision;
import Game.api.engine.IGameEngine;
import Game.api.player.IPlayerState;
import Game.gameCore.gameDesign.GameUtilInput;
import Game.gameCore.gameDesign.GameUtilPrint;
import customCollections.ExtendedArrayUnorderedList;

import java.util.Random;


/**
 * Event that swaps the positions of two players.
 * <p>
 * The triggering player swaps positions with another valid target.
 * For bots, the target is chosen randomly; for normal players, the
 * target is chosen via user input.
 * </p>
 */
public class SwapEvent extends Event {

    private static final String DEFAULT_MESSAGE = " swapped with ";

    /**
     * Creates a {@code SwapEvent} with the default description.
     */
    public SwapEvent() {
        this.setDescription(DEFAULT_MESSAGE);
    }

    /**
     * Executes the event by selecting a target player and swapping
     * their positions with the triggering player.
     *
     * @param statePlayer the player that triggered the event
     * @param engine the game engine used to obtain valid targets
     */
    @Override
    public void execute(IPlayerState statePlayer, IGameEngine engine) {
        this.setPlayer(statePlayer);
        IPlayerState otherPlayer = null;

        GameUtilPrint.printEventGenerated();

        ExtendedArrayUnorderedList<IPlayerState> validTargets = engine.getValidPlayerTargets(statePlayer);

        if (statePlayer.getPlayer().isBot()) {
            this.executeAsBot(statePlayer, validTargets);
        }

        GameUtilPrint.printPlayers(validTargets.iterator());
        boolean found;

        do {
            found = true;
            try {
                otherPlayer = validTargets.getByIndex(GameUtilInput.possiblePlayersInput());
            } catch (ElementNotFoundException e) {
                GameUtilPrint.invalidPlayerChosen();
                found = false;
            }
        } while(!found);

        this.swapPositions(statePlayer, otherPlayer);
    }

    /**
     * Executes the swap event in bot mode, choosing a random target player.
     *
     * @param statePlayer the bot player that triggered the event
     * @param validTargets list of valid target player states
     */
    private void executeAsBot(IPlayerState statePlayer, ExtendedArrayUnorderedList<IPlayerState> validTargets) {
        boolean found;
        IPlayerState otherPlayer = null;
        do {
            found = true;
            try {
                otherPlayer = validTargets.getByIndex(new Random().nextInt(validTargets.size()));
            } catch (ElementNotFoundException e) {
                found = false;
            }
        } while(!found);

        this.swapPositions(statePlayer, otherPlayer);
    }

    /**
     * Swaps the current divisions of the two given players and updates
     * the event description accordingly.
     *
     * @param statePlayer the triggering player
     * @param otherPlayer the target player
     */
    private void swapPositions(IPlayerState statePlayer, IPlayerState otherPlayer) {
        IDivision divisionTemp = statePlayer.getMovementPlayer().getDivision();

        statePlayer.getMovementPlayer().clearHistory();
        statePlayer.getMovementPlayer().setCurrentDivision(otherPlayer.getMovementPlayer().getDivision());

        otherPlayer.getMovementPlayer().clearHistory();
        otherPlayer.getMovementPlayer().setCurrentDivision(divisionTemp);

        this.setDescription(statePlayer.getPlayer().getName() + DEFAULT_MESSAGE + otherPlayer.getPlayer().getName());
    }

}
