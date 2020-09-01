/**
 * @author Created by joshuazeitsoff on 9/2/17.
 */

public class BuggyIntDListSolution extends IntDList {

    /**
     *
     * @param values creates a BuggyIntDListSolution with ints values.
     */
    public BuggyIntDListSolution(Integer... values) {
        super(values);
    }

    /**
     *
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        _back = new DNode(_back, d, null);
        if (_front == null)
            _front = _back;
        else{
            _back._prev._next = _back;
        }
    }

    /**
     *
     * Hint: this is what comes after the "java.lang" at the top
     * of the stack trace.
     * @return The name of the type of exception that is thrown.
     */
    public String getException() {
        return "NullPointerException";
    }

    /**
     *
     * Hint: This is the first function name that you see when reading
     * the stack trace from top to bottom.
     * @return The name of the function in which the error occurs.
     */
    public String getErrorFunction() {
        return "insertBack";
    }

    /**
     *
     * Hint: This is the number that comes after whichever function
     * the error is occurring in.
     * @return The line number at which the error occurs
     */
    public int getErrorLineNumber() {
        return 21;
    }
}
