package customCollections;

import Collections.exceptions.ElementNotFoundException;
import Collections.list.ArrayUnorderedList;


/**
 * Array-based implementation of {@link ExtendedArrayUnorderedListADT}.
 * <p>
 * Extends {@link ArrayUnorderedList} by exposing indexed access to the
 * underlying storage, allowing elements to be retrieved directly by position.
 * </p>
 *
 * @param <T> the type of elements stored in this list
 */
public class ExtendedArrayUnorderedList<T> extends ArrayUnorderedList<T>
        implements ExtendedArrayUnorderedListADT<T> {

    /**
     * Returns the element stored at the given index in the internal array.
     *
     * @param index zero-based position of the element
     * @return the element at the specified index
     * @throws ElementNotFoundException if the index is negative or greater than or equal to the current size
     */
    @Override
    public T getByIndex(int index) throws ElementNotFoundException {
        if (index >= 0 && index < this.counter) {
            return this.list[index];
        } else {
            throw new ElementNotFoundException();
        }
    }

}
