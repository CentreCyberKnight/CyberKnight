package ckGameEngine.actions;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.MutableTreeNode;

import testing.CKGridItemSet;
import ckCommonUtils.CKPosition;
import ckEditor.CKActorEffectAddMenu;
import ckEditor.CKTravelEffectAddMenu;
import ckEditor.treegui.CKActorEffect;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKGUINodeLabel;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTravelEffect;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.CKGraphicsEngine.RelationalLinkType;
import ckGraphicsEngine.CircularDependanceError;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKSpellAction extends CKGameAction implements
		CKGameActionListenerInterface
{

	private static final int CASTER_EFFECT = 0;
	private static final int FIRST_TRAVEL_EFFECT = 1;
	private static final int SECOND_TRAVEL_EFFECT = 2;
	private static final int SPELL_SUCCEEDS_EFFECT = 3;
	private static final int SPELL_FAILS_EFFECT = 4;
	private static final int SPELL_EFFECT = 6;

	private static final int ALTER_CAST = 8;
	private static final int SPELL_REDIRECT_EFFECT = 9;
	// private static final int SPELL_FAILS_EFFECT=17;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8054374774106602058L;

	public CKSpellAction()
	{
		this.setAllowsChildren(true);
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
		this.setPastableChildren(false);

		add(new CKActorEffect("Caster Effect"));
		add(new CKTravelEffect("First Travel Effect"));
		add(new CKTravelEffect("Second Travel Effect"));
		add(new CKActorEffect("Successful Effect Graphics"));
		add(new CKActorEffect("unSuccessful Effect Graphics"));

		add(new CKGUINodeLabel("Spell Effect"));
		add(new CKAlterCPCmd());
		add(new CKGUINodeLabel("Alter Cast"));
		add(new CKAlterSpellCPCmd());
		add(new CKActorEffect("Spell Redirect Effects"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{

		JMenu addEffect = new JMenu("Add Effect");
		addEffect.add(CKActorEffectAddMenu.getMenu(tree, "Caster Effect",
				CASTER_EFFECT, true));
		addEffect.add(CKTravelEffectAddMenu.getMenu(tree, "First Travel Effect",
				FIRST_TRAVEL_EFFECT, true));
		addEffect.add(CKTravelEffectAddMenu.getMenu(tree, "Second Travel Effect",
				SECOND_TRAVEL_EFFECT, true));
		addEffect.add(CKActorEffectAddMenu.getMenu(tree, "Successful Effect",
				SPELL_SUCCEEDS_EFFECT, true));
		addEffect.add(CKActorEffectAddMenu.getMenu(tree, "Unsuccessful Effect",
				SPELL_FAILS_EFFECT, true));
		addEffect.add(CKGameActionAddMenu.getMenu(tree,getQuest(),"Spell Effect",
				SPELL_EFFECT,true));
		addEffect.add(CKGameActionAddMenu.getMenu(tree,getQuest(),"Alter Cast",
				ALTER_CAST,true));
		addEffect.add(CKActorEffectAddMenu.getMenu(tree, "Spell Redirect Effect",
				SPELL_REDIRECT_EFFECT, true));
		
		
		
		
		
		
		// addEffect.add(CKStartSoundEffectAddMenu.getMenu(tree,"Caster Sounds",CASTER_SOUND_EFFECT,true));
		return addEffect;
	}

	public synchronized CKActorEffect getCasterEffect()
	{
		return (CKActorEffect) getChildAt(CASTER_EFFECT);
	}

	public synchronized void setCasterEffect(CKActorEffect effect)
	{
		remove(CASTER_EFFECT);
		super.insert(effect, CASTER_EFFECT);
	}

	protected int doCasterEffect(CKSpellCast cast, int startTime)
	{
		return getCasterEffect().doActorEffect(cast, true,startTime);
	}

	public synchronized CKTravelEffect getFirstTravelEffect()
	{
		return (CKTravelEffect) getChildAt(FIRST_TRAVEL_EFFECT);
	}

	public synchronized void setFirstTravelEffect(CKTravelEffect effect)
	{
		remove(FIRST_TRAVEL_EFFECT);
		super.insert(effect, FIRST_TRAVEL_EFFECT);
	}

	public int doFirstTravelGraphics(CKPosition startingPos, CKSpellCast cast,
			int startTime)
	{

		return getFirstTravelEffect().doTravelEffect(startingPos,
				cast.getTargetPosition(), startTime);

	}

	public synchronized CKTravelEffect getSecondTravelEffect()
	{
		return (CKTravelEffect) getChildAt(SECOND_TRAVEL_EFFECT);
	}

	public synchronized void setSecondTravelEffect(CKTravelEffect effect)
	{
		remove(SECOND_TRAVEL_EFFECT);
		super.insert(effect, SECOND_TRAVEL_EFFECT);
	}

	public int doSecondTravelGraphics(CKPosition startingPos, CKSpellCast cast,
			int startTime)
	{
		return getSecondTravelEffect().doTravelEffect(startingPos,
				cast.getTargetPosition(), startTime);

	}

	public synchronized CKActorEffect getSpellSucceedsEffect()
	{
		return (CKActorEffect) getChildAt(SPELL_SUCCEEDS_EFFECT);
	}

	public synchronized void setSpellSucceedsEffect(CKActorEffect effect)
	{
		remove(SPELL_SUCCEEDS_EFFECT);
		super.insert(effect, SPELL_SUCCEEDS_EFFECT);
	}

	public int doSpellSuccedsEffect(CKSpellCast cast, int startTime)
	{
		return getSpellSucceedsEffect().doActorEffect(cast,false, startTime);

	}

	public synchronized CKActorEffect getSpellFailsEffect()
	{
		return (CKActorEffect) getChildAt(SPELL_FAILS_EFFECT);
	}

	public synchronized void setSpellFailsEffect(CKActorEffect effect)
	{
		remove(SPELL_FAILS_EFFECT);
		super.insert(effect, SPELL_FAILS_EFFECT);
	}

	public int doSpellFailsEffect(CKSpellCast cast, int startTime)
	{
		return getSpellFailsEffect().doActorEffect(cast,false, startTime);

	}

	public synchronized CKGameAction getAlterCast()
	{
		return (CKGameAction) getChildAt(ALTER_CAST);
	}

	public synchronized void setAlterCast(CKGameAction effect)
	{
		remove(ALTER_CAST);
		super.insert(effect, ALTER_CAST);
	}

	public void doAlterCast(CKSpellCast cast)
	{
		getAlterCast().doAction(this, cast);
		return;

	}

	public synchronized CKGameAction getDoEffect()
	{
		return (CKGameAction) getChildAt(SPELL_EFFECT);
	}

	public synchronized void setDoEffect(CKGameAction effect)
	{
		remove(SPELL_EFFECT);
		super.insert(effect, SPELL_EFFECT);
	}

	public void doEffect(CKSpellCast cast)
	{
		getDoEffect().doAction(this, cast);
		return;

	}

	public synchronized CKActorEffect getRedirectEffect()
	{
		return (CKActorEffect) getChildAt(SPELL_REDIRECT_EFFECT);
	}

	public synchronized void setRedirectEffect(CKActorEffect effect)
	{
		remove(SPELL_REDIRECT_EFFECT);
		super.insert(effect, SPELL_REDIRECT_EFFECT);
	}

	public int doSpellRedirectEffect(CKSpellCast cast, int startTime)
	{
		return getRedirectEffect().doActorEffect(cast, false,startTime);

	}

	public final int EFFECT_FAILS = 0;
	public final int EFFECT_SUCCEEDS = 1;

	protected int hitsTarget(CKSpellCast cast)
	{

		return EFFECT_SUCCEEDS;
	}

	protected void doEffectGraphics(int success, CKSpellCast cast,
			int travelTime)
	{
		// Should do ignition effect
		CKGridActor pc = cast.getActorTarget();

		if (pc != null)
		{
			try
			{
				int targetEffect = engine.createInstance(0, "illuminate",
						cast.getTargetPosition(), travelTime,// q.getStartTime(),
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
	CK2dGraphicsEngine engine;

	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		quest = CKGameObjectsFacade.getQuest();
		engine = CKGameObjectsFacade.getEngine();

		boolean notRedirected = cast.getRedirect() == null;

		// if no redirect
		// int sourceEffect = -1;
		// int sourceSound=-1;
		// int travelTime = -1;
		CKPosition startingPos;

		int presentTime = quest.getStartTime();

		if (notRedirected)
		{
			quest.startTransaction();
			replaceListener(L);
			startingPos = cast.getSource().getPos();

			presentTime = doCasterEffect(cast, presentTime);
			// TODO add sounds here

			presentTime = doFirstTravelGraphics(startingPos, cast, presentTime);
			quest.setStartTime(presentTime); // only store the time if your are
												// in the first leg.

			// if(sourceEffect != -1) { engine.destroy(0, sourceEffect,
			// travelTime); }
			// if(sourceSound!=-1) { engine.stopSound(travelTime,sourceSound );
			// }

		} else
		{
			startingPos = cast.getRedirect();
			presentTime = doSecondTravelGraphics(startingPos, cast, presentTime);
		}

		if (cast.getItemTarget() instanceof CKGridItemSet)
		{
			doSpellRedirectEffect(cast, presentTime);
			doAlterCast(cast);

			CKGridItemSet set = (CKGridItemSet) cast.getItemTarget();

			for (CKAbstractGridItem pos : set.getItems())
			{
				CKSpellCast c2 = new CKSpellCast(pos, cast.getSource(),
						cast.getChapter(), cast.getPage(), cast.getCp(),
						cast.getKey());
				c2.setRedirect(set.getPos());
				c2.castSpell(this, "");
			}
		} else
		{
			if (hitsTarget(cast) == EFFECT_SUCCEEDS)
			{
				doSpellSuccedsEffect(cast, presentTime);
				doEffect(cast);
			} else
			{
				doSpellFailsEffect(cast, presentTime);
			}

		}
		if (notRedirected)
		{
			Thread T = new waitForGraphicsToFinish(this, this);
			T.start();
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CKSpellAction action = new CKSpellAction();

		action.children.clear();
		for (Object n : this.children)
		{
			action.add((MutableTreeNode) ((CKGUINode) n).clone());
		}
		return action;

	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("CyberKnight Actor Editor");
		CKGuiRoot root = new CKGuiRoot();

		CKSpellAction action = new CKSpellAction();

		/*CKTravelEffect travel = new CKTravelEffect();
		CKSoundPickerNode effect = new CKSoundPickerNode();
		effect.setAID("BOB");
		CKGraphicsAssetPickerNode ga = new CKGraphicsAssetPickerNode();
		ga.setAID("FRED");

		travel.setGraphic(ga);
		travel.setSound(effect);

		action.setFirstTravelEffect(travel);
		*/
		
		root.add(action);
		frame.add(new CKTreeGui(root));
		// frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// heroAct.writeToStream(System.out);
		// tl.writeToStream(System.out);

	}

}