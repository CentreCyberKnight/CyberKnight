package ckEditor;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;

import ckEditor.treegui.CKTreeGui;
import ckSatisfies.*;

public class CKSatistfiesAddMenu
{
	public static JMenu notSupported()
	{
		JMenu addActions = new JMenu("Add Satisfies");
		addActions.setEnabled(false);
		return addActions;
	}
	public static JMenu getMenu(CKTreeGui tree)
	{
		JMenu addActions = new JMenu("Add Satisfies");
			/*        Add Actions --should these be based on the item?*/
			JMenuItem addSeq = new JMenuItem("Add And Satisfies");
			addSeq.addActionListener(new TreeAddActionListener("AND",tree));
			addActions.add(addSeq);

			JMenuItem addConn = new JMenuItem("Add Or Satisfies");
			addConn.addActionListener(new TreeAddActionListener("OR",tree));
			addActions.add(addConn);
			
			JMenuItem addGUI= new JMenuItem("Add Not Satisfies");
			addGUI.addActionListener(new TreeAddActionListener("NOT",tree));
			addActions.add(addGUI);
			
			addActions.add(new JSeparator());

			JMenuItem addTrue= new JMenuItem("Add True Satisfies");
			addTrue.addActionListener(new TreeAddActionListener("TRUE",tree));
			addActions.add(addTrue);

			JMenuItem addItem= new JMenuItem("Add False Satisfies");
			addItem.addActionListener(new TreeAddActionListener("FALSE",tree));
			addActions.add(addItem);


			addActions.add(new JSeparator());

			addItem= new JMenuItem("Add Dead Satisfies");
			addItem.addActionListener(new TreeAddActionListener("DEAD",tree));
			addActions.add(addItem);

			addItem= new JMenuItem("Add Position Satisfies");
			addItem.addActionListener(new TreeAddActionListener("POS",tree));
			addActions.add(addItem);

			addItem= new JMenuItem("Add Turns Satisfies");
			addItem.addActionListener(new TreeAddActionListener("TURNS",tree));
			addActions.add(addItem);

			addItem= new JMenuItem("Spell Satisfies");
			addItem.addActionListener(new TreeAddActionListener("SPELLCAST",tree));
			addActions.add(addItem);

			addItem= new JMenuItem("Book Satisfies");
			addItem.addActionListener(new TreeAddActionListener("BOOK",tree));
			addActions.add(addItem);
			
			addItem= new JMenuItem("CP Satisfies");
			addItem.addActionListener(new TreeAddActionListener("CP",tree));
			addActions.add(addItem);

			
			return addActions;
		}
	
	


	
static class TreeAddActionListener implements ActionListener
{
		String name;
		String hero = "HERO";
		CKTreeGui tree;
		
		public TreeAddActionListener(String n,CKTreeGui t)
		{
			name = n;
			tree=t;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
			
				return;
			}
			Satisfies action=new FalseSatisfies();
			if(name.compareTo("AND")==0)
			{
				action = new AndSatisfies();
			}
			else if(name.compareTo("OR")==0)
			{
				action = new OrSatisfies();
			}
			else if(name.compareTo("NOT")==0)
			{				
				action = new NotSatisfies();
			}
			else if(name.compareTo("TRUE")==0)
			{
				action = new TrueSatisfies();
			}
			else if(name.compareTo("FALSE")==0)
			{
				action = new FalseSatisfies();
			}
			else if(name.compareTo("DEAD")==0)
			{
				action = new DeadSatisfies();
			}
			else if(name.compareTo("POS")==0)
			{
				action = new PositionReachedSatisfies();
			}
			else if(name.compareTo("TURNS")==0)
			{
				action = new TurnsTakenSatisfies();
			}
			else if(name.compareTo("SPELLCAST")==0)
			{
				action = new SpellSatisfies();
			}
			else if(name.compareTo("BOOK")==0)
			{
				action = new BookSatisfies();
			}
			else if(name.compareTo("CP")==0)
			{
				action = new ActorCPSatisfies();
			}

			
			
			tree.addNode(node,action);		
		}
			
	
}

}

