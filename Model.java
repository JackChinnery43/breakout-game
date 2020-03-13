import java.util.ArrayList;
import javafx.scene.paint.*;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.media.AudioClip;

/**
*   The model represents all the actual content and functionality of the app
*   For Breakout, it manages all the game objects that the View needs
*   (the bat, ball, bricks, score, and the lives), provides methods to allow the Controller
*   to move the bat (and a couple of other functions - change the speed or stop 
*   the game), and runs a background process (a 'thread') that moves the ball 
*   every 20 milliseconds and checks for collisions 
*/

public class Model 
{
    // a collection of useful values for calculating sizes and layouts etc.

    public int B              = 6;      // Border round the edge of the panel
    public int M              = 40;     // Height of menu bar space at the top

    public int BALL_SIZE      = 10;     // Ball size
    public int BRICK_WIDTH    = 60;     // Brick size (width)
    public int BRICK_HEIGHT   = 30;     // Brick size (height)

    public int BAT_MOVE       = 10;     // Distance to move bat on each keypress
    public int BALL_MOVE      = 3;      // Units to move the ball on each step

    public int HIT_BRICK      = 50;     // Score for hitting a brick
    public int HIT_BOTTOM     = -1;     // Lose a life for hitting the bottom of the screen

    // The other parts of the model-view-controller setup
    View view;
    Controller controller;

    // The game 'model' - these represent the state of the game
    // and are used by the View to display it
    public GameObj ball;                // The ball
    public ArrayList<GameObj> bricks;   // The bricks
    public GameObj bat;                 // The bat
    public int score = 0;               // The score
    public int lives = 3;               // The lives
    public boolean gameOver = false;    // check if game over

    // variables that control the game 
    public boolean gameRunning = true;  // Set false to stop the game
    public boolean fast = false;        // Set true to make the ball go faster

    // initialisation parameters for the model
    public int width;                   // Width of game
    public int height;                  // Height of game

    /**
     * CONSTRUCTOR - needs to know how big the window will be
     */
    public Model( int w, int h )
    {
        Debug.trace("Model::<constructor>");  
        width = w; 
        height = h;

        initialiseGame();
    }

    /**
     * This method initialises the game - reset the score and create the game objects
     * I have created a key code 'ENTER' in the controller class
     * that is linked to this method and initialises the game when inputted
     */
    public void initialiseGame()
    {       
        score = 0;
        lives = 3;
        ball   = new GameObj(width/2, height/2, BALL_SIZE, BALL_SIZE, Color.RED );
        bat    = new GameObj(width/2, height - BRICK_HEIGHT*3/2, BRICK_WIDTH*3, 
            BRICK_HEIGHT/4, Color.BLACK);
        bricks = new ArrayList<>();

        int WALL_TOP = 100;                     // how far down the screen the wall starts
        int NUM_BRICKS = width/BRICK_WIDTH;     // how many bricks fit on screen
        for (int i=0; i < NUM_BRICKS; i++) {
            GameObj brick = new GameObj(BRICK_WIDTH*i, WALL_TOP, BRICK_WIDTH, BRICK_HEIGHT, Color.BLUE);
            bricks.add(brick);      // add this brick to the list of bricks
        }
        for (int i=0; i < NUM_BRICKS; i++) {
            GameObj brick = new GameObj(BRICK_WIDTH*i, WALL_TOP+35, BRICK_WIDTH, BRICK_HEIGHT, Color.RED);
            bricks.add(brick);      // add this brick to the list of bricks
        }
        for (int i=0; i < NUM_BRICKS; i++) {
            GameObj brick = new GameObj(BRICK_WIDTH*i, WALL_TOP+70, BRICK_WIDTH, BRICK_HEIGHT, Color.ORANGE), Border;
            bricks.add(brick);      // add this brick to the list of bricks
        }
        for (int i=0; i < NUM_BRICKS; i++) {
            GameObj brick = new GameObj(BRICK_WIDTH*i, WALL_TOP+105, BRICK_WIDTH, BRICK_HEIGHT, Color.YELLOW);
            bricks.add(brick);      // add this brick to the list of bricks
        }
    }
    
    /**
     * Animating the game
     * The game is animated by using a 'thread'. Threads allow the program to do 
     * two (or more) things at the same time. In this case the main program is
     * doing the usual thing (View waits for input, sends it to Controller,
     * Controller sends to Model, Model updates), but a second thread runs in 
     * a loop, updating the position of the ball, checking if it hits anything
     * (and changing direction if it does) and then telling the View the Model changed.
    
     * When we use more than one thread, we have to take care that they don't
     * interfere with each other (for example, one thread changing the value of 
     * a variable at the same time the other is reading it). We do this by 
     * SYNCHRONIZING methods. For any object, only one synchronized method can
     * be running at a time - if another thread tries to run the same or another
     * synchronized method on the same object, it will stop and wait for the
     * first one to finish.
    
     * Start the animation thread
     */
    public void startGame()
    {
        Thread t = new Thread( this::runGame );     // create a thread runnng the runGame method
        t.setDaemon(true);                          // Tell system this thread can die when it finishes
        t.start();                                  // Start the thread running
    }   
    
    /**
     * The main animation loop for the game.
     * This will run until gameRunning is set to false
     */
    public void runGame()
    {
        try
        {
            // set gameRunning true - game will stop if it is set false (eg from main thread)
            setGameRunning(true);
            while (getGameRunning())
            {
                updateGame();                        // update the game state
                modelChanged();                      // Model changed - refresh screen
                Thread.sleep( getFast() ? 10 : 20 ); // wait a few milliseconds
            }
        } catch (Exception e) 
        { 
            Debug.error("Model::runAsSeparateThread error: " + e.getMessage() );
        }
    }
  
    /**
     * Updating the game - this happens about 50 times a second to give the impression of movement 
     */
    public synchronized void updateGame()
    {
        // move the ball one step (the ball knows which direction it is moving in)
        ball.moveX(BALL_MOVE);                      
        ball.moveY(BALL_MOVE);
        // get the current ball possition (top left corner)
        int x = ball.topX;  
        int y = ball.topY;
        // Deal with possible edge of board hit
        if (x >= width - B - BALL_SIZE)  ball.changeDirectionX();
        if (x <= 0 + B)  ball.changeDirectionX();
        if (y >= height - B - BALL_SIZE)    // Bottom
        { 
            ball.changeDirectionY(); 
            minusLife(HIT_BOTTOM);        // minus life for hitting the bottom of the screen
        }
        if (y <= 0 + M)  ball.changeDirectionY();
        
        // check whether ball has hit a (visible) brick
        boolean hit = false;
    
        for (GameObj brick: bricks) {
            if (brick.visible && brick.hitBy(ball)) {
                hit = true;
                brick.visible = false;      // set the brick invisible
                addToScore(HIT_BRICK);      // add to score for hitting a brick
                Sound.soundClip();          // run sound() method when ball hits brick
            }
        }    
    
        if (hit)
            ball.changeDirectionY();
    
        // check whether ball has hit the bat
        if (ball.hitBy(bat))
            ball.changeDirectionY();
            
        // The below if statement tells the program to run the End window if lives = 0, showing a game over message    
        if (lives == 0) {
            Platform.runLater(new Runnable() { //This is used to tell it to run later to avoid a crash.
                @Override
                public void run() {
                    view.End(view.viewWindow);
                    gameRunning = false;
                }
            });
        }
        
        // The below if statement tells the program to run the Win window if the maximum score is reached, showing a well done message
        if (score == 2000){          
            Platform.runLater(new Runnable() { //This is used to tell it to run later to avoid a crash.
                @Override
                public void run() {
                    view.Win(view.viewWindow);
                    gameRunning = false;
                }
            });
        }
    }
    
    /**
     * This is how the Model talks to the View
     * Whenever the Model changes, this method calls the update method in
     * the View. It needs to run in the JavaFX event thread, and Platform.runLater 
     * is a utility that makes sure this happens even if called from the
     * runGame thread
     */
    public synchronized void modelChanged()
    {
        Platform.runLater(view::update);
    }
    
    // Methods for accessing and updating values
    // these are all synchronized so that the can be called by the main thread 
    // or the animation thread safely    
    
    /**
     * Change game running state - set to false to stop the game
     */
    public synchronized void setGameRunning(Boolean value)
    {  
        gameRunning = value;
    }
    
    /**
     * Return game running state
     */
    public synchronized Boolean getGameRunning()
    {  
        return gameRunning;
    }
    
    /**
     * Change game speed - false is normal speed, true is fast
     */
    public synchronized void setFast(Boolean value)
    {  
        fast = value;
    }
    
    /**
     * Return game speed - false is normal speed, true is fast
     */
    public synchronized Boolean getFast()
    {  
        return(fast);
    }
    
    /**
     * Return bat object
     */
    public synchronized GameObj getBat()
    {
        return(bat);
    }
    
    /**
     * return ball object
     */
    public synchronized GameObj getBall()
    {
        return(ball);
    }
    
    /**
     * return bricks
     */
    public synchronized ArrayList<GameObj> getBricks()
    {
        return(bricks);
    }
    
    /**
     * return score
     */
    public synchronized int getScore()
    {
        return(score);
    }
    
    /**
     * update the score
     */
    public synchronized void addToScore(int n)    
    {
        score += n;        
    }
    
    /**
     * return lives
     */
    public synchronized int getLives()
    {
        return(lives);
    }
  
    /**
     * update the lives
     */
    public synchronized int minusLife(int n)
    {
        lives += n;
        return(lives);
    }
        
    /**
     * move the bat one step - -1 is left, +1 is right
     */
    public synchronized void moveBat( int direction )
        {        
        int dist = direction * BAT_MOVE;    // Actual distance to move
        Debug.trace( "Model::moveBat: Move bat = " + dist );
        bat.moveX(dist);
    }
}   
