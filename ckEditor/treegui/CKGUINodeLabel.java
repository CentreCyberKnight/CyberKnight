package ckEditor.treegui;

public class CKGUINodeLabel extends CKGUINode
{
	String label;
	
	
	
	public CKGUINodeLabel()
	{
		this("Empty Label");
	}
	
	public CKGUINodeLabel(String l)
	{
		label=l;
		this.setAllowsChildren(false);
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}
	
	public String toString()
	{
		return label;
	}

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	
	
	
	
	
}
