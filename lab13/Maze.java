import java.util.Random;
import java.awt.Color;
import java.util.Observer;
import java.util.Observable;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Explorable maze.
 *  @author Unknown.
 */
public class Maze implements Observer {

    /** Type of maze.
     *     SINGLE_GAP: No more than one path through each cell.
     *     POPEN_SOLVABLE: Solvable with each wall having given probability of
     *           being absent.
     *     BLANK: All walls present.
     */
    public enum MazeType {
        SINGLE_GAP, POPEN_SOLVABLE, BLANK
    }

    /** Updates the drawing of the maze. */
    @Override
    public void update(Observable o, Object arg) {
        MazeExplorer me = (MazeExplorer) o;
        StdDraw.clear();
        draw();
        for (int i = 0; i < N * N; i += 1) {
            if (me.marked[i]) {
                drawEdges(i, me);
            }
        }
        for (int i = 0; i < N * N; i += 1) {
            if (me.marked[i]) {
                draw(i, me);
            }
        }

        StdDraw.show(drawDelayMS);
    }

    /** Returns neighbor vertices of vertex V. */
    public Iterable<Integer> adj(int v) {
        int x = toX(v);
        int y = toY(v);
        TreeSet<Integer> neighbors = new TreeSet<Integer>();
        if (!wallExists(x, y, "North")) {
            neighbors.add(xyTo1D(x, y + 1));
        }

        if (!wallExists(x, y, "East")) {
            neighbors.add(xyTo1D(x + 1, y));
        }

        if (!wallExists(x, y, "South")) {
            neighbors.add(xyTo1D(x, y - 1));
        }

        if (!wallExists(x, y, "West")) {
            neighbors.add(xyTo1D(x - 1, y));
        }

        return neighbors;
    }

    /** Returns x coordinate for vertex V.
      * For example if N = 10, and V = 12, returns 2. */
    public int toX(int v) {
        return v % N + 1;
    }

    /** Returns y coordinate for vertex V.
      * For example if N = 10, and V = 12, returns 1. */
    public int toY(int v) {
        return v / N + 1;
    }

    /** Returns one dimensional coordinate for vertex in position
     *  (X, Y). */
    public int xyTo1D(int x, int y) {
        return (y - 1) * N + (x - 1);
    }

    /** Returns true if wall in direction S from (X, Y) exists. */
    private boolean wallExists(int x, int y, String s) {
        int tx = targetX(x, s);
        int ty = targetY(y, s);

        if (tx == 0 || ty == 0 || tx == N + 1 || ty == N + 1) {
            return true;
        }

        switch (s) {
        case "North":
            return north[x][y];
        case "East":
            return east[x][y];
        case "South":
            return south[x][y];
        case "West":
            return west[x][y];
        default:
            return true;
        }
    }

    /** Returns number of spaces in the maze. */
    public int V() {
        return N * N;
    }

    /** Returns size of the maze. */
    public int N() {
        return N;
    }

    /** Default delay before drawing updated maze. */
    private static final int DEFAULT_DRAW_DELAY = 50;
    /** Default POPEN_SOLVABLE parameter. */
    private static final double DEFAULT_POPEN = 0.48;

    /** Creates a Maze from config file CONFIGFILENAME. */
    public Maze(String configFilename) {
        In in = new In(configFilename);
        int rseed = 0;
        N = 10;
        drawDelayMS = DEFAULT_DRAW_DELAY;
        MazeType mt = MazeType.SINGLE_GAP;
        double pOpen = DEFAULT_POPEN;
        String configPatternString = "(\\w+)\\s*=\\s*([a-zA-Z0-9_.]+)";
        Pattern configPattern = Pattern.compile(configPatternString);

        while (!in.isEmpty()) {
            String thisLine = in.readLine();
            if (thisLine.length() == 0 || thisLine.charAt(0) == '%') {
                continue;
            }

            Matcher m = configPattern.matcher(thisLine);
            if (m.find()) {
                String variable = m.group(1);
                String value = m.group(2);
                switch (variable) {
                case "N":
                    N = Integer.parseInt(value); break;
                case "rseed":
                    rseed = Integer.parseInt(value); break;
                case "pOpen":
                    pOpen = Double.parseDouble(value); break;
                case "drawDelayMS":
                    drawDelayMS = Integer.parseInt(value); break;
                case "MazeType":
                    if (value.equals("SINGLE_GAP")) {
                        mt = MazeType.SINGLE_GAP;
                    }

                    if (value.equals("POPEN_SOLVABLE")) {
                        mt = MazeType.POPEN_SOLVABLE;
                    }

                    if (value.equals("BLANK")) {
                        mt = MazeType.BLANK;
                    }

                    break;
                default:
                    break;
                }
            }
        }
        init(rseed, pOpen, mt);
    }

    /** A Maze with given size DIM, random seed RSEED, parameter P
     *  (not used by all maze types), and type MT. */
    public Maze(int dim, int rseed, double p, MazeType mt) {
        this.N = dim;
        init(rseed, p, mt);
    }

    /** Initialize Maze with random seed RSEED, parameter P
     *  (not used by all maze types), and type MT. */
    private void init(int rseed, double p, MazeType mt) {
        StdDraw.setXscale(0, N + 2);
        StdDraw.setYscale(0, N + 2);
        rgen = new Random(rseed);
        if (mt == MazeType.SINGLE_GAP) {
            generateSingleGapMaze();
        }
        if (mt == MazeType.POPEN_SOLVABLE) {
            generatePopenSolvableMaze(p);
        }
        if (mt == MazeType.BLANK) {
            generateBlankMaze();
        }
    }

    /** Make current maze blank. */
    private void generateBlankMaze() {
        /** Create arrays of all false. */
        north = new boolean[N + 2][N + 2];
        east  = new boolean[N + 2][N + 2];
        south = new boolean[N + 2][N + 2];
        west  = new boolean[N + 2][N + 2];
    }


    /** Make current maze have <= 1 path through it, starting from
     *  (X, Y), assuming MARKED indicates processed nodes. */
    private void generateSingleGapMaze(int x, int y, boolean[][] marked) {

        marked[x][y] = true;
        while (!marked[x][y + 1] || !marked[x + 1][y]
               || !marked[x][y - 1] || !marked[x - 1][y]) {

            while (true) {

                double r = rgen.nextDouble();

                if (r < 0.25 && !marked[x][y + 1]) {
                    north[x][y] = south[x][y + 1] = false;
                    generateSingleGapMaze(x, y + 1, marked);
                    break;
                } else if (r >= 0.25 && r < 0.50 && !marked[x + 1][y]) {
                    east[x][y] = west[x + 1][y] = false;
                    generateSingleGapMaze(x + 1, y, marked);
                    break;
                } else if (r >= 0.5 && r < 3 * 0.25 && !marked[x][y - 1]) {
                    south[x][y] = north[x][y - 1] = false;
                    generateSingleGapMaze(x, y - 1, marked);
                    break;
                } else if (r >= 3 * 0.25 && r < 1.00 && !marked[x - 1][y]) {
                    west[x][y] = east[x - 1][y] = false;
                    generateSingleGapMaze(x - 1, y, marked);
                    break;
                }
            }
        }
    }

    /** Generate single gap maze starting from lower left. */
    private void generateSingleGapMaze() {
        boolean[][] marked = new boolean[N + 2][N + 2];
        for (int x = 0; x < N + 2; x++) {
            marked[x][0] = marked[x][N + 1] = true;
        }
        for (int y = 0; y < N + 2; y++) {
            marked[0][y] = marked[N + 1][y] = true;
        }

        north = new boolean[N + 2][N + 2];
        east  = new boolean[N + 2][N + 2];
        south = new boolean[N + 2][N + 2];
        west  = new boolean[N + 2][N + 2];
        for (int x = 0; x < N + 2; x++) {
            for (int y = 0; y < N + 2; y++) {
                north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
            }
        }

        generateSingleGapMaze(1, 1, marked);
    }


    /** Set current maze so that each wall is open with probability
     *  POPEN. */
    private void generatePopenSolvableMaze(double pOpen) {
        north = new boolean[N + 2][N + 2];
        east  = new boolean[N + 2][N + 2];
        south = new boolean[N + 2][N + 2];
        west  = new boolean[N + 2][N + 2];
        for (int x = 0; x < N + 2; x++) {
            for (int y = 0; y < N + 2; y++) {
                north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
            }
        }

        for (int x = 1; x < N + 1; x += 1) {
            for (int y = 1; y < N + 1; y += 1) {
                double r = rgen.nextDouble();
                if (r < pOpen) {
                    if (inBounds(x, y + 1)) {
                        north[x][y] = south[x][y + 1] = false;
                    }
                }

                r = rgen.nextDouble();
                if (r < pOpen) {
                    if (inBounds(x + 1, y)) {
                        east[x][y] = west[x + 1][y] = false;
                    }
                }

                r = rgen.nextDouble();
                if (r < pOpen) {
                    if (inBounds(x, y - 1)) {
                        south[x][y] = north[x][y - 1] = false;
                    }
                }

                r = rgen.nextDouble();
                if (r < pOpen) {
                    if (inBounds(x - 1, y)) {
                        west[x][y] = east[x - 1][y] = false;
                    }
                }
            }
        }
    }

    /** Returns true iff (X, Y) is inside the bounds of the maze. */
    private boolean inBounds(int x, int y) {
        return (!(x == 0 || x == N + 1 || y == 0 || y == N + 1));
    }

    /** Returns x coordinate of target given source x location X in
     * direction S. */
    private int targetX(int x, String s) {
        if (s.equals("West")) {
            return x - 1;
        }
        if (s.equals("East")) {
            return x + 1;
        }
        return x;
    }

    /** Returns y coordinate of target given source y location Y in
     * direction S. */
    private int targetY(int y, String s) {
        if (s.equals("South")) {
            return y - 1;
        }
        if (s.equals("North")) {
            return y + 1;
        }
        return y;
    }

    /** Draws a filled circle of desired color C in cell I. */
    private void draw(int i, Color c) {
        StdDraw.setPenColor(c);
        int x = toX(i);
        int y = toY(i);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
    }

    /** Draws a filled circle of desired color C in cell (X,Y). */
    private void draw(int x, int y, Color c) {
        StdDraw.setPenColor(c);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
    }

    /** Draws the state of cell I in ME, including any back edges. */
    private void draw(int i, MazeExplorer me) {
        int x = toX(i);
        int y = toY(i);
        if (me.marked[i]) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        }
        if (me.distTo[i] < Integer.MAX_VALUE) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(x + 0.5, y + 0.5, Integer.toString(me.distTo[i]));
        }
    }

    /** Draws the state of edge I in ME. */
    private void drawEdges(int i, MazeExplorer me) {
        int x = toX(i);
        int y = toY(i);
        if (me.edgeTo[i] < Integer.MAX_VALUE) {
            StdDraw.setPenColor(StdDraw.MAGENTA);
            int fromX = toX(me.edgeTo[i]);
            int fromY = toY(me.edgeTo[i]);
            StdDraw.line(fromX + 0.5, fromY + 0.5, x + 0.5, y + 0.5);
        }
    }

    /** draw the maze. */
    private void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= N; x++) {
            for (int y = 1; y <= N; y++) {
                if (south[x][y]) {
                    StdDraw.line(x, y, x + 1, y);
                }
                if (north[x][y]) {
                    StdDraw.line(x, y + 1, x + 1, y + 1);
                }
                if (west[x][y]) {
                    StdDraw.line(x, y, x, y + 1);
                }
                if (east[x][y]) {
                    StdDraw.line(x + 1, y, x + 1, y + 1);
                }
            }
        }
    }

    /** Draws the maze with all spots numbered by 1D index. */
    private void drawDotsByIndex() {
        for (int i = 0; i < V(); i += 1) {
            int x = toX(i);
            int y = toY(i);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(x + 0.5, y + 0.5, Integer.toString(i));
        }
        StdDraw.show();
    }

    /** Draws the maze with all spots numbered by x, y coordinates. */
    private void drawDotsByXY() {
        for (int i = 0; i < V(); i += 1) {
            int x = toX(i);
            int y = toY(i);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(x + 0.5, y + 0.5,
                         Integer.toString(x) + ", " + Integer.toString(y));
        }
        StdDraw.show();
    }


    /* A test client. */
    /* public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int rseed = Integer.parseInt(args[1]);

        Maze maze = new Maze(N, rseed, 0.48, MazeType.POPEN_SOLVABLE);
        StdDraw.show(0);
        maze.draw();
        // MazeExplorer mdfp = new MazeAStarPath(maze, 4, 4, N, N);
        MazeExplorer mdfp = new MazeCycles(maze);
        mdfp.solve();
    }
    */

    /** Dimension of maze. */
    private int N;
    /** D[x][y] iff there is a wall in direction D from cell (x, y). */
    private boolean[][] north, east, south, west;
    /** PRNG for randomly generated mazes. */
    private static Random rgen;
    /** Drawing delay. */
    private static int drawDelayMS = DEFAULT_DRAW_DELAY;
}
