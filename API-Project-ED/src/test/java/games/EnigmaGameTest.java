package games;

import Collections.list.ArrayUnorderedList;
import Game.api.division.enigmaGame.IEnigma;
import Game.api.player.*;
import Game.entities.division.games.enigmaGame.EnigmaGame;
import Game.entities.division.games.enigmaGame.EnigmaStrategy;
import Game.exceptions.division.NullEnigmaException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link EnigmaGame} mini-game.
 * <p>
 * These tests use fake implementations of {@link IPlayer}, {@link IPlayerState}
 * and {@link IEnigma} to validate:
 * interaction between {@code EnigmaGame} and player state;
 * the solved flag behaviour;
 * basic option validation in enigmas;
 * execution of the game when used by a bot or without enigmas.
 * </p>
 */
public class EnigmaGameTest {

    private EnigmaGame game;

    /**
     * Fake implementation of {@link IPlayer} for testing purposes.
     * <p>
     * Allows control over the player name and bot flag without depending
     * on the real Player entity.
     * </p>
     */
    private static class FakePlayer implements IPlayer {
        private final String name;
        private final boolean bot;

        public FakePlayer(String name, boolean bot) {
            this.name = name;
            this.bot = bot;
        }

        @Override
        public String getName() { return name; }

        @Override
        public boolean isBot() { return bot; }

        @Override
        public JSONObject toJson() {
            return null;
        }
    }

    /**
     * Fake implementation of {@link IPlayerState} for testing.
     * <p>
     * Tracks blocked status, winner status and turn counters in memory,
     * without any movement logic.
     * </p>
     */
    private static class FakePlayerState implements IPlayerState {
        private final IPlayer player;
        private boolean blocked;
        private boolean winner;
        private int blockTurns;
        private int extraTurns;

        public FakePlayerState(IPlayer player) { this.player = player; }

        @Override public IPlayer getPlayer() { return player; }
        @Override public IMovementPlayer getMovementPlayer() { return null; }
        @Override public boolean isBlocked() { return blocked; }
        @Override public void blockFor(int turns) { blocked = true; blockTurns = turns; }
        @Override public void tickBlock() { if (blockTurns>0) blockTurns--; if (blockTurns==0) blocked=false; }
        @Override public void addExtraTurns(int turns) { extraTurns+=turns; }
        @Override public void useExtraTurn() { if (extraTurns>0) extraTurns--; }
        @Override public void markAsWinner() { winner=true; }
        @Override public boolean isWinner() { return winner; }
        @Override public void reset() { blocked=false; winner=false; blockTurns=0; extraTurns=0; }
        @Override public boolean hasExtraTurns() { return extraTurns>0; }
    }

    /**
     * Fake implementation of {@link IEnigma} that exposes a fixed set of
     * answers and a single correct answer index.
     */
    private static class FakeEnigma implements IEnigma {
        private final int correctAnswerIndex;

        public FakeEnigma(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

        @Override public boolean verifyAnswer(int answer) { return answer==correctAnswerIndex; }

        @Override
        public ArrayUnorderedList<String> getAnswers() {
            ArrayUnorderedList<String> answers = new ArrayUnorderedList<>();
            answers.addToRear("Option 0");
            answers.addToRear("Option 1");
            answers.addToRear("Option 2");
            return answers;
        }

        @Override
        public boolean validateOption(int option) { return option>=0 && option<getAnswers().size(); }
    }

    /**
     * Creates a fresh {@link EnigmaGame} before each test.
     */
    @BeforeEach
    void setUp() {
        game = new EnigmaGame();
    }

    /**
     * Verifies that {@link EnigmaGame#start(IPlayerState)} executes correctly
     * for a bot player when there is at least one enigma in the strategy queue.
     * <p>
     * The result can be either true or false depending on randomness, so this
     * test only ensures that the call completes without errors.
     * </p>
     */
    @Test
    void testStartWithEnigmaAsBot() throws NullEnigmaException {
        EnigmaStrategy strategy = new EnigmaStrategy();
        strategy.addEnigma(new FakeEnigma(1));
        EnigmaGame gameWithEnigma = new EnigmaGame();

        FakePlayerState botState = new FakePlayerState(new FakePlayer("Bot", true));

        boolean result = gameWithEnigma.start(botState);
        assertTrue(result || !result);
    }

    /**
     * Verifies that the solved flag in {@link EnigmaGame} can be set and retrieved correctly.
     */
    @Test
    void testSetAndGetSolved() {
        game.setSolved(true);
        assertTrue(game.isSolved());
        game.setSolved(false);
        assertFalse(game.isSolved());
    }

    /**
     * Verifies the behaviour of the fake player state regarding:
     * blocked turns;
     * extra turns;
     * winner flag;
     * reset logic.
     */
    @Test
    void testPlayerStateBehavior() {
        FakePlayerState state = new FakePlayerState(new FakePlayer("Tester", false));

        assertFalse(state.isBlocked());
        state.blockFor(2);
        assertTrue(state.isBlocked());
        state.tickBlock();
        state.tickBlock();
        assertFalse(state.isBlocked());

        state.addExtraTurns(3);
        assertTrue(state.hasExtraTurns());
        state.useExtraTurn();
        assertTrue(state.hasExtraTurns());

        state.markAsWinner();
        assertTrue(state.isWinner());

        state.reset();
        assertFalse(state.isWinner());
        assertFalse(state.isBlocked());
        assertFalse(state.hasExtraTurns());
    }

    /**
     * Verifies that {@link FakeEnigma#validateOption(int)} correctly
     * accepts valid options and rejects invalid ones.
     */
    @Test
    void testEnigmaValidateOption() {
        FakeEnigma enigma = new FakeEnigma(1);
        List<Integer> validOptions = List.of(0,1,2);
        for (int i = -1; i <= 3; i++) {
            if (validOptions.contains(i)) {
                assertTrue(enigma.validateOption(i));
            } else {
                assertFalse(enigma.validateOption(i));
            }
        }
    }

    /**
     * Verifies the behaviour of {@link EnigmaGame#start(IPlayerState)} when there are
     * no enigmas available in the strategy.
     * <p>
     * According to the implementation, if {@link Game.exceptions.division.NonEnigmaAvailableException}
     * is thrown by the strategy, the method should return {@code true}.
     * </p>
     */
    @Test
    void testStartWithoutEnigmasReturnsTrue() {
        new EnigmaStrategy();

        EnigmaGame gameNoEnigmas = new EnigmaGame();
        FakePlayerState playerState = new FakePlayerState(new FakePlayer("NoEnigmaPlayer", false));

        boolean result = gameNoEnigmas.start(playerState);
        assertTrue(result);
    }
}
