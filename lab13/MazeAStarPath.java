/**
 *  @author Josh Hug
 */

public class MazeAStarPath extends MazeExplorer {
    /** Source. */
    private int s;
    /** Target. */
    private int t;
    /** True iff target has been processed. */
    private boolean targetFound = false;

    /** Set up to find path through M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY) via A* search. */
    public MazeAStarPath(Maze m, int sourceX,
                         int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Returns estimate of the distance from V to the target. */
    private int h(int v) {
        return -1;
    }

    /** Returns vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex START. */
    private void astar(int start) {
    }

    @Override
    public void solve() {
        astar(s);
    }

}

