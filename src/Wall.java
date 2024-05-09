import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall extends Rectangle {
    
    private int id;
    private int hits; // Number of hits the wall has received

    Wall(int x, int y, int PADDLE_WIDTH, int WALL_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, WALL_HEIGHT);
        this.id = id;
        hits = 0; // Initialize hits to 0
    }

    public void draw(Graphics g) {
        // Set color based on the wall's id
        if (id == 1)
            g.setColor(Color.blue);
        else
            g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }

    // Method to increment the number of hits
    public void hit() {
        hits++;
    }

    // Method to check if the wall is broken
    public boolean isBroken() {
        return hits >= 3;
    }
}