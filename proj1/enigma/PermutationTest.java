package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static enigma.TestUtils.*;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", alphabet);
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('A'), 'W');
        assertEquals(p.invert('W'), 'X');
        assertEquals(p.invert('J'), 'C');
        assertEquals(p.invert('C'), 'J');
        assertEquals(p.invert('P'), 'H');
        assertEquals(p.invert('H'), 'N');
    }

    @Test
    public void testPermuteChar() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", alphabet);
        assertEquals(p.permute('A'), 'B');
        assertEquals(p.permute('G'), 'G');
        assertEquals(p.permute('W'), 'A');
        assertEquals(p.permute('X'), 'W');
        assertEquals(p.permute('J'), 'C');
        assertEquals(p.permute('C'), 'J');
        assertEquals(p.permute('P'), 'N');
        assertEquals(p.permute('H'), 'P');
    }

    @Test
    public void testDerangement() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", alphabet);
        assertEquals(p.derangement(), false);
        Permutation d = new Permutation(
                "(ABCDEFGH) (IJKL) (MNOPQRSTUVWXYZ)", alphabet);
        assertEquals(d.derangement(), true);
        Permutation f = new Permutation(
                "(ABCDEFG) (H) (IJKL) (MNOPQRSTUVWXYZ)", alphabet);
        assertEquals(f.derangement(), false);
    }


}
