import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The pink ghost class
 */
public class PinkGhost extends Enemy {
    private final Image GHOST_ICON = new Image("res/ghostPink.png");
    private static final double MOVEMENT = 3;
    private static double curX;
    private static double curY;
    private static int startingX;
    private static int startingY;
    private static double direction;
    private final double ICON_WIDTH = GHOST_ICON.getWidth();
    private final double ICON_HEIGHT = GHOST_ICON.getHeight();
    private static boolean isAlive = true;

    /**
     * constructor for pink ghost
     */
    public PinkGhost() {
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
     * draws pink ghost as given points
     * @param x x coordinate
     * @param y y coordinate
     */
    public void drawIcon(double x, double y) {
        // draws ghosts at given points
        // and switches to frenzy ghost image in frenzy mode
        if (getFrenzyMode()) drawIcon(getFRENZY_GHOST(), x, y);
        else drawIcon(GHOST_ICON, x, y);
    }

    /**
     * changes the direction of pink ghost
     * @param newDirection new direction of pink ghost
     */
    @Override
    protected void changeDirection(double newDirection) {
        // sets current direction of ghost
        direction = newDirection;
    }

    // needs to be removed
    @Override
    protected void addHitBox(Point point) {

    }

    /**
     * if it's a valid move, allows pink ghost to keep moving
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
     * checks collision against the player
     * @param player player rectangle
     */
    protected void collision(Rectangle player) {
        // checks if player has collided with a ghost
        collision(player, GHOST_ICON, curX, curY);
    }

    /**
     * switches direction when pink ghost collides against the wall
     * @param curDirection direction of pink ghost
     */
    @Override
    protected void switchDirections(double curDirection) {
        // bounces the ghost
        if (curDirection == RIGHT) chooseRandomDirection();
        else if (curDirection == LEFT) chooseRandomDirection();
        else if (curDirection == DOWN) chooseRandomDirection();
        else if (curDirection == UP) chooseRandomDirection();
    }

    /**
     * how pink ghost moves through the level
     */
    @Override
    protected void traverse() {
        // movement for ghost
        if (direction == RIGHT) curX += changeMovement();
        if (direction == LEFT) curX -= changeMovement();
        if (direction == DOWN) curY += changeMovement();
        if (direction == UP) curY -= changeMovement();
    }

}
