package ckGameEngine.actions;

import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

public abstract class CKQuestAction extends CKGameAction 
implements CKGameActionListenerInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2485725293744454678L;

	
	

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListenerInterface#runAction(ckGameEngine.actions.CKGameAction, ckGameEngine.CKSpellCast)
	 */
	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		act.doAction(this,cast);
		
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListenerInterface#actionCompleted(ckGameEngine.actions.CKGameAction)
	 */
	@Override
	public void actionCompleted(CKGameAction action)
	{
		notifyListener();
		
	}

	@Override
	final public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		replaceListener(L);
		//System.out.println("starting this one"+toString());
		questDoAction(cast);
		
		notifyListener();
	//	Thread T = new waitForGraphicsToFinish(this,this);
	//	T.start();
	}
	
	abstract protected void questDoAction(CKSpellCast cast);
	
	
	protected CKGridActor getPC(String name)
	{
		
		return getQuest().getActor(name);
	}
	
	

}
