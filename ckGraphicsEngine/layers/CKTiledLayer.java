package ckGraphicsEngine.layers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.CKNullAsset;

public class CKTiledLayer extends CKGraphicsLayer
{

	public enum OffsetType 
	{
		
		FIXED("FIXED")
		{
			public Point calcOffset(CKPosition p,CKCoordinateTranslator trans,int frame )
			{
				return new Point((int)(p.getX()),(int)(p.getY()));
			}			
		},
		CAMERA_FOLLOW("CAMERA_FOLLOW")
		{
			public Point calcOffset(CKPosition p,CKCoordinateTranslator trans,int frame )
			{
				Point p2 = trans.convertMapToScreen(0, 0);
				
				return new Point((int)(p2.x + p.getX()),
						         (int)(p2.y + p.getY()));
			}	
		},
		DRIFT_INDEPENANT("DRIFT_INDEPENDENT")
		{
			public Point calcOffset(CKPosition p,CKCoordinateTranslator trans,int frame )
			{
				CKPosition scaled = p.scaleVector(frame);
				return new Point((int)(scaled.getX()),(int)(scaled.getY()));
			}			
		},
		DRIFT_W_CAMERA("DRIFT_W_CAMERA")
		{
			public Point calcOffset(CKPosition p,CKCoordinateTranslator trans,int frame )
			{
			Point p2 = trans.convertMapToScreen(0, 0);
			CKPosition scaled = p.scaleVector(frame);
			return new Point((int)(scaled.getX()+p2.x),(int)(scaled.getY()+p2.y));
			}			
		},
		NONE("NONE");	
	
		
	private final String name;	
	//need to add data to these to make print pretty and recognize themselves.
	OffsetType(String name) {this.name=name;}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){ return this.name;}
	
	/**
	 * Returns a Point that contains the offset the layered tile should be laid out in
	 * @param p      - CKPosition of the the original offset
	 * @param trans  - CKcoordinateTranslator for information about the viewWindow 
	 * @param frame  - what frame of animation we are on
	 * @return
	 */
	public Point calcOffset(CKPosition p,CKCoordinateTranslator trans,int frame )
	{
		return new Point(0,0);
		
	}	
	

	/**
	 * gets the offsettype that corresponds to the string name of the type.
	 * @param name string representing the offsettype
	 * @return offsettype for the string
	 */
	static public OffsetType getOffsetType(String name)
	{
		for (OffsetType t:OffsetType.values())
		{
			if(t.toString().equals(name))
			{
				return t;
			}
		}
		return OffsetType.NONE;
	}
	
	};
	
	
	CKGraphicsAsset asset;
  	CKPosition pos;
	OffsetType offset;
	

	
	public CKTiledLayer(int d,String id,String descr,OffsetType type)
	{
		super(d,id,descr);
		asset=CKNullAsset.getNullAsset();
		pos=new CKPosition(0,0,0,0);
		setVisible(true);
		offset=type;
	}


	public CKTiledLayer(int d,String descr)
	{
		this(d,"",descr,OffsetType.NONE);
	}
	
	public CKTiledLayer(int d)
	{
		this(d, "","", OffsetType.NONE);
	}
	
	public CKTiledLayer()
	{
		this(CKGraphicsLayer.GROUND_LAYER);
	}

	
	public void reset()
	{
		//I'm good.
	}
	
	
	public void addAsset(CKPosition p,CKGraphicsAsset t)
	{
		//what should pos d in this case....
		pos=p;
		asset = t;  		
	}
	
	public void setOffset(OffsetType offset)
	{
		this.offset=offset;
	}
	
	
	/**
	 * @return the offset
	 */
	public OffsetType getOffset()
	{
		return offset;
	}


	@Override
	public void drawLayerToGraphics(Graphics g, int frame,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		
			Point p = offset.calcOffset(pos, translator, frame);
			int assetWidth = asset.getWidth(0);
			int assetHeight = asset.getHeight(0);
			
			int startX = (p.x % assetWidth) - assetWidth;
			int startY = (p.y % assetHeight) - assetHeight;
			
			int width = translator.getScreenWidth()+assetWidth;
			int height = translator.getScreenHeight()+ assetHeight;
			
			
//			System.out.println("Here is Point"+p+" Here is the width/height"+width+","+height);

			
			for(int x = startX;x<width;x+=assetWidth)
			{
				for(int y = startY;y<height;y+=assetHeight)
				{
					asset.drawToGraphics(g,x,y, frame, 0, observer);
					
				}
			}

	}

	@Override
	public void drawLayerRowToGraphics(Graphics g, int frame, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		drawLayerToGraphics(g,frame,observer,translator);
	}

	@Override
	public void drawLayerTileToGraphics(Graphics g, int frame, int x, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		drawLayerToGraphics(g,frame,observer,translator);
	}

	
	

	@Override
	public void drawLayerToGraphics(GraphicsContext g, int frame,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		
			Point p = offset.calcOffset(pos, translator, frame);
			int assetWidth = asset.getWidth(0);
			int assetHeight = asset.getHeight(0);
			
			int startX = (p.x % assetWidth) - assetWidth;
			int startY = (p.y % assetHeight) - assetHeight;
			
			int width = translator.getScreenWidth()+assetWidth;
			int height = translator.getScreenHeight()+ assetHeight;
			
			
//			System.out.println("Here is Point"+p+" Here is the width/height"+width+","+height);

			
			for(int x = startX;x<width;x+=assetWidth)
			{
				for(int y = startY;y<height;y+=assetHeight)
				{
					asset.drawToGraphics(g,x,y, frame, 0, observer);
					
				}
			}

	}

	@Override
	public void drawLayerRowToGraphics(GraphicsContext g, int frame, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		drawLayerToGraphics(g,frame,observer,translator);
	}

	@Override
	public void drawLayerTileToGraphics(GraphicsContext g, int frame, int x, int y,
			ImageObserver observer, CKCoordinateTranslator translator)
	{
		drawLayerToGraphics(g,frame,observer,translator);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		CKImageAsset backgroundImg=null;
	/*	try
		{
			backgroundImg = CKImageAsset.readImage("stone",512, 512,1,1,
					TileType.BASE,"images/darkClouds.png");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		CKTiledLayer layer = new CKTiledLayer(-4000,"stoneLayer","stone layer",OffsetType.DRIFT_INDEPENANT);
		
		layer.addAsset(new CKPosition(20,-10,0,0), backgroundImg);
		
		/* Container c = frame.getContentPane();*/    
		CKLayerViewer view=new CKLayerViewer(1,layer,new Dimension(256,256),new Dimension(20,20));
		JFrame frame = new JFrame();
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//Thread animator = new Thread(test);
		//animator.start();
		   frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	}
	);


	}


	@Override
	public void changeHeight(int x, int y, double heightDiff)
	{
	}

	

	@Override
	public void addInstance(CKAssetInstance t)
	{
		addAsset(t.getPosition(),t.getAsset());
		
	}


	@Override
	public boolean removeInstance(CKAssetInstance t)
	{
		if(t.getAsset()==asset)
		{
			addAsset(pos,CKNullAsset.getNullAsset());
			return true;
		}
		else { 	return false; }
	}


	@Override
	public boolean getLayerBounds(Point minPoint, Point maxPoint)
	{
		minPoint.x=0;
		minPoint.y=0;
		maxPoint.x=1;
		maxPoint.y=1;
		return false;
	}


	@Override
	public void removeAllInstances()
	{
		addAsset(pos,CKNullAsset.getNullAsset());
		
	}


	@Override
	public Iterator<CKAssetInstance> iterator()
	{
		Vector<CKAssetInstance> vec=new Vector<CKAssetInstance>();
		vec.add(new CKAssetInstance(pos,asset,0));
		return vec.iterator();
	}
	
	
	
	
	
	
	
	
}
