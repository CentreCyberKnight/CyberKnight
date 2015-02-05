package ckGameEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckGameEngine.CKDeltaBook.DELTA_TYPES;


public class CKDeltaChapter extends CKChapter
{
	
	private static final long serialVersionUID = -8974768260751543428L;

	protected DELTA_TYPES type;
	
	protected int amount;

	
	
	
	
	
	
	
	public CKDeltaChapter(String name, int value, DELTA_TYPES type)
	{
		super(name,value);
		this.type =type; 
	}

	/**
	 * @return the type
	 */
	public CKDeltaBook.DELTA_TYPES getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CKDeltaBook.DELTA_TYPES type)
	{
		this.type = type;
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKChapter#toString()
	 */
	@Override
	public String toString()
	{
		String s = super.toString();
		switch (type)
		{
		case NO_CHANGE:
			return s;
		case REMOVED:
			return s+" was removed";
		case ADDED:
			return s+ " was added";
		case INCREASED:
			return s+ " increased by "+amount;
		case DECREASED:
			return s+" decreased by "+amount;
			
		}
		return s;
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKChapter#treeString()
	 */
	@Override
	public String treeString()
	{
		return super.treeString();
	}
	
	public static void main(String [] args)
	{
		CKDeltaChapter chap = new CKDeltaChapter("frog",10,DELTA_TYPES.DECREASED);
		chap.setAmount(-5);
		System.out.println(chap);
	
	}
	
	
	
	/*
	 *  GUI editors
	 *
	 */

	 static JPanel []panel;
	
	 static JLabel []labels;
	 static JLabel[]deltaLabels;
	 static Font normalFont;
	 static Font strikeFont;
	
	private final static Icon downArrow = new ImageIcon("ckEditor/images/tools/red_arrow_down.png");
	private final static Icon upArrow = new ImageIcon("ckEditor/images/tools/green_arrow_up.png");


	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			//panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.LINE_AXIS));
			//panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.LINE_AXIS));

			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
		
			
			labels = new JLabel[2];
			labels[0] = new JLabel();
			labels[1] = new JLabel();
			panel[0].add(labels[0]);
			panel[1].add(labels[1]);
	
			deltaLabels = new JLabel[2];
			deltaLabels[0] = new JLabel();
			deltaLabels[1] = new JLabel();
			panel[0].add(deltaLabels[0]);
			panel[1].add(deltaLabels[1]);
	
			Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) labels[0].getFont().getAttributes();
			normalFont = new Font(attributes);
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			strikeFont =  new Font(attributes) ;
			
			
		}
					
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	private final static Color ADDED= new Color(0,180,0);
	private final static Color REMOVED= Color.red;
	private final static Color SAME= Color.black;
	
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
//		panel[index].setBackground(colors[index]);
	
		labels[index].setText(" "+getName()+" "+getValue());
		labels[index].setFont(normalFont);
		deltaLabels[index].setIcon(null);
		
		switch (type)
		{
		case NO_CHANGE:
			deltaLabels[index].setText("");
			labels[index].setForeground(SAME);
			labels[index].setFont(normalFont);
			break;			
		case REMOVED:
			labels[index].setForeground(REMOVED);
			deltaLabels[index].setText("");
			//Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) labels[index].getFont().getAttributes();
			//attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			//labels[index].setFont( new Font(attributes) );
			labels[index].setFont( strikeFont );
			break; 
		case ADDED:
			labels[index].setForeground(ADDED);
			deltaLabels[index].setText("");
			break; 
		case INCREASED:
			deltaLabels[index].setForeground(ADDED);
			deltaLabels[index].setText(" "+amount);
			deltaLabels[index].setIcon(upArrow);
			deltaLabels[index].validate();
			labels[index].setForeground(SAME);
			//deltaLabels[index].setIcon(icon);
			break; 
		case DECREASED:
			deltaLabels[index].setForeground(REMOVED);
			deltaLabels[index].setText(" "+amount);
			deltaLabels[index].setIcon(downArrow);
			labels[index].setForeground(SAME);
			//deltaLabels[index].setIcon(icon);
	//		return s+" decreased by "+amount;
			
		}	
		
		panel[index].validate();
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
