import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * enemy that appears in level 0 and 1
 */
public class RedGhost extends Enemy {
    private final Image GHOST_ICON = new Image("res/ghostRed.png");
    private static ArrayList<Double> ghostLocations = new ArrayList<>();
    private static ArrayList<Rectangle> ghostRectangles = new ArrayList<>();
    private final static int MOVEMENT = 1;
    private static double curX;
    private static double curY;
    private static int startingX;
    private static int startingY;
    private static double direction;
    private final double ICON_WIDTH = GHOST_ICON.getWidth();
    private final double ICON_HEIGHT = GHOST_ICON.getHeight();
    private static boolean isAlive = true;

    /**
     * constructor for red ghost
     */
    protected RedGhost() {
    }

    /**
     * stores the starting positions for resetting
     * @param x starting x coordinate
     * @param y starting y coordinate
     */
     protected void setStartPos(int x, int y) {
        // sets the current position for moving
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
     * draws red ghost as given points
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
     * adds x and y coordinates to arraylist of integers
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void addToList (int x, int y) {
        // adds the x, y coordinates into an array list for redrawing and collision lvl 0
        addToList(ghostLocations, x, y);
    }

    /**
     * redraws red ghosts for level 0 based on locations arraylist
     */
    protected void redraw() {
        // redraws icons from ArrayList lvl 0
        redraw(ghostLocations, GHOST_ICON);
    }

    /**
     * adds rectangles to arraylist of rectangles
     * @param ghostPoint point to be turned into rectangle and added to an arraylist
     */
    @Override
    protected void addHitBox(Point ghostPoint) {
        // adds hit boxes for ghosts lvl 0
        Rectangle ghostBox = new Rectangle(ghostPoint, GHOST_ICON.getWidth(), GHOST_ICON.getHeight());
        ghostRectangles.add(ghostBox);
    }

    /**
     * makes a new arraylists for locations and rectangles
     */
    protected void restartLists() {
        // restarts lists for next levels
        ghostLocations = new ArrayList<>();
        ghostRectangles = new ArrayList<>();
    }

    /**
     * changes the direction of red ghost
     * @param newDirection new direction of red ghost
     */
    @Override
    protected void changeDirection(double newDirection) {
        // sets current direction of ghost
        direction = newDirection;
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
     * if it's a valid move, allows red ghost to keep moving
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
     * checks collision against player and ghosts in arraylist
     * @param player player rectangle
     */
    protected void collision(Rectangle player) {
        // checks if player has collided with a ghost
        collision(player, GHOST_ICON, curX, curY);

        for (Rectangle curGhost : ghostRectangles) {
            if (curGhost.intersects(player)) {
                // if collision, reset player location
                    loseLife();
            }
        }
    }

    /**
     * switches direction when red ghost collides against the wall
     * @param curDirection direction of red ghost
     */
    @Override
    protected void switchDirections(double curDirection) {
        // bounces the ghost
        if (curDirection == RIGHT) changeDirection(LEFT);
        else if (curDirection == LEFT) changeDirection(RIGHT);
    }

    /**
     * how red ghost moves through the level
     */
    @Override
    protected void traverse() {
        // movement for the ghost
        if (direction == RIGHT) curX += changeMovement();
        if (direction == LEFT) curX -= changeMovement();
    }

}
