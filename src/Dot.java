import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * dot class
 */
public class Dot extends Objective {
    private static final String name = "Dot";
    private final Image DOT_ICON = new Image("res/dot.png");
    private static ArrayList<Integer> dotLocations = new ArrayList<>();
    private static ArrayList<Rectangle> dotRectangles = new ArrayList<>();
    private static final int VALUE = 10;

    /**
     * dot constructor
     */
    public Dot() {
    }

    /**
     * draws dots at given points
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void drawIcon(int x, int y) {
        // draws dots at given points
        drawIcon(DOT_ICON, x, y);
    }

    /**
     * adds coordinates to arraylist
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void addToList(int x, int y) {
        // adds the x,y coordinates into an ArrayList for redrawing and collision
        addToList(dotLocations, x, y);
        Point dotPoint = new Point(x, y);
        addHitBox(dotPoint);
    }

    /**
     * redraws dots based off arraylist
     */
    protected void redraw() {
        // redraws icons from ArrayList
        redraw(dotLocations, DOT_ICON);
    }

    /**
     * adds hit boxes for dots
     * @param point used to make rectangles to add into arraylist
     */
    protected void addHitBox(Point point) {
        // adds hit boxes for dots
        addHitBox(DOT_ICON, point, dotRectangles);
    }

    /**
     * restarts arraylists
     */
    protected void restartLists() {
        dotLocations = new ArrayList<>();
        dotRectangles = new ArrayList<>();
    }

    /**
     * increases the score when colliding with player
     * @param playerBox player rectangle
     */
    protected static void increasePoint(Rectangle playerBox) {
        // increase the score when player intersects a dot hit box
        increasePoint(name, playerBox, dotRectangles, VALUE);
    }

    /**
     * remove dot from both arraylists
     * @param dotBox the dot to remove
     */
    protected static void removeDotBox(Rectangle dotBox) {
        // when the player intersects with a dot, remove the dot
        removeDotBox(dotBox, dotLocations, dotRectangles);
    }
}
