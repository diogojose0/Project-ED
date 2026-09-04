package customCollections;

import Collections.exceptions.ElementNotFoundException;
import Collections.exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for {@link ExtendedArrayUnorderedList}.
 * <p>
 * These tests validate:
 * Correct element retrieval by index;
 * Exception handling for invalid indices (negative or out of bounds);
 * Behaviour of {@code getByIndex} after removals;
 * That {@code getByIndex} does not modify the internal state of the list;
 * Exception behaviour when the list is empty.
 * </p>
 */
public class ExtendedArrayUnorderedListTest {

    /** List instance used in each test. */
    private ExtendedArrayUnorderedList<String> list;

    /**
     * Initializes the list with three elements before each test:
     * {@code ["A", "B", "c"]}.
     */
    @BeforeEach
    void setUp() {
        list = new ExtendedArrayUnorderedList<>();
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");
    }

    /**
     * Verifies that {@code getByIndex} returns the correct element
     * for valid indices within the list bounds.
     *
     * @throws ElementNotFoundException if an unexpected error occurs during retrieval
     */
    @Test
    void testGetByIndexReturnsCorrectElement() throws ElementNotFoundException {
        assertEquals("A", list.getByIndex(0));
        assertEquals("B", list.getByIndex(1));
        assertEquals("C", list.getByIndex(2));
    }

    /**
     * Verifies that {@code getByIndex} throws {@link ElementNotFoundException}
     * when a negative index is provided.
     */
    @Test
    void testGetByIndexThrowsExceptionForNegativeIndex() {
        assertThrows(ElementNotFoundException.class, () -> list.getByIndex(-1));
    }

    /**
     * Verifies that {@code getByIndex} throws {@link ElementNotFoundException}
     * when the index is greater than or equal to the list size.
     */
    @Test
    void testGetByIndexThrowsExceptionForIndexTooHigh() {
        assertThrows(ElementNotFoundException.class, () -> list.getByIndex(3));
        assertThrows(ElementNotFoundException.class, () -> list.getByIndex(10));
    }

    /**
     * Verifies that {@code getByIndex} works as expected after elements
     * have been removed from the list.
     *
     * @throws ElementNotFoundException if an unexpected error occurs during retrieval
     * @throws EmptyCollectionException if an unexpected error occurs when removing elements
     */
    @Test
    void testGetByIndexAfterAddingAndRemovingElements() throws ElementNotFoundException, EmptyCollectionException {
        list.removeFirst();
        assertEquals("B", list.getByIndex(0));
        assertEquals("C", list.getByIndex(1));
    }

    /**
     * Ensures that calling {@code getByIndex} does not modify the state of the list,
     * subsequent calls still return the same elements in the same order.
     *
     * @throws ElementNotFoundException if an unexpected error occurs during retrieval
     */
    @Test
    void testGetByIndexDoesNotModifyListState() throws ElementNotFoundException {
        String original = list.getByIndex(1);
        assertEquals("B", original);
        assertEquals("A", list.getByIndex(0));
        assertEquals("C", list.getByIndex(2));
    }

    /**
     * Verifies that {@code getByIndex} throws {@link ElementNotFoundException}
     * when called on an empty list.
     */
    @Test
    void testGetByIndexEmptyListThrowsException() {
        ExtendedArrayUnorderedList<Integer> emptyList = new ExtendedArrayUnorderedList<>();
        assertThrows(ElementNotFoundException.class, () -> emptyList.getByIndex(0));
    }
}
