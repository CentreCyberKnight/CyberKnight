package ckGraphicsEngine;


import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class FXGamePanelTimer extends Canvas
{
	
	/**
	 * 
	 */

	private double fps;          //how many frames should be displayed per second.
	private GraphicsContext graphics;
	//private Canvas canvas;
	
	//private Image screenBuffer; //buffers what should be drawn to the screen to avoid flickering effect.
	AnimationTimer timer;
	private void initialize(double f_per_s,int drops)
	{
		//screenBuffer=null;
		//canvas=new Canvas();
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
	public abstract void drawOffScreenBuffer(GraphicsContext g,double d,double e);
	
	private void bufferToScreen()
	{//not using any buffering, it will go directly to it.
	/*	GraphicsContext g;
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
		*/
		
	/*double width = getWindowWidth();
		double height=getWindowHeight();
			
		graphics = this.getGraphicsContext2D();
		graphics.setFill(Color.RED);
		graphics.fillRect(0,0,width,height);*/
		
		/* try two
		this.getGraphicsContext2D()
		.drawImage(canvas.snapshot(new SnapshotParameters(),null),0,0);
		*/
		
		
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
/*
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
	*/
	
	//graphics is already a buffer in javafx.
	
	//System.out.println("resetting width to"+width+" and height to "+height);
	/*canvas.setWidth(width);
	canvas.setHeight(height);

	graphics = canvas.getGraphicsContext2D();
	
	*/
	
	graphics = this.getGraphicsContext2D();
	graphics.setFill(Color.BLACK);
	graphics.fillRect(0,0,width,height);
	return true;	
}
	

public void setFps(double fps)
{
	this.fps = fps;
	//long framePeriod = (long) (1000000000L/this.fps);//nano seconds
	
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
