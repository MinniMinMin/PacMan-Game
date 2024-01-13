import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * cherry class
 */
public class Cherry extends Objective {
    private static final String name = "Cherry";
    private final Image CHERRY_ICON = new Image("res/cherry.png");
    private static ArrayList<Integer> cherryLocations = new ArrayList<>();
    private static ArrayList<Rectangle> cherryRectangles = new ArrayList<>();
    private static final int VALUE = 20;

    /**
     * cherry constructor
     */
    public Cherry() {

    }

    /**
     * draws cherries at given points
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void drawIcon(int x, int y) {
        drawIcon(CHERRY_ICON, x, y);
    }

    /**
     * adds coordinates to arraylist
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void addToList(int x, int y) {
        addToList(cherryLocations, x, y);
        Point cherryPoint = new Point(x, y);
        addHitBox(cherryPoint);
    }

    /**
     * redraws cherries based off arraylist
     */
    protected void redraw() {
        redraw(cherryLocations, CHERRY_ICON);
    }

    /**
     * adds hit boxes for cherries
     * @param point used to make rectangles to add into arraylist
     */
    protected void addHitBox(Point point) {
        addHitBox(CHERRY_ICON, point, cherryRectangles);
    }

    /**
     * increases the score when colliding with player
     * @param playerBox player rectangle
     */
    protected static void increasePoint (Rectangle playerBox) {
        // increase the score when player intersects a dot hit box
        increasePoint(name, playerBox, cherryRectangles, VALUE);
    }

    /**
     * remove cherry from both arraylists
     * @param cherryBox the cherry to remove
     */
    protected static void removeDotBox (Rectangle cherryBox) {
        // when the player intersects with a dot, remove the dot
        removeDotBox(cherryBox, cherryLocations, cherryRectangles);
    }

}
