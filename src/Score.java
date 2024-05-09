import java.awt.*;

/**
 * The Score class represents the scoring mechanism for the game.
 * It extends the Rectangle class and provides methods to draw the scores on the screen.
 */
public class Score extends Rectangle{

	static int GAME_WIDTH;
	static int GAME_HEIGHT;
	int player1;
	int player2;
	
	/**
     * Constructs a Score object with the specified game width and height.
     * @param gameWidth The width of the game window.
     * @param gameHeight The height of the game window.
     */
	Score(int GAME_WIDTH, int GAME_HEIGHT){
		Score.GAME_WIDTH = GAME_WIDTH;
		Score.GAME_HEIGHT = GAME_HEIGHT;
	}

	/**
     * Draws the scores on the screen using the specified graphics context.
     * @param g The Graphics object used for drawing.
     */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.PLAIN,60));
		
		g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
		
		g.drawString(String.valueOf(player1/10)+String.valueOf(player1%10), (GAME_WIDTH/2)-85, 50);
		g.drawString(String.valueOf(player2/10)+String.valueOf(player2%10), (GAME_WIDTH/2)+20, 50);
	}
}