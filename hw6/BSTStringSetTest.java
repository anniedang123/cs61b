import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;


/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    @Test
    public void testPut() {
        BSTStringSet T = new BSTStringSet();
        T.put("Kiwi");
        T.put("Pear");
        T.put("Pineapple");
        T.put("Apple");
        T.put("Orange");
        System.out.println(T.asList());

        ArrayList<String> A = new ArrayList<String>();
        A.add("Apple");
        A.add("Kiwi");
        A.add("Orange");
        A.add("Pear");
        A.add("Pineapple");

        assertEquals(T.asList(), A);
    }

    public static void main(String[] args) {
    }

}
