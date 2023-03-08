import bagel.Image;
import bagel.util.Point;

/**
 * Demon is a concrete class that inherits behaviour from parent class `Enemy`
 * This class can be used to render a Demon type object in Game
 */
public class Demon extends Enemy{
    private final static String DEMON_R = "res/demon/demonRight.png";
    private final static String DEMON_L = "res/demon/demonLeft.png";
    private final static String DEMON_R_I = "res/demon/demonInvincibleRight.PNG";
    private final static String DEMON_L_I = "res/demon/demonInvincibleLeft.PNG";
    private final Image FIRE_IMAGE = new Image("res/demon/demonFire.png");
    private final static int DAMAGE_POINTS = 10;
    private final static double ATTACK_RANGE = 150;
    private final static int MAX_HEALTH = 40;


    public Demon(Point position) {
        super(position);
        setFire(new Fire(FIRE_IMAGE));
        setDamagePoints(DAMAGE_POINTS);
        setHealth(new Health(MAX_HEALTH, HEALTH_FONT));
        setAttackRange(ATTACK_RANGE);
        setInvincible(false);
        setNavec(false);
        setAggressive(getRandomNum() == RANDOM_CHECK);
        // default image
        setCurrentImage(new Image(DEMON_R));
        if (isMoveInX() && !isMoveRight()) {
            setCurrentImage(new Image(DEMON_L));
        }
    }

    @Override
    public void printLog(int prevHealth) {
        int damageCaused = prevHealth - getHealthPoints();
        System.out.println("Fae inflicts "+ damageCaused+ " damage points on Demon."
                            +" Demon’s current health: "+ getHealthPoints() + "/" + MAX_HEALTH);
    }

    @Override
    public void drawImage() {
        Point position = getPosition();
        setCurrentImage(new Image(DEMON_R));
        if (isInvincible())
            setCurrentImage(new Image(DEMON_R_I));
        if (!getIsMoveUp()) {
            if (isMoveInX() && !getIsMoveRight()) {
                setCurrentImage(new Image(DEMON_L));
                if (isInvincible())
                    setCurrentImage(new Image(DEMON_L_I));
            }
        }
        getCurrentImage().drawFromTopLeft(position.x, position.y);
        getHealth().renderHealth((int)position.x, (int)position.y-HEALTH_OFFSET_Y);
    }

    @Override
    public void damageToPlayerLog(int prevHealth, int newHealth, int maxHealth) {
        System.out.println("Demon inflicts "+ DAMAGE_POINTS + " damage points on Fae. "
                            + "Fae’s current health: " + newHealth + "/" + maxHealth);

    }
}
