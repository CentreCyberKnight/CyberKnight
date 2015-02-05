/**
 * 
 */
package ckGameEngine.actions;

import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKGameObjectsFacade;

/**
 * @author dragonlord
 *
 */
public class CKRemoveActorCmd extends CKQuestAction 
{
	


	
	private static final long serialVersionUID = -6822390508048805078L;
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Remove Actor";
	}

	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{	
		CKAbstractGridItem item = cast.getItemTarget();
		if(cast.getActorTarget()!=null)
		{
			cast.getActorTarget().getTurnController().onRemove();
		}
		
		item.removeItemFromGrid(CKGameObjectsFacade.getQuest().getGrid());	
	}

	
}


	
	