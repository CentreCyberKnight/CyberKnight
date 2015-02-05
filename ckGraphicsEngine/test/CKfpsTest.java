package ckGraphicsEngine.test;
/**
 * FPS stats lifted from  
 * 
 * SwingTimerTest.java 
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
 * 
*
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import ckGraphicsEngine.CKGamePanelTimer;

public class CKfpsTest extends CKGamePanelTimer
{
	
	//code from killer game programming 
	
	

	  

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1580791402422807004L;

	private static long MAX_STATS_INTERVAL = 1000L;
	    // record stats every 1 second (roughly)

	  private static int NUM_FPS = 10;
	     // number of FPS values stored to get an average

	
	// used for gathering statistics
	  private long statsInterval = 0L;    // in ms
	  private long prevStatsTime;   
	  private long totalElapsedTime = 0L;

	  private long frameCount = 0;
	  private double fpsStore[];
	  private long statsCount = 0;
	  private double averageFPS = 0.0;


	  private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
	  private DecimalFormat timedf = new DecimalFormat("0.####");  // 4 dp

	  private int period;       

	  public CKfpsTest(double targetfps,int maxdropped)
	  {
		  super(targetfps,maxdropped);
		  period = (int) (1000/targetfps);
		  
	    //period = p;

	    // initialize timer stats
	    fpsStore = new double[NUM_FPS];
	    for (int i=0; i < NUM_FPS; i++)
	      fpsStore[i] = 0.0;

	    prevStatsTime = System.nanoTime();

	  
	  } // end of SwingTimerTest()


	  private void reportStats()
	  /* The statistics:
	       - the summed periods for all the iterations in this interval
	         (period is the amount of time a single frame iteration should take), 
	         the actual elapsed time in this interval, 
	         the error between these two numbers;

	       - the total number of calls to paintComponent();

	       - the FPS (frames/sec) for this interval, and the average 
	         FPS over the last NUM_FPSs intervals.

	     The data is collected every MAX_STATS_INTERVAL  (1 sec).
	  */
	  { 
	    frameCount++;
	    statsInterval += period;

	    if (statsInterval >= MAX_STATS_INTERVAL) {     // record stats every MAX_STATS_INTERVAL
	      long timeNow = System.nanoTime();

	      long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
	      totalElapsedTime += realElapsedTime;

	      long sInterval = (long)statsInterval*1000000L;  // ms --> ns
	      double timingError = 
	          ((double)(realElapsedTime - sInterval)) / sInterval * 100.0;

	      double actualFPS = 0;     // calculate the latest FPS
	      if (totalElapsedTime > 0)
	        actualFPS = (((double)frameCount / totalElapsedTime) * 1000000000L);

	      // store the latest FPS
	      fpsStore[ (int)statsCount%NUM_FPS ] = actualFPS;
	      statsCount = statsCount+1;

	      double totalFPS = 0.0;     // total the stored FPSs
	      for (int i=0; i < NUM_FPS; i++)
	        totalFPS += fpsStore[i];

	      if (statsCount < NUM_FPS)  // obtain the average FPS
	        averageFPS = totalFPS/statsCount;
	      else
	        averageFPS = totalFPS/NUM_FPS;

	      System.out.println(timedf.format( (double) statsInterval/1000) + " " + 
	                        timedf.format((double) realElapsedTime/1000000000L) + "s " + 
							df.format(timingError) + "% " + 
	                        frameCount + "c " +
	                        df.format(actualFPS) + " " +
	                        df.format(averageFPS) + " afps" );

	      prevStatsTime = timeNow;
	      statsInterval = 0L;   // reset
	    }
	  }  // end of reportStats()

	  
	  
	  
	  
	  
	@Override
	public void calcState()
	{
		//do nothing..
		//System.out.println("calc state was called");

	}

	public void drawDebugToOffScreenBuffer(Graphics g,int width,int height)
	{
		//System.out.println("drawOffscreen buffer");
		reportStats();
		g.setColor(Color.WHITE);
		g.drawString("Average FPS"+ df.format(averageFPS), 10, 25);

	}
	
	@Override
	public void drawOffScreenBuffer(Graphics g,int width,int height)
	{
		this.drawDebugToOffScreenBuffer(g,width,height);
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	JFrame frame = new JFrame();
	//frame.setSize(400, 400);
	/* Container c = frame.getContentPane();*/    
	CKfpsTest test = new CKfpsTest(120,5);
	//test.setPreferredSize(new Dimension(500,500));
	//test.setVisible(true);
	frame.add(test);
	frame.pack();
	frame.setVisible(true);
	//Thread animator = new Thread(test);
	//animator.start();
	   frame.addWindowListener(new WindowAdapter(){
           public void windowClosing(WindowEvent e)
           	{
               	System.exit(0);
           	}
       	}
);

	}

}
