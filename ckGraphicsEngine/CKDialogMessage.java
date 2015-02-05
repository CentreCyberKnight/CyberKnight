package ckGraphicsEngine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import ckCommonUtils.CKEntitySelectedListener;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKNullAsset;

public class CKDialogMessage 
{
	CKEntitySelectedListener<CKDialogChoice> listener;
	CKGraphicsAsset asset;
	//CKGraphicsText message;
	CKDialogChoice message;
	//Rectangle bounds = new Rectangle(0,0,0,0);
	protected int xmargin;
	protected int ymargin;
	private int frame;
	
	public CKDialogMessage(String message, CKGraphicsAsset asset, int feedbackID)
	{
		
		this.asset=asset;
		this.xmargin = 20;
		this.ymargin = 0;
		this.message=new CKDialogChoice(feedbackID,message);
		this.message.setMargins(xmargin,ymargin);
		frame = 0;
	}
	

	public CKDialogMessage(String message,CKGraphicsAsset asset)
	{
		this(message,asset,0);
	}
	

	public CKDialogMessage(String message)
	{
		this(message,CKNullAsset.getNullAsset(),0);
	}
	
	
	
	/**Draws the asset to the left side of the drawable area
	 * 
	 * @param g
	 * @param clip
	 * @param observer
	 * @return  the bounds used to draw the asset
	 * 
	 **/
	protected Rectangle drawAsset(Graphics g,  Rectangle clip,ImageObserver observer)
	{
		//TODO should return rectangle...
		//Might want to generalize this to do either side or the center
		Point UL = new Point();
		Point LR = new Point();
		
		asset.getDrawBounds(0, 0, UL, LR);
		int awidth  = LR.x-UL.x;
		int aheight = LR.y-UL.y;
		
		int x = xmargin-UL.x +clip.x;
		int y = (clip.height-aheight)/2 - UL.y+clip.y;
		if(awidth >0)
		{
			asset.drawToGraphics(g, x, y, frame++, 0, observer);
		}
		else
		{
			awidth = 0-awidth;
		}
		return new Rectangle(clip.x,clip.y,	awidth+2*xmargin,clip.height);
	}
	
	
	
	/**Will create the rectangle formed by the difference of subtracting the smaller from the larger.  Assumes that one of the edges will match
	 * @param bigger
	 * @param smaller
	 * @return
	 */
	protected static Rectangle createDiffRect(Rectangle bigger,Rectangle smaller)
	{
		//System.out.print("Bigger:"+bigger+" smaller:"+smaller);
		Rectangle ret = null;
		if(!bigger.contains(smaller))
		{
			ret= new Rectangle(0,0,0,0);
		}
			
		//shave off of the ends
		if(smaller.y > bigger.y) //shave off bottom
		{
			ret = new Rectangle(bigger.x,bigger.y,bigger.width, bigger.height-smaller.height);
		}
		else if(smaller.x > bigger.x) //shave off right
		{
			ret = new Rectangle(bigger.x,bigger.y,bigger.width-smaller.width,bigger.height);
		}
		//now must be different starting point
		else if (bigger.width>smaller.width) //shave off left
		{
			ret = new Rectangle(bigger.x+smaller.width,bigger.y,
					 bigger.width-smaller.width,bigger.height);
		}
		else //shave off bottom
		{
			ret = new Rectangle(bigger.x,bigger.y+smaller.height,
					 bigger.width ,bigger.height-smaller.height);
		}	
		//System.out.println("Solution:"+ret);
		return ret;
	}
	
	
	public Rectangle drawMessage(Graphics g, Rectangle drawableArea,ImageObserver observer)
	{		
		Rectangle bounds = drawAsset(g,drawableArea,observer);
		return message.drawText(g, createDiffRect(drawableArea,bounds));
	}
	
	
	/**
	 * passes a mouse event to the dialog and asks it it should close this dialog
	 * @param e
	 * @return true if the dialog should close
	 */
	public boolean shouldCloseFromMouse(MouseEvent e)
	{
		if(message.contians(e.getPoint()))
		{
			notifyListeners(this.message);
			return true;
		}
		return false;
	}
	
	/**
	 * passes a mouse event to the dialog and asks it it should close this dialog
	 * @param e
	 * @return message selected or null if none was selected.
	 */
	public String pickedByMouse(MouseEvent e)
	{
		if(message.contians(e.getPoint()))
		{
			notifyListeners(this.message);
			return message.getChoice();
		}
		return null;
	}	
	
	
	public boolean shouldCloseFromScript(String mess)
	{
		if(message.getChoice().compareToIgnoreCase(mess)==0)
		{
			notifyListeners(this.message);
			return true;
		}
		return false;
	}
	
	
	protected void notifyListeners(CKDialogChoice ch)
	{
		if (listener != null)
			{
				listener.entitySelected(ch);
			}
	}
	
	public void replaceEventListener(CKEntitySelectedListener<CKDialogChoice> listener)
	{
		this.listener = listener;		
	}
	
	
}
