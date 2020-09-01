package gitlet;
import java.io.Serializable;
import java.io.File;
import static gitlet.Utils.sha1;
import static gitlet.Utils.readContents;

/** Class for Blob; contents of the files.
 * @author annie dang
 */
public class Blob implements Serializable {
    /** Constructor. Build with NAME of blob. */
    public Blob(String name) {
        File f = new File(name);
        _contents = readContents(f);
        _hash = sha1(_contents);
        _name = name;
    }

    /** Returns HASH of blob. */
    String hash() {
        return _hash;
    }

    /** Return CONTENTS of blob. */
    byte[] contents() {
        return _contents;
    }

    /** Blob contents. */
    private byte[] _contents;
    /** Blob hash. */
    private String _hash;
    /** Blob Name. */
    private String _name;

}


