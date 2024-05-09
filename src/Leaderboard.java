import java.util.HashMap;

/**
 * The Leaderboard class manages the high scores for different game modes.
 * It allows updating and retrieving high scores for each game mode.
 */
public class Leaderboard {

    private HashMap<String, Integer> highScores; // Map game mode names to high scores

    /**
     * Constructs a new Leaderboard object.
     * Initializes the highScores HashMap.
     */
    public Leaderboard() {
        highScores = new HashMap<>();
    }

    /**
     * Updates the high score for a specific game mode.
     * If the provided score is higher than the existing high score for the game mode, it is updated.
     * @param gameMode The name of the game mode.
     * @param score The new high score for the game mode.
     */
    public void updateHighScore(String gameMode, int score) {
        if (!highScores.containsKey(gameMode) || score > highScores.get(gameMode)) {
            highScores.put(gameMode, score);
        }
    }

    /**
     * Retrieves the high score for a specific game mode.
     * @param gameMode The name of the game mode.
     * @return The high score for the specified game mode, or 0 if no high score is found.
     */
    public int getHighScore(String gameMode) {
        return highScores.getOrDefault(gameMode, 0); // Return 0 if no high score found
    }

    /**
     * Retrieves all high scores stored in the leaderboard.
     * @return A HashMap containing game mode names mapped to their respective high scores.
     */
    public HashMap<String, Integer> getAllHighScores() {
        return highScores;
    }
}
