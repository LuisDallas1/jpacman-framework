package nl.tudelft.jpacman.level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.board.UnitVisitor;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A player operated unit in our game.
 *
 * <p>
 * <strong>Observer pattern:</strong> {@code Player} is the <em>Subject</em>.
 * It maintains a set of {@link PlayerObserver}s and notifies them whenever its
 * observable state changes:
 * <ul>
 *   <li>{@link #addPoints(int)} fires {@link PlayerObserver#scoreChanged(int)}.</li>
 *   <li>{@link #setAlive(boolean)} fires {@link PlayerObserver#playerDied()} when
 *       {@code isAlive} becomes {@code false}.</li>
 * </ul>
 * This eliminates the need for any external component to poll
 * {@link #getScore()} or {@link #isAlive()} on a fixed timer.
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Player extends Unit {

    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * The amount of points accumulated by this player.
     */
    private int score;

    /**
     * The animations for every direction.
     */
    private final Map<Direction, Sprite> sprites;

    /**
     * The animation that is to be played when Pac-Man dies.
     */
    private final AnimatedSprite deathSprite;

    /**
     * <code>true</code> iff this player is alive.
     */
    private boolean alive;

    /**
     * The set of observers registered to receive state-change notifications.
     * Uses a {@link HashSet} to match the convention used by
     * {@link Level.LevelObserver} (the existing Level observer collection).
     */
    private final Set<PlayerObserver> observers = new HashSet<>();

    /**
     * Creates a new player with a score of 0 points.
     *
     * @param spriteMap
     *            A map containing a sprite for this player for every direction.
     * @param deathAnimation
     *            The sprite to be shown when this player dies.
     */
    protected Player(Map<Direction, Sprite> spriteMap, AnimatedSprite deathAnimation) {
        this.score = 0;
        this.alive = true;
        this.sprites = spriteMap;
        this.deathSprite = deathAnimation;
        deathSprite.setAnimating(false);
    }

    /**
     * Registers an observer to be notified of player state changes.
     *
     * @param observer The observer to add. Must not be {@code null}.
     */
    public void addObserver(PlayerObserver observer) {
        assert observer != null;
        observers.add(observer);
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer The observer to remove.
     */
    public void removeObserver(PlayerObserver observer) {
        observers.remove(observer);
    }

    /**
     * Returns whether this player is alive or not.
     *
     * @return <code>true</code> iff the player is alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets whether this player is alive or not.
     *
     * <p>
     * When {@code isAlive} is {@code false}, all registered
     * {@link PlayerObserver}s are notified via {@link PlayerObserver#playerDied()}.
     * </p>
     *
     * @param isAlive
     *            <code>true</code> iff this player is alive.
     */
    public void setAlive(boolean isAlive) {
        this.alive = isAlive;
        if (isAlive) {
            deathSprite.setAnimating(false);
        } else {
            deathSprite.restart();
            notifyPlayerDied();
        }
    }

    /**
     * Returns the amount of points accumulated by this player.
     *
     * @return The amount of points accumulated by this player.
     */
    public int getScore() {
        return score;
    }

    @Override
    public Sprite getSprite() {
        if (isAlive()) {
            return sprites.get(getDirection());
        }
        return deathSprite;
    }

    /**
     * Adds points to the score of this player and notifies all registered
     * {@link PlayerObserver}s via {@link PlayerObserver#scoreChanged(int)}.
     *
     * @param points
     *            The amount of points to add to the points this player already
     *            has.
     */
    public void addPoints(int points) {
        score += points;
        notifyScoreChanged();
    }

    /**
     * Notifies all observers that the score has changed.
     */
    private void notifyScoreChanged() {
        for (PlayerObserver observer : observers) {
            observer.scoreChanged(score);
        }
    }

    /**
     * Notifies all observers that the player has died.
     */
    private void notifyPlayerDied() {
        for (PlayerObserver observer : observers) {
            observer.playerDied();
        }
    }
}
