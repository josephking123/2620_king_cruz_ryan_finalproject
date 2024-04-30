import java.awt.*;
import java.awt.event.KeyEvent;


public class Paddle extends Rectangle{

	int player;
	int yVelocity;
	int speed = 10;
	
	Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int player){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		this.player=player;
	}
	
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode()==KeyEvent.VK_W) {
			setYDirection(-speed);
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			setYDirection(speed);
		}	
	}
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_W) {
			setYDirection(0);
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			setYDirection(0);
		}
	}
	public void setYDirection(int yDirection) {
		y = yDirection;
	}
	public void move() {
		y = y + yVelocity;
	}
	public void draw(Graphics g) {
		if(player==1)
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		g.fillRect(x, y, width, height);
	}
}