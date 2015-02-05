package ckGameEngine;

public interface CKTurnListener
{
	/**
	 * 
	 * @param cast
	 * @return true if the listener should be removed
	 */
	public boolean turnEvent(CKSpellCast cast);
}
