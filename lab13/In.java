/*************************************************************************
 *  Compilation:  javac In.java
 *  Execution:    java In   (basic test --- see source for required files)
 *  Dependencies: none
 *
 *  Reads in data of various types from standard input, files, and URLs.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.Socket;
/* import java.net.HttpURLConnection; */
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  <i>Input</i>. This class provides methods for reading strings
 *  and numbers from standard input, file input, URLs, and sockets.
 *  <p>
 *  The Locale used is: language = English, country = US. This is consistent
 *  with the formatting conventions with Java floating-point literals,
 *  command-line arguments (via {@link Double#parseDouble(String)})
 *  and standard output.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *  Like {@link Scanner}, reading a token also consumes preceding Java
 *  whitespace, reading a full line consumes
 *  the following end-of-line delimeter, while reading a character consumes
 *  nothing extra.
 *  <p>
 *  Whitespace is defined in {@link Character#isWhitespace(char)}. Newlines
 *  consist of \n, \r, \r\n, and Unicode hex code points
 *  0x2028, 0x2029, 0x0085;
 *  see <a href="http://www.docjar.com/html/api/java/util/Scanner.java.html">
 *  Scanner.java</a> (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *
 *  @author David Pritchard
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author P. N. Hilfinger (style changes; remove testing).
 */
public final class In {

    /* begin: section (1 of 2) of code duplicated from In to StdIn. */

    /** Assume Unicode UTF-8 encoding. */
    private static final String CHARSET_NAME = "UTF-8";

    /** Assume language = English, country = US for consistency with
     *  System.out. */
    private static final Locale LOCALE = Locale.US;

    /** The default token separator. We maintain the invariant that this value
     * is held by the scanner's delimiter between calls. */
    private static final Pattern WHITESPACE_PATTERN
        = Pattern.compile("\\p{javaWhitespace}+");

    /** Make whitespace characters significant. */
    private static final Pattern EMPTY_PATTERN
        = Pattern.compile("");

    /** Used to read the entire input. source:
     *  http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
     */
    private static final Pattern EVERYTHING_PATTERN
        = Pattern.compile("\\A");

    /* end: section (1 of 2) of code duplicated from In to StdIn. */

    /** Input source. */
    private Scanner scanner;

   /**
     * Initializes an input stream from standard input.
     */
    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

   /** An input stream from SOCKET. */
    public In(Socket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("socket argument is null");
        }
        try {
            InputStream is = socket.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + socket,
                                               ioe);
        }
    }

   /** An input stream URL. */
    public In(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url argument is null");
        }
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is),
                                             CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + url, ioe);
        }
    }

   /** An input stream from FILE. */
    public In(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file argument is null");
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            scanner = new Scanner(new BufferedInputStream(fis), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + file, ioe);
        }
    }


   /** An input stream from NAME, a filename or web page name. */
    public In(String name) {
        /* To set User-Agent, replace definition of site with
         *  HttpURLConnection site = (HttpURLConnection) url.openConnection();
         *  site.addRequestProperty("User-Agent", "Mozilla/4.76");
         */

        if (name == null) {
            throw new IllegalArgumentException("argument is null");
        }
        try {
            File file = new File(name);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis),
                                      CHARSET_NAME);
                scanner.useLocale(LOCALE);
                return;
            }

            URL url = getClass().getResource(name);

            if (url == null) {
                url = new URL(name);
            }

            URLConnection site = url.openConnection();

            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is),
                                             CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Could not open " + name, ioe);
        }
    }

    /**
     * An input stream from SCANNER0; use with
     * {@code new Scanner(String)} to read from a string.
     *
     * This does not create a defensive copy, so the
     * scanner will be mutated as you read on.
     */
    public In(Scanner scanner0) {
        if (scanner0 == null) {
            throw new IllegalArgumentException("scanner argument is null");
        }
        this.scanner = scanner0;
    }

    /**
     * Returns true iff this input stream exists.
     */
    public boolean exists()  {
        return scanner != null;
    }

    /* Begin: section (2 of 2) of code duplicated from In to StdIn,
     *  with all methods changed from "public" to "public static". */

   /**
     * Returns true iff input stream is empty (except possibly whitespace).
     * Use this to know whether the next call to {@link #readString()},
     * {@link #readDouble()}, etc will succeed.
     */
    public boolean isEmpty() {
        return !scanner.hasNext();
    }

   /**
     * Returns true iff this input stream has a next line.
     * Use this method to know whether the
     * next call to {@link #readLine()} will succeed.
     * This method is functionally equivalent to {@link #hasNextChar()}.
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Returns true iff this input stream has more inputy (including
     * whitespace).  Use this method to know whether the next call
     * to {@link #readChar()} will succeed. This method is functionally
     * equivalent to {@link #hasNextLine()}.
     */
    public boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


   /** Reads and returns the next line in this input stream. */
    public String readLine() {
        String line;
        try {
            line = scanner.nextLine();
        } catch (NoSuchElementException e) {
            line = null;
        }
        return line;
    }

    /** Reads and returns the next character in this input stream. */
    public char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String ch = scanner.next();
        assert ch.length() == 1 : "Internal (Std)In.readChar() error!"
            + " Please contact the authors.";
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return ch.charAt(0);
    }


    /** Reads and returns the remainder of this input stream. */
    public String readAll() {
        if (!scanner.hasNextLine()) {
            return "";
        }
        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


   /** Reads and returns the next token from this input stream. */
    public String readString() {
        return scanner.next();
    }

    /** Reads and returns the next token from this input stream,
     *  parsed as an int. */
    public int readInt() {
        return scanner.nextInt();
    }

   /**
     * Reads and returns the next token from this input stream, parsed
     * as a double.
     */
    public double readDouble() {
        return scanner.nextDouble();
    }

   /**
     * Read and return the next token from this input stream, parsed
     * as a float.
     */
    public float readFloat() {
        return scanner.nextFloat();
    }

   /**
     * Reads and returns the next token from this input stream, parsed
     * as a long.
     */
    public long readLong() {
        return scanner.nextLong();
    }

   /**
     * Reads and returns the next token from this input stream, parsed
     * as a short.
     */
    public short readShort() {
        return scanner.nextShort();
    }

   /**
     * Reads abd returns the next token from this input stream, parsed
     * as a byte. To read binary data, use {@link BinaryIn}.
     */
    public byte readByte() {
        return scanner.nextByte();
    }

    /**
     * Reads and returns the next token from this input stream, parsed
     * as a boolean.
     *
     * Interprets either "true" or "1" as true,
     * and either "false" or "0" as false.
     */
    public boolean readBoolean() {
        String s = readString().toLowerCase();
        switch (readString().toLowerCase()) {
        case "true": case "1":
            return true;
        case "false": case "0":
            return false;
        default:
            throw new InputMismatchException();
        }
    }

    /**
     * Returns and returns an array of all remaining tokens from this
     * input stream.
     */
    public String[] readAllStrings() {
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0) {
            return tokens;
        }
        String[] decapitokens = new String[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i++) {
            decapitokens[i] = tokens[i + 1];
        }
        return decapitokens;
    }

    /**
     * Reads and returns an array of all remaining lines from this input
     * stream.
     */
    public String[] readAllLines() {
        ArrayList<String> lines = new ArrayList<String>();
        while (hasNextLine()) {
            lines.add(readLine());
        }
        return lines.toArray(new String[lines.size()]);
    }


    /**
     * Reads and returns an array of all remmaining tokens from this
     * input stream, parsed as ints.
     */
    public int[] readAllInts() {
        String[] fields = readAllStrings();
        int[] vals = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Integer.parseInt(fields[i]);
        }
        return vals;
    }

    /**
     * Reads and returns an array of all remmaining tokens from this
     * input stream, parsed as longs.
     */
    public long[] readAllLongs() {
        String[] fields = readAllStrings();
        long[] vals = new long[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Long.parseLong(fields[i]);
        }
        return vals;
    }

    /**
     * Reads and returns an array of all remmaining tokens from this
     * input stream, parsed as doubles.
     */
    public double[] readAllDoubles() {
        String[] fields = readAllStrings();
        double[] vals = new double[fields.length];
        for (int i = 0; i < fields.length; i++) {
            vals[i] = Double.parseDouble(fields[i]);
        }
        return vals;
    }

    /* end: section (2 of 2) of code duplicated from In to StdIn */

   /**
     * Closes this input stream.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Reads and returns all tokens from FILENAME parsed as an array of ints.
     * @deprecated Replaced by {@code new In(filename)}.{@link #readAllInts()}.
     */
    @Deprecated
    public static int[] readInts(String filename) {
        return new In(filename).readAllInts();
    }

   /**
     * Reads and returns all tokens from FILENAME parsed
     * as an array of doubles.
     * @deprecated Replaced by
     *  {@code new In(filename)}.{@link #readAllDoubles()}.
     */
    @Deprecated
    public static double[] readDoubles(String filename) {
        return new In(filename).readAllDoubles();
    }

   /**
     * Reads and returns all tokens from FILENAME as an array.
     * @deprecated Replaced by
     *  {@code new In(filename)}.{@link #readAllStrings()}.
     */
    @Deprecated
    public static String[] readStrings(String filename) {
        return new In(filename).readAllStrings();
    }

    /**
     * Reads and returns all tokens from the standard input as an array
     * of ints.
     * @deprecated Replaced by
     *  {@code new In()}.{@link #readAllInts()}.
     */
    @Deprecated
    public static int[] readInts() {
        return new In().readAllInts();
    }

    /**
     * Reads and returns all tokens from the standard input as an array
     * of doubles.
     * @deprecated Replaced by
     *  {@code new In()}.{@link #readAllDoubles()}.
     */
    @Deprecated
    public static double[] readDoubles() {
        return new In().readAllDoubles();
    }

   /**
     * Reads and returns all strings from standard input as an array.
     * @deprecated Replaced by {@link StdIn#readAllStrings()}.
     */
    @Deprecated
    public static String[] readStrings() {
        return new In().readAllStrings();
    }

}
