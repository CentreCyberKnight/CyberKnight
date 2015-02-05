package ckGameEngine;

/**
 * 
 * @author CyberKnight Graphics Team
 * Nathan, Tina, and Phillip
 *
 */
public abstract class Tile
{
	/**
	 * Private Data Members
	 */
	//private String[] imageName;
	private boolean hasEffect;
	
	/**
	 * Getter for hasEffect
	 * @param None
	 * @pre 
	 * @post
	 * @calls None
	 * @calledBy
	 * @return hasEffect
	 */
	public boolean hasEffect()
	{
		return hasEffect;
	}
	
	/**
	 * needs to be implemented still
	 * @param
	 * @pre 
	 * @post
	 * @calls
	 * @calledBy
	 * @returns
	 */
	public abstract void placeEffect();
}
