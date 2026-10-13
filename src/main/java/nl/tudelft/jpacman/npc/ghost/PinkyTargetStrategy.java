package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;

import java.util.Optional;

/**
 * Concrete {@link GhostTargetStrategy} for Pinky (Speedy).
 *
 * <p>
 * Pinky's strategy is to target the square that is {@value #SQUARES_AHEAD}
 * grid spaces ahead of Pac-Man in the direction Pac-Man is currently facing,
 * attempting to intercept rather than directly chase.
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
public class PinkyTargetStrategy implements GhostTargetStrategy {

    /**
     * Number of squares ahead of the player that Pinky aims for.
     */
    private static final int SQUARES_AHEAD = 4;

    /**
     * Selects the square {@value #SQUARES_AHEAD} cells in front of the nearest
     * {@link Player} as Pinky's target.
     *
     * <p>
     * Pinky is affected by a targeting quirk when Pac-Man faces up: the
     * destination shifts diagonally up-left, mirroring the original arcade bug.
     * </p>
     *
     * @param ghost the ghost unit invoking this strategy (never {@code null})
     * @return an {@link Optional} containing the intercept square, or empty if
     *         no player is found on the board
     */
    @Override
    public Optional<Square> selectTarget(Unit ghost) {
        Unit player = Navigation.findNearest(Player.class, ghost.getSquare());
        if (player == null) {
            return Optional.empty();
        }
        assert player.hasSquare();
        return Optional.of(player.squaresAheadOf(SQUARES_AHEAD));
    }
}
