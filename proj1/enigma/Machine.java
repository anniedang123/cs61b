package enigma;



import java.util.Collection;



/** Class that represents a complete enigma machine.
 *  @author Annie Dang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        rotArr = new Rotor[numRotors];
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int j = 0; j < rotors.length; j++) {
            for (int i = 0; i < _allRotors.toArray().length; i++) {
                String c = rotors[j].toString();
                if (c.equals((((Rotor) _allRotors.toArray()[i]).name()))) {
                    rotArr[j] = (Rotor) _allRotors.toArray()[i];
                }
            }
        }
        if (rotArr.length != rotors.length) {
            throw new EnigmaException("rotors are misnamed");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int i = 1;
        for (char x: setting.toCharArray()) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw new EnigmaException("Not found in alphabet");
            }
            rotArr[i].set(setting.charAt(i - 1));
            i += 1;
        }

    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Return array of rotors. */
    Rotor[] getArr() {
        return rotArr;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        int len = rotArr.length - 1;
        rotArr[len].advance();
        boolean[] check = new boolean [rotArr.length - 1];
        check[len - 1] = true;
        for (int i = len; i > 0; i--) {
            if (rotArr[i - 1].tr()
                    || check[i - 1] && rotArr[i - 1].rotates()
                    && rotArr[i].atNotch()) {
                check[i - 2] = true;
                rotArr[i - 1].advance();
            }
        }
        int per = _plugboard.permute(c);
        for (int u = rotArr.length - 1; u >= 0; u--) {
            per = rotArr[u].convertForward(per);
        }
        for (int l = 1; l < rotArr.length; l++) {
            per = rotArr[l].convertBackward(per);
        }
        per = _plugboard.permute(per);
        return per;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        String[] mArr = msg.split("");
        int[] iArr = new int[mArr.length];
        int i = 0;
        for (String x : mArr) {
            iArr[i] = convert(_alphabet.toInt(x.charAt(0)));
            i++;
        }
        int j = 0;
        String[] msgCon = new String[iArr.length];
        for (int y : iArr) {
            msgCon[j] = Character.toString(_alphabet.toChar(iArr[j]));
            j++;
        }
        String converted = "";
        for (String c : msgCon) {
            converted += c;
        }
        return converted;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors. */
    private int _numRotors;
    /** Number of pawls. */
    private int _pawls;
    /** Rotor collection. */
    private Collection<Rotor> _allRotors;
    /** Plugboard of permutation. */
    private Permutation _plugboard;
    /** Rotors array. */
    private Rotor[] rotArr;


}
