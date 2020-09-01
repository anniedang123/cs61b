import static org.junit.Assert.*;
import org.junit.Test;

/** Perform tests of the IntDList class
 */

public class IntDListTest {

    private IntDList d;

    /** Tests the constructor and size operations. */
    @Test
    public void testSize() {
        d = new IntDList();
        assertEquals("Size of empty", 0, d.size());
        d = new IntDList(5);
        assertEquals("Size of singleton", 1, d.size());
        d = new IntDList(5, 10, 15);
        assertEquals("Size of 3-element list", 3, d.size());
    }

    /** Test front and back. */
    @Test
    public void testFrontBack() {
        d = new IntDList(0);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 0, d.getBack());

        d = new IntDList(0, 5);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 5, d.getBack());

        d = new IntDList(0, 5, 10);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 10, d.getBack());

        d = new IntDList(0, 5, 10, 15);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 15, d.getBack());


    }

    /** Tests the get operation. */
    @Test
    public void testGet() {
        d = new IntDList(0, 5, 10, 15);
        assertEquals(".get(0)", 0, d.get(0));
        assertEquals(".get(1)", 5, d.get(1));
        assertEquals(".get(2)", 10, d.get(2));
        assertEquals(".get(3)", 15, d.get(3));

        assertEquals(".get(-1)", 15, d.get(-1));
        assertEquals(".get(-2)", 10, d.get(-2));
        assertEquals(".get(-3)", 5, d.get(-3));
        assertEquals(".get(-4)", 0, d.get(-4));
    }

    /** Test insertBack*/
    @Test
    public void testInsertBack() {
        d = new IntDList();
        d.insertBack(0);

        assertEquals("Size after 1 insert should be 1", 1, d.size());
        assertEquals("First element after inserting 0 should be 0", 0, d.get(0));
        assertEquals("Last element after inserting 0 should be 0", 0, d.get(-1));

        d.insertBack(5);
        assertEquals("Size after 2 inserts should be 2", 2, d.size());
        assertEquals("First element after inserting 5 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 5 should be 5", 5, d.get(1));
        assertEquals("Last element after inserting 5 should be 5", 5, d.get(-1));
        assertEquals("Second to last element after inserting 5 should be 0", 0, d.get(-2));

        d.insertBack(10);
        assertEquals("Size after 3 inserts should be 3", 3, d.size());
        assertEquals("First element after inserting 10 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 10 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 10 should be 10", 10, d.get(2));
        assertEquals("Last element after inserting 10 should be 10", 10, d.get(-1));
        assertEquals("Second to last element after inserting 10 should be 5", 5, d.get(-2));
        assertEquals("Third to last element after inserting 10 should be 0", 0, d.get(-3));

        d.insertBack(15);
        assertEquals("Size after 4 inserts should be 4", 4, d.size());
        assertEquals("First element after inserting 15 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 15 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 15 should be 10", 10, d.get(2));
        assertEquals("Fourth element after inserting 15 should be 15", 15, d.get(3));
        assertEquals("Last element after inserting 15 should be 15", 15, d.get(-1));
        assertEquals("Second to last element after inserting 15 should be 10", 10, d.get(-2));
        assertEquals("Third to last element after inserting 15 should be 5", 5, d.get(-3));
        assertEquals("Fourth to last element after inserting 15 should be 0", 0, d.get(-4));
    }

    /** Test insertBack*/
    @Test
    public void testInsertFront() {
        d = new IntDList();
        d.insertFront(0);

        assertEquals("Size after 1 insert should be 1", 1, d.size());
        assertEquals("First element after inserting 0 should be 0", 0, d.get(0));
        assertEquals("Last element after inserting 0 should be 0", 0, d.get(-1));

        d.insertFront(5);
        assertEquals("Size after 2 inserts should be 2", 2, d.size());
        assertEquals("First element after inserting 5 should be 5", 5, d.get(0));
        assertEquals("Second element after inserting 5 should be 0", 0, d.get(1));
        assertEquals("Last element after inserting 5 should be 0", 0, d.get(-1));
        assertEquals("Second to last element after inserting 5 should be 5", 5, d.get(-2));

        d.insertFront(10);
        assertEquals("Size after 3 inserts should be 3", 3, d.size());
        assertEquals("First element after inserting 10 should be 10", 10, d.get(0));
        assertEquals("Second element after inserting 10 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 10 should be 0", 0, d.get(2));
        assertEquals("Last element after inserting 10 should be 0", 0, d.get(-1));
        assertEquals("Second to last element after inserting 10 should be 5", 5, d.get(-2));
        assertEquals("Third to last element after inserting 10 should be 10", 10, d.get(-3));

        d.insertFront(15);
        assertEquals("Size after 4 inserts should be 4", 4, d.size());
        assertEquals("First element after inserting 15 should be 15", 15, d.get(0));
        assertEquals("Second element after inserting 15 should be 10", 10, d.get(1));
        assertEquals("Third element after inserting 15 should be 5", 5, d.get(2));
        assertEquals("Fourth element after inserting 15 should be 0", 0, d.get(3));
        assertEquals("Last element after inserting 15 should be 0", 0, d.get(-1));
        assertEquals("Second to last element after inserting 15 should be 5", 5, d.get(-2));
        assertEquals("Third to last element after inserting 15 should be 10", 10, d.get(-3));
        assertEquals("Fourth to last element after inserting 15 should be 15", 15, d.get(-4));
    }

    /* Tests deleteBack */
    @Test
    public void testDeleteBack() {
        d = new IntDList(0, 5, 10, 15);
        assertEquals(".deleteBack() value (15)", 15, d.deleteBack());
        assertEquals(".getBack()", 10, d.getBack());
        assertEquals(".size() after 1 deleteBack", 3, d.size());
        assertEquals(".deleteBack() value (10)", 10, d.deleteBack());
        assertEquals(".getBack()", 5, d.getBack());
        assertEquals(".size() after 2 deleteBack", 2, d.size());
        assertEquals(".deleteBack() value (5)", 5, d.deleteBack());
        assertEquals(".getBack()", 0, d.getBack());
        assertEquals(".size() after 3 deleteBack", 1, d.size());
        assertEquals(".deleteBack() value (0)", 0, d.deleteBack());
        assertEquals(".size() after delete", 0, d.size());
    }

    @Test
    public void testToString() {
        d = new IntDList(5, 10, 15, 20);
        assertEquals(".toString()", "[5, 10, 15, 20]", d.toString());
        assertEquals(".toString() of empty", "[]", new IntDList().toString());
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(IntDListTest.class));
    }
}
