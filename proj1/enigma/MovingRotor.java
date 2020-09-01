package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Annie Dang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        String[] split = _notches.split("");
        boolean atN = false;
        int [] spArr = new int [split.length];
        int i = 0;
        for (String x : split) {
            spArr[i] = _permutation.alphabet().toInt(x.charAt(0));
            i++;
        }
        for (int k = 0; k < spArr.length; k += 1) {
            int p = _permutation.wrap(spArr[k] + 1);
            if (super.setting() == p) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean tr() {
        boolean tr = false;
        String[] split = _notches.split("");
        int [] y = new int [split.length];
        int u = 0;
        for (String g : split) {
            y[u] = _permutation.alphabet().toInt(g.charAt(0));
            u++;
        }
        for (int p : y) {
            tr = (setting() == _permutation.wrap(p)) ? true : false;
        }
        return tr;
    }
    @Override
    void advance() {
        super.set(_permutation.wrap(super.setting() + 1));
    }

    /** notch of rotor. **/
    private String _notches;

    /** permutation. **/
    private Permutation _permutation;

}
