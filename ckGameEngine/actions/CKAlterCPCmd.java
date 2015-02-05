package ckGameEngine.actions;

import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

final public class CKAlterCPCmd extends CKGameAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */	
	@Override
	public String toString()
	{
		//return "Giving "+spell.getActorTarget() + " " + spell.getCp()+" Cyber Points";
		return "Alter CP";
		
	}

	
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		System.err.println("         altering "+cast.getItemTarget().getName()+" CP by:"+cast.getCp());
		CKGridActor actor = cast.getActorTarget();
		if(actor != null)
		{
			actor.setCyberPoints(actor.getCyberPoints() + cast.getCp());
		}
		
		L.actionCompleted(this);
	}


}
