import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * blue ghost
 */
public class BlueGhost extends Enemy {
    private final Image GHOST_ICON = new Image("res/ghostBlue.png");
    private final static int MOVEMENT = 2;
    private static double curX;
    private static double curY;
    private static int startingX;
    private static int startingY;
    private static double direction;
    private final double ICON_WIDTH = GHOST_ICON.getWidth();
    private final double ICON_HEIGHT = GHOST_ICON.getHeight();
    private static boolean isAlive = true;

    /**
     * constructor for blue ghost
     */
    public BlueGhost() {
    }

    /**
     * alternates between movement speeds depending on frenzyMode
     * @return the current movement speed
     */
    @Override
    protected double changeMovement() {
        // if in frenzy mode, return the frenzy speed
        if (getFrenzyMode()) return getFrenzyMovement();
        // else return the normal speed
        return MOVEMENT;
    }

    /**
     * stores the starting positions for resetting
     * @param x starting x coordinate
     * @param y starting y coordinate
     */
    protected void setStartPos(int x, int y) {
        // sets the starting position
        curX = x;
        curY = y;
        startingX = x;
        startingY = y;
    }

    /**
     * resets ghost back to its starting position
     */
    @Override
    protected void resetGhost() {
        if (isAlive) {
            // if ghost is alive, reset to its starting location
            curX = startingX;
            curY = startingY;
        } else {
            // if dead, set current ghost position to (0,0),
            // so it can't interact with the player
            curX = ORIGIN_X;
            curY = ORIGIN_Y;
        }
    }

    /**
     * draws blue ghost as given points
     * @param x x coordinate
     * @param y y coordinate
     */
    public void drawIcon(double x, double y) {
        // draws ghosts at given points
        // and switches to frenzy ghost image in frenzy mode
        if (getFrenzyMode()) drawIcon(getFRENZY_GHOST(), x, y);
        else drawIcon(GHOST_ICON, x, y);
    }

    @Override
    protected void addHitBox(Point point) {

    }

    /**
     * if it's a valid move, allows blue ghost to keep moving
     */
    @Override
    protected void movementPath() {
        // path checker for ghost

        if (isAlive) {
            if (validMove(direction, changeMovement(), curX, curY, ICON_WIDTH, ICON_HEIGHT)) traverse();
            drawIcon(curX, curY);
        } else if (!getFrenzyMode()) {
            changeState();
        }
    }

    /**
     * changes the isAlive boolean
     * for when the ghost is eaten during frenzy mode
     */
    @Override
    protected void changeState() {
        isAlive = !isAlive;
        resetGhost();
    }

    /**
     * changes the direction of blue ghost
     * @param newDirection new direction of blue ghost
     */
    @Override
    protected void changeDirection(double newDirection) {
        // sets the current direction of ghost
        direction = newDirection;
    }

    /**
     * checks collision against the player
     * @param player player rectangle
     */
    protected void collision(Rectangle player) {
        // checks if player has collided with a ghost
        collision(player, GHOST_ICON, curX, curY);
    }

    /**
     * chooses random direction again when current direction isn't valid
     */
    protected void checkDirection() {
        if (direction == LEFT) chooseRandomDirection();
        if (direction == RIGHT) chooseRandomDirection();
    }

    /**
     * switches direction when blue ghost collides against the wall
     * @param curDirection direction of blue ghost
     */
    @Override
    protected void switchDirections(double curDirection) {
        // bounces the ghost
        if (curDirection == DOWN) changeDirection(UP);
        else if (curDirection == UP) changeDirection(DOWN);
    }

    /**
     * how blue moves through the level
     */
    @Override
    protected void traverse() {
        // movement of the ghost
        checkDirection();
        if (direction == DOWN) curY += changeMovement();
        if (direction == UP) curY -= changeMovement();
    }

}
