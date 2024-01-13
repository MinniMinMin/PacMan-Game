import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * objective parent class
 */
public abstract class Objective {
    // basic parent class for objectives (dots, pellets, cherries)
    private static int score = 0;
    private static int maxScore;
    private static final int SCORE_FONT_SIZE = 20;
    private static final int SCORE_LOCATION = 25;

    /**
     * draws icons
     * @param image image that is to be drawn
     * @param x x coordinate of image
     * @param y y coordinate of image
     */
    protected void drawIcon(Image image, int x, int y) {
        image.drawFromTopLeft(x, y);
    }
    protected void addToList(ArrayList<Integer> locations, int x, int y) {
        locations.add(x);
        locations.add(y);
    }

    /**
     * redraws objectives from arraylist
     * @param locations arraylist of coordinates
     * @param image image to be redrawn
     */
    protected void redraw(ArrayList<Integer> locations, Image image) {
        for (int i = 0; i < locations.size(); i+= 2) {
            if (locations.get(i) == null) continue;
            drawIcon(image, locations.get(i), locations.get(i+1));
        }
    }

    /**
     * adds rectangles to an arraylist
     * @param image image of objective
     * @param point point used to be converted into rectangle
     * @param rectangles arraylist of rectangles
     */
    protected void addHitBox(Image image, Point point, ArrayList<Rectangle> rectangles) {
        Rectangle dotBox = new Rectangle(point, image.getWidth(), image.getHeight());
        rectangles.add(dotBox);
    }

    /**
     * remove objectives from arraylists
     * @param curObjective the objective to be removed from arraylists
     * @param locations arraylist of locations
     * @param rectangles arraylist of rectangles
     */
    protected static void removeDotBox (Rectangle curObjective, ArrayList<Integer> locations,
                                        ArrayList<Rectangle> rectangles) {
        // when the player intersects with a dot, remove the dot
        int index = 0;

        for (int i = 0; i < rectangles.size(); i++) {
            if (curObjective.equals(rectangles.get(i))) {
                // checks for correct dot hit hox, and removes it
                rectangles.set(i, null);
                index = 2 * i;
            }
        }
        // removes dots from list for drawing
        locations.set(index, null);
        locations.set(index + 1, null);
    }

    /**
     * decides how to increase the score after collision with player
     * @param type type of objective
     * @param playerBox rectangle of the player
     * @param rectangles arraylist of rectangles
     * @param value the value of the objective
     */
    protected static void increasePoint (String type, Rectangle playerBox, ArrayList<Rectangle> rectangles, int value) {
        // increase the score when player intersects a dot hit box
        for (Rectangle curObjective : rectangles) {
            if (curObjective == null) continue;
            if (curObjective.intersects(playerBox)) {
                // remove dot hit box and increase score
                if (type.equals("Dot")) Dot.removeDotBox(curObjective);
                if (type.equals("Cherry")) Cherry.removeDotBox(curObjective);
                if (type.equals("Pellet")) Pellet.removeDotBox(curObjective);
                addPoints(value);
            }
        }
    }

    /**
     * increases the score
     * @param value how much to increase score by
     */
    protected static void addPoints(int value) {
        score += value;
    }

    /**
     * gets the current score
     * @return current score
     */
    protected static int getScore() {
        return score;
    }

    /**
     * increases the maxScore
     * @param value how much to increase the maxScore by
     */
    protected static void increaseMaxScore(int value) {
        maxScore += value;
    }

    /**
     * changes the maxScore when on a new level
     */
    protected static void newMaxScore() {
        maxScore = 800;
    }

    /**
     * resets the score back to 0
     */
    protected static void resetScore () {
        score = 0;
    }

    /**
     * resets maxScore back to 0
     */
    protected static void resetMaxScore () {
        maxScore = 0;
    }

    /**
     * returns the current maxScore
     * @return the current maxScore
     */
    protected static int getMaxScore () {
        return maxScore;
    }

    protected static void drawScore() {
        // draws the score
        Font scoreFont = new Font("res/FSO8BITR.ttf", SCORE_FONT_SIZE);
        scoreFont.drawString("SCORE " + getScore(), SCORE_LOCATION, SCORE_LOCATION);
    }

}

