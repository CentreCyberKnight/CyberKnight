/**
 * 
 */
package ckGameEngine;

/**
 * @author Nate
 *
 */
public final class ActorNullController extends ActorController 
{
	
	public ActorNullController()
	{
		super();
	}
	
	public ActorNullController(CKGridActor pc)
	{
		super(pc);
	}
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.PCTurnController#takeTurn()
	 */
	@Override
	protected final void takeMyTurn() {

	}

}
