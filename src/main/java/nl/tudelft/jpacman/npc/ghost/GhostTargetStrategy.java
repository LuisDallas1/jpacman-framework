package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

import java.util.Optional;

/**
 * Strategy interface for selecting the movement target of a ghost (NPC).
 *
 * <p>
 * This interface is part of the <em>Strategy</em> design pattern applied to
 * the ghost AI system. Each concrete implementation encapsulates a distinct
 * target-selection algorithm (e.g., chasing Pac-Man directly, targeting a
 * square ahead of Pac-Man, or wandering randomly), decoupling the
 * <em>what-to-target</em> decision from the <em>how-to-navigate</em>
 * mechanics that are centralised in {@link nl.tudelft.jpacman.npc.Ghost}.
 * </p>
 *
 * <p>
 * A strategy receives the ghost's current square and may query the board as
 * needed to determine the desired destination. Returning {@link Optional#empty()}
 * signals that no AI-driven target is available and the ghost should fall back
 * to a random move.
 * </p>
 *
 * @author Refactoring: Strategy pattern applied to ghost target selection
 * @see nl.tudelft.jpacman.npc.Ghost
 */
public interface GhostTargetStrategy {

    /**
     * Determines the target square for the ghost's next move.
     *
     * @param ghost
     *            The ghost unit that is about to move. Never {@code null}.
     *            Precondition: {@code ghost.hasSquare()} holds.
     * @return An {@link Optional} containing the target {@link Square} if the
     *         strategy can determine a destination, or an empty Optional if
     *         no meaningful target exists (causing the ghost to fall back to a
     *         random move).
     */
    Optional<Square> selectTarget(Unit ghost);
}
