package ckGameEngine;

import static ckGameEngine.CKGameObjectsFacade.disableTextInput;
import static ckGameEngine.CKGameObjectsFacade.enableTextInput;
import static ckGameEngine.CKGameObjectsFacade.getQuest;

@Deprecated
public class PCTextController extends ActorController
{

	
	
	
	
	public PCTextController(CKGridActor pc)
	{
		super(pc);

	}


	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		Quest w = getQuest();
		w.startTransaction();
		enableTextInput();
		
		System.out.println("Text input enabled"+CKGameObjectsFacade.getCurrentPlayer());

		w.waitForInput();
		System.out.println("waited for text input");
		
		disableTextInput(); //just in case should already be disabled
		w.endTransaction();
		CKGameObjectsFacade.getEngine().blockTilActionsComplete();

		
		
		/*	focusCharacter();

			Quest w = getQuest();
			w.startTransaction();
			CKGameObjectsFacade.setCurrentPlayer(getCharacter());
			enableTextInput();
			
			System.out.println("Text input enabled"+CKGameObjectsFacade.getCurrentPlayer());

			w.waitForInput();
			System.out.println("waited for text input");
			
			disableTextInput(); //just in case should already be disabled
			CKGameObjectsFacade.setCurrentPlayer(null);
			w.endTransaction();
			CKGameObjectsFacade.getEngine().blockTilActionsComplete();
			*/			
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PCTextController for "+getActor().getName();
	}

	
}
