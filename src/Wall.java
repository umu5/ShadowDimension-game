import bagel.Image;
import bagel.util.Point;

/**
 * Following class is a modified version of a Wall class originally written by
 * "Tharun Dharmawickrema" (SWEN20003 head tutor)
 * This class inherits from Stationary class and can be used to render Walls type object in game
 */

public class Wall extends Stationary {
    private final Image WALL = new Image("res/wall.png");

    public Wall(int startX, int startY){
        super(new Point(startX, startY));
        setCurrImage(WALL);
    }

    @Override
    public void update() { WALL.drawFromTopLeft(POSITION.x, POSITION.y); }
}