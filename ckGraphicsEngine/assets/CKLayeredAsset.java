package ckGraphicsEngine.assets;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.XMLEncoder;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.UnknownAnimationError;
import javafx.scene.canvas.GraphicsContext;



/**Creates an Asset by layering an ordered set of other assets.
 * @author dragonlord
 *
 */
public class CKLayeredAsset extends CKCompositeAsset
{

	ArrayList<CKGraphicsAsset> assets;

	public CKLayeredAsset()
	{
		this("","");
	}
	
	public CKLayeredAsset(String description)
	{
		this("", description);
	}

	public CKLayeredAsset(String aid, String description)
	{
		super(aid, description);
		assets = new ArrayList<CKGraphicsAsset>();
	}

	public void add(String s)
	{
		addAsset(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(s));
	}
	
	public void addAsset(CKGraphicsAsset img)
	{
		assets.add(img);
	}

	class LayeredAssetPersistenceDelegate extends DefaultPersistenceDelegate
	{
	    protected void initialize(Class type, Object oldInstance,
	                              Object newInstance, Encoder out) 
	    {
	        super.initialize(type, oldInstance,  newInstance, out);

	        CKLayeredAsset asset = (CKLayeredAsset) oldInstance;

	        Iterator<CKGraphicsAsset> iter = asset.iterator();
	        while(iter.hasNext())
	        {
	            out.writeStatement(new java.beans.Statement(oldInstance,
	                              "add", // Could also use "addElement" here.
	                              new Object[]{iter.next().getAID()}) );
	        }
	    }
	}
	
		/* (non-Javadoc)
	 * @see ckGraphicsEngine.assets.CKGraphicsAsset#writeToStream(java.io.OutputStream)
	 */
	@Override
	public void writeToStream(OutputStream out)
	{

		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.setPersistenceDelegate(getClass(), new LayeredAssetPersistenceDelegate());
		e.writeObject(this);
		e.close();
		
	
	}

	

	
	
	
	// A helper function to swap layers in the asset,
	public void swapLayers(int pos1, int pos2)
	{
		CKGraphicsAsset tmp = assets.get(pos1);
		assets.set(pos1, assets.get(pos2));
		assets.set(pos2, tmp);
	}

	/* will need to add more here for the editor people */

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		for (int i = assets.size() - 1; i >= 0; i--)
		{

			assets.get(i).drawToGraphics(g, screenx, screeny, frame, 0,
					observer);
		}
	}
	
	@Override
	public void drawToGraphics(GraphicsContext g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		for (int i = assets.size() - 1; i >= 0; i--)
		{

			assets.get(i).drawToGraphics(g, screenx, screeny, frame, 0,
					observer);
		}
	}

	// draw all of the layers laid out.
	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		for (int i = 0; i < assets.size(); i++)
		{
			CKGraphicsAsset img = assets.get(i);
			img.drawPreviewToGraphics(g, screenx, screeny, observer);// MKB
			screeny += img.getHeight(0);
		}
	}

	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx, int screeny,
			ImageObserver observer)
	{
		for (int i = 0; i < assets.size(); i++)
		{
			CKGraphicsAsset img = assets.get(i);
			img.drawPreviewToGraphics(g, screenx, screeny, observer);// MKB
			screeny += img.getHeight(0);
		}
	}
	
	private CKGraphicsAsset getAsset(int row)
	{
		try
		{
			row = row % assets.size();
			return assets.get(row);
		} catch (ArithmeticException e)
		{
			return CKNullAsset.getNullAsset();
		}
	}

	// draws the ith layer
	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		getAsset(row)
				.drawPreviewRowToGraphics(g, screenx, screeny, 0, observer);
	}

	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx,
			int screeny, int frame, ImageObserver observer)
	{

		for (CKGraphicsAsset img : assets)
		{
			img.drawToGraphics(g, screenx, screeny, frame, 0, observer);
			screeny = img.getHeight(0);
		}

	}
	
	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		getAsset(row)
				.drawPreviewRowToGraphics(g, screenx, screeny, 0, observer);
	}

	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int screenx,
			int screeny, int frame, ImageObserver observer)
	{

		for (CKGraphicsAsset img : assets)
		{
			img.drawToGraphics(g, screenx, screeny, frame, 0, observer);
			screeny = img.getHeight(0);
		}

	}


	@Override
	public int getFrames(int row)
	{
		int max = 0;
		int val;
		for (CKGraphicsAsset img : assets)
		{
			val = img.getFrames(0);
			if (val > max)
			{
				max = val;
			}
		}

		return max;// getAsset(row).getFrames(0);
	}

	@Override
	public int getRows()
	{
		return 1;// preSpriteLayers.size()+postSpriteLayers.size();
	}

	@Override
	public int getHeight(int row)
	{
		return getAsset(row).getHeight(0);
	}

	@Override
	public int getWidth(int row)
	{
		return getAsset(row).getWidth(0);
	}

	public static void main(String[] args)
	{
	}

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		boolean first = true;

		for (CKGraphicsAsset img : assets)
		{
			if (first)
			{
				img.getDrawBounds(frame, row, off, bounds);
				first = false;
			} else
			{
				Point offtemp = new Point(0, 0);
				Point boundstemp = new Point(0, 0);
				img.getDrawBounds(frame, row, offtemp, boundstemp);
				off.x = Math.min(offtemp.x, off.x);
				off.y = Math.min(offtemp.y, off.y);
				bounds.x = Math.max(boundstemp.x, bounds.x);
				bounds.y = Math.max(boundstemp.y, bounds.y);

			}
		}

	}

	@Override
	public Iterator<CKGraphicsAsset> iterator()
	{
		return assets.iterator();
	}

	@Override
	public void removeAsset(CKGraphicsAsset asset)
	{
		assets.remove(asset);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ckGraphicsEngine.CKAgrrgateAssetInterface#moveUpAsset(ckGraphicsEngine
	 * .CKGraphicsAsset)
	 */
	@Override
	public void moveUpAsset(CKGraphicsAsset asset)
	{
		for (int i = 0; i < assets.size(); i++)
		{
			if (assets.get(i) == asset)
			{
				if (i != 0)
				{
					Collections.swap(assets, i, i - 1);
				}
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ckGraphicsEngine.CKAgrrgateAssetInterface#moveDownAsset(ckGraphicsEngine
	 * .CKGraphicsAsset)
	 */
	@Override
	public void moveDownAsset(CKGraphicsAsset asset)
	{
		for (int i = 0; i < assets.size(); i++)
		{
			if (assets.get(i) == asset)
			{
				if (i != assets.size() - 1)
				{
					Collections.swap(assets, i, i + 1);
				}
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#supportsOrdering()
	 */
	@Override
	public boolean supportsOrdering()
	{
		return true;
	}

	
	@Override
	public int getAnimationLength(String animation)
			throws UnknownAnimationError
	{
		return 0;
	}

}
