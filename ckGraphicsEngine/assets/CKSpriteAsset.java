package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.UnknownAnimationError;

public class CKSpriteAsset extends CKCompositeAsset
{
	
	
	
	public static class SpriteNode
	{
		CKGraphicsAsset asset;
		String assetID;
		String name;
		
		public SpriteNode()
		{
			asset=CKNullAsset.getNullAsset();
			assetID=asset.getAID();
			name="None";
		}
		
		public SpriteNode(CKGraphicsAsset s,String n)
		{
			asset=s;
			assetID=s.getAID();
			name=n;
		}

		/**
		 * @return the assetID
		 */
		public String getAssetID()
		{
			return assetID;
		}

		/**
		 * @param assetID the assetID to set
		 */
		public void setAssetID(String assetID)
		{
			this.assetID = assetID;
			asset =CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
		}

		/**
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name)
		{
			this.name = name;
		}
		
		
		
	}
	
	class SpriteIterator implements Iterator<CKGraphicsAsset>
	{
		Iterator<SpriteNode> iter;
		
		public SpriteIterator(Iterator<SpriteNode> n)
		{
			iter = n;
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
	
	//HashMap<String,CKGraphicsAsset> map;
	//ArrayList<CKGraphicsAsset> array;
	ArrayList<SpriteNode> list;
	int unique=1;
	
	
	public CKSpriteAsset()
	{
		this("","");
	}

	public CKSpriteAsset(String desc)
	{
		this("",desc);
	}
		
	public CKSpriteAsset( String AID,String desc)
	{
		super(AID,desc);
		list = new ArrayList<SpriteNode>();
		unique=1;
	}
	
/*
 * Mainly for XMLEncoder
 * 
 */
	
	
	
	public void add(SpriteNode n)
	{
		list.add(n);
	}
	
	


	/**
	 * @return the list
	 */
	public ArrayList<SpriteNode> getList()
	{
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<SpriteNode> list)
	{
		this.list = list;
	}

	public void addAnimation(String animation,CKGraphicsAsset asset)
	{
		list.add(new SpriteNode(asset,animation));
	}
	
		
	private CKGraphicsAsset getAnimaiton(String animation)  throws UnknownAnimationError 
	{
		Iterator<SpriteNode> iter = list.iterator();
		while(iter.hasNext())
		{
			SpriteNode node = iter.next();
			if(node.name.equals(animation))
			{
				return node.asset;
			}
			
		}
		throw new UnknownAnimationError(animation);
	}
	
	public int getRowIndex(String animation) throws UnknownAnimationError 
	{
		Iterator<SpriteNode> iter = list.iterator();
		int row = 0;
		while(iter.hasNext())
		{
			SpriteNode node = iter.next();
			if(node.name.equals(animation))
			{
				return row;
			}
			row++;
		}
		throw new UnknownAnimationError(animation);
	}
	
	public int getAnimationLength(String animation) throws UnknownAnimationError
	{
		CKGraphicsAsset asset=getAnimaiton(animation);
		return asset.getFrames(0);	
	}
	
		
	
	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		for(SpriteNode node:list)
		{
			node.asset.drawPreviewRowToGraphics(g, screenx, screeny, 0, observer);
			screeny+=node.asset.getHeight(0);			
		}
	}

	@Override
	public void drawToGraphics(Graphics g, int screenx, int screeny, int frame,
			int row, ImageObserver observer)
	{
		if( list.size() > 0)
		{
			int r = row % list.size();
			list.get(r).asset.drawToGraphics(g, screenx, screeny, frame,0,observer);
		}
	}


	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		if(list.size()>0)
		{
			int r = row % list.size();
			list.get(r).asset.getDrawBounds(frame, row, off, bounds);
		}
		else
		{
			off.x=0;
			off.y=0;
			bounds.x=0;
			bounds.y=0;
		}
		
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.assets.CKGraphicsAsset#drawPreviewRowToGraphics(java.awt.Graphics, int, int, int, java.awt.image.ImageObserver)
	 */
	@Override
	public void drawPreviewRowToGraphics(Graphics g, int screenx, int screeny,
			int row, ImageObserver observer)
	{
		int r = row % list.size();
		list.get(r).asset.drawPreviewRowToGraphics(g, screenx, screeny, 0,observer);
	}

	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int screenx, int screeny,
			int frame, ImageObserver observer)
	{
		for(SpriteNode node:list)
		{
			node.asset.drawToGraphics(g, screenx, screeny, frame,0, observer);
			screeny+=node.asset.getHeight(0);			
		}
	}

	@Override
	public int getFrames(int row)
	{   
		try
		{
		return list.get(row).asset.getFrames(row);
		}
		catch(IndexOutOfBoundsException e)
		{
		return 0;
		}
	}
	
	public int getFrames(String animation) throws UnknownAnimationError
	{
		return getAnimaiton(animation).getFrames(0);
		
	}
	
	
	@Override
	public int getRows()
	{
		return list.size();
	}

	

	@Override
	public int getHeight(int row)
	{
		if(list.size()==0)
		{
			return 0;
		}
		int r = row % list.size();
		return list.get(r).asset.getHeight(0);
	}

	@Override
	public int getWidth(int row)
	{
		if(list.size()>0)
		{
			int r = row % list.size();
			return list.get(r).asset.getWidth(0);
		}
		else
		{
			return 0;
		}
	}
	
		


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#iterator()
	 */
	@Override
	public Iterator<CKGraphicsAsset> iterator()
	{
		return new SpriteIterator(list.iterator());
	}



	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#addAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void addAsset(CKGraphicsAsset asset)
	{
		
		addAnimation("Please Name Me"+unique++,asset);
		
	}



	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#removeAsset(ckGraphicsEngine.CKGraphicsAsset)
	 */
	@Override
	public void removeAsset(CKGraphicsAsset asset)
	{
		Iterator<SpriteNode> iter = list.iterator();
		while(iter.hasNext())
		{
			SpriteNode node = iter.next();
			if(node.asset == asset)
			{
				iter.remove();
				return;
			}
			
		}
	}

		
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#supportsNaming()
	 */
	public boolean supportsNaming() { return true; }
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#getAssetName(ckGraphicsEngine.CKGraphicsAsset)
	 */
	public String getAssetName(CKGraphicsAsset asset)
	{
		for(SpriteNode node: list)
		{
	        if(node.asset==asset)
	        {
	        	return node.name;
	        }
	    }
		return null;
	}

	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKCompositeAsset#renameAsset(ckGraphicsEngine.CKGraphicsAsset, java.lang.String)
	 */
	public void renameAsset(CKGraphicsAsset asset,String newName)
	{
		for(SpriteNode node: list)
		{
	        if(node.asset==asset)
	        {
	        	node.name = newName;
	        }
	    }
	}


	
}	

