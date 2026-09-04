package Game.entities.division.games.leverGame;

import Collections.exceptions.ElementNotFoundException;
import Game.api.division.leverGame.ILever;
import Game.api.division.leverGame.ILeverGame;
import Game.api.player.IPlayerState;
import Game.exceptions.division.LeverAlreadyPushedException;
import Game.exceptions.division.LeverNotFoundException;
import Game.gameCore.gameDesign.GameUtilInput;
import Game.gameCore.gameDesign.GameUtilPrint;
import customCollections.ExtendedArrayUnorderedList;

import java.util.Random;


/**
 * Implementation of {@link ILeverGame} that represents a mini-game
 * where the player must choose the correct lever among several options.
 */
public class LeverGame implements ILeverGame {

    /** Collection of levers available in this mini-game. */
    private ExtendedArrayUnorderedList<ILever> levers;
    /** Flag that indicates whether the mini-game has been solved. */
    private boolean solved;

    /**
     * Creates a new {@code LeverGame} with no levers and unsolved state.
     */
    public LeverGame() {
        levers = new ExtendedArrayUnorderedList<>();
        this.solved = false;
    }

    /**
     * Adds a lever to this mini-game.
     *
     * @param lever the lever to add
     */
    @Override
    public void addLever (ILever lever) {
        levers.addToRear(lever);
    }

    /**
     * Indicates whether this lever mini-game has been solved.
     *
     * @return {@code true} if solved, {@code false} otherwise
     */
    @Override
    public boolean isSolved() {
        return solved;
    }

    /**
     * Sets the solved state of this lever mini-game.
     *
     * @param solved {@code true} to mark as solved, {@code false} otherwise
     */
    @Override
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Starts the lever mini-game for the given player.
     * The player chooses a lever by index, and the result is evaluated.
     *
     * @param playerState the player attempting the lever mini-game
     * @return {@code true} if a correct lever is chosen, {@code false} otherwise
     */
    @Override
    public boolean start(IPlayerState playerState) {
        boolean found;
        int option;
        boolean isSuccessfully = false;

        GameUtilPrint.printLeverGame();
        do {
            found = true;
            if (playerState.getPlayer().isBot()) {
                option = this.startAsBot();
            } else {
                GameUtilPrint.printLevers(this.levers.iterator());
                option = GameUtilInput.leaverInput();
            }

            try {

                isSuccessfully = this.checkLevers(option);
            } catch (LeverNotFoundException e) {
                found = false;
                GameUtilPrint.error();
                GameUtilPrint.printException(e.getMessage());
            } catch (LeverAlreadyPushedException e) {

                if (!playerState.getPlayer().isBot()) {
                    GameUtilPrint.error();
                    GameUtilPrint.printException(e.getMessage());
                }
                found = false;
            }
        } while (!found);

        return isSuccessfully;
    }

    /**
     * Validates and triggers the lever at the given index.
     *
     * @param index the index of the lever to check
     * @return {@code true} if the lever is correct and opens a target division, {@code false} otherwise
     * @throws LeverNotFoundException if the lever index is invalid
     * @throws LeverAlreadyPushedException if the lever was already pushed before
     */
    private boolean checkLevers (int index) throws LeverNotFoundException, LeverAlreadyPushedException {
        ILever lever;
        try {
            lever = levers.getByIndex(index);
        } catch (ElementNotFoundException e) {
            throw new LeverNotFoundException();
        }

        if(lever.isPushed()) {
            throw new LeverAlreadyPushedException();
        }

        lever.push();

        if(lever.getTarget() != null) {
            this.solved = true;
            GameUtilPrint.leverSuccessfully(lever.getTarget().getName());
            return true;
        }

        GameUtilPrint.leverFailed();
        return false;
    }

    /**
     * Simulates a bot choosing a lever by picking a random index.
     *
     * @return the chosen lever index
     */
    private int startAsBot() {
        return new Random().nextInt(this.levers.size());
    }

    public ExtendedArrayUnorderedList<ILever> getLevers() {
        return levers;
    }

    /**
     * Returns a generic description of this lever mini-game.
     *
     * @return a constant description string
     */
    @Override
    public String toString() {
        return "YOU HAVE NOW ENTERED AN LEVER DIVISION - CHOOSE CORRECTLY TO ADVANCE!!!";
    }

}
