import java.util.Iterator;

/**
 * A generic wrapper class that holds a sorted ArrayList of elements
 * @author  Maya Rao
 * @version 10-15-2024
 *
 * @param <E>   elements in the list (must be able to use Comparable)
 */

public class SortedArrayList<E extends Comparable<E>> implements SortedArrayListInterface<E>, Iterable<E> {
    // instance variables
    /** The backend ArrayList that actually stores data and provides basic ArrayList methods */
    private ArrayList<E> elementList;

    // constructors
    /** Constructs a SortedArrayList that has an ArrayList of DEFAULT_CAPACITY under the hood */
    public SortedArrayList() {
        elementList = new ArrayList<E>();
    }

    /**
     * Constructs a SortedArrayList that has an ArrayList of a given capacity under the hood
     * @param capacity  the capacity of the underlying ArrayList
     */
    public SortedArrayList(int capacity) {
        elementList = new ArrayList<E>(capacity);
    }

    // methods
    /**
     * Retrieves the number of elements being maintained by the list
     *
     * @return the number of elements being maintained
     */
    @Override
    public int size() {
        return elementList.size();
    }

    /**
     * Retrieves whether the list is empty
     *
     * @return true, if there are no elements in the list; false, if there are elements
     */
    @Override
    public boolean isEmpty() {
        return elementList.isEmpty();
    }

    /** Clears the list; no elements will remain after the call, and size will be 0 */
    @Override
    public void clear() {
        elementList.clear();
    }

    /**
     * Retrieves whether the specified element is in the list
     *
     * @param value the value to search for
     * @return true, if the element is in the list; false, if not
     */
    @Override
    public boolean contains(E value) {
        // value can't be null because compareTo doesn't work with nulls
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        // if indexOf(value) is negative, the value is not inside the list
        return indexOf(value) >= 0;
    }

    /**
     * Uses a binary search to find the index of the first occurrence of the specified value,
     * or, if not found, the place that value should be
     *
     * @param value the value to search for
     * @return if found, the index of the value in the list (range 0 to size - 1);
     * if not found, an index representing where the value would go, if added, returned
     * as -(position+1), e.g., -1 means it goes at index 0, -5 means it goes at index 4
     */
    @Override
    public int indexOf(E value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        // binary search through list and order elements
        int min = 0;
        int max = elementList.size() - 1;

        // checking to see if mid is before or after value
        int mid = -1;
        while (min <= max) {
            mid = (max + min) / 2;
            if (elementList.get(mid).compareTo(value) == 0) {
                // element has been found, backtrack to the first duplicate
                while (elementList.get(mid - 1).compareTo(value) == 0) {
                    mid--;
                }
                return mid;     // found it!
            } else if (elementList.get(mid).compareTo(value) < 0) {
                min = mid + 1;  // too small
            } else {   // elementList.get(mid).compareTo(value) > 0
                max = mid - 1;  // too large
            }
            mid = (max + min) / 2;
        }

        return -min - 1;   // not found
    }

    /**
     * Retrieves the element at the specified position in the list
     *
     * @param index the index (position) in the list; must be 0 to size-1
     * @return the element at the specified position
     */
    @Override
    public E get(int index) {
        return elementList.get(index);
    }

    /**
     * Retrieves an array of elements that compare themselves equally to the specified value (via compareTo),
     * with results being stored in the array specified.
     *
     * @param value    the element being sought; will be used to compareTo() other elements
     * @param template a template array used to create results; pass in a 0-sized array
     * @return a new array that is right-sized and contains element references, if any
     */
    @Override
    public E[] get(E value, E[] template) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        if (template == null) {
            throw new IllegalArgumentException("template must not be null");
        }

        // holds all the elements that match value
        ArrayList<E> matches = new ArrayList<E>();

        // if the element compared to value is the same, add it to matches
        for (E element : elementList) {
            if (element.compareTo(value) == 0) {
                matches.add(element);
            }
        }

        // turn matches into an array with the template
        return matches.toArray(template);
    }

    /**
     * Adds a new element to the list, maintaining sorting via natural order (via compareTo)
     *
     * @param value the value to add to the list
     */
    @Override
    public void add(E value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        // get the correctly ordered index of value
        int index = indexOf(value);

        // if the value doesn't exist (index is negative), turn index into the correct insertion point
        if (index < 0) {
            index = -(index + 1);
        }

        // use ArrayList<E> add at given index method to insert value
        elementList.add(index, value);
    }

    /**
     * Removes from the list the element at the specified index
     *
     * @param index the index in the list; must be in range  0 to size-1
     */
    @Override
    public void remove(int index) {
        elementList.remove(index);
    }

    /**
     * Retrieves an iterator over list elements; for/each loops are also supported
     *
     * @return a strongly typed iterator over list elements
     */
    @Override
    public Iterator<E> iterator() {
        // allows users to go through elements in a list
        return elementList.iterator();
    }

    /**
     * Retrieves a  text representation of the elements in the list
     * @return      text representing of list elements
     */
    public String toString() {
        return elementList.toString();
    }

    /**
     * Retrieves an array representing the contents of the list
     *
     * @param template a template list of the proper type, e.g., if E is String,
     *                 the caller can pass in as an argument: new String[0]
     * @return an array containing object references to list elements
     */
    @Override
    public E[] toArray(E[] template) {
        if (template == null) {
            throw new IllegalArgumentException("template must not be null");
        }

        return elementList.toArray(template);
    }
}
