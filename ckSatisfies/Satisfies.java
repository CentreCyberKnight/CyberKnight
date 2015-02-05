package ckSatisfies;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ckEditor.CKSatistfiesAddMenu;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

abstract  public class Satisfies extends CKGUINode
{
	
	private static final long serialVersionUID = 4305422054680480969L;
	public Satisfies()
	{
		setAllowsChildren(false);
	}
	
	public abstract boolean isSatisfied(CKSpellCast cast );
	
	protected CKGridActor getPC(String name)
	{
		
		return getQuest().getActor(name);
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGUIEditable#GUIEdit()
	 */
	@Override
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Satisfies");
		menu.setEnabled(false);
		return menu;
	}
/*
	@Override
	public JMenu GUIAddNode(CKGameActionBuilder tree)
	{
		return CKSatistfiesAddMenu.notSupported();
	}
*/
	 /* (non-Javadoc)
	 * @see ckGameEngine.CKGUIEditable#GUIAddNode(ckGameEngine.CKGameActionBuilder)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKSatistfiesAddMenu.getMenu(tree);
	}
	
	private final static Icon satisfiesIcon = new ImageIcon("ckEditor/images/tools/SatisfiesTreeIcon.png");
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		
		return satisfiesIcon;
	}

	
}
