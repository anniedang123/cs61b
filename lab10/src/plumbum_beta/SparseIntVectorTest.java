package plumbum_beta;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseIntVectorTest {
	@Test
	public void testDot() {
		SparseIntVector a = new SparseIntVector(0, 2, 0, 1, 0, 0, -10, 6, 0, 0, 0, 40);
		SparseIntVector b = new SparseIntVector(0, 0, 0, 4, 0, 3, 18, 0, 0, 0, 9, 10);
		SparseIntVector c = new SparseIntVector(0, 0, 43, 0, 0, 23, 0, 0, 0, -14, 0, 0);

		assertEquals(224, SparseIntVector.dot(a, b));
		assertEquals(69, SparseIntVector.dot(b, c));
		assertEquals(0, SparseIntVector.dot(a, c));

		assertTrue(SparseIntVector.dot(a, b) == SparseIntVector.dot(b, a));
	}

	@Test
	public void testSize() {
		SparseIntVector a = new SparseIntVector(0, 2, 0);
		SparseIntVector b = new SparseIntVector(0, 0, 0, 0, 0, 0);
		SparseIntVector c = new SparseIntVector(1, 2, 3, 5, 19, 29, 10, 58, 128);
		SparseIntVector d = new SparseIntVector();

		assertEquals(3, a.size());
		assertEquals(6, b.size());
		assertEquals(9, c.size());
		assertEquals(0, d.size());
	}
}
