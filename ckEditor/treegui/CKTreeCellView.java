package ckEditor.treegui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

@SuppressWarnings("serial")
public class CKTreeCellView extends AbstractCellEditor implements
		TreeCellRenderer,TreeCellEditor
{
	protected CKGUINode presentValue;
	protected Component view;
	//making this protected, might want to get icons?
	
	//I need two since there is a delay in working with one.
	protected DefaultTreeCellRenderer rend = new DefaultTreeCellRenderer();
	protected DefaultTreeCellRenderer edit = new DefaultTreeCellRenderer();
	
	public CKTreeCellView()
	{
		
	}
	
	
	@Override
	public Object getCellEditorValue()
	{
		System.out.println("returning present value"+presentValue);
		return presentValue;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		System.out.println("rendering"+value);
		//presentValue=(CKGUINode) value;
		view = rend.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		
		return view;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row)
	{
		System.out.println("editing"+value);		
		presentValue=(CKGUINode) value;
		view = edit.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);
		return view;
	}

}
