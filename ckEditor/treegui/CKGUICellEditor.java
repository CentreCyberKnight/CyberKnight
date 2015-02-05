package ckEditor.treegui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;


public class CKGUICellEditor extends AbstractCellEditor implements TreeCellEditor,TreeCellRenderer 
{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -1061531717538346365L;
	JTree tree;
	  Object presentValue;
	  private  final Component editComponent=new JLabel("Not Supported");
	  private  final Component renderComponent=new JLabel("Not Supported");
	  
	  private final  CKGUITreeNodeContainer editCont = new CKGUITreeNodeContainer(); 
	  private final  CKGUITreeNodeContainer renderCont = new CKGUITreeNodeContainer(); 
	
	  
	  
	  public CKGUICellEditor(JTree tree)
	  {
	    this.tree = tree;
	  }
	  
	  
	  public Object getCellEditorValue() 
	  {
		  if (presentValue instanceof CKGUINode)
		  {
			  CKGUINode node = (CKGUINode) presentValue;
			  node.storeComponentValues();
			  
			  //signal that the node has changed.
			  DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			  model.valueForPathChanged(
					  new TreePath(model.getPathToRoot(node))				  
					  , node);
		  }
		  
	    return presentValue;
	  }
	
	  
	  public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected,
	      boolean expanded, boolean leaf, int row) 
	  {
		  presentValue=value;
		  if (value instanceof CKGUINode)
		  {
			  CKGUINode node = (CKGUINode) value;
			  editCont.setIcon(node.getTreeIcon(leaf, expanded));
			  editCont.setComp(node.getTreeCellEditorComponent(tree,value,selected,expanded,leaf,row));
		  }
		  else
		  {
			  editCont.setIcon(null);
			  editCont.setComp(editComponent);			  
		  }		  
		  
		  //get it ready?
		  return editCont;
	  }


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		  if (value instanceof CKGUINode)
		  {
			  CKGUINode node = (CKGUINode) value;
			  renderCont.setIcon(node.getTreeIcon(leaf, expanded));
			  renderCont.setComp(node.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row, hasFocus));
		  }
		  else
		  {
			  renderCont.setIcon(null);
			  renderCont.setComp(renderComponent);			  
		  }		  
		
		  renderCont.validate();
		  return renderCont;//this.getTreeCellEditorComponent(tree, value, selected, expanded, leaf, row);//titorComponent//renderCont;
	}
	  
	}