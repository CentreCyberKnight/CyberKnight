/**
 * 
 */
package ckGameEngine.actions;

import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
@Deprecated
public class ScryTrait extends ScryAction
{
	//private NumericalCostType t = NumericalCostType.FALSE;


	
	
	private static final long serialVersionUID = -1112825497910646743L;

	public ScryTrait() {}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Scrying";
	}




	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);
		switch (cast.getKey().toLowerCase())
		{
		case "trap":
			

			 
			break;

			
		}
		cast.getResult();
		notifyListener();
		
	}

}