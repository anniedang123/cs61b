import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(1, CompoundInterest.numYears(2020));
        assertEquals(0, CompoundInterest.numYears(2019));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10.00, 12.00, 2021), tolerance);
        assertEquals(8.8, CompoundInterest.futureValue(10.00, -12.00, 2020), tolerance);

    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10.00, 12.00, 2021, 3), tolerance);
        assertEquals(7.62129, CompoundInterest.futureValueReal(10.00, -10.00, 2021, 3), tolerance);

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(5000, CompoundInterest.totalSavings(5000, 2019, 10), tolerance);
        assertEquals(9500, CompoundInterest.totalSavings(5000, 2020, -10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(5000, CompoundInterest.totalSavingsReal(5000, 2019, 10,10), tolerance);
        assertEquals(8550, CompoundInterest.totalSavingsReal(5000, 2020, -10,10), tolerance);

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
