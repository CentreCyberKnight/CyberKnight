package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import javax.swing.JPanel;

//TODO stop thread when jcomponent is removed/disposed

public abstract class CKGamePanelTimer extends JPanel implements Runnable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2619910248895198792L;
	private double fps;          //how many frames should be displayed per second.
	private long framePeriod;  // how many nano seconds? take place between frames.
	private long presentFrame;    //-current frame game engine is working to calculate.  Note that frames will likely cycle throught all possible values in the course of the game and should not be viewed as unique identifiers, but rather relative identifiers.
	private int maxDroppedFrames; //maximum number of frames that can be dropped before the timer should drop out of real-time.
	private Image screenBuffer; //buffers what should be drawn to the screen to avoid flickering effect.
	private Graphics graphics;
	private volatile boolean running;  //is the panel running
	private volatile boolean paused; //is the panel in a paused state?
	private int minSleep; //minimum number of milliseconds to sleep
	private Thread animator; //thread to run the panel
	
	private void initialize(double f_per_s,int drops)
	{
		setFps(f_per_s);
		presentFrame=0;
		maxDroppedFrames=drops;
		screenBuffer=null;
		running=false;
		paused=false;
		minSleep=1;
	}
	
	

	public CKGamePanelTimer()
	{
		initialize(30,10);
	
	}

	public CKGamePanelTimer(LayoutManager layout)
	{
		super(layout);
		initialize(30,10);
	}

	public CKGamePanelTimer(double framesPerSecond,int maxDropped)
	{
		initialize(framesPerSecond,maxDropped);
	
	}

	public CKGamePanelTimer(LayoutManager layout,double framesPerSecond,int maxDropped)
	{
		super(layout);
		initialize(framesPerSecond,maxDropped);
	}

	
	
	public abstract void calcState();
	public abstract void drawOffScreenBuffer(Graphics g,int width,int height);
	
	private void bufferToScreen()
	{
		Graphics g;
		try
		{
			g=this.getGraphics();
			if((g != null) && (screenBuffer!=null))
			{
				g.drawImage(screenBuffer,0,0,null);
			}
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}catch( Exception e)
		{
			System.out.println("Graphics content error "+e);
			stopPanel();
			
		}	
		
	}
	
	
private boolean prepGraphics(boolean force)
{
	int width = getWidth();
	int height=getHeight();
	
	if(screenBuffer==null || force||
			screenBuffer.getWidth(null)<width ||
			screenBuffer.getHeight(null)<height)
	{//TODO - get screen coords
		if(width<128) { width=128; }
		if(height<128) { height=128;}
		//System.out.println("resetting width to"+width+" and height to "+height);
		screenBuffer=createImage(width,height);
	
		if(screenBuffer==null)
		{
			return false;			
		}
		graphics = screenBuffer.getGraphics();
	}
	graphics.setColor(Color.BLACK);
	graphics.fillRect(0,0,screenBuffer.getWidth(null),screenBuffer.getHeight(null));
	return true;	
}
	
	
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(screenBuffer!=null)
		{
			g.drawImage(screenBuffer, 0,0,null);
		}
	}



	@Override
	public void run()
	{
		//System.out.println("thread running");
		running=true;
		int droppedFrames=0;
		long oversleep=0;
		long nextWake=System.nanoTime();
		
		while(running)
		{
			presentFrame++;
			nextWake+=framePeriod;
			calcState();
			if(oversleep>framePeriod && droppedFrames<maxDroppedFrames)
			{
				droppedFrames++;
				presentFrame++;
				nextWake+=framePeriod;
				calcState();
				oversleep-=framePeriod;
			}
			if(!paused && isShowing())
			{
				prepGraphics(false);
				drawOffScreenBuffer(graphics,getWidth(),getHeight());
				bufferToScreen();
			}
			droppedFrames=0;
			
			
			//sleep now..
			try
			{
				Thread.sleep(calcSleepTime(nextWake));
			} catch (InterruptedException e)
			{
				//do nothing..
			}
			
			//calculate oversleep time
			oversleep=System.nanoTime()-nextWake;
		}//game is stopped
		
		
	}
	
	public double getFps()
	{
		return fps;
	}



	public void setFps(double fps)
	{
		this.fps = fps;
		framePeriod=(long) (1000000000L/this.fps);//nano seconds
	}



	public int getMaxDroppedFrames()
	{
		return maxDroppedFrames;
	}



	public void setMaxDroppedFrames(int maxDroppedFrames)
	{
		this.maxDroppedFrames = maxDroppedFrames;
	}



	public boolean isPaused()
	{
		return paused;
	}



	public void pauseGame(boolean paused)
	{
		this.paused = paused;
	}



	public int getMinSleep()
	{
		return minSleep;
	}



	public void setMinSleep(int minSleep)
	{
		this.minSleep = minSleep;
	}



	public long getPresentFrame()
	{
		return presentFrame;
	}



	public void stopPanel()
	{
		this.running = false;
	}

	
	  public void addNotify()
	  // wait for the JPanel to be added to the JFrame before starting
	  { super.addNotify();   // creates the peer
	    startGame();         // start the thread
	  }


	  public void startGame()
	  // initialize and start the thread 
	  { 
	    if (animator == null || !running) {
	      animator = new Thread(this);
		  animator.start();
	    }
	  } // end of startGame()
	    



	private int calcSleepTime(long nextWake)
	{
		int present = (int) ((nextWake - System.nanoTime())/1000000L);
		if(present<0) {present=minSleep;}
		return present;
	}

}
