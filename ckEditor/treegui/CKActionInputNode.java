package ckEditor.treegui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

public class CKActionInputNode extends CKGUINode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String Action="null";
	String description;
	
	public CKActionInputNode()
	{
		this("Type in action");
	}
	
	public CKActionInputNode(String description)
	{
		this.description=description;
		this.setAllowsChildren(false);
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return Action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		Action = action;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString()
	{
		return "CKActionInputNode [description=" + description + "]";
	}
	
	///////////////////////////////////////
	//mouse event
	class AssetViewerPopupListener implements ActionListener
	{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			setAction(nameFields[EDIT].getText());
			System.out.println(Action);
			
		}
	}
	/////////////////////////////////////////
	
	
	
	//panel
	static private JPanel[] panel;
	static JLabel[] descLabel;
	static JTextField[] nameFields;
	static JButton[] enterAction;
	static AssetViewerPopupListener ActionListener;
	
	
	private void initPanel(boolean force)
	{
		if(panel==null||force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();
			
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));
			
			JPanel [] top = new JPanel[2];
			top[0]=new JPanel();
			top[1]=new JPanel();

			//description
			descLabel = new JLabel[2];
			descLabel[0]=new JLabel();
			descLabel[1]=new JLabel();
			
			
			top[0].add(descLabel[0]);
			top[1].add(descLabel[1]);
			
			
			JPanel [] bottom = new JPanel[2];
			bottom[0]=new JPanel();
			bottom[1]=new JPanel();

			
			
			
			
			//name of the action
			nameFields = new JTextField[2];
			nameFields[0]=new JTextField(15);
			nameFields[1]=new JTextField(15);
			
			//button
			enterAction=new JButton[2];
			enterAction[0]=new JButton("Enter the Action");
			enterAction[1]=new JButton("Enter the Action");
			
			
			bottom[0].add(nameFields[0]);
			bottom[1].add(nameFields[1]);
			
			bottom[0].add(enterAction[0]);
			bottom[1].add(enterAction[1]);
			
			panel[0].add(top[0]);
			panel[0].add(bottom[0]);
			
			panel[1].add(top[1]);
			panel[1].add(bottom[1]);
		}
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		if(panel==null){ initPanel(true); }
		panel[index].setBackground(colors[index]);
		
		descLabel[index].setText(description);
		nameFields[index].setText(Action);
	}
	
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		enterAction[EDIT].removeActionListener(ActionListener);
		setPanelValues(EDIT);
		ActionListener=new AssetViewerPopupListener();
		enterAction[EDIT].addActionListener(ActionListener);
		return panel[EDIT];	
	}
	
	@Override
	public void storeComponentValues()
	{
		System.out.println("Storing the value:::::");
		System.out.println("Edit"+nameFields[EDIT].getSelectedText());
		System.out.println("Edit"+nameFields[RENDER].getSelectedText());
		//setAction(nameFields[EDIT].getSelectedText());
		//Action=(String)nameFields[EDIT].getSelectedText();
		System.out.println("Action is: "+Action);
	}
	
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
			
			CKActionInputNode asset = new CKActionInputNode();
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