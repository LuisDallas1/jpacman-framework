package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

import java.util.Optional;

/**
 * Concrete {@link GhostTargetStrategy} that always signals "no target",
 * causing the ghost to fall back to the random-walk behaviour provided by
 * {@link nl.tudelft.jpacman.npc.Ghost#randomMove()}.
 *
 * <p>
 * This strategy replaces the {@code RandomGhost} inner class that existed in
 * {@link nl.tudelft.jpacman.level.LevelFactory}. Any ghost that should wander
 * without purpose can now be configured with this strategy instead of requiring
 * a dedicated subclass.
 * </p>
 *
 * <p>
 * This class is a <em>ConcreteStrategy</em> in the Strategy pattern.
 * </p>
 *
 * @author Refactoring: Strategy pattern applied to ghost target selection
 * @see GhostTargetStrategy
 */
public class RandomTargetStrategy implements GhostTargetStrategy {

    /**
     * Always returns {@link Optional#empty()}, delegating movement entirely to
     * the random-walk fallback in the context ghost.
     *
     * @param ghost the ghost unit invoking this strategy (unused)
     * @return an empty {@link Optional}
     */
    @Override
    public Optional<Square> selectTarget(Unit ghost) {
        return Optional.empty();
    }
}
