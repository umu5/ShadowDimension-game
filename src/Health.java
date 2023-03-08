import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

/**
 * The implementation of this class is inspired from the implementation used in project 1 solution to render players
 * health written by "Tharun Dharmawickrema" (SWEN20003 head tutor)
 * Health class is used by entities such as enemies and player. This class can be used as an instance variable
 * in other classes
 */
public class Health {
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static String FONT_TYPE ="res/frostbite.ttf";
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final Font font;
    private final int MAX_HEALTH;
    private int currHealth;

    public Health(int maxHealth, int fontSize) {
        this.MAX_HEALTH = maxHealth;
        this.font = new Font(FONT_TYPE, fontSize);
        this.currHealth = MAX_HEALTH;
    }

    /**
     * updates current health to a given newHealth
     */
    public void changeHealth(int newHealth) {
        this.currHealth = Math.max(newHealth, 0);
    }
    /**
     * This is a modified method copied from Player class originally written by
     * "Tharun Dharmawickrema" (SWEN20003 head tutor)
     * Method renders the health at a specified position in a specific colour determined by this method
     */
    public void renderHealth(int healthX, int healthY) {
        double percentageHP = ((double) currHealth/MAX_HEALTH) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        } else {
            COLOUR.setBlendColour(GREEN);
        }
        font.drawString(Math.round(percentageHP) + "%", healthX, healthY, COLOUR);
    }

    // some getters
    public int getCurrHealth() {
        return currHealth;
    }
}
