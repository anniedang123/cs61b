package tablut;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author Annie Dang
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Throne's four surrounding squares. */
    static final Square[] SURRONDING_THRONE = {
            sq(4, 5), sq(4, 3),
            sq(3, 4), sq(5, 4)
    };

    /** SURRONDING_THRONE as a list. */
    static final HashSet<Square> SURR_LIST =
            new HashSet<>(Arrays.asList(SURRONDING_THRONE));

    /** Throne's four surrounding squares. */
    static final Square[] OUTER_RING = {
            sq(4, 2), sq(4, 6),
            sq(6, 4), sq(2, 4)
    };

    /** SURRONDING_THRONE as a list. */
    static final HashSet<Square> OR = new HashSet<>(Arrays.asList(OUTER_RING));

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /** List of squares that may capture the king. */
    static final Square[] AROUND_THRONE = {
            sq(2, 4), sq(3, 3), sq(3, 4), sq(3, 5),
            sq(4, 2), sq(4, 3), sq(4, 5), sq(4, 6),
            sq(5, 3), sq(5, 4), sq(5, 5), sq(6, 4)
    };

    /** List of EDGES. */
    static final Square[] EDGE_SQUARES = {
            sq(0, 3), sq(0, 6), sq(1, 0),
            sq(0, 1), sq(0, 4), sq(0, 7), sq(2, 0),
            sq(0, 2), sq(0, 5), sq(3, 0),
            sq(4, 0), sq(7, 0), sq(8, 2), sq(8, 5),
            sq(5, 0), sq(8, 3), sq(8, 6),
            sq(6, 0), sq(8, 1), sq(8, 4), sq(8, 7),
            sq(3, 8), sq(6, 8),
            sq(1, 8), sq(4, 8), sq(7, 8),
            sq(2, 8), sq(5, 8)
    };

    /** AROUND_THRONE as a list. */
    static final HashSet<Square> E_LIST =
            new HashSet<>(Arrays.asList(EDGE_SQUARES));


    /** AROUND_THRONE as a list. */
    static final HashSet<Square> THRONE_LIST =
            new HashSet<>(Arrays.asList(AROUND_THRONE));

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        this._turn = model._turn;
        this._winner = model._winner;
        this._moveCount = model._moveCount;
        this._repeated = model._repeated;
        this._repeatB = model._repeatB;
        this._moveLim = model._moveLim;
        this._pieces = model._pieces;
        this._squares = model._squares;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                _board[x][y] = model._board[x][y];
            }
        }

    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = BLACK;
        _winner = null;
        _repeated = false;
        _board = new Piece[9][9];
        _repeatB = new HashMap<>();
        _pieces = new Stack<Piece>();
        _squares = new Stack<Square>();
        put(KING, THRONE);
        _board[4][4] = get(THRONE);
        for (Square sq : INITIAL_ATTACKERS) {
            put(BLACK, sq);
        }
        for (Square sq2 : INITIAL_DEFENDERS) {
            put(WHITE, sq2);
        }
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (_board[x][y] == null) {
                    _board[x][y] = EMPTY;
                }
            }
        }
        if (_repeatB != null && _pieces != null
                && _squares != null) {
            clearUndo();
        }
    }

    /** Set the move limit to N.  It is an error if 2*N <= moveCount(). */
    void setMoveLimit(int n) {
        _moveLim = n;
        if (2 * n <= _moveCount) {
            throw new IllegalArgumentException();
        }

    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {
        if (_moveCount != 0 && _repeatB.containsKey(encodedBoard())
                && _turn.equals(_repeatB.get(encodedBoard()))) {
            _repeated = true;
            _winner = _turn.opponent();
        } else {
            _repeatB.put(encodedBoard(), _turn);
        }

    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        Square king = sq(4, 4);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (_board[x][y] == KING) {
                    return sq(x, y);
                }
            }
        }
        return king;
    }


    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return _board[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _board[s.col()][s.row()] = p;
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        put(p, s);
        _pieces.push(p);
        _squares.push(s);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        if (from.isRookMove(to)) {
            if (to.col() == from.col() && to.row() > from.row()) {
                for (int x = from.row() + 1; x <= to.row(); x++) {
                    if (!get(to.col(), x).equals(EMPTY)) {
                        return false;
                    }
                }
            }
            if (to.col() == from.col() && to.row() < from.row()) {
                for (int x = from.row() - 1; x >= to.row(); x--) {
                    if (!get(to.col(), x).equals(EMPTY)) {
                        return false;
                    }
                }
            }
            if (to.row() == from.row() && to.col() < from.col()) {
                for (int x = from.col() - 1; x >= to.col(); x--) {
                    if (!get(x, to.row()).equals(EMPTY)) {
                        return false;
                    }
                }
            }
            if (to.row() == from.row() && to.col() > from.col()) {
                for (int x = from.col() + 1; x <= to.col(); x++) {
                    if (!get(x, to.row()).equals(EMPTY)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (get(from) == EMPTY || get(from) == null) {
            return false;
        }
        if (!isLegal(from) || from.col() < 0 || from.row() < 0) {
            return false;
        }
        if (to.col() == from.col() && to.row() == from.row()) {
            return false;
        }
        if (to.col() > SIZE - 1 || to.row() > SIZE - 1
                || to.col() < 0 || to.col() < 0) {
            return false;
        }
        if (!isUnblockedMove(from, to)) {
            return false;
        }
        if (to.equals(THRONE) && !get(from).equals(KING)) {
            return false;
        }
        return true;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        revPut(get(from), to);
        revPut(EMPTY, from);
        for (int dir = 0; dir < 4; dir++) {
            Square other = to.rookMove(dir, 2);
            if (other != null && (get(other).side().equals(get(to).side())
                    || (other.equals(THRONE) && get(THRONE).equals(EMPTY)))
                    && get(to.between(other)).side().equals(get(to).opponent())
                    && !to.between(other).equals(sq(4, 4))) {
                if (get(to.between(other)).equals(KING)
                        && !SURR_LIST.contains(to.between(other))) {
                    capture(to, other);
                } else if (!get(to.between(other)).equals(KING)) {
                    capture(to, other);
                }
            }
        }
        if (THRONE_LIST.contains(to) && get(to).equals(BLACK)) {
            Square king = kingPosition();
            if (OR.contains(to) && get(THRONE).equals(KING) && bCount() == 3
                   && get(to.rookMove(to.direction(king), 1)).equals(WHITE)) {
                capture(to, THRONE);
            }
            if (bCount() == 4 && get(THRONE).equals(KING)) {
                capture(to, to.rookMove(to.direction(THRONE), 2));
            }
            if (king.equals(NTHRONE) || king.equals(STHRONE)
                    || king.equals(ETHRONE) || king.equals(WTHRONE)
                    && get(kingPosition()).equals(KING)) {
                Square kin = to.rookMove(to.direction(king), 1);
                Square dirT = to.rookMove(to.direction(king), 2);
                boolean d1 = get(to.diag1(king)).equals(BLACK);
                boolean d2 = get(to.diag2(king)).equals(BLACK);
                boolean d3 = to.diag2(king).equals(THRONE);
                boolean d4 = to.diag1(king).equals(THRONE);
                if (dirT.equals(THRONE) && d1 && d2 && get(kin).equals(KING)) {
                    capture(to, dirT);
                }
                if (get(dirT).equals(BLACK)
                        && (d1 && d3) || (d2 && d4) && get(kin).equals(KING)) {
                    capture(to, dirT);
                }
            }
        }

        if (get(to).equals(KING) && to.isEdge()) {
            _winner = WHITE;
        }
        checkRepeated();
        _turn = get(to).opponent();
        if (!hasMove(_turn)) {
            _winner = _turn.opponent();
        }
        _moveCount++;
    }



    /** Count incidences of black in four surrounding squares. Returns B. */
    int bCount() {
        int b = 0;
        for (Square s : SURRONDING_THRONE) {
            if (get(s).equals(BLACK)) {
                b++;
            }
        }
        return b;
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        if (get(sq0.between(sq2)).equals(KING)) {
            _winner = BLACK;
        }
        Square bet = sq0.between(sq2);
        revPut(get(bet), bet);
        revPut(EMPTY, bet);
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            Piece p = _pieces.pop();
            Square s = _squares.pop();
            Piece p5 = _pieces.pop();
            Square s6 = _squares.pop();
            while (s.equals(s6)) {
                put(p5, s6);
                p = _pieces.pop();
                s = _squares.pop();
                p5 = _pieces.pop();
                s6 = _squares.pop();
            }
            put(p5, s);
            put(p, s6);
            _moveCount--;
            _winner = null;
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        if (_repeated || _moveCount == 0) {
            _repeated = false;
        } else {
            _repeatB.remove(encodedBoard());
        }
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        _repeatB.clear();
        _pieces.empty();
        _squares.empty();
        _moveCount = 0;
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        ArrayList<Square> sqSide = new ArrayList<Square>();
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (_board[x][y].side() == side) {
                    sqSide.add(sq(x, y));
                }
            }
        }
        for (Square sq0 : sqSide) {
            int i0 = sq0.index();
            for (int d = 0; d < 4; d += 1) {
                for (Square sq1 : ROOK_SQUARES[i0][d]) {
                    if (isLegal(sq0, sq1)) {
                        moves.add(mv(sq0, sq1));
                    }
                }
            }
        }

        return moves;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return !legalMoves(side).isEmpty();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    private HashSet<Square> pieceLocations(Piece side) {
        HashSet<Square> si = new HashSet<Square>();
        assert side != EMPTY;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (_board[x][y] == side) {
                    si.add(sq(x, y));
                }
            }
        }
        return si;
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** Return number of (WHITE or BLACK) @ P. */
    int getNumPieces(Piece p) {
        int piece = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (_board[x][y].side().equals(p)) {
                    piece++;
                }
            }
        }
        return piece;
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;


    /** Board as a 2d array. */
    private Piece[][] _board;

    /** Hashmap of previous positions. */
    private HashMap<String, Piece> _repeatB;

    /** move limit. */
    private int _moveLim;

    /** stack of previous pieces for undoing. */
    private Stack<Piece> _pieces;

    /** stack of previous squares for undoing. */
    private Stack<Square> _squares;





}
