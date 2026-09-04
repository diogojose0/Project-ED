package customCollections;

import Collections.exceptions.ElementNotFoundException;


/**
 * Contract for unordered array-based lists that support indexed access.
 *
 * @param <T> the type of elements stored in the list
 */
public interface ExtendedArrayUnorderedListADT<T> {

    /**
     * Returns the element stored at the given index.
     *
     * @param index zero-based position of the element
     * @return element at the specified index
     * @throws ElementNotFoundException if the index is out of bounds
     */
    T getByIndex(int index) throws ElementNotFoundException;
}
