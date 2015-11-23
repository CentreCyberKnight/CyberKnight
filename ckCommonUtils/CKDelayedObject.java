package ckCommonUtils;

/*
 * This class is used to provide a object for things that cannot be calculated immediatly. 
 * 
 * This class will returned and when the value is set, it will beready to use
 * 
 */
public class CKDelayedObject<T>
{
	
	private volatile T value = null;
	
	public CKDelayedObject()
	{
			
	}
	
	public CKDelayedObject(T val)
	{
		value = val;
	}
	
	public boolean isSet()
	{
		return value !=null;
	}
	
	public void setValue(T val)
	{
		value = val;
	}
	
	public T getValue()
	{
		return value;
	}

}
