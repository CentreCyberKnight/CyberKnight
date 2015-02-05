package ckSatisfies;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckGameEngine.CKSpellCast;

public class AndSatisfies extends Satisfies
{

	
	
	private static final long serialVersionUID = 4650970642462783892L;

	public AndSatisfies()
	{
		setAllowsChildren(true);
	}
	
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof Satisfies)
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
				node.add( (MutableTreeNode) ((Satisfies) o ).clone());
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
		return "And Satisfies";
	}


	public AndSatisfies(Satisfies satisfiesOne, Satisfies satisfiesTwo)
	{
		this();
		add(satisfiesOne);
		add(satisfiesTwo);
	}
	
	/*
	public void addSatisfies(Satisfies satisfies)
	{
		vec.add(satisfies);
	}
	*/
	@Override
	public boolean isSatisfied(CKSpellCast cast)
	{
		if(children==null)
			return false;
		
		for(Object o: children)
		{
			Satisfies s = (Satisfies) o;
			if(s.isSatisfied(cast)==false)
				return false;
		}
		return true;
	}
	
	
}
