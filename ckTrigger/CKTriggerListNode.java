package ckTrigger;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKGameActionListener;
import ckGameEngine.actions.CKGameActionListenerInterface;


abstract public class CKTriggerListNode extends CKGameAction//CKGUINode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8352591688558659358L;
	protected String name;
	//private String AID;
	
	public CKTriggerListNode()
	{
		this("");
	}
	
	
	public CKTriggerListNode(String n)
	{
		name = n;
	}
	
	
			
			

	/**
	 * This will test all of the triggers and
	 * execute any that are satisfied and 
	 * notify the boss when all have been tested.
	 * 
	 * @param boss
	 * @param init
	 * @param cast
	 * @return
	 */
	abstract public TriggerResult doTriggers(CKGameActionListenerInterface boss,boolean init,CKSpellCast cast);

	
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
			
			JMenu addActor = new JMenu("Add Trigger");
			JMenuItem normalTrigger = new JMenuItem("Add Trigger");
			JMenuItem sharedTrigger = new JMenuItem("Add Shared Trigger");
			addActor.add(normalTrigger);
			addActor.add(sharedTrigger);
			normalTrigger.addActionListener(new TreeAddTriggerListener(tree));
			sharedTrigger.addActionListener(new TreeAddSharedTriggerListener(tree));
			
			return addActor;
		}
	
	


	
class TreeAddTriggerListener implements ActionListener
{
	
	    CKTreeGui tree;
		
		public TreeAddTriggerListener(CKTreeGui t)
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
			tree.addNode(node,new CKTrigger());		
		}
			
}

class TreeAddSharedTriggerListener implements ActionListener
{
	
	    CKTreeGui tree;
		
		public TreeAddSharedTriggerListener(CKTreeGui t)
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
			tree.addNode(node,new CKSharedTrigger());		
		}
			
}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(name.length()>0) 	{ 	return name; 	}
		else 							{ 	return "Trigger List"; }
	}


	public TriggerResult doTriggers(boolean init, CKSpellCast cast)
	{
		CKGameActionListener listener = new CKGameActionListener();
		return doTriggers(listener,init,cast);
	}


	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		doTriggers(L,false,cast);
	}
	
	
	static JPanel []panel;
	static JTextField[] nameText;
	

	
	static private void initPanel(boolean force,Object caller)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			nameText = new JTextField[2];
			nameText[0] = new JTextField();
			nameText[1] = new JTextField();
			
			panel[0].add(new JLabel("Trigger List  "));
			panel[0].add(nameText[0]);		
			
			panel[1].add(new JLabel("Trigger List  "));
			panel[1].add(nameText[1]);		

			

		}
		
	}
	
	protected final static int EDIT=0;
	protected final static int RENDER=1;
	protected final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true,this); }
		panel[index].setBackground(colors[index]);
		
		nameText[index].setText(getName());
		nameText[index].setColumns(15);
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		setName( (String)nameText[EDIT].getText() );
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}

	
	
	
	
	
	
	
	
	
}
