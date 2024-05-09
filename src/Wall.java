import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The Wall class represents a wall object in the game.
 * It extends the Rectangle class and provides methods to draw the wall and manage its state.
 */
public class Wall extends Rectangle {
    
    private int id;
    private int hits; // Number of hits the wall has received

    /**
     * Constructs a Wall object with the specified position, width, height, and identifier.
     * @param x The x-coordinate of the top-left corner of the wall.
     * @param y The y-coordinate of the top-left corner of the wall.
     * @param paddleWidth The width of the wall.
     * @param wallHeight The height of the wall.
     * @param id The identifier for the wall (1 or 2).
     */
    Wall(int x, int y, int PADDLE_WIDTH, int WALL_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, WALL_HEIGHT);
        this.id = id;
        hits = 0; // Initialize hits to 0
    }

    /**
     * Draws the wall on the screen using the specified graphics context.
     * The color of the wall is determined by its identifier.
     * @param g The Graphics object used for drawing.
     */
    public void draw(Graphics g) {
        // Set color based on the wall's id
        if (id == 1)
            g.setColor(Color.blue);
        else
            g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }


    /**
     * Increments the number of hits received by the wall.
     */
    public void hit() {
        hits++;
    }

    /**
     * Checks if the wall is broken based on the number of hits it has received.
     * @return true if the wall is broken (hits >= 3), false otherwise.
     */
    public boolean isBroken() {
        return hits >= 3;
    }
}