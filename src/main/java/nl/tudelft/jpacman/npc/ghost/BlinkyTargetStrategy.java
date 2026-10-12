package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;

import java.util.Optional;

/**
 * Concrete {@link GhostTargetStrategy} for Blinky (Shadow).
 *
 * <p>
 * Blinky's strategy is to directly chase Pac-Man: the target square is always
 * Pac-Man's current position.
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
public class BlinkyTargetStrategy implements GhostTargetStrategy {

    /**
     * Selects the square currently occupied by the nearest {@link Player} as
     * Blinky's target.
     *
     * @param ghost the ghost unit invoking this strategy (never {@code null})
     * @return an {@link Optional} containing the player's square, or empty if
     *         no player is found on the board
     */
    @Override
    public Optional<Square> selectTarget(Unit ghost) {
        // TODO Blinky should patrol his corner every once in a while.
        // TODO Blinky should speed up when there are only a few pellets left.
        Unit nearest = Navigation.findNearest(Player.class, ghost.getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        return Optional.of(nearest.getSquare());
    }
}
