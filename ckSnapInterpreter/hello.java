package ckSnapInterpreter;

public class hello
{
	protected int a;
	protected String greeting;
	protected boolean z;
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public hello(int x, String y, boolean z)
	{
		super();
		this.a = x;
		this.greeting = y;
		this.z = z;
	}
	/**
	 * @return the x
	 */
	public int getX()
	{
		return a;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x)
	{
		this.a = x;
	}
	/**
	 * @return the y
	 */
	public String getY()
	{
		return greeting;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(String y)
	{
		this.greeting = y;
	}
	/**
	 * @return the z
	 */
	public boolean isZ()
	{
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(boolean z)
	{
		this.z = z;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "hello [x=" + a + ", y=" + greeting + ", z=" + z + "]";
	}
	
	
}
