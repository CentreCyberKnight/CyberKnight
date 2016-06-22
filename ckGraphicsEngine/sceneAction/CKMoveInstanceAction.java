package ckGraphicsEngine.sceneAction;

import ckCommonUtils.CKPosition;
import ckCommonUtils.INTERPOLATE;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKAssetInstance;

public class CKMoveInstanceAction extends CKSceneAction
{
	CKAssetInstance instance;
	CKPosition sPos;
	CKPosition ePos;
	int framePerTile;
	//////
	INTERPOLATE fun;
	
	/**
	 * Moves an instance to a particular destination across a period of time. 
	 * 
	 * @param startPos - position the action starts at, may be null, then the position will be calculated when  the action takes place.
	 * @param endPos   - Where the instance will end up
	 * @param inst     - instance to move
	 * @param stime    - when to start moving
	 * @param etime    - when to complete moving
	 */
	public CKMoveInstanceAction(CKPosition startPos,CKPosition endPos,
			CKAssetInstance inst, int stime, int etime)
	{
		super(stime, etime);
		instance=inst;
		if(sPos != null)
		{
			sPos=(CKPosition) startPos.clone();
		}
		ePos=(CKPosition) endPos.clone();
		fun=CKPosition::interpolate;
	}
	
	//////////
	public CKMoveInstanceAction(CKPosition startPos,CKPosition endPos,
			CKAssetInstance inst, int stime, int etime,INTERPOLATE f)
	{
		super(stime, etime);
		instance=inst;
		if(sPos != null)
		{
			sPos=(CKPosition) startPos.clone();
		}
		ePos=(CKPosition) endPos.clone();
		fun=f;
	}
	
	


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		if(startTime <=frame && frame<=endTime)
		{
			if (sPos==null)
			{
				sPos=instance.getPosition();
				
			}
			float frac;
			if(endTime-startTime == 0)
			{//instantaneous move
				frac = 1;
				//System.out.println("Instantaneous movement");
			}
			else
			{
				frac= ((float)(frame-startTime))/(endTime-startTime);
			}
		//	System.out.println("Moving "+instance.getAsset().getDescription()+
		//			" with a frac of "+frac+" frame "+frame+" start "+startTime+
		//			" end "+endTime);

			//CKPosition pos = CKPosition.interpolate(sPos,ePos, frac);
			CKPosition pos=fun.interpolate(sPos, ePos, frac);
		//	System.out.println("x coords S"+sPos.getX()+" "+pos.getX()+" "+ePos.getX());
			instance.moveTo(pos);
//			System.out.println("Sprite Position"+pos);
		}
	}
	
	
	
	/**
	 * Returns the number of frames it takes to travel from p1 to p2
	 * @param p1 - initial position
	 * @param p2 - ending position
	 * @param speed - how many frames to cross a tile
	 * @return - number of frames to travel
	 */
	static public int calcTravelTime(CKPosition p1,CKPosition p2,int speed)
	{
		double dist = CKPosition.planarDistance(p1, p2);
		return (int) (dist*speed);		
	}
	

}
