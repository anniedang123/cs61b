package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;


import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Annie Dang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        String nxt = _input.nextLine();
        if (!nxt.contains("*")) {
            throw new EnigmaException("Missing confi");
        }

        while (_input.hasNext()) {
            String set = nxt;
            if (!set.contains("*")) {
                throw new EnigmaException("Wrong setting");
            }
            setUp(machine, set);
            nxt = (_input.nextLine()).toUpperCase();
            while (nxt.equals("")) {
                _output.println(nxt);
                nxt = (_input.nextLine()).toUpperCase();
            }
            while (!(nxt.contains("*"))) {
                String rpl = nxt.replaceAll(" ", "");
                if (nxt.equals("")) {
                    _output.println();
                } else {
                    printMessageLine(machine.convert(rpl));
                }
                if (_input.hasNext()) {
                    nxt = (_input.nextLine()).toUpperCase();
                } else {
                    nxt = "*";
                }
            }
        }
        while (_input.hasNextLine() && _input.nextLine().equals("")) {
            _output.println();
        }
    }



    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String next = _config.next();
            _alphabet = new Alphabet(next);
            if (next.contains("(") || next.contains(")")
                    || next.contains("*")) {
                throw new EnigmaException("Incorrect config");
            }
            if (!_config.hasNextInt()) {
                throw new EnigmaException("incorrect configuration");
            }
            int rotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("incorrect configuration");
            }
            int numPawls = _config.nextInt();
            if (!_config.hasNext()) {
                throw new EnigmaException("incorrect configuration");
            }
            temp = _config.next();
            while (_config.hasNext()) {
                nameR = temp;
                notches = _config.next();
                _rotorsArr.add(readRotor());
            }
            return new Machine(_alphabet, rotors, numPawls, _rotorsArr);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            temp = _config.next();
            String permutation = "";
            while (_config.hasNext() && temp.contains(")")) {
                permutation = permutation + temp + " ";
                temp = _config.next();
            }
            if (!_config.hasNext()) {
                permutation = permutation + temp + " ";
            }
            if (notches.charAt(0) == 'N') {
                Permutation p = new Permutation(permutation, _alphabet);
                return new FixedRotor(nameR, p);
            } else if (notches.charAt(0) == 'M') {
                Permutation p1 = new Permutation(permutation, _alphabet);
                return new MovingRotor(nameR, p1, notches.substring(1));
            } else {
                Permutation p2 = new Permutation(permutation, _alphabet);
                return new Reflector(nameR, p2);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] set = settings.split(" ");
        if (set.length - 1 <= M.numRotors()) {
            throw new EnigmaException("Missing arguments");
        }
        if (set[M.numRotors() + 1].length() != M.numRotors() - 1) {
            throw new EnigmaException("Setting is wrong length");
        }
        if (set[2].equals("I")) {
            throw new EnigmaException("Wrong arguments");
        }
        int nRot = M.numRotors();
        String[] rotors = new String[nRot];
        int y = 0;
        for (String x : rotors) {
            rotors[y] = set[y + 1];
            y++;
        }
        M.insertRotors(rotors);

        if (!M.getArr()[0].reflecting()) {
            throw new EnigmaException("First Rotor should be a reflector");
        }
        String emp = "";
        for (int i = 7; i < set.length; i++) {
            emp = emp + set[i] + " ";
        }
        M.setRotors(set[M.numRotors() + 1]);
        M.setPlugboard(new Permutation(emp, _alphabet));

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int q = 0; q < msg.length(); q += 1) {
            if (q % 6 == 0) {
                int l = msg.length();
                msg = msg.substring(0, q) + " " + msg.substring(q, l);
            }
        }
        String n = msg.trim();
        _output.println(n);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** ArrayList of rotors. */
    private ArrayList<Rotor> _rotorsArr = new ArrayList<>();

    /** name of rotor. */
    private String nameR;

    /** Notch of rotor. */
    private String notches;

    /** Temporary string. */
    private String temp;
}
