import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
*   The View class creates and manages the GUI for the application.
*   It doesn't know anything about the game itself, it just displays
*   the current state of the Model, and handles user input from the Controller class
*/

public class View implements EventHandler<KeyEvent>
{ 
    // variables for components of the user interface
    public int width;       // width of window
    public int height;      // height of window

    // usr interface objects
    public Pane pane;       // basic layout pane
    public Canvas canvas;   // canvas to draw game on
    public Label infoText;  // info at top of screen
    public Label infoTextLives; // declaring variable for lives label at top of screen
    public Label infoTextHighScore; // declaring variable for high score at top of screen
    public Label infoTextGameOver;  // declaring variable for lives label at top of screen
    public Label infoTextYouWin;    // declaring variable for lives label at top of screen

    // The other parts of the model-view-controller setup
    public Controller controller;
    public Model model;

    public GameObj   bat;            // The bat
    public GameObj   ball;           // The ball
    public ArrayList<GameObj> bricks;     // The bricks
    public int score = 0;  // The score
    public int lives = 3;  // The Lives
    
    Stage viewWindow;

    // we don't really need a constructor method, but include one to print a 
    // debugging message if required
    public View(int w, int h)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
    }
    
    /**
    *   start is called from Main, to start the GUI up
    *   Note that it is important not to create controls etc here and
    *   not in the constructor (or as initialisations to instance variables),
    *   because we need things to be initialised in the right order
    */
    
    public void start(Stage window) 
    {
        viewWindow = window;
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the text at the top - this
        // is a label which sits 'on top of' the canvas.
        
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box for the score - a label which we position on 
        //the canvas with translations in X and Y coordinates
        infoText = new Label("Score = " + score);
        infoText.setId("infoTextScore");
        infoText.setTranslateX(50);
        infoText.setTranslateY(10);
        pane.getChildren().add(infoText);  // add label to the pane
        
        // code to add a lives counter to the view
        infoTextLives = new Label("Lives = " + lives);
        infoTextLives.setId("infoTextLives");
        infoTextLives.setTranslateX(400);
        infoTextLives.setTranslateY(10);
        pane.getChildren().add(infoTextLives);
        
        // code to show the users highscore
        // infoTextHighScore = new Label("High score = ");
        // infoTextHighScore.setTranslateX(50);
        // infoTextHighScore.setTranslateY(40);
        // pane.getChildren().add(infoTextHighScore);
        
        // add the complete GUI to the scene
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // Add an event handler for key presses. We use the View object itself
        // and provide a handle method to be called when a key is pressed.
        scene.setOnKeyPressed(this);

        // put the scene in the window and display it
        window.setScene(scene);
        window.show();
    }
    
    /**
    *   The End method creates a new scene that displays
    *   the end game result if the user runs out of lives,
    *   showing a game over message, coloured red, at the
    *   top of the screen, as well as their final score.
    *   The game also ends.
    */    
    public void End(Stage window) 
    {    
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box if user runs out of lives - a label which we position on 
        // the canvas with translations in X and Y coordinates
        infoTextGameOver = new Label("Game Over!");
        infoTextGameOver.setId("infoTextGameOver"); // id set so text can be edited in CSS file
        infoTextGameOver.setTranslateX(50);
        infoTextGameOver.setTranslateY(10);
        pane.getChildren().add(infoTextGameOver);  // add label to the pane
        
        // infoText box for the score - a label which we position on 
        //the canvas with translations in X and Y coordinates
        infoText = new Label("Your final score = " + score);
        infoText.setId("infoTextScore"); // id set so text can be edited in CSS file
        infoText.setTranslateX(400);
        infoText.setTranslateY(10);
        pane.getChildren().add(infoText);  // add label to the pane
        
        // add the complete GUI to the scene
        Scene sceneEnd = new Scene(pane);   
        sceneEnd.getStylesheets().add("breakout.css"); // tell the app to use our css file
        
        // Add an event handler for key presses. We use the View object itself
        // and provide a handle method to be called when a key is pressed.
        sceneEnd.setOnKeyPressed(this);
        
        // put the scene in the winodw and display it
        window.setScene(sceneEnd);
        window.show();
    }

    /**
    *   The Win method creates a new scene that displays
    *   the final game result if the user removes all the bricks,
    *   and achieves the max score of 2000. It displays a
    *   well done, message, coloured green, at the top of the screen, 
    *   as well as their final score. The game also ends.
    */    
    public void Win(Stage window) 
    {
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the text at the top - this
        // is a label which sits 'on top of' the canvas.
        
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box for if the user reacher the max score - a label which we position on 
        //the canvas with translations in X and Y coordinates
        infoTextYouWin = new Label("Well done, you win!");
        infoTextYouWin.setId("infoTextYouWin"); // id set so text can be edited in CSS file
        infoTextYouWin.setTranslateX(50);
        infoTextYouWin.setTranslateY(10);
        pane.getChildren().add(infoTextYouWin);  // add label to the pane
        
        // infoText box for the score - a label which we position on 
        //the canvas with translations in X and Y coordinates
        infoText = new Label("Your final score = " + score);
        infoText.setId("infoTextScore"); // id set so text can be edited in CSS file
        infoText.setTranslateX(400);
        infoText.setTranslateY(10);
        pane.getChildren().add(infoText);  // add label to the pane
        
        // add the complete GUI to the scene
        Scene sceneWin = new Scene(pane);   
        sceneWin.getStylesheets().add("breakout.css"); // tell the app to use our css file
        
        // Add an event handler for key presses. We use the View object itself
        // and provide a handle method to be called when a key is pressed.
        sceneWin.setOnKeyPressed(this);
        
        // put the scene in the winodw and display it
        window.setScene(sceneWin);
        window.show();
    }    
    
    /**
     * Event handler for key presses - it just passes the event to the controller 
     */
        public void handle(KeyEvent event)
    {
        // send the event to the controller
        controller.userKeyInteraction( event );
    }
    
    /**
     * drawing the gane
     */
    public void drawPicture(){
    // the ball movement is runnng 'i the background' so we have
    // add the following line to make sure
    synchronized( Model.class )    // Make thread safe
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
    
        // clear the canvas to redraw
        gc.setFill( Color.WHITE );
        gc.fillRect( 0, 0, width, height );
        
        // update score
        infoText.setText("Score = " + score);
        // update lives
        infoTextLives.setText("Lives = " + lives);
    
        // draw the bat and ball
        displayGameObj( gc, ball );   // Display the Ball
        displayGameObj( gc, bat  );   // Display the Bat
    
        // *[2]*****************************************************
        // * Display the bricks that make up the game                 *
        // * Fill in code to display bricks from the ArrayList        *
        // * Remember only a visible brick is to be displayed         *
        // ************************************************************
        for (GameObj brick: bricks) {
            if (brick.visible) {
                displayGameObj(gc, brick);
            }
        }           
    }
    }
    
    /**
     * Display a game object
     */
    public void displayGameObj( GraphicsContext gc, GameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }   
    
    /**
     * This is how the Model talks to the View
     * This method gets called BY THE MODEL, whenever the model changes
     * It has to do whatever is required to update the GUI to show the new model status
     */
    public void update()
    {
        // Get from the model the ball, bat, bricks & addToScorescore
        ball    = model.getBall();              // Ball
        bricks  = model.getBricks();            // Bricks
        bat     = model.getBat();               // Bat
        score   = model.getScore();             // Score
        lives   = model.getLives();
        
        //Debug.trace("Update");
        drawPicture();                     // Re draw game
    }
    
    
}