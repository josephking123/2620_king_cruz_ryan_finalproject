import java.awt.*;
import java.util.*;

/**
 * The Ball class represents the ball object in the Pong game.
 * It extends the Rectangle class to inherit position and size properties.
 */
public class Ball extends Rectangle{

	Random random;
	int xV;
	int yV;
	int initialSpeed = 2;
	
	/**
     * Constructs a Ball object with the specified position and size.
     * Initializes the random object and sets random initial directions.
     * @param x The x-coordinate of the ball's top-left corner.
     * @param y The y-coordinate of the ball's top-left corner.
     * @param width The width of the ball.
     * @param height The height of the ball.
     */
	Ball(int x, int y, int width, int height){
		super(x,y,width,height);
		random = new Random();
		int randomXDirection = random.nextInt(2);
		if(randomXDirection == 0)
			randomXDirection--;
		setXDirection(randomXDirection*initialSpeed);
		
		int randomYDirection = random.nextInt(2);
		if(randomYDirection == 0)
			randomYDirection--;
		setYDirection(randomYDirection*initialSpeed);
		
	}
	
	/**
     * Sets the x-direction velocity of the ball.
     * @param randomXDirection The x-direction velocity of the ball.
     */
	public void setXDirection(int randomXDirection) {
		xV = randomXDirection;
	}

	/**
     * Sets the y-direction velocity of the ball.
     * @param randomYDirection The y-direction velocity of the ball.
     */
	public void setYDirection(int randomYDirection) {
		yV = randomYDirection;
	}

	/**
     * Moves the ball by updating its position based on the velocity.
     */
	public void move() {
		x += xV;
		y += yV;
	}

	/**
     * Draws the ball on the graphics context.
     * @param g The graphics context on which to draw the ball.
     */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}
}