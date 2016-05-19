package ckGameEngine.actions;

import java.util.Iterator;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

public class CKSequentialAction extends CKCompoundGameAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1369167558151858392L;
	//Vector<CKGameAction> actions;
	Iterator<CKGameAction> iter;
	//Iterator<Object> iter;
	CKSpellCast cast;
	
	public CKSequentialAction()
	{
		super();
		//actions = new Vector<CKGameAction>();
		iter = null;
		cast = null;
	}
	
	
	@Override
	public void actionCompleted(CKGameAction action)
	{
		doNextAction();	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{//TODO will cause errors if this is called twice!
		replaceListener(L);
		iter = children.iterator();
		this.cast = cast;
		doNextAction();
	}

	private void doNextAction()
	{
		if(iter.hasNext())
		{
			CKGameAction act = iter.next();
			CKGameObjectsFacade.getQuest().startTransaction();
			act.doAction(this,cast,true);//not sure if this is needed
			//act.doAction(this,cast);
			CKGameObjectsFacade.getQuest().endTransaction();
		}
		else
		{
			notifyListener();
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Sequential Action:"+getName();
	}

	
	
}
