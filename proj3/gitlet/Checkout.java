package gitlet;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Helper class for Checkout command.
 * @author annie dang
 */

public class Checkout {

    /** Checkout helper method using N.*/
    protected static void checkout1(String n) {
        if (!GitCommands.git.currBl.containsKey(n)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            GitCommands.git.currStage.remove(n);
            new File(GitCommands.STGPATH + n).delete();
        }
        File cF = new File(
                GitCommands.COMMPATH + GitCommands.git.currPath);
        File name = new File(GitCommands.MYPTAH + n);
        Utils.writeContents(name,
                GitCommands.deserialize(cF).getBlob(n).contents());

    }

    /** The commit checkout helper method. N and ID used.*/
    protected static void checkout2(String id, String n) {
        boolean fID = c3help(id);
        String id1 = c3help2(id);
        if (!fID
                && id1.length() == Utils.UID_LENGTH
                && !GitCommands.git.currCom.containsKey(id1)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        GitCommands comm = GitCommands.deserialize(
                new File(GitCommands.COMMPATH + id1));
        if (comm != null) {
            if (!comm.getBlobs().containsKey(n)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            } else if (GitCommands.git.currStage.containsKey(n)) {
                GitCommands.git.currStage.remove(n);
                File del = new File(GitCommands.STGPATH + n);
                del.delete();
            }
            Utils.writeContents(new File(GitCommands.MYPTAH
                    + n), comm.getBlobs().get(n).contents());
        }
    }

    /** Helper method for checkout2 using ID to return FID.*/
    private static boolean c3help(String id) {
        boolean fid = false;
        if (Utils.UID_LENGTH > id.length()) {
            Collection<GitCommands> gc = GitCommands.git.currCom.values();
            Iterator<GitCommands> itr = gc.iterator();
            while (itr.hasNext()) {
                GitCommands nxt = itr.next();
                String cID = nxt.hash().substring(0, id.length());
                if (cID.equals(id)) {
                    id = nxt.hash();
                    fid = true;
                }
            }
        }
        return fid;
    }

    /** Helper method for checkout2 using ID to return new ID.*/
    private static String c3help2(String id) {
        if (Utils.UID_LENGTH > id.length()) {
            Collection<GitCommands> gc = GitCommands.git.currCom.values();
            Iterator<GitCommands> itr = gc.iterator();
            while (itr.hasNext()) {
                GitCommands nxt = itr.next();
                String cID = nxt.hash().substring(0, id.length());
                if (cID.equals(id)) {
                    id = nxt.hash();
                }
            }
        }
        return id;
    }

    /** Checkout branch helper using BR. */
    protected static void checkout3(String br) {
        if (br.equals(GitCommands.git.currBr)) {
            System.out.println("No need to checkout "
                    + "the current branch.");
            System.exit(0);
        } else if (GitCommands.git.curBra.get(br) == null) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        GitCommands comm = GitCommands.deserialize(
                new File(GitCommands.COMMPATH
                        + GitCommands.git.curBra.get(br)));
        List<String> des = Utils.plainFilenamesIn(GitCommands.MYPTAH);
        for (int i = 0; i < des.size(); i++) {
            if (!GitCommands.git.currStage.containsKey(des.get(i))
                    && !GitCommands.git.currBl.containsKey(des.get(i))
                    && comm.getBlobs().containsKey(des.get(i))) {
                System.out.println(
                        "There is an untracked file in the way;"
                                + " delete it or add it first.");
                System.exit(0);
            }
        }
        HashMap<String, Blob> ovrwte = helpter(comm);
        GitCommands.git.currPath = comm.hash();
        GitCommands.git.currBr = br;
        GitCommands.git.currBl.clear();
        helpter2(ovrwte, comm);
        GitCommands.git.currStage.clear();
    }

    /** Helper method for checkout3 using OW and COMMIT.*/
    private static void helpter2(HashMap<String,
            Blob> ow, GitCommands commit) {
        File f = new File(GitCommands.STGPATH);
        File[] filesL = f.listFiles();
        for (int i = 0; i < filesL.length; i++) {
            filesL[i].delete();
        }
        Set<String> ovrwte = ow.keySet();
        for (Iterator ks = ovrwte.iterator(); ks.hasNext(); ) {
            String nxt = (String) ks.next();
            GitCommands.git.currBl.put(nxt, commit.getBlobs().get(nxt));
        }
    }

    /** Helper method for c3 using COMM and return hashmap.*/
    private static HashMap<String, Blob> helpter(GitCommands comm) {
        HashMap<String, Blob> ovrwrte = new HashMap<>();
        Set<String> fi = GitCommands.git.currBl.keySet();
        for (Iterator ks = fi.iterator(); ks.hasNext(); ) {
            String nxt = (String) ks.next();
            if (!ovrwrte.containsKey(nxt)) {
                new File(nxt).delete();
            }
        }
        if (comm.getBlobs() != null) {
            Set<String> s = comm.getBlobs().keySet();
            for (Iterator ks = s.iterator(); ks.hasNext(); ) {
                String d = (String) ks.next();
                Utils.writeContents(new File(d),
                        comm.getBlobs().get(d).contents());
                ovrwrte.put(d, comm.getBlobs().get(d));
            }
        }

        return ovrwrte;
    }

}
