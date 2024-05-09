import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CastleDefenseMode extends JFrame {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    static final int WALL_HEIGHT = GAME_HEIGHT; // Make wall height same as game height
    
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    GamePanel panel;
    Wall leftWalls;
    Wall rightWalls;
    boolean leftWallBroken = false;
    boolean rightWallBroken = false;
    boolean running = true;

    SoundPlayer paddleHitSound;
    SoundPlayer wallBreakSound;
    SoundPlayer winSound;

    CastleDefenseMode() {

        panel = new GamePanel(); // Pass the main menu instance to the game panel

        this.add(panel);
        this.setTitle("Defense Game");
        this.setResizable(false);
        this.setBackground(Color.BLACK); // Set background color of JFrame to black
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide window instead of exiting
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        paddleHitSound = new SoundPlayer("lib\\Ball_Return.wav");
        wallBreakSound = new SoundPlayer("lib\\BrickBreak.wav");
        winSound = new SoundPlayer("lib\\win.wav");
       

        Thread gameThread = new Thread(new GameLoop());
        gameThread.start();
    }

    class GameLoop implements Runnable {
        public void run() {
            while (running) { // Check the running flag
                move();
                checkCollision();
                panel.repaint();
                try {
                    Thread.sleep(16); // Cap the frame rate to approximately 60 fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(100, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH - 100, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void newWalls() {
        leftWalls = new Wall(0, 0, PADDLE_WIDTH, WALL_HEIGHT, 1); // Create left wall
        rightWalls = new Wall(GAME_WIDTH - PADDLE_WIDTH, 0, PADDLE_WIDTH, WALL_HEIGHT, 2); // Create right wall
    }

    public class GamePanel extends JPanel {

        GamePanel() {
            newPaddles();
            newBall();
            newWalls();
            this.setFocusable(true);
            this.addKeyListener(new ActionListener());
            this.setPreferredSize(SCREEN_SIZE);
            this.setBackground(Color.BLACK); 
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            paddle1.draw(g);
            paddle2.draw(g);
            ball.draw(g);
            if (!leftWallBroken) leftWalls.draw(g);
            if (!rightWallBroken) rightWalls.draw(g);
            g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

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
            ball.setXDirection(Math.abs(ball.xV));
            ball.xV++;
            ball.setYDirection(ball.yV);
            playPaddleHitSound();
        }
        if (ball.intersects(paddle2)) {
            ball.setXDirection(-Math.abs(ball.xV));
            ball.setYDirection(ball.yV);
            playPaddleHitSound();
        }

        // bounce ball off walls
        if (ball.intersects(leftWalls) && !leftWallBroken) {
            ball.setXDirection(-ball.xV);
            leftWalls.hit();
            playWallHitSound();
            if (leftWalls.isBroken()) {
                leftWallBroken = true;
            }
        }
        if (ball.intersects(rightWalls) && !rightWallBroken) {
            ball.setXDirection(-ball.xV);
            rightWalls.hit();
            playWallHitSound();
            if (rightWalls.isBroken()) {
                rightWallBroken = true;
            }
        }

        // stops paddles at window edges
        if (paddle1.y <= 0) paddle1.y = 0;
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (paddle2.y <= 0) paddle2.y = 0;
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (leftWallBroken && ball.x <= 0) {
            finish();
        }
        if (rightWallBroken && ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            finish();
        }
    }

    public void finish() {
        playWinSound();

        int choice = JOptionPane.showConfirmDialog(panel, "Final Score:  \n\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            // Restart the game
            newPaddles();
            newBall();
            newWalls();
            leftWallBroken = false;
            rightWallBroken = false;
        } else {
            running = false;
            dispose(); 
        }
    }

    public class ActionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }

    public void playPaddleHitSound() {
        paddleHitSound.play();
    }

    public void playWinSound() {
        winSound.play();
    }

    public void playWallHitSound() {
       wallBreakSound.play();
    }
}