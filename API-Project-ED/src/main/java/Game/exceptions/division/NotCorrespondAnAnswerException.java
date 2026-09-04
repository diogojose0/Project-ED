package Game.exceptions.division;


/**
 * NotCorrespondAnAnswerException is an exception thrown when none of the
 * provided answers matches the correct answer for an enigma.
 */
public class NotCorrespondAnAnswerException extends Exception {

    /**
     * Default message used when all possible answers do not correspond
     * to the correct answer.
     */
    private static final String DEFAULT_MESSAGE = "All possible answers were not correspond to the correct answer.";

    /**
     * Creates a {@code NotCorrespondAnAnswerException} with the default message.
     */
    public NotCorrespondAnAnswerException() { super(DEFAULT_MESSAGE); }

    /**
     * Creates a {@code NotCorrespondAnAnswerException} with the specified message.
     *
     * @param message the message to be displayed with the exception
     */
    public NotCorrespondAnAnswerException(String message) {
        super(message);
    }
}
