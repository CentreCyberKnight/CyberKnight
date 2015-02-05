package ckSatisfies;

import ckGameEngine.CKSpellCast;

public class FalseSatisfies extends Satisfies
{

	
	private static final long serialVersionUID = -1159607244944832649L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "False Satisfies";
	}

	public FalseSatisfies()
	{
		
	}
	
	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		return false;
	}

}
