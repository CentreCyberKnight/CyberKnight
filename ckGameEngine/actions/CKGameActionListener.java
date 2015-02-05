package ckGameEngine.actions;

import ckCommonUtils.CKLock;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

public class CKGameActionListener implements CKGameActionListenerInterface
{

	
	private final CKLock runningLock = new CKLock(); 
	private final CKLock waitingLock = new CKLock(); 
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListenerInterface#runAction(ckGameEngine.actions.CKGameAction, ckGameEngine.CKSpellCast)
	 */
	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		try
		{
		runningLock.lock();           //only allow one action to play at a time
		waitingLock.lock();           //this lock is for the action to notify completion
		
		CKGameObjectsFacade.getQuest().startTransaction();
		act.doAction(this,cast);
		CKGameObjectsFacade.getQuest().endTransaction();
		//MKB
		
		waitingLock.lock();           //must be unlocked by action for me to get it
		}
		finally                       //cleanup
		{
			waitingLock.unlock();
			runningLock.unlock();
		}
		//both locks will be unlocked at this point.
		
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListenerInterface#actionCompleted(ckGameEngine.actions.CKGameAction)
	 */
	@Override
	public void actionCompleted(CKGameAction action)
	{
		waitingLock.unlock();
		
	}

	


	
	
}
