import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class Classic extends JFrame implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    static final int WINNING_SCORE = 7; // Define the winning score
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    GamePanel panel;
    MainMenu mainMenu; // Reference to the main menu

    // Sound clips
    Clip paddleHitSound;
    Clip scoreSound;
    Clip winSound;

    Classic (){

        mainMenu = new MainMenu(); // Create a new instance of the main menu
        panel = new GamePanel(mainMenu); // Pass the main menu instance to the game panel
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.BLACK); // Set background color of JFrame to black
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide window instead of exiting
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // Load sound files
        try {
            paddleHitSound = AudioSystem.getClip();
            AudioInputStream paddleHitStream = AudioSystem.getAudioInputStream(new File("lib\\Ball_Return.wav"));
            paddleHitSound.open(paddleHitStream);

            scoreSound = AudioSystem.getClip();
            AudioInputStream scoreStream = AudioSystem.getAudioInputStream(new File("lib\\score.wav"));
            scoreSound.open(scoreStream);

            winSound = AudioSystem.getClip();
            AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("lib\\win.wav"));
            winSound.open(winStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTicks =60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now -lastTime)/ns;
            lastTime = now;
            if(delta >=1) {
                move();
                checkCollision();
                checkWinner(); // Check for winner after each move
                panel.repaint();
                delta--;
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

        @SuppressWarnings("unused")
        private MainMenu mainMenu; // Reference to the main menu

        GamePanel(MainMenu mainMenu){
            this.mainMenu = mainMenu; // Save reference to the main menu
            newPaddles();
            newBall();
            score = new Score(GAME_WIDTH,GAME_HEIGHT);
            this.setFocusable(true);
            this.addKeyListener(new ActionListener());
            this.setPreferredSize(SCREEN_SIZE);
            this.setBackground(Color.BLACK); // Set background color of JPanel to black

            gameThread = new Thread(Classic.this);
            gameThread.start();
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
            score.draw(g);
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
            playPaddleHitSound(); // Play paddle hit sound
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
            playPaddleHitSound(); // Play paddle hit sound
        }
        //stops paddles at window edges
        if(paddle1.y<=0)
            paddle1.y=0;
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
        //give a player 1 point and creates new paddles & ball
        if(ball.x <=0) {
            score.player2++;
            newPaddles();
            newBall();
            playScoreSound(); // Play score sound
            System.out.println("Player 2: "+score.player2);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            playScoreSound(); // Play score sound
            System.out.println("Player 1: "+score.player1);
        }
    }

    public void checkWinner() {
        if (score.player1 >= WINNING_SCORE || score.player2 >= WINNING_SCORE) {
            String winner = (score.player1 >= WINNING_SCORE) ? "Player 1" : "Player 2";
            playWinSound();
            int choice = JOptionPane.showConfirmDialog(panel, winner + " wins!\n\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                // Restart the game
                score.player1 = 0;
                score.player2 = 0;
                newPaddles();
                newBall();
            } else {
                // Stop the game loop
                gameThread.interrupt(); // Interrupt the game thread to stop the loop
                
                // Schedule the window disposal outside of the event dispatch thread
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        dispose(); // Dispose current window
                    }
                });
            }
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

    // Method to play score sound
    public void playScoreSound() {
        if (scoreSound != null && !scoreSound.isRunning()) {
            scoreSound.setFramePosition(0); // Rewind to the beginning
            scoreSound.start(); // Play the sound
        }
    }

    public void playWinSound() {
        if (winSound != null && !winSound.isRunning()) {
            winSound.setFramePosition(0); // Rewind to the beginning
            winSound.start(); // Play the sound
        }
    }
}
