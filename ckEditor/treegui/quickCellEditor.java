package ckEditor.treegui;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

public class quickCellEditor extends DefaultTreeCellEditor
{

	public quickCellEditor(JTree tree, DefaultTreeCellRenderer renderer,
			TreeCellEditor editor)
	{
		super(tree, renderer, editor);
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellEditor#canEditImmediately(java.util.EventObject)
	 */
	@Override
	protected boolean canEditImmediately(EventObject event)
	{
		//changed it to at least two click.
		 if (event == null || !(event instanceof MouseEvent) || 
				 (((MouseEvent) event).getClickCount() > 1 &&
						 inHitRegion(((MouseEvent) event).getX(),((MouseEvent) event).getY())))
		 {
			 return true;
		 }
		 return false;
	}

	
	
}
