package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete {@link GhostTargetStrategy} for Clyde (Pokey).
 *
 * <p>
 * Clyde has a dual-mode AI: when he is far from Pac-Man (more than
 * {@value #SHYNESS} grid spaces away), he behaves like Blinky and approaches
 * Pac-Man's exact position. Once he gets within {@value #SHYNESS} cells he
 * turns and flees in the opposite direction.
 * </p>
 *
 * <p>
 * This class is a <em>ConcreteStrategy</em> in the Strategy pattern. Unlike
 * the other strategies it needs to resolve the <em>path</em> internally in
 * order to measure distance; the resolved target square is still returned as a
 * {@link Square} and the final navigation is left to the context
 * ({@link nl.tudelft.jpacman.npc.Ghost}).
 * </p>
 *
 * @author Refactoring: Strategy pattern applied to ghost target selection
 * @see GhostTargetStrategy
 */
public class ClydeTargetStrategy implements GhostTargetStrategy {

    /**
     * The minimum path length (in grid squares) Clyde wants to keep between
     * himself and Pac-Man before switching to flee mode.
     */
    private static final int SHYNESS = 8;

    /**
     * Lookup map for the direction opposite to each cardinal direction, used
     * when computing the flee target.
     */
    private static final Map<Direction, Direction> OPPOSITES =
        new EnumMap<>(Direction.class);

    static {
        OPPOSITES.put(Direction.NORTH, Direction.SOUTH);
        OPPOSITES.put(Direction.SOUTH, Direction.NORTH);
        OPPOSITES.put(Direction.WEST, Direction.EAST);
        OPPOSITES.put(Direction.EAST, Direction.WEST);
    }

    /**
     * Selects Clyde's target square based on his distance to Pac-Man.
     *
     * <p>
     * If Clyde is more than {@value #SHYNESS} squares away from Pac-Man, the
     * target is Pac-Man's square (chase mode). If Clyde is within
     * {@value #SHYNESS} squares, the target is the square behind Clyde
     * (flee mode), computed by following the path to Pac-Man one step and then
     * taking the opposite direction.
     * </p>
     *
     * @param ghost the ghost unit invoking this strategy (never {@code null})
     * @return an {@link Optional} containing the selected target square, or
     *         empty if no player is reachable
     */
    @Override
    public Optional<Square> selectTarget(Unit ghost) {
        Unit nearest = Navigation.findNearest(Player.class, ghost.getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square playerSquare = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(ghost.getSquare(), playerSquare, ghost);
        if (path == null || path.isEmpty()) {
            return Optional.empty();
        }

        if (path.size() <= SHYNESS) {
            // Flee: move one step in the opposite direction of the first step towards the player.
            Direction fleeDirection = OPPOSITES.get(path.get(0));
            return Optional.of(ghost.getSquare().getSquareAt(fleeDirection));
        }
        // Chase: head towards Pac-Man.
        return Optional.of(playerSquare);
    }
}
