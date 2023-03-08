import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * This is an abstract class that implements common features among stationary kind of objects
 */
public abstract class Stationary {
    protected final Point POSITION;
    private Image currImage;

    public Stationary(Point position) {

        this.POSITION = position;
    }

    /**
     * Method that performs state update
     */
    public abstract void update();

    /**
     * return a rectangle data type around the current position and image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(POSITION, currImage.getWidth(), currImage.getHeight());
    }

    // some getters
    public void setCurrImage(Image currImage) { this.currImage = currImage; }
}
