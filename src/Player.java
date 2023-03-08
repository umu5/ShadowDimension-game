import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Following class is a modified version of a Player class originally written by
 * "Tharun Dharmawickrema" (SWEN20003 head tutor)
 * This a concrete class that can be used to render a player in game
 */

public class Player {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String FAE_RIGHT_A = "res/fae/faeAttackRight.png";
    private final static String FAE_LEFT_A = "res/fae/faeAttackLeft.png";
    private final static int DAMAGE_POINTS = 20;
    private final static double REFRESH_RATE = 60;
    private final static double TOT_MS = 1000;
    private final static int MAX_HEALTH_POINTS = 100;
    private final static double MOVE_SIZE = 2;
    private final static double ATTACK_TIME = 1000;
    private final static double COOL_DOWN = 2000;
    private final static double INVINCIBLE_TIME = 3000;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int FONT_SIZE = 30;
    private final Health health = new Health(MAX_HEALTH_POINTS, FONT_SIZE);

    private boolean isInvincible;
    private boolean isIdle;
    private boolean canAttack;
    private Point position;
    private Point prevPosition;
    private Image currentImage;
    private boolean facingRight;
    private double counter;
    private double invinsCounter;

    public Player(int startX, int startY){
        this.position = new Point(startX, startY);
        this.currentImage = new Image(FAE_RIGHT);
        this.facingRight = true;
        this.isIdle = true;
        this.canAttack = true;
        this.isInvincible = false;
    }

    /**
     * Method that performs state update and set players current image according to users input
     */
    public void update(Input input, ShadowDimension gameObject){
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        }
        else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        }
        else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight && isIdle) {
                this.currentImage = new Image(FAE_LEFT);
                facingRight = !facingRight;
            } else if (facingRight) {
                this.currentImage = new Image(FAE_LEFT_A);
                facingRight = !facingRight;

            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight && isIdle) {
                this.currentImage = new Image(FAE_RIGHT);
                facingRight = !facingRight;
            } else if (!facingRight) {
                this.currentImage = new Image(FAE_RIGHT_A);
                facingRight = !facingRight;
            }
        } else if (input.wasPressed(Keys.A) && canAttack) {
            isIdle = false;
            counter = 0;
            if (facingRight) {
                this.currentImage = new Image(FAE_RIGHT_A);
            } else {
                this.currentImage = new Image(FAE_LEFT_A);
                facingRight = false;
            }

        }

        this.currentImage.drawFromTopLeft(position.x, position.y);
        // check player collision with stationary entities
        gameObject.playerCollisions(this);
        renderHealthPoints();
        // check if player is within game bounds
        if (gameObject.checkOutOfBounds(this.position)) {
            moveBack();
        }
    }
    /**
     * This method keep tracks of how many times screen is refreshed. Gets called from ShadowDimension Class.
     * This helps to implement player's invincible and `can attack` logic
     */
    public void updateCalled() {
        counter++;
        invinsCounter++;
        double time = counter/(REFRESH_RATE/TOT_MS);

        // if player is in attacking position but now attack time has passed
        if (!isIdle && time >= ATTACK_TIME) {
            isIdle = true;
            canAttack = false;
            // set counter to 0, so can be used to determine cooldown time
            counter = 0;
            // need to update players images in idle state
            if (facingRight) {
                this.currentImage = new Image(FAE_RIGHT);
            } else {
                this.currentImage = new Image(FAE_LEFT);
            }
        }
        // if player is in idle state and cooldown time has passed player must be allowed to attack again
        if (isIdle && time >= COOL_DOWN) {
            canAttack = true;
            counter = 0;
        }
        // when player is in invincible state but invincible time has passed
        if (isInvincible && invinsCounter/(REFRESH_RATE/TOT_MS) >= INVINCIBLE_TIME) {
            isInvincible = false;
            invinsCounter = 0;
        }
    }

    /**
     * Method that stores player's previous position
     */
    private void setPrevPosition(){
        this.prevPosition = new Point(position.x, position.y);
    }

    /**
     * Method that moves player back to previous position
     */
    public void moveBack(){
        this.position = prevPosition;
    }

    /**
     * Method that moves player given the direction
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        health.renderHealth(HEALTH_X, HEALTH_Y);
    }

    /**
     * Method that checks if player's health has depleted
     */
    public boolean isDead() {
        return health.getCurrHealth() <= 0;
    }

    /**
     * Method that checks if player has found the gate
     */
    public boolean reachedGate(){
        return (this.position.x >= WIN_X) && (this.position.y >= WIN_Y);
    }

    /**
     * Method that updates players health to a given new health and also turns player to invincible state if
     * health got depleted
     */
    public void setHealthPoints(int healthPoints) {
        int prevHealth = getHealthPoints();
        if (!isInvincible) {
            health.changeHealth(healthPoints);
        }
        if(prevHealth > getHealthPoints()) {
           isInvincible = true;
           invinsCounter = 0;
        }

    }

    /**
     * returns rectangle data type around player based on players position and current image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     * updates the player health given the damage points that player had incurred
     */
    public void getDamage(int damagePoints) {
        setHealthPoints(getHealthPoints() - damagePoints);
    }


    /* some getters */

    public Point getPosition() { return position; }

    public int getHealthPoints() { return health.getCurrHealth(); }

    public static int getMaxHealthPoints() { return MAX_HEALTH_POINTS; }

    public boolean isIdle() { return isIdle; }
    public int getDamagePoints() { return DAMAGE_POINTS; }

    public int getMaxHealth() { return MAX_HEALTH_POINTS; }
}