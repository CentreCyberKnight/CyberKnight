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
		switch (cast.getKey().toLowerCase())
		{
		case "height":
			cast.setResult(Integer.toString(cast.getItemTarget().getTotalHeight()));
			break;
		case "move":
			cast.setResult(Integer.toString(cast.getItemTarget().getMoveCost()));	
			break;
		case "slide":
			cast.setResult(Integer.toString(cast.getItemTarget().getSlideCost()));
			break;
		case "name":
			cast.setResult(cast.getItemTarget().getName());
			break;
		case "trap":
			 if(cast.getActorTarget().getAbilities().hasPage("trait", "trap"))
			  {
			  		//CKSpellCast cry = voice("talk", 0, cast.getPCSource().getPos(), "It's a trap!");
			  }
			 break;

			
		}
	}

	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);
		switchHandler(cast);
		cast.getResult();
		notifyListener();
		
	}

}