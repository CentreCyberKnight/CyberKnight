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
		String page = cast.getPage();
		String key  = cast.getKey();
		
//		String resultType = cast.getKey().toLowerCase();  
		switch (page)
		{
		case "height":
			cast.addResult(cast.getItemTarget(),action,page,
					cast.getItemTarget().getTotalHeight());
			break;
		case "move":
			cast.addResult(cast.getItemTarget(),action,page,cast.getItemTarget().getMoveCost());	
			break;
		case "slide":
			cast.addResult(cast.getItemTarget(),action,page,cast.getItemTarget().getSlideCost());
			break;
		case "name":
			cast.addResult(cast.getItemTarget(),page,cast.getItemTarget().getName(),1);
			break;
		/*case "trap":
			cast.addResult(cast.getItemTarget(),action,page,
			cast.getActorTarget().getAbilities().hasPage("trait", "trap"));
			break;*/
		case "trait":
			cast.addResult(cast.getItemTarget(),action,key,
			cast.getActorTarget().getAbilities().hasPage("trait", key));
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