/**
 * @author Created by joshuazeitsoff on 9/2/18.
 */

public class BuggyIntDList extends IntDList {

    /**
     *
     * @param values creates a BuggyIntDList with ints values.
     */
    public BuggyIntDList(Integer... values) {
        super(values);
    }

    /**
     *
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        _back = new DNode(_back, d, null);
        _back._prev._next = _back;
    }
}
