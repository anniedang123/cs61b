package gitlet;

import java.io.File;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/** GL class.
 * @author annie dang
 */

public class GL implements Serializable {

    /**
     * Gitlet constructor.
     */
    public GL() {
        File gl = new File(".gitlet/gitlet");
        if (gl.exists()) {
            GL obj;
            try {
                ObjectInputStream in =
                        new ObjectInputStream(new FileInputStream(gl));
                obj = (GL) in.readObject();
                in.close();
                im(obj);
            } catch (GitletException
                    | IOException | ClassNotFoundException exception) {
                System.out.println(exception);
            }
        } else {
            curRem = new ArrayList<>();
            currCom = new LinkedHashMap<>();
            currBl = new HashMap<>();
            currStage = new HashMap<>();
            curBra = new HashMap<>();
            currSpl = new HashMap<>();
            curUnTrac = new LinkedHashMap<>();
            tbr = new ArrayList<>();
            currPath = null;
            currBr = null;
            currPath = null;
        }
    }

    /**
     * imports existing repo using CURR.
     * @throws GitletException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void im(GL curr)
            throws GitletException {
        curRem = curr.curRem;
        tbr = curr.tbr;
        currPath = curr.currPath;
        currStage = curr.currStage;
        curUnTrac = curr.curUnTrac;
        curBra = curr.curBra;
        currCom = curr.currCom;
        currBl = curr.currBl;
        currBr = curr.currBr;
        currSpl = curr.currSpl;

    }

    /** Current path.*/
    protected String currPath;
    /** Current branch.*/
    protected String currBr;
    /** Files to be remove in the next commit.*/
    protected ArrayList<String> tbr;
    /** MyStaged files hashmap.*/
    protected HashMap<String, Blob> currStage;
    /** Removed files.*/
    protected ArrayList<String> curRem;
    /** Commits and hashcodes Linkedhashmap.*/
    protected LinkedHashMap<String, GitCommands> currCom;
    /** Hashes myBranches with their hashcode.*/
    protected HashMap<String, String> curBra;
    /** Hashes myBranches with their hashcode. */
    protected HashMap<String, String> currSpl;
    /** Files' names and locations hasmap. */
    protected HashMap<String, Blob> currBl;
    /** HashMap storing all commits and their hashcodes.*/
    protected LinkedHashMap<String, Blob> curUnTrac;
}
