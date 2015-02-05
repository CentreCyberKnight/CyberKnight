package ckCommonUtils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



public class CKTabPane extends JTabbedPane  
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4968390871789157803L;
	int len=0;
	public CKTabPane() 
	{		
		super();
	}
	
	public class TabComponent extends JPanel implements ActionListener
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 840752656089278284L;
		TabButton button;
		JLabel label;
		CKTabPane containingWin;
		
		public TabComponent(String s, CKTabPane tp)
		{
			containingWin=tp;
			
			setPreferredSize(new Dimension(100,21));
			
			label=new JLabel(s);
			label.setBounds(0, 0, 73, 16);
			label.setVisible(true);
			setLayout(null);
			add(label);
			setBackground(new Color(255,255,255,0));//entirely transparent to let the gui show through
			
			button=new TabButton();
			button.setForeground(Color.RED);
			button.setBounds(86, 3, 14, 14);
			button.setVisible(true);
			button.addActionListener(this);
			add(button);
			setVisible(true);
		}
		
		public void setLabel(String s)
		{
			label.setText(s);
		}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			containingWin.removeTab(this);
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
			this.setPreferredSize(new Dimension(10,18));
			this.setText("x");
			
		}

	}
	
	
	
	public void addTab(Component c, String s)
	{
		this.add(c);
		this.setTabComponentAt(len, new TabComponent(s,this));
		len++;
	}

	public void removeTab(TabComponent c)
	{
		remove(indexOfTabComponent(c));
		len--;
	}
		

}
