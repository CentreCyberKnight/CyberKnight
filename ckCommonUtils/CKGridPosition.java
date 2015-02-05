package ckCommonUtils;

import ckGameEngine.*;
public class CKGridPosition extends CKPosition 
{
	

	Tile tile; //perhaps this could be in a subclass -Bradshaw
	PlaceHolder tennant;
	
	
	public CKGridPosition(double x,double y,double z,int depth)
	{
		super(x,y,z,depth);
		this.tennant=null;
		this.tile=null;
	}
	
	public CKGridPosition()
	{
		this(0,0,0,0);
	}
	
	public CKGridPosition(double x,double y)
	{
		this(x,y,0,0);
	}
	
	
	public CKGridPosition(double x,double y,double z)
	{
		this(x,y,z,0);
	}

	/**
	 * @return the tennant
	 */
	public PlaceHolder getPlaceHolder()
	{
		return tennant;
	}

	/**
	 *  @param tennent
	 */
	public void setPlaceHolder(PlaceHolder tennant)
	{
		this.tennant = tennant;
	}

//	@Override
//	public int compareTo(CKPosition that) {
//		if(this.getX()!=that.getX())
//		{
//			return false;
//		}
//	}
	
}
