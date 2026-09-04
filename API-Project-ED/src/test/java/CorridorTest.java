import Game.api.corridor.ICorridor;
import Game.api.corridor.IEventStrategy;
import Game.entities.corridor.Corridor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Corridor} class.
 * <p>
 * These tests verify:
 * name storage and accessors;
 * string representation;
 * JSON conversion;
 * event generation behavior and probability;
 * that event generation never throws exceptions.
 * </p>
 */
public class CorridorTest {

    /**
     * Verifies that the constructor correctly stores the corridor name
     * and that {@link Corridor#getName()} returns the same value.
     */
    @Test
    void testConstructorStoresNameCorrectly() {
        ICorridor corridor = new Corridor("Hallway");
        assertNotNull(corridor);
        assertEquals("Hallway", corridor.getName());
    }

    /**
     * Verifies that {@link Corridor#toString()} returns the corridor name.
     */
    @Test
    void testToStringReturnsName() {
        ICorridor corridor = new Corridor("North Tunnel");
        assertEquals("North Tunnel", corridor.toString());
    }

    /**
     * Verifies that {@link Corridor#toJson()} contains the expected "name" field
     * with the correct value.
     */
    @Test
    void testToJsonContainsName() {
        Corridor corridor = new Corridor("Secret Passage");
        var json = corridor.toJson();

        assertNotNull(json);
        assertTrue(json.containsKey("name"));
        assertEquals("Secret Passage", json.get("name"));
    }

    /**
     * Verifies that {@link Corridor#generateEvent()} sometimes returns
     * {@code null} and sometimes a non-null {@link IEventStrategy},
     * given enough repetitions.
     * <p>
     * This does not check the exact probability, only that both cases occur.
     * </p>
     */
    @Test
    void testGenerateEventReturnsNullOrEventStrategy() {
        Corridor corridor = new Corridor("Main Corridor");

        boolean eventOccurred = false;
        boolean nullOccurred = false;

        for (int i = 0; i < 200; i++) {
            IEventStrategy event = corridor.generateEvent();
            if (event == null) {
                nullOccurred = true;
            } else {
                eventOccurred = true;
            }
            if (eventOccurred && nullOccurred) break;
        }

        assertTrue(eventOccurred);
        assertTrue(nullOccurred);
    }

    /**
     * Verifies that the empirical probability of generating an event
     * is roughly within the expected range (around 30%).
     * <p>
     * The test accepts a broad range (20%–50%) to avoid flakiness
     * due to randomness.
     * </p>
     */
    @Test
    void testGenerateEventProbabilityRoughlyWithinExpectedRange() {
        Corridor corridor = new Corridor("Probability Corridor");
        int eventCount = 0;
        int totalRuns = 10_000;

        for (int i = 0; i < totalRuns; i++) {
            if (corridor.generateEvent() != null) {
                eventCount++;
            }
        }

        double percentage = (eventCount / (double) totalRuns) * 100;

        assertTrue(percentage >= 20 && percentage <= 50);
    }

    /**
     * Verifies that {@link Corridor#generateEvent()} never throws an exception,
     * even when called multiple times.
     */
    @Test
    void testGenerateEventNeverThrowsException() {
        Corridor corridor = new Corridor("Safe Corridor");
        assertDoesNotThrow(corridor::generateEvent);
    }
}
