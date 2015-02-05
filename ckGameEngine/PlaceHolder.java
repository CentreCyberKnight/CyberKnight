package ckGameEngine;
import ckCommonUtils.*;

/**
 * @author CyberKnight Graphics Team
 * Nathan, Tina, Phillip
 *
 */


public class PlaceHolder 
{
	private CKGridPosition position;
	
	/**
	 * returns the position
	 * @pre 
	 * @post
	 * @param None
	 * @return position
	 * @calls
	 * @calledBy
	 * 
	 */
	public CKGridPosition getPosition()
	{
		return position;
	}
	
	/**
	 * Sets the Position of the Placeholder by getting private Data member
	 * @pre 
	 * @post
	 * @param pos
	 * @calls Nnone
	 * @calledBy
	 */
	public void setPosition(CKGridPosition pos)
	{
		this.position = pos;
	}
	
	/**
	 * checks out a Position for the Placeholder object to a tennent
	 * @pre 
	 * @post
	 * @param pos
	 * @calls setPlaceHolder
	 * @calledBy
	 * 
	 */
	public boolean checkOutPosition(CKGridPosition pos)
	{
		//System.out.println(pos.getPlaceHolder());
		if(pos.getPlaceHolder()==null)
		{
			pos.setPlaceHolder(this);
			this.setPosition(pos);
			return true;
		}
		return false;
	}
	
	/**
	 * checks in a position that is no longer in use, does this by setting to null
	 * @pre 
	 * @post
	 * @param None
	 * @returns None
	 * @calls setPlaceHolder
	 * @calledBy
	 * 
	 */
	public void checkInPosition()
	{
		this.position.setPlaceHolder(null);
		position = null;
	}
}
