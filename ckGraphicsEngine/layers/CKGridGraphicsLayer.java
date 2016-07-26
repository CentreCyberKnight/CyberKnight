package ckGraphicsEngine.layers;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckDatabase.CKGridFactory;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridItem;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.assets.CKAssetInstance;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKGridGraphicsLayer extends CKGraphicsLayer
{
	
	
	
    /*int mapWidth;
    int mapHeight;
	*/
	private CKGrid grid;
	@SuppressWarnings("unused")
	private String gridID;
	private HashMap<Integer,CKGraphicsAsset> instanceMap = new HashMap<Integer,CKGraphicsAsset>();
	
	
	public CKGridGraphicsLayer()
	{
		this("","",new CKGrid(1,1),0);	
	}
	
	public CKGridGraphicsLayer(CKGrid grid)
	{
		this("grid"+grid.getAID(),grid.getDescription(),grid,GROUND_LAYER);
	}

	
	/**
	 * Creates a map to start with.  Can be altered later.  Used in designing a new map.
	 * @param tile_width
	 * @param tile_height
	 * @param mapWidth
	 * @param mapHeight
	 */
	public CKGridGraphicsLayer(String id,String desc, CKGrid grid,int layerDepth)
	{
		super(layerDepth,id,desc);
		this.grid=grid;
		gridID=grid.getAID();
		
		setVisible(true);
	}
	
	
	/**
	 * @return the gridID
	 */
	public String getGridID()
	{
		return grid.getAID();
	}

	/**
	 * @param gridID the gridID to set
	 */
	public void setGridID(String gridID)
	{
		this.gridID = gridID;
		this.grid = CKGridFactory.getInstance().getAsset(gridID);
	}

	
	
	/**
	 * @return the mapWidth
	 */
	public int getMapWidth()
	{
		return grid.getWidth();
	}


	/**
	 * @param mapWidth the mapWidth to set
	 */
	public void setMapWidth(int mapWidth)
	{
		grid.setWidth(mapWidth);
	}


	/**
	 * @return the mapHeight
	 */
	public int getMapHeight()
	{
		return grid.getHeight();

	}


	/**
	 * Side-effect will resize the matrix and set it with all new values.
	 * @param mapHeight the mapHeight to set
	 */
	public void setMapHeight(int mapHeight)
	{
		grid.setHeight(mapHeight);
	}


	public void addAsset(CKPosition pos,CKGraphicsAsset t)
	{
		CKAbstractGridItem item = new CKGridItem();
		item.setAssetID(t.getAID());
		
		grid.addToPosition(item,(int) pos.getX(),(int) pos.getY());
	}
	
	public void addInstance(CKAssetInstance i)
	{
		instanceMap.put(i.getIID(), i.getAsset());
		addAsset(i.getPosition(),i.getAsset());
	}
	

	protected void drawGrid(Graphics g, int frame, 
			ImageObserver observer, CKCoordinateTranslator translator,
			int x1,int x2,int y1,int y2)
	{
		if(x1<0) 					{x1=0;}
		if(y1<0) 					{y1=0;}
		if(x2>getMapWidth()) 		{x2=getMapWidth()-1;}
		if(y2>getMapHeight()) 		{y2=getMapHeight()-1;}
		
		for(int y=y1;y<=y2;y++)
		{
			for(int x=x1;x<=x2;x++)
			{	
				grid.drawPosition(x,y,frame,observer,translator,g);
			}
		}	
		
		
		
		
	}
	
	
	protected void drawGrid(GraphicsContext g, int frame, 
			ImageObserver observer, CKCoordinateTranslator translator,
			int x1,int x2,int y1,int y2)
	{
		if(x1<0) 					{x1=0;}
		if(y1<0) 					{y1=0;}
		if(x2>getMapWidth()) 		{x2=getMapWidth()-1;}
		if(y2>getMapHeight()) 		{y2=getMapHeight()-1;}
		
		for(int y=y1;y<=y2;y++)
		{
			for(int x=x1;x<=x2;x++)
			{	
				grid.drawPosition(x,y,frame,observer,translator,g);
			}
		}	
		
		
		
		
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
	drawGrid(g,frame,observer,translator,mapMin.x,mapMax.x,mapMin.y,mapMax.y);
}


@Override
public void drawLayerRowToGraphics(Graphics g, int frame, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	if (y >= getMapHeight()) return; //asking beyond the bounds
	
	Point mapMin = new Point();
	Point mapMax = new Point();
	translator.fillVisibleTileBounds(mapMin,mapMax);
	//reset to be inside the bounds
	
	
	mapMin.x = Math.max(0, mapMin.x);
	mapMax.x = Math.min(mapMax.x, getMapWidth()-1);

	drawGrid(g,frame,observer,translator,mapMin.x,mapMax.x,y,y);

	
}


@Override
public void drawLayerTileToGraphics(Graphics g, int frame, int x, int y,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	if(!isVisible()) return;
	grid.drawPosition(x,y,frame,observer,translator,g);
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
drawGrid(g,frame,observer,translator,mapMin.x,mapMax.x,mapMin.y,mapMax.y);
}


@Override
public void drawLayerRowToGraphics(GraphicsContext g, int frame, int y,
	ImageObserver observer, CKCoordinateTranslator translator)
{
if(!isVisible()) return;
if (y >= getMapHeight()) return; //asking beyond the bounds

Point mapMin = new Point();
Point mapMax = new Point();
translator.fillVisibleTileBounds(mapMin,mapMax);
//reset to be inside the bounds


mapMin.x = Math.max(0, mapMin.x);
mapMax.x = Math.min(mapMax.x, getMapWidth()-1);

drawGrid(g,frame,observer,translator,mapMin.x,mapMax.x,y,y);


}


@Override
public void drawLayerTileToGraphics(GraphicsContext g, int frame, int x, int y,
	ImageObserver observer, CKCoordinateTranslator translator)
{
if(!isVisible()) return;
grid.drawPosition(x,y,frame,observer,translator,g);
}



@Override
public void changeHeight(int x, int y, double heightDiff)
{
	CKAbstractGridItem item =grid.getPosition(x, y);
	item.changeHeight(heightDiff);
}





public static void main(String[] args)
{
	JFrame frame = new JFrame();
	
	CKGrid grid = new CKGrid(10,10);
	//now to place some tiles.
	
	
	
	for(int i =2;i<=8;i++)
		for(int j =2;j<=8;j++)
		{
			CKAbstractGridItem land= new CKGridItem();
			land.setAssetID("blue");
			land.setMoveCost(1);
			grid.setPosition(land, i, j);				
		}
	
	
	for(int i =4;i<=6;i++)
		for(int j =4;j<=6;j++)
		{
			CKAbstractGridItem block= new CKGridItem();
			block.setAssetID("pineBlock");
			block.setMoveCost(2);
			block.setItemHeight(1);
			grid.addToPosition(block, i,j);
		}
	
	CKAbstractGridItem bigBlock=new CKGridItem();
	bigBlock.setAssetID("stoneBlock");
	bigBlock.setMoveCost(1);
	bigBlock.setItemHeight(2);
	grid.addToPosition(bigBlock,5, 5);
	
	
	/*CKGridItem block= new CKGridItem();
	block.setAssetID("kFloor");
	block.setMoveCost(2);
	block.setItemHeight(1);
	grid.addToPosition(block, 2,2);

	CKGridItem block2= new CKGridItem();
	block2.setAssetID("kFloor");
	block2.setMoveCost(2);
	//block2.setItemHeight(1);
	grid.addToPosition(block2, 2,4);

	
	CKGridItem block3= new CKGridItem();
	block3.setAssetID("kFloor");
	block3.setMoveCost(2);
	block3.setItemHeight(1);
	grid.setPosition(block3, 0,3);
*/
	/*
	CKGrid grid = new CKGrid(1,1);
	//now to place some tiles.
	
	
	CKGridItem land= new CKGridItem();
	land.setAssetID("blue");
	land.setMoveCost(1);
	grid.setPosition(land, 0,0);				
			
	CKGridItem block= new CKGridItem();
	block.setAssetID("kFloor");
	block.setMoveCost(2);
	block.setItemHeight(1);
	grid.addToPosition(block, i,j);
	*/
	
	
	
	CKGridGraphicsLayer layer = new CKGridGraphicsLayer("Testing","hope",grid,GROUND_LAYER);
	
	CKLayerViewer view=new CKLayerViewer(1,layer,new Dimension(512,512),new Dimension(10,10));
	frame.add(view);
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
}


//FIXME - I only look at how people 'look' to remove them...
@Override
public boolean removeInstance(CKAssetInstance t)
{
	//int x = (int) t.getPosition().getX();
	//int y = (int) t.getPosition().getY();

	if(!instanceMap.containsKey(t.getIID()))
	{
		return false;
	}
	
	//this should never get called..could have dups....
	CKAbstractGridItem item =grid.getPosition(t.getPosition()).findItemWithAsset(t.getAsset().getAID());
	if(item == null)
	{
		return false;
	}
	//TODO could create a neat animation here for the removal.
	item.removeItemFromGrid(grid);
	
	
	return true;
	
	
	}


/* (non-Javadoc)
 * @see ckGraphicsEngine.CKGraphicsLayer#getLayerBounds(java.awt.Point, java.awt.Point)
 */
@Override
public boolean getLayerBounds(Point minPoint, Point maxPoint)
{
	minPoint.x = 0;
	minPoint.y=0;
	
	maxPoint.x = getMapWidth();
	maxPoint.y = getMapHeight();
	return true;
}


@Override
public void removeAllInstances()
{
	grid.resize(getMapWidth(),getMapHeight());
}


@Override
public Iterator<CKAssetInstance> iterator()
{
	/*Vector<CKAssetInstance> vec =new Vector<CKAssetInstance>();
	for(int i =0;i<getMapWidth();i++)
		for(int j=0;j<getMapHeight();j++)
		{
			CKPosition pos = new CKPosition(i,j,grid.getPosition(i,j).height,0);
			CKAssetInstance inst = new CKAssetInstance(pos,tileMatrix[i][j].img,0);
			vec.add(inst);
		}
	return vec.iterator();
	FIXME Later....
	*/
	return null;
}
















}
	