package enigma;

import static enigma.EnigmaException.error;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Annie Dang
 */
class Alphabet {
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alph = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alph.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (char x: _alph.toCharArray()) {
            if (x == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw error("character index not in range");
        }
        return _alph.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("Character not found in alphabet");
        }
        for (char x: _alph.toCharArray()) {
            if (x == ch) {
                return _alph.indexOf(ch);
            }
        }
        throw error("Character not found in alphabet");
    }

    /** Alphabet string. **/
    private String _alph;

}
