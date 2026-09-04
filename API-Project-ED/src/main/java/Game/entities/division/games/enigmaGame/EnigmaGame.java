package Game.entities.division.games.enigmaGame;

import Game.api.division.enigmaGame.IEnigma;
import Game.api.division.enigmaGame.IEnigmaGame;
import Game.api.division.enigmaGame.IEnigmaStrategy;
import Game.api.player.IPlayerState;
import Game.exceptions.division.NonEnigmaAvailableException;
import Game.gameCore.gameDesign.GameUtilInput;
import Game.gameCore.gameDesign.GameUtilPrint;

import java.util.Random;


/**
 * Implementation of {@link IEnigmaGame} that runs a question-and-answer mini-game
 * based on enigmas provided by an {@link IEnigmaStrategy}.
 */
public class EnigmaGame implements IEnigmaGame {

    /** Shared strategy used to supply enigmas for all enigma games. */
    private static IEnigmaStrategy strategy = new EnigmaStrategy();
    /** Flag that indicates whether this mini-game has been solved. */
    private boolean solved;

    /**
     * Creates a new {@code EnigmaGame} initially marked as unsolved.
     */
    public EnigmaGame() {
        this.solved = false;
    }

    /**
     * Starts the enigma mini-game for the given player.
     * If there is no available enigma, the method returns {@code true} by default.
     * For normal players the method reads input, and for bots it selects a random option.
     *
     * @param playerState the player state that is attempting the enigma
     * @return {@code true} if the enigma is answered correctly, {@code false} otherwise
     */
    @Override
    public boolean start(IPlayerState playerState) {
        IEnigma enigma;
        boolean optionValid;
        try {
            enigma = strategy.getEnigma();
        } catch (NonEnigmaAvailableException e) {
            return true;
        }

        int option;
        if (playerState.getPlayer().isBot()) {
            option = this.startAsBot(enigma);
        } else {
            GameUtilPrint.printEnigmaGame();
            GameUtilPrint.printEnigma(enigma.toString());
            do {
                option = GameUtilInput.enigmaInput();
                optionValid = enigma.validateOption(option);
                if (!optionValid) {
                    GameUtilPrint.wrongOption();
                }
            } while (!optionValid);
        }

        if (enigma.verifyAnswer(option)) {
            GameUtilPrint.enigmaCorrect();
            this.solved = true;
            return true;
        } else {
            GameUtilPrint.enigmaIncorrect();
            return false;
        }
    }

    /**
     * Indicates whether this enigma mini-game has been solved.
     *
     * @return {@code true} if solved, {@code false} otherwise
     */
    @Override
    public boolean isSolved() {
        return this.solved;
    }

    /**
     * Sets the solved state of this enigma mini-game.
     *
     * @param solved {@code true} to mark as solved, {@code false} otherwise
     */
    @Override
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Simulates a bot answering the enigma by choosing a random answer.
     *
     * @param enigma the enigma to answer
     * @return the chosen option index
     */
    private int startAsBot(IEnigma enigma) {
        Random rand = new Random();
        return rand.nextInt(enigma.getAnswers().size());
    }

}
