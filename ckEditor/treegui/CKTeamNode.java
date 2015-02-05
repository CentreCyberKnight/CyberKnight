package ckEditor.treegui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckDatabase.CKTeamFactory;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKTeam;
import ckGameEngine.Event;

public class CKTeamNode extends CKGUINode
{

	
	private static final long serialVersionUID = -2822706926399693719L;
	
	String teamId="NULL";
	


	public CKGridActor getPC(String name)
	{
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			if(actor.getName().compareTo(name)==0)
			{
				return actor.getPC(teamId);
			}			
		}
		return null;
		
	}

	

	public String getPCAssetId(String name)
	{
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			if(actor.getName().compareTo(name)==0)
			{
				return actor.getPC(teamId).getAssetID();
			}			
		}
		return "";
		
	}

	public String[] getActorNames()
	{
		String[] names = new String[ getChildCount() ];
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			names[i] = actor.getName();
		}
		return names;		
	}
	
	public List<String> getActorList()
	{
		//String[] names = new String[ getChildCount() ];
		ArrayList<String> list = new ArrayList<String>(getChildCount());
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			list.add(actor.getName());
		}
		return list;		
	}
	
	
	public Vector<CKGridActor> getGridActorList()
	{
		//String[] names = new String[ getChildCount() ];
		Vector<CKGridActor> list = new Vector<>(getChildCount());
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			list.add(actor.getPC(teamId));
		}
		return list;		
	}
	
	
	
	public void  enqueueActors(PriorityQueue <Event>queue)
	{
		CKTeam team = CKTeamFactory.getInstance().getAsset(teamId);
		for (int i=0;i<getChildCount();i++)
		{
			ActorNode actor = (ActorNode) getChildAt(i);
			//TODO need to work with PC priorities.
			CKGridActor pc = actor.startPC(teamId); //must call to initialize
			pc.setTeam(team);
			team.addCharacter(pc);
			pc.getTurnController().onLoad();
			Event e = pc.getTurnController().getInitialTurnEvent(0);
			if(e!=null)
			{
				queue.add(e);
			}			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof ActorNode)
		{
			super.insert(newChild, childIndex);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		DefaultMutableTreeNode node= (DefaultMutableTreeNode) super.clone();
		if(children != null)
		{
			for(Object o: children)
			{
				//	Satisfies s = (Satisfies) o;
				node.add( (MutableTreeNode) ((ActorNode) o ).clone());
			}
		}
		return node;
	}

	/**
	 * @return the teamId
	 */
	public String getTeamId()
	{
		return teamId;
	}



	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(String teamId)
	{
		this.teamId = teamId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Actor List ";
	}
	
	
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKActorAddMenu.getMenu(tree);
	}
	
	
	
	static JPanel []panel;
	static JComboBox<CKTeam> []teamBox;
	
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
			
			
			teamBox = new JComboBox[2];
			teamBox[0] = new JComboBox<CKTeam>();
			panel[0].add(new JLabel("Team "));
			panel[0].add(teamBox[0]);		

			teamBox[1] = new JComboBox<CKTeam>();
			panel[1].add(new JLabel("Team "));
			panel[1].add(teamBox[1]);		


		}
		
	}


	private void setPanelValues(int index)
	{
		if(panel==null) { initPanel(true); }
		
		
		this.initializeJComboBoxByID(teamBox[index], CKTeamFactory.getInstance().getAllAssets(),teamId);
		/*
		teamBox[index].removeAllItems();
		
		Iterator<CKTeam> iter = 
		while(iter.hasNext())
		{
			CKTeam s = iter.next();
			teamBox[index].addItem(s);
			if(s.getAID().compareTo(teamId)==0)
			{
				teamBox[index].setSelectedItem(s);
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
		teamId = ((CKTeam)teamBox[EDIT].getSelectedItem()).getAID();
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
