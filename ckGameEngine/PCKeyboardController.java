package ckGameEngine;

import ckGameEngine.CKGameObjectsFacade;
import static ckGameEngine.CKGameObjectsFacade.*;

@Deprecated
public class PCKeyboardController extends ActorController
{

	
	
	
	
	public PCKeyboardController(CKGridActor ch)
	{
		super(ch);

	}

	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		Quest w = getQuest();	
		w.waitForInput();
		CKGameObjectsFacade.getEngine().blockTilActionsComplete();
	}

}
