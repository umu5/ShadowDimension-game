import bagel.Image;
import bagel.util.Point;

/**
 * Following class is a modified version of a Sinkhole class originally written by
 * "Tharun Dharmawickrema" (SWEN20003 head tutor)
 * This class inherits from Stationary class and can be used to render SinkHoles type object in game
 */

public class Sinkhole extends Stationary {
    private final Image SINKHOLE = new Image("res/sinkhole.png");
    private final static int DAMAGE_POINTS = 30;
    private boolean isActive;

    public Sinkhole(int startX, int startY){
        super(new Point(startX, startY));
        setCurrImage(SINKHOLE);
        this.isActive = true;
    }

    @Override
    public void update() {
        if (isActive){
            SINKHOLE.drawFromTopLeft(POSITION.x, POSITION.y);
        }
    }

    public int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}