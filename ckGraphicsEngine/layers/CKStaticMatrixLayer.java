package ckGraphicsEngine.layers;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.Vector;

import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKNullAsset;
import javafx.scene.canvas.GraphicsContext;

public class CKStaticMatrixLayer extends CKGraphicsLayer
{
	
	
	class CKTileNode
	{
		CKGraphicsAsset img;
		double height;
		
		public CKTileNode()
		{
			img = null; //new CKTileImage(32,16,1);
			height =0;
		}
		
		public CKTileNode(CKGraphicsAsset i, double h)
		{
			img=i;
			height=h;
		}
		
			
	}
    CKTileNode[][] tileMatrix;
    int mapWidth;
    int mapHeight;
	
	
	public CKStaticMatrixLayer()
	{
		this("","",1,1,0);	
	}

	
	/**
	 * Creates a map to start with.  Can be altered later.  Used in designing a new map.
	 * @param tile_width
	 * @param tile_height
	 * @param mapWidth
	 * @param mapHeight
	 */
	public CKStaticMatrixLayer(String id,String desc, int mapWidth,int mapHeight,int layerDepth)
	{
		super(layerDepth,id,desc);
		tileMatrix=new CKTileNode[mapWidth][mapHeight];
		
		//CKTileNode n = new CKTileNode(CKNullAsset.getNullAsset(),0);
		CKTileNode n = new CKTileNode(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("highlight"),0);
		
		this.mapWidth=mapWidth;
		this.mapHeight=mapHeight;
				
		for(int i=0;i<mapWidth;i++)
			for(int j=0;j<mapHeight;j++)
			{
				tileMatrix[i][j]=n;
			}
		setVisible(true);
	}
	
	
	/**
	 * @return the mapWidth
	 */
	public int getMapWidth()
	{
		return mapWidth;
	}


	/**
	 * @param mapWidth the mapWidth to set
	 */
	public void setMapWidth(int mapWidth)
	{
		this.mapWidth = mapWidth;
		tileMatrix=new CKTileNode[mapWidth][mapHeight];
//		CKTileNode n = new CKTileNode(CKNullAsset.getNullAsset(),0);
		CKTileNode n = new CKTileNode(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("highlight"),0);
		for(int i=0;i<mapWidth;i++)
			for(int j=0;j<mapHeight;j++)
			{
				tileMatrix[i][j]=n;
			}
	}


	/**
	 * @return the mapHeight
	 */
	public int getMapHeight()
	{
		return mapHeight;

	}


	/**
	 * Side-effect will resize the matrix and set it with all new values.
	 * @param mapHeight the mapHeight to set
	 */
	public void setMapHeight(int mapHeight)
	{
		this.mapHeight = mapHeight;
		CKTileNode n = new CKTileNode(CKNullAsset.getNullAsset(),0);
		tileMatrix=new CKTileNode[mapWidth][mapHeight];
		for(int i=0;i<mapWidth;i++)
			for(int j=0;j<mapHeight;j++)
			{
				tileMatrix[i][j]=n;
			}
	}


	public void addAsset(CKPosition pos,CKGraphicsAsset t)
	{
		tileMatrix[(int) pos.getX()][(int) pos.getY()] =new CKTileNode(t, pos.getZ());
	}
	
	public void addInstance(CKAssetInstance i)
	{
		addAsset(i.getPosition(),i.getAsset());
	}
	
	
@Override
public void drawLayerToGraphics(Graphics g, int frame, 
		ImageObserver observer, CKCoordinateTranslator translator)
{
	//System.out.println("Entering LayertoGraphics");
	if(!isVisible()) return;
	Point mapMin = new Point();
	Point mapMax = new Point();
	translator.fillVisibleTileBounds(mapMin,mapMax);
	//System.out.println("Translation points Min:"+mapMin+" Max:"+mapMax );
	for(int y=mapMin.y;y<=mapMax.y;y++)
	{
		for(int x=mapMin.x;x<=mapMax.x;x++)
		{	
			//System.out.println("drawing tile("+x+","+y+")");
			CKTileNode node = tileMatrix[x][y];
			Point screenP = translator.convertMapToScreen(x,y,node.height);
			//System.out.println("drawing tile("+x+","+y+")");
			//System.out.format("drawing%s(%d,%d) at (%s,%d)\n",node.img.getDescription(),x,y,screenP.x,screenP.y);
			node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
		}
	}
//	System.out.println("Exiting LayetoGraphics");
}


@Override
public void drawLayerRowToGraphics(Graphics g, int frame, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	if (y >= tileMatrix[0].length) return; //asking beyond the bounds
	
	Point mapMin = new Point();
	Point mapMax = new Point();
	translator.fillVisibleTileBounds(mapMin,mapMax);
	//reset to be inside the bounds
	
	
	mapMin.x = Math.max(0, mapMin.x);
	mapMax.x = Math.min(mapMax.x, tileMatrix.length-1);
	
	for(int x= mapMin.x;x<=mapMax.x;x++)
	{	
		CKTileNode node = tileMatrix[x][y];
		Point screenP = translator.convertMapToScreen(x,y,node.height);
		//System.out.format("drawing(%d,%d) at (%s,%d)\n",x,y,screenP.x,screenP.y);
		node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
	}
	
}


@Override
public void drawLayerTileToGraphics(Graphics g, int frame, int x, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	CKTileNode node = tileMatrix[x][y];
	Point screenP = translator.convertMapToScreen(x,y,node.height);
	//System.out.format("drawing(%d,%d) at (%s,%d)\n",x,y,screenP.x,screenP.y);
	node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
}


@Override
public void drawLayerToGraphics(GraphicsContext g, int frame, 
		ImageObserver observer, CKCoordinateTranslator translator)
{
	//System.out.println("Entering LayertoGraphics");
	if(!isVisible()) return;
	Point mapMin = new Point();
	Point mapMax = new Point();
	translator.fillVisibleTileBounds(mapMin,mapMax);
	//System.out.println("Translation points Min:"+mapMin+" Max:"+mapMax );
	for(int y=mapMin.y;y<=mapMax.y;y++)
	{
		for(int x=mapMin.x;x<=mapMax.x;x++)
		{	
			//System.out.println("drawing tile("+x+","+y+")");
			CKTileNode node = tileMatrix[x][y];
			Point screenP = translator.convertMapToScreen(x,y,node.height);
			//System.out.println("drawing tile("+x+","+y+")");
			//System.out.format("drawing%s(%d,%d) at (%s,%d)\n",node.img.getDescription(),x,y,screenP.x,screenP.y);
			node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
		}
	}
//	System.out.println("Exiting LayetoGraphics");
}


@Override
public void drawLayerRowToGraphics(GraphicsContext g, int frame, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	if (y >= tileMatrix[0].length) return; //asking beyond the bounds
	
	Point mapMin = new Point();
	Point mapMax = new Point();
	translator.fillVisibleTileBounds(mapMin,mapMax);
	//reset to be inside the bounds
	
	
	mapMin.x = Math.max(0, mapMin.x);
	mapMax.x = Math.min(mapMax.x, tileMatrix.length-1);
	
	for(int x= mapMin.x;x<=mapMax.x;x++)
	{	
		CKTileNode node = tileMatrix[x][y];
		Point screenP = translator.convertMapToScreen(x,y,node.height);
		//System.out.format("drawing(%d,%d) at (%s,%d)\n",x,y,screenP.x,screenP.y);
		node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
	}
	
}


@Override
public void drawLayerTileToGraphics(GraphicsContext g, int frame, int x, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	CKTileNode node = tileMatrix[x][y];
	Point screenP = translator.convertMapToScreen(x,y,node.height);
	//System.out.format("drawing(%d,%d) at (%s,%d)\n",x,y,screenP.x,screenP.y);
	node.img.drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
}



@Override
public void changeHeight(int x, int y, double heightDiff)
{
	tileMatrix[x][y].height+=heightDiff;
	System.out.println("New Height is "+tileMatrix[x][y].height);
}





public static void main(String[] args)
{
	
	
}


@Override
public boolean removeInstance(CKAssetInstance t)
{
	int x = (int) t.getPosition().getX();
	int y = (int) t.getPosition().getY();

	try
	{
		if( tileMatrix[x][y].img == t.getAsset() )
		{
			tileMatrix[x][y].img=CKNullAsset.getNullAsset();
			System.out.println("Out of static?");
			return true;		
		}
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
		return false;
	}
	return false;
}


/* (non-Javadoc)
 * @see ckGraphicsEngine.CKGraphicsLayer#getLayerBounds(java.awt.Point, java.awt.Point)
 */
@Override
public boolean getLayerBounds(Point minPoint, Point maxPoint)
{
	minPoint.x = 0;
	minPoint.y=0;
	
	maxPoint.x = tileMatrix.length;
	maxPoint.y = tileMatrix[0].length;
	return true;
}


@Override
public void removeAllInstances()
{
	for(int i =0;i<tileMatrix.length;i++)
		for(int j=0;j<tileMatrix[i].length;j++)
		{
			tileMatrix[i][j].img=CKNullAsset.getNullAsset();
			tileMatrix[i][j].height=0;
		}
	
}


@Override
public Iterator<CKAssetInstance> iterator()
{
	Vector<CKAssetInstance> vec =new Vector<CKAssetInstance>();
	for(int i =0;i<tileMatrix.length;i++)
		for(int j=0;j<tileMatrix[i].length;j++)
		{
			CKPosition pos = new CKPosition(i,j,tileMatrix[i][j].height,0);
			CKAssetInstance inst = new CKAssetInstance(pos,tileMatrix[i][j].img,0);
			vec.add(inst);
		}
	return vec.iterator();
}
















}
	