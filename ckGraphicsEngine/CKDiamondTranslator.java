package ckGraphicsEngine;



import static ckGraphicsEngine.CKGraphicsConstants.BASE_HEIGHT;
import static ckGraphicsEngine.CKGraphicsConstants.BASE_WIDTH;
import static ckGraphicsEngine.CKGraphicsConstants.HEIGHT_MULTIPLIER;
import static ckGraphicsEngine.CKGraphicsConstants.TOP_SCREEN_MARGIN;

import java.awt.Dimension;
import java.awt.Point;


public class CKDiamondTranslator extends CKCoordinateTranslator
{
	//private int worldWidth;
	//private int worldHeight;
	private float tileratio;

	
	
	public CKDiamondTranslator()
	{
		super();
		tileratio=BASE_WIDTH/ (float) (BASE_HEIGHT);
	}

	public CKDiamondTranslator(int mapWidth,int mapHeight,
			int screenWidth,int screenHeight)
	{
		super(mapWidth,mapHeight,screenWidth,screenHeight);	
		tileratio=BASE_WIDTH/ (float) (BASE_HEIGHT);
	}

	
	/**
	 * Creates a map to start with.  Can be altered later.  Used in designing a new map.
	 * @param map_width
	 * @param map_height
	 */
	public CKDiamondTranslator(Dimension dim)
	{
		super(dim);
		tileratio=BASE_WIDTH/ (float) (BASE_HEIGHT);
	}

	/*
	 * Just use the parents resetmapdimensions instead
	 * public void resetMapDimensions(int mapWidth,int mapHeight)
	{
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		worldWidth = (mapHeight+mapWidth)*BASE_WIDTH/2;
		worldHeight= (mapHeight+mapWidth)*BASE_HEIGHT/2 +TOP_SCREEN_MARGIN;
	}
	*/
	

	public void fillVisibleTileBounds(Point mapMin,Point mapMax)
	{
		//TODO Rework this to cache bounds 
		Point upperLeft = convertScreenToMap(new Point(0,0));
    	Point lowerRight = convertScreenToMap(new Point(screenWidth,screenHeight));
		Point upperRight = convertScreenToMap(new Point(screenWidth,0));
    	Point lowerLeft = convertScreenToMap(new Point(0,screenHeight));
    	//need to adjust the max point to get the partial tiles as well
    	
    	
    	//reset points to keep from going over
    	confineToMapCoords(upperLeft);
    	confineToMapCoords(lowerRight);
    	confineToMapCoords(upperRight);
    	confineToMapCoords(lowerLeft);
    	
    	mapMin.x = upperLeft.x;
    	mapMin.y = upperRight.y;
    	mapMax.x = lowerRight.x;
    	mapMax.y = lowerLeft.y;
    	
    	
    	//System.out.println("bounds are"+mapMin.toString()+" "+mapMax.toString());
    	//TODO need to restrict to what will be seen in the window. 
	}
	

	

	
		
	public Point convertScreenToMap(int screenX,int screenY)
	{
		//System.out.println("using offsets"+worldOffsetX+","+worldOffsetY);
		//convert to world
		int worldX = screenX+worldOffsetX - mapWidth*BASE_WIDTH/2-BASE_WIDTH;
		int worldY = screenY+worldOffsetY -TOP_SCREEN_MARGIN +BASE_HEIGHT/2;
		//convert to map
		int x = (int) ((worldY+worldX/tileratio)* (tileratio/BASE_WIDTH));
		int y = (int) ((worldY-worldX/tileratio)* (tileratio/BASE_WIDTH));
		return new Point(x,y-1);		
	}
	

	
	
	public Point convertMapToScreen(double xmap, double ymap,double height)
	{
		//convert to world coords
		double xworld = (xmap-ymap+mapWidth)*BASE_WIDTH/2;
		double yworld = (xmap+ymap)*BASE_HEIGHT/2 + TOP_SCREEN_MARGIN 
			-height*HEIGHT_MULTIPLIER;;
		
		
		//convert to screen coords
		int xscreen = (int) xworld-worldOffsetX;
		int yscreen= (int) yworld-worldOffsetY;
		//System.out.println("draw to screen ("+xscreen+","+yscreen+")");
		
		
		return new Point(xscreen,yscreen);
	}
	
}