package ckGameEngine.actions;

import java.util.Iterator;
import java.util.Random;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

public class CKRandomAction extends CKCompoundGameAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1369167558151858392L;
	//Vector<CKGameAction> actions;
	Iterator<CKGameAction> iter;
	//Iterator<Object> iter;
	CKSpellCast cast;
	
	
	
	
	public CKRandomAction()
	{
		super();
		//actions = new Vector<CKGameAction>();
		iter = null;
		cast = null;
	}
	
	
	@Override
	public void actionCompleted(CKGameAction action)
	{
		notifyListener();
	}
	
	
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		replaceListener(L);
		//iter = children.iterator();
		this.cast = cast;
		doNextAction();
	}

	private void doNextAction()
	{
		/*CKGameAction[] storage=new CKGameAction[maxRandom];
		int acc=0;

		Boolean trying=false;
		while(iter.hasNext())
		{
			CKGameAction act = iter.next();			
			//CKGameObjectsFacade.getQuest().startTransaction();		
			storage[acc] = act;		
			//
			//act.doAction(this,cast);
			//CKGameObjectsFacade.getQuest().endTransaction();
			acc=acc+1;
		}		
		if(acc!=0 && !iter.hasNext()){	*/		
		Random num=new Random();
		int rand=num.nextInt(children.size());
			/*System.out.println(acc);
			System.out.println("<<<<<<<<<"+rand+">>>>>>>>>");*/
			CKGameAction t=(CKGameAction) children.get(rand);
			CKGameObjectsFacade.getQuest().startTransaction();
			t.doAction(this,cast,true);//not sure if this is needed
			CKGameObjectsFacade.getQuest().endTransaction();			
			//trying=true;
		//}		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Random Action:"+getName();
	}

	
	
}
