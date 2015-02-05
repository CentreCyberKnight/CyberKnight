package ckSatisfies;

import ckGameEngine.CKSpellCast;

public class TrueSatisfies extends Satisfies {

	
	private static final long serialVersionUID = 8921599223947316415L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "True Satisfies";
	}

	public TrueSatisfies()
	{

	}
	
	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{
		return true;
	}

	
}
