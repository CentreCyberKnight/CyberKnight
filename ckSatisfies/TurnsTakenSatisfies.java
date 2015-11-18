package ckSatisfies;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;


public class TurnsTakenSatisfies extends Satisfies {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4790066457093363160L;

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Satisfied when " + pc + " has " + turns + " turns";
	}


	String pc = "";
	int turns;
	boolean useselfTarget;
	
	public TurnsTakenSatisfies(String pc, int turns)
	{
		this.pc=pc;
		this.turns=turns;
	}
	
	/**
	 * @return the selfTarget
	 */
	public boolean isUseSelfTarget() {
		return useselfTarget;
	}

	/**
	 * @param selfTarget the selfTarget to set
	 */
	public void setUseSelfTarget(boolean selfTarget) {
		this.useselfTarget = selfTarget;
	}

	public TurnsTakenSatisfies()
	{
		this("HERO",5);
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

	/**
	 * @return the turns
	 */
	public int getTurns()
	{
		return turns;
	}

	/**
	 * @param turns the turns to set
	 */
	public void setTurns(int turns)
	{
		this.turns = turns;
	}

	@Override
	public boolean isSatisfied(CKSpellCast cast)
{
		CKGridActor tar;
		if(cast != null) 	{tar = cast.getActorTarget(); }
		else						{tar = getPC(pc); }
		
		
		if(tar.getTurnNumber()==turns)
		{
			return true;
		}
		return false;
	}

	
	class EditAction  implements ActionListener
	{
		SpinnerNumberModel spin;
		SpinnerListModel modelA;
		
		public EditAction(SpinnerListModel modelA,SpinnerNumberModel s)
		{
			this.modelA = modelA;
			spin=s;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			pc = (String) modelA.getValue();
			turns=spin.getNumber().intValue();
			
		}
	}
	
	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static SpinnerNumberModel [] turnsTaken;
	static JCheckBox [] targetSelf;
	
	@SuppressWarnings("unchecked")
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();
			
			targetSelf = new JCheckBox [2];
			targetSelf[0] = new JCheckBox("Target Self?");
			targetSelf[1] = new JCheckBox("Target Self?");
			
			panel[0].add(targetSelf[0]);
			panel[1].add(targetSelf[1]);


			panel[0].add(new JLabel("When "));
			panel[1].add(new JLabel("When "));
			
			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			panel[0].add(nameBox[0]);		
			panel[1].add(nameBox[1]);		
			
			panel[0].add(new JLabel("Reaches Turn"));
			panel[1].add(new JLabel("Reaches Turn"));

			turnsTaken=new SpinnerNumberModel[2]; 
			turnsTaken[0] = new SpinnerNumberModel(1, 0, 100,1);
			turnsTaken[1] = new SpinnerNumberModel(1, 0, 100,1);
			JSpinner spin = new JSpinner(turnsTaken[0]);
			panel[0].add(spin);
			spin = new JSpinner(turnsTaken[1]);
			panel[1].add(spin);
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
		
		targetSelf[index].setSelected(useselfTarget);

		if(getQuest()!=null)
		{
			targetSelf[index].setSelected(useselfTarget);
			pc = initializeActorBox(nameBox[index],pc);
			nameBox[index].setEnabled(!useselfTarget);
		}
		
		/*
		String temp = this.getParent().getParent().toString();
		if(!useselfTarget)
		{
		pc = initializeActorBox(nameBox[index],pc);
		}
//		else if (temp !=null)
//		else if (this.getParent().getParent()==null)
		{
			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
			pc = temp;
		}*/
		else {pc="";}
		turnsTaken[index].setValue(turns);	
		
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
		turns=(Integer) turnsTaken[EDIT].getNumber();
		useselfTarget = (boolean) targetSelf[EDIT].isSelected();

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


