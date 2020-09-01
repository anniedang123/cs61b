import java.util.LinkedList;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits visible fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */
    private int s;
    private int t;
    private boolean tf = false;
    private Maze maze;
    private LinkedList<Integer> fringe = new LinkedList<Integer>();


    /** A breadth-first search of paths in M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY). */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY,
                                 int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        fringe.add(s);
        marked[s] = true;
        announce();
        if(s==t){
            tf=true;
        }
        if(tf){
            return;
        }

        while(!fringe.isEmpty()){
            int v = fringe.pollFirst();
            for(int w:maze.adj(v)){
                if(!marked[w]){
                    fringe.add(w);
                    marked[w]=true;
                    edgeTo[w]=v;
                    announce();
                    distTo[w]=distTo[v]+1;
                    if(t==w){
                        tf=true;
                    }
                    if(tf){
                        announce();
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

