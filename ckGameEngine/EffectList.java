package ckGameEngine;
import java.util.ArrayList;

/**
 * Creates the EffectList class
 * @author Nathan Blanchard, Tina Edge, Phillip Dold
 *
 */
@Deprecated
public class EffectList {
	//this was implemented with an ArrayList but Java is having problems with it
	//since we are currently not using effects I've just commented it out to avoid
	//errors, but this needs to be addressed
	ArrayList/**<Effect>*/<Object> effects;
	
	/**
	 * This will sort the effects begining with the effect that will expire first
	 * The thought is that delete expired will then run in constant time,
	 * just checking the first effect to see if it is expired with a quick while loop
	 * @param None
	 * @return None
	 *
	 */
	public void sortEffects()
	{
		//sort effects - TBI
		//by increasing expirationCount
	}
	
	/**
	 * Deletes any expired effects currently on a character
	 * for this to work, 
	 * NEEDS TO HAVE sortEffects() called before this
	 */
	public void deleteExpired()
	{
		/*
		sortEffects();
		while(effects.get(0).isExpired())
		{
			effects.remove(0);
		}
		*/
	}
	
	/**
	 * this function will calcuate any damage done from effects.
	 * Currently the implementation just adds all damage together and 
	 * inflicts it all at once. 
	 * @return damage - total damage done by all effects
	 */
	public int damageCountdown()
	{
		
		int damage = 0;
		/*
		for(int i = 0; i < effects.size(); i++)
		{
			Effect thisEffect = effects.get(i);
			thisEffect.countdown();
			damage += thisEffect.damage;
		}
		*/
		return damage;
	}
}
