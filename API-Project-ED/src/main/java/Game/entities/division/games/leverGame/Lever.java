package Game.entities.division.games.leverGame;

import Game.api.division.IDivision;
import Game.api.division.leverGame.ILever;
import org.json.simple.JSONObject;


/**
 * Implementation of {@link ILever} representing a single lever
 * that may or may not point to a valid target division.
 */
public class Lever implements ILever {

    /** Target division unlocked when this lever is correct. */
    private IDivision target;
    /** Flag that indicates if this lever has been pushed. */
    private boolean pushed = false;

    /**
     * Creates a new {@code Lever} with the given target division.
     *
     * @param target the division unlocked by this lever, or {@code null} if it is a wrong lever
     */
    public Lever (IDivision target) {
        this.target = target;
    }

    /**
     * Marks this lever as pushed.
     */
    @Override
    public void push() {
        pushed = true;
    }

    /**
     * Indicates whether this lever has already been pushed.
     *
     * @return {@code true} if pushed, {@code false} otherwise
     */
    @Override
    public boolean isPushed() {
        return pushed;
    }

    /**
     * Returns the division that is unlocked by this lever.
     *
     * @return the target division, or {@code null} if there is no target
     */
    @Override
    public IDivision getTarget() {
        return target;
    }

    /**
     * Updates the target division associated with this lever.
     *
     * @param target the new target division, or {@code null} to mark it as a wrong lever
     */
    @Override
    public void setTarget(IDivision target) {
        this.target = target;
    }


    /**
     * Returns a string representation of the lever, indicating whether it has been pushed or not.
     *
     * @return formatted lever description
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Lever - ").append(this.pushed ? "Pushed" : "UnPushed");
        return builder.toString();
    }

    /**
     * Converts this lever into its JSON representation.
     *
     * @return JSON object representing this lever
     */
    @Override
    public JSONObject toJson() {
        JSONObject leverJson = new JSONObject();

        if(target != null) {
            leverJson.put("target", "right");
        } else {
            leverJson.put("target", "wrong");
        }

        return leverJson;
    }

}
