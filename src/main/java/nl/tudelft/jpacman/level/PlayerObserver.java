package nl.tudelft.jpacman.level;

/**
 * Observer interface for events produced by a {@link Player}.
 *
 * <p>
 * This interface is the <em>Observer</em> role in the Observer design pattern
 * applied to player state changes. It mirrors the existing
 * {@link nl.tudelft.jpacman.level.Level.LevelObserver} mechanism: just as
 * {@code Level} notifies registered observers when the level is won or lost,
 * {@code Player} now notifies registered {@code PlayerObserver}s when the
 * score changes or the player dies.
 * </p>
 *
 * <p>
 * Interested parties (e.g. {@link nl.tudelft.jpacman.ui.ScorePanel}, a sound
 * engine, or an achievement tracker) implement this interface and register
 * themselves via {@link Player#addObserver(PlayerObserver)}. They are then
 * notified on demand instead of polling the player state periodically.
 * </p>
 *
 * @author Refactoring: Observer pattern applied to Player events
 * @see Player
 */
public interface PlayerObserver {

    /**
     * Called when the player's score increases.
     *
     * @param newScore The player's updated total score after the change.
     */
    void scoreChanged(int newScore);

    /**
     * Called when the player dies, i.e. {@link Player#setAlive(boolean)} is
     * invoked with {@code false}.
     */
    void playerDied();
}
