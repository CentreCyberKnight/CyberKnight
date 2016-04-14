package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CKTextAsset extends CKGraphicsAsset
{

	String text;
	Font font;
	Color color;
	protected CKTextAsset(String text, Font font,Color color)
	{
		super("'textAsset"+text+color, "Temp Text Asset");
		this.text = text;
		this.font = font;
		this.color = color;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @return the font
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);
	}

	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx,
			int screeny, ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);
		
	}

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny,
			int frame, int row, ImageObserver observer)
	{
		g.setStroke(color);
		g.setFont(font);
		g.setTextAlign(TextAlignment.CENTER);
		g.strokeText(text, screenx, screeny);
	}

	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);
	}

	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int screenx,
			int screeny, int row, ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);

	}

	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);


	}

	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int screenx,
			int screeny, int frame, ImageObserver observer)
	{
		drawToGraphics(g,screenx,screeny,0,0,observer);
	}

	@Override
	public int getFrames(int row)
	{
		return 1;
	}

	@Override
	public int getRows()
	{
		return 1;
	}

	@Override
	public int getHeight(int row)
	{
		Point off = new Point(0,0);
		Point bound = new Point(0,0);
		
		getDrawBounds(0,0,off,bound);
		return bound.y-off.y;
	}

	@Override
	public int getWidth(int row)
	{
		Point off = new Point(0,0);
		Point bound = new Point(0,0);
		
		getDrawBounds(0,0,off,bound);
		return bound.x-off.x;
	}

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		//http://stackoverflow.com/questions/13015698/how-to-calculate-the-pixel-width-of-a-string-in-javafx
		Text temp = new Text(text);
		temp.setFont(font);
		
		Bounds layout = temp.getLayoutBounds();
		double width = layout.getWidth();
		
		off.x=(int) layout.getMinX();
		off.y=(int) layout.getMinY();
		bounds.x=(int) layout.getMaxX();
		bounds.y=(int) layout.getMaxY();
		// TODO Auto-generated method stub

	}

}
