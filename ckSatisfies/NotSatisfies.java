package ckSatisfies;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckEditor.CKSatistfiesAddMenu;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;

public class NotSatisfies extends Satisfies {

	private static final long serialVersionUID = 6845066400492791509L;



	public NotSatisfies(Satisfies satisfies)
		{
	//		this.satisfies=satisfies;
			setAllowsChildren(true);	
			add(satisfies);
			
		}
		
		 
		
		public NotSatisfies()
		{
		//	to get XML to work, I can't prefill the node..this(new TrueSatisfies());
			setAllowsChildren(true);
			//add(new TrueSatisfies());
		}
		
		
	/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
		 */
		@Override
		public void insert(MutableTreeNode newChild, int childIndex)
		{
		
			if(newChild instanceof Satisfies)
			{
				//get rid of old one, add new one
				super.removeAllChildren();
				super.insert( newChild, 0);
			}
			
			// TODO Auto-generated method stub
		
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "Not Satisfies";
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


	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{
		//System.out.println("I have how mand kids "+getChildCount());
		if(getChildCount()>0)
		{
			return  ! ( (Satisfies )getChildAt(0) ).isSatisfied(cast);
		}
		return true;
	}



	@Override
	public JMenuItem GUIAddNode(CKTreeGui tree)
	{
		return CKSatistfiesAddMenu.getMenu(tree);
	}

	
	
	 public static void main(String [] args)
	 {
		 NotSatisfies not = new NotSatisfies(new TrueSatisfies());
		 NotSatisfies notB = new NotSatisfies(new FalseSatisfies());
		 System.out.println("Here "+not.isSatisfied(null)+" "+notB.isSatisfied(null));
		 
	 }
	
}
