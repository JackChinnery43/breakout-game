import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

/**
*   The breakout controller receives KeyPress events from the GUI (via
*   the KeyEventHandler). It maps the keys onto methods in the model and
*   calls them appropriately
*/

public class Controller
{
    public Model model;
    public View  view;
    
    // we don't really need a constructor method, but include one to print a 
    // debugging message if required
    public Controller()
    {
        Debug.trace("Controller::<constructor>");
    }

    /**
    *   This is how the View talks to the Controller
    *   AND how the Controller talks to the Model
    *   This method is called by the View to respond to key presses in the GUI
    *   The controller's job is to decide what to do. In this case it converts
    *   the keypresses into commands which are run in the model.
    *   I edited the template code provided to us to use several if statements
    *   instead of the switch statement that was provided to us.
    *   This was because I understand if statements more than I understand
    *   switch statements, and so I was more confident making changes to the code.
    */
    
    public void userKeyInteraction(KeyEvent event)
    {
    Debug.trace("Controller::userKeyInteraction: keyCode = " + event.getCode() );
     
    if (event.getCode() == KeyCode.LEFT){ // key code so the user can move the bat to the left
        model.moveBat(-1); }
        
    if (event.getCode() == KeyCode.RIGHT){ // key code so the user can move the bat to the right
        model.moveBat(+1); }
        
    if (event.getCode() == KeyCode.F){ // key code so the user can increase the speed of the ball
        model.setFast(true); }
        
    if (event.getCode() == KeyCode.N){ // key code so the user can slow down the speed of the ball
        model.setFast(false); }
        
    if (event.getCode() == KeyCode.ENTER){ // key code I added so that the user can restart the game by pressing the ENTER key
        model.initialiseGame(); }
    
    if (event.getCode() == KeyCode.ESCAPE){ // key code I added so that the user can stop the game running by pressing the ESCAPE key
        model.setGameRunning(false); 
    }
  }

}
