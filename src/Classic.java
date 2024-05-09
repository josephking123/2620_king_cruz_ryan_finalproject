import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * The Classic class represents the classic Pong game mode.
 * It extends JFrame and provides the main game functionality.
 */
public class Classic extends JFrame {

    private static final int GAME_WIDTH = 1000;
    private static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_WIDTH = 25;
    private static final int PADDLE_HEIGHT = 100;
    private static final int WINNING_SCORE = 3; // Define the winning score
    private Leaderboard leaderboard;

    private Random random;
    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private Score score;
    private GamePanel panel;
    private boolean running = true;

    private SoundPlayer paddleHitSound;
    private SoundPlayer scoreSound;
    private SoundPlayer winSound;

    /**
     * Constructs a Classic object with the specified leaderboard.
     * @param leaderboard The leaderboard to update scores.
     */
    Classic(Leaderboard leaderboard) {

        panel = new GamePanel(); // Pass the main menu instance to the game panel
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.BLACK); // Set background color of JFrame to black
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide window instead of exiting
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.leaderboard = leaderboard;

        // Load sound files
        paddleHitSound = new SoundPlayer("lib\\Ball_Return.wav");
        scoreSound = new SoundPlayer("lib\\score.wav");
        winSound = new SoundPlayer("lib\\win.wav");

        // Start game thread
        Thread gameThread = new Thread(new GameLoop());
        gameThread.start();
    }

     /**
     * The GameLoop class implements the game loop.
     */
    class GameLoop implements Runnable {
        public void run() {
            while (running) { // Check the running flag
                move();
                checkCollision();
                checkWinner(); // Check for winner after each move
                panel.repaint();
                try {
                    Thread.sleep(16); // Cap the frame rate to approximately 60 fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if there is a winner in the game.
     */
    public void checkWinner() {
        if (score.player1 >= WINNING_SCORE || score.player2 >= WINNING_SCORE) {
            if (Math.abs(score.player1 - score.player2) >= 2) {
                String winner = (score.player1 >= WINNING_SCORE) ? "Player 1" : "Player 2";
                playWinSound();

                int highScore = Math.max(score.player1, score.player2); // Determine high score
                // Update leaderboard with high score
                leaderboard.updateHighScore("Classic", highScore);

                int choice = JOptionPane.showConfirmDialog(panel, winner + " wins!\n\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    // Restart the game
                    score.player1 = 0;
                    score.player2 = 0;
                    newPaddles();
                    newBall();
                } else {
                    running = false;
                    dispose();
                }
            }
        }
    }

    /**
     * Creates a new ball in the game.
     */
    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    /**
     * Creates new paddles in the game.
     */
    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    /**
     * The GamePanel class represents the panel where the game is drawn.
     */
    class GamePanel extends JPanel {

        /**
         * Constructs a GamePanel object.
         */
        GamePanel() {
            newPaddles();
            newBall();
            score = new Score(GAME_WIDTH, GAME_HEIGHT);
            this.setFocusable(true);
            this.addKeyListener(new ActionListener());
            this.setPreferredSize(SCREEN_SIZE);
            this.setBackground(Color.BLACK); // Set background color of JPanel to black
        }

        /**
         * Paints the components of the panel.
         * @param g The Graphics object used to paint.
         */
        public void paintComponent(Graphics g) {
            // Paint the background color
            super.paintComponent(g);
            draw(g);
        }

        /**
         * Draws the game components.
         * @param g The Graphics object used for drawing.
         */
        public void draw(Graphics g) {
            paddle1.draw(g);
            paddle2.draw(g);
            ball.draw(g);
            score.draw(g);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    /**
     * Moves the game objects.
     */
    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    /**
     * Checks collision between game objects.
     */
    public void checkCollision() {

        // bounce ball off top & bottom window edges
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yV);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yV);
        }
        // bounce ball off paddles
        if (ball.intersects(paddle1)) {
            ball.xV = Math.abs(ball.xV);
            ball.xV++; // optional for more difficulty
            if (ball.yV > 0)
                ball.yV++; // optional for more difficulty
            else
                ball.yV--;
            ball.setXDirection(ball.xV);
            ball.setYDirection(ball.yV);
            playPaddleHitSound(); // Plays paddle hit sound
        }
        if (ball.intersects(paddle2)) {
            ball.xV = Math.abs(ball.xV);
            ball.xV++; // optional for more difficulty
            if (ball.yV > 0)
                ball.yV++; // optional for more difficulty
            else
                ball.yV--;
            ball.setXDirection(-ball.xV);
            ball.setYDirection(ball.yV);
            playPaddleHitSound(); // Plays paddle hit sound
        }
        // stops paddles at window edges
        if (paddle1.y <= 0)
            paddle1.y = 0;
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (paddle2.y <= 0)
            paddle2.y = 0;
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        // gives a player 1 point and creates new paddles & ball
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
            playScoreSound(); // Play score sound
            System.out.println("Player 2: " + score.player2);
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            playScoreSound(); // Play score sound
            System.out.println("Player 1: " + score.player1);
        }
    }

    class ActionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }

    // Method to play paddle hit sound
    public void playPaddleHitSound() {
        paddleHitSound.play();
    }

    // Method to play score sound
    public void playScoreSound() {
        scoreSound.play();
    }

    public void playWinSound() {
        winSound.play();
    }
}
