package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;

import java.util.List;
import java.util.Optional;

/**
 * Concrete {@link GhostTargetStrategy} for Inky (Bashful).
 *
 * <p>
 * Inky has the most complex targeting algorithm of all ghosts. He considers
 * two reference points:
 * <ol>
 *   <li>The square {@value #SQUARES_AHEAD} spaces in front of Pac-Man (point B).</li>
 *   <li>Blinky's current position (point A).</li>
 * </ol>
 * Inky draws a vector from A to B and doubles it, making the tip of that
 * extended vector his destination.
 * </p>
 *
 * <p>
 * Implementation approximation: determine the shortest path from Blinky (A)
 * to the square two spaces ahead of Pac-Man (B), then walk that same sequence
 * of directions starting from B. The resulting square is Inky's target.
 * </p>
 *
 * <p>
 * This class is a <em>ConcreteStrategy</em> in the Strategy pattern. It
 * encapsulates only the target-selection logic; the path-finding and
 * move-execution skeleton live in {@link nl.tudelft.jpacman.npc.Ghost}.
 * </p>
 *
 * @author Refactoring: Strategy pattern applied to ghost target selection
 * @see GhostTargetStrategy
 */
public class InkyTargetStrategy implements GhostTargetStrategy {

    /**
     * Number of squares ahead of Pac-Man used as the first reference point (B).
     */
    private static final int SQUARES_AHEAD = 2;

    /**
     * Selects Inky's target square using the Blinky-vector algorithm.
     *
     * @param ghost the ghost unit invoking this strategy (never {@code null})
     * @return an {@link Optional} containing the computed target square, or
     *         empty if Blinky or the player cannot be found, or if the
     *         reference path does not exist
     */
    @Override
    public Optional<Square> selectTarget(Unit ghost) {
        Unit blinky = Navigation.findNearest(Blinky.class, ghost.getSquare());
        Unit player = Navigation.findNearest(Player.class, ghost.getSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        assert player.hasSquare();
        Square playerDestination = player.squaresAheadOf(SQUARES_AHEAD);

        List<Direction> firstHalf = Navigation.shortestPath(
            blinky.getSquare(), playerDestination, null);

        if (firstHalf == null) {
            return Optional.empty();
        }

        // Walk the same path from point B to get Inky's destination.
        Square destination = playerDestination;
        for (Direction d : firstHalf) {
            destination = destination.getSquareAt(d);
        }

        return Optional.of(destination);
    }
}
