package nl.tudelft.jpacman.board;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Test various aspects of board.
 *
 * @author Jeroen Roosen 
 */
class BoardTest {

    private static final int MAX_WIDTH = 2;
    private static final int MAX_HEIGHT = 3;

    private final Square[][] grid = {
        { mock(Square.class), mock(Square.class), mock(Square.class) },
        { mock(Square.class), mock(Square.class), mock(Square.class) },
    };
    private final Board board = new Board(grid);

    /**
     * Visits every square once in row order on a rectangular board.
     */
    @Test
    void iteratesInRowOrder() {
        assertThat(board).containsExactly(grid[0][0], grid[1][0],
            grid[0][1], grid[1][1], grid[0][2], grid[1][2]);
    }

    /**
     * Checks exhaustion and the iterator's next contract.
     */
    @Test
    void rejectsNextAfterExhaustion() {
        Iterator<Square> iterator = board.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertThat(iterator.hasNext()).isFalse();
        assertThatThrownBy(iterator::next)
            .isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Checks that separate iterators maintain independent positions.
     */
    @Test
    void iteratorsAreIndependent() {
        Iterator<Square> first = board.iterator();
        Iterator<Square> second = board.iterator();
        assertThat(first.next()).isSameAs(grid[0][0]);
        assertThat(first.next()).isSameAs(grid[1][0]);
        assertThat(second.next()).isSameAs(grid[0][0]);
    }

    /**
     * Verifies the board has the correct width.
     */
    @Test
    void verifyWidth() {
        assertThat(board.getWidth()).isEqualTo(MAX_WIDTH);
    }

    /**
     * Verifies the board has the correct height.
     */
    @Test
    void verifyHeight() {
        assertThat(board.getHeight()).isEqualTo(MAX_HEIGHT);
    }

    /**
     * Verify that squares at key positions are properly set.
     * @param x Horizontal coordinate of relevant cell.
     * @param y Vertical coordinate of relevant cell.
     */
    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 2",
        "0, 1"
    })
    void testSquareAt(int x, int y) {
        assertThat(board.squareAt(x, y)).isEqualTo(grid[x][y]);
    }
}
