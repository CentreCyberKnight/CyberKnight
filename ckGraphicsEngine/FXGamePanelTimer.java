package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import java.util.concurrent.Semaphore;


import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

public abstract class FXGamePanelTimer extends Canvas
{
	
	/**
	 * 
	 */

	private double fps;          //how many frames should be displayed per second.
	private Graphics graphics;
	private Image screenBuffer; //buffers what should be drawn to the screen to avoid flickering effect.
	AnimationTimer timer;
	private void initialize(double f_per_s,int drops)
	{
		screenBuffer=null;
	
		@SuppressWarnings("rawtypes")
		ScheduledService time = new ScheduledService(){
			protected Task createTask() {
				return new Task() {
					protected Integer call() {
						Platform.runLater(() -> {
							calcState();
							prepGraphics(false);
							drawOffScreenBuffer(graphics,getWindowWidth(),getWindowHeight());
							bufferToScreen();		
						});
						return 0;
					}
				};
			}
		};
		time.setPeriod(Duration.seconds(1/f_per_s));
		time.start();
	}
	
	

	public FXGamePanelTimer()
	{
		super(600,600);
		initialize(30,10);
	
	}

		public FXGamePanelTimer(double framesPerSecond,int maxDropped)
	{
		super(600,600);
		initialize(framesPerSecond,maxDropped);
	
	}

				
		
	/* (non-Javadoc)
		 * @see javafx.scene.Node#isResizable()
		 */
		@Override
		public boolean isResizable()
		{
			return true;
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
	
	/**
	 * This calculates the width as it stands in its parent window 
	 * @return double of width
	 */
	public double getWindowWidth()
	{
		return getBoundsInParent().getWidth();
	}

	/**
	 * This calculates the height as it stands in its parent window 
	 * @return double of height
	 */
	public double getWindowHeight()
	{
		return getBoundsInParent().getHeight();
	}
	
	
	
private boolean prepGraphics(boolean force)
{
	double width = getWindowWidth();
	double height=getWindowHeight();
	
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
	

public void setFps(double fps)
{
	this.fps = fps;
	long framePeriod = (long) (1000000000L/this.fps);//nano seconds
	
}



public void startGame()
{
	// TODO Auto-generated method stub
	
}



public double getFps()
{
	return fps; 
}



}
