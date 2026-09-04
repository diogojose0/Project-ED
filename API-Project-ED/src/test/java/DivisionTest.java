import Game.api.division.IDivision;
import Game.api.division.IMiniGame;
import Game.api.player.IPlayerState;
import Game.entities.division.Division;
import Game.exceptions.division.NullMiniGameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Division} class.
 * <p>
 * These tests cover basic construction and flags (treasure / entry point),
 * mini-game handling, equality logic and id generation.
 * </p>
 */
public class DivisionTest {

    private IDivision division;
    private IDivision treasureDivision;
    private IDivision entryDivision;

    /**
     * Simple fake implementation of {@link IMiniGame} for testing purposes.
     * It only tracks whether {@code start()} has been called.
     */
    private static class FakeMiniGame implements IMiniGame {
        boolean started = false;

        @Override
        public boolean start(IPlayerState playerState) {
            started = true;
            return true;
        }

        @Override
        public boolean isSolved() {
            return started;
        }

        @Override
        public void setSolved(boolean solved) {
            started = solved;
        }
    }

    /**
     * Minimal fake implementation of {@link IPlayerState} used to call
     * {@link Division#startMiniGame(IPlayerState)} without depending on the full player model.
     */
    private static class FakePlayerState implements IPlayerState {
        @Override public Game.api.player.IPlayer getPlayer() { return null; }
        @Override public Game.api.player.IMovementPlayer getMovementPlayer() { return null; }
        @Override public boolean isBlocked() { return false; }
        @Override public void blockFor(int turns) { }
        @Override public void tickBlock() { }
        @Override public void addExtraTurns(int turns) { }
        @Override public void useExtraTurn() { }
        @Override public void markAsWinner() { }
        @Override public boolean isWinner() { return false; }
        @Override public void reset() { }
        @Override public boolean hasExtraTurns() { return false; }
    }

    /**
     * Initializes sample divisions used across tests.
     */
    @BeforeEach
    void setUp() {
        division = new Division("Hallway", false, false);
        treasureDivision = new Division("Treasure", true, false);
        entryDivision = new Division("Entrance", false, true);
    }


    /**
     * Verifies that constructor parameters are correctly exposed
     * through {@link Division#getName()}, {@link Division#isTreasureRoom()}
     * and {@link Division#isEntryPoint()}.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("Hallway", division.getName());
        assertFalse(division.isTreasureRoom());
        assertFalse(division.isEntryPoint());

        assertTrue(treasureDivision.isTreasureRoom());
        assertTrue(entryDivision.isEntryPoint());
    }

    /**
     * Verifies that each division receives a unique id when created
     * using the auto-increment constructor.
     */
    @Test
    void testUniqueIdGeneration() {
        int id1 = division.getId();
        int id2 = new Division("Next", false, false).getId();
        assertNotEquals(id1, id2);
    }

    /**
     * Verifies that {@link Division#toString()} returns the division name
     * when no mini-game is attached.
     */
    @Test
    void testToStringReturnsName() {
        assertEquals("Hallway", division.toString());
    }


    /**
     * Verifies that {@link Division#startMiniGame(IPlayerState)} calls
     * {@link IMiniGame#start(IPlayerState)} when a mini-game is set.
     */
    @Test
    void testStartMiniGameSuccessfully() throws NullMiniGameException {
        FakeMiniGame miniGame = new FakeMiniGame();
        division.setMiniGame(miniGame);

        division.startMiniGame(new FakePlayerState());
        assertTrue(miniGame.started);
    }

    /**
     * Verifies that {@link Division#startMiniGame(IPlayerState)} throws
     * {@link NullMiniGameException} when no mini-game is associated.
     */
    @Test
    void testStartMiniGameThrowsWhenNull() {
        division.setMiniGame(null);
        assertThrows(NullMiniGameException.class,
                () -> division.startMiniGame(new FakePlayerState()));
    }

    /**
     * Verifies that setters and getters for the mini-game work as expected.
     */
    @Test
    void testSetAndGetMiniGame() {
        FakeMiniGame miniGame = new FakeMiniGame();
        division.setMiniGame(miniGame);
        assertEquals(miniGame, division.getMiniGame());
    }


    /**
     * Verifies that {@link Division#equals(Object)} returns {@code true}
     * when the same instance is compared to itself.
     */
    @Test
    void testEqualsReturnsTrueForSameObject() {
        assertEquals(division, division);
    }

    /**
     * Verifies that {@link Division#equals(Object)} returns {@code false}
     * when comparing a division with an object of another type.
     */
    @Test
    void testEqualsReturnsFalseForDifferentType() {
        assertNotEquals(division, "NotADivision");
    }

    /**
     * Verifies that two references pointing to the same {@link Division}
     * instance are considered equal.
     */
    @Test
    void testEqualsReturnsTrueForSameValues() {
        IDivision d1 = new Division("Lab", false, false);
        IDivision d2 = d1;
        assertEquals(d1, d2);
    }

    /**
     * Verifies that two different {@link Division} objects with the same
     * name but different ids are not considered equal.
     */
    @Test
    void testEqualsReturnsFalseForDifferentObjects() {
        Division d1 = new Division("A", false, false);
        Division d2 = new Division("A", false, false);
        assertNotEquals(d1, d2);
    }

    /**
     * Verifies that {@link Division#getNextID()} increases when a new
     * division is instantiated.
     */
    @Test
    void testNextIdIncrementsProperly() {
        int before = Division.getNextID();
        new Division("Temp", false, false);
        int after = Division.getNextID();
        assertTrue(after > before);
    }
}
