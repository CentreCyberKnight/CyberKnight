package ckSatisfies;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;

import ckGameEngine.CKSpellCast;

public class DeadSatisfies extends Satisfies {

	
	private static final long serialVersionUID = -9010735723191503399L;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DeadSatisfies " + pc ;
	}


	public Object clone()
	{
		
		return new DeadSatisfies(pc);
	}


	String pc;
	
	public DeadSatisfies(String pc)
	{
		super();
		this.pc=pc;
	}
	
	public DeadSatisfies()
	{
		this("HERO");
	}
	
	

	/**
	 * @return the pc
	 */
	public String getPc()
	{
		return pc;
	}

	/**
	 * @param pc the pc to set
	 */
	public void setPc(String pc)
	{
		this.pc = pc;
	}

	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{
		if(cast!=null)
		{
			
			return cast.getActorTarget().isDead();
		}
		return getPC(pc).isDead();
	}
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Name:"));
		SpinnerListModel model = new SpinnerListModel(getQuest().getActors().getActorNames());
		model.setValue(pc);
		JSpinner spin = new JSpinner(model);
		((JSpinner.DefaultEditor)spin.getEditor()).getTextField().setColumns(16);
		panel.add(spin);
		menu.add(panel);	

		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(model));
		menu.add(store);
			
		return menu;
	}
	
	
	class EditAction  implements ActionListener
	{
		SpinnerListModel text;
		
		public EditAction(SpinnerListModel model)
		{
			text= model;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			pc = (String)text.getValue();
			
		}
	}
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			panel[0].add(new JLabel("When "));
			panel[0].add(nameBox[0]);		
			panel[0].add(new JLabel(" is dead"));
			
			panel[1].add(new JLabel("When "));
			panel[1].add(nameBox[1]);		
			panel[1].add(new JLabel(" is dead"));
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		
		if(getQuest()!=null){
		pc = initializeActorBox(nameBox[index],pc);
		}
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
		pc = (String)nameBox[EDIT].getSelectedItem();
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
