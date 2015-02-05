package ckTrigger;

import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKXMLAsset;
import ckEditor.CKGUINodePropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKSpellCast;
import ckGameEngine.actions.CKGameActionListenerInterface;


public class CKTriggerList extends CKTriggerListNode implements CKXMLAsset<CKTriggerList>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8352591688558659358L;
	//private String name;
	private String AID;
	
	public CKTriggerList()
	{
		this("");
	}
	
	
	public CKTriggerList(String n)
	{
		name = n;
		setAllowsChildren(true);
	}
	
	
			
			

	@SuppressWarnings("unchecked")
	public TriggerResult doTriggers(CKGameActionListenerInterface boss,boolean init,CKSpellCast cast)
	{
		if(children==null) { return TriggerResult.UNSATISFIED; }
		
		Iterator<CKTriggerNode> iter = (Iterator<CKTriggerNode>) children.iterator();
		while(iter.hasNext())
		{
			TriggerResult result = iter.next().doTriggerAction(boss,init,cast);
			if(result == TriggerResult.SATISFIED_ONCE)
			{
				iter.remove();
			}
			else if(result == TriggerResult.SATISFIED_END_QUEST)
			{
				return result;
			}
			else if(result == TriggerResult.SATISFIED_END_LOOP)
			{
				return result;
			}
		}
		return TriggerResult.UNSATISFIED;		
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



	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{
		if(newChild instanceof CKTriggerNode)
		{
			super.insert(newChild , childIndex);
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
				node.add( (MutableTreeNode) ((CKTriggerNode) o ).clone());
			}
		}
		return node;
	}


	
	@Override
	public String getAID()
	{
		return AID;
	}


	@Override
	public void setAID(String a)
	{
		AID=a;
	}


	@Override
	public JComponent getXMLAssetViewer()
	{
		
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	
	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		
		return new CKTreeGui(this);
	}


	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKTriggerList> getXMLPropertiesEditor()
	{
		return new CKGUINodePropertiesEditor<CKTriggerList>(this);
	}


	
	
	
	
	
	
	
	
	
	
	
}
