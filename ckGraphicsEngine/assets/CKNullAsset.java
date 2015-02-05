package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;


final public class CKNullAsset extends CKGraphicsAsset
{

	/**
	 * This is here for the XMLEncoder, call getNullAsset instead.
	 */
	public CKNullAsset()
	{
		super("null","NULL ASSET");
		setClean();
	}
	
	private static CKNullAsset nullAsset=null;

	final public static CKNullAsset getNullAsset()
	{
		if(nullAsset==null)
		{
			nullAsset = new CKNullAsset();
		}
		
		return nullAsset;
	}
	
	

	@Override
	final public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		// do nothing

	}

	@Override
	final public void drawToGraphics(Graphics g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		// do nothing

	}

	@Override
	final public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		// do nothing

	}

	@Override
	final public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		// do nothing
	}

	@Override
	final public int getFrames(int row)
	{
		return 1;
	}

	@Override
	final public int getRows()
	{
		return 1;
	}

	@Override
	final public int getHeight(int row)
	{
		return 0;
	}

	@Override
	final public int getWidth(int row)
	{
		return 0;
	}

	

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		off.x=0;
		off.y=0;
		bounds.x=0;
		bounds.y=0;
		
	}

}
