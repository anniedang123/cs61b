package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/** Merge helper class.
 *  @author annie dang
 */

public class Merge {
    /** Merge helper using SPL, CURR, ME, BN.
     * @throws Exception for exceptions.*/
    protected static void pMerge(GitCommands spl, GitCommands curr,
                                       GitCommands me, String bN)
            throws Exception {
        int c = 0;
        Set<String> f = me.getFiles();
        for (Iterator ks = f.iterator(); ks.hasNext(); ) {
            String i = (String) ks.next();
            if (!spl.getFiles().contains(i)
                && GitCommands.git.currBl.containsKey(i)) {
                boolean e1 = GitCommands.git.currBl.get(i).hash().equals(
                        me.getBlob(i).hash());
                c = helper(e1, c, i, bN);
            }
            if (!spl.getFiles().contains(i)
                    && GitCommands.git.currBl.get(i) == null) {
                GitCommands.checkout(
                        new String[] {"checkout", me.hash(), "--", i});
                GitCommands.git.currStage.put(i, me.getBlob(i));
            } else if (!GitCommands.git.currBl.containsKey(i)) {
                boolean e2 = me.getBlob(i).hash().equals(
                        spl.getBlob(i).hash());
                c = helper(e2, c, i, bN);
            } else {
                boolean e3 = GitCommands.git.currBl.get(i).hash().equals(
                        me.getBlob(i).hash());
                c = helper(e3, c, i, bN);
            }
        }
        help1(spl, curr, me, bN, c);
    }

    /**  Merge helper using EQUALS, COUNT, S, BN. Returns count.*/
    private static int helper(boolean equals, int count, String s, String bN) {
        if (!equals) {
            conflict(s, bN);
            count++;
        }
        return count;
    }

    /**  Merge helper. SPL, CURR, MER, BR, CO.*/
    protected static void help1(GitCommands spl, GitCommands curr,
                                GitCommands mer, String br, int co) {
        Set<String> f = spl.getFiles();
        for (Iterator ks = f.iterator(); ks.hasNext(); ) {
            String n = (String) ks.next();
            if (!mer.getFiles().contains(n)) {
                if (GitCommands.git.currBl.get(n) != null) {
                    String cch = GitCommands.git.currBl.get(n).hash();
                    if (cch.equals(spl.getBlob(n).hash())) {
                        GitCommands.rm(n);
                    }
                }
            }
            if (curr.getBlobs().keySet().contains(n)
                    && mer.getBlobs().keySet().contains(n)
                    && spl.getBlobs().keySet().contains(n)) {
                String splH = spl.getBlobs().get(n).hash();
                if (!mer.getBlobs().get(n).hash().equals(splH)
                        && curr.getBlobs().get(n).hash().equals(splH)) {
                    Blob blob = mer.getBlobs().get(n);
                    Utils.writeContents(new File(n), blob.contents());
                    GitCommands.git.currBl.put(n, blob);
                    GitCommands.git.currStage.put(n, blob);
                    Utils.writeContents(new File(GitCommands.STGPATH
                            + blob.hash()), blob.contents());
                }
            }
        }
        Set<String> r = GitCommands.git.currBl.keySet();
        for (Iterator ks = r.iterator(); ks.hasNext(); ) {
            String n = (String) ks.next();
            if (!spl.getFiles().contains(n)) {
                if (mer.getFiles().contains(n)) {
                    boolean e1 = GitCommands.git.currBl.get(n).hash().equals(
                            mer.getBlob(n).hash());
                    co = helper(e1, co, n, br);
                }
            } else if (!mer.getFiles().contains(n)) {
                boolean e2 = GitCommands.git.currBl.get(n).hash().equals(
                        spl.getBlob(n).hash());
                co = helper(e2, co, n, br);
            } else {
                boolean e3 = GitCommands.git.currBl.get(n).hash().equals(
                        mer.getBlob(n).hash());
                co = helper(e3, co, n, br);
            }
        }
        if (co > 0) {
            System.out.println("Encountered a merge conflict");
        }
        help2(br);
    }

    /**Merge helper using BN.*/
    protected static void help2(String bN) {
        if (GitCommands.git.currStage.size() != 0) {
            Set<String> s = GitCommands.git.currStage.keySet();
            for (Iterator ks = s.iterator(); ks.hasNext(); ) {
                String fi = (String) ks.next();
                Blob p = GitCommands.git.currStage.get(fi);
                GitCommands.git.currBl.put(fi, p);
            }
        }
        h22(bN);
        File[] sf = new File(GitCommands.STGPATH).listFiles();
        for (int i = 0; i < sf.length; i++) {
            sf[i].delete();
        }
        GitCommands.git.currStage.clear();
        GitCommands.git.tbr = new ArrayList<>();
    }

    /** Merge helper using BN.*/
    private static void h22(String bN) {
        String message = "Merged " + bN
                + " into " + GitCommands.git.currBr + ".";
        GitCommands merCom = new GitCommands(GitCommands.git.currBl,
                GitCommands.git.currPath, message, GitCommands.git.currBr,
                GitCommands.git.curBra.get(bN));
        GitCommands.git.currPath = merCom.hash();
        GitCommands.git.currCom.put(GitCommands.git.currPath, merCom);
        GitCommands.git.curBra.put(GitCommands.git.currBr,
                GitCommands.git.currPath);
        Utils.writeContents(new File(GitCommands.COMMPATH
                + merCom.hash()), GitCommands.serialize(merCom));
    }

    /** Merge conflicts using F, B.*/
    protected static void conflict(String f, String b) {
        HashMap<String, Blob> bmap = GitCommands.deserialize(
                new File(GitCommands.COMMPATH
                        + GitCommands.git.curBra.get(b))).getBlobs();
        if (!bmap.containsKey(f)) {
            Utils.writeContents(new File(GitCommands.MYPTAH
                            + File.separator + f), "<<<<<<< HEAD\n",
                    GitCommands.git.currBl.get(f).contents(), "=======\n",
                    ">>>>>>>\n");
            GitCommands.git.currStage.put(f, new Blob(f));
        } else {
            Utils.writeContents(new File(GitCommands.MYPTAH
                            + File.separator + f),
                    "<<<<<<< HEAD\n",
                    GitCommands.git.currBl.get(f).contents(), "=======\n",
                    bmap.get(f).contents(), ">>>>>>>\n");
            GitCommands.git.currStage.put(f, new Blob(f));
        }

    }

}
