import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class Controller
{
    public Model model;
    public View  view;
    
   
    public Controller()
    {
        Debug.trace("Controller::<constructor>");
    }

    
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
