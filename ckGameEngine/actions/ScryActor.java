/**
 * 
 */
package ckGameEngine.actions;

import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class ScryActor extends ScryAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ScryActor() {	}

	public void switchHandler(CKSpellCast cast)
	{
		super.switchHandler(cast);
		String action ="scry:"+cast.getKey().toLowerCase();
		switch (cast.getKey().toLowerCase())
		{
			case "hitpoints":
				cast.addResult(cast.getItemTarget(),action,Integer.toString(cast.getActorTarget().getCyberPoints()));
				break;
			//Currently no weakness list, so returns name of Actor
			case "weakness":
				cast.addResult(cast.getItemTarget(),action,cast.getActorTarget().getName());
				break;
		}
	}
	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);
		switchHandler(cast);
		notifyListener();

	}
	
}