package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;


abstract public class CKSceneAction implements Comparable<CKSceneAction>
{

	protected int startTime;
	protected int endTime;
	

	
	public CKSceneAction(int stime,int etime)
	{
		startTime=stime;
		endTime=etime;		
	}
	

	
	public CKSceneAction(int instantTime)
	{
		startTime=instantTime;
		endTime=instantTime;		
	}
	
	
	abstract public void performAction(CKGraphicsSceneInterface scene, int frame);
	
	public final void offsetTimes(int offset)
	{
		startTime+=offset;
		endTime+=offset;
	}
	
	/**
	 * @return the startTime
	 */
	public final int getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public final void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public final int getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public final void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

	@Override
	public int compareTo(CKSceneAction arg0)
	{
		if(startTime < arg0.startTime)
		{
			return -1;
		}
		else if (startTime<arg0.startTime)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

}
