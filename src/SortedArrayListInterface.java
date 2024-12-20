import java.util.Iterator;

/**
 * Provides required methods for the SortedArrayList class
 * @param <E>   type parameter for the elements in the list
 */
public interface SortedArrayListInterface<E> {

    /**
     * Retrieves the number of elements being maintained by the list
     * @return      the number of elements being maintained
     */
    public int size();

    /**
     * Retrieves whether the list is empty
     * @return  true, if there are no elements in the list; false, if there are elements
     */
    public boolean isEmpty();

    /**
     * Clears the list; no elements will remain after the call, and size will be 0
     */
    public void clear();

    /**
     * Retrieves whether the specified element is in the list
     * @param value     the value to search for
     * @return          true, if the element is in the list; false, if not
     */
    public boolean contains(E value);

    /**
     * Uses a binary search to find the index of the first occurrence of the specified value,
     * or, if not found, the place that value should be
     * @param       value   the value to search for
     * @return              if found, the index of the value in the list (range 0 to size - 1);
     *                      if not found, an index representing where the value would go, if added, returned
     *                      as -(position+1), e.g., -1 means it goes at index 0, -5 means it goes at index 4
     */
    public int indexOf(E value);

    /**
     * Retrieves the element at the specified position in the list
     * @param index     the index (position) in the list; must be 0 to size-1
     * @return          the element at the specified position
     */
    public E get(int index);

    /**
     * Retrieves an array of elements that are compare themselves equally to the specified value (via compareTo),
     * with results being stored in the array specified.
     * @param value         the element being sought; will be used to compareTo() other elements
     * @param template      a template array used to create results; pass in a 0-sized array
     * @return              a new array that is right-sized and contains element references, if any
     */
    public E[] get(E value, E[] template);

    /**
     * Adds a new element to the list, maintaining sorting via natural order (via compareTo)
     * @param value     the value to add to the list
     */
    public void add(E value);

    /**
     * Removes from the list the element at the specified index
     * @param index     the index in the list; must be in range  0 to size-1
     */
    public void remove(int index);

    /**
     * Retrieves an iterator over list elements; for/each loops are also supported
     * @return      a strongly typed iterator over list elements
     */
    public Iterator<E> iterator();

    /**
     * Retrieves a  text representation of the elements in the list
     * @return      text representing of list elements
     */
    public String toString();

    /**
     * Retrieves an array representing the contents of the list
     * @param template      a template list of the proper type, e.g., if E is String,
     *                          the caller can pass in as an argument: new String[0]
     * @return              an array containing object references to list elements
     */
    public E[] toArray(E[] template);

}
