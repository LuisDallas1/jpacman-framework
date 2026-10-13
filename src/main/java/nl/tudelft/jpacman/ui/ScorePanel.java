package nl.tudelft.jpacman.ui;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerObserver;

/**
 * A panel consisting of a column for each player, with the numbered players on
 * top and their respective scores underneath.
 *
 * <p>
 * <strong>Observer pattern:</strong> {@code ScorePanel} is the
 * <em>ConcreteObserver</em>. It implements {@link PlayerObserver} and registers
 * itself as an observer of each {@link Player} at construction time. Label
 * updates are now driven by events ({@link #scoreChanged(int)} and
 * {@link #playerDied()}) rather than by the periodic {@code refresh()} call
 * that {@link PacManUI} previously scheduled every 40 ms.
 * </p>
 *
 * <p>
 * Because Swing updates must happen on the Event Dispatch Thread, all label
 * mutations are wrapped with {@link SwingUtilities#invokeLater}.
 * </p>
 *
 * @author Jeroen Roosen
 */
public class ScorePanel extends JPanel implements PlayerObserver {

    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map of players and the labels their scores are on.
     */
    private final Map<Player, JLabel> scoreLabels;

    /**
     * The default way in which the score is shown.
     */
    public static final ScoreFormatter DEFAULT_SCORE_FORMATTER =
        (Player player) -> String.format("Score: %3d", player.getScore());

    /**
     * The way to format the score information.
     */
    private ScoreFormatter scoreFormatter = DEFAULT_SCORE_FORMATTER;

    /**
     * Creates a new score panel with a column for each player and registers
     * itself as an observer of each player.
     *
     * @param players
     *            The players to display the scores of.
     */
    public ScorePanel(List<Player> players) {
        super();
        assert players != null;

        setLayout(new GridLayout(2, players.size()));

        for (int i = 1; i <= players.size(); i++) {
            add(new JLabel("Player " + i, JLabel.CENTER));
        }
        scoreLabels = new LinkedHashMap<>();
        for (Player player : players) {
            JLabel scoreLabel = new JLabel("0", JLabel.CENTER);
            scoreLabels.put(player, scoreLabel);
            add(scoreLabel);
            // Register as observer — no polling needed anymore.
            player.addObserver(this);
        }
    }

    /**
     * Called by {@link Player} when a player's score increases.
     *
     * <p>
     * Finds the label for the affected player and updates its text on the
     * Swing Event Dispatch Thread.
     * </p>
     *
     * @param newScore The player's updated total score (unused directly; the
     *                 label is refreshed from the player object to keep
     *                 formatting consistent with {@link #scoreFormatter}).
     */
    @Override
    public void scoreChanged(int newScore) {
        SwingUtilities.invokeLater(this::refreshAllLabels);
    }

    /**
     * Called by {@link Player} when the player dies.
     *
     * <p>
     * Updates all labels on the Swing Event Dispatch Thread so the "You died."
     * prefix appears immediately.
     * </p>
     */
    @Override
    public void playerDied() {
        SwingUtilities.invokeLater(this::refreshAllLabels);
    }

    /**
     * Refreshes all score labels from the current player state.
     *
     * <p>
     * This method replaces the old {@code refresh()} that was called
     * periodically by {@link PacManUI}. It is now invoked only when a player
     * event fires, eliminating redundant work during frames where nothing changed.
     * </p>
     *
     * <p>
     * Must be called on the Swing Event Dispatch Thread.
     * </p>
     */
    private void refreshAllLabels() {
        for (Map.Entry<Player, JLabel> entry : scoreLabels.entrySet()) {
            Player player = entry.getKey();
            String score = "";
            if (!player.isAlive()) {
                score = "You died. ";
            }
            score += scoreFormatter.format(player);
            entry.getValue().setText(score);
        }
    }

    /**
     * Provide means to format the score for a given player.
     */
    public interface ScoreFormatter {

        /**
         * Format the score of a given player.
         * @param player The player and its score
         * @return Formatted score.
         */
        String format(Player player);
    }

    /**
     * Let the score panel use a dedicated score formatter.
     * @param scoreFormatter Score formatter to be used.
     */
    public void setScoreFormatter(ScoreFormatter scoreFormatter) {
        assert scoreFormatter != null;
        this.scoreFormatter = scoreFormatter;
    }
}
