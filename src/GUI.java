import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * The GUI class represents the main graphical user interface for the Pong game.
 * It extends the JFrame class and implements the Runnable interface to support multithreading.
 */
public class GUI extends JFrame implements Runnable{

    private static final int GAME_WIDTH = 1000;
    private static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_WIDTH = 25;
    private static final int PADDLE_HEIGHT = 100;
    private Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Random random;
    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private Score score;


    /**
     * Constructs a GUI object for the Pong game.
     * Initializes the game elements, sets up the window, and starts the game thread.
     */
    GUI(){
        newPaddles();
        newBall();
        this.addKeyListener(new ActionListener());
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    /**
     * Creates a new ball object with random initial position.
     */
    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    /**
     * Creates new paddles for player 1 and player 2.
     */
    public void newPaddles() {
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
    }

    /**
     * The run method required by the Runnable interface.
     * This method is executed when the game thread is started.
     */
    @Override
    public void run() {

    }
    
    /**
     * ActionListener to handle keyboard input for controlling the paddles.
     */
    public class ActionListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        @Override
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
