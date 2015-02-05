package ckGameEngine.actions;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ckGameEngine.CKSpellCast;

final public class CKNullAction extends CKGameAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Null Action";
	}

	
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		L.actionCompleted(this);
	}

	@Override
	public JMenuItem GUIEdit()
	{
		JMenu menu=new JMenu("Edit Action");
		menu.setEnabled(false);
		return menu;
	}

}
