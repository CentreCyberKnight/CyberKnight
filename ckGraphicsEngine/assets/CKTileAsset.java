package ckGraphicsEngine.assets;


import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import static ckGraphicsEngine.CKGraphicsConstants.*;

/**
 * Creates an asset which uses an ordered set assets and height data to create a new asset 
 * @author dragonlord
 *
 */
@Deprecated
public class CKTileAsset extends  CKCompositeAsset
{
	
	
	class CKTileNode
	{
		CKGraphicsAsset asset;
		double height;
		
		public CKTileNode()
		{
			asset = null; //new CKTileImage(32,16,1);
			height =0;
		}
		
		public CKTileNode(CKGraphicsAsset i, double h)
		{
			asset=i;
			height=h;
		}
		
			
	}
	
	Vector<CKTileNode> layers;
	
	
	public CKTileAsset(String desc)
	{
		this("",desc);
	}
	
	protected CKTileAsset(String a, String desc)
	{
		super(a,desc);
		layers = new Vector<CKTileNode>();
	}

	public void addAsset(CKGraphicsAsset img,double height)
	{
		layers.add(new CKTileNode(img,height));
	}
	
	
		
	
	final private void drawLayerToGraphics(Graphics g,int screenx,int screeny,
            int frame,int row,ImageObserver observer)
	{
		for(int i = layers.size()-1;i>=0;i--)
		{
			CKTileNode n = layers.get(i);
            n.asset.drawToGraphics(g, screenx, (int)(screeny-n.height*HEIGHT_MULTIPLIER), frame, 0,observer);
        }		
	}

	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		boolean first=true;
		
		for(CKTileNode n: layers)
        {
			if(first)
			{
				n.asset.getDrawBounds(frame, row, off, bounds);
				off.y-=n.height*HEIGHT_MULTIPLIER;
				bounds.y-=n.height*HEIGHT_MULTIPLIER;
				
				first=false;
			}
			else
			{
				Point offtemp =new Point(0,0);
				Point boundstemp=new Point(0,0);
				n.asset.getDrawBounds(frame, row, offtemp, boundstemp);

				offtemp.y-=n.height*HEIGHT_MULTIPLIER;
				boundstemp.y-=n.height*HEIGHT_MULTIPLIER;				
				
				off.x=Math.min(offtemp.x,off.x);
				off.y=Math.min(offtemp.y,off.y);
				bounds.x=Math.max(boundstemp.x,bounds.x);
				bounds.y=Math.max(boundstemp.y,bounds.y);


			}
        }
		
	}
	@Override    
    public void drawToGraphics (Graphics g,int screenx,int screeny,
            int frame,int row,ImageObserver observer)
    {
		drawLayerToGraphics(g,screenx,screeny, frame,row,observer);
    }
    

	//draw all of the layers laid out.
	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		CKGraphicsAsset img;
		
		//for(int i = layers.size()-1;i>=0;i--)
		for(int i = 0;i<layers.size();i++)
		{
			img=layers.get(i).asset;
			img.drawPreviewToGraphics(g, screenx, screeny,observer);
            screeny+=img.getHeight(0);
		}
	}

	
	private CKGraphicsAsset getAsset(int row)
	{
		try
		{
			row = row %layers.size();
			return layers.get(row).asset;
		}
		catch(ArithmeticException e)
		{
			return CKNullAsset.getNullAsset();
		}
	}
	
	
	//draws the ith layer
	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		getAsset(row).drawPreviewRowToGraphics(g, screenx, screeny, 0, observer);
	}


	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{

		for(CKTileNode n: layers)
        {
            n.asset.drawToGraphics(g, screenx, screeny,frame,0,observer);
            screeny=n.asset.getHeight(0);
        }		

	
	}


	@Override
	public int getFrames(int row)
	{
		int max = 0;
		int val;	
		for(CKTileNode n: layers)
        {
			val = n.asset.getFrames(0);
            if(val > max)
            { max = val;}
        }		
		
		return max;//getAsset(row).getFrames(0);
	}


	@Override
	public int getRows()
	{
		return 0;//preSpriteLayers.size()+postSpriteLayers.size();
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
		///methods for the agregateasset interface
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#iterator()
	 */
	@Override
	public Iterator<CKGraphicsAsset> iterator()
	{
		return new TileIterator(layers.iterator());
	}

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#moveUpAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void moveUpAsset(CKGraphicsAsset asset)
	{
		for(int i = 0;i<layers.size();i++)
		{
			CKGraphicsAsset img=layers.get(i).asset;
			if(img == asset)
			{
				if(i!=0)
				{
					Collections.swap(layers,i, i-1);
				}
				return;
			}
		}	
	}
	

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#moveDownAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void moveDownAsset(CKGraphicsAsset asset)
	{
		for(int i = 0;i<layers.size();i++)
		{
			CKGraphicsAsset img=layers.get(i).asset;
			if(img == asset)
			{
				if(i!=layers.size()-1)
				{
					Collections.swap(layers,i, i+1);
				}
					return;	
			}
		}	
	}

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#removeAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void removeAsset(CKGraphicsAsset asset)
	{
		for(CKTileNode n : layers)
		{
			if(n.asset==asset)
			{
				layers.remove(n);
				return;
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#addHeight(ckGraphicsEngine.CKGraphicsAsset, int)
	 */
	@Override
	public void addHeight(CKGraphicsAsset asset,int increment)
	{
		for(int i = 0;i<layers.size();i++)
		{
			CKGraphicsAsset img=layers.get(i).asset;
			if(img == asset)
			{
				CKTileNode n = layers.get(i);
				n.height +=increment;
				//because we are using pointers this should be ok
				return;	
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#supportsHeight()
	 */
	@Override
	public boolean supportsHeight() { return true; }
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#supportsOrdering()
	 */
	@Override
	public boolean supportsOrdering() { return true; }
	
	
	
	
	
	
	private class TileIterator implements Iterator<CKGraphicsAsset>
	{
		Iterator<CKTileNode> iter;
		
		public TileIterator(Iterator<CKTileNode> i)
		{
			iter=i;
		}
		
		
		@Override
		public boolean hasNext()
		{
			
			return iter.hasNext();
		}

		@Override
		public CKGraphicsAsset next()
		{
			
			return iter.next().asset;
		}

		@Override
		public void remove()
		{
			iter.remove();			
		}
		
	}
	

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAgrrgateAssetInterface#addAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void addAsset(CKGraphicsAsset asset)
	{
		addAsset(asset,0);
		
	}
		



	
	
	
	
}
