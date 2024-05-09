import java.awt.*;
import java.awt.event.*;

/**
 * The Paddle class represents the paddle object in the Pong game.
 * It extends the Rectangle class to inherit position and size properties.
 */
public class Paddle extends Rectangle{

	private int id;
	private int yV;
	private int speed = 10;
	
	/**
     * Constructs a Paddle object with the specified position, size, and identifier.
     * @param x The x-coordinate of the top-left corner of the paddle.
     * @param y The y-coordinate of the top-left corner of the paddle.
     * @param PADDLE_WIDTH The width of the paddle.
     * @param PADDLE_HEIGHT The height of the paddle.
     * @param id The identifier for the paddle (1 for player 1, 2 for player 2).
     */
	Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		this.id=id;
	}
	
	/**
     * Handles key pressed events to move the paddle.
     * @param e The KeyEvent associated with the key press.
     */
	public void keyPressed(KeyEvent e) {
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(speed);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(-speed);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(speed);
			}
			break;
		}
	}

	/**
     * Handles key released events to stop the paddle movement.
     * @param e The KeyEvent associated with the key release.
     */
	public void keyReleased(KeyEvent e) {
		switch(id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(0);
			}
			break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(0);
			}
			break;
		}
	}

	/**
     * Sets the y-direction velocity of the paddle.
     * @param yDirection The y-direction velocity of the paddle.
     */
	public void setYDirection(int yDirection) {
		yV = yDirection;
	}

	/**
     * Moves the paddle by updating its y-coordinate based on the velocity.
     */
	public void move() {
		y = y + yV;
	}

	/**
     * Draws the paddle on the graphics context.
     * The color of the paddle is based on its identifier.
     * @param g The graphics context on which to draw the paddle.
     */
	public void draw(Graphics g) {
		if(id==1)
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		g.fillRect(x, y, width, height);
	}
}