package ckGameEngine;

/**
 * @author 
 *Creates the Effect Class
 *
 */
public class Effect {
	int expireCount;
	int decayRate;
	int damage;
	
	/**
	 * A method that determines if an effect is expired or not
	 * @pre the effect must exist
	 * @post function will return a boolian
	 * @param None
	 * @return returns a boolean based on if the expirecount is less than or equal to zero.
	 */
	public boolean isExpired()
	{
		return expireCount <= 0;
	}
	
	/**
	 * A method that returns the expireCount 
	 * Pre: 
	 * Post:
	 * @param None
	 * @return returns an integer that is the expireCount
	 */
	public int getExpireCount()
	{
		return expireCount;
	}
	
	/**
	 * A method that countdown how much time is left on the effect by using decayRate
	 * to subtract from expireCount
	 * @param None
	 * @return None
	 * 
	 */
	public void countdown()
	{
		expireCount -= decayRate;
	}
}
