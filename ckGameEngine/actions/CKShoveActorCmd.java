/**
 * 
 */
package ckGameEngine.actions;

import static ckCommonUtils.CKPropertyStrings.P_PULL;
import static ckCommonUtils.CKPropertyStrings.P_SHOVE;

import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;


/**
 * @author dragonlord
 *
 */
public class CKShoveActorCmd extends CKQuestAction
{
	
	

	private static final long serialVersionUID = 1094846919794057731L;



	public CKShoveActorCmd()
	{
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target = cast.getActorTarget();
		CKAbstractGridItem source= cast.getSource();
		Direction dir;
		
		String page = cast.getPage();
		
		if(page.compareToIgnoreCase(P_SHOVE)==0)
			{
			dir = Direction.getDirectionTo(source.getPos(),target.getPos()); 
			//CKGridActor actor = (CKGridActor) source;
			//dir = actor.getDirection();
			shove(target,dir,cast.getCp());
			}
		else if (page.compareToIgnoreCase(P_PULL)==0)
			{	
				dir = Direction.getDirectionTo(target.getPos(),source.getPos()); 
				shove(target,dir,cast.getCp());			
			}
		
		
	}
	
	
	public void shove(CKGridActor target,Direction dir,int CP)
	{

		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		int presentCP = CP - grid.costSlide(target,dir);
		System.out.println("shove is called");

		 
		while(presentCP >=0)
		{
			//TODO will need to alter this to handle graphics for each type of movement
			//1 move
			//2 jump down      
			//3 jump over         /\
				int endTime = grid.move(target, dir);
				CKGameObjectsFacade.getQuest().setStartTime(endTime);
				
				if(presentCP ==0) { break; }
				
				presentCP = presentCP - grid.costSlide(target, dir);
		}
		
		
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Shove Action";
	}
	
	

}
