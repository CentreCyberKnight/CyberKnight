package ckEditor.treegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.treegui.CKTreeGui;

public class CKActorAddMenu
{
	public static JMenuItem getMenu(CKTreeGui tree)
	{
		
			JMenuItem addActor = new JMenuItem("Add Actor");
			addActor.addActionListener(new TreeAddActorListener(tree));
			
			return addActor;
		}
	
	
}

	
class TreeAddActorListener implements ActionListener
{
	
	    CKTreeGui tree;
		
		public TreeAddActorListener(CKTreeGui t)
		{
			tree=t;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			//	node = (DefaultMutableTreeNode) treeModel.getRoot();
				return;
			}	
			tree.addNode(node,new ActorNode(1));		
		}
			
	
}

