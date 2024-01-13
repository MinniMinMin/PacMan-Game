import bagel.*;

import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2023

 * Please enter your name below
 * // Min Thu Han
 */

public class ShadowPac extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW PAC";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private boolean isPressed = false;
    private boolean gameOver = false;
    private boolean inGame = false;
    private int curLevel = 0;
    private final int MAXLEVEL = 1;
    private final double RIGHT = Math.toRadians(0);
    private int framesCounter = 0;
    private final Player PLAYER = new Player();
    private final Wall WALL = new Wall();
    private final Dot DOT = new Dot();
    private final Cherry CHERRY = new Cherry();
    private final Pellet PELLET = new Pellet();
    private final RedGhost RED_GHOST = new RedGhost();
    private final BlueGhost BLUE_GHOST = new BlueGhost();
    private final PinkGhost PINK_GHOST = new PinkGhost();
    private final GreenGhost GREEN_GHOST = new GreenGhost();
    private final int BIG_FONT_SIZE = 64;
    private final int MEDIUM_FONT_SIZE = 40;
    private final int SMALL_FONT_SIZE = 24;
    private final int GAME_TITLE_X = 260;
    private final int GAME_TITLE_Y = 250;
    private final int SUBTITLE_X = 320;
    private final int SUBTITLE_Y = 340;
    private final int Y_INCREMENT = 50;
    private final int X_INCREMENT = 30;
    private final int SCREEN_X = 200;
    private final int SCREEN_Y = 350;



    public ShadowPac(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */

    private void readCSV(String fileName) {
        // reads the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;

            while ((text = br.readLine()) != null) {
                // while not null, splits each line by ","
                String[] objects = text.split(",");
                // draws the objects
                drawObject(objects[0], Integer.parseInt(objects[1]), Integer.parseInt(objects[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point for the program.
     */

    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */

    @Override
    protected void update(Input input) {
        // looks for inputs
        if (input.wasPressed(Keys.ESCAPE)){
            // closes the game when ESC is pressed
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (input.isUp(Keys.SPACE) && !isPressed) {
            // draws starting screen if space isn't pressed
            drawStart();
        } else if (input.wasPressed(Keys.SPACE) && !inGame) {
            // checks to see if space was pressed to start the actual game
            readCSV("res/level0.csv");
            isPressed = true;
            inGame = true;
        }

        if (isPressed) {
            // start playing game after space was pressed

            // draws all entities
            drawLevelEntities();

            changeLevels(input);

            // can only move one direction at a time
            if (!gameOver && inGame) {
                // lock controls when game is over
                Player.stationaryCollision();
                if (input.isDown(Keys.UP)) PLAYER.moveUp();
                else if (input.isDown(Keys.DOWN)) PLAYER.moveDown();
                else if (input.isDown(Keys.LEFT)) PLAYER.moveLeft();
                else if (input.isDown(Keys.RIGHT)) PLAYER.moveRight();
            }
        }

    }

    /**
     * draws the entities of the different levels
     */
    private void drawLevelEntities() {
        // draws base entities from level 0
        DOT.redraw();
        Objective.drawScore();
        WALL.redraw();
        RED_GHOST.redraw();
        Player.drawLives();
        Player.changeIcon();
        Player.incrementFramesCounter();

        if (curLevel == MAXLEVEL) {
            // draws entities to draw in level 1
            Objective.newMaxScore();
            CHERRY.redraw();
            PELLET.redraw();
            RED_GHOST.movementPath();
            BLUE_GHOST.movementPath();
            PINK_GHOST.movementPath();
            GREEN_GHOST.movementPath();

            // is used to keep track of frenzy mode and switch back to normal
            // after 1000 frames
            Enemy.frenzyModeTimer();
        }
    }

    /**
     * draws level completion screen and reads the next level
     * @param input player input
     */
    private void changeLevels(Input input) {
        if (Dot.getScore() >= Dot.getMaxScore() && curLevel < MAXLEVEL) {
            // to enter new level
            drawLevelCompletion();
            if (input.isDown(Keys.SPACE) && inGame) {
                // once pressed new level is drawn
                curLevel += 1;
                gameOver = false;
                readCSV("res/level1.csv");
            }
        }

        // checks win/loss conditions
        if (Dot.getScore() >= Dot.getMaxScore() && curLevel == MAXLEVEL) drawWinScreen();
        if (Player.getCurLives() == 0) drawLossScreen();
    }

    /**
     * draws the start screen when playing the game
     */
    private void drawStart() {
        // draws the start screen
        Font title = new Font("res/FSO8BITR.ttf", BIG_FONT_SIZE);
        Font subtitle = new Font("res/FSO8BITR.ttf", SMALL_FONT_SIZE);

        title.drawString(GAME_TITLE, GAME_TITLE_X, GAME_TITLE_Y);
        subtitle.drawString("PRESS SPACE TO START", SUBTITLE_X, SUBTITLE_Y);
        subtitle.drawString("USE ARROW KEYS TO MOVE", SUBTITLE_X, SUBTITLE_Y + Y_INCREMENT);
    }

    /**
     * draws the level complete screen after every level
     */
    private void drawLevelCompletion() {
        wipeScreen();
        final String TEXT = "LEVEL COMPLETE!";
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        Font font = new Font("res/FSO8BITR.ttf", BIG_FONT_SIZE);
        font.drawString(TEXT, (WINDOW_WIDTH - font.getWidth(TEXT))/2,
                (double) WINDOW_HEIGHT /2 - (double) BIG_FONT_SIZE /2);
        inGame = false;
        framesCounter ++;

        if (framesCounter >= 300) drawNextLevelScreen();
    }

    /**
     * draws the screen after the level complete screen
     * is drawn before new levels
     */
    private void drawNextLevelScreen() {
        // draw the screen between levels
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        Font font = new Font("res/FSO8BITR.ttf", MEDIUM_FONT_SIZE);
        font.drawString("PRESS SPACE TO START", SCREEN_X, SCREEN_Y);
        font.drawString("USE ARROW KEYS TO MOVE", SCREEN_X, SCREEN_Y + Y_INCREMENT);
        font.drawString("EAT THE PELLET TO ATTACK", SCREEN_X, SCREEN_Y + 2 * Y_INCREMENT);
        gameOver = true;
        inGame = true;
    }

    /**
     * clears arraylists in other classes that are used for
     * drawing and collisions
     */
    private void wipeScreen() {
        // wipe all data from the previous level
        Dot dot = new Dot();
        Wall wall = new Wall();
        RedGhost ghost = new RedGhost();
        dot.restartLists();
        wall.restartLists();
        ghost.restartLists();
        Dot.resetMaxScore();
        Dot.resetScore();
    }

    /**
     * draws the win screen
     */
    private void drawWinScreen() {
        // draws the screen when win condition is met
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        Font winScreen = new Font("res/FSO8BITR.ttf", BIG_FONT_SIZE);
        winScreen.drawString("WELL DONE!", SCREEN_X + X_INCREMENT, Window.getHeight()/2.0);
        gameOver = true;
    }

    /**
     * draws the loss screen
     */
    private void drawLossScreen() {
        // draws the screen when loss condition is met
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        Font winScreen = new Font("res/FSO8BITR.ttf", BIG_FONT_SIZE);
        winScreen.drawString("GAME OVER!", SCREEN_X + X_INCREMENT, Window.getHeight()/2.0);
        gameOver = true;
    }

    /**
     * draws icons from the csv, and adds elements to arraylists
     * @param name name of the element imported from csv
     * @param x the x coordinate of element
     * @param y the y coordinate of element
     */
    private void drawObject(String name, int x, int y) {
        // creates points for collision and draws objects onto the screen
        switch (name) {
            case "Player":
                // draws player at the start and during gameplay
                Player.setCurX(x);
                Player.setCurY(y);
                Player.drawPlayer(x, y, RIGHT);
                Player.setStartingPos(x, y);
                break;
            case "Wall":
                // draws all walls at the start and adds them to the ArrayList
                WALL.addToList(x, y);
                WALL.drawIcon(x, y);
                break;
            case "Dot":
                // draws all dots at the start and adds them to the ArrayList
                DOT.addToList(x, y);
                DOT.drawIcon(x, y);
                Dot.increaseMaxScore(10);
                break;
            case "Pellet":
                // draws all pellets at lvl 1 and adds them to the ArrayList
                PELLET.addToList(x, y);
                PELLET.drawIcon(x, y);
                break;
            case "Cherry":
                // draws all dots at lvl 1 and adds them to the ArrayList
                CHERRY.addToList(x, y);
                CHERRY.drawIcon(x, y);
                Dot.increaseMaxScore(20);
                break;
            case "Ghost":
                // draws all ghosts at the start and adds them to the ArrayList
                RED_GHOST.addToList(x, y);
                RED_GHOST.drawIcon(x, y);
                break;
            case "GhostRed":
                // draws the red ghost for lvl 1
                RED_GHOST.setStartPos(x, y);
                RED_GHOST.changeDirection(RIGHT);
                break;
            case "GhostPink":
                // draws the pink ghost for lvl 1
                PINK_GHOST.setStartPos(x, y);
                PINK_GHOST.changeDirection(RIGHT);
                break;
            case "GhostGreen":
                // draws the green ghost for lvl 1
                GREEN_GHOST.setStartPos(x, y);
                GREEN_GHOST.chooseRandomDirection();
                break;
            case "GhostBlue":
                // draws the blue ghost for lvl 1
                BLUE_GHOST.setStartPos(x, y);
                BLUE_GHOST.chooseRandomDirection();
                break;
        }
    }
}