package ckEditor.treegui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

public class CKNumberPickerNode  extends CKGUINode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1703639546241072687L;
	int number;
	String description;
	
	
	public CKNumberPickerNode()
	{
		this("Number Picker");
	}
	
	public CKNumberPickerNode(String description)
	{
		this.description = description;
		this.setAllowsChildren(false);
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
		
	}


	
	
	
	/**
	 * @return the number
	 */
	public int getNumber()
	{
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number)
	{
		this.number = number;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CKNumberPickerNode [description=" + description + "]";
	}



	static private JPanel[] panel;
	static JLabel[] descLabel;
	static SpinnerNumberModel [] num;

	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			

			//description
			descLabel = new JLabel[2];
			descLabel[0]=new JLabel();
			descLabel[1]=new JLabel();
			
			
			panel[0].add(descLabel[0]);
			panel[1].add(descLabel[1]);
			
			
			num=new SpinnerNumberModel[2];			
			num[0] = new SpinnerNumberModel(1, -1000, 1000,1);
			JSpinner spin = new JSpinner(num[0]);
			panel[0].add(spin);
			num[1] = new SpinnerNumberModel(1, -1000, 1000,1);
			spin = new JSpinner(num[1]);
			panel[1].add(spin);
			
			
			
			
		
			
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.ORANGE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		
		descLabel[index].setText(description);
		num[index].setValue(number);
		
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		
		
		
		//change values
		setPanelValues(EDIT);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		
		number = (Integer) num[EDIT].getNumber();
			
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
	

	
	
	public static void main(String[] args)
	 {
			JFrame frame = new JFrame("CyberKnight Actor Editor");
			CKGuiRoot root = new CKGuiRoot();
			
			CKNumberPickerNode asset = new CKNumberPickerNode();
			root.add(asset);
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		 
			//heroAct.writeToStream(System.out);
			//tl.writeToStream(System.out);
		 
		 
		 
	 }
	
	
	
	
}
