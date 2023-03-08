import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Data type of this class is used by enemies to render their fire images
 */
public class Fire {
    private final static double TOP_LEFT = 0;
    private final static double TOP_RIGHT = (Math.PI)/2;
    private final static double BOTTOM_LEFT = (3*Math.PI)/2;
    private final static double BOTTOM_RIGHT = Math.PI;
    private final Image FIRE_IMAGE;
    private final DrawOptions ROTATION = new DrawOptions();
    private Point position;

    public Fire(Image fireImage) {
        FIRE_IMAGE = fireImage;
    }

    /**
     * Method does the specific calculation using enemies and players centre points to render fire images
     * at a particular location with a particular rotation
     */
    public void renderFire(Rectangle enemyRectangle, Point enemyCentre, Point playerCentre) {
        double xP = playerCentre.x;
        double yP = playerCentre.y;
        double xE = enemyCentre.x;
        double yE = enemyCentre.y;
        Point pos;

        // need to render fire at from top left enemy's image
        if (xP <= xE && yP <= yE) {
            ROTATION.setRotation(TOP_LEFT);
            pos = enemyRectangle.topLeft();
           drawFire(pos.x - FIRE_IMAGE.getWidth(), pos.y - FIRE_IMAGE.getHeight());
        // need to render fire from bottom left of enemy's image
        } else if (xP <= xE && yP > yE) {
            ROTATION.setRotation(BOTTOM_LEFT);
            pos = enemyRectangle.bottomLeft();
            drawFire(pos.x - FIRE_IMAGE.getWidth(), pos.y);
        // need to render fire from top right of enemy's image
        } else if (xP > xE && yP <= yE) {
            ROTATION.setRotation(TOP_RIGHT);
            pos = enemyRectangle.topRight();
            drawFire(pos.x, pos.y - FIRE_IMAGE.getHeight());
        // need to render fire from bottom right of enemy's image
        } else if (xP > xE && yP > yE) {
            ROTATION.setRotation(BOTTOM_RIGHT);
            pos = enemyRectangle.bottomRight();
            drawFire(pos.x, pos.y);
        }
    }

    /**
     * Method draws the fire image at given coordinates
     */
    private void drawFire(double x, double y) {
        this.position = new Point(x, y);
        FIRE_IMAGE.drawFromTopLeft(x, y, ROTATION);
    }

    /**
     * returns a rectangle data type around current fire image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, FIRE_IMAGE.getWidth(), FIRE_IMAGE.getHeight());
    }
}
