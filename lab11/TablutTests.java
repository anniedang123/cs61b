package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;
import java.util.List;

/** Junit tests for our Tablut Board class.
 *  @author Vivant Sakore
 */
public class TablutTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(TablutTests.class);
    }

    /**
     * Tests legalMoves for white pieces to make sure it returns all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalWhiteMoves() {
        // FIXME: REPLACE THIS LINE - Build a board by initializing it with initialBoardState

        List<Move> movesList = b.legalMoves(null); // FIXME: Get legal moves for white pieces

        assertEquals(56, movesList.size());

        // Check for absence of illegal moves
        assertFalse(movesList.contains(Move.mv("e7-8")));
        assertFalse(movesList.contains(Move.mv("e8-f")));

        // Check for presence of legal moves
        assertTrue(movesList.contains(Move.mv("e6-f")));
        assertTrue(movesList.contains(Move.mv("f5-8")));

        // FIXME: Add more assertions
    }

    /**
     * Tests legalMoves for black pieces to make sure it returns all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalBlackMoves() {
        // FIXME: REPLACE THIS LINE - Build a board by initializing it with initialBoardState

        List<Move> movesList = b.legalMoves(null); // FIXME: Get legal moves for black pieces

        assertEquals(80, movesList.size());

        // Check for absence of illegal moves
        assertFalse(movesList.contains(Move.mv("e8-7")));
        assertFalse(movesList.contains(Move.mv("e7-8")));

        // Check for presence of legal moves
        assertTrue(movesList.contains(Move.mv("f9-i")));
        assertTrue(movesList.contains(Move.mv("h5-1")));

        // FIXME: Add more assertions
    }

    // FIXME: Add more test cases for other Board.java methods


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - 1 - row][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;
    static final Piece W = Piece.WHITE;
    static final Piece B = Piece.BLACK;
    static final Piece K = Piece.KING;

    static final Piece[][] initialBoardState = {
            {E, E, E, B, B, B, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, E, W, E, E, E, E},
            {B, E, E, E, W, E, E, E, B},
            {B, B, W, W, K, W, W, B, B},
            {B, E, E, E, W, E, E, E, B},
            {E, E, E, E, W, E, E, E, E},
            {E, E, E, E, B, E, E, E, E},
            {E, E, E, B, B, B, E, E, E},
    };
}