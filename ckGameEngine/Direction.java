package ckGameEngine;

import java.util.stream.Stream;

import ckCommonUtils.CKPosition;

public enum Direction {

	
	NORTHEAST("NORTHEAST",0,-1), 
	SOUTHEAST("SOUTHEAST",1,0), 
	SOUTHWEST("SOUTHWEST",0,1), 
	NORTHWEST("NORTHWEST",-1,0),
	NONE("NONE",0,0);
	
	
	private final String directionName;
	public final int dx;
	public final int dy;
	private final double dist;
	
	
	
	Direction(String name, int x, int y)
	{
		this.directionName=name;
		this.dx=x;
		this.dy=y;
		dist= Math.sqrt(x*x+y*y);
	}
	

	public Direction getLeftNeightbor()
	{
		if(this==NONE) { return NONE; }
		
		final int len = Direction.values().length-1;
		int ord = (ordinal()+len-1)%len; 
		return Direction.values()[ord];
	}
	
	

	
	public Direction getRightNeightbor()
	{
		if(this==NONE) { return NONE; }
		
		final int len = Direction.values().length-1;
		int ord = (ordinal()+len+1)% len;
		return Direction.values()[ord];
	}
	
	
	
	
	private static double angleTo(Direction dir,double dx,double dy,double dist)
	{
		final double dot = dx*dir.dx+dy*dir.dy;
		return Math.acos(dot/(dist*dir.dist));		
	}
	
	/**
	 * returns the clockwise degree to find the stuff
	 * @param dir
	 * @return
	 */
	public double angleTo(Direction dir)
	{
		final double dot = this.dx*dir.dx+this.dy*dir.dy;
		final double det = this.dx*dir.dy-this.dy*dir.dx;
		return Math.atan2(det, dot);
		//return angleTo(this,dir.dx,dir.dy,dir.dist);
	}
	
	/**
	 *	gets sprite's direction to given Position
	 *	takes into consideration that pos may not be on sprite's axis
	 *	renders closest direction. example: if pos is 5w and 1n, returns west
	 *	we can use this to move to a Position, even if not on axis
	 *	get direction that leads closest to pos, move one tile that dir
	 * 	recalculate dir, that should render shortest route
	 * 	@param pos
	 * 	@calls getDirection
	 *  @calledBy 
	 * 	@return direction to face
	 */
	public static Direction getDirectionTo(CKPosition startPos, CKPosition endPos)
	{
		final double diffX = endPos.getX() - startPos.getX();
		final double diffY = endPos.getY() - startPos.getY();
		final double distance = Math.sqrt(diffX*diffX+diffY*diffY);
		
		
		Direction closest = NORTHWEST;
		double minAngle=400;
		for (Direction d:Direction.values())
		{
			double angle = angleTo(d,diffX,diffY,distance);
			if(angle<minAngle)
			{
				closest=d;
				minAngle=angle;
			}
			
		}
		
		return closest;
	
		 
	}
	
	public Direction getOppositeDirection()
	{
		return getLeftNeightbor().getLeftNeightbor();
		//TODO There is probably a real slick way to do this
		/*if(direction==NORTHEAST)
			return SOUTHWEST;
		else if(direction==SOUTHEAST)
			return NORTHWEST;
		else if(direction==SOUTHWEST)
			return NORTHEAST;
		return SOUTHEAST;*/
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() 
	{
		return this.directionName;
	}
	
	/**
	 * gets the tiletype that has the string representation of name, NONE otherwise
	 * @param name string represeting the tiletype
	 * @return tiletype for the string
	 */
	static public Direction getDirection(String name)
	{
		for (Direction t:Direction.values())
		{
			if(t.toString().equals(name))
			{
				return t;
			}
		}
		return Direction.SOUTHEAST;
	}
	
	public static String[] getDirectionNames()
	{
		String[] names = new String[Direction.values().length];
		int i =0;
		for (Direction t:Direction.values())
		{
			names[i]=t.toString();
			i++;
		}
		return names;
	}
	
	public static Stream<Direction> stream()
	{
		return Stream.of(values());
	}
	
	public CKPosition getAheadPosition(CKPosition pos,int maxWidth,int maxHeight)
	{
		int x = (int)pos.getX()+(dx);
		int y = (int)pos.getY()+(dy);
		if(x<0 | y<0 | x>maxWidth-1 | y>maxHeight-1)
		{
			return (CKPosition) pos.clone();
		}
		return new CKPosition(x,y,pos.getZ());
		
	}
	
	
	public CKPosition getAhead(CKPosition pos, int numberOfSquares, CKGrid grid)
	{
		int x = (int)pos.getX()+(dx*numberOfSquares);
		int y = (int)pos.getY()+(dy*numberOfSquares);
		//CKGridPosition nextPos = grid.getPositionFromList((int)pos.getX()+(dx*numberOfSquares),(int)pos.getY()+(dy*numberOfSquares));
		if(x<0 | y<0 | x>grid.getWidth()-1 | y>grid.getHeight()-1)
		{
			//opposite Direction
			//System.out.println("HEY! LOOK AT MEEE from DIrection" + pos);
			return pos;
		}
		//return nextPos;
		return new CKPosition((int)pos.getX()+(dx*numberOfSquares),(int)pos.getY()+(dy*numberOfSquares));
				//grid.getPositionFromList((int)pos.getX()+(dx*numberOfSquares),(int)pos.getY()+(dy*numberOfSquares));
	}
	
	public CKPosition getAhead(CKPosition ckPosition, CKGrid grid)
	{
		return getAheadPosition(ckPosition, grid.getWidth(),grid.getHeight());
	}
	
}
