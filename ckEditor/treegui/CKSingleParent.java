package ckEditor.treegui;

import javax.swing.JFrame;
import javax.swing.tree.MutableTreeNode;

import ckGameEngine.actions.CKNullAction;
import ckSatisfies.TrueSatisfies;

public class CKSingleParent extends CKGUINode

{
	
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5753930438633354120L;
	String name = "";
	
	
	public CKSingleParent(CKGUINode node)
	{//have to be careful with how we initialize.
		if(node != null) add(node);
		setChildOrderLocked(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		if(this.getChildCount()>0)
		{
			return new CKSingleParent( (CKGUINode) ((CKGUINode) this.getChildAt(0) ).clone());
		}
		return new CKSingleParent();
	}

	
	public CKSingleParent()
	{
		this(null);
	}
		
		
/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		if( getChildCount() >0)
			{	remove(0); }
			super.insert(newChild,0);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SingleParent";
	}
	
/*
	static JPanel []panel;
	static JTextField[] nameText;
	static JComboBox[] resultBox;

	
	static private void initPanel(boolean force,Object caller)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			nameText = new JTextField[2];
			nameText[0] = new JTextField();
			nameText[1] = new JTextField();
			
			panel[0].add(new JLabel("Trigger   "));
			panel[0].add(nameText[0]);		
			
			panel[1].add(new JLabel("Trigger   "));
			panel[1].add(nameText[1]);		

			resultBox = new JComboBox[2];
			resultBox[0]=new JComboBox(TriggerResult.values());
			resultBox[1]=new JComboBox(TriggerResult.values());
			panel[0].add(resultBox[0]);
			panel[1].add(resultBox[1]);
			

		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true,this); }
		panel[index].setBackground(colors[index]);
		
		nameText[index].setText(name);
		nameText[index].setColumns(15);
		resultBox[index].setSelectedItem(result);
	}
	*/
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	/*
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);
	}
*/
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	/*@Override
	public void storeComponentValues()
	{
		name = (String)nameText[EDIT].getText();
		result = (TriggerResult)resultBox[EDIT].getSelectedItem();
	}
*/
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	/*
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}
*/
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Single Parent Test");
		CKGuiRoot root = new CKGuiRoot();
		CKSingleParent p1 = new CKSingleParent(new TrueSatisfies());
		CKSingleParent p2 = new CKSingleParent(new CKNullAction());
		
		root.add( p1);
		root.add(p2);
		frame.add(new CKTreeGui(root));
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
	
	
	
}
