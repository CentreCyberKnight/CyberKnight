/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;

/**
 * @author dragonlord
 *
 */
public class CKPCTurnCmd extends CKQuestAction
{
	private static final long serialVersionUID = 3835865644408867827L;
	
	String name;
	boolean isRightTurn;
	int delay;
	
	public CKPCTurnCmd()
	{
		this("HERO",true,15);
	}
	
	public CKPCTurnCmd(String name,boolean isRightTurn, int delay)
	{
		this.name=name;
		this.isRightTurn = isRightTurn;
		this.delay=delay;
	}
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target;
		if(cast!=null) 	{target = cast.getActorTarget(); }
		else					{target = getPC(name); }
		
		//TODO need to handle this as a spell
		
		
		Direction d;
		if(isRightTurn) 
		{ 
			d = target.getDirection().getRightNeightbor();
		}
		else 
		{ 
			d = target.getDirection().getLeftNeightbor();
		}
		
		CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
		grid.setDirection(target, d);
		
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the isRightTurn
	 */
	public boolean isRightTurn()
	{
		return isRightTurn;
	}

	/**
	 * @param isRightTurn the isRightTurn to set
	 */
	public void setRightTurn(boolean isRightTurn)
	{
		this.isRightTurn = isRightTurn;
	}

	/**
	 * @return the delay
	 */
	public int getDelay()
	{
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String dir="right";
		if(! isRightTurn) 
		{
			dir = "left";
		}
		return "Turn " + name + " " +dir +" and wait " + delay;
	}

	/*
	public JMenuItem GUIEdit()
	{
		JMenuItem edit= new JMenuItem("Edit Action");
		
		
		/*
		JMenu menu = new JMenu("Edit Action");
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("Name:"));
		SpinnerListModel modelN = new SpinnerListModel(getQuest().getActors().getActorNames());
		modelN.setValue(name);
		JSpinner spinN = new JSpinner(modelN);
		((JSpinner.DefaultEditor)spinN.getEditor()).getTextField().setColumns(16);
		panel.add(spinN);	
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Turn:"));
		String [] choices = {"Right","Left"};
		SpinnerListModel model = new SpinnerListModel(choices);
		if(isRightTurn) { model.setValue("Right"); }
		else { model.setValue("Left"); }
		JSpinner spin =new JSpinner(model);
		panel.add(spin);		
		menu.add(panel);	

		
		
		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(modelN,model));
		menu.add(store);
	
		return menu;
	}
	*/
	
	class EditAction  implements ActionListener
	{
		SpinnerListModel text;
		SpinnerListModel spin;
		
		public EditAction(SpinnerListModel modelN,SpinnerListModel s)
		{
			text= modelN;
			spin=s;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			name = (String)text.getValue();
			isRightTurn = ( (String)spin.getValue()).compareTo("Right")==0 ;
			
		}
	}

	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static JComboBox<Object> []directionBox;
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			panel[0].add(nameBox[0]);		
			panel[0].add(new JLabel("turns"));
			nameBox[1] = new JComboBox<String>();
			panel[1].add(nameBox[1]);		
			panel[1].add(new JLabel("turns"));

			String [] choices = {"Right","Left"};
			directionBox=new JComboBox[2];
			directionBox[0]=new JComboBox<Object>(choices);
			panel[0].add(directionBox[0]);

			directionBox[1]=new JComboBox<Object>(choices);
			panel[1].add(directionBox[1]);

		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	//@SuppressWarnings("unchecked")
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);
		

		if(getQuest()!=null)
		{
			name = initializeActorBox(nameBox[index],name);
		}
		else
		{

			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
		}
		if(isRightTurn) { directionBox[index].setSelectedItem("Right"); }
		else { directionBox[index].setSelectedItem("Left"); }	
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
		name = (String)nameBox[EDIT].getSelectedItem();
		isRightTurn = ( (String)directionBox[EDIT].getSelectedItem()).compareTo("Right")==0 ;
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
