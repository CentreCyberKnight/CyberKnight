package ckGameEngine.actions;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKSpellCast;

final public class CKAlterSpellCPCmd extends CKGameAction implements ItemListener 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CKSpellCast spell;
	int cp;
	int fixNum;
	int perNum;
	boolean useFixed;
	boolean usePerc;
	
	
	public int getFixNum() {
		return fixNum;
	}


	public void setFixNum(int fixNum) {
		this.fixNum = fixNum;
	}


	public int getPerNum() {
		return perNum;
	}


	public void setPerNum(int perNum) {
		this.perNum = perNum;
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	

	
	@Override
	public String toString()
	{
		return "Alterting spell cyber points to "+cp;
	}

	
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		System.out.println("Before: " +cast.getCp());
		if(useFixed==true)
		{
			cast.setCp(cast.getCp()+cp);
		}
		else if(usePerc==true)
		{
			cast.setCp(cast.getCp()*(100-cp)/100);
		}
		System.out.println("After: " +cast.getCp());
		L.actionCompleted(this);
	}
	
	
	public CKSpellCast getSpell() {
		return spell;
	}

	public void setSpell(CKSpellCast spell) {
		this.spell = spell;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public boolean isUseFixed() {
		return useFixed;
	}

	public void setUseFixed(boolean useFixed) {
		this.useFixed = useFixed;
	}

	public boolean isUsePerc() {
		return usePerc;
	}

	public void setUsePerc(boolean usePerc) {
		this.usePerc = usePerc;
	}


	static JPanel []panel;
	static SpinnerNumberModel []percentBox;
	static SpinnerNumberModel[]fixedBox;
	static JCheckBox []perc;
	static JCheckBox []fix;
	static ItemListener boxListener;

	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();	
			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
		
			
			fix = new JCheckBox[2];
			fix[0] = new JCheckBox("Fixed Amount?");
			fix[1] = new JCheckBox("Fixed Amount?");
			panel[0].add(fix[0]);
			panel[1].add(fix[1]);
			
			
			fixedBox = new SpinnerNumberModel[2];
			fixedBox[0] = new SpinnerNumberModel(1, -100, 100,1);
			fixedBox[1] = new SpinnerNumberModel(1, -100, 100,1);
			JSpinner spin = new JSpinner(fixedBox[0]);
			panel[0].add(spin);
			spin = new JSpinner(fixedBox[1]);
			panel[1].add(spin);

			
			perc = new JCheckBox[2];
			perc[0] = new JCheckBox("Percentage?");
			perc[1] = new JCheckBox("Percentage?");
			panel[0].add(perc[0]);
			panel[1].add(perc[1]);
			
			percentBox=new SpinnerNumberModel[2];
			percentBox[0]=new SpinnerNumberModel(1, -100, 100,1);
			percentBox[1]=new SpinnerNumberModel(1, -100, 100,1);
			spin = new JSpinner(percentBox[0]);
			panel[0].add(spin);
			spin = new JSpinner(percentBox[1]);
			panel[1].add(spin);



			
		}
		
	}


	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		fix[index].setSelected(useFixed);
		perc[index].setSelected(usePerc);
		percentBox[index].setValue(perNum);
		fixedBox[index].setValue(fixNum);
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		
		perc[EDIT].removeItemListener(boxListener);
		fix[EDIT].removeItemListener(boxListener);
		setPanelValues(EDIT);
		
		
		boxListener = this;
		perc[EDIT].addItemListener(boxListener);
		fix[EDIT].addItemListener(boxListener);
		
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		
		useFixed = (boolean) fix[EDIT].isSelected();
		usePerc = (boolean) perc[EDIT].isSelected();
		if(useFixed==true)
		{
			cp = (Integer) fixedBox[EDIT].getNumber();
		}
		else if(usePerc==true)
		{
			cp = (Integer) percentBox[EDIT].getNumber();

		}
		else {cp = 0;}
		fixNum = (Integer) fixedBox[EDIT].getNumber();
		perNum = (Integer) percentBox[EDIT].getNumber();



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


	@Override
	public void itemStateChanged(ItemEvent e) {
		 Object source = e.getItemSelectable();
		
		if(source==perc[EDIT])
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				fix[EDIT].setSelected(false);
			}
		}
		else if(source==fix[EDIT])
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				perc[EDIT].setSelected(false);
			}
		}
		
	}
}




