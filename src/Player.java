import bagel.DrawOptions;
import bagel.Image;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * player class
 */
public class Player {
    private static final Image PLAYER_ICON = new Image("res/pac.png");
    private static final Image PLAYER_OPEN_ICON = new Image("res/pacOpen.png");
    private static final Image HEART_ICON = new Image("res/heart.png");
    private final double UP = Math.toRadians(270);
    private final double RIGHT = Math.toRadians(0);
    private final double LEFT = Math.toRadians(180);
    private final double DOWN = Math.toRadians(90);
    private final int MOVEMENT = 3;
    private final double FRENZY_SPEED = 4;
    private static int curX;
    private static int curY;
    private static int startingX;
    private static int startingY;
    private static double direction;
    private static int framesCounter = 0;
    private static boolean isOpen = false;
    private static int curLives = 3;
    private static final DrawOptions rotation = new DrawOptions();

    /**
     * player constructor
     */
    public Player () {
    }

    /**
     * gets the current x coordinate
     * @return current x coordinate
     */
    public static int getCurX () {
        return curX;
    }

    /**
     * sets current x coordinate
     * @param curX  new x coordinate
     */
    public static void setCurX (int curX) {
        Player.curX = curX;
    }

    /**
     * gets the current y coordinate
     * @return current y coordinate
     */
    public static int getCurY () {
        return curY;
    }

    /**
     * sets current y coordinate
     * @param curY new y coordinate
     */
    public static void setCurY (int curY) {
        Player.curY = curY;
    }

    /**
     * increases framesCounter
     */
    public static void incrementFramesCounter () {
        framesCounter ++;
    }

    /**
     * sets the starting coordinate for resetting player
     * @param x starting x coordinate
     * @param y starting y coordinate
     */
    public static void setStartingPos (int x, int y) {
        startingX = x;
        startingY = y;
    }

    /**
     * gets the starting x coordinate
     * @return starting x coordinate
     */
    public static int getStartingX() {
        return startingX;
    }

    /**
     * gets the starting y coordinate
     * @return starting y coordinate
     */
    public static int getStartingY() {
        return startingY;
    }

    /**
     * gets current amount of lives
     * @return current amount of lives
     */
    public static int getCurLives() {
        return curLives;
    }

    /**
     * decrements current lives
     */
    public static void lostLives() {
        curLives --;
    }

    /**
     * draws the player with a closed mouth in its current direction
     * @param x current x coordinate
     * @param y current y coordinate
     * @param rotations the direction the player is facing
     */
    public static void drawPlayer(int x, int y, double rotations) {
        // draws the player at given point with a point and facing direction
        PLAYER_ICON.drawFromTopLeft(x, y, rotation.setRotation(rotations));
    }

    /**
     * draws the player with an open mouse in its current direction
     * @param x current x coordinate
     * @param y current y coordinate
     * @param rotations the direction the player is facing
     */
    public static void drawOpenPlayer(int x, int y, double rotations) {
        // draws the open player icon with a point and facing direction
        PLAYER_OPEN_ICON.drawFromTopLeft(x, y, rotation.setRotation(rotations));
    }

    /**
     * draws current lives
     */
    public static void drawLives () {
        // draws the lives on top right
        int x = 900;
        for (int i = 0; i < curLives; i++) {
            HEART_ICON.drawFromTopLeft(x, 10);
            x += 30;
        }
    }

    /**
     * changes between mouth open and closed every 15 frames
     */
    protected static void changeIcon() {
        // changes between mouth closed and open player image

        // draws the mouth open player when stationary
        if (isOpen) drawOpenPlayer(curX, curY, getDirection());
        // draws the mouth closed player when stationary
        else drawPlayer(curX, curY, getDirection());

        if (isOpen && framesCounter == 15) {
            // changes to the mouth closed player at 15 frames
            drawPlayer(curX, curY, getDirection());
            framesCounter = 0;
            isOpen = false;
        } else if (!isOpen && framesCounter == 15){
            // changes to mouth open player at 15 frames
            drawOpenPlayer(curX, curY, getDirection());
            framesCounter = 0;
            isOpen = true;
        }
    }

    /**
     * gets the current direction
     * @return current direction
     */
    private static double getDirection() {
        // simply returns the current facing direction as a radian
        return direction;
    }

    /**
     * changes between movement speeds during and out of frenzy mode
     * @return normal or frenzy speed
     */
    private double changeMovement() {
        if (Enemy.getFrenzyMode()) return FRENZY_SPEED;
        return MOVEMENT;
    }

    /**
     * UP key is pressed
     */
    public void moveUp() {
        // move up if valid move
        if (validMove(Keys.UP)) curY -= changeMovement();
        direction = UP;
        changeIcon();
    }

    /**
     * DOWN key is pressed
     */
    public void moveDown() {
        // move down if valid move
        if (validMove(Keys.DOWN)) curY += changeMovement();
        direction = DOWN;
        changeIcon();
    }

    /**
     * LEFT key is pressed
     */
    public void moveLeft() {
        // move left if valid move
        if (validMove(Keys.LEFT)) curX -= changeMovement();
        direction = LEFT;
        changeIcon();
    }

    /**
     * RIGHT key was pressed
     */
    public void moveRight() {
        // move right if valid move
        if (validMove(Keys.RIGHT)) curX += changeMovement();
        direction = RIGHT;
        changeIcon();
    }

    /**
     * checks collision with ghosts when stationary
     */
    protected static void stationaryCollision() {
        // checks for collision when stationary
        RedGhost redGhost = new RedGhost();
        BlueGhost blueGhost = new BlueGhost();
        PinkGhost pinkGhost = new PinkGhost();
        GreenGhost greenGhost = new GreenGhost();

        Point playerPoint = new Point(curX, curY);
        Rectangle playerHitBox = new Rectangle(playerPoint, PLAYER_ICON.getWidth(), PLAYER_ICON.getHeight());

        redGhost.collision(playerHitBox);
        blueGhost.collision(playerHitBox);
        pinkGhost.collision(playerHitBox);
        greenGhost.collision(playerHitBox);
    }

    /**
     * checks if the move is valid
     * @param key the direction the player wants to travel
     * @return if the move is valid
     */
    private boolean validMove(Keys key) {
        // checks for collision with walls and dots before player is actually moved
        int futureX = curX, futureY = curY;
        RedGhost redGhost = new RedGhost();

        // setting up checking by getting future moves from paremeter
        if (key == Keys.UP) futureY -= changeMovement();
        if (key == Keys.DOWN) futureY += changeMovement();
        if (key == Keys.LEFT) futureX -= changeMovement();
        if (key == Keys.RIGHT) futureX += changeMovement();

        Point playerPoint = new Point(futureX, futureY);
        Rectangle playerHitBox = new Rectangle(playerPoint, PLAYER_ICON.getWidth(), PLAYER_ICON.getHeight());

        // checks collision with dots, and increases points
        Dot.increasePoint(playerHitBox);
        redGhost.collision(playerHitBox);
        Pellet.increasePoint(playerHitBox);
        Cherry.increasePoint(playerHitBox);

        for (int i = 0; i < Wall.getWallRectanglesSize(); i++) {
            // checks for intersections with any wall rectangle in ArrayList
            Rectangle curRectangle = Wall.getWallRectangle(i);
            // returns false if colliding
            if (playerHitBox.intersects(curRectangle)) return false;
        }

        // otherwise returns true
        return true;
    }

}
