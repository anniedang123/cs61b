import net.sf.saxon.expr.accum.Accumulator;

/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        AddFunction func = new AddFunction(n);
        return L.map(func);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        Accum sum = new Accum();
        L.map(sum);
        return sum.count();
    }

    private static class AddFunction implements IntUnaryFunction {
        private int _n;

        public AddFunction(int n) {
            _n = n;
        }

        public int apply(int x) {
            return _n + x;
        }

        int getN() {
            return _n;
        }
    }

    private static class Accum implements IntUnaryFunction {

        private int _counter = 0;

        public int apply(int x) {
            _counter += x;
            return _counter;
        }

        int count() {
            return _counter;
        }
    }
    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
