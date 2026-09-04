import Game.api.engine.ITurnManager;
import Game.api.player.IPlayer;
import Game.api.player.IPlayerState;
import Game.gameCore.gameLogistic.TurnManager;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link TurnManager} implementation of {@link ITurnManager}.
 * <p>
 * These tests cover:
 * Basic cyclic turn rotation;
 * Skipping blocked players and unblocking them;
 * Handling extra turns correctly;
 * Resetting the internal queue;
 * Behaviour with an empty queue.
 * </p>
 */
public class TurnManagerTest {

    /** Turn manager under test. */
    private ITurnManager manager;

    /**
     * Simple fake implementation of {@link IPlayer} for testing purposes.
     */
    private static class FakePlayer implements IPlayer {
        private final String name;
        private final boolean bot;

        public FakePlayer(String name, boolean bot) {
            this.name = name;
            this.bot = bot;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isBot() {
            return bot;
        }

        @Override
        public JSONObject toJson() {
            return null;
        }
    }

    /**
     * Simple fake implementation of {@link IPlayerState} for testing the turn logic.
     * <p>
     * It tracks:
     * blocked state and remaining blocked turns;
     * number of extra turns.
     * </p>
     */
    private static class FakePlayerState implements IPlayerState {
        private final IPlayer player;
        private boolean blocked = false;
        private int blockTurns = 0;
        private int extraTurns = 0;

        public FakePlayerState(IPlayer player) {
            this.player = player;
        }

        @Override
        public IPlayer getPlayer() {
            return player;
        }

        @Override
        public Game.api.player.IMovementPlayer getMovementPlayer() {
            return null;
        }

        @Override
        public boolean isBlocked() {
            return blocked || blockTurns > 0;
        }

        @Override
        public void blockFor(int turns) {
            blocked = true;
            blockTurns = turns;
        }

        @Override
        public void tickBlock() {
            if (blockTurns > 0) {
                blockTurns--;
            }
            if (blockTurns == 0) {
                blocked = false;
            }
        }

        @Override
        public void addExtraTurns(int turns) {
            extraTurns += turns;
        }

        @Override
        public void useExtraTurn() {
            if (extraTurns > 0) {
                extraTurns--;
            }
        }

        @Override
        public boolean hasExtraTurns() {
            return extraTurns > 0;
        }

        @Override
        public void markAsWinner() {}

        @Override
        public boolean isWinner() {
            return false;
        }

        @Override
        public void reset() {}
    }

    /**
     * Creates a fresh {@link TurnManager} instance before each test.
     */
    @BeforeEach
    void setUp() {
        manager = new TurnManager();
    }

    /**
     * Verifies that players loaded into the manager are returned
     * in the correct cyclic order: p1 → p2 → p3 → p1 → ...
     */
    @Test
    void testLoadPlayersAndNormalTurnRotation() {
        FakePlayerState p1 = new FakePlayerState(new FakePlayer("Alice", false));
        FakePlayerState p2 = new FakePlayerState(new FakePlayer("Bob", true));
        FakePlayerState p3 = new FakePlayerState(new FakePlayer("Charlie", false));

        Collections.list.ArrayUnorderedList<IPlayerState> list = new Collections.list.ArrayUnorderedList<>();
        list.addToRear(p1);
        list.addToRear(p2);
        list.addToRear(p3);

        manager.loadPlayers(list);

        assertEquals(p1, manager.thisTurn());
        assertEquals(p2, manager.thisTurn());
        assertEquals(p3, manager.thisTurn());
        assertEquals(p1, manager.thisTurn());
    }

    /**
     * Verifies that a blocked player is skipped for one turn and unblocked
     * after {@link IPlayerState#tickBlock()} is called internally.
     */
    @Test
    void testBlockedPlayerIsSkippedAndUnblockedAfterTick() {
        FakePlayerState p1 = new FakePlayerState(new FakePlayer("Alice", false));
        FakePlayerState p2 = new FakePlayerState(new FakePlayer("Bob", false));

        p1.blockFor(1);

        Collections.list.ArrayUnorderedList<IPlayerState> list = new Collections.list.ArrayUnorderedList<>();
        list.addToRear(p1);
        list.addToRear(p2);

        manager.loadPlayers(list);

        IPlayerState firstTurn = manager.thisTurn();
        assertEquals(p2, firstTurn);
        assertFalse(p1.isBlocked());
    }

    /**
     * Verifies that a player with extra turns gets two consecutive turns
     * before the turn rotates to the next player.
     */
    @Test
    void testPlayerWithExtraTurnGetsTwoConsecutiveTurns() {
        FakePlayerState p1 = new FakePlayerState(new FakePlayer("Alice", false));
        FakePlayerState p2 = new FakePlayerState(new FakePlayer("Bob", false));

        // Give p1 one extra turn
        p1.addExtraTurns(1);

        Collections.list.ArrayUnorderedList<IPlayerState> list = new Collections.list.ArrayUnorderedList<>();
        list.addToRear(p1);
        list.addToRear(p2);

        manager.loadPlayers(list);

        IPlayerState first = manager.thisTurn();
        IPlayerState second = manager.thisTurn();

        assertEquals(p1, first);
        assertEquals(p1, second);
    }

    /**
     * Verifies that after calling {@link TurnManager#reset()} the internal
     * queue is cleared and {@link TurnManager#thisTurn()} returns {@code null}.
     */
    @Test
    void testResetClearsQueue() {
        FakePlayerState p1 = new FakePlayerState(new FakePlayer("Test", false));
        Collections.list.ArrayUnorderedList<IPlayerState> list = new Collections.list.ArrayUnorderedList<>();
        list.addToRear(p1);
        manager.loadPlayers(list);

        manager.reset();
        assertNull(manager.thisTurn());
    }

    /**
     * Verifies that calling {@link TurnManager#thisTurn()} on an empty manager
     * returns {@code null} instead of throwing an exception.
     */
    @Test
    void testEmptyQueueReturnsNull() {
        assertNull(manager.thisTurn());
    }
}
