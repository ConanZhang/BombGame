package cs2420;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.swing.JApplet;
import com.jogamp.opengl.util.FPSAnimator;
import linked_list.LinkedList;
import cs2420.Bomb;
import cs2420.Target;

/**
 * @author conanz
 *
 */
public class BombGame extends JApplet implements GLEventListener, MouseListener
{
    /**CLASS MEMBER VARIABLES**/
    GLU glu;
    int winWidth, winHeight;
    
    /**Linked List**/
    //targets
    LinkedList<Target> targets;
    LinkedList<Target>.Iterator targetIterator;
    int targetAmount;
    int targetsAlive;
    
    //bombs
    LinkedList<Bomb> bombs;
    LinkedList<Bomb>.Iterator bombIterator;
    
    //animator that refreshes by FPS
    FPSAnimator animator;
    
    //separate DISPLAY thread from animation thread
    Thread t;
    long lastTime;
    
    /**CONSTRUCTOR**/
    public BombGame() 
    {
        //initiate class member variables
        winWidth = 700;
        winHeight = 700;
        
        targets = new LinkedList<Target>();
        bombs = new LinkedList<Bomb>();
        
        targetAmount = 15;
        targetsAlive = targetAmount;
        
        //Create targets on random levels
        for(int i = 0; i<targetAmount; i++)
        {
            Random r = new Random();
            int level = r.nextInt(20-4)+4;

            targets.insert(new Target(winWidth,winHeight, level));
        }
        
        //take first time
        lastTime = System.currentTimeMillis();
    }
    
    /**FUNCTION TO UPDATE BOMBS AND TARGETS BY CALLING THEIR RESPECTIVE FUNCTIONS**/
    public synchronized void update() 
    {
        //calculate time since last update to use for next update
        long time = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        
        /**TARGETS**/
        //Create new iterator to scroll through TARGET linked list
        targetIterator = targets.first();

        //While the iterator actually points to a node in the linked list... (NOT null, NOT header, and NOT footer)
        while(targetIterator.valid() )
        {
            //Get the data to work with
            Target current = targetIterator.getData();
  
            //update the target
            current.update(time);
            
            /**CHECK COLLISIONS**/
            //CHECKS ALL BOMBS AGAINST ALL TARGETS**/
            
            //on current target
            Target currentTarget = targetIterator.getData();
            //start on first bomb
            bombIterator = bombs.first();
            
            while(bombIterator.valid())
            {
                //on current bomb
                Bomb currentBomb = bombIterator.getData();
                
                //Collision uses distance formula and level check
                if(Math.pow(currentBomb.xCoordinate - currentTarget.xCoordinate, 2) + 
                   Math.pow(currentBomb.yCoordinate - currentTarget.yCoordinate, 2) < 3000  
                   && currentBomb.level == currentTarget.level)
                {
                    //set the logic to remove the items from their linked lists
                    currentBomb.setGone(true);
                    currentTarget.setGone(true);
                    
                    //decrease counter
                    targetsAlive -=1;
                }
                
                //check next bomb
                bombIterator.next();
            }
            
            //check if we need to remove the target from the linked list
            if (current.getGone())
            {
                targetIterator.remove();
                // go back one spot so we don't skip an item
                targetIterator.prev();
            }
            
            //Advance to next node in list
            targetIterator.next();
        }
        
        /**BOMBS**/
        //Create new iterator to scroll through BOMB linked list 
        bombIterator = bombs.first();

        //While the iterator actually points to a node in the linked list... (NOT null, NOT header, and NOT footer)
        while(bombIterator.valid() )
        {
            //Get the data to work with
            Bomb current = bombIterator.getData();
  
            //update bomb
            current.update(time);
            
            //check if we need to remove the bomb from the linked list
            if (current.getGone())
            {
                bombIterator.remove();
                // go back one spot so we don't skip an item
                bombIterator.prev();
            }
            
            //Advance to next node in list
            bombIterator.next();
        }
        
        //MAKE MORE TARGETS!!! HAHAHA, THE GAME NEVER ENDS!!!
        if(targetsAlive <= 0)
        {
            //Create MORE targets on random levels
            for(int i = 0; i<targetAmount; i++)
            {
                Random r = new Random();
                int level = r.nextInt(20-4)+4;

                targets.insert(new Target(winWidth,winHeight, level));
            }
            
            //reset counter
            targetsAlive = targetAmount;
        }
    }
    
    /**FUNCTION TO DISPLAY BOMBS AND TARGETS BY CALLING THEIR RESPECTIVE FUNCTIONS**/
    public synchronized void display (GLAutoDrawable gld)
    {
        //need to create GL2 to use
        final GL2 gl = gld.getGL().getGL2();
        
        //clear the window
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //create two frame buffers to display one frame and update another to replace the previous one
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        //set up the camera 
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        //create a 2 dimensional orthographic window display with a camera that has theoretically infinite focal length
        glu.gluOrtho2D(0.0, winWidth, 0.0, winHeight);
        
        //initialize world transformations
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);//create matrix stack because things are stored with hierarchy
        gl.glLoadIdentity();

        /**TARGETS**/
        //Create new iterator to scroll through linked list with
        targetIterator = targets.first();

        //While the iterator actually points to a node in the linked list... (NOT null, NOT header, and NOT footer)
        while(targetIterator.valid() )
        {
            //Get the data to work with
            Target current = targetIterator.getData();
            
            //update display
            current.display(gld);
            
            //Advance to next node in list
            targetIterator.next();
        }        
        
        /**BOMBS**/
        //Create new iterator to scroll through linked list with
        bombIterator = bombs.first();

        //While the iterator actually points to a node in the linked list... (NOT null, NOT header, and NOT footer)
        while(bombIterator.valid() )
        {
            //Get the data to work with
            Bomb current = bombIterator.getData();
  
            //update display
            current.display(gld);
            
            //Advance to next node in list
            bombIterator.next();
        }
        
    }

    /**INITIATE GLU**/
    public void init (GLAutoDrawable arg0)
    {
        glu = new GLU();
    }

    /**RESHAPE THE GL**/
    public void reshape (GLAutoDrawable gld, int x, int y, int width, int height)
    {
        //create GL to use
        GL gl = gld.getGL();
        
        //assign class member variables to parameters
        winWidth = width;
        winHeight = height;
        
        //set the mapping from camera space to the window
        gl.glViewport(0,0, width, height);
    }

    /**INITIATE THE APPLET**/
    public void init() 
    {
        //insert widget
        setLayout(new FlowLayout());
        
        //create a gl drawing canvas and map gl to window to create link between them
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        //create canvas to draw to
        GLCanvas canvas = new GLCanvas(caps);
        canvas.setPreferredSize(new Dimension(winWidth, winHeight));
        
        //event listeners
        canvas.addGLEventListener(this);
        canvas.addMouseListener(this);
        
        add(canvas);
        setSize(winWidth, winHeight);
        
        //create an animator to call display which refreshes the canvas
        animator = new FPSAnimator(canvas, 30);
        
        //create thread with super class runnable
        t = new Thread( new Runnable(){
                                        /**THREAD TO ALWAYS RUNS UPDATE**/
                                		public void run() 
                                		{
                                		    while (true)
                                		    {
                                		        update();
                                		    }
                                		}
	                                    
                                      });
    }

    /**START EVERYTHING**/
    public void start() 
    {
        animator.start();
        t.start();
    }
    
    /**STOP ANIMATING**/
    public void stop() 
    {
        animator.stop();
    }
    
    /**CREATE BOMBS WHEN THE MOUSE IS PRESSED**/
    public void mousePressed (MouseEvent e)
    {
        bombs.insert(new Bomb(e.getX(),  winHeight - e.getY()));
    }

    /**UNIMPLEMENTED FUNCTIONS REQUIRED FOR BOMBGAME CLASS IMPLEMENTATIONS GLEventListener, MouseListener**/
    public synchronized void mouseReleased (MouseEvent e)
    {
        
    }

    public void mouseClicked (MouseEvent e)
    {
        
    }

    public void mouseEntered (MouseEvent e)
    {
        
    }

    public void mouseExited (MouseEvent e)
    {
        
    }
    
	public void dispose (GLAutoDrawable arg0)
    {
        
    }
}