package ckEditor.treegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckGameEngine.CKGridActor;
import ckGameEngine.Event;

public class CKTeamList extends CKGUINode
{

	
	
	private static final long serialVersionUID = 3901706905627372955L;



	public CKGridActor getPC(String name)
	{
		CKGridActor actor = null;
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			actor = team.getPC(name);
			if(actor != null)
			{
				return actor;
			}			
		}
		return null;
		
	}

	

	public String getPCAssetId(String name)
	{
		String id;
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			id = team.getPCAssetId(name);
			if(id.compareTo("")!=0)
			{
				return id;
			}			
		}
		return "";
		
	}

	public String[] getActorNames()
	{
		return getActorList().toArray(new String[0]);		
	}
	
	public List<String> getActorList()
	{
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			list.addAll(team.getActorList());
		}
		return list;		
	}
	
	


	public Vector<CKGridActor> getActorsFromTeam(String teamID)
	{
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			if(team.getTeamId().compareTo(teamID)==0)
			{
				return team.getGridActorList();
			}
		}
		return new Vector<CKGridActor>();
	}

	public Vector<CKGridActor> getGridActors()
	{
		Vector<CKGridActor> actors = new Vector<CKGridActor>();
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			actors.addAll(team.getGridActorList());
		}
		return actors;
	}
	
	
	
	
	public void  enqueueActors(PriorityQueue <Event>queue)
	{
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			team.enqueueActors(queue);
			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKTeamNode)
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
				node.add( (MutableTreeNode) ((CKTeamNode) o ).clone());
			}
		}
		return node;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Team List ";
	}
	
	
		
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
			
			JMenuItem addActor = new JMenuItem("Add Team");
			addActor.addActionListener(new TeamListener(tree));
			
			return addActor;
	}
	

	
	class TeamListener implements ActionListener
	{
	
	    CKTreeGui tree;
		
		public TeamListener(CKTreeGui t)
		{
			tree=t;
		}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode	node	= tree.getSelected();
			
			if(node == null)
			{
				return;
			}	
			tree.addNode(node,new CKTeamNode());		
		}
			
}



	public void addActor(ActorNode act, String teamID)
	{
		for (int i=0;i<getChildCount();i++)
		{
			CKTeamNode team = (CKTeamNode) getChildAt(i);
			if(team.getTeamId().compareTo(teamID)==0)
			{
				team.add(act);
				return;
			}
		}
		//create anew
		CKTeamNode team = new CKTeamNode();
		team.setTeamId(teamID);
		team.add(act);
		add(team);
		
		
	}


	
	
	
}
