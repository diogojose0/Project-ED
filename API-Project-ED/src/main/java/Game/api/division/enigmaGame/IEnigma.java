package Game.api.division.enigmaGame;

import Collections.list.ArrayUnorderedList;


/**
 * Contract for enigmas used in enigma mini-games.
 * <p>
 * Implementations provide a set of possible answers, validate player choices
 * and check whether a given option is correct.
 * </p>
 */
public interface IEnigma {

    /**
     * Verifies whether the given answer option corresponds to the correct solution.
     *
     * @param answer the chosen answer option
     * @return {@code true} if the answer is correct, {@code false} otherwise
     */
    boolean verifyAnswer(int answer);

    /**
     * Returns the list of available answer options for this enigma.
     *
     * @return an {@link ArrayUnorderedList} containing the answer texts
     */
    ArrayUnorderedList<String> getAnswers();

    /**
     * Checks whether the given option represents a valid choice.
     * <p>
     * This method is used to detect invalid input before attempting to verify the answer.
     * </p>
     *
     * @param option the option to validate
     * @return {@code true} if the option is within the allowed range, {@code false} otherwise
     */
    boolean validateOption(int option);
}

