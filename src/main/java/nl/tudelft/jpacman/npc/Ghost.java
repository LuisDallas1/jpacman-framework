package nl.tudelft.jpacman.npc;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.ghost.GhostTargetStrategy;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A non-player unit (ghost / NPC).
 *
 * <p>
 * <strong>Strategy pattern:</strong> ghost movement is split into two
 * responsibilities:
 * <ol>
 *   <li><em>Target selection</em> — delegated to a {@link GhostTargetStrategy}
 *       that is injected at construction time. Each ghost personality supplies
 *       its own concrete strategy (e.g. chase, intercept, flee).</li>
 *   <li><em>Navigation skeleton</em> — the common steps of validating the
 *       target, computing the shortest path and extracting the first direction
 *       are centralised here in {@link #nextAiMove()}, eliminating the
 *       duplicated boilerplate that previously appeared in every subclass.</li>
 * </ol>
 *
 * <p>
 * Subclasses are now only responsible for supplying the correct
 * {@link GhostTargetStrategy} and their own timing constants; they no longer
 * need to override {@code nextAiMove()}.
 * </p>
 *
 * @author Jeroen Roosen
 */
public abstract class Ghost extends Unit {

    /**
     * The sprite map, one sprite for each direction.
     */
    private final Map<Direction, Sprite> sprites;

    /**
     * The base move interval of the ghost.
     */
    private final int moveInterval;

    /**
     * The random variation added to the {@link #moveInterval}.
     */
    private final int intervalVariation;

    /**
     * The strategy that selects this ghost's movement target.
     * Injected at construction; can be replaced at runtime to change behaviour.
     */
    private GhostTargetStrategy targetStrategy;

    /**
     * Creates a new ghost.
     *
     * @param spriteMap         The sprites for every direction.
     * @param moveInterval      The base interval of movement.
     * @param intervalVariation The variation of the interval.
     * @param targetStrategy    The strategy used to select the ghost's target
     *                          square each turn. Must not be {@code null}.
     */
    protected Ghost(Map<Direction, Sprite> spriteMap,
                    int moveInterval,
                    int intervalVariation,
                    GhostTargetStrategy targetStrategy) {
        this.sprites = spriteMap;
        this.intervalVariation = intervalVariation;
        this.moveInterval = moveInterval;
        this.targetStrategy = targetStrategy;
    }

    /**
     * Replaces the current target-selection strategy at runtime.
     *
     * <p>
     * This method enables dynamic behaviour changes during a game (e.g.,
     * switching a ghost from chase mode to scatter mode) without altering the
     * ghost's identity or recreating the object.
     * </p>
     *
     * @param strategy The new strategy to apply. Must not be {@code null}.
     */
    public void setTargetStrategy(GhostTargetStrategy strategy) {
        this.targetStrategy = strategy;
    }

    /**
     * Returns the currently active target-selection strategy.
     *
     * @return The current {@link GhostTargetStrategy}. Never {@code null}.
     */
    public GhostTargetStrategy getTargetStrategy() {
        return targetStrategy;
    }

    /**
     * Calculates the next move for this unit and returns the direction to move
     * in.
     * <p>
     * Precondition: The NPC occupies a square (hasSquare() holds).
     *
     * @return The direction to move in, or {@code null} if no move could
     *         be devised.
     */
    public Direction nextMove() {
        return nextAiMove().orElseGet(this::randomMove);
    }

    /**
     * Computes the ghost's next AI-driven move using the injected
     * {@link GhostTargetStrategy}.
     *
     * <p>
     * <strong>Template / common skeleton:</strong>
     * <ol>
     *   <li>Ask the strategy for a target {@link Square}.</li>
     *   <li>If no target is available, return empty (fall back to random move).</li>
     *   <li>Compute the shortest path from the ghost's current square to the
     *       target using {@link Navigation#shortestPath}.</li>
     *   <li>Return the first direction of that path, or empty if no path
     *       exists.</li>
     * </ol>
     * This common logic was previously duplicated inside every subclass's
     * {@code nextAiMove()} override.
     * </p>
     *
     * @return An {@link Optional} containing the move direction, or empty if no
     *         AI move could be computed.
     */
    public Optional<Direction> nextAiMove() {
        assert hasSquare();

        Optional<Square> target = targetStrategy.selectTarget(this);
        if (target.isEmpty()) {
            return Optional.empty();
        }

        List<Direction> path = Navigation.shortestPath(getSquare(), target.get(), this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Sprite getSprite() {
        return sprites.get(getDirection());
    }

    /**
     * The time that should be taken between moves.
     *
     * @return The suggested delay between moves in milliseconds.
     */
    public long getInterval() {
        return this.moveInterval + new Random().nextInt(this.intervalVariation);
    }

    /**
     * Determines a possible move in a random direction.
     *
     * @return A direction in which the ghost can move, or {@code null} if
     *         the ghost is shut in by inaccessible squares.
     */
    protected Direction randomMove() {
        Square square = getSquare();
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (square.getSquareAt(direction).isAccessibleTo(this)) {
                directions.add(direction);
            }
        }
        if (directions.isEmpty()) {
            return null;
        }
        int i = new Random().nextInt(directions.size());
        return directions.get(i);
    }
}
