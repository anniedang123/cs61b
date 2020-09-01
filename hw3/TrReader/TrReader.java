import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author your name here
 */
public class TrReader extends Reader {
    /**
     * A new TrReader that produces the stream of characters produced
     * by STR, converting all characters that occur in FROM to the
     * corresponding characters in TO.  That is, change occurrences of
     * FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     * in STR unchanged.  FROM and TO must have the same length.
     */
    private Reader _source;
    private String _from;
    private String _to;

    public TrReader(Reader str, String from, String to) {
        _source = str;
        _from = from;
        _to = to;
    }

    /*
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
        public void close() throws IOException {
            _source.close();
        }

        private char transChar(char word) {
            int i = _from.indexOf(word);
            if (i == -1) {
                return word;
            } else {
                return _to.charAt(i);
        }

    }

@Override
public int read(char[] cArray, int one, int len) throws IOException {
        int aRead = _source.read(cArray, one, len);
        for (int i = one; i < one+aRead; i++){
            cArray[i] = transChar(cArray[i]);
        }
        return aRead;
    }
}



