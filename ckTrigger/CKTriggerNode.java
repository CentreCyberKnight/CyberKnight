package ckTrigger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ckSatisfies.*;
import ckEditor.CKSatistfiesAddMenu;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.*;

abstract public class CKTriggerNode extends CKGUINode 
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 353426377539815697L;
	private final static Icon triggerIcon = new ImageIcon("ckEditor/images/tools/TriggerTreeIcon.png");
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		return triggerIcon;
	}

	private String name = "";

		
	/**
	 * @return the satisfy
	 */
	abstract public Satisfies getSatisfy();

	/**
	 * @param satisfy the satisfy to set
	 */
	//abstract public void setSatisfy(Satisfies satisfy);

	/**
	 * @return the action
	 */
	abstract public CKGameAction getAction();
	
	/**
	 * @param action the action to set
	 */
	// public void setAction(CKGameAction action);
	
	/**
	 * @return the result
	 */
	abstract public TriggerResult getResult();
	
	/**
	 * @param result the result to set
	 */
	//abstract public void setResult(TriggerResult result);
	

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("Add Nodes");
		menu.add(CKSatistfiesAddMenu.getMenu(tree) );
		menu.add(CKGameActionAddMenu.getMenu(tree, getQuest()) );
		return menu;		
	}
		

	
	/**
	 * returns true if the trigger is satisfied with the given cast
	 * @param cast
	 * @return true or false
	 */
	abstract public boolean shouldActNow(CKSpellCast cast);
	
	public TriggerResult doTriggerAction(CKGameActionListenerInterface boss,boolean init,CKSpellCast cast	)
	{
		
		if(init) 
		{
			if(getResult() == TriggerResult.INIT_ONLY)
			{
				//don't need a seperate call for the triggerlist Actions, their return value is not needed
				if(shouldActNow(cast)) { boss.runAction(getAction(),cast); }
				return TriggerResult.SATISFIED_ONCE;
			}
		}		
		else  if(shouldActNow(cast))
		{
			if(getAction() instanceof CKTriggerListNode)
			{  //refactor this if you ever get the chance/reason.
				CKTriggerListNode tl = (CKTriggerListNode) getAction();
				return tl.doTriggers(boss, init, cast);
			}
			else
			{
				boss.runAction(getAction(),cast);
				return getResult();
			}
		}
		return TriggerResult.UNSATISFIED;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Trigger Node " +name;
	}
	
		

	
	
	
}
