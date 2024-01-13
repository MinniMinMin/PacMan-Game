import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * wall class
 */
public class Wall {
    // basic walls
    private final Image WALL_ICON = new Image("res/wall.png");
    private static ArrayList<Integer> wallLocations = new ArrayList<>();
    private static ArrayList<Rectangle> wallRectangles = new ArrayList<>();

    /**
     * wall constructor
     */
    protected Wall () {
    }

    /**
     * draws walls at given points
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void drawIcon(int x, int y) {
        // draws wall as a rectangle for collision at given point
        // ands adds to collision checking list
        WALL_ICON.drawFromTopLeft(x, y);
    }

    /**
     * adds coordinates to arraylist
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void addToList(int x, int y) {
        // adds the x,y coordinates into an array list for redrawing and collision
        wallLocations.add(x);
        wallLocations.add(y);
        Point wallPoint = new Point(x, y);
        addHitBox(wallPoint);
    }

    /**
     * redraws walls based off arraylist
     */
    protected void redraw() {
        // redraws icons from ArrayList
        for (int i = 0; i < wallLocations.size(); i += 2) {
            drawIcon(wallLocations.get(i), wallLocations.get(i + 1));
        }
    }

    /**
     * adds hit boxes for walls
     * @param wallPoint used to make rectangles to add into arraylist
     */
    protected void addHitBox(Point wallPoint) {
        // adds hit boxes for walls
        Rectangle wallBox = new Rectangle(wallPoint, WALL_ICON.getWidth(), WALL_ICON.getHeight());
        wallRectangles.add(wallBox);
    }

    /**
     * restarts arraylists
     */
    protected void restartLists() {
        // restart the array-lists for new levels
        wallLocations = new ArrayList<>();
        wallRectangles = new ArrayList<>();
    }

    /**
     * gets the size of arraylist
     * @return size of arraylist
     */
    protected static int getWallRectanglesSize() {
        return wallRectangles.size();
    }

    /**
     * gets the rectangle at specified index
     * @param index the index of rectangle wanted
     * @return returns a rectangle
     */
    protected static Rectangle getWallRectangle(int index) {
        return wallRectangles.get(index);
    }

}

