package ckGraphicsEngine;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Timer;


public class CKSceneSlider extends CKSceneMouseListener implements ActionListener
{
	
	private int delay; 
	Timer t;
	public CKSceneSlider(CKGraphicsSceneInterface s)
	{
		super(s);
		delay=0;
		t = new Timer(200,this);
		
	}

 	private double xshift = 0;
   	private double yshift=0;
   	
   	

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionAdapter#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		
    	Point p = e.getPoint();

       	xshift = 0;
       	yshift=0;
     	int activeMargin = 50;
    	double mapShift = .5;
    	int prevalue=delay;
    	    	
    	//check if a slide is necessary
    	if(p.x<activeMargin)
    	{
    		xshift=mapShift;
    		yshift=-mapShift;
    		delay++;
    	}
    	else if((getScene().getTrans().getScreenWidth()-p.x)<activeMargin)
    	{
    		xshift=-mapShift;
    		yshift=mapShift;
    		delay++;
     	}
    	if(p.y<activeMargin)
    	{
        		yshift=mapShift;
        		xshift=mapShift;
        		delay++;
    	}
    	else if((getScene().getTrans().getScreenHeight()-p.y)<activeMargin)
    	{
    		yshift=-mapShift;
    		xshift=-mapShift;
    		delay++;
    	}
    
    	if(prevalue==delay)
    	{
    		delay=0;
    		t.stop();
    	}
    	else
    	{
    		t.start();
    	}
  	}
	
	


	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		t.stop();
	}




	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(delay > 10)
		{
			getScene().getCamera().moveBy(-xshift, -yshift);
		}
		else if(delay>0)
		{
			delay++;
		}

		
	}

	
	
	
	
}
