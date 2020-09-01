package enigma;


import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Annie Dang
 */
class Permutation {
    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String x = cycles.trim();
        x = x.replace("(", "");
        x = x.replace(")", "");
        _cycles = x.split(" ");

    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        int l = _cycles.length;
        String[] cyc = new String[l + 1];
        for (int i = 0; i < l; i++) {
            cyc[i] = _cycles[i];
        }
        cyc[l + 1] = cycle;
        _cycles = cyc;

    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char i = _alphabet.toChar(wrap(p));
        char perm = '0';
        for (int arrInd = 0; arrInd < _cycles.length; arrInd++) {
            int arrL = _cycles[arrInd].length();
            for (int cycInd = 0; cycInd < arrL; cycInd++) {
                if (_cycles[arrInd].charAt(cycInd) == i) {
                    if (cycInd == arrL - 1 || arrL == 1) {
                        perm = _cycles[arrInd].charAt(0);
                    } else {
                        perm = _cycles[arrInd].charAt(cycInd + 1);
                    }
                    return _alphabet.toInt(perm);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char i = _alphabet.toChar(wrap(c));
        char perm = '0';
        for (int arrInd = 0; arrInd < _cycles.length; arrInd++) {
            int arrL = _cycles[arrInd].length();
            for (int cycInd = 0; cycInd < arrL; cycInd++) {
                if (_cycles[arrInd].charAt(cycInd) == i) {
                    if (cycInd == 0 || arrL == 1) {
                        perm = _cycles[arrInd].charAt(arrL - 1);
                    } else {
                        perm = _cycles[arrInd].charAt(cycInd - 1);
                    }
                    return _alphabet.toInt(perm);
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int i = _alphabet.toInt(p);
        return _alphabet.toChar(permute(i));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int i = _alphabet.toInt(c);
        return _alphabet.toChar(invert(i));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (int i = 0; i < _cycles.length; i++) {
            count += _cycles[i].length();
        }
        if (count != _alphabet.size()) {
            return false;
        }
        for (String x : _cycles) {
            if (x.length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** An array of strings of cycles. **/
    private String[] _cycles;

}

