import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * pellet class
 */
public class Pellet extends Objective {
    private static final String name = "Pellet";
    private final Image PELLET_ICON = new Image("res/pellet.png");
    private static ArrayList<Integer> pelletLocations = new ArrayList<>();
    private static ArrayList<Rectangle> pelletRectangles = new ArrayList<>();
    private static final int VALUE = 0;

    /**
     * pellet constructor
     */
    public Pellet() {
    }

    /**
     * draws pellets at given points
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void drawIcon(int x, int y) {
        PELLET_ICON.drawFromTopLeft(x, y);
    }

    /**
     * adds coordinates to arraylist
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void addToList(int x, int y) {
        addToList(pelletLocations, x, y);
        Point pelletPoint = new Point(x, y);
        addHitBox(pelletPoint);
    }

    /**
     * redraws pellets based off arraylist
     */
    protected void redraw() {
        redraw(pelletLocations, PELLET_ICON);
    }

    /**
     * adds hit boxes for pellets
     * @param point used to make rectangles to add into arraylist
     */
    protected void addHitBox(Point point) {
        addHitBox(PELLET_ICON, point, pelletRectangles);
    }

    /**
     * increases the score when colliding with player
     * @param playerBox player rectangle
     */
    protected static void increasePoint (Rectangle playerBox) {
        // increase the score when player intersects a pellet hit box
        increasePoint(name, playerBox, pelletRectangles, VALUE);
    }

    /**
     * remove pellet from both arraylists
     * and activate frenzy mode
     * @param pelletBox the cherry to remove
     */
    protected static void removeDotBox (Rectangle pelletBox) {
        // when the player intersects with a dot, remove the dot
        // and activate frenzy mode
        Enemy.changeFrenzyMode();
        removeDotBox(pelletBox, pelletLocations, pelletRectangles);
    }

}
