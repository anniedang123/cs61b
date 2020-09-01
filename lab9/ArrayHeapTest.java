import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapTest {

    /** Basic test of adding, checking, and removing two elements from a heap */
    @Test
    public void simpleTest() {
        ArrayHeap<String> pq = new ArrayHeap<>();
        pq.insert("Tab", 2);
        pq.insert("Lut", 1);
        assertEquals(2, pq.size());

        String first = pq.removeMin();
        assertEquals("Lut", first);
        assertEquals(1, pq.size());

        String second = pq.removeMin();
        assertEquals("Tab", second);
        assertEquals(0, pq.size());
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArrayHeapTest.class));
    }


}
