package games;

import Collections.list.ArrayUnorderedList;
import Game.api.division.enigmaGame.IEnigma;
import Game.entities.division.games.enigmaGame.Enigma;
import Game.exceptions.division.NotCorrespondAnAnswerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the {@link Enigma} class.
 * <p>
 * These tests validate:
 * constructor validation
 * correct answer verification;
 * options validation;
 * </p>
 */
public class EnigmaTest {

    /** Shared list of answers used in most tests. */
    private ArrayUnorderedList<String> answers;

    /**
     * Initializes a default list of three answers: "A", "B", "C".
     */
    @BeforeEach
    void setUp() {
        answers = new ArrayUnorderedList<>();
        answers.addToRear("A");
        answers.addToRear("B");
        answers.addToRear("C");
    }

    /**
     * Verifies that the constructor accepts a valid index for the correct answer
     * and stores the provided list of answers.
     */
    @Test
    void testConstructorValidAnswer() throws NotCorrespondAnAnswerException {
        IEnigma enigma = new Enigma("Choose the correct letter", 1, answers);
        assertNotNull(enigma);
        assertEquals(answers, enigma.getAnswers());
    }

    /**
     * Verifies that the constructor throws {@link NotCorrespondAnAnswerException}
     * when the index of the correct answer is out of bounds.
     */
    @Test
    void testConstructorInvalidAnswerThrows() {
        assertThrows(NotCorrespondAnAnswerException.class, () ->
                new Enigma("Invalid", 10, answers));
    }

    /**
     * Verifies that the constructor throws {@link NotCorrespondAnAnswerException}
     * when the list of answers is empty, regardless of the index.
     */
    @Test
    void testConstructorEmptyAnswersThrows() {
        ArrayUnorderedList<String> empty = new ArrayUnorderedList<>();
        assertThrows(NotCorrespondAnAnswerException.class, () ->
                new Enigma("No answers", 0, empty));
    }

    /**
     * Verifies that {@link Enigma#verifyAnswer(int)} returns {@code true}
     * only for the correct answer index.
     */
    @Test
    void testVerifyAnswer() throws NotCorrespondAnAnswerException {
        Enigma enigma = new Enigma("Q?", 1, answers);
        assertTrue(enigma.verifyAnswer(1));
        assertFalse(enigma.verifyAnswer(0));
        assertFalse(enigma.verifyAnswer(2));
    }

    /**
     * Verifies that {@link Enigma#validateOption(int)} accepts only options
     * within the allowed range according to the implemented rule.
     */
    @Test
    void testValidateOption() throws NotCorrespondAnAnswerException {
        Enigma enigma = new Enigma("Pick one", 1, answers);
        assertTrue(enigma.validateOption(1));
        assertFalse(enigma.validateOption(0));
        assertFalse(enigma.validateOption(5));
    }

    /**
     * Verifies the formatting of {@link Enigma#toString()}, ensuring that the
     * question and the numbered options appear in the output.
     */
    @Test
    void testToStringFormat() throws NotCorrespondAnAnswerException {
        Enigma enigma = new Enigma("Question?", 1, answers);
        String result = enigma.toString();
        assertTrue(result.contains("Question?"));
        assertTrue(result.contains("1 - A"));
        assertTrue(result.contains("3 - C"));
    }

    /**
     * Verifies that the constructor also accepts a correct answer index
     * equal to the last valid index (size - 1).
     */
    @Test
    void testConstructorValidLastAnswerIndex() throws NotCorrespondAnAnswerException {
        int lastIndex = answers.size() - 1;
        Enigma enigma = new Enigma("Last index test", lastIndex, answers);
        assertNotNull(enigma);
        assertTrue(enigma.verifyAnswer(lastIndex));
    }
}
