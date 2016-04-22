package ckGraphicsEngine;


import static ckGraphicsEngine.CKGraphicsConstants.BASE_HEIGHT;
import static ckGraphicsEngine.CKGraphicsConstants.BASE_WIDTH;

//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
//import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.CKNullAsset;

public class CKGraphicsBoard implements MouseMotionListener 
{//might need to alter this is we want things inside of the component
	/**
     * 
     */
   
    CKGraphicsAsset[][] tileMatrix;
	CKImageAsset backgroundImg;
	
	//screen->world->map coordinate systems
	//screen will start at 0,0 and then getWidth and getHeight to do the rest
	//world coords start at 0,0 and the uses the map to define maximum
	Dimension worldMax;
	//present world coords for upper left-hand corner
	Point worldPresent;
	//map is the tile space, stored in 1-dim array this will store the dimensions for calcualtions
	Dimension mapDim;
	
	
	public CKGraphicsBoard()
	{
		reCalcCoords(1,1);
	}

	
	/**
	 * Creates a map to start with.  Can be altered later.  Used in designing a new map.
	 * @param tile_width
	 * @param tile_height
	 * @param map_width
	 * @param map_height
	 */
	public CKGraphicsBoard(int map_width,int map_height)
	{
		reCalcCoords(map_width,map_height);
		//create blank tiles to populate the map
		for(int x=0;x<map_height;x++)
		{
			for(int y=0;y<map_width;y++)
			{
				replaceTile(x,y,CKNullAsset.getNullAsset());
			}
		}
	}

	private void reCalcCoords(int mapWidth,int mapHeight)
	{
		worldPresent = new Point(0,0);
		resetMapDimensions(mapWidth,mapHeight);
	}
	 
	public void resetMapDimensions(int mapWidth,int mapHeight)
	{
		tileMatrix=new CKGraphicsAsset[mapWidth][mapHeight];
		mapDim = new Dimension(mapWidth,mapHeight);
		worldMax = new Dimension((int) ((mapDim.width+.5)*BASE_WIDTH),
					((mapDim.height+1)*BASE_HEIGHT)/2);	
	}
	
	
	public void replaceTile(int x,int y,CKGraphicsAsset t)
	{
		tileMatrix[x][y] =t;
	}
	
	/********************Translation services************************/
	private Point convertScreenToMapRough(Point p)
	{
		return convertScreenToMap(p.x,p.y);
	}
	
	
	private Point convertScreenToMapRough(int screenX,int screenY)
	{
		int x = (worldPresent.x+screenX) /BASE_WIDTH;
		int y = 2*((worldPresent.y+screenY)/BASE_HEIGHT);
		System.out.format("from screen (%d,%d) to Rough Map (%d,%d)\n",screenX,screenY,x,y);
		return new Point(x,y);
	}

	private Point convertScreenToMap(Point p)
	{
		return convertScreenToMap(p.x,p.y);
	}
	
	
	
	private Point convertScreenToMap(int screenX,int screenY)
	{
		
		Point P = convertScreenToMapRough(screenX,screenY);
		System.out.format("from screen (%d,%d) to Map (%d,%d)\n",screenX,screenY,P.x,P.y);
		//need to calculate internal values
		int tileX = (worldPresent.x+screenX) %BASE_WIDTH;
		int tileY = (worldPresent.y+screenY)%BASE_HEIGHT;
		
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
	

	@SuppressWarnings("unused")
	private Point convertMaptoScreen(Point P)
	{
		return convertMapToScreen(P.x,P.y);
	}
	
	private Point convertMapToScreen(int xmap,int ymap)
	{
		//convert to world coords
		int xworld = (int) ((xmap+.5*(ymap%2))*BASE_WIDTH);
		int yworld = ymap*BASE_HEIGHT/2;
		//convert to screen coords
		int xscreen = xworld-worldPresent.x;
		int yscreen= yworld-worldPresent.y;
		return new Point(xscreen,yscreen);
	}
	
	
	/**
	 * Alters the point, p, to within the bounds of the map
	 * @param p point to be checked and if needed altered
	 * @return True if the point has been altered, false otherwise
	 */
	private boolean confineToMapCoords(Point p)
	{	
		boolean change=false;
		
		if (p.x <0){ p.x=0;change=true;}
		if (p.y <0){ p.y=0;change=true;}
		if (p.x >=mapDim.width){ p.x=mapDim.width;change=true;}
		if (p.y >=mapDim.height){ p.y=mapDim.height;change=true;}
		return change;
	}
	
	
	
	/********End Translation services ***************/
	
	
	public void drawToGraphics(Graphics g,int frame,
			int screenWidth,int screenHeight,ImageObserver observer) 
    {
		//draw background?
		/*
     	Rectangle rect = new Rectangle(0,0,128,128);
  	  	TexturePaint tex = new TexturePaint(backgroundImg, rect);
  	  	Graphics2D g2 = (Graphics2D)g;
  	  	g2.setPaint(tex);
  	  	g2.fill(new Rectangle(getSize()));
*/
      	Point mapMin = convertScreenToMapRough(new Point(0,0));
    	Point mapMax = convertScreenToMapRough(new Point(screenWidth,screenHeight));
    	//need to adjust the max point to get the partial tiles as well
    	mapMax.x++;
    	mapMax.y=mapMax.y+2;
    	
    	//System.out.println("bounds are"+mapMin.toString()+" "+mapMax.toString());
    	
    	//reset points to keep from going over
    	confineToMapCoords(mapMin);
    	confineToMapCoords(mapMax);
 
    	System.out.println("bounds are"+mapMin.toString()+" "+mapMax.toString());
    	
    	//now draw only the tiles necessary
    	//can reduce calculations here if necessary, for now use functions.
    	for(int y=mapMin.y;y<mapMax.y;y++)
    	{
    		for(int x=mapMin.x;x<mapMax.x;x++)
    		{	
    			Point screenP = convertMapToScreen(x,y);
    			//System.out.format("drawing(%d,%d) at (%s,%d)\n",x,y,screenP.x,screenP.y);
    			tileMatrix[x][y].drawToGraphics(g, screenP.x, screenP.y,
    					frame,0, observer);
    		}
    	}

    	/*
    	//overview-to show a grid?
    	Rectangle rect = new Rectangle(0,0,64,32);
  	  	TexturePaint tex = new TexturePaint(mouseMap, rect);
  	  	Graphics2D g2 = (Graphics2D)g;
  	  	g2.setPaint(tex);
  	  	g2.fill(new Rectangle(getSize()));
*/
    	   
    }
	
    
    
 
    /********Mouse adapter services...these will likely be moved out******/
  
    	 @Override
		public void mouseMoved(MouseEvent e) 
    	 {
    		 System.out.println("Moved the mouse");
    		 mapSlider(e);
    	 }

    	 @Override
		public void mouseDragged(MouseEvent e)
    	 {
    	//	 System.out.println("Moved the mouse");
    	//	 redispatchMouseEvent(e, false);
    	 }
/*
    	 public void mouseClicked(MouseEvent e) 
    	 {
    		// System.out.println("Moved the mouse");
  //  		 redispatchMouseEvent(e, false);
    	 }

    	 public void mouseEntered(MouseEvent e) 
    	 {
    		// System.out.println("Moved the mouse");
    	//	 redispatchMouseEvent(e, false);
    	 }

    	 public void mouseExited(MouseEvent e) 
    	 {
    		// System.out.println("Moved the mouse");
    	//	 redispatchMouseEvent(e, false);
    	 }

    	 public void mousePressed(MouseEvent e) 
    	 {
 //   		 System.out.println("Moved the mouse");
    	//	 reoffsetdispatchMouseEvent(e, false);
    	 }

    	 public void mouseReleased(MouseEvent e) 
    	 {
//    		 System.out.println("Moved the mouse");
    	//	 redispatchMouseEvent(e, true);
    	 }

 //   }
  */  
    
    
    @SuppressWarnings("unused")
	private CKGraphicsAsset getTileFromScreenCoords(Point p)
    {
    	Point mapP = convertScreenToMap(p);
    	if(confineToMapCoords(mapP))
    	{ //point is not on a map coord
    		return null;
    	}
    	System.out.format("highlighting tile (%d,%d) from (%d,%d)\n",mapP.x,mapP.y,p.x,p.y);
    	return tileMatrix[mapP.x][mapP.y];
    }
     
  /*  public void unhighlight()
    {
    	for (CKTile t:highlighted)	
    	{	
    		t.removeHighlights();
    	}
    	
    	highlighted.clear();
    }
    */
    
    /*
     * Shifts the world coordinates when mouse is near the edges of the map
     * 
     * 
     */
    private void mapSlider(MouseEvent e)
    {
    	int activeMargin = 50;
    	int mapShift = 10;
    	//boolean altered = false;
    	Point p = e.getPoint();
    	
    	//check if a slide is necessary
    	if(p.x<activeMargin)
    	{
    		worldPresent.x-=mapShift;
    		//altered=true;
    	}
    	
    	if(p.y<activeMargin)
    	{
    		worldPresent.y-=mapShift;
    		//altered=true;
   
   
    	}
	

    }
}
