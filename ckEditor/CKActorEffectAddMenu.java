package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.CKTravelEffectAddMenu.TreeAddActionListener;
import ckEditor.treegui.CKActorEffect;
import ckEditor.treegui.CKChangeAnimation;
import ckEditor.treegui.CKTreeGui;
import ckEditor.treegui.DownTravelEffect;

public class CKActorEffectAddMenu
{
	public static JMenu notSupported(String text)
	{
		JMenu addActions = new JMenu(text);
		addActions.setEnabled(false);
		return addActions;
	}
	
	public static JMenu getMenu(CKTreeGui tree)
	{
		return getMenu(tree,"Add Actor Effect",0,false);
	}
	
	public static JMenu getMenu(CKTreeGui tree,String text,int pos, boolean replace)
	{
		JMenu addActions = new JMenu(text);
			/*        Add Actions --should these be based on the item?*/
			JMenuItem addSeq = new JMenuItem("Add Lights and Sound effect");
			addSeq.addActionListener(new TreeAddActionListener("LAS",tree,text,pos,replace));
			addActions.add(addSeq);
			
			//change action menu
			JMenuItem ChangeAction=new JMenuItem("ChangeAction");
			ChangeAction.addActionListener(new TreeAddActionListener("ChangeAct",tree,text,pos,replace));
			addActions.add(ChangeAction);

			return addActions;
		}
			
	


	
static class TreeAddActionListener implements ActionListener
{
		String name;
		String hero = "HERO";
		CKTreeGui tree;
		int position;
		boolean replace;
		String text;
		
		public TreeAddActionListener(String n,CKTreeGui t,String text,int pos,boolean replace)
		{
			name = n;
			tree=t;
			position = pos;
			this.replace=replace;
			this.text=text;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			
				return;
			}
			CKActorEffect action=null;
			if(name.compareTo("LAS")==0)
			{
				action=new CKActorEffect(text+"Add light and sound");
			}
			//ChangeAnimation action
			else if(name.compareTo("ChangeAct")==0)
			{
				action=new CKChangeAnimation(text+": ChangeAct");
			}

			if(replace)
			{
				node.remove(position);
			}
			
			tree.addNode(node,action,position);		
		}
			
	
}


}
