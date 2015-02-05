package ckSatisfies;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckEditor.CKSatistfiesAddMenu;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;

public class OrSatisfies extends Satisfies {

	private static final long serialVersionUID = -695355969847219335L;


	public OrSatisfies()
	{
		setAllowsChildren(true);
	}
	
	public OrSatisfies(Satisfies satisfiesOne, Satisfies satisfiesTwo)
	{
		this();
		add(satisfiesOne);
		add(satisfiesTwo);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof Satisfies)
		{
			super.insert(  newChild, childIndex);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Or Satisfies";
	}

	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{
		if(children == null)
			return false;
		for(Object o: children)
		{			
			Satisfies s = (Satisfies)o;
			if(s.isSatisfied(cast)==true)
				return true;
		}
		return false;
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
				//		Satisfies s = (Satisfies) o;
				node.add( (MutableTreeNode) ((Satisfies) o ).clone());
			}
		}
		return node;
	}
	
	
	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKSatistfiesAddMenu.getMenu(tree);
	}
}
