/**
 * 
 */
package ckGameEngine;

/**
 * @author Nate
 *
 */
public class ActorTurnController extends ActorController {

	
	public ActorTurnController(CKGridActor pc)
	{
		super(pc);
	}
	
	public ActorTurnController()
	{
	super();
	}

	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.PCTurnController#takeTurn()
	 */
	@Override
	protected void takeMyTurn() {

	}

	/* (non-Javadoc)
	 * @see ckGameEngine.ActorController#getInitialTurnEvent()
	 */
	@Override
	public Event getInitialTurnEvent(double start)
	{
		return new ActorEvent(getActor(),getActor().calcTimeToNextTurn(start));
	}
	
	

	
	

}
