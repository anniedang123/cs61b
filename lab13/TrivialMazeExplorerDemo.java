/**
 *  @author Josh Hug
 */

public class TrivialMazeExplorerDemo {
    /** Demonstrate maze exploration. */
    public static void main(String[] unused) {
        Maze maze = new Maze("maze.config");
        TrivialMazeExplorer tme = new TrivialMazeExplorer(maze);
        tme.solve();
    }
}
