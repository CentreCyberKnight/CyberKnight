/**
 * 
 */
package ckGraphicsEngine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import ckGraphicsEngine.assets.CKGraphicsAsset;

/**
 * @author dragonlord
 *
 */
public class CKDialogChoiceMessage extends CKDialogMessage
{
	ArrayList<CKDialogChoice> choices = new ArrayList<CKDialogChoice>();
	
	public CKDialogChoiceMessage(String message,CKGraphicsAsset asset)
	{
		super(message,asset,0);
		
	}
	
	public CKDialogChoiceMessage(String message)
	{
		super(message);
		
	}

	public void addChoice(CKDialogChoice ch)
	{
		ch.setMargins(2*xmargin,ymargin);
		choices.add(ch);
	}
	


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKDialogMessage#drawMessage(java.awt.Graphics, java.awt.Rectangle, java.awt.image.ImageObserver)
	 */
	@Override
	public Rectangle drawMessage(Graphics g, Rectangle drawableArea,
			ImageObserver observer)
	{
		Rectangle bounds = drawAsset(g,drawableArea,observer);
		Rectangle newBounds = CKDialogMessage.createDiffRect(drawableArea, bounds);
		bounds = message.drawText(g, newBounds);
		//message.outlineText(g, Color.RED);
		//need to move the choices in more
		for(CKDialogChoice ch: choices)
		{
			newBounds = CKDialogMessage.createDiffRect(newBounds, bounds);
			//System.out.println("Rect:"+newBounds+" bounds:"+bounds+" for message "+ch);
			bounds = ch.drawText(g,newBounds);
			//ch.outlineText(g, Color.RED);
		}
		//return the earea that got used
		return CKDialogMessage.createDiffRect(drawableArea, newBounds);
	}



	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKDialogMessage#shouldCloseFromMouse(java.awt.event.MouseEvent)
	 */
	@Override
	public boolean shouldCloseFromMouse(MouseEvent e)
	{
		for(CKDialogChoice ch: choices)
		{
			if(ch.contians(e.getPoint()))
			{
				ch.setSelected(true);
				notifyListeners(ch);
				return true;
			}
		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKDialogMessage#pickedByMouse(java.awt.event.MouseEvent)
	 */
	@Override
	public String pickedByMouse(MouseEvent e)
	{
		for(CKDialogChoice ch: choices)
		{
			if(ch.contians(e.getPoint()))
			{
				ch.setSelected(true);
				notifyListeners(ch);
				return ch.getChoice();
			}
		}
		return null;
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKDialogMessage#shouldCloseFromScript(java.lang.String)
	 */
	@Override
	public boolean shouldCloseFromScript(String mess)
	{
		for(CKDialogChoice ch: choices)
		{
			if(ch.getChoice().compareToIgnoreCase(mess)==0)
			{
				ch.setSelected(true);
				notifyListeners(ch);
				return true;
			}
		}
		return false;
	}
	
	

}
