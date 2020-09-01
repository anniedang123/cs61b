package enigma;

import org.junit.Test;

import static enigma.TestUtils.*;
import static org.junit.Assert.assertEquals;


public class RotorTest {

    @Test
    public void testConvertForward() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alphabet);
        Rotor r = new Rotor("I", p);
        r.set(5);
        assertEquals(r.convertForward(5), 8);

        Permutation d = new Permutation(
                "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alphabet);
        Rotor e = new Rotor("III", d);
        e.set(23);
        assertEquals(e.convertForward(21), 9);
    }

    @Test
    public void testConvertBackward() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation p = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alphabet);
        Rotor r = new Rotor("I", p);
        r.set(5);
        assertEquals(r.convertBackward(9), 7);

    }

    @Test
    public void testRotors() {
        Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Permutation i = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alphabet);
        Permutation iv = new Permutation(
                "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", alphabet);
        Permutation iii = new Permutation(
                "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alphabet);
        Permutation beta = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", alphabet);
        Permutation b = new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) "
                        + "(IJ) (LO) (MP) (RX) (SZ) (TV)",
                alphabet);
        MovingRotor one = new MovingRotor("i", i, "Q");
        MovingRotor four = new MovingRotor("iv", iv, "J");
        MovingRotor three = new MovingRotor("iii", iii, "V");
        FixedRotor bet = new FixedRotor("beta", beta);
        Reflector bee = new Reflector("b", b);
        assertEquals(bet.rotates(), false);
        assertEquals(three.rotates(), true);
        assertEquals(bee.rotates(), false);
        assertEquals(bee.reflecting(), true);

    }



}
