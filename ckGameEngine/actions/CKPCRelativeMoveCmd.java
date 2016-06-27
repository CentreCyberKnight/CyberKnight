/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class CKPCRelativeMoveCmd extends CKQuestAction
{
	
	private static final long serialVersionUID = 160976804696658958L;
	String name;
	int squares;
	
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

	/**
	 * @return the squares
	 */
	public int getSquares()
	{
		return squares;
	}

	/**
	 * @param squares the squares to set
	 */
	public void setSquares(int squares)
	{
		this.squares = squares;
	}

	public CKPCRelativeMoveCmd()
	{
		this("HERO",1);
	}
	
	public CKPCRelativeMoveCmd(String name,int squares)
	{
		this.name=name;
		this.squares = squares;
	}
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target;
		if(cast!=null) 	{target = cast.getActorTarget(); }
		else					{target = getPC(name); }
		
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		for(int i =0;i<squares;i++)
		{
			int endTime = grid.move(target, target.getDirection());
			CKGameObjectsFacade.getQuest().setStartTime(endTime);		
		}
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Relative Move " + name + " forward "+ squares+" squares";
	}
	/*
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Actor:"));
		SpinnerListModel modelN = new SpinnerListModel(getQuest().getActors().getActorNames());
		modelN.setValue(name);
		JSpinner spinN = new JSpinner(modelN);
		((JSpinner.DefaultEditor)spinN.getEditor()).getTextField().setColumns(16);
		panel.add(spinN);
		menu.add(panel);	
		
		
		
		panel = new JPanel();
		panel.add(new JLabel("Forward:"));
		SpinnerNumberModel model = new SpinnerNumberModel(squares, 0, 1000,1);
		JSpinner spin = new JSpinner(model);
		panel.add(spin);
		menu.add(panel);	

		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(modelN,model));
		menu.add(store);
		
		return menu;
	}
	*/
	
	class EditAction  implements ActionListener
	{
		SpinnerNumberModel spin;
		SpinnerListModel text;
		
		public EditAction(SpinnerListModel modelN,SpinnerNumberModel s)
		{
			text=modelN;
			spin=s;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			name = (String)text.getValue();
			squares=spin.getNumber().intValue();
			
		}
	}
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static SpinnerNumberModel []steps; //forward;
	
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
			panel[0].add(nameBox[0]);		
			panel[0].add(new JLabel("moves forward"));
			panel[1].add(nameBox[1]);		
			panel[1].add(new JLabel("moves forward"));

			steps=new SpinnerNumberModel[2];			
			steps[0] = new SpinnerNumberModel(1, 1, 100,1);
			JSpinner spin = new JSpinner(steps[0]);
			panel[0].add(spin);
			steps[1] = new SpinnerNumberModel(1, 1, 100,1);
			spin = new JSpinner(steps[1]);
			panel[1].add(spin);
			
			panel[0].add(new JLabel("Steps"));
			panel[1].add(new JLabel("Steps"));
			
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true);
		System.out.println("squares"+squares);}
		panel[index].setBackground(colors[index]);
		
		if(getQuest()!=null)
		{
			name = initializeActorBox(nameBox[index],name);
		}
		else
		{

			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
		}
		
		steps[index].setValue(squares);	
		System.out.println("New squares"+squares);
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
		name = (String)nameBox[EDIT].getSelectedItem();
		squares=(Integer) steps[EDIT].getNumber();
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
