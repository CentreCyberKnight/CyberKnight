package ckCommonUtils;

public class CKLock
{

	/**
	 * I believe that the synchronization in the methods removes the need for this to be volatile
	 */
	private boolean locked = false;
	
	
	public synchronized void lock() 
	{
		while(locked) //need a while since more than one might wake up.
		{
			try
			{
				wait();
			} catch (InterruptedException e)
			{
				//good to do
			}
		}
		locked=true;		
	}
	
	public synchronized void unlock()
	{
		if(locked)
		{
			locked=false;
			notify();
		}		
	}
	

}
