/**
 * 
 */
package ckGameEngine.actions;

import java.util.Iterator;

import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
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
/*
 Going to change heavily because I can't tell at all what some person was trying to do here
 */

	public void switchHandler(CKSpellCast cast)
	{
		String action ="scry";//+cast.getKey().toLowerCase();
		String page = cast.getPage();
		String key  = cast.getKey();
		String resultType = cast.getKey().toLowerCase();		
		switch (page)
		{
		case "visible":
			//for visible I'm going to hardcode in the page that it should be because I will probably change it later
			System.out.println("Scry action called");			
			cast.addResult(cast.getItemTarget(),action,"height",
					cast.getItemTarget().getTotalHeight());
			cast.addResult(cast.getItemTarget(),action,"move",cast.getItemTarget().getMoveCost());
			cast.addResult(cast.getItemTarget(),action,"slide",cast.getItemTarget().getSlideCost());
			cast.addResult(cast.getItemTarget(),"name",cast.getItemTarget().getName(),1);
			break;
		/*case "trap":
			cast.addResult(cast.getItemTarget(),action,page,
			cast.getActorTarget().getAbilities().hasPage("trait", "trap"));
			break;*/
		case "traits":						
			CKPage test;
			Iterator<CKPage> iterator;
			if(cast.getItemTarget() instanceof CKGridActor){
				iterator=cast.getActorTarget().getAbilities().getChapter("traits").getPages();
				while(iterator.hasNext())
				{
					test=iterator.next();										
					cast.addResult(cast.getItemTarget(),action,test.getName(),
							cast.getActorTarget().getAbilities().hasPage("traits", test.getName()));
				}
			}
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