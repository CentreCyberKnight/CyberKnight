package ckGameEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import ckGameEngine.CKDeltaBook.DELTA_TYPES;

public class CKDeltaPage extends CKPage
{
	
	
	private static final long serialVersionUID = -5880845838261236518L;
	
	protected DELTA_TYPES type;

	public CKDeltaPage(String name, DELTA_TYPES type)
	{
		super(name);
		this.type=type;
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
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.CKPage#toString()
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
		default:
			break;
		}
		return s;
		
	}

	/*
	 *  GUI editors
	 *
	 */

	static JPanel []panel;
	
	static JLabel []labels;
	static Font normalFont;
	static Font strikeFont;

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
	
		labels[index].setText(getName());
		labels[index].setFont(normalFont);
		
		switch (type)
		{
		case NO_CHANGE:
			labels[index].setForeground(SAME);
			break;			
		case REMOVED:
			labels[index].setForeground(REMOVED);
			labels[index].setFont( strikeFont );
			break;
		case ADDED:
			labels[index].setForeground(ADDED);
			break; 
		default:
			break;
			
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
		panel[EDIT].validate();
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
		panel[RENDER].validate();
		return panel[RENDER];
	}
	
	
}
