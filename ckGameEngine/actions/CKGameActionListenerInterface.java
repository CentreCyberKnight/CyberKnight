package ckGameEngine.actions;

import ckGameEngine.CKSpellCast;

public interface CKGameActionListenerInterface
{
	
	/**
	 * The listener will perform the action and block until the action uses the call back
	 * actionCompleted
	 * @param act
	 * @param cast
	 */
	public void runAction(CKGameAction act,CKSpellCast cast);
	
	
	/**
	 * A callback for an action to notify that it has completed its job
	 * 
	 * @param action
	 */
	public void actionCompleted(CKGameAction action);


}
