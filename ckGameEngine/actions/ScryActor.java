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
		String action ="scry";
		String resultType = cast.getKey().toLowerCase();
		switch (resultType)
		{
			case "cyberPoints":
				cast.addResult(cast.getItemTarget(),action,resultType,cast.getActorTarget().getCyberPoints());
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