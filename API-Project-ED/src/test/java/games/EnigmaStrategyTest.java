package games;

import Game.api.division.enigmaGame.IEnigma;
import Game.api.division.enigmaGame.IEnigmaStrategy;
import Game.entities.division.games.enigmaGame.EnigmaStrategy;
import Game.exceptions.division.NonEnigmaAvailableException;
import Game.exceptions.division.NullEnigmaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link EnigmaStrategy} class.
 * <p>
 * These tests verify:
 * adding and retrieving enigmas from the strategy queue;
 * validation of null inputs;
 * behaviour when no enigmas are available;
 * rotation behaviour of the internal queue;
 * preservation of all enigmas after shuffling.
 * </p>
 */
public class EnigmaStrategyTest {

    private IEnigmaStrategy strategy;

    /**
     * Minimal fake {@link IEnigma} implementation for testing the strategy.
     * Uses a simple name field to distinguish instances.
     */
    private static class FakeEnigma implements IEnigma {
        private final String name;

        public FakeEnigma(String name) {
            this.name = name;
        }

        @Override
        public boolean verifyAnswer(int answer) {
            return answer == 0;
        }

        @Override
        public boolean validateOption(int option) {
            return option >= 0;
        }

        @Override
        public Collections.list.ArrayUnorderedList<String> getAnswers() {
            Collections.list.ArrayUnorderedList<String> list = new Collections.list.ArrayUnorderedList<>();
            list.addToRear("A");
            list.addToRear("B");
            return list;
        }

        @Override
        public String toString() {
            return "FakeEnigma{" + name + "}";
        }
    }

    /**
     * Creates a fresh {@link EnigmaStrategy} before each test.
     * <p>
     * Note: the internal queue is static, and the constructor resets it.
     * </p>
     */
    @BeforeEach
    void setUp() {
        strategy = new EnigmaStrategy();
    }

    /**
     * Verifies that adding a non-null enigma and retrieving it via
     * {@link EnigmaStrategy#getEnigma()} works correctly.
     */
    @Test
    void testAddAndGetEnigma() throws NullEnigmaException, NonEnigmaAvailableException {
        IEnigma e1 = new FakeEnigma("First");
        strategy.addEnigma(e1);
        IEnigma result = strategy.getEnigma();
        assertNotNull(result);
        assertEquals("FakeEnigma{First}", result.toString());
    }

    /**
     * Verifies that adding a {@code null} enigma causes a {@link NullEnigmaException}.
     */
    @Test
    void testAddNullThrows() {
        assertThrows(NullEnigmaException.class, () -> strategy.addEnigma(null));
    }

    /**
     * Verifies that requesting an enigma from an empty strategy
     * throws {@link NonEnigmaAvailableException}.
     */
    @Test
    void testGetEmptyThrows() {
        assertThrows(NonEnigmaAvailableException.class, () -> strategy.getEnigma());
    }

    /**
     * Verifies that the queue rotation works: calling {@link EnigmaStrategy#getEnigma()}
     * multiple times returns enigmas in a cyclic fashion and not always the same reference.
     */
    @Test
    void testQueueRotation() throws NullEnigmaException, NonEnigmaAvailableException {
        IEnigma e1 = new FakeEnigma("E1");
        IEnigma e2 = new FakeEnigma("E2");
        strategy.addEnigma(e1);
        strategy.addEnigma(e2);

        IEnigma first = strategy.getEnigma();
        IEnigma second = strategy.getEnigma();

        assertNotNull(first);
        assertNotNull(second);
        assertNotEquals(first.toString(), second.toString());
    }

    /**
     * Verifies that, after shuffling and rotating, the strategy still
     * returns a non-null enigma for each previously added enigma.
     */
    @Test
    void testShuffleKeepsAllEnigmas() throws NullEnigmaException, NonEnigmaAvailableException {
        for (int i = 0; i < 5; i++) {
            strategy.addEnigma(new FakeEnigma("E" + i));
        }

        for (int i = 0; i < 5; i++) {
            assertNotNull(strategy.getEnigma());
        }
    }

    /**
     * Verifies that multiple rotations (more calls than enigmas available)
     * never return {@code null}.
     */
    @Test
    void testMultipleCyclesNeverReturnNull() throws NullEnigmaException, NonEnigmaAvailableException {
        strategy.addEnigma(new FakeEnigma("A"));
        strategy.addEnigma(new FakeEnigma("B"));
        strategy.addEnigma(new FakeEnigma("C"));

        for (int i = 0; i < 20; i++) {
            assertNotNull(strategy.getEnigma());
        }
    }

    /**
     * Verifies that all enigmas returned by the strategy belong to the set
     * originally added (no "foreign" or lost data).
     */
    @Test
    void testReturnedEnigmasBelongToInitialSet() throws NullEnigmaException, NonEnigmaAvailableException {
        FakeEnigma e1 = new FakeEnigma("E1");
        FakeEnigma e2 = new FakeEnigma("E2");
        FakeEnigma e3 = new FakeEnigma("E3");

        strategy.addEnigma(e1);
        strategy.addEnigma(e2);
        strategy.addEnigma(e3);

        for (int i = 0; i < 10; i++) {
            IEnigma e = strategy.getEnigma();
            assertTrue(
                    e.toString().equals("FakeEnigma{E1}") ||
                            e.toString().equals("FakeEnigma{E2}") ||
                            e.toString().equals("FakeEnigma{E3}")
            );
        }
    }
}
