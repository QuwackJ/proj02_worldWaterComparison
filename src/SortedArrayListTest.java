import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class SortedArrayListTest {

    private SortedArrayList<String> test1;
    private SortedArrayList<Double> test2;
    private SortedArrayList<Integer> test3;

    @BeforeEach
    void setUp() {
        test1 = new SortedArrayList<String>(10);
        test2 = new SortedArrayList<Double>();
        test3 = null;

        test1.add("hello");  // sorted index: 4
        test1.add("null");   // sorted index: 7
        test1.add("bye");    // sorted index: 2
        test1.add("world");  // sorted index: 8
        test1.add("hello");  // sorted index: 5
        test1.add("apple");  // sorted index: 0
        test1.add("apples"); // sorted index: 1
        test1.add("hello");  // sorted index: 6
        test1.add("world");  // sorted index: 9
        test1.add("crazy");  // sorted index: 3
    }

    @Test
    void size() {
        assertEquals(10, test1.size());

        assertEquals(0, test2.size());

        assertThrows(NullPointerException.class, () -> test3.size());
    }

    @Test
    void isEmptyAndClear() {
        assertEquals(false, test1.isEmpty());

        test1.clear();
        assertEquals(true, test1.isEmpty());
        assertEquals(0, test1.size());

        assertEquals(true, test2.isEmpty());
        assertEquals(0, test2.size());

        assertThrows(NullPointerException.class, () -> test3.isEmpty());
        assertThrows(NullPointerException.class, () -> test3.clear());
    }

    @Test
    void contains() {
        assertEquals(true, test1.contains("hello"));
        assertEquals(false, test1.contains("purple"));

        assertEquals(false, test2.contains(2.9));

        assertThrows(IllegalArgumentException.class, () -> test2.contains(null));
        assertThrows(NullPointerException.class, () -> test3.contains(1));
    }

    @Test
    void indexOf() {
        assertEquals(1, test1.indexOf("apples"));
        assertEquals(4, test1.indexOf("hello"));
        assertEquals(7, test1.indexOf("null"));
        assertEquals(8, test1.indexOf("world"));
        assertThrows(IllegalArgumentException.class, () -> test1.indexOf(null)); // can't use .compareTo() on null

        assertEquals(-1, test2.indexOf(3.14));

        assertThrows(NullPointerException.class, () -> test3.indexOf(5));
    }

    @Test
    void get() {
        assertEquals("apple", test1.get(0));
        assertEquals("hello", test1.get(4));
        assertEquals("hello", test1.get(6));

        assertThrows(IndexOutOfBoundsException.class, () -> test2.get(9));

        assertThrows(NullPointerException.class, () -> test3.get(8));
    }

    @Test
    void getTwoParameters() {
        String[] test1Array = test1.get("hello", new String[0]);
        assertEquals(3, test1Array.length);

        String[] test1Array2 = test1.get("crazy", new String[0]);
        assertEquals(1, test1Array2.length);

        Double[] test2Array = test2.get(1.25, new Double[0]);
        assertEquals(0, test2Array.length);

        assertThrows(IllegalArgumentException.class, () -> test1.get(null, new String[0]));
        assertThrows(IllegalArgumentException.class, () -> test2.get(2.5, null));
        assertThrows(NullPointerException.class, () -> test3.get(2, new Integer[0]));
    }

    @Test
    void add() {
        test1.add("purple");
        assertEquals(8, test1.indexOf("purple"));
        assertEquals(11, test1.size());

        test2.add(1.27);
        assertEquals(1, test2.size());

        assertThrows(IllegalArgumentException.class, () -> test2.add(null));
        assertThrows(NullPointerException.class, () -> test3.add(9));
    }

    @Test
    void remove() {
        test1.remove(3);
        assertEquals(9, test1.size());
        assertEquals(3, test1.indexOf("hello"));

        assertThrows(IndexOutOfBoundsException.class, () -> test2.remove(0));

        assertThrows(NullPointerException.class, () -> test3.remove(5));
    }

    @Test
    void iterator() {
        int wordCount = 0;
        for (String word : test1) {
            wordCount++;
        }
        assertEquals(10, wordCount);

        Iterator<String> test1Iterator = test1.iterator();
        assertNotNull(test1Iterator);
        assertEquals(true, test1Iterator.hasNext());
        assertEquals("apple", test1Iterator.next());
        assertEquals("apples", test1Iterator.next());
        assertEquals("bye", test1Iterator.next());

        Iterator<Double> test2Iterator = test2.iterator();
        assertNotNull(test2Iterator);
        assertEquals(false, test2Iterator.hasNext());

        assertThrows(NullPointerException.class, () -> test3.iterator());
    }

    @Test
    void testToString() {
        assertEquals("[apple, apples, bye, crazy, hello, hello, hello, " +
                              "null, world, world]", test1.toString());

        assertEquals("[]", test2.toString());

        assertThrows(NullPointerException.class, () -> test3.toString());
    }

    @Test
    void toArray() {
        String[] test1Array = test1.toArray(new String[0]);
        assertEquals(10, test1Array.length);

        String[] test1Array2 = test1.toArray(new String[10]);
        assertEquals(10, test1Array2.length);

        Double[] test2Array = test2.toArray(new Double[100]);
        assertEquals(100, test2Array.length);
        assertEquals(null, test2Array[3]);
        assertEquals(null, test2Array[67]);

        assertThrows(IllegalArgumentException.class, () -> test1.toArray(null));
        assertThrows(NullPointerException.class, () -> test3.toArray(new Integer[2]));
    }
}
