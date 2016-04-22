package ckGameEngine;

import java.util.Arrays;

import ckCommonUtils.CKPosition;

public class AimDescription
{
	int cp;
	CKPosition [] offsets;
	Direction direction = Direction.NONE;
	boolean autoResolve=false;
	double minDistance =0;
	double maxDistance=0;
	
	
	
	
	
	
	
	/**@param offsets     are the Positions where the effect will occur relative to the 
	 * origin of the effect					  	  
	 * @param direction   Lists the direction that the player is facing for the following offsets to take effect.
	 * If the players direction is not important this should be Direction.NONE.  Most will be initialized with Direction.SOUTHEAST
	 * @param autoResolve Can this be resolved without the user clicking on the destination (i.e. is there no choice in the matter)
	 * @param minDistance minimum distance from the source of the effect before an origin of the effect can be selected
	 * @param maxDistance minimum distance from the source of the effect before an origin of the effect can be selected
	 */
	public AimDescription(int cp,CKPosition[] offsets, Direction direction,
			boolean autoResolve, double minDistance, double maxDistance)
	{
		super();
		this.cp = cp;
		this.offsets = offsets;
		this.direction = direction;
		this.autoResolve = autoResolve;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}







	/**
	 * @return the cp
	 */
	public int getCp()
	{
		return cp;
	}







	/**
	 * @param cp the cp to set
	 */
	public void setCp(int cp)
	{
		this.cp = cp;
	}







	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "AimDescription [cp=" + cp + ", offsets="
				+ Arrays.toString(offsets) + ", direction=" + direction
				+ ", autoResolve=" + autoResolve + ", minDistance="
				+ minDistance + ", maxDistance=" + maxDistance + "]";
	}





	public static CKPosition[] calculateTarget(CKPosition origin,CKPosition[] offsets)
	{
		
		CKPosition [] ret = new CKPosition[offsets.length];
		for(int i =0;i<offsets.length;i++)
		{
			ret[i] = origin.add(offsets[i]);
		}
		return ret;
		
		
	}

	/**
	 * @return the offsets
	 */
	public CKPosition[] getOffsets()
	{
		return offsets;
	}


	public CKPosition[] getOffsets(Direction d)
	{
		if(d==direction || direction==Direction.NONE || d==Direction.NONE)
		{
			return offsets;
		}
		//else we need to create the offsets
		CKPosition [] ret = new CKPosition[offsets.length];
		double deg = direction.angleTo(d);
		for(int i =0;i<offsets.length;i++)
		{
			ret[i] = offsets[i].rotate(deg);
		}
		
		return ret;		
	}
	
	
	public CKPosition[] getInverse(Direction d)
	{
		CKPosition[] off = getOffsets(d);
		CKPosition [] ret = new CKPosition[off.length];
		for(int i =0;i<off.length;i++)
		{
			ret[i] = new CKPosition(-off[i].getX(),-off[i].getY());
		}
		return ret;
	}


	/**
	 * @param offsets the offsets to set
	 */
	public void setOffsets(CKPosition[] offsets)
	{
		this.offsets = offsets;
	}







	/**
	 * @return the direction
	 */
	public Direction getDirection()
	{
		return direction;
	}







	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}







	/**
	 * @return the autoResolve
	 */
	public boolean isAutoResolve()
	{
		return autoResolve;
	}







	/**
	 * @param autoResolve the autoResolve to set
	 */
	public void setAutoResolve(boolean autoResolve)
	{
		this.autoResolve = autoResolve;
	}







	/**
	 * @return the minDistance
	 */
	public double getMinDistance()
	{
		return minDistance;
	}







	/**
	 * @param minDistance the minDistance to set
	 */
	public void setMinDistance(double minDistance)
	{
		this.minDistance = minDistance;
	}







	/**
	 * @return the maxDistance
	 */
	public double getMaxDistance()
	{
		return maxDistance;
	}







	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(double maxDistance)
	{
		this.maxDistance = maxDistance;
	}


	
	
	
}
