package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javafx.scene.canvas.GraphicsContext;

public class CKGraphicsText
{
	
	private int xmargin;
	private int ymargin;
	private Font font;
	private Color color;
	private AttributedString aString;
	private String message;
	
	public CKGraphicsText(String text, int xmargin,int ymargin)
	{
		this.message=text;
		this.xmargin=xmargin;
		this.ymargin=ymargin;
		//this.font = (Font) UIManager.get("Label.font");
		this.font = new Font("LucidaSans", Font.PLAIN, 24);
		this.color = Color.DARK_GRAY;
		setAttributedString();
	}
	
	public CKGraphicsText(String text)
	{
		this(text,30,0);
	}

	public String getString() { return message; }
	
	public void setMargin(int newXMargin,int newYMargin) 
	{ 
		xmargin = newXMargin;
		ymargin = newYMargin;
		//setAttributedString();
	}
	public void setFont(Font newFont) 
	{ 
		font=newFont;
		setAttributedString();
	}
	
	public void setColor(Color newColor) 
	{ 
		color=newColor;
		setAttributedString();
	}
	
	
	protected void setAttributedString()
	{
		aString = new AttributedString(message);
	    aString.addAttribute(TextAttribute.FONT,font );
	    aString.addAttribute(TextAttribute.FOREGROUND, color);		
	}
	
	

	public Rectangle drawText(Graphics g, Rectangle clip)
	{
		AttributedCharacterIterator characterIterator = aString.getIterator();
		Graphics2D g2d = (Graphics2D) g;
		
	    FontRenderContext fontRenderContext = g2d.getFontRenderContext();
	    LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator,
	        fontRenderContext);

	    int width = clip.width-xmargin*2;
	    if(width<xmargin)
	    {
	    	return new Rectangle(clip.x,clip.y,0,0);
	    }
	    final int startX = xmargin+clip.x;
	    int y = ymargin+clip.y;

	    while (measurer.getPosition() < characterIterator.getEndIndex())
	    {
	    	int next = measurer.nextOffset(width);
	    	int limit = next;
	    	
	    	
	    	   for (int i = measurer.getPosition()+1; i < next-1; ++i) 
	    	   {
	    	      if (message.charAt(i) == '\n') 
	    	      {
	    	         limit = i;
	    	         //System.out.println(message+"\nfound a slash-n at limit "+limit+"next"+next +"starting "+measurer.getPosition());
	    	         break;
	    	      }
	    	   }
	    	   //System.out.println("exiting text loop " );
	    	   
	    	
	    		    	
	      TextLayout layout = measurer.nextLayout(width, limit, false);
	      float dx = layout.isLeftToRight() ? 0 : (width - layout.getAdvance());
	      y+=layout.getAscent();
	      layout.draw(g2d, startX + dx, y);
	      y += layout.getDescent() + layout.getLeading();
	    }
	//    System.out.println("GraphicsText:"+new Rectangle(clip.x,clip.y,clip.width,y-clip.y)+
	//    		"xmar:"+xmargin+" ymar"+ymargin+" string"+message);		
	    return new Rectangle(clip.x,clip.y,clip.width,y-clip.y);		
	}
	
	
//FIXME FX need to do line wrap here, might move this out of canvas drawing
//and into something that is part of the GUI...
//
	public Rectangle drawText(GraphicsContext g, Rectangle clip)
	{
		g.setFill(javafx.scene.paint.Color.BLACK);
		
	
		g.setFont(javafx.scene.text.Font.font(40));
		
		
		g.fillText(message, clip.x, clip.y+50,clip.width);
		
		int height = (int) (g.getFont().getSize()*2);
		
		return new Rectangle(clip.x,clip.y,clip.width,height);
		
		/*
		AttributedCharacterIterator characterIterator = aString.getIterator();
		//Graphics2D g2d = (Graphics2D) g;
		
		FontMetrics fm = new FontMetrics(g.getFont());
	   FontRenderContext fontRenderContext = g.getFontRenderContext();
	    LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator,
	        fontRenderContext);

	    int width = clip.width-xmargin*2;
	    if(width<xmargin)
	    {
	    	return new Rectangle(clip.x,clip.y,0,0);
	    }
	    final int startX = xmargin+clip.x;
	    int y = ymargin+clip.y;

	    while (measurer.getPosition() < characterIterator.getEndIndex())
	    {
	    	int next = measurer.nextOffset(width);
	    	int limit = next;
	    	
	    	
	    	   for (int i = measurer.getPosition()+1; i < next-1; ++i) 
	    	   {
	    	      if (message.charAt(i) == '\n') 
	    	      {
	    	         limit = i;
	    	         //System.out.println(message+"\nfound a slash-n at limit "+limit+"next"+next +"starting "+measurer.getPosition());
	    	         break;
	    	      }
	    	   }
	    	   //System.out.println("exiting text loop " );
	    	   
	    	
	    		    	
	      TextLayout layout = measurer.nextLayout(width, limit, false);
	      float dx = layout.isLeftToRight() ? 0 : (width - layout.getAdvance());
	      y+=layout.getAscent();
	      layout.draw(g, startX + dx, y);
	      y += layout.getDescent() + layout.getLeading();
	    }
	//    System.out.println("GraphicsText:"+new Rectangle(clip.x,clip.y,clip.width,y-clip.y)+
	//    		"xmar:"+xmargin+" ymar"+ymargin+" string"+message);		
	    return new Rectangle(clip.x,clip.y,clip.width,y-clip.y);
	    */		
	}
	
}
