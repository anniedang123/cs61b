import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by joshuazeitsoff on 9/2/18.
 */
public class BuggyIntDListTest {

    /* Tests insertBack. */
    @Test
    public void testInsertBack() {
        BuggyIntDList d = new BuggyIntDList(5);

        assertEquals("getBack after inserting 5 should be 5", 5, d.getBack());
        assertEquals("getFront after inserting 5 should be 5", 5, d.getFront());

        d.insertBack(10);
        assertEquals("getBack after inserting 10 should be 10", 10, d.getBack());
        assertEquals("getFront after inserting 10 should be 5", 5, d.getFront());

        d.insertBack(15);
        assertEquals("getBack after inserting 15 should be 15", 15, d.getBack());
        assertEquals("getFront after inserting 15 should be 5", 5, d.getFront());

    }
}
