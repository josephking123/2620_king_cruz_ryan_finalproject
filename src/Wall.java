import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall extends Rectangle{
    
    int id;

    Wall(int x, int y, int PADDLE_WIDTH, int WALL_HEIGHT, int id){
		super(x,y,PADDLE_WIDTH,WALL_HEIGHT);
        this.id=id;
	}


    public void draw(Graphics g) {
		if(id==1)
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		g.fillRect(x, y, width, height);
	}
    
}
