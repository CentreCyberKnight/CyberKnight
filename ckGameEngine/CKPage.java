package ckGameEngine;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import ckEditor.treegui.CKGUINode;

public class CKPage extends CKGUINode implements Comparable<CKPage> 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6202450614525311367L;
	String name;
	
	public CKPage(String n) 
	{
		name = n;
	}

	public CKPage()
	{
		this("");
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	


	@Override
	public int compareTo(CKPage ability)
	{
		
		return name.compareTo(ability.name);
	}


	public String treeString()
	{
		// TODO Auto-generated method stub
		return "    "+toString()+"\n";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CKPage)
		{
			
		    return name.compareTo(((CKPage) obj).name)==0;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Page "+name;
	}
	
	
	/*
	 *  GUI editors
	 *
	 */

	static JPanel []panel;
	static JLabel[] names;
	
	
	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.LINE_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.LINE_AXIS));

			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
		
			panel[0].add(new JLabel("Page: "));
			panel[1].add(new JLabel("Page: "));
			
			
			names= new JLabel[2];
			names[0] = new JLabel();
			names[1] = new JLabel();
			//name
		//	nameFields = new JTextField[2];
		//	nameFields[0]=new JTextField(12);
		//	nameFields[1]=new JTextField(12);
			panel[0].add(names[0]);
			panel[1].add(names[1]);
			
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
		
		names[index].setText(name);
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
		//none
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
