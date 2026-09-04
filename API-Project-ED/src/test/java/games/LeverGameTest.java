package games;

import Game.api.division.IDivision;
import Game.api.division.leverGame.ILeverGame;
import Game.api.player.IPlayer;
import Game.api.player.IPlayerState;
import Game.entities.division.games.leverGame.Lever;
import Game.entities.division.games.leverGame.LeverGame;
import Game.exceptions.division.LeverAlreadyPushedException;
import Game.exceptions.division.LeverNotFoundException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Unit tests for the {@link LeverGame} mini-game.
 * <p>
 * These tests validate:
 * success and failure paths when starting the game with a bot player;
 * handling of multiple levers without throwing exceptions;
 * correct behaviour of the solved flag;
 * exception logic in the private {@code checkLevers} method;
 * basic behaviour of {@link LeverGame#getLevers()} and {@link LeverGame#toString()}.
 * </p>
 */
public class LeverGameTest {

    /**
     * Minimal fake implementation of {@link IDivision} used as lever targets.
     */
    private static class FakeDivision implements IDivision {

        private final String name;
        public FakeDivision(String name) { this.name = name; }

        @Override public int getId() { return 0; }
        @Override public void startMiniGame(IPlayerState playerState) {}
        @Override public Game.api.division.IMiniGame getMiniGame() { return null; }
        @Override public void setMiniGame(Game.api.division.IMiniGame miniGame) {}
        @Override public String getName() { return name; }
        @Override public boolean isTreasureRoom() { return false; }
        @Override public boolean isEntryPoint() { return false; }
        @Override public JSONObject toJson() {
            return null;
        }
    }

    /**
     * Minimal fake implementation of {@link IPlayer} with configurable name and bot flag.
     */
    private static class FakePlayer implements IPlayer {
        private final boolean bot;
        private final String name;
        public FakePlayer(String name, boolean bot) { this.name = name; this.bot = bot; }
        @Override public String getName() { return name; }
        @Override public boolean isBot() { return bot; }
        @Override public JSONObject toJson() {
            return null;
        }
    }

    /**
     * Minimal fake implementation of {@link IPlayerState} that only exposes the player.
     */
    private static class FakePlayerState implements IPlayerState {
        private final IPlayer player;
        public FakePlayerState(IPlayer player) { this.player = player; }

        @Override public IPlayer getPlayer() { return player; }
        @Override public Game.api.player.IMovementPlayer getMovementPlayer() { return null; }
        @Override public boolean isBlocked() { return false; }
        @Override public void blockFor(int turns) {}
        @Override public void tickBlock() {}
        @Override public void addExtraTurns(int turns) {}
        @Override public void useExtraTurn() {}
        @Override public void markAsWinner() {}
        @Override public boolean isWinner() { return false; }
        @Override public void reset() {}
        @Override public boolean hasExtraTurns() { return false; }
    }

    /** Instance of the lever mini-game under test. */
    private ILeverGame leverGame;

    /**
     * Creates a fresh {@link LeverGame} before each test.
     */
    @BeforeEach
    void setUp() {
        leverGame = new LeverGame();
    }

    /**
     * Verifies that when there is a single lever with a non-null target,
     * and the player is a bot, the game is solved successfully.
     * <p>
     * Since there is exactly one lever, the random choice is deterministic.
     * </p>
     */
    @Test
    void testAddLeverAndSolveWhenTargetExists() {
        Lever lever = new Lever(new FakeDivision("TreasureRoom"));
        leverGame.addLever(lever);

        boolean result = leverGame.start(new FakePlayerState(new FakePlayer("Bot", true)));

        assertTrue(result);
        assertTrue(leverGame.isSolved());
    }

    /**
     * Verifies that when there is a single lever with a {@code null} target,
     * the game does not mark as solved and returns {@code false}.
     */
    @Test
    void testAddLeverAndFailWhenNoTarget() {
        Lever lever = new Lever(null);
        leverGame.addLever(lever);

        boolean result = leverGame.start(new FakePlayerState(new FakePlayer("Bot", true)));

        assertFalse(result);
        assertFalse(leverGame.isSolved());
    }

    /**
     * Verifies that starting the game with multiple levers for a bot player
     * does not throw any exceptions (even though the exact outcome is random).
     */
    @Test
    void testMultipleLeversDoesNotCrash() {
        leverGame.addLever(new Lever(null));
        leverGame.addLever(new Lever(new FakeDivision("TreasureRoom")));
        leverGame.addLever(new Lever(null));

        FakePlayerState bot = new FakePlayerState(new FakePlayer("Bot", true));

        assertDoesNotThrow(() -> leverGame.start(bot));
    }

    /**
     * Verifies the correct behaviour of {@link LeverGame#setSolved(boolean)}
     * and {@link LeverGame#isSolved()}.
     */
    @Test
    void testSetAndGetSolved() {
        leverGame.setSolved(true);
        assertTrue(leverGame.isSolved());
        leverGame.setSolved(false);
        assertFalse(leverGame.isSolved());
    }

    /**
     * Verifies that the private method {@code checkLevers(int)} throws
     * {@link LeverAlreadyPushedException} when the lever is already pushed.
     */
    @Test
    void testLeverAlreadyPushedThrowsWhenChecked() throws Exception {
        Lever lever = new Lever(new FakeDivision("Room"));
        lever.push();
        leverGame.addLever(lever);

        var method = LeverGame.class.getDeclaredMethod("checkLevers", int.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(leverGame, 0));

        assertTrue(ex.getCause() instanceof LeverAlreadyPushedException);
    }

    /**
     * Verifies that the private method {@code checkLevers(int)} throws
     * {@link LeverNotFoundException} when the underlying collection is empty
     * or the index is invalid.
     */
    @Test
    void testLeverNotFoundThrowsWhenEmpty() throws Exception {
        var method = LeverGame.class.getDeclaredMethod("checkLevers", int.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(leverGame, 0));

        assertTrue(ex.getCause() instanceof LeverNotFoundException);
    }

    /**
     * Verifies that {@link LeverGame#getLevers()} reflects lever additions correctly.
     */
    @Test
    void testGetLeversReflectsAddedLevers() {
        LeverGame concreteGame = new LeverGame();
        assertEquals(0, concreteGame.getLevers().size());

        concreteGame.addLever(new Lever(null));
        concreteGame.addLever(new Lever(new FakeDivision("X")));

        assertEquals(2, concreteGame.getLevers().size());
    }

    /**
     * Verifies that {@link LeverGame#toString()} returns the expected
     * descriptive message for the mini-game.
     */
    @Test
    void testToStringReturnsDescription() {
        String description = leverGame.toString();
        assertNotNull(description);
        assertTrue(description.contains("LEVER DIVISION"));
    }
}
