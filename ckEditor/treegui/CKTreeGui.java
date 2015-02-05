package ckEditor.treegui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.Vector;


import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import ckDatabase.CKQuestFactory;

/*import ckSatisfies.NotSatisfies;
import ckSatisfies.TrueSatisfies;
import ckTrigger.Trigger;
*/
public class CKTreeGui extends JScrollPane implements TreeModelListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7351090440885788892L;
	//CKGameAction action;
	protected JTree tree;
	DefaultTreeModel treeModel;
	DefaultMutableTreeNode copybuffer;
	
	
	public CKTreeGui(DefaultMutableTreeNode act)
	{
		this(act,true);
	}
	
	public CKTreeGui(DefaultMutableTreeNode act,boolean canEdit)
	{
		
		//action=act;
		//DefaultMutableTreeNode top =createTree(act);
		treeModel = new InvisibleTreeModel(act,false,true);
		treeModel.addTreeModelListener(this);
		tree = new JTree(treeModel);
		tree.setEditable(canEdit);
		
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		CKGUICellEditor editor = new CKGUICellEditor(tree);
		tree.setCellEditor(editor);
		tree.setCellRenderer(editor);
		
		/*
		XMLEncoder e;
		try
		{
			e = new XMLEncoder(
			        new BufferedOutputStream(
			            new FileOutputStream("Test.xml")));
			e.writeObject(act);
			e.close();
			
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		for (int i = 0; i < tree.getRowCount(); i++) {
		         tree.expandRow(i);
		}
		
		getViewport().add(tree);
		
		//initPopup();
		//tree.setEditable(true);
		if(canEdit)
		{
			tree.addMouseListener(new PopupListener());
		}
		else
		{
			setPreferredSize(new Dimension(350,200));
		}
	}

	
//test it out	
	public CKTreeGui()
	{
		CKGUINode node= null;
		try
		{
			 XMLDecoder d = new XMLDecoder(
                     new BufferedInputStream(
                         new FileInputStream("Test.xml")));
			 node = (CKGUINode) d.readObject();
			 d.close();
			
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//treeModel = new DefaultTreeModel(top);
		treeModel = new InvisibleTreeModel(node,true);
		tree = new JTree(treeModel);
		//tree = new JTree(new DefaultMutableTreeNode());
		tree.setEditable(true);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
        
		
		
		getViewport().add(tree);
		
	
		
		tree.addMouseListener(new PopupListener());
	}

	
	Vector<ChangeListener> listeners=new Vector<>();
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	
	public void notifyChange()
	{
		for (ChangeListener l:listeners)
		{
			l.stateChanged(null);
		}
	}
	
	
	public DefaultMutableTreeNode getRoot()
	{
		return (DefaultMutableTreeNode) treeModel.getRoot();
	}
	
	public void setRoot(CKGUINode node)
	{
		treeModel.setRoot(node);
	}

	/**
	 * @param copybuffer the copybuffer to set
	 */
	public void setCopyBuffer(DefaultMutableTreeNode copybuffer)
	{
		this.copybuffer = copybuffer;
	}


	/**
	 * @return the copybuffer
	 */
	public DefaultMutableTreeNode getCopyBuffer()
	{
		return copybuffer;
	}

	
	public void startEditing()
	{
		tree.startEditingAtPath(tree.getSelectionPath());
	}
	
	public void stopEditing()
	{
		if(tree.isEditing())
			{
				tree.stopEditing();
			}
	}
	
	public void saveState()
	{
		stopEditing();
	}
	

	public static DefaultMutableTreeNode copyTree(DefaultMutableTreeNode action)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)action.clone();
		//clone should handle the copies 
		/*
		if(!action.isLeaf())
		{
			for(int i=0;i<action.getChildCount();i++)
			{
				DefaultMutableTreeNode child = copyTree((DefaultMutableTreeNode) action.getChildAt(i));
				node.add(child);
			}
		}
		*/
		return node;
	}

	/*public DefaultMutableTreeNode getRoot()
	{
		//
	}*/

	public JPopupMenu getPopup(CKGUIEditable act)
	{
		if( act instanceof CKGUINode)
		{
			return ((CKGUINode) act).getPopup(this);
		}
		JPopupMenu popup=new JPopupMenu();
		boolean selected = act != null; 
		if( act != null )
		{
			JMenuItem editAction= act.GUIEdit();
			popup.add(editAction);
		}
		else
		{
			JMenuItem editAction=new JMenuItem("Edit Action");
			editAction.setEnabled(false);			
			popup.add(editAction);
		}
		
		popup.add(new JSeparator());

		
		JMenuItem copyAction = new JMenuItem("Copy Action");
		if(selected)
		{
			copyAction.addActionListener(new TreeCopyActionListener());
		}
		else { copyAction.setEnabled(false); }
		popup.add(copyAction);
		
		JMenuItem pasteAction = new JMenuItem("Paste Action");
		if(copybuffer!=null) { 	pasteAction.addActionListener(new TreePasteActionListener());}
		else { pasteAction.setEnabled(false);}
		popup.add(pasteAction);

		JMenuItem removeAction = new JMenuItem("Remove Action");
		if(selected) { removeAction.addActionListener(new TreeRemoveActionListener()); }
		else { removeAction.setEnabled(false); }
		popup.add(removeAction);
		
		JMenuItem moveUpAction = new JMenuItem("Move Action Up");
		if(selected) {moveUpAction.addActionListener(new TreeMoveUpActionListener());}
		else { moveUpAction.setEnabled(false); }
		popup.add(moveUpAction);

		JMenuItem moveDownAction = new JMenuItem("Move Action Down");
		if(selected) { moveDownAction.addActionListener(new TreeMoveDownActionListener());}
		else { moveDownAction.setEnabled(false); }
		popup.add(moveDownAction);

		if( selected )
		{
			JMenuItem addAction= act.GUIAddNode(this);
			popup.add(addAction);
		}
		else
		{
			JMenuItem addAction=new JMenuItem("Add Action");
			addAction.setEnabled(false);			
			popup.add(addAction);
		}
		
		return popup;
	}
	
	
	public DefaultMutableTreeNode getSelected()
	{
		return (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
	}
	
	public void addNode(DefaultMutableTreeNode parent,DefaultMutableTreeNode child)
	{
		addNode(parent,child,0);
	}
	
	

	public void addNode(DefaultMutableTreeNode parent,
										   DefaultMutableTreeNode childNode,
										   int pos)
	{

		if(parent == null)
		{
			treeModel.setRoot(childNode);
		}
		else if (parent.getAllowsChildren())
		{
			treeModel.insertNodeInto(childNode, parent, pos);
			treeModel.nodeStructureChanged(parent); //handles weird cases where the parent must remove a node.
			//??fire a node change here
		}
		else 
		{
			DefaultMutableTreeNode newDad = (DefaultMutableTreeNode) parent.getParent();
			if(newDad != null)
			{
				int newPos =newDad.getIndex(parent); 
				addNode(newDad,childNode,newPos);
			}
		}
	}
	
	
	public void moveNode(DefaultMutableTreeNode node,boolean up)
	{
		if(node == null) {return;}
		DefaultMutableTreeNode parent= (DefaultMutableTreeNode) node.getParent();
		if(parent != null)
		{
			int index = parent.getIndex(node);
			//int start=index;
			treeModel.removeNodeFromParent(node);
			if(up) 	{ index--;	}
			else    { index++;} 
				
			treeModel.insertNodeInto(node, parent, index);
			//CKCompoundGameAction action = (CKCompoundGameAction) parent.getUserObject();
			//action.swap(index, start);
			
		}
		
	}
	
	public void removeNode(DefaultMutableTreeNode node)
	{
		if(node == null) {return;}
		/*DefaultMutableTreeNode parent= (DefaultMutableTreeNode) node.getParent();
		if(parent != null)
		{
			int index = parent.getIndex(node);

			CKCompoundGameAction action = (CKCompoundGameAction) parent.getUserObject();
			//action.removeAction(index);
		}*/
		treeModel.removeNodeFromParent(node);
	}
	
	
	
	
	
	//Inner class to check whether mouse events are the popup trigger
	class PopupListener extends MouseAdapter {
	 public void mousePressed(MouseEvent e) {
	   checkPopup(e);
	 }

	 public void mouseClicked(MouseEvent e) {
	   checkPopup(e);
	 }

	 public void mouseReleased(MouseEvent e) {
	   checkPopup(e);
	 }

	 private void checkPopup(MouseEvent e) {
	   if (e.isPopupTrigger()) 
	   {
		   //stop editing
		   tree.stopEditing();
		   //mark the present thing selected
		   tree.setSelectionPath(tree.getPathForLocation(e.getX(),e.getY()));
		   //build the popup
		    JViewport V = getViewport();
		   Rectangle rec = V.getViewRect();
		   //System.out.println("My rectangle is "+rec);
		     getPopup((CKGUIEditable) getSelected())
		                .show(CKTreeGui.this, e.getX()- (int)rec.getX(), e.getY()-(int)rec.getY());
		               
	   }
	 }
	}
	
	
	public class TreeEditActionListener implements ActionListener
	{
		Component parent;
		
		public TreeEditActionListener(Component parent)
		{
			this.parent=parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			//TODO can we make this more generic??
			//CKGameAction node = (CKGameAction) getSelected();
			///TODO node.GUIEdit(parent);
		}
	}
	
	public class TreeRemoveActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode node = getSelected();
			removeNode(node);
		}
	}
	
	public class TreeCopyActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			copybuffer = copyTree( getSelected() );
		}

	}
	
	public class TreePasteActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(copybuffer != null)
			{
			DefaultMutableTreeNode	parent	= getSelected();
			addNode(parent, copyTree(copybuffer));
			}
		}
	}

	public class TreeMoveUpActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= getSelected();
			moveNode(node,true);			
		}
			
	}


	public class TreeMoveDownActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= getSelected();
			moveNode(node,false);			
		}
			
	}

	
	/**
	 * This is a tree model that will allow us to hide nodes from displaying in JTrees
	 * 
	 * 
	 * heavily copied from http://www.java2s.com/Code/Java/Swing-Components/InvisibleNodeTreeExample.htm
	 *
	 */
	class InvisibleTreeModel extends DefaultTreeModel
	{

		  /**
		 * 
		 */
		private static final long serialVersionUID = -8039049103440390531L;
		protected boolean filterIsActive;

		  public InvisibleTreeModel(DefaultMutableTreeNode root) 
		  {
		    this(root, false);
		  }

		  public InvisibleTreeModel(DefaultMutableTreeNode root, boolean asksAllowsChildren) {
		    this(root, false, false);
		  }

		  public InvisibleTreeModel(DefaultMutableTreeNode root, boolean asksAllowsChildren,
		      boolean filterIsActive) {
		    super(root, asksAllowsChildren);
		    this.filterIsActive = filterIsActive;
		  }

		  public void activateFilter(boolean newValue) {
		    filterIsActive = newValue;
		  }

		  public boolean isActivatedFilter() {
		    return filterIsActive;
		  }

		  public Object getChild(Object parent, int index) {
		    if (filterIsActive) {
		      if (parent instanceof CKGUINode) {
		        return ((CKGUINode) parent).getVisibleChildAt(index);
		      }
		    }
		    return ((TreeNode) parent).getChildAt(index);
		  }

		  public int getChildCount(Object parent) 
		  {
			  if (filterIsActive) 
			  {
				  if (parent instanceof CKGUINode) 
				  {
					  return ((CKGUINode) parent).getVisibleChildCount();
				  }
		    }
		    return ((TreeNode) parent).getChildCount();
		  }

		}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("CyberKnight Quest Editor");
		CKGuiRoot root = new CKGuiRoot();
		root.add( CKQuestFactory.getInstance().getAsset("level1-db"));
		frame.add(new CKTreeGui(root));
		//frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e)
	{
		notifyChange();		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e)
	{
		notifyChange();		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e)
	{
		notifyChange();		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e)
	{
		notifyChange();		
	}
		
	
	
}









