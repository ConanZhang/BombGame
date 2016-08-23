/**
 * 
 */
package cs2420;

import javax.media.opengl.*;

/**
 * @author conanz
 * 
 * Target class that contains all the properties of a target and the functions it needs to do.
 * 
 * Functions:
 * 
 *      Update: Updates target's properties, CALLED IN BOMBGAME'S UPDATE
 *      Display: Updates target's visuals, CALLED IN BOMBGAME'S DISPLAY
 *      Getter/Setter: isGone variable to determine when target is destroyed
 *
 */
public class Bomb
{
    /**Class Member Variables**/
    public double windowWidth, windowHeight;

    public double xCoordinate, yCoordinate;
    public double xVelocity, yVelocity;
    public double size;
    public double red, green, blue;
    
    private boolean isGone;
    private int timeAlive;
    public int level;
    
    /**CONSTRUCTOR**/
    public Bomb(double xCoordinateParameter, double yCoordinateParameter)
    {
        //set class member variables equal to parameters
        xCoordinate = xCoordinateParameter;
        yCoordinate = yCoordinateParameter;
        
        /**Initiate Default Values**/
        size = 20;
        
        //color
        red = 1.0f;
        green = 1.0f;
        blue = 1.0f;
        
        //initial state and position
        isGone = false;
        timeAlive = 2000;
        level = 20;
    }
    
    /**FUNCTION UPDATE TO BE CALLED FROM THE BOMBGAME UPDATE THAT UPDATES ALL PROPERTIES OF BOMBS**/
    public synchronized void update(long time)
    {
        //Shrink by time duration
        if(size > 0)
        {
            size -= (double)time/100;
        }
        else if(size <= 0)
        {
            isGone = true;
        }
        
        //Fade color
        red -= 0.0000005;
        green -= 0.0000005;
        blue -= 0.0000005;
        
        //Decrease level by time alive
        timeAlive -= time;
        level = timeAlive/100;
    }
    /**FUNCTION DISPLAY TO BE CALLED FROM THE BOMBGAME DISPLAY THAT UPDATES WHAT THE BOMB LOOKS LIKE**/
    public synchronized void display (GLAutoDrawable gld)
    {
        //create gl to use
        final GL2 gl = gld.getGL().getGL2();
        
        //Create triangle and it's color
        gl.glColor3f((float)red, (float)green, (float)blue);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        
        //coordinates
        gl.glVertex3f((float)(xCoordinate-size), (float)(yCoordinate-size), 0);//bottom left
        gl.glVertex3f((float)(xCoordinate+size), (float)(yCoordinate-size), 0);//bottom right
        gl.glVertex3f((float)(xCoordinate), (float)(yCoordinate+size), 0);//top center
        
        gl.glEnd();
    }
    
    /**GETTER FUNCTION FOR isGone BOOLEAN**/
    public boolean getGone()
    {
        return isGone;
    }
    
     /**SETTER FUNCTION FOR isGone BOOLEAN**/
    public void setGone(boolean goneParameter)
    {
        isGone = goneParameter;
    }
}