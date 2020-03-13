import javafx.scene.paint.*;

/**
*   The GameObj class states the variables for objects in the game, 
*   represented as a rectangle, with a position, a colour, and a direction of movement.
*/
public class GameObj
{
    // state variables for a game object
    public boolean visible  = true;     // Can see (change to false when the brick gets hit)
    public int topX   = 0;              // Position - top left corner X
    public int topY   = 0;              // position - top left corner Y
    public int width  = 0;              // Width of object
    public int height = 0;              // Height of object
    public Color colour;                // Colour of object
    public int   dirX   = 1;            // Direction X (1 or -1)
    public int   dirY   = 1;            // Direction Y (1 or -1)


    public GameObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }

    /**
     * move in x axis
     */ 
    public void moveX( int units )
    {
        topX += units * dirX;
    }

    /** 
     * move in y axis
     */
    public void moveY( int units )
    {
        topY += units * dirY;
    }

     /** 
     * change direction of movement in x axis (-1, 0 or +1)
     */
    public void changeDirectionX()
    {
        dirX = -dirX;
    }

     /** 
     * change direction of movement in y axis (-1, 0 or +1)
     */
    public void changeDirectionY()
    {
        dirY = -dirY;
    }
    
     /** 
     * The hitBy() method detects collision between the stated objects,
     * by determining the width and height of the objects and whether they
     * overlap each other
     */
    public boolean hitBy( GameObj obj )
    {
        return ! ( topX >= obj.topX+obj.width     ||
            topX+width <= obj.topX         ||
            topY >= obj.topY+obj.height    ||
            topY+height <= obj.topY );

    }
}
