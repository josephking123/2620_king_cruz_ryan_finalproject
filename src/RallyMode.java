import java.util.Random;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RallyMode extends JFrame {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    GamePanel panel;
    int rallyScore = 0;
    boolean running = true;
    Clip paddleHitSound;
    Clip winSound;
   
    private Leaderboard leaderboard;

    RallyMode(Leaderboard leaderboard){
        panel = new GamePanel(); // Pass the main menu instance to the game panel

        this.add(panel);
        this.setTitle("Rally Game");
        this.setResizable(false);
        this.setBackground(Color.BLACK); // Set background color of JFrame to black
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide window instead of exiting
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.leaderboard = leaderboard;

        try {
            paddleHitSound = AudioSystem.getClip();
            AudioInputStream paddleHitStream = AudioSystem.getAudioInputStream(new File("lib\\Ball_Return.wav"));
            paddleHitSound.open(paddleHitStream);

            winSound = AudioSystem.getClip();
            AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("lib\\win.wav"));
            winSound.open(winStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

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
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }

    public class GamePanel extends JPanel {

        GamePanel(){
            newPaddles();
            newBall();
            score = new Score(GAME_WIDTH,GAME_HEIGHT);
            this.setFocusable(true);
            this.addKeyListener(new ActionListener());
            this.setPreferredSize(SCREEN_SIZE);
            this.setBackground(Color.BLACK); // Set background color of JPanel to black
        }
    
        public void paintComponent(Graphics g) {
            // Paint the background color
            super.paintComponent(g);
            draw(g);
        }
        
        public void draw(Graphics g) {
            paddle1.draw(g);
            paddle2.draw(g);
            ball.draw(g);
            g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
    
            // Draw combined score
            Font font = new Font("Consolas", Font.BOLD, 50);
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("" + rallyScore, GAME_WIDTH/2 - 100, 50);
    
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {

        //bounce ball off top & bottom window edges
        if(ball.y <=0) {
            ball.setYDirection(-ball.yV);
        }
        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
            ball.setYDirection(-ball.yV);
        }
        //bounce ball off paddles
        if(ball.intersects(paddle1)) {
            ball.xV = Math.abs(ball.xV);
            ball.xV++; //optional for more difficulty
            if(ball.yV>0)
                ball.yV++; //optional for more difficulty
            else
                ball.yV--;
            ball.setXDirection(ball.xV);
            ball.setYDirection(ball.yV);
            score.player1++;
            playPaddleHitSound();            

        }
        if(ball.intersects(paddle2)) {
            ball.xV = Math.abs(ball.xV);
            ball.xV++; //optional for more difficulty
            if(ball.yV>0)
                ball.yV++; //optional for more difficulty
            else
                ball.yV--;
            ball.setXDirection(-ball.xV);
            ball.setYDirection(ball.yV);
            score.player2++;
            playPaddleHitSound();
            
        }

        rallyScore = score.player1 + score.player2;

        //stops paddles at window edges
        if(paddle1.y<=0)
            paddle1.y=0;
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;


        if(ball.x <=0) {
            finish();
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            finish();
        }
    }

    public void finish(){
        playWinSound();
        
        leaderboard.updateHighScore("Rally", rallyScore);
        int choice = JOptionPane.showConfirmDialog(panel, "Final Score: " + rallyScore + " \n\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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


    public class ActionListener extends KeyAdapter{
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
        if (paddleHitSound != null && !paddleHitSound.isRunning()) {
            paddleHitSound.setFramePosition(0); // Rewind to the beginning
            paddleHitSound.start(); // Play the sound
        }
    }

    public void playWinSound() {
        if (winSound != null && !winSound.isRunning()) {
            winSound.setFramePosition(0); // Rewind to the beginning
            winSound.start(); // Play the sound
        }
    }
}
