import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Sample test that verifies correctness of the IntList.list static
     *  method. The main point of this is to convince you that
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.
     *
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with
     *  IntList empty = IntList.list().
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A.
     */

    @Test
    public void testDcatenate() {
        IntList one = new IntList(1, null);
        IntList two = new IntList(2, one);
        IntList three = new IntList(3, two);
        assertEquals(IntList.dcatenate(IntList.list(), IntList.list(3,2,1)), three);
        assertEquals(IntList.dcatenate(IntList.list(3,2,1),IntList.list()), three);
    }


    /** Tests that subtail works properly. Again, don't use new.
     *
     *  Make sure to test that subtail does not modify the list.
     */

    @Test
    public void testSubtail() {
        IntList one = new IntList(1, null);
        IntList two = new IntList(2, one);
        IntList three = new IntList(3, two);
        IntList subT = IntList.subTail(three, -1);
        subT = IntList.subTail(three, 1);
        assertEquals(two, subT);
    }

    /** Tests that sublist works properly. Again, don't use new.
     *
     *  Make sure to test that sublist does not modify the list.
     */

    @Test
    public void testSublist() {
        IntList one = new IntList(1, null);
        IntList two = new IntList(2, one);
        IntList three = new IntList(3, two);
        IntList subT = IntList.sublist(three, -1,0);
        subT = IntList.sublist(three, 1,2);
        assertEquals(two, subT);
    }

    /** Tests that dSublist works properly. Again, don't use new.
     *
     *  As with testDcatenate, it is not safe to assume that list passed
     *  to dSublist is the same after any call to dSublist
     */

    @Test
    public void testDsublist() {
        IntList one = new IntList(1, null);
        IntList two = new IntList(2, one);
        IntList three = new IntList(3, two);
        IntList subT = IntList.dsublist(three, -1,0);
        subT = IntList.dsublist(three, 1,2);
        assertEquals(three.tail, subT);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
