package ckGraphicsEngine;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javafx.scene.input.MouseEvent;


public class CKSceneSlider extends CKSceneMouseListener
implements ActionListener
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
   	
   	

	
	@Override
	public void handleMouseMoved(javafx.scene.input.MouseEvent e)
	{
		
    	Point p = new Point ((int)e.getSceneX(),(int)e.getSceneY());

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
	
	


	@Override
	public void handleMouseExited(MouseEvent e)
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




	@Override
	public void handleMouseClicked(javafx.scene.input.MouseEvent e)
	{
		//pass
	}








	
	
	
	
}
