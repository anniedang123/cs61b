package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** All commands of Gitlet.
 * @author annie dang
 */
public class GitCommands implements Serializable {

    /** Constructor. BLB, PAR, MESS, BRNCH as arguments.*/
    GitCommands(HashMap<String, Blob> blb, String par,
                String mess, String brnch) {
        ZonedDateTime rn = ZonedDateTime.now();
        messa = mess;
        time = rn.format(DateTimeFormatter.ofPattern(
                "EEE MMM dd HH:mm:ss yyyy xxxx"));
        hash = hashHelper();
        bran = brnch;
        parrent = par;
        blob = blb;
        bran2 = null;
    }

    /** Constructor. BLB, PAR, MESS, BRNCH, BRNCH2 as args. */
    GitCommands(HashMap<String, Blob> blb, String par,
           String mess, String brnch, String brnch2) {
        ZonedDateTime rn = ZonedDateTime.now();
        messa = mess;
        time = rn.format(DateTimeFormatter.ofPattern(
                "EEE MMM dd HH:mm:ss yyyy xxxx"));
        hash = hashHelper();
        bran = brnch;
        parrent = par;
        bran2 = brnch2;
        blob = blb;
    }

    /** Constructor w MESS & T. BRNCH of commit. */
    GitCommands(String mess, String t, String brnch) {
        messa = mess;
        time = t;
        hash = hashHelper();
        bran = brnch;
        parrent = null;
        bran2 = null;
        blob = null;
    }

    /** Hashes commit, returns HASH. */
    public String hashHelper() {
        List<Object> h = new ArrayList<>();
        h.add(messa);
        h.add(time);
        if (blob != null) {
            Set<String> s = blob.keySet();
            for (Iterator ks = s.iterator(); ks.hasNext(); ) {
                String x = (String) ks.next();
                h.add(x);
            }
        }
        if (parrent != null) {
            h.add(parrent);
        }
        return Utils.sha1(h);
    }

    /** Hashes commit. Returns MYHASH of commit.*/
    public String hash() {
        return hash;
    }
    /** Gets parent. Returns PARENT.*/
    String getParent() {
        return parrent;
    }
    /** Gets time. Returns MYTIME.*/
    String getTime() {
        return time;
    }
    /** Gets commit message. Returns MYMESSAGE message.*/
    String getMessage() {
        return messa;
    }
    /** Map of all files to be committed. Returns MYBLOBS.*/
    HashMap<String, Blob> getBlobs() {
        return blob;
    }
    /** Gets a file from commit. NAME of file to get and returns the file.*/
    Blob getBlob(String name) {
        return blob.get(name);
    }
    /**Gets all the files to be committed. Returns all files to be committed.*/
    Set<String> getFiles() {
        return blob.keySet();
    }
    /** Gets the branch of this commit. Returns the branch.*/
    String getBranch() {
        return bran;
    }


    /** Deserialize the file in DIR. Returns NULL if the file does not exist. */
    static GitCommands deserialize(File dir) {
        GitCommands outcome;
        try {
            ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(dir));
            outcome = (GitCommands) input.readObject();
            input.close();
        } catch (IOException excp) {
            throw new Error("Internal error deserializing.");
        } catch (ClassNotFoundException excp2) {
            throw new Error("Internal Error deserializing.");
        }
        return outcome;
    }

    /** Deserialize the serializable OBJ. Returns serialiazble obj. */
    protected static byte[] serialize(Serializable obj) {
        return Utils.serialize(obj);
    }

    /** Creates a new Gitlet version-control system in the current directory.
     * @throws Exception for exceptions. */
    protected static void init() throws Exception {
        Path gl = Paths.get(GITPATH);
        Files.createDirectories(gl);
        Path stage = Paths.get(STGPATH);
        Files.createDirectory(stage);
        Path comm = Paths.get(COMMPATH);
        Files.createDirectory(comm);
        GitCommands initC = new GitCommands("initial commit",
                "Thu Jan 01 00:00:00 1970 -0800", "master");
        git.currPath = initC.hash();
        String icHash = initC.hash();
        git.currCom.put(icHash, initC);
        git.currBr = "master";
        branch("master");
        Utils.writeContents(new File(COMMPATH
                        + git.currPath), serialize(initC));
    }

    /** Adds a copy of the file as it currently exists to the staging area
     * using the NAME of the file.*/
    protected static void add(String name) {
        File file = new File(name);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Blob b = new Blob(name);
        String bHash = b.hash();
        GitCommands thisC = deserialize(new File(
                COMMPATH + git.currPath));
        boolean identical = thisC.getBlobs() != null
                && thisC.getBlobs().containsKey(name);
        if (identical) {
            if (thisC.getBlobs().get(name).hash().equals(b.hash())) {
                if (git.currStage.containsKey(name)) {
                    git.currStage.remove(name);
                    new File(STGPATH
                            + git.currStage.get(name).hash()).delete();
                    return;
                }
                return;
            }
            if (git.currStage.containsKey(name)) {
                git.currStage.put(name, b);
                return;
            }
        }
        if (git.tbr.contains(name)) {
            git.curUnTrac.remove(name);
            git.tbr.remove(name);
            return;
        }
        git.currStage.put(name, b);
        Utils.writeContents(new File(STGPATH + bHash),
                serialize(b));
    }

    /** Saves a snapshot of files current commit/staging area. MSG. */
    protected static void commit(String msg) {
        if (git.tbr.size() == 0 && git.currStage.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        helpter();
        GitCommands commit = new GitCommands(git.currBl,
                        git.currPath, msg, git.currBr);
        git.currPath = commit.hash();
        String cP = COMMPATH + git.currPath;
        File n = new File(cP);
        Utils.writeContents(n, serialize(commit));
        File[] f = new File(STGPATH).listFiles();
        for (int i = 0; i < f.length; i++) {
            f[i].delete();
        }
        git.currCom.put(git.currPath, commit);
        git.curBra.put(git.currBr, git.currPath);
        git.currStage.clear();
        git.tbr.clear();
    }

    /** Helper method for commit. */
    private static void helpter() {
        if (git.currStage.size() != 0) {
            Set<String> x = git.currStage.keySet();
            for (Iterator ks = x.iterator(); ks.hasNext(); ) {
                String p = (String) ks.next();
                Blob stage = git.currStage.get(p);
                git.currBl.put(p, stage);
            }
        }
        if (git.tbr.size() != 0) {
            ArrayList<String> o = git.tbr;
            for (int i = 0; i < o.size(); i++) {
                if (git.currBl.containsKey(o.get(i))) {
                    git.currBl.remove(o.get(i));
                }
            }
        }
    }

    /** Removes a file from staging using file NAME. */
    protected static void rm(String name) {
        File f = new File(COMMPATH + git.currPath);
        GitCommands commit = deserialize(f);
        if ((git.currStage != null && git.currStage.containsKey(name))
                || (commit.getBlobs() != null)
                && commit.getBlobs().containsKey(name)) {
            if (commit.getBlobs() != null
                && commit.getBlobs().containsKey(name)) {
                if (git.curUnTrac.containsKey(name)) {
                    git.curUnTrac.remove(name);
                }
                if (new File(name).exists()) {
                    new File(name).delete();
                }
                for (int i = 0; i < 2; i++) {
                    if (!git.tbr.contains(name)) {
                        git.tbr.add(name);
                    }
                }
            }
            if (git.currStage != null) {
                if (git.currStage.containsKey(name)) {
                    new File(STGPATH
                            + git.currStage.get(name).hash()).delete();
                    git.curUnTrac.put(name, new Blob(name));
                    git.curRem.add(name);
                    git.currStage.remove(name);
                }
            }
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /** Shows previous commit information.*/
    protected static void log() {
        List<String> name = new ArrayList<>();
        File c = new File(COMMPATH + git.currPath);
        name.add(deserialize(c).hash());
        GitCommands cc = deserialize(c);
        while (cc.getParent() != null) {
            name.add(cc.getParent());
            File des = new File(COMMPATH + cc.getParent());
            cc = deserialize(des);
        }
        for (int i = 0; i < name.size(); i++) {
            System.out.println("===");
            System.out.println("commit " + name.get(i));
            System.out.println("Date: "
                    + git.currCom.get(name.get(i)).getTime());
            System.out.println(git.currCom.get(name.get(i)).getMessage());
            System.out.println();
            if (git.currCom.get(name.get(i)).getParent() == null) {
                break;
            }
        }
    }

    /** Helper method to reverse the LIST using T. */
    public static <T> void reverseList(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        T value = list.remove(0);
        reverseList(list);
        list.add(value);
    }

    /** Displays all commits information.*/
    protected static void globalLog() {
        List<String> name = new ArrayList<>(git.currCom.keySet());
        reverseList(name);
        for (String hash : name) {
            System.out.println("===");
            System.out.println("commit " + hash);
            System.out.println("Date: " + git.currCom.get(hash).getTime());
            System.out.println(git.currCom.get(hash).getMessage());
            System.out.println();
            if (git.currCom.get(hash).getParent() == null) {
                break;
            }
        }
    }

    /** Prints IDs commits with given commit MESSAGE.*/
    protected static void find(String message) {
        int in = 0;
        Set<String> set = git.currCom.keySet();
        for (Iterator ks = set.iterator(); ks.hasNext(); ) {
            String f = (String) ks.next();
            if (git.currCom.get(f).getMessage().equals(message)) {
                System.out.println(git.currCom.get(f).hash());
                in++;
            }
        }
        if (in == 0) {
            System.out.println("Found no commit with that message.");
        }
    }


    /** Empty branch helper for status.*/
    private static void emptyBranch() {
        Set<String> set = git.curBra.keySet();
        if (!git.curBra.isEmpty()) {
            for (Iterator ks = set.iterator(); ks.hasNext(); ) {
                String b = (String) ks.next();
                if (!b.equals(git.currBr)) {
                    System.out.println(b);
                }
            }
        }
    }

    /** Empty stage helper for status.*/
    private static void emptyStage() {
        Set<String> set = git.currStage.keySet();
        if (!git.currStage.isEmpty()) {
            for (Iterator ks = set.iterator(); ks.hasNext(); ) {
                String f = (String) ks.next();
                System.out.println(f);
            }
        }
    }

    /** Empty to be removed helper for status.*/
    private static void emptyRemoved() {
        ArrayList<String> set = git.tbr;
        if (!git.tbr.isEmpty()) {
            for (int i = 0; i < set.size(); i++) {
                System.out.println(set.get(i));
            }
        }
    }

    /** Status of current branches.*/
    protected static void status() {
        System.out.println("=== Branches ===");
        System.out.println("*" + git.currBr);
        emptyBranch();
        System.out.println();
        System.out.println("=== Staged Files ===");
        emptyStage();
        System.out.println();
        System.out.println("=== Removed Files ===");
        emptyRemoved();
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** Checkout method using ARG. */
    protected static void checkout(String[] arg) {
        if (!git.curUnTrac.isEmpty()) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it or add it first.");
            System.exit(0);
        }
        if (arg.length == 3) {
            Checkout.checkout1(arg[2]);
        } else if (arg.length ==  4 && arg[2].equals("--")) {
            Checkout.checkout2(arg[1], arg[3]);
        } else if (arg.length == 2) {
            Checkout.checkout3(arg[1]);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /** Create a branch with the given N.*/
    protected static void branch(String n) {
        if (!git.curBra.containsKey(n)) {
            git.curBra.put(n, git.currPath);
            git.currSpl.put(n, git.currPath);
        } else {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
    }

    /** Delete branch with N.*/
    protected static void removeBranch(String n) {
        if (n.equals(git.currBr)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else if (git.curBra.containsKey(n)) {
            git.curBra.remove(n);
        } else {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }

    /** Reset using N.*/
    protected static void reset(String n) {
        if (!git.currCom.containsKey(n)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        GitCommands com = deserialize(new File(COMMPATH + n));
        List<String> l = Utils.plainFilenamesIn(MYPTAH);
        for (int i = 0; i < l.size(); i++) {
            if (!git.currStage.containsKey(l.get(i))
                && !git.currBl.containsKey(l.get(i))
                && com.getBlobs().containsKey(l.get(i))) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }
        helper(com);
        helper2();
        git.currStage.clear();
        git.tbr = new ArrayList<>();
        git.currPath = com.hash();
        git.currBl = com.getBlobs();
        git.currBr = com.getBranch();
        git.curBra.put(git.currBr, n);
    }

    /** Helper method for rmbranch.*/
    private static void helper2() {
        File[] o = new File(STGPATH).listFiles();
        for (int i = 0; i < o.length; i++) {
            o[i].delete();
        }
    }

    /** Second Helper method for rmbranch using COM.*/
    private static void helper(GitCommands com) {
        Set<String> s = com.getBlobs().keySet();
        for (Iterator ks = s.iterator(); ks.hasNext(); ) {
            String f = (String) ks.next();
            Utils.writeContents(new File(f),
                    com.getBlobs().get(f).contents());
        }
        Set<String> b = git.currBl.keySet();
        for (Iterator ks = b.iterator(); ks.hasNext(); ) {
            String f = (String) ks.next();
            if (!com.getFiles().contains(f)) {
                new File(f).delete();
                git.tbr.add(f);
            }
        }
    }

    /**  Merges files from the given BN into the current branch.
     * @throws Exception for exceptions.*/
    protected static void merge(String bN)
            throws Exception {
        if (bN.equals(GitCommands.git.currBr)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (!GitCommands.git.tbr.isEmpty()
                || !GitCommands.git.currStage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!GitCommands.git.curBra.containsKey(bN)) {
            System.out.println(
                    "A branch with that name does not exist.");
            System.exit(0);
        }
        GitCommands m = deserialize(
                new File(COMMPATH + (git.curBra.get(bN))));
        hel(m);
        GitCommands spl;
        if (bN.equals("master")) {
            spl = deserialize(new File(COMMPATH + git.currPath));
        } else {
            spl = deserialize(new File(COMMPATH
                    + git.currSpl.get(bN)));
        }
        if (git.currPath.equals(spl.hash())) {
            git.currBl.clear();
            git.currBl = m.getBlobs();
            git.currBr = bN;
            git.currPath = m.hash();
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        if (spl.hash().equals(m.hash())) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
            System.exit(0);
        }
        GitCommands curr = deserialize(
                new File(COMMPATH + (git.curBra.get(git.currBr))));
        Merge.pMerge(spl, curr, m, bN);
    }

    /**  Merge helper using M.*/
    private static void hel(GitCommands m) {
        List<String> t = Utils.plainFilenamesIn(MYPTAH);
        for (int i = 0; i < t.size(); i++) {
            if (!git.currStage.containsKey(t.get(i))
                    && !git.currBl.containsKey(t.get(i))
                    && m.getBlobs().containsKey(t.get(i))) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }
    }

    /** Parent commit's sha1.*/
    private String parrent;
    /*** Parent2 commit's sha1.*/
    private String bran2;
    /** Branch of this commit.*/
    private String bran;
    /** Commit time.*/
    private String time;
    /***  Commit message.*/
    private String messa;
    /**A HashMap between files' nam, <name, SHA>.*/
    private HashMap<String, Blob> blob;
    /**The SHA of this commit.*/
    private String hash;
    /** My gitlet.*/
    protected static GL git;
    /** Root user directory.*/
    protected static final String MYPTAH =
            System.getProperty("user.dir") + File.separator;
    /** Gitlet path.*/
    protected static final String GITPATH = MYPTAH + ".gitlet" + File.separator;
    /** Commits path.*/
    protected static final String COMMPATH =
            GITPATH + "commits" + File.separator;
    /** Stage path.*/
    protected static final String STGPATH =
            GITPATH + "stage" + File.separator;
}
