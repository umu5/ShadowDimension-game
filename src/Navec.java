import bagel.Image;
import bagel.util.Point;

/**
 * Navec is a concrete class that inherits from Enemy class
 * Navec class can be used to render Navec type object in Game
 */
public class Navec extends Enemy {
    private final static String NAVEC_L = "res/navec/navecLeft.png";
    private final static String NAVEC_R = "res/navec/navecRight.png";
    private final static String NAVEC_L_I = "res/navec/navecInvincibleLeft.PNG";
    private final static String NAVEC_R_I = "res/navec/navecInvincibleRight.PNG";
    private final Image FIRE_IMAGE = new Image("res/navec/navecFire.png");
    private final int DAMAGE_POINTS = 20;
    private final double ATTACK_RANGE = 200;
    private final int MAX_HEALTH = 80;

    public Navec(Point position, boolean moveInX, boolean isMoveRight) {
        super(position);
        setFire(new Fire(FIRE_IMAGE));
        setDamagePoints(DAMAGE_POINTS);
        setHealth(new Health(MAX_HEALTH, HEALTH_FONT));
        setAttackRange(ATTACK_RANGE);
        setInvincible(false);
        setNavec(true);
        // default image
        setCurrentImage(new Image(NAVEC_R));
        if (moveInX && !isMoveRight) {
            setCurrentImage(new Image(NAVEC_L));
        }
    }

    @Override
    public void printLog(int prevHealth) {
        int damageCaused = prevHealth - getHealthPoints();
        System.out.println("Fae inflicts "+ damageCaused+ " damage points on Navec."
                +" Navec’s current health: "+ getHealthPoints() + "/" + MAX_HEALTH);
    }

    @Override
    public void drawImage() {
        Point position = getPosition();
        setCurrentImage(new Image(NAVEC_R));
        if (isInvincible())
            setCurrentImage(new Image(NAVEC_R_I));
        if (!getIsMoveUp()) {
            if (isMoveInX() && !getIsMoveRight()) {
                setCurrentImage(new Image(NAVEC_L));
                if (isInvincible())
                    setCurrentImage(new Image(NAVEC_L_I));
            }
        }
        getCurrentImage().drawFromTopLeft(position.x, position.y);
        getHealth().renderHealth((int)position.x, (int)position.y-HEALTH_OFFSET_Y);
    }

    @Override
    public void damageToPlayerLog(int prevHealth, int newHealth, int maxHealth) {
        System.out.println("Navec inflicts "+ DAMAGE_POINTS + " damage points on Fae. "
                + "Fae’s current health: " + newHealth + "/" + maxHealth);

    }



}
