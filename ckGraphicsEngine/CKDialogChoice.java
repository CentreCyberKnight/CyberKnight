package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;



public class CKDialogChoice
{
	CKGraphicsText text;
	int id;
	Rectangle bounds = new Rectangle(0,0,0,0);
	boolean selected = false;
	
	private static CKDialogChoice NULL_CHOICE;
	
	public static CKDialogChoice getNullInstance()
	{
		if(NULL_CHOICE==null)
		{
			NULL_CHOICE= new CKDialogChoice(0," ");
		}
		return NULL_CHOICE;
	}
	
	public CKDialogChoice(int id,String message)
	{
		this.id = id;
		this.text = new CKGraphicsText(message);
		
	}
	
	public String getChoice()
	{
		return text.getString();
	}
	
	public int getID() { return id; }
	
	public void setMargins(int xmargin, int ymargin)
	{
		this.text.setMargin(xmargin,ymargin);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DialogChoice"+text.getString();
	}

	public Rectangle drawText(Graphics g, Rectangle clip)
	{
		bounds= text.drawText(g, clip);
		return bounds;
	}
	
	public void outlineText(Graphics g, Color c)
	{
		g.setColor(c);
		Graphics2D gd = (Graphics2D)g;
		gd.draw(bounds);
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		if(selected)
		{
			this.text.setColor(Color.RED);
		}
		else
		{
			this.text.setColor(Color.DARK_GRAY);
		}
	}

	public boolean contians(Point point)
	{
		return bounds.contains(point);
	}


	

	
}
