package ckGameEngine;

import ckGameEngine.CKGameObjectsFacade;
import ckPythonInterpreter.CKPlayerObjectsFacade;
import static ckGameEngine.CKGameObjectsFacade.*;

public class ActorArtifactController extends ActorController
{

	public ActorArtifactController()
	{
		this(null,ActorController.NO_CONTROL);
	}
	
	
	
	public ActorArtifactController(CKGridActor pc,String permissions)
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

		
		Quest w = getQuest();
		
		w.startTransaction();
		enableArtifactInput();

//		w.waitForInput();//this is satisfied by the completion of the python code running
	
		getConsole().waitForCompletion();
		//FIXME

		
		disableArtifactInput(); //just in case should already be disabled
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
