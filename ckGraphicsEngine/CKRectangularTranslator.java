package ckGraphicsEngine;



import java.awt.Dimension;
import java.awt.Point;

import ckGraphicsEngine.assets.CKImageAsset;


import static ckGraphicsEngine.CKGraphicsConstants.*;


public class CKRectangularTranslator extends CKCoordinateTranslator
{
	@SuppressWarnings("unused")
	private int worldWidth;
	@SuppressWarnings("unused")
	private int worldHeight;
	
	
	public CKRectangularTranslator()
	{
		super();
	}

	public CKRectangularTranslator(int mapWidth,int mapHeight,
			int screenWidth,int screenHeight)
	{
		super(mapWidth,mapHeight,screenWidth,screenHeight);		
	}

	
	/**
	 * Creates a map to start with.  Can be altered later.  Used in designing a new map.
	 * @param map_width
	 * @param map_height
	 */
	public CKRectangularTranslator(Dimension dim)
	{
		super(dim);
	}

		
	
	
	 
	public void resetMapDimensions(int mapWidth,int mapHeight)
	{
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		worldWidth = (int) ((mapWidth+.5)*BASE_WIDTH);
		worldHeight= (int) ((mapHeight+1)*BASE_HEIGHT)/2;	
	}
	
	
/*	
	public void getVisibleTileBounds(Point mapMin,Point mapMax)
	{
		//creates new point here...TODO
		Point minP = convertScreenToMapRough(new Point(0,0));
    	Point maxP = convertScreenToMapRough(new Point(screenDim.width,screenDim.height));
    	//need to adjust the max point to get the partial tiles as well
    	minP.x++;
    	maxP.y=maxP.y+2;
    	
 
    	
    	//reset points to keep from going over
    	confineToMapCoords(minP);
    	confineToMapCoords(maxP);
    	
    	mapMin.x = minP.x;
    	mapMin.y = minP.y;
    	mapMax.x = maxP.x;
    	mapMax.y = maxP.y;
    	
    	
    	System.out.println("bounds are"+mapMin.toString()+" "+mapMax.toString());
    	//TODO need to restrict to what will be seen in the window. 
	}
	
	*/
	
	/********************Translation services************************/
	@SuppressWarnings("unused")
	private Point convertScreenToMapRough(Point p)
	{
		return convertScreenToMapRough(p.x,p.y);
	}
	
	
	private Point convertScreenToMapRough(int screenX,int screenY)
	{
		int x = (worldOffsetX+screenX) /BASE_WIDTH;
		int y = 2*((worldOffsetY+screenY)/BASE_HEIGHT);
		//System.out.format("from screen (%d,%d) to Rough Map (%d,%d)\n",screenX,screenY,x,y);
		return new Point(x,y);
	}

	
		
	public Point convertScreenToMap(int screenX,int screenY)
	{
		
		Point P = convertScreenToMapRough(screenX,screenY);
		//System.out.format("from screen (%d,%d) to Map (%d,%d)\n",screenX,screenY,P.x,P.y);
		//need to calculate internal values
		int tileX = (worldOffsetX+screenX) %BASE_WIDTH;
		int tileY = (worldOffsetY+screenY)%BASE_HEIGHT;
		
		//now adjust if necessary.
        if(CKImageAsset.isBaseUpperLeft(tileX,tileY,BASE_WIDTH,BASE_HEIGHT))
        { //upper left
            P.x--;
            P.y--;
        }
        else if(CKImageAsset.isBaseUpperRight(tileX,tileY,BASE_WIDTH,BASE_HEIGHT))
        {//upper right
            //P.x++;
            P.y--;
        }
        else if(CKImageAsset.isBaseLowerLeft(tileX,tileY,BASE_WIDTH,BASE_HEIGHT))
        { //lower left
            P.x--;
        	P.y++;
        }
        else if(CKImageAsset.isBaseLowerRight(tileX,tileY,BASE_WIDTH,BASE_HEIGHT))
        {//lower right
            //P.x++;
            P.y++;
        }
        System.out.format("Tile coords (%d,%d) leads to final(%d,%d)\n",tileX,tileY,P.x,P.y);
        return P;
		
	}
	

	
	
	public Point convertMapToScreen(double xmap, double ymap,double height)
	{
		//convert to world coords
		double xworld = ((xmap+.5*(ymap%2))*BASE_WIDTH);
		double yworld = ymap*BASE_HEIGHT/2-height*HEIGHT_MULTIPLIER;
		//convert to screen coords
		int xscreen = (int) xworld-worldOffsetX;
		int yscreen= (int) yworld-worldOffsetY;
		//System.out.println("draw to screen ("+xscreen+","+yscreen+")");
		return new Point(xscreen,yscreen);
	}
	
}