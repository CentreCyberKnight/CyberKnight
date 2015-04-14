package ckGameEngine.actions;

import testing.CKGridItemSet;
import ckCommonUtils.CKPosition;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CKGraphicsEngine;
import ckGraphicsEngine.CKGraphicsEngine.RelationalLinkType;
import ckGraphicsEngine.CircularDependanceError;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKSpellActionOld extends CKGameAction implements CKGameActionListenerInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8054374774106602058L;

	
	public CKSpellActionOld(){
		
	}



	

	
	/**
	 * provides the graphics for the act of casting the spell.
	 * 
	 *  needs to return the instance of the effect.
	 * 
	 * 
	 * @param startingPos
	 * @param cast
	 * @return the id of the casting effect's instance, -1 if there is no effect to remove later.
	 */
	public int doCasterGraphics(CKPosition startingPos, CKSpellCast cast)
	{
		int sourceEffect=-1;
		try
		{
			sourceEffect = engine.createInstance(0,"Swirl", startingPos,quest.getStartTime(), 
					CKGraphicsLayer.REARHIGHLIGHT_LAYER);
		} catch (LoadAssetError e)
		{
			e.printStackTrace();
		}
		return sourceEffect;
		
	}



public int doFirstTravelGraphics(CKPosition startingPos, CKSpellCast cast)
	{
	int travelTime=quest.getStartTime();
	try
	{
		int travel1Effect = engine.createInstance(0, "orb2", startingPos, quest.getStartTime(), 
				CKGraphicsLayer.FRONTHIGHTLIGHT_LAYER);
		
		travelTime = engine.move(0,travel1Effect, quest.getStartTime()+45,
				startingPos, cast.getTargetPosition(),10);

		engine.destroy(0,travel1Effect,travelTime);
	} catch (LoadAssetError e)
	{
		e.printStackTrace();
	} catch (BadInstanceIDError e)
	{
		e.printStackTrace();
	}
	
	return travelTime;
		
	}
	

public int doSecondTravelGraphics(CKPosition startingPos, CKSpellCast cast)
{
	return doFirstTravelGraphics(startingPos,cast);
}
	
public final int EFFECT_FAILS=0;
public final int EFFECT_SUCCEEDS=1;

protected int doEffect(CKSpellCast cast)
{
	
	return EFFECT_SUCCEEDS;
}




protected void doEffectGraphics(int success, CKSpellCast cast, int travelTime)
{
	//Should do ignition effect
	CKGridActor pc = cast.getActorTarget();

	
	
	if(pc != null)
	{
		try
		{
			int targetEffect = engine.createInstance(0,"illuminate", cast.getTargetPosition(),
				travelTime,//q.getStartTime(), 
				CKGraphicsLayer.REARHIGHLIGHT_LAYER);
			engine.linkGraphics(0, targetEffect, pc.getInstanceID(), 
				RelationalLinkType.RELATIVE, quest.getStartTime());
		} catch (LoadAssetError e)
		{
			e.printStackTrace();
		} catch (BadInstanceIDError e)
		{
			e.printStackTrace();
		} catch (CircularDependanceError e)
		{
			e.printStackTrace();
		}
	}
	
}
	
	Quest quest;
	CKGraphicsEngine engine;
	
	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
			 quest = CKGameObjectsFacade.getQuest();
			 engine = CKGameObjectsFacade.getEngine();

			 boolean notRedirected = cast.getRedirect()==null;
			 
			try
			{
				//if no redirect
				int sourceEffect = -1;
				int travelTime = -1;
				CKPosition startingPos;
				if(notRedirected)
				{
					quest.startTransaction(); 
					replaceListener(L);					
					startingPos=cast.getSource().getPos();
					
					sourceEffect = doCasterGraphics(startingPos,cast);
					travelTime = doFirstTravelGraphics(startingPos,cast);
					quest.setStartTime(travelTime); //only store the time if your are in the first leg.

					if(sourceEffect != -1l)  	{ engine.destroy(0, sourceEffect, travelTime); 	}
					
				}
				else
				{
					startingPos = cast.getRedirect();
					travelTime = doSecondTravelGraphics(startingPos,cast);
				}
					
			if(cast.getItemTarget() instanceof CKGridItemSet)
			{
				doRedirectEffects(cast);
				doAlterCast(cast);
				
				CKGridItemSet set = (CKGridItemSet) cast.getItemTarget();
				
				for(CKAbstractGridItem pos: set.getItems())
				{
					CKSpellCast c2 = new CKSpellCast(pos,cast.getSource(), 
							cast.getChapter(),cast.getPage(),cast.getCp(),
							cast.getKey());
					c2.setRedirect(set.getPos());
					c2.castSpell(this, "");
				}
			}
			else
			{  
				int success = doEffect(cast);
				doEffectGraphics(success,cast,travelTime);
			}
			
			
			} catch (BadInstanceIDError e)
			{
				e.printStackTrace();
			}
			if(notRedirected)
				{
				Thread T = new waitForGraphicsToFinish(this,this);
				T.start();
				}

		}

	//this would be a nice place to alter the CP used on redirect (or each redirect)
	protected void doAlterCast(CKSpellCast cast)
	{
		
		
	}


	//handle any effect that should happen when the spell redirects
	protected void doRedirectEffects(CKSpellCast cast)
	{
		
	}



	@Override
	public void actionCompleted(CKGameAction action)
	{
		notifyListener();		
	}
	
	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		// TODO Auto-generated method stub
		act.doAction(this, cast);
		
	}



	


}