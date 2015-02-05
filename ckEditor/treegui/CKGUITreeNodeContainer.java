package ckEditor.treegui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * Stole this idea from the class path documents on the defaultTreeCellEditor
 * http://developer.classpath.org/doc/javax/swing/tree/DefaultTreeCellEditor-source.html
 *
 */
public class CKGUITreeNodeContainer extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262660054157512096L;
	private Icon icon;
	private JLabel iconCont;

	private Component comp;
	private JPanel compPanel;
	
	
	public CKGUITreeNodeContainer()
	{
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(2, 0,2,0));
		iconCont=new JLabel();
		add(iconCont,BorderLayout.WEST);
		
		compPanel = new JPanel();
		compPanel.setBackground(Color.WHITE);
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.CENTER;
	    gridbag.setConstraints(compPanel, constraints);
	    compPanel.setLayout(gridbag);
		
		
		
		
		//compPanel.setLayout(new BorderLayout());
		add(compPanel,BorderLayout.CENTER);
	}
	

	
	
	
	/**
	 * @return the icon
	 */
	public Icon getIcon()
	{
		return icon;
	}




	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon)
	{
		this.icon = icon;
		iconCont.setIcon(icon);	
	}




	/**
	 * @return the comp
	 */
	public Component getComp()
	{
		return comp;

	}

	/**
	 * @param component the comp to set
	 */
	public void setComp(Component component)
	{
		if(this.comp != null)
		{
			compPanel.remove(this.comp);
		}
		
		this.comp = component;
		//add(this.comp,BorderLayout.CENTER);
//		compPanel.add(this.comp,BorderLayout.NORTH);
		compPanel.add(this.comp);

	}




	
	
}
	
	
	

