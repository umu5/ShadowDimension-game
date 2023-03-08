import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * This is an abstract class that contains common features that all enemies can possess
 */
public abstract class  Enemy {
    private final static double SPEED_H = 0.7;
    private final static double SPEED_L = 0.2;
    private final static double INVINCIBLE_TIME = 3000;
    protected final static int HEALTH_FONT = 15;
    protected final static int HEALTH_OFFSET_Y = 6;
    private final static int RANDOM_BOUND = 3;
    protected final static int RANDOM_CHECK = 0;
    private final static double REFRESH_RATE = 60;
    private final static double TOT_MS = 1000;

    private static double origSpeed;
    private static double currSpeed;
    private Fire fire;
    private int damagePoints;
    private double attackRange;
    private boolean isNavec = false;
    private boolean isActive;
    private boolean isAggressive;
    private boolean isInvincible = false;
    private boolean moveInX;
    private boolean isMoveRight;
    private boolean isMoveUp;
    private Point position;
    private Health health;
    private Point prevPosition;
    private Image currentImage;
    private double invinsCounter;


    public Enemy(Point position) {
        this.position = position;
        this.isActive = true;
        setDirections();
    }

    /**
     * Method uses java Random class to generate Random number. Uses this randomness to decide in
     * which direction should enemy move
     */
    private void setDirections() {
        moveInX = (getRandomNum() == RANDOM_CHECK);
        if (!moveInX) {
            isMoveUp = (getRandomNum() == RANDOM_CHECK);
        } else {
            isMoveRight = getRandomNum() == RANDOM_CHECK;
        }
    }

    /**
     * performs the state update for enemy
     */
    public void update(ShadowDimension gameObject) {
        if (isActive) {
            if (isNavec || isAggressive) {
                move(gameObject);
            } else {
                drawImage();
            }
            // check whether the enemy intersects with the player
            gameObject.checkPlayerEnemyIntersects(this);
        }
    }

    /**
     * Method used to render fire when player is within enemy's attacking range ,hence, player
     * can potentially receive damage. Also, checks whether enemy can get damage from player
     */
    public void playerIntersects(Player player) {
        // get enemy and player's centre points of their current image
        Point playerCentre = player.getBoundingBox().centre();
        Point enemyCentre = this.getBoundingBox().centre();

        // if player is within enemy attacking range then render fire
        if (enemyCentre.distanceTo(playerCentre) <= attackRange) {
            fire.renderFire(this.getBoundingBox(), enemyCentre, playerCentre);
            // if fire intersects with player then player can potentially take damage
            if (fire.getBoundingBox().intersects(player.getBoundingBox())) {
                int playerPrevHealth = player.getHealthPoints();
                player.getDamage(damagePoints);
                // print the player got damage log if player actually received a damage
                if (playerPrevHealth > player.getHealthPoints()) {
                    damageToPlayerLog(playerPrevHealth, player.getHealthPoints(), player.getMaxHealth());
                }
            }
        }
        // if player was in attacking state and intersected with enemy then enemy can potentially take damage
        if (!player.isIdle() && player.getBoundingBox().intersects(this.getBoundingBox())) {
            getDamage(player.getDamagePoints());
        }
    }


    /**
     * This method keeps track of time and determines when an enemy should be converted back to normal state from
     * an invincible state, if previously was in invincible state. Gets called from ShadowDimension class
     */
    public void updateCalled() {
        invinsCounter++;
        double time = invinsCounter/(REFRESH_RATE/TOT_MS);

        if (isInvincible && time >= INVINCIBLE_TIME) {
            isInvincible = false;
            invinsCounter = 0;
        }
    }

    /**
     * updates enemy's health given a new health
     */
    private void getDamage(int damagePoints) {
        setHealthPoints(getHealthPoints() - damagePoints);

    }

    /**
     * Method updates enemy's health if not in invincible state. Also, if enemy health reduced then enemy should
     * be changed to invincible state. Moreover, enemy's active status need to be updated if enemy's health reached
     * 0
     */
    private void setHealthPoints(int healthPoints) {
        int prevHealth = getHealthPoints();
        if (!isInvincible) {
            health.changeHealth(healthPoints);
            printLog(prevHealth);
        }
        if(prevHealth > getHealthPoints()) {
            isInvincible = true;
            invinsCounter = 0;
        }
        if (getHealthPoints() <= 0) {
            isActive = false;
        }
    }

    /**
     * A static method gets called to randomly set enemy class speed within its predefined bounds
     */
    public static void setOrigSpeed() {
        Random random = new Random();
        double val = random.nextDouble();
        if (val > SPEED_H) {
            val -= (1.0-SPEED_H);
        }
        if (val < SPEED_L) {
            val += SPEED_L;
        }
        Enemy.origSpeed = val;
        Enemy.currSpeed = Enemy.origSpeed;
    }

    /**
     * this method returns a random num within a bound. Used while updating some random features such as direction
     */
    public int getRandomNum() {
        Random random = new Random();
        return random.nextInt(RANDOM_BOUND);
    }

    /**
     * This method set enemies position to a new position according to its movement logic (either moving in Y or
     * X coordinate). Also changes enemies direction if it gets out of the bounds or collides with stationary object.
     */
    private void move(ShadowDimension gameObject) {
        setPrevPosition(position);
        Point newPosition = position;

        // checks if enemy currently moving in x so update its position in x coordinate
        if (moveInX) {
            // should move right
            newPosition = new Point(position.x+currSpeed, position.y);
            // already moving left, so, should move to left
            if (!isMoveRight) {
                newPosition = new Point(position.x-currSpeed, position.y);
            }
        }
        // Enemy is moving in Y-coordinate
        if (!moveInX) {
            // should move up;
            newPosition = new Point(position.x, position.y-currSpeed);
            // already moving down so should move down
            if (!isMoveUp) {
                newPosition = new Point(position.x, position.y+currSpeed);
            }
        }
        // check whether the new position is within the bounds and if yes then update enemy's position
        if (!gameObject.checkOutOfBounds(newPosition)) {
            this.position = newPosition;

        // new position was out of bounds so enemy should move in opposite direction
        } else {
            changeDirection();
        }
        // convert enemy's position back to previous position if collides with a stationary object, also,
        // change enemy's direction
        if (gameObject.enemyCollisions(this.getBoundingBox())) {
            position = prevPosition;
            changeDirection();

        }
        // now draw the enemy's updated image
        drawImage();
    }

    /**
     * Method changes enemy's direction to an opposite direction
     */
    public void changeDirection() {
        if (moveInX) {
            isMoveRight = !isMoveRight;
        } else {
            isMoveUp = !isMoveUp;
        }
    }

    /**
     * returns rectangle data type around enemy based on its position and current image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     * method that checks if enemy's health has depleted
     */
    public boolean isDead() { return health.getCurrHealth() <= 0; }

    /**
     * methods draw the enemy according to its current state and position. Also, it renders enemy's health
     */
    public abstract void drawImage();

    /**
     * Method prints the log if enemy received any damage
     */
    public abstract void printLog(int prevHealth);

    /**
     * Method that prints the log about players health. prevHealth, newHealth and maxHealth are all player's
     * attributes
     */
    public abstract void damageToPlayerLog(int prevHealth, int newHealth, int maxHealth);


    /* Followings methods are some getters and setters */

    public void setNavec(boolean navec) { isNavec = navec; }

    public void setCurrentImage(Image currentImage) { this.currentImage = currentImage; }

    public void setAggressive(boolean aggressive) { isAggressive = aggressive; }

    public void setInvincible(boolean invincible) { isInvincible = invincible; }

    public void setHealth(Health health) { this.health = health; }

    public void setFire(Fire fire) { this.fire = fire; }

    public void setAttackRange(double range) { this.attackRange = range; }

    public void setDamagePoints(int points) { this.damagePoints = points; }

    public void setPrevPosition(Point position) { this.prevPosition = position; }

    public static void setCurrSpeed(double currSpeed) { Enemy.currSpeed = currSpeed; }

    public int getHealthPoints() { return health.getCurrHealth(); }


    public Point getPosition() { return position; }

    public boolean isMoveInX() { return moveInX; }

    public boolean isMoveRight() { return isMoveRight; }

    public Image getCurrentImage() { return currentImage; }

    public boolean getIsMoveRight() { return isMoveRight;}

    public boolean getIsMoveUp() { return isMoveUp; }

    public boolean isInvincible() { return isInvincible; }

    public Health getHealth() { return health; }

    public static double getOrigSpeed() { return origSpeed; }

}