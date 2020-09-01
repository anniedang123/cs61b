package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** Array Tests
 *  @author
 */

public class ArraysTest {
    /** test methods in Array
     */
    @Test
    public boolean testCatenate() {
        int[] A = {1, 4, 5},
                B = {8, 6},
                C = {},
                D = {1, 4, 5, 8, 6};
        return Utils.equals(Arrays.catenate(A, B), D) && Utils.equals(Arrays.catenate(A, C), A);
    }
    @Test
    public boolean testRemove() {
        int[] A = {1, 2, 3, 4, 5, 6},
                B = {1, 5, 6};
        return Utils.equals(Arrays.remove(A, 2, 3), B);
    }
    @Test
    public boolean testNaturalRuns2() {
        int[] A = {1, 3, 7, 5, 4, 6, 9, 10},
                B = {};
        int[][] C = new int[3][],
                D = new int[0][];
        C[0] = new int[]{1, 3, 7};
        C[1] = new int[]{5};
        C[2] = new int[]{4, 6, 9, 10};
        return Utils.equals(Arrays.naturalRuns(A), C) &&
                Utils.equals(Arrays.naturalRuns(B), D);
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
