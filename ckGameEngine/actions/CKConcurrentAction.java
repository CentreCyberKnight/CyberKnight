package ckGameEngine.actions;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

public class CKConcurrentAction extends CKCompoundGameAction
{
	
	private static final long serialVersionUID = 6923528449215700767L;
	//Vector<CKGameAction> actions;
	volatile int completed;
	
	
	public CKConcurrentAction()
	{
		super();
		//actions = new Vector<CKGameAction>();
		completed = 0;
	}
	
	
	
	@Override
	public synchronized void actionCompleted(CKGameAction action)
	{
		completed++;
		if(isCompleted()) {notifyListener();}

	}

	
	
	
	
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		replaceListener(L);
		completed = 0;
		
		for (Object obj:children)
		{
			CKGameAction act = (CKGameAction) obj;
			act.doAction(this,cast);
		}
		CKGameObjectsFacade.getQuest().endTransaction();
		if(isCompleted()) { notifyListener(); }

	}
	
	private boolean isCompleted()
	{
		return completed == children.size();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Concurrent Action:"+getName();
	}
	
	

}
