/**
 * 
 */
package ckGameEngine.actions;

import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_JUMP_DOWN;
import static ckCommonUtils.CKPropertyStrings.P_JUMP_OVER;
import static ckCommonUtils.CKPropertyStrings.P_JUMP_UP;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;


/**
 * @author dragonlord
 *
 */
public class CKMoveActorCmd extends CKQuestAction
{
	
	

	private static final long serialVersionUID = 1094846919794057731L;



	public CKMoveActorCmd()
	{
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target = cast.getActorTarget(); 

		String page = cast.getPage();
		
		if(page.compareToIgnoreCase(P_FORWARD)==0)
			{	moveForward(target,cast.getCp());      	}
		else if (page.compareToIgnoreCase(P_LEFT)==0)
			{	turnLeft(target,cast.getCp());                  }
		else if (page.compareToIgnoreCase(P_RIGHT)==0)
			{	turnRight(target,cast.getCp());                }   
		else if (page.compareToIgnoreCase(P_JUMP_UP)==0)
			{	jumpUp(target,cast.getCp());}
		else if (page.compareToIgnoreCase(P_JUMP_OVER)==0)
			{	jumpOver(target,cast.getCp());}
		else if (page.compareToIgnoreCase(P_JUMP_DOWN)==0)
			{	jumpDown(target,cast.getCp());}
					
		
	}
	
	
	
	
	
	private void jumpDown(CKGridActor target, int cp)
	{
		Direction direction = target.getDirection();
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		
		int cost = grid.costJumpUp(target, direction, false);
		if( cost <=cp)
		{
			int endTime = grid.move(target, target.getDirection());
			CKGameObjectsFacade.getQuest().setStartTime(endTime);			
		}
		//else the jump fails
		//TODO print out failure
			
	}




	private void jumpOver(CKGridActor target, int cp)
	{
		Direction direction = target.getDirection();
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		
		int squares = grid.calcJumpOverDistance(target, direction,cp);
		/*
		 * if squares==0, the effect fails
		 */
		//must calculate final position to avoid touching tiles inbetween.
		CKPosition pos = target.getPos();
		pos.setX(pos.getX() + (squares*direction.dx));
		pos.setY(pos.getY() + (squares*direction.dy));
		
		
		
		int endTime = grid.move(target, pos);
		CKGameObjectsFacade.getQuest().setStartTime(endTime);			
		
	}




	private void jumpUp(CKGridActor target, int cp)
	{
		Direction direction = target.getDirection();
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		
		int cost = grid.costJumpUp(target, direction, true);
		if( cost <=cp)
		{
			int endTime = grid.move(target, target.getDirection());
			CKGameObjectsFacade.getQuest().setStartTime(endTime);			
		}
		//else the jump fails
		//TODO print out failure
		
	}




	public void turnLeft(CKGridActor target,int CP)
	{
		Direction direction = target.getDirection();
		for(int i =0;i<CP;i++)
		{
			direction = direction.getLeftNeightbor();
		}
		
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		grid.setDirection(target, direction);
	}
	
	
	public void turnRight(CKGridActor target,int CP)
	{
		Direction direction = target.getDirection();
		for(int i =0;i<CP;i++)
		{
			direction = direction.getRightNeightbor();
		}
		
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		grid.setDirection(target, direction);
	}
		
	
	public void moveForward(CKGridActor target,int CP)
	{

		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		int presentCP = CP - grid.costStep(target, target.getDirection()); 
		 
		while(presentCP >=0)
		{
				int endTime = grid.move(target, target.getDirection());
				CKGameObjectsFacade.getQuest().setStartTime(endTime);
				
				if(presentCP ==0) { break; }
				
				presentCP = presentCP - grid.costStep(target, target.getDirection());
		}
		
		
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Move Action Command";
	}
	
	

}
