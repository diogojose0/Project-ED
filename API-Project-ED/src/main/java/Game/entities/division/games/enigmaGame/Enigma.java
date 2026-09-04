package Game.entities.division.games.enigmaGame;

import Collections.list.ArrayUnorderedList;
import Game.api.division.enigmaGame.IEnigma;
import Game.exceptions.division.NotCorrespondAnAnswerException;


/**
 * Implementation of {@link IEnigma} that represents a single enigma.
 */
public class Enigma implements IEnigma {

    /** The enigma question text. */
    private String question;
    /** List of possible answers for this enigma. */
    private ArrayUnorderedList<String> answers = new ArrayUnorderedList<>();
    /** Index of the correct answer in the answers list. */
    private int correctAnswer;

    /**
     * Creates a new {@code Enigma} with the given question, correct answer index,
     * and list of possible answers.
     *
     * @param question the enigma question text
     * @param correctAnswer the index of the correct answer
     * @param answers the list of possible answers
     * @throws NotCorrespondAnAnswerException if the correct answer index is invalid
     */
    public Enigma(String question, int correctAnswer, ArrayUnorderedList<String> answers) throws NotCorrespondAnAnswerException {
        if (this.containsAnswer(answers, correctAnswer)) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.answers = answers;
        } else {
            throw new NotCorrespondAnAnswerException();
        }
    }

    /**
     * Checks whether the given correct answer index is valid for the list of answers.
     *
     * @param answers the list of answers
     * @param correctAnswer the index to validate
     * @return {@code true} if the index is valid, {@code false} otherwise
     */
    private boolean containsAnswer(ArrayUnorderedList<String> answers, int correctAnswer) {
        if (answers.isEmpty()) {
            return false;
        }

        if(correctAnswer >= 0 && correctAnswer < answers.size()) {
            return true;
        }
        return false;
    }

    /**
     * Verifies whether the provided answer index matches the correct answer.
     *
     * @param answer the chosen answer index
     * @return {@code true} if the answer is correct, {@code false} otherwise
     */
    @Override
    public boolean verifyAnswer(int answer) {
        return this.correctAnswer == answer;
    }

    /**
     * Validates whether a given option is within the valid range of answers.
     *
     * @param option the option index chosen by the player
     * @return {@code true} if the option is within the answer list size, {@code false} otherwise
     */
    @Override
    public boolean validateOption(int option) {
        return option > 0 && option <= this.answers.size();
    }

    /**
     * Returns the list of answers for this enigma.
     *
     * @return list of answer strings
     */
    @Override
    public ArrayUnorderedList<String> getAnswers() {
        return answers;
    }

    /**
     * Returns a representation of this enigma, including the question and all possible answers.
     *
     * @return formatted string of the enigma and answers
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int counter = 1;
        builder.append(question).append("\n");
        for (String answer : answers) {
            builder.append(counter++ + " - ").append(answer).append("\n");
        }
        return builder.toString();
    }

}
