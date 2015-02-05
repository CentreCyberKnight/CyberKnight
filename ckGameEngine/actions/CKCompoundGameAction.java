package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckGameEngine.CKSpellCast;

public abstract class CKCompoundGameAction extends CKGameAction implements CKGameActionListenerInterface
{

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameActionListenerInterface#runAction(ckGameEngine.actions.CKGameAction, ckGameEngine.CKSpellCast)
	 */
	@Override
	public void runAction(CKGameAction act, CKSpellCast cast)
	{
		act.doAction(this, cast);
		
	}


	private static final long serialVersionUID = 18627128487561999L;
	String name="     ";
	public CKCompoundGameAction()
	{
		setAllowsChildren(true);
		if(children==null)
			children=new Vector<DefaultMutableTreeNode>();
	}
	
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


	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKGameAction)
		{
			super.insert(newChild,childIndex);
		}
	}
	

	

	

	public void swap(int posA,int posB)
	{
		if(posA < getChildCount() && posB < getChildCount())
		{
			Collections.swap(children, posA, posB);
			
		}
	}
	
	
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Name:"));
		JTextField text = new JTextField(name,20);
		panel.add(text);
		menu.add(panel);	

		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(text));
		menu.add(store);
			
		return menu;
	}
	
	
	class EditAction  implements ActionListener
	{
		JTextField text;
		
		public EditAction(JTextField f)
		{
			text= f;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			name = text.getText();
			
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		DefaultMutableTreeNode node= (DefaultMutableTreeNode) super.clone();
		if(children != null)
		{
			for(Object o: children)
			{
				//	Satisfies s = (Satisfies) o;
				node.add( (MutableTreeNode) ((CKGameAction) o ).clone());
			}
		}
		return node;
	}

	
	static JPanel []panel;
	static JTextField [] nameText;
	static JLabel [] descr;

	
	static private void initPanel(boolean force,CKCompoundGameAction caller)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			descr=new JLabel[2];
			descr[0] = new JLabel();
			descr[1] = new JLabel();
			panel[1].add(descr[1]);
			panel[0].add(descr[0]);
			nameText=new JTextField[2];
			nameText[0] = new JTextField(15);
			nameText[1] = new JTextField(15);
			//panel[0].add(new JLabel(caller.toString()));
			//panel[1].add(new JLabel(caller.toString()));

			panel[0].add(nameText[0]);		
			panel[1].add(nameText[1]);	
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true,this); }
		panel[index].setBackground(colors[index]);
		
		descr[index].setText(this.toString().substring(0, 17));
		nameText[index].setText(name);
		
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
		name = (String)nameText[EDIT].getText();
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
