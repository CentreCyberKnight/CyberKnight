package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.treegui.CKCasterEffect;
import ckEditor.treegui.CKTravelEffect;
import ckEditor.treegui.CKTreeGui;

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
			CKTravelEffect action=new CKTravelEffect(text);
			//if(name.compareTo("A2B")==0)
			//{
					//do nothing already default
			//}

			if(replace)
			{
				node.remove(position);
			}
			
			tree.addNode(node,action,position);		
		}
			
	
}


}
