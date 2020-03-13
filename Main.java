import javafx.application.Application;
import javafx.stage.Stage;

/**
 *  breakoutJavaFX project Main class
 */

public class Main extends Application
{
    public static void main( String args[] )
    {
        /**
        *   The main method only gets used when launching from the command line
        *   launch initialises the system and then calls start
        *   In BlueJ, BlueJ calls start itself
        *   The main class is linked to all other classes required for the game to run.
        */
        launch(args);
    }

    public void start(Stage window) 
    {
        int H = 800;         // Height of window pixels 
        int W = 600;         // Width  of windinfoTextow pixels 

        // set up debugging and print initial debugging message
        Debug.set(true);             
        Debug.trace("breakoutJavaFX starting"); 
        Debug.trace("Main::start"); 

        // Create the Model, View and Controller objects
        Model model = new Model(W,H);
        View  view  = new View(W,H);
        Controller controller  = new Controller();

        // Link them together so they can talk to each other
        // Each one has instance variables for the other two
        model.view = view;
        model.controller = controller;
        controller.model = model;
        controller.view = view;
        view.model = model;
        view.controller = controller;

        // start up the GUI (view), and then tell the model to initialise itself
        // and start the game running
        view.start(window); 
        model.startGame();

        // application is now running
        Debug.trace("breakoutJavaFX running"); 
    }
}
