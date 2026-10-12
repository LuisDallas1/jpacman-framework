package nl.tudelft.jpacman.board;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Traverses a board row by row without exposing its internal matrix.
 */
final class BoardIterator implements Iterator<Square> {

    private final Board board;
    private int x;
    private int y;

    /**
     * Creates an iterator positioned at the first square.
     *
     * @param board The board to traverse.
     */
    BoardIterator(Board board) {
        this.board = board;
    }

    @Override
    public boolean hasNext() {
        return board.getWidth() > 0 && y < board.getHeight();
    }

    @Override
    public Square next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Square square = board.squareAt(x, y);
        x++;
        if (x == board.getWidth()) {
            x = 0;
            y++;
        }
        return square;
    }
}
