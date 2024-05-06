import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class MainMode extends JFrame implements Runnable {

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

    MainMode(){
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
    }

    public static void main(String[] args) {
        new MainMode();
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

            gameThread = new Thread(MainMode.this);
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
            System.out.println("Player 2: "+score.player2);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.player1);
        }
    }
    
    // Check for the winner
    public void checkWinner() {
        if (score.player1 >= WINNING_SCORE || score.player2 >= WINNING_SCORE) {
            String winner = (score.player1 >= WINNING_SCORE) ? "Player 1" : "Player 2";
            int choice = JOptionPane.showConfirmDialog(panel, winner + " wins!\n\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                // Restart the game
                score.player1 = 0;
                score.player2 = 0;
                newPaddles();
                newBall();
            } else {
                // Generate a new main menu instance and dispose the current window
                mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // Dispose current window
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
}