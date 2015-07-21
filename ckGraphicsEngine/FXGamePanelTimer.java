package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class FXGamePanelTimer extends Canvas
{
	
	/**
	 * 
	 */

	//private double fps;          //how many frames should be displayed per second.
	//private long framePeriod;  // how many nano seconds? take place between frames.
	//private long presentFrame;    //-current frame game engine is working to calculate.  Note that frames will likely cycle throught all possible values in the course of the game and should not be viewed as unique identifiers, but rather relative identifiers.
	//private int maxDroppedFrames; //maximum number of frames that can be dropped before the timer should drop out of real-time.
	private Image screenBuffer; //buffers what should be drawn to the screen to avoid flickering effect.
	private Graphics graphics;
	/*private volatile boolean running;  //is the panel running
	private volatile boolean paused; //is the panel in a paused state?
	private int minSleep; //minimum number of milliseconds to sleep
	private Thread animator; //thread to run the panel
	*/
	AnimationTimer timer;
	private boolean skip=false;
	
	
	private void initialize(double f_per_s,int drops)
	{
		screenBuffer=null;
	
		timer = new AnimationTimer()
		{
		
		@Override
		public void handle(long arg0)
		{
			if (arg0%5 == 0)
			{
			calcState();
			prepGraphics(false);
			drawOffScreenBuffer(graphics,getWidth(),getHeight());
			bufferToScreen();
			}
			skip=!skip;
			
		}
		};
		timer.start();
	}
	
	

	public FXGamePanelTimer()
	{
		initialize(30,10);
	
	}

		public FXGamePanelTimer(double framesPerSecond,int maxDropped)
	{
		initialize(framesPerSecond,maxDropped);
	
	}

		
	public abstract void calcState();
	public abstract void drawOffScreenBuffer(Graphics g,double d,double e);
	
	private void bufferToScreen()
	{
		GraphicsContext g;
		try
		{
			g=this.getGraphicsContext2D();
			if((g != null) && (screenBuffer!=null))
			{
				
				g.drawImage(SwingFXUtils.toFXImage((BufferedImage) screenBuffer,null),0,0);
			}
			//Toolkit.getDefaultToolkit().sync();
			//g.dispose();
		}catch( Exception e)
		{
			System.out.println("Graphics content error "+e);
			
		}	
		
	}
	
	
private boolean prepGraphics(boolean force)
{
	double width = getWidth();
	double height=getHeight();
	
	if(screenBuffer==null || force||
			screenBuffer.getWidth(null)<width ||
			screenBuffer.getHeight(null)<height)
	{//TODO - get screen coords
		if(width<128) { width=128; }
		if(height<128) { height=128;}
		//System.out.println("resetting width to"+width+" and height to "+height);
		screenBuffer=new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_ARGB);
	
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
	
	
	
	/*
	
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
			if(!paused)
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
	*/
	

}