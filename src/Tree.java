import bagel.Image;
import bagel.util.Point;

/**
 * This class inherits from Stationary class and can be used to render Trees type object in game
 */
public class Tree extends Stationary {
    private final Image TREE = new Image("res/tree.png");

    public Tree(int startX, int startY){
        super(new Point(startX, startY));
        setCurrImage(TREE);
    }

    @Override
    public void update() { TREE.drawFromTopLeft(POSITION.x, POSITION.y);}
}
