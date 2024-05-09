import java.util.HashMap;

public class Leaderboard {

    private HashMap<String, Integer> highScores; // Map game mode names to high scores

    public Leaderboard() {
        highScores = new HashMap<>();
    }

    // Method to update high score for a game mode
    public void updateHighScore(String gameMode, int score) {
        if (!highScores.containsKey(gameMode) || score > highScores.get(gameMode)) {
            highScores.put(gameMode, score);
        }
    }

    // Method to get high score for a game mode
    public int getHighScore(String gameMode) {
        return highScores.getOrDefault(gameMode, 0); // Return 0 if no high score found
    }

    // Method to get all high scores
    public HashMap<String, Integer> getAllHighScores() {
        return highScores;
    }
}