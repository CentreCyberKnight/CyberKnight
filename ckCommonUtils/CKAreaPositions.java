package ckCommonUtils;

public class CKAreaPositions extends CKPosition
{
	private CKPosition [] area;
	
	
	public CKAreaPositions()
	{
		this(0,0,0,0,null);
	}
	
	public CKAreaPositions(double x,double y,double z,int depth,CKPosition [] area)
	{
		super(x,y,z,depth);
		this.area = area;		
	}
	
	
	public CKAreaPositions(double x,double y,double z,int depth)
	{
		this(x,y,z,depth,null);
	}
	
	public CKAreaPositions(CKPosition pos,CKPosition [] area)
	{
		super(pos);
		this.area = area;		
	}
	

	/**
	 * @return the area
	 */
	public CKPosition[] getArea()
	{
		return area;
	}


	/**
	 * @param area the area to set
	 */
	public void setArea(CKPosition[] area)
	{
		this.area = area;
	}
	
	
	

	
	
	
}
