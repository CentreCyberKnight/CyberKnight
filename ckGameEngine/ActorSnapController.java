package ckGameEngine;

import ckGameEngine.CKGameObjectsFacade;
import ckPythonInterpreter.CKPlayerObjectsFacade;
import static ckGameEngine.CKGameObjectsFacade.*;

public class ActorSnapController extends ActorController
{

	public ActorSnapController()
	{
		this(null,ActorController.NO_CONTROL);
	}
	
	
	
	public ActorSnapController(CKGridActor pc,String permissions)
	{
		super(pc);
		this.permissions = permissions;

	}

	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		CKGameObjectsFacade.setCurrentPlayer(getActor());
		CKPlayerObjectsFacade.calcCPTurnLimit();
		//FIXME need to some how set the current actor in the model as well.
		
		Quest w = getQuest();
		
		w.startTransaction();
		//enableArtifactInput(); - should be handled by the GUI now.

//		w.waitForInput();//this is satisfied by the completion of the python code running
	
		getConsole().waitForCompletion();
		//FIXME

		
		
		w.endTransaction();
		CKGameObjectsFacade.getEngine().blockTilActionsComplete();	
		getActor().setCPConsumedLastRound(CKPlayerObjectsFacade.getCPTurnMax() - CKPlayerObjectsFacade.getCPTurnRemaining());
		
		CKGameObjectsFacade.setCurrentPlayer(null);
		//main thread will run through here.
	}

	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#getInitialTurnEvent()
	 */
	@Override
	public Event getInitialTurnEvent(double time)
	{
		
		//FIXME need to evaluate the 2 based on speed
		return new ActorEvent(getActor(),getActor().calcTimeToNextTurn(time));
	}

	

	
}
