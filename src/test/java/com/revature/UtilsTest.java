package com.revature;

// Junit imports
import static org.junit.Assert.assertEquals;
import org.junit.*;

// Utils imports
import com.revature.courseapp.utils.*;

public class UtilsTest {
    @Test
    public void ListInstantiation () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3});
        assertEquals("{1, 2, 3}", testList.toString());
    }

    @Test
    public void ListGet () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3});
        assertEquals(1, (int)testList.get(0));
        assertEquals(2, (int)testList.get(1));
        assertEquals(3, (int)testList.get(2));
    }

    @Test
    public void ListGetAdd () {
        LinkedList<Integer> testList = new LinkedList<Integer>();
        testList.add (1);
        testList.add (2);
        testList.add (3);
        testList.add (6);
        testList.add (7);
        assertEquals(1, (int)testList.get(0));
        assertEquals(2, (int)testList.get(1));
        assertEquals(3, (int)testList.get(2));
        assertEquals(6, (int)testList.get(3));
        assertEquals(7, (int)testList.get(4));
    }

    @Test
    public void ListGetAdd2 () {
        LinkedList<Integer> testList = new LinkedList<Integer>();
        testList.add (5);
        testList.add (11);
        testList.add (3);
        testList.add (10);
        testList.add (7);
        assertEquals(5, (int)testList.get(0));
        assertEquals(11, (int)testList.get(1));
        assertEquals(3, (int)testList.get(2));
        assertEquals(10, (int)testList.get(3));
        assertEquals(7, (int)testList.get(4));
    }

    @Test
    public void ListAddTwoLists () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3,4});
        LinkedList<Integer> testList2 = new LinkedList<Integer>(new Integer[]{5,6,7,8,9,10});
        testList.addAll (testList2);
        assertEquals(1, (int)testList.get(0));
        assertEquals(2, (int)testList.get(1));
        assertEquals(5, (int)testList.get(4));
        assertEquals(6, (int)testList.get(5));
        assertEquals("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}", testList.toString());
    }

    @Test
    public void ListSize () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3});
        assertEquals(3, testList.size());
        testList.add(1);
        testList.add(5);
        testList.add(6);
        assertEquals(6, testList.size());
        testList.remove();
        assertEquals(5, testList.size());
        testList.remove();
        testList.remove();
        assertEquals(3, testList.size());
    }

    @Test
    public void ListSet () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3});
        assertEquals(3, testList.size());
        testList.set(2, 6);
        assertEquals(6, (int)testList.get(2));
        testList.set(1, 3);
        assertEquals(3, (int)testList.get(1));
    }

    @Test
    public void ListRemoveTail () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3,4,5});
        assertEquals(5, testList.size());

        Integer removedInt = testList.remove(); // end of list remove
        assertEquals(5, (int)removedInt);
        assertEquals(4, testList.size());
        assertEquals("{1, 2, 3, 4}", testList.toString());
        assertEquals(4, (int)testList.get(3));

        removedInt = testList.remove(); // end of list remove
        assertEquals(4, (int)removedInt);
        assertEquals(3, testList.size());
        assertEquals("{1, 2, 3}", testList.toString());
        assertEquals(3, (int)testList.get(2));

        removedInt = testList.remove(); // end of list remove
        assertEquals(3, (int)removedInt);
        removedInt = testList.remove(); // end of list remove
        assertEquals(2, (int)removedInt);
        assertEquals(1, testList.size());
        assertEquals("{1}", testList.toString());
        assertEquals(1, (int)testList.get(0));

        removedInt = testList.remove(); // end of list remove
        assertEquals(1, (int)removedInt);
        assertEquals(0, testList.size());
        assertEquals("{}", testList.toString());
        assertEquals(null, testList.get(0));
    }

    @Test
    public void ListRemoveIndex () {
        LinkedList<Integer> testList = new LinkedList<Integer>(new Integer[]{1,2,3,4,5});
        assertEquals(5, testList.size());

        Integer removedInt = testList.removeAtIndex(3); // remove at index
        assertEquals(4, (int)removedInt);
        assertEquals(4, testList.size());
        assertEquals("{1, 2, 3, 5}", testList.toString());
        assertEquals(5, (int)testList.get(3));

        removedInt = testList.removeAtIndex(2);
        assertEquals(3, (int)removedInt);
        assertEquals(3, testList.size());
        assertEquals("{1, 2, 5}", testList.toString());
        assertEquals(2, (int)testList.get(1));

        // Test out of range
        removedInt = testList.removeAtIndex(7);
        assertEquals(null, removedInt);
        removedInt = testList.removeAtIndex(-2);
        assertEquals(null, removedInt);

        removedInt = testList.removeAtIndex(1);
        assertEquals(2, (int)removedInt);

        removedInt = testList.removeAtIndex(0);
        assertEquals(1, (int)removedInt);
        assertEquals(1, testList.size());
        assertEquals("{5}", testList.toString());
        assertEquals(5, (int)testList.get(0));

        removedInt = testList.removeAtIndex(0);
        assertEquals(5, (int)removedInt);
        assertEquals(0, testList.size());
        assertEquals("{}", testList.toString());
        assertEquals(null, testList.get(0));
    }
}
