package gitlet;

import java.io.File;
import java.util.ArrayList;

/** Driver main class for Gitlet.
 *  @author annie dang
 */

public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws Exception {
        ArrayList<String> input = new ArrayList<String>();
        for (String each : args) {
            input.add(each);
        }
        Main m = new Main();
        GL g = new GL();
        gitlet.git = g;
        m.operate(input, g, args);
        Utils.writeContents(
                new File(".gitlet/gitlet"), Utils.serialize(g));
    }

    /** The /.gitlet directory. */
    private static final File DIR = new File(".gitlet/");
    /** gitlet commands. */
    private static GitCommands gitlet;
    /** Input from command line. */
    private ArrayList<String> input;

    /** Perform git commands. Using IN, GIT, ARGS. */
    public void operate(
            ArrayList<String> in, GL git, String...args) throws Exception {
        if (in.isEmpty()) {
            System.out.println("Please enter a command.");
            return;
        }
        String comd = in.remove(0);
        input = in;
        switch (comd) {
        case "init":
            init();
            break;
        case "add":
            add();
            break;
        case "commit":
            commit();
            break;
        case "branch":
            branch();
            break;
        case "rm-branch":
            removeBranch();
            break;
        case "rm":
            remove();
            break;
        case "log":
            log();
            break;
        case "global-log":
            globalLog();
            break;
        case "status":
            status();
            break;
        case "find":
            find();
            break;
        case "reset":
            reset();
            break;
        case "merge":
            merge();
            break;
        case "checkout":
            cheout(args);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
            break;
        }
    }

    /** Initializes. */
    public void init() throws Exception {
        if (input.size() != 0) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (DIR.isDirectory() && DIR.exists()) {
            System.out.print("A gitlet version-control system already exists");
            System.out.println("in the current directory.");
            return;
        }
        gitlet.init();
    }

    /** Adds. */
    public void add() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.add(input.get(0));
    }

    /** Commits. */
    public void commit() {
        if (input.get(0).isEmpty()
                || input.get(0).equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.commit(input.get(0));
    }

    /** Branch. */
    public void branch() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.branch(input.get(0));
    }

    /** Removes branch. */
    public void removeBranch() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.removeBranch(input.get(0));
    }

    /** Removes. */
    public void remove() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.rm(input.get(0));
    }

    /** Finds. */
    public void find() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.find(input.get(0));
    }

    /** Log. */
    public void log() {
        if (input.size() != 0) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.log();
    }

    /** Global log. */
    public void globalLog() {
        if (input.size() != 0) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.globalLog();
    }

    /** Status. */
    public void status() {
        if (input.size() != 0) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.status();
    }

    /** Reset. */
    public void reset() {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.reset(input.get(0));
    }

    /** Merges. */
    public void merge() throws Exception {
        if (input.size() != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.merge(input.get(0));
    }

    /** Checkout using ARGS. */
    public void cheout(String... args) {
        if (args.length == 0 || args.length > 4) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        gitlet.checkout(args);
    }
}
