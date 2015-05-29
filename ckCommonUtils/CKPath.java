package ckCommonUtils;

public class CKPath extends CKPosition{
	private CKPath parent;
	private String action;
	
	public CKPath()
	{
		this(0,0,0,0,null, "NONE");
	}
	
	public CKPath(double x,double y,double z,int depth,CKPath parent, String action)
	{
		super(x,y,z,depth);
		this.parent = parent;
		this.action = action;
	}
	
	public CKPath(CKPosition pos,CKPath parent,String action)
	{
		super(pos);
		this.parent=parent;
		this.action=action;
				
		
	}
	
	
	public CKPath(double x,double y,double z,int depth)
	{
		this(x,y,z,depth,null, "NONE");
	}


	/**
	 * @return the area
	 */
	public CKPosition getParent()
	{
		return this.parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setArea(CKPath parent)
	{
		this.parent = parent;
	}
	
	public String getAction() {
		return this.action;
	}
}
