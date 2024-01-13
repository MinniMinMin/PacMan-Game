import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * the enemy parent class
 */
public abstract class Enemy {
    // basic enemy parent class
    protected final double UP = Math.toRadians(270);
    protected final double RIGHT = Math.toRadians(0);
    protected final double LEFT = Math.toRadians(180);
    protected final double DOWN = Math.toRadians(90);
    private static boolean frenzyMode = false;
    private final int VALUE = 30;
    private static final double FRENZY_MOVEMENT = 0.5;
    private static int counter = 0;
    private static final int MAX_FRENZY_TIME = 1000;
    private final Image FRENZY_GHOST = new Image("res/ghostFrenzy.png");
    public final int ORIGIN_X = 0;
    public final int ORIGIN_Y = 0;


    /**
     * used to draw enemies
     * @param image the icon passed used for drawing
     * @param x the x coordinate for drawing
     * @param y the y coordinate for drawing
     */
    protected void drawIcon(Image image, double x, double y) {
        // draws the enemy at given points
        image.drawFromTopLeft(x, y);
    }

    /**
     * adds coordinates to an arraylist
     * @param locations arraylist of integers
     * @param x the x coordinate of element
     * @param y the y coordinate of element
     */
    protected void addToList(ArrayList<Double> locations, double x, double y) {
        // adds the x, y coordinates into an array list for redrawing and collision
        locations.add(x);
        locations.add(y);
        Point enemyPoint = new Point(x, y);
        addHitBox(enemyPoint);
    }

    /**
     * redraws enemies from location arraylist and given image
     * @param locations arraylist of integers
     * @param image image to be drawn
     */
    public void redraw(ArrayList<Double> locations, Image image) {
        // redraws enemies from given list of locations
        for (int i = 0; i < locations.size(); i += 2) {
            drawIcon(image, locations.get(i), locations.get(i + 1));
        }
    }

    /**
     * getter for FRENZY_MOVEMENT
     * @return FRENZY_MOVEMENT
     */
    protected static double getFrenzyMovement() {
        return FRENZY_MOVEMENT;
    }

    /**
     * returns count of how many frames frenzy mode has been active
     * @return returns the current count
     */
    protected static int getCounter() {
        return counter;
    }

    /**
     * increments count of how long frenzy mode has been active
     */
    protected static void incrementCounter() {
        counter ++;
    }

    /**
     * checks collision of enemies against walls
     * @param direction the current direction of the enemy
     * @param movement amount of pixels moved
     * @param curX current x coordinate
     * @param curY current y coordinate
     * @param width the width of the enemy
     * @param height the height of the enemy
     * @return if it's valid to keep moving in the direction
     */
    protected boolean validMove(double direction, double movement, double curX, double curY,
                                     double width, double height) {
        // collision checker for enemies, bounces enemies when colliding with a wall

        double futureX = curX, futureY = curY;

        // makes future moves
        if (direction == RIGHT) futureX += movement;
        if (direction == LEFT) futureX -= movement;
        if (direction == UP) futureY -= movement;
        if (direction == DOWN) futureY += movement;

        Point ghost = new Point(futureX, futureY);
        Rectangle ghostHitBox = new Rectangle(ghost, width, height);

        for (int i = 0; i < Wall.getWallRectanglesSize(); i++) {
            // checks if moving in the given direction will collide with the walls
            // switch directions if collision
            if (ghostHitBox.intersects(Wall.getWallRectangle(i))) {
                switchDirections(direction);
                return false;
            }
        }
        return true;
    }

    /**
     * chooses a random direction via Random()
     */
    protected void chooseRandomDirection() {
        // chooses a random direction for ghost by getting random number 0-3
        Random rand = new Random();
        int upperBound = 4;
        int int_random = rand.nextInt(upperBound);

        if (int_random == 0) changeDirection(UP);
        if (int_random == 1) changeDirection(RIGHT);
        if (int_random == 2) changeDirection(LEFT);
        if (int_random == 3) changeDirection(DOWN);
    }

    /**
     * checks collision against the player
     * @param playerHitBox  rectangle of the player
     * @param ghostHitBox rectangle of enemy
     * @return if there was any collision
     */
    protected boolean playerCollision(Rectangle playerHitBox, Rectangle ghostHitBox) {
        // collision with lvl 1 ghosts
        return ghostHitBox.intersects(playerHitBox);
    }

    /**
     * abstract method that adds points to add to an arraylist of rectangles for collision
     * @param point point to be turned into rectangle and added to an arraylist
     */
    protected abstract void addHitBox(Point point);

    /**
     * abstract method for enemies to move
     */
    protected abstract void movementPath();

    /**
     * takes a life away from player when collision occurs
     */
    protected void loseLife() {
        // sets the player back to starting position
        // and removes a life
        Player.setCurX(Player.getStartingX());
        Player.setCurY(Player.getStartingY());
        Player.changeIcon();
        Player.lostLives();
    }

    /**
     * takes away life and resets ghost and player if they collide
     * @param player player rectangle to check for collision
     * @param image the image used for making rectangle of enemies
     * @param curX the current x coordinate of the ghost
     * @param curY the current y coordinate of the ghost
     */
    protected void collision(Rectangle player, Image image, double curX, double curY) {
        // checks if player has collided with a ghost
        Point ghostPoint = new Point(curX, curY);
        Rectangle ghostBox = new Rectangle(ghostPoint, image.getWidth(), image.getHeight());

        if (playerCollision(player, ghostBox)) {
            // checks collision between the player and ghost
            if (!getFrenzyMode()) loseLife();
            if (getFrenzyMode()) {
                // while is frenzy mode, ghosts are "dead" and their value is added to points
                changeState();
                Objective.addPoints(VALUE);
            }
            // reset the ghost after every collision
            resetGhost();

        }
    }
    protected abstract void changeState();

    /**
     * changes frenzyMode boolean value
     */
    protected static void changeFrenzyMode() {
        frenzyMode = !frenzyMode;
    }

    /**
     * check the current state of the frenzyMode boolean value
     * @return frenzyMode boolean value
     */
    protected static boolean getFrenzyMode() {
        return frenzyMode;
    }

    /**
     * abstract method for when enemies are in frenzy mode
     * @return the current movement speed of the enemy
     */
    protected abstract double changeMovement();

    /**
     * resets enemies back to their starting position
     */
    protected abstract void resetGhost();

    /**
     * abstract method decides what happens when enemies collide with a wall
     * @param curDirection direction of the enemy
     */
    protected abstract void switchDirections(double curDirection);

    /**
     * abstract method of how enemies move through the level
     */
    protected abstract void traverse();

    /**
     * abstract method that changes enemy direction
     * @param newDirection new direction of enemy
     */
    protected abstract void changeDirection(double newDirection);

    /**
     * gets the frenzied ghost image
     * @return frenzy ghost image
     */
    protected Image getFRENZY_GHOST() {
        // gets the frenzied ghost image
        return FRENZY_GHOST;
    }

    /**
     * gets the max frenzy time
     * @return max frenzy time
     */
    protected static int getMAX_FRENZY_TIME() {
        return MAX_FRENZY_TIME;
    }

    /**
     * controls the frenzy mode, changing back to normal after 1000 frames
     */
    protected static void frenzyModeTimer() {
        if (getFrenzyMode() && getCounter() == getMAX_FRENZY_TIME()) changeFrenzyMode();
        else if (getFrenzyMode()) incrementCounter();
    }

}
