/**
 * 
 */
package cs2420;

import javax.media.opengl.*;

/**
 * @author conanz
 * 
 * Bomb class that contains all the properties of a bomb and the functions it needs to do.
 * 
 * Functions:
 * 
 *      Update: Updates bomb properties, CALLED IN BOMBGAME'S UPDATE
 *      Display: Updates bomb visuals, CALLED IN BOMBGAME'S DISPLAY
 *      Getter/Setter: isGone variable to determine when bomb is too small to display
 *
 */
public class Target
{
    /**Class Member Variables**/
    public double windowWidth, windowHeight;

    public double xCoordinate, yCoordinate;
    public double xVelocity, yVelocity;
    public double red, green, blue;
    public double size;
    
    public int level;
    private boolean isGone;

    /**CONSTRUCTOR**/
    public Target(double windowWidthParameter, double windowHeightParameter, int levelParameter)
    {
      //set class member variables equal to parameters
        windowWidth = windowWidthParameter;
        windowHeight = windowHeightParameter;
        
        level = levelParameter;
        
        /**Initiate Default Values**/
        size = Math.random()*(40-10)+10;
        
        //randomized velocities
        xVelocity = Math.random()*2;
        yVelocity = Math.random()*2;

        //randomized position
        xCoordinate = Math.random()*windowWidth/2;
        yCoordinate = Math.random()*windowHeight/2;
        
        //color depends on level
        red = 0.07f*(level);
        green = 0.07f*(level);
        blue = 0.07f*(level);
        
        //initial state 
        isGone = false;
    }
    
    /**FUNCTION UPDATE TO BE CALLED FROM THE BOMBGAME UPDATE THAT UPDATES ALL PROPERTIES OF TARGETS**/
    public synchronized void update(long time)
    {
        //Move
        xCoordinate += (xVelocity)*time/10;
        yCoordinate += (yVelocity)*time/10;
        
        //Bounce if necessary
        if(xCoordinate+size >= windowWidth || xCoordinate-size < 0)
        {
            xVelocity *= -1;
        }
        if(yCoordinate+size >= windowHeight || yCoordinate-size < 0)
        {
            yVelocity *= -1;
        }
        
        //Reset to random position when target may get stuck
        if(xCoordinate+size - windowWidth > 5 || xCoordinate-size < -5)
        {
            xCoordinate = Math.random()*windowWidth/2;
        }
        if(yCoordinate+size - windowHeight > 5 || yCoordinate-size < -5)
        {
            yCoordinate = Math.random()*windowHeight/2;
        }
    }
    /**FUNCTION DISPLAY TO BE CALLED FROM THE BOMBGAME DISPLAY THAT UPDATES WHAT THE TARGET LOOKS LIKE**/
    public synchronized void display (GLAutoDrawable gld)
    {
        //create gl to use
        final GL2 gl = gld.getGL().getGL2();//need to run gl
        
        //Create square and it's color
        gl.glColor3d(red, green, blue);
        gl.glBegin(GL2GL3.GL_QUADS);//changes how inputs are interpreted below
        
        //COORDINATES DRAWN CLOCKWISE
        gl.glVertex3d(xCoordinate-size, yCoordinate-size, 0);//bottom left
        gl.glVertex3d(xCoordinate+size, yCoordinate-size, 0);//top left
        gl.glVertex3d(xCoordinate+size, yCoordinate+size, 0);//top right
        gl.glVertex3d(xCoordinate-size, yCoordinate+size, 0);//bottom right
        
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
