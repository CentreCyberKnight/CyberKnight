/**
 * 
 */
package ckGameEngine.actions;

import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class ScryAction extends CKGameAction 
{
	//private NumericalCostType t = NumericalCostType.FALSE;


	
	
	private static final long serialVersionUID = -1112825497910646743L;

	public ScryAction() {}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Scrying";
	}


	public void switchHandler(CKSpellCast cast)
	{
		String action ="scry";//+cast.getKey().toLowerCase();
		String resultType = cast.getKey().toLowerCase();  
		switch (resultType)
		{
		case "height":
			cast.addResult(cast.getItemTarget(),action,resultType,
					cast.getItemTarget().getTotalHeight());
			break;
		case "move":
			cast.addResult(cast.getItemTarget(),action,resultType,cast.getItemTarget().getMoveCost());	
			break;
		case "slide":
			cast.addResult(cast.getItemTarget(),action,resultType,cast.getItemTarget().getSlideCost());
			break;
		case "name":
			cast.addResult(cast.getItemTarget(),action+":name",cast.getItemTarget().getName(),1);
			break;
		case "trap":
			cast.addResult(cast.getItemTarget(),action,resultType,
			cast.getActorTarget().getAbilities().hasPage("trait", "trap"));
			 			 break;

			
		}
	}

	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);
		switchHandler(cast);
		//cast.getResult();  FIXME don't think this does anything...
		notifyListener();
		
	}

}