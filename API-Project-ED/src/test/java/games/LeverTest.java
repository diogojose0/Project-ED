package games;

import Game.api.division.IDivision;
import Game.api.division.leverGame.ILever;
import Game.entities.division.games.leverGame.Lever;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Lever} class.
 * <p>
 * These tests validate the initial state of the lever (unpushed),
 * the state change after calling {@code push()}, the textual
 * representation returned by {@code toString()}, the JSON
 * serialization produced by {@code toJson()}, and the behaviour
 * of the {@code setTarget(IDivision)} method.
 * </p>
 */
public class LeverTest {

    /**
     * Fake minimal implementation of {@link IDivision} used as target
     * for testing the {@link Lever} class.
     */
    private static class FakeDivision implements IDivision {
        private final String name;

        public FakeDivision(String name) {
            this.name = name;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public void startMiniGame(Game.api.player.IPlayerState playerState) { }

        @Override
        public Game.api.division.IMiniGame getMiniGame() { return null; }

        @Override
        public void setMiniGame(Game.api.division.IMiniGame miniGame) { }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isTreasureRoom() {
            return false;
        }

        @Override
        public boolean isEntryPoint() {
            return false;
        }

        @Override
        public JSONObject toJson() {
            return null;
        }
    }

    /**
     * Verifies that a new {@link Lever} starts in the "unpushed" state
     * and that the target division is stored correctly.
     */
    @Test
    void testLeverStartsUnpushed() {
        Lever lever = new Lever(new FakeDivision("TreasureRoom"));
        assertFalse(lever.isPushed(), "Lever should start unpushed");
        assertEquals("TreasureRoom", lever.getTarget().getName());
    }

    /**
     * Verifies that calling {@link Lever#push()} changes the lever state to pushed.
     */
    @Test
    void testLeverPushChangesState() {
        ILever lever = new Lever(null);
        assertFalse(lever.isPushed());
        lever.push();
        assertTrue(lever.isPushed());
    }

    /**
     * Verifies that {@link Lever#toString()} reflects the correct state
     * (UnPushed / Pushed) before and after calling {@code push()}.
     */
    @Test
    void testLeverToStringDisplaysState() {
        ILever lever = new Lever(null);
        String unpushed = lever.toString();
        assertTrue(unpushed.contains("UnPushed"));
        lever.push();
        String pushed = lever.toString();
        assertTrue(pushed.contains("Pushed"));
    }

    /**
     * Verifies that {@link Lever#toJson()} returns "right" when the target
     * division is not {@code null}.
     */
    @Test
    void testToJsonRightTarget() {
        Lever lever = new Lever(new FakeDivision("Room"));
        JSONObject json = lever.toJson();
        assertEquals("right", json.get("target"));
    }

    /**
     * Verifies that {@link Lever#toJson()} returns "wrong" when the target
     * division is {@code null}.
     */
    @Test
    void testToJsonWrongTarget() {
        Lever lever = new Lever(null);
        JSONObject json = lever.toJson();
        assertEquals("wrong", json.get("target"));
    }

    /**
     * Verifies that {@link Lever#setTarget(IDivision)} correctly updates
     * the target division.
     */
    @Test
    void testSetTargetChangesTarget() {
        Lever lever = new Lever(null);
        assertNull(lever.getTarget());

        IDivision division = new FakeDivision("NewTarget");
        lever.setTarget(division);

        assertNotNull(lever.getTarget());
        assertEquals("NewTarget", lever.getTarget().getName());
    }

}
