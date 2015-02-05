package ckCommonUtils;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ckEditor.Artifact.window;



public class CKTabIconPane extends JTabbedPane implements ActionListener
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4968390871789157803L;
	
	public RequestNewComponentListener compController=null;
	public boolean canRemove=true;
	
	public CKTabIconPane() 
	{		
		super();
	}
	
	public CKTabIconPane(RequestNewComponentListener l,boolean canRemove,boolean canAdd)
	{
		compController = l;
		this.canRemove=canRemove;
		if (l != null && canAdd)// http://stackoverflow.com/questions/1971141/java-add-tab-button-for-a-jtabbedpane
		{

			addTab("test", null);
			FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

			// Make a small JPanel with the layout and make it non-opaque
			JPanel pnlTab = new JPanel(f);
			pnlTab.setOpaque(false);
			// Create a JButton for adding the tabs
			JButton addTab = new JButton("+");
			addTab.setOpaque(false); //
			addTab.setBorder(null);
			addTab.setContentAreaFilled(false);
			addTab.setFocusPainted(false);

			addTab.setFocusable(false);
			pnlTab.add(addTab);
			setTabComponentAt(getTabCount() - 1, pnlTab);

			addTab.setFocusable(false);
			addTab.addActionListener(this);
			//setVisible(true);

		}
	}	

	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Component c = compController.requestNewComponent();
		String s = compController.requestNewDescriptionForComponent(c);
		Icon icon = compController.requestNewIconforComponent(c);
		
		addTab(c,icon,s);
				
	}
	
	
	
	
	public TabComponent addTab(Component c,String s)
	{
		return addTab(c,null,s);
	}
	
	public TabComponent addTab(Component c, Icon icon, String s)
	{
		this.add(c);
		TabComponent tComp = new TabComponent(icon,s,this,canRemove);
		this.setTabComponentAt(getTabCount()-1,tComp);
		return tComp;
	}

	public TabComponent updateTab(Component c,Icon icon,String s)
	{
		int index = this.indexOfComponent(c);
		TabComponent tComp = new TabComponent(icon,s,this,canRemove);
		this.setTabComponentAt(index,tComp);
		return tComp;
		
	}
	
	
	public void removeTab(TabComponent c)
	{
		if(compController!=null)
		{
			compController.notifyComponentRemoved(this.getComponent(indexOfTabComponent(c)));
		}
		remove(indexOfTabComponent(c));

	}
		

	
	public static void main(String [] args)
	{
		
		JFrame frame = new JFrame();
		CKTabIconPane p = new CKTabIconPane(new LabelMaker(),true,true);
		
		Icon actorIcon = new ImageIcon("ckEditor/images/tools/actor.png");
		Icon targetIcon = new ImageIcon("ckEditor/images/tools/aim.png");
		Icon left_arrowIcon = new ImageIcon("ckEditor/images/tools/left_arrow.png");
		p.addTab(new JLabel("p1"), left_arrowIcon, "Left");
		p.addTab(new JLabel("p2"), null, "aim");
		p.addTab(new JLabel("p3"), actorIcon, "actor");
		
		
		
		frame.add(p);
		
		frame.setVisible(true);
		frame.setSize(1100, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setLocationRelativeTo(null);
		
		
		
	}

	
	public static class LabelMaker implements RequestNewComponentListener
	{

		int i=0;
		Icon targetIcon = new ImageIcon("ckEditor/images/tools/aim.png");
		
		@Override
		public Component requestNewComponent()
		{
			i++;
			return new JLabel("Label "+i);
		}

		@Override
		public Icon requestNewIconforComponent(Component comp)
		{

			return targetIcon;
		}

		@Override
		public String requestNewDescriptionForComponent(Component comp)
		{
			return ((JLabel)comp).getText();
		}

		@Override
		public void notifyComponentRemoved(Component comp)
		{
			System.out.println("Thanks for telling me "+requestNewDescriptionForComponent(comp));
			
		}
		
	}
	
	
	
}
