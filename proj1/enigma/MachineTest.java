package enigma;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


public class MachineTest {
    Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    Permutation i = new Permutation(
            "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", alphabet);
    Permutation iv = new Permutation(
            "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", alphabet);
    Permutation iii = new Permutation(
            "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alphabet);
    Permutation beta = new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", alphabet);
    Permutation b = new Permutation(
            "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
            alphabet);
    MovingRotor one = new MovingRotor("i", i, "ZM");
    MovingRotor four = new MovingRotor("iv", iv, "E");
    MovingRotor three = new MovingRotor("iii", iii, "V");
    FixedRotor bet = new FixedRotor("beta", beta);
    Reflector bee = new Reflector("b", b);

    Collection<Rotor> allRotors = new ArrayList<Rotor>();

    @Test
    public void testConvertInt() {
        allRotors.add(bee);
        allRotors.add(bet);
        allRotors.add(three);
        allRotors.add(four);
        allRotors.add(one);
        Machine m = new Machine(alphabet, 5, 3, allRotors);
        String[] rotors = new String[] {"b", "beta", "iii", "iv", "i"};
        m.insertRotors(rotors);
        m.setRotors("AXLE");
        Permutation plug = new Permutation("(YF) (ZH)", alphabet);
        m.setPlugboard(plug);
        assertEquals(m.convert(24), 10);
    }

    @Test
    public void testConvertString() {
        allRotors.add(bee);
        allRotors.add(bet);
        allRotors.add(three);
        allRotors.add(four);
        allRotors.add(one);
        Machine m = new Machine(alphabet, 5, 3, allRotors);
        String[] rotors = new String[] {"b", "beta", "iii", "iv", "i"};
        m.insertRotors(rotors);
        m.setRotors("AAAZ");
        Permutation plug = new Permutation(
                "", alphabet);
        m.setPlugboard(plug);
        assertEquals(m.convert("AAAA"), "DNLE");


    }
}
