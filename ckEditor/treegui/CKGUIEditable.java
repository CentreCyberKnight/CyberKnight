package ckEditor.treegui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public interface CKGUIEditable
{
	/**
	 * Creates a menu with appropriate listeners that will update the object that supports this interface.
	 * @returnJMenuItem that will provide options to the user.
	 */
	public  JMenuItem GUIEdit();
	
	/**
	 * Creates a menu with appropriate listeners that 
	 * will provide options to add new, appropriate nodes to the tree.  
	 * @param tree - the tree to add nodes to
	 * @return a JMenuItem that will provide these options to the user. 
	 */
	public  JMenuItem GUIAddNode(CKTreeGui tree); //  need to figure out what to add these to...
	
	
	/**Creates the popup that will allow the tree structure of the game to be altered.
	 * @param tree
	 * @return JPopupmenu with appropriate listeners already installed to handle events.
	 */
	public JPopupMenu getPopup(CKTreeGui tree);
}
