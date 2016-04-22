package ckGraphicsEngine;

import java.awt.Point;

import javafx.scene.input.MouseEvent;

//import java.awt.event.MouseEvent;

public interface CKGraphicMouseInterface
{
	
	/**
	 * Helper function for conversion between swing and FX.
	 * @param e
	 * @return
	 */
	public default  Point getPoint(MouseEvent e)
	{
		return new Point((int)e.getSceneX(),(int)e.getSceneY());
		
	}
	public void handleMouseClicked(MouseEvent e);
	public void handleMouseMoved(MouseEvent e);
	public void handleMouseExited(MouseEvent e);
	
}
