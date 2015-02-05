package ckCommonUtils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
public class TabComponent extends JPanel 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 840752656089278284L;
	TabButton button;
	//JLabel label;
	CKTabIconPane containingWin;
	
	public TabComponent(Icon icon, String s, CKTabIconPane tp, boolean canRemove)
	{
		containingWin=tp;
		

		setIcon(icon,s);
		if(canRemove)
		{ 
			addRemoveButton();
		}
		

		setVisible(true);
	}
	
	
	
	public void setIcon(Icon icon,String s)
	{
		removeAll();
		setOpaque(false);
		JButton label = new JButton();
		label.setFocusable(false);
		label.setOpaque(false); //
		label.setBorder(null);
		label.setContentAreaFilled(false);
		label.setFocusPainted(false);
		label.setFocusable(false);

		if(icon!=null) 
		{
			label.setIcon(icon);
		}
		else
		{
			label.setText(s);
		}
		label.setToolTipText(s);
		label.addActionListener(new SwitchToTab());
		add(label);
		
		
		
		
		
		
	}
	
	protected void addRemoveButton()
	{
		button=new TabButton();
		button.setForeground(Color.RED);
		button.setBounds(86, 3, 14, 14);
		button.setVisible(true);
		button.addActionListener(new RemoveTab());
		add(button);
	
	}
	
	
	
	public class RemoveTab implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			containingWin.removeTab(TabComponent.this);
		}
	}
	
	
	public class SwitchToTab implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			containingWin.setSelectedIndex(containingWin.indexOfTabComponent(TabComponent.this));
			
		}
		
		
	}
	
	
	
	
	public class TabButton extends JButton
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7753631695503497197L;
		/*date*/
		
		
		public TabButton()
		{
			//this.setPreferredSize(new Dimension(10,18));
			this.setText("x");
			
		}

	}
	
}



