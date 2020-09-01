/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits protected fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    private int[] previous;

    /** Set up to find cycles of M. */
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = maze.xyTo1D(1, 1);
        previous = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(s);
    }

    private void dfs(int v) {
        marked[v] = true;
        announce();
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                previous[w]=v;
                System.out.println("w: " + w + " v: "+ v);
                dfs(w);
            }
            else{
                if(previous[v]!=w){
                    System.out.println("w: " + w + " v: "+ v);
                    return;
                }
            }
        }
    }
}

