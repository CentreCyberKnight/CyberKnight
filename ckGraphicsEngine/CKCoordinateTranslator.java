package ckGraphicsEngine;



import java.awt.Dimension;
import java.awt.Point;

import ckCommonUtils.CKPosition;

abstract public class CKCoordinateTranslator
{
	
	/**
	 * screen->world->map coordinate systems
	 * screen and world are in pixels, map is indexes into a matrix.
	 */
	
	
	//screen will start at 0,0 and then getWidth and getHeight to do the rest
	volatile protected int screenWidth;
	volatile protected int screenHeight;
	
	//world coords start at 0,0 and the uses the map to define maximum
	//protected int worldWidth;
	//protected int worldHeight;
	
	//map is the tile space, stored in 1-dim array this will store the dimensions for calcualtions
	protected int mapWidth;
	protected int mapHeight;
	
		
	//present world coords for upper left-hand corner
	volatile protected int worldOffsetX;
	volatile protected int worldOffsetY;
	
	
	

	/**
	 *  Creates a translator between coordinate systems.
	 * @param mapWidth       
	 * @param mapHeight
	 * @param screenWidth
	 * @param screenHeight
	 */
	public CKCoordinateTranslator(int mapWidth,int mapHeight,
			int screenWidth,int screenHeight)
	{
		this.worldOffsetX=0;
		this.worldOffsetY=0;

		setScreenDimensions(screenWidth,screenHeight);
		resetMapDimensions(mapWidth,mapHeight);
	
	}
	
	public CKCoordinateTranslator()
	{
		this(1,1,1,1);
	}

	
	public CKCoordinateTranslator(Dimension dim)
	{
		this(dim.width,dim.height,1,1);
	}
	
	 
	/**
	 * changes the map and world coordinates based on parameters.
	 * @param mapWidth - columns in the map
	 * @param mapHeight - rows in the map
	 */
	protected void resetMapDimensions(int mapWidth,int mapHeight)
	{
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}
	
	/**
	 * This function will ensure that the Translator's map coordinates will contain the necessayMax Point
	 * If it does not already contain the necessary Max, the map coordinsates will be extended to include
	 * the necessaryPoint.  
	 * @param necessaryMax the Point that must be inlcuded in the map coordinates
	 */
	public void insureMapBounds(Point necessaryMax)
	{
		if(mapWidth < necessaryMax.x) { mapWidth = necessaryMax.x; }
		if(mapHeight < necessaryMax.y) { mapHeight = necessaryMax.y; }
	}
	
	
	synchronized public void setScreenDimensions(int screenWidth,int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	
	
	public int getMapRows()
	{
		return mapHeight;
	}
	
	public int getMapColumns()
	{
		return mapWidth;
	}
	

	public int getScreenWidth()
	{
		return screenWidth;
	}
	
	public int getScreenHeight()
	{
		return screenHeight;
	}
	/**
	 * Fills mapMin and MapMax with the bounds for what tiles are visible on screen.
	 * @param mapMin
	 * @param mapMax
	 */
	public void fillVisibleTileBounds(Point mapMin,Point mapMax)
	{
		//creates new point here...TODO
		Point minP = convertScreenToMap(new Point(0,0));
    	Point maxP = convertScreenToMap(new Point(screenWidth,screenHeight));
    	
       	//reset points to keep from going over
    	confineToMapCoords(minP);
    	confineToMapCoords(maxP);
    	
    	mapMin.x = minP.x;
    	mapMin.y = minP.y;
    	mapMax.x = maxP.x;
    	mapMax.y = maxP.y;
    	
    	
//     	System.out.println("bounds are"+mapMin.toString()+" "+mapMax.toString());
   
	}

/**
 * Determines what the screen coordinates would be given the Map coordinates that define a boundry
 * Also note that the screen coordinates might not actually be visible from the present viewport on the screen.
 * @param mapMin     - Minimum coordinate in the map
 * @param mapMax     - Maximum coordinate in the map
 * @param ScreenMin  - Minimum calculated coordinate in the screen (value returned in pass by reference)
 * @param ScreenMax  - Maximum calculated coordinate in the screen (value returned in pass by reference)
 */
	public void getScreenCoordsForBounds(Point mapMin, Point mapMax,
			Point screenMin,Point screenMax)
	{
		Point mapCorner2 = new Point(mapMin.x,mapMax.y);
		Point mapCorner3 = new Point(mapMax.x,mapMin.y);
		
		//convert bounds
		Point C1 = convertMapToScreen(mapMin);
		Point C2 = convertMapToScreen(mapCorner2);
		Point C3 = convertMapToScreen(mapCorner3);
		Point C4 = convertMapToScreen(mapMax);
		//assuming topological similarities between the two spaces, these bounds should form the screen bounds

		//initialize min and max
		screenMin.x = C1.x;
		screenMax.x = C1.x;
		screenMin.y = C1.y;
		screenMax.y = C1.y;
		int [] xs ={ C2.x,C3.x,C4.x};
		int [] ys ={ C2.y,C3.y,C4.y};
		for(int i=0;i<3;i++ )
		{
			if(screenMin.x > xs[i]) { screenMin.x = xs[i] ; }
			if(screenMin.y > ys[i]) { screenMin.y = ys[i] ; }
			if(screenMax.x < xs[i]) { screenMax.x = xs[i] ; }
			if(screenMax.y < ys[i]) { screenMax.y = ys[i] ; }
			
		}
		//System.out.println("Mincoords"+screenMin+" and max coords"+screenMax);

		//man and min are now in the parameters (pass by reference)
	}
	
	
	/** 
	 * Determines what the screen coordinates would be for the dimensions of the map.
	 * Also note that the screen coordinates might not actually be visible from the present viewport on the screen.
	 * @param ScreenMin  - Minimum calculated coordinate in the screen  (value returned in pass by reference)
	 * @param ScreenMax  - Maximum calculated coordinate in the screen  (value returned in pass by reference)
	 */
		public void getScreenCoordsForBounds(Point screenMin,Point screenMax)
	{
		getScreenCoordsForBounds(new Point(-1,-1),new Point(mapWidth+1,mapHeight+1), screenMin,screenMax);
		
	}
	
	
	
	
	
	/********************Translation services************************/
	
	public Point convertScreenToMap(Point p)
	{
		return convertScreenToMap(p.x,p.y);
	}
		
	abstract public Point convertScreenToMap(int screenX,int screenY);
		

	public Point convertMapToScreen(Point P)
	{
		return convertMapToScreen(P.x,P.y);
	}
	
	public Point convertMapToScreen(int xmap,int ymap)
	{
		return convertMapToScreen((double)xmap,(double)ymap,0);
	}
	public Point convertMapToScreen(CKPosition pos)
	{
		return convertMapToScreen(pos.getX(),pos.getY(),pos.getZ());
	}
		
	abstract public Point convertMapToScreen(double xmap, double ymap,double height);
	
	
	/**
	 * Alters the point, p, to within the bounds of the map
	 * @param p point to be checked and if needed altered
	 * @return True if the point has been altered, false otherwise
	 */
	public boolean confineToMapCoords(Point p)
	{	
		boolean change=false;
		//System.out.println("Incoming confine to "+p);
		if (p.x <0){ p.x=0;change=true;}
		if (p.y <0){ p.y=0;change=true;}
		if (p.x >=mapWidth){ p.x=mapWidth-1;change=true;}
		if (p.y >=mapHeight){ p.y=mapHeight-1;change=true;}
		
		//System.out.println("confined to "+p);
		return change;
	}

	synchronized public void shiftWorldOffset(int x, int y)
	{
		//System.out.println("Resetting offsets"+worldOffsetX+","+worldOffsetY);
		worldOffsetX+=-x;
		worldOffsetY+=-y;
		//System.out.println("Resetting offsets Are Now "+worldOffsetX+","+worldOffsetY);
	}
	
	synchronized public void setWorldOffset(int x, int y)
	{
		worldOffsetX=-x;
		worldOffsetY=-y;
	}
}
