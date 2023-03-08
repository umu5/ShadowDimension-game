import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 * @author Muhammad Usman (student Id: 1229086)
 */

/**
 * Following class is a modified version of a ShadowDimension class originally written by
 * "Tharun Dharmawickrema" (SWEN20003 head tutor)
 * This class runs the whole game and deals with transition between the game levels.
 */
public class ShadowDimension extends AbstractGame {
    // constants used for this class
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static double REFRESH_RATE = 60;
    private final static double TOT_MILLI_S = 1000;
    private final static double LEVEL0_WIN_DURATION = 3000;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String FILE0 = "res/level0.csv";
    private final static String FILE1 = "res/level1.csv";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE1 = new Image("res/background1.png");
    private final static double DEC_SPEED = 0.5;
    private final static double INC_SPEED = 1.5;
    private final static int TIME_SCALE_L = -3;
    private final static int TIME_SCALE_H = 3;
    private final static int TITLE_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private final static int INS_X_LEVEL1 = 350;
    private final static int INS_Y_LEVEL1 = 350;
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String INSTRUCTION_MESSAGE1 = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static String END_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "LEVEL COMPLETE!";
    private final static String GAME_WIN = "CONGRATULATIONS!";
    private final static List<Stationary> stationaries = new ArrayList<>();
    private final static  List<Demon> demons = new ArrayList<>();

    // instance variables used in this class
    private Point topLeft;
    private Point bottomRight;
    private Player player;
    private Navec navec;
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;
    private boolean isLevel1;
    private double level0EndCounter;
    private int timeScale;


    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        isLevel1 = false;
        readCSV(FILE0);
        hasStarted = false;
        gameOver = false;
        playerWin = false;
    }

    /**
     * This a modified version of method originally written by "Tharun Dharmawickrema" (SWEN20003 head tutor)
     * Method used to read file and create objects
     */
    private void readCSV(String file){
        // clear() methods are used for proper functioning of game when 'W' key is pressed during the game
        demons.clear();
        stationaries.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){

            String line;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Fae":
                        player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Wall":
                        stationaries.add(new Wall(Integer.parseInt(sections[1]),Integer.parseInt(sections[2])));
                        break;
                    case "Sinkhole":
                        stationaries.add(new Sinkhole(Integer.parseInt(sections[1]),Integer.parseInt(sections[2])));
                        break;
                    case "Tree":
                        stationaries.add(new Tree(Integer.parseInt(sections[1]),Integer.parseInt(sections[2])));
                    case "TopLeft":
                        topLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Navec":
                        Point pos1 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        navec = new Navec(pos1, true, false);
                        break;
                    case "Demon":
                        Point pos2 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        demons.add(new Demon(pos2));
                        break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update by calling different methods that are particular to current game level
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        if (input.wasPressed(Keys.W)) {
            isLevel1 = true;
            hasStarted = false;
            gameOver = false;
            playerWin = false;
            timeScale = 0;
            readCSV(FILE1);
        }
        if (!isLevel1) {
            gameLevel0(input);
        }
        player.updateCalled();
        if (isLevel1) {
            gameLevel1(input);
        }

    }

    /**
     * This method gets called from update method. This is used to run game specified by level 0
     * The implementation for gameLevel0 is copied from update method (project1) that was originally
     * written by "Tharun Dharmawickrema" (SWEN20003 head tutor)
     */
    private void gameLevel0(Input input) {
        if(!hasStarted){
            drawStartScreen(false);
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
            }
        }
        if (gameOver){
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
            // used to display level complete message for 3 seconds
            level0EndCounter++;
            if (level0EndCounter/(REFRESH_RATE/TOT_MILLI_S) >= LEVEL0_WIN_DURATION) {
                isLevel1 = true;
                // reset the instance variable to false so can get used in level1
                hasStarted = false;
                gameOver = false;
                playerWin = false;
                // read the level1 file now
                readCSV(FILE1);
            }
        }
        // leve0 game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            for (Stationary current: stationaries) {
                current.update();
            }
            player.update(input, this);

            if (player.isDead()){
                gameOver = true;
            }
            if (player.reachedGate()){
                playerWin = true;
            }
        }
    }

    /**
     * This method gets called from update method. This is used to run the game specified by level1
     */
    private void gameLevel1(Input input) {
        if(!hasStarted){
            drawStartScreen(true);
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
                // randomly set the static speed of Enemy class
                Enemy.setOrigSpeed();
            }
        }
        if (gameOver) {
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(GAME_WIN);
        }

        // level1 game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            // change enemy speed if possible
            if (input.wasPressed(Keys.K)) {
                changeSpeed(DEC_SPEED);
            }
            if (input.wasPressed(Keys.L)) {
                changeSpeed(INC_SPEED);
            }
            for (Stationary current: stationaries) {
                current.update();
            }
            enemiesUpdate();
            player.update(input, this);

            if (player.isDead()){
                gameOver = true;
            }
            if (navec.isDead()) {
                playerWin = true;
            }
        }
    }

    /**
     * Method that checks for collisions between player and the stationary entities, and performs
     * corresponding actions.
     */
    public void playerCollisions(Player player){
        Rectangle faeBox = player.getBoundingBox();

        for (Stationary current: stationaries) {
            Rectangle stationaryBox = current.getBoundingBox();
            if (faeBox.intersects(stationaryBox)) {
                if (!(current instanceof Sinkhole)) {
                    player.moveBack();
                }
                // stationary is an instance of SinkHole class, so need to do some further actions
                else {
                    Sinkhole hole = (Sinkhole) current;
                    if (hole.isActive()) {
                        int prevHealth = player.getHealthPoints();
                        player.setHealthPoints(Math.max(player.getHealthPoints() - hole.getDamagePoints(), 0));
                        player.moveBack();
                        hole.setActive(false);
                        // need to print the log if player health got reduced
                        if (prevHealth > player.getHealthPoints()) {
                            System.out.println("Sinkhole inflicts " + hole.getDamagePoints()
                                    + " damage points on Fae." + " Fae's current health: " + player.getHealthPoints()
                                    + "/" + Player.getMaxHealthPoints());
                        }
                    }
                }
            }
        }
    }

    /**
     * Method that checks if given position has gone out-of-bounds then returns true, otherwise false
     */
    public boolean checkOutOfBounds(Point position){
        if ((position.y > bottomRight.y) || (position.y < topLeft.y) || (position.x < topLeft.x)
                || (position.x > bottomRight.x)){
            return true;
        }
        return false;
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    private void drawStartScreen(boolean isLevel1){
        if (!isLevel1) {
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE, TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
        } else {
            INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE1, INS_X_LEVEL1, INS_Y_LEVEL1);
        }
    }

    /**
     * Method used to draw end screen messages
     */
    private void drawMessage(String message){
        TITLE_FONT.drawString(message, (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(message)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }

    /**
     * This method returns True if given rectangle intersects with stationary objects, otherwise, false
     */
    public boolean enemyCollisions(Rectangle enemyBox) {
        for (Stationary current: stationaries) {
            Rectangle stationaryBox = current.getBoundingBox();
            if (enemyBox.intersects(stationaryBox)) {
                // collides with a sinkHole, should return false if a sinkHole was not active
                if ((current instanceof Sinkhole) && !((Sinkhole) current).isActive()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check if enemy intersects with a player and if it does then it calls a method from enemy class to do further
     * action on enemy and player
     */
    public void checkPlayerEnemyIntersects(Enemy enemy) {
        Rectangle enemyBox = enemy.getBoundingBox();
        Rectangle faeBox = player.getBoundingBox();
        if (enemyBox.intersects(faeBox)) {
            enemy.playerIntersects(player);
        }
    }

    /**
     * Method gets called from update method used to do state update for  enemies
     */
    private void enemiesUpdate() {
        navec.updateCalled();
        navec.update(this);
        for (Demon d: demons) {
            d.updateCalled();
            d.update(this);
        }
    }

    /**
     * This method used to change static variable of Enemy class `currSpeed` according to the timeScale variable
     * and a given factor
     */
    private void changeSpeed(double factor) {
        int prevTimeScale = timeScale;
        double newFac = factor;

        // speed must be increased
        if (factor  ==  INC_SPEED) {
            timeScale += 1;
            // This ensures we are staying within the limits of timeScale variable so speed can be changed
            if (timeScale >= TIME_SCALE_L && timeScale <= TIME_SCALE_H) {
                System.out.println("Sped up, Speed: " + timeScale);
                // timeScale is currently a negative int. So need to adjust value of newFac which can then be applied
                // on Enemy `origSpeed` to get new `currSpeed`
                if (timeScale <= 0) {
                    newFac -= 1;
                }
                Enemy.setCurrSpeed(Enemy.getOrigSpeed() * Math.pow(newFac, Math.abs(timeScale)));
            } else {
                timeScale = prevTimeScale;
            }
        } else {
            timeScale -= 1;
            // This ensures we are staying within the limits of timeScale variable so speed can be changed
            if (timeScale >= TIME_SCALE_L && timeScale <= TIME_SCALE_H) {
                System.out.println("Slowed down, Speed: " + timeScale);
                // timeScale is currently a positive int. So need to adjust value of newFac
                if (timeScale >= 0) {
                    newFac += 1;
                }
                Enemy.setCurrSpeed(Enemy.getOrigSpeed() * Math.pow(newFac, Math.abs(timeScale)));
            } else {
                timeScale = prevTimeScale;
            }
        }

    }
}