package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** List Tests
 *
 *  @author
 */

public class ListsTest {
    /** Test Natural Run with (1, 3, 7, 5, 4, 6, 9, 10, 10, 11)
     */
    @Test
    private static boolean testNaturalRuns() {
        int[][] r = new int[3][];
        r[0] = new int[]{1, 3, 7};
        r[1] = new int[]{5};
        r[2] = new int[]{4, 6, 9, 10};
        r[3] = new int[]{10, 11};
        IntListList p = Lists.naturalRuns(IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11));
        return p.equals(IntListList.list(r));
    }
    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
