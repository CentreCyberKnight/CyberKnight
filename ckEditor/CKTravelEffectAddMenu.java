package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.treegui.CKTravelEffect;
import ckEditor.treegui.CKTreeGui;
import ckEditor.treegui.DownTravelEffect;
import ckEditor.treegui.Teleport;
import ckEditor.treegui.UpTravelEffect;

public class CKTravelEffectAddMenu
{
	public static JMenu notSupported(String text)
	{
		JMenu addActions = new JMenu(text);
		addActions.setEnabled(false);
		return addActions;
	}
	
	public static JMenu getMenu(CKTreeGui tree)
	{
		return getMenu(tree,"Add Travel Effect",0,false);
	}
	
	public static JMenu getMenu(CKTreeGui tree,String text,int pos, boolean replace)
	{
		JMenu addActions = new JMenu(text);
			/*        Add Actions --should these be based on the item?*/
			JMenuItem addSeq = new JMenuItem("Point A to B");
			addSeq.addActionListener(new TreeAddActionListener("A2B",tree,text,pos,replace));
			addActions.add(addSeq);
			//
			JMenuItem Up=new JMenuItem("MoveUp");
			Up.addActionListener(new TreeAddActionListener("Up",tree,text,pos,replace));
			addActions.add(Up);
			
			//REMEMBER TO DELETE LATER..........
			JMenuItem Down=new JMenuItem("MoveDown");
			Down.addActionListener(new TreeAddActionListener("Down",tree,text,pos,replace));
			addActions.add(Down);

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
			CKTravelEffect action=null;
			if(name.compareTo("A2B")==0)
			{
				action=new CKTravelEffect(text+": A to B");	
				//do nothing already default
			}
			else if(name.compareTo("Up")==0)
			{
				action=new UpTravelEffect(text+": Up");
			}
			//NEED TO REMOVE LATER
			else if(name.compareTo("Down")==0)
			{
				action=new DownTravelEffect(text+":Down");
			}

			if(replace)
			{
				node.remove(position);
			}
			
			tree.addNode(node,action,position);		
		}
			
	
}


}
