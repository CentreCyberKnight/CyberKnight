package ckTrigger;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTree;
import ckDatabase.CKTriggerListFactory;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameActionListenerInterface;


public class CKSharedTriggerList extends CKTriggerListNode
{



	private static final long serialVersionUID = -9049507497125492393L;
	
	private String TLid;
	private CKTriggerListNode list;

	


	public CKSharedTriggerList()
	{
		this("NULL");
	}
	
	
	public CKSharedTriggerList(String n)
	{
		this.setAllowsChildren(false);
		setTLid(n);		
	}
	
	
	/**
	 * @return the tLid
	 */
	public String getTLid()
	{
		return TLid;
	}


	/**
	 * @param tLid the tLid to set
	 */
	public void setTLid(String tLid)
	{
		TLid = tLid;
		list = CKTriggerListFactory.getInstance().getAsset(TLid);
		list.setParent(this);
	}
	
			
			

	public TriggerResult doTriggers(CKGameActionListenerInterface boss,boolean init,CKSpellCast cast)
	{
		return list.doTriggers(boss, init, cast);
	}
	
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return list.getName();
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		list.setName(name);
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		return new CKSharedTriggerList(TLid);
	}


	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#GUIAddNode(ckGraphics.treegui.CKTreeGui)
	 */
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
			
			JMenuItem addActor = new JMenuItem("Can't Add Trigger");
			//addActor.addActionListener(new TreeAddTriggerListener(tree));
			
			return addActor;
		}
	
	

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Shared TriggerList "+list.toString();
	}
	
	
	
	static JPanel []panel;
	static JComboBox<CKTriggerList> []triggerListBox;
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			
			panel[0].setBackground(colors[0]);
			panel[1].setBackground(colors[1]);
			
			
			triggerListBox = new JComboBox[2];
			triggerListBox[0] = new JComboBox<CKTriggerList>();
			panel[0].add(new JLabel("Shared TriggerList"));
			panel[0].add(triggerListBox[0]);		

			triggerListBox[1] = new JComboBox<CKTriggerList>();
			panel[1].add(new JLabel("Shared TriggerList"));
			panel[1].add(triggerListBox[1]);		


		}
		
	}


	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		this.initializeJComboBoxByID(triggerListBox[index],
				CKTriggerListFactory.getInstance().getAllAssets(),TLid);
		/*
		triggerListBox[index].removeAllItems();
		
		Iterator<CKTriggerList> iter = CKTriggerListFactory.getInstance().getAllAssets();
		String safeName ="NULL";
		while(iter.hasNext())
		{
			String s = iter.next().getAID();
			triggerListBox[index].addItem(s);
			if(s.compareTo(TLid)==0)
			{
				safeName=s;
				triggerListBox[index].setSelectedItem(safeName);
			}
		}
		*/
	
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
		setTLid(((CKTriggerList)triggerListBox[EDIT].getSelectedItem()).getAID());
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
