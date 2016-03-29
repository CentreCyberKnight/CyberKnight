package ckGameEngine;

import ckGameEngine.actions.CKGameActionListener;

public class OngoingEffectController extends ActorController implements CKTurnListener
{

	public OngoingEffectController()
	{
		this(null);
	}
	
	
	
	public OngoingEffectController(CKGridActor pc)
	{
		super(pc);
	}



	@Override
	final protected void takeMyTurn()
	{ //do nothing, I take other turns 		
	}



	@Override
	public boolean turnEvent(CKSpellCast cast)
	{
		System.err.println("turn event occurs");
		
		CKGameActionListener boss = new CKGameActionListener();
		getActor().targetSpell(boss, cast);
		return false;
	}

	public CKGridActor actorUnderneath()
	{
		
		CKAbstractGridItem item = getActor().prev;
		if(item == null || ! (item instanceof CKGridActor))
		{
			return null;
		}
		return (CKGridActor) item; 		
	}
	

	/* (non-Javadoc)
	 * @see ckGameEngine.ActorController#onLoad()
	 */
	@Override
	public void onLoad()
	{
		super.onLoad();
		
		CKGridActor actor = actorUnderneath();
		if(actor ==null) { return; }
		actor.getTurnController().addTurnListener(this);
	}



	/* (non-Javadoc)
	 * @see ckGameEngine.ActorController#addTurnListener(ckGameEngine.CKTurnListener)
	 */
	@Override
	public void addTurnListener(CKTurnListener l)
	{
		CKGridActor actor = actorUnderneath();
		if(actor ==null) 
		{
			super.addTurnListener(l);
		}
		actor.getTurnController().addTurnListener(l);
		
	}



	/* (non-Javadoc)
	 * @see ckGameEngine.ActorController#removeTurnListener(ckGameEngine.CKTurnListener)
	 */
	@Override
	public void removeTurnListener(CKTurnListener l)
	{
		CKGridActor actor = actorUnderneath();
		if(actor ==null) 
		{
			super.removeTurnListener(l);
		}
		actor.getTurnController().removeTurnListener(l);
		
		
	}



	/* (non-Javadoc)
	 * @see ckGameEngine.ActorController#onRemove()
	 */
	@Override
	public void onRemove()
	{
		//we do nothing special.  
		//All "death" effects for an ongoing effect should be handled in the call to die.
	}

	
	
	
	
	

	
}
