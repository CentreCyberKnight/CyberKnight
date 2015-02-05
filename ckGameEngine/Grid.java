package ckGameEngine;

import ckCommonUtils.*;

/**
 * Grid Class
 * @author 
 *
 */
public class Grid {
	CKGridPosition[][] posList;
	private int width;
	private int height;
	
	//tina says: this should get Positions from the db, rather than creating new ones
	
	public Grid()
	{
		//FIXME!!!!width and height changes should trigger poslist recalc.
		this(10,10,0,0);
	}
	
	
	/**
	 * Constructs a basic grid, assigning the apropriate width and length
	 * assigns all z in location to zero
	 * assigns all depths to zero 
	 * @param width width of grid
	 * @param lenght length of grid
	 * unit test created 3/22/11
	 */
	public Grid(int width, int length)
	{
		this(width,length,0,0);
	}
	
	/**
	 * Constructs a more complex grid by setting the width, length, height, and depth
	 * @param x - width of the grid
	 * @param y - length of the grid
	 * @param z - height of the grid
	 * @param depth - depth of the grid
	 * @calls setWidth and setHeight
	 *	unit test created 3/22/11
	 */
	public Grid(int x, int y, int z, int depth)
	{
		//this should be tweeked probably, just a tempary 
		posList = new CKGridPosition[x][y];
		this.setWidth(x);
		this.setHeight(y);
		for(int i=0; i<x; i++)
		{
			for (int j=0; j<y; j++)
			{
				CKGridPosition temp = new CKGridPosition(i,j,0,0);
				posList[i][j] = temp;
			}
				/*for (int k=-1; k<=z; k++)
				{
					Position temp = new Position(i,j,k);
					posList[i+j+k] = temp;
				}*/
			
		}
	}
	
	
	/**
	 * Private, called by constructor
	 * @param width datamember is set
	 */
	private void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Private, called by constructor
	 * height data member is set
	 * @param height
	 */
	private void setHeight(int height) {
		this.height = height;
	}

	/**
	 * gets the width of the grid
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * gets the height of the grid
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * This function returns the location that is saved at the array position.
	 * This allows retrieving locations to act in constant time
	 * @param x
	 * @param y
	 * @return Position at coordinants
	 * Unit test created 3/22/11
	 */
	public CKGridPosition getPositionFromList(int x, int y)
	{
		return posList[x][y];
	}
	
	
	public CKGridPosition getPositionFromList(CKPosition pos)
	{
		return getPositionFromList( (int) pos.getX(), (int)pos.getY());
	}

	/**
	 * @param args
	 * @throws LoadAssetError 
	 */
	/*
	public static void main(String[] args) throws LoadAssetError {
		//This is basically our current "alpha engine"
		//creates a 4x4 grid
		//Grid grid = new Grid(10,10);
		World world = new World();
		//creates two characters, both PCs

		//hero.setGrid(grid);


		//Now graphics start getting involved (mostly working from template at this point
		JFrame frame = new JFrame();
		CK2dGraphicsEngine engine = new CK2dGraphicsEngine(30,5);
		engine.setPreferredSize(new Dimension(600,600));
		engine.loadScene(2);
		PC hero = new PC(world.grid.getPositionFromList(0, 5), world);
		System.out.println("Hero is at Position " + hero.getPosition().getX() + " " + hero.getPosition().getY());

		try
		{
			int tid =  engine.startTransaction(true);

			engine.loadAsset(tid, hero.getAssetId());
			System.out.println("hero assetId " + hero.getAssetId());
			int spriteID = engine.createInstance(tid,hero.getAssetId(),hero.getPosition(),30,CKGraphicsLayer.SPRITE_LAYER);
			hero.setInstanceId(spriteID);
			//delay
			int finishTime = engine.cameraPointAt(tid, new CKPosition(5,5,0,0), 15, 80);
			//focus on Hero
			finishTime=engine.cameraPointAt(tid, hero.getPosition(), finishTime, 30);
			PC invisibleWall = new PC(grid.getPositionFromList(0, 7), world);
			//Should probably make this into a function
			int stepsToTake=4;
			CKPosition startPos = hero.getPosition();
			//int stepsTaken = hero.move(Direction.NORTHEAST,stepsToTake);
			System.out.println(hero.getPosition());
			engine.setAnimation(tid,hero.getInstanceId(),hero.getDirection().toString(), finishTime);
			System.out.println("Hereo is at " + hero.getPosition().getX() + " , " + hero.getPosition().getY());
			finishTime=engine.cameraFollowInstance(tid, spriteID, finishTime, 0);
			System.out.println((hero.getPosition().getX()-grid.getWidth()) + " " + (hero.getPosition().getY()-grid.getHeight()));
			/*if(stepsTaken!=stepsToTake)
			{
				finishTime=engine.move(tid,hero.getInstanceId(),finishTime,startPos,grid.getPositionFromList((int)hero.getPosition().getX(),((int)Math.abs(hero.getPosition().getY()-grid.getHeight()))),10);
				//Shake camera left then right then left then center (probably make this a function)
				finishTime=engine.cameraPointAt(tid, grid.getPositionFromList(Math.abs((int)hero.getPosition().getX()-1),(int)hero.getPosition().getY()), finishTime, 3);
				finishTime=engine.cameraPointAt(tid, grid.getPositionFromList((int)hero.getPosition().getX(),Math.abs((int)hero.getPosition().getY()-1)), finishTime, 3);
				finishTime=engine.cameraPointAt(tid, grid.getPositionFromList((int)hero.getPosition().getX(),((int)Math.abs(hero.getPosition().getY()-grid.getHeight()))), finishTime, 20);
			}
			else
			{
				finishTime=engine.move(tid,hero.getInstanceId(),finishTime,startPos,grid.getPositionFromList((int)hero.getPosition().getX(),((int)Math.abs(hero.getPosition().getY()-grid.getHeight()))),10);
			}
			 
			engine.endTransaction(tid, true);

		}catch (LoadAssetError e)
		{
			
			e.printStackTrace();
		} catch (BadInstanceIDError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CircularDependanceError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownAnimationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		//hero is placed at position (0,0)
		//hero.checkOutPosition(grid.getPositionFromList(0,0));
		//checks to make sure the hero is at the correct position
		//System.out.println("Hero is at Position " + hero.getPosition().getX() + " " + hero.getPosition().getY());
		//attempts to move hero to location. 
		//hero.move();
		//checks to see if it worked
		//System.out.println("Hero is at Position " + hero.getPosition().getX() + " " + hero.getPosition().getY());
		
		frame.add(engine);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		}
		*/
	

}
