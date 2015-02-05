package ckGameEngine.actions;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;


/**
 * @author dragonlord
 *
 *The CKGameAction class comprises the parent of the actions that take place in the gameEngine.
 *
 *There are three important functions that a Game Action must fulfill.
 *
 *1) doAction - This will perform the requested action. 
 *2) notifyListener - This will tell the caller that the action has run its course.
 *    notifyListener, must NOT be called within doAction.  This will violate the various properties of the system
 *3)ActionCompleted - this is the function that a child action will call to report the completion of an action.    
 *
 *
 */
public abstract class CKGameAction extends CKGUINode //implements CKGameActionListenerInterface 
{
	


	
	private static final long serialVersionUID = -4980678609878258503L;
	CKGameActionListenerInterface listener;
	
	/**
	 * Finish indicates if the action should notify its parent as soon as it is done or if it needs to wait until
	 * all of its actions have been carried out.
	 */
	private boolean finish=false;
	
	public CKGameAction()
	{
		super();
		setAllowsChildren(false);
		
		listener=null;
	}
	
	final public void doAction(CKGameActionListenerInterface L,CKSpellCast cast,boolean finish)
	{
		this.finish = finish;
		doAction(L,cast);
	}
	
	
	public abstract void doAction(CKGameActionListenerInterface L,CKSpellCast cast);
	

	
	/*
	 (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListener#actionCompleted(ckGameEngine.actions.CKGameAction)
	 
	@Override
	public void actionCompleted(CKGameAction action)
	{
		assert(false);
	}

	
	
	
	 (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListener#runAction(ckGameEngine.actions.CKGameAction, ckGameEngine.CKSpellCast)
	 
	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		//any protection should happen in the doAction...
		act.doAction(this,cast);
	}
*/
	/* (non-Javadoc)
	 * @see ckGameEngine.CKGUIEditable#GUIAddNode(ckGameEngine.CKGameActionBuilder)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKGameActionAddMenu.getMenu(tree, getQuest());
	}

	protected void replaceListener(CKGameActionListenerInterface L)
	{
		listener = L;
		
	}

	

	
	protected void notifyListener()
	{
		//System.out.println(toString() +" Notify listener ");
		if(listener != null)
		{
			if(!finish) { listener.actionCompleted(this); }
			else
			{
				Thread T = new waitForGraphicsToFinish(listener,this);
				T.start();
				finish=false;
			}
			
		}
	}

	
	
	
	protected class waitForGraphicsToFinish extends Thread
	{
		CKGameActionListenerInterface listener;
		CKGameAction act;
		
		public waitForGraphicsToFinish(CKGameActionListenerInterface L,CKGameAction act)
		{
			listener = L;
			this.act = act;
		}
		public void run()
		{

			CKGameObjectsFacade.getQuest().endTransaction();
			CKGameObjectsFacade.getEngine().blockTilActionsComplete();
			//notifyListener();
			
			listener.actionCompleted(act);
			
		}
	}
	
/*
	protected class waitForGraphicsFinish extends Thread
	{
		CKGameAction listener;
		
		public waitForGraphicsFinish(CKGameAction L)
		{
			listener = L;
		}
		public void run()
		{

			CKGameObjectsFacade.getQuest().endTransaction();
			CKGameObjectsFacade.getEngine().blockTilActionsComplete();
			//notifyListener();
			
			listener.actionCompleted(listener);
			
		}
	}
	
	*/
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Game Action";
	}
	
	
	private final static Icon actionIcon = new ImageIcon("ckEditor/images/tools/ActionTreeIcon.png");
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		return actionIcon;
	}

	
	
}
