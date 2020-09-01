
import java.util.Observable;
/**
 *  @author Josh Hug
 */

public abstract class MazeExplorer extends Observable {
    /** distTo[k] is current estimated distance to vertex k. */
    protected int[] distTo;
    /** edgeTo[k] is vertex in best path from start. */
    protected int[] edgeTo;
    /** marked[k] is true iff k has been visited. */
    protected boolean[] marked;
    /** Current maze being explored. */
    protected Maze maze;

    /** Notify all Observers of a change. */
    protected void announce() {
        setChanged();
        notifyObservers();
    }

    /** An exploration of M, pre-solution. */
    public MazeExplorer(Maze m) {
        maze = m;

        distTo = new int[maze.V()];
        edgeTo = new int[maze.V()];
        marked = new boolean[maze.V()];
        for (int i = 0; i < maze.V(); i += 1) {
            distTo[i] = Integer.MAX_VALUE;
            edgeTo[i] = Integer.MAX_VALUE;
        }
        addObserver(maze);
    }

    /** Solves the maze, modifying distTo and edgeTo as it goes. */
    public abstract void solve();
}
