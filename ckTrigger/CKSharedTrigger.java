package ckTrigger;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKTriggerFactory;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckSatisfies.Satisfies;


public class CKSharedTrigger extends CKTriggerNode implements ActionListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 427007074944930655L;
	private String triggerName;
	private CKTriggerNode trigger;
	
	
	
	public CKSharedTrigger(String Tid)
	{//have to be careful with how we initialize.
		this.setAllowsChildren(false);
		setTriggerName(Tid);
	}
	
	public CKSharedTrigger()
	{
		this("NULL");
	}
	
	
	
	
	/**
	 * @return the trigName
	 */
	public String getTriggerName()
	{
		return triggerName;
	}



	/**
	 * @param trigName the trigName to set
	 */
	public void setTriggerName(String trigName)
	{
		this.triggerName = trigName;
		trigger = CKTriggerFactory.getInstance().getAsset(trigName);
		//FIXME this is a hot fix--I have no idea what will happen in larger systems....
		trigger.setParent(this);//will swap children around
	}




	private final static Icon sharedTriggerIcon = new ImageIcon("ckEditor/images/tools/SharedTriggerTreeIcon.png");
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeIcon(boolean, boolean)
	 */
	@Override
	public Icon getTreeIcon(boolean leaf, boolean expanded)
	{
		return sharedTriggerIcon;
	}

	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		return new CKSharedTrigger(triggerName);
	}

	/**
	 * @return the satisfy
	 */
	public Satisfies getSatisfy()
	{
		return trigger.getSatisfy();
	}


	/**
	 * @return the action
	 */
	public CKGameAction getAction()
	{
		return trigger.getAction();
	}

	@Override
	public CKGameAction getFailedAction() {
		return trigger.getFailedAction();
	}

	/**
	 * @return the result
	 */
	public TriggerResult getResult()
	{
		return trigger.getResult();
	}
	
	/*
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		JMenu menu = new JMenu("");
//		menu.add(CKSatistfiesAddMenu.getMenu(tree) );
//		menu.add(CKGameActionAddMenu.getMenu(tree) );
		return menu;		
	}
		
*/
	
	
	public boolean shouldActNow(CKSpellCast cast)
	{
		return trigger.shouldActNow(cast);
	}
	
	public TriggerResult doTriggerAction(CKGameActionListenerInterface boss,boolean init,CKSpellCast cast	)
	{
		return trigger.doTriggerAction(boss, init, cast);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Shared Trigger:  " +triggerName;
	}
	
	
	
	static JPanel []panel;
	static JLabel[] nameText;
	static JButton[] pickTriggerButton;
	
	static private void initPanel(boolean force,Object caller)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.LINE_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.LINE_AXIS));

			nameText = new JLabel[2];
			nameText[0] = new JLabel();
			nameText[1] = new JLabel();
			
			panel[0].add(new JLabel("Shared Trigger "));
			panel[0].add(nameText[0]);		
			
			panel[1].add(new JLabel("Shared Trigger "));
			panel[1].add(nameText[1]);
			
			
			pickTriggerButton = new JButton[2];
			pickTriggerButton[0] = new JButton("Pick Trigger");
			pickTriggerButton[1] = new JButton("Pick Trigger");
			
			panel[0].add(pickTriggerButton[0]);
			panel[1].add(pickTriggerButton[1]);
			
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
		
		nameText[index].setText(trigger.getName());
	}
	
	private static ActionListener listener = null;
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		pickTriggerButton[EDIT].removeActionListener(listener);
		setPanelValues(EDIT);
		
		listener = this;
		
		pickTriggerButton[EDIT].addActionListener(listener);
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		//cannot be altered!
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

	
	
	
	class PickerListener implements CKEntitySelectedListener<CKTrigger>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKTrigger a)
		{
			//CKAssetEditor.this.entitySelected(a);
			setTriggerName(a.getAID());
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
			
			//force redraw
			getTreeCellEditorComponent(null, null, false,false,false ,0);
		}

	}
	
@Override
public void actionPerformed(ActionEvent arg0)
{
	JFrame frame = new JFrame();
	CKXMLFilteredAssetPicker<CKTrigger,CKTriggerFactory> picker = 
			new CKXMLFilteredAssetPicker<CKTrigger,CKTriggerFactory>(CKTriggerFactory.getInstance());		
	picker.addSelectedListener(new PickerListener(frame));
	frame.add(picker);
	frame.pack();
	frame.setVisible(true);
	
}

	/*
	 * These should not be called...
	 * 
	 */
	/*
	@Override
	public void setSatisfy(Satisfies satisfy)
	{
		trigger.setSatisfy(satisfy);
		
	}

	@Override
	public void setAction(CKGameAction action)
	{
		trigger.setAction(action);
	}

	@Override
	public void setResult(TriggerResult result)
	{
		trigger.setResult(result);		
	}
	
	*/
	
	
	
}
