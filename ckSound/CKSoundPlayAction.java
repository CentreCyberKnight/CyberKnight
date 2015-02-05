
package ckSound;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.sceneAction.CKSceneAction;

public class CKSoundPlayAction extends CKSceneAction {

	CKSound cks;
	double vol;

	public CKSoundPlayAction(int stime, int etime, CKSound cks, double vol) {
		super(stime, etime);
		// TODO Auto-generated constructor stub
		
		
		this.cks = cks;
		this.vol = vol;
	}





	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame) {
		// TODO Auto-generated method stub

		if(startTime <=frame && frame<=endTime)
		{
			cks.loop();
			cks.playAtPercent(vol);
		}
		
		if(frame >= endTime)
		{
			cks.stop();
		}
		
		
			
			
	}

	/*//look in ck2dGRaphics engine
	public int move(int tid, int IID, int startFrame, CKPosition orgin,CKPosition destination,int speed) throws BadInstanceIDError
	{ 
		
		//do not need to clone positions as they will be cloned in ckmoveInstanceAction
		CKAssetInstance inst = getInstance(IID);
		
		//need to calc ending time
		int frames = CKMoveInstanceAction.calcTravelTime(orgin, destination, speed);
		
		//System.out.println("I need "+frames+"to complete this action");
		CKMoveInstanceAction a1=new CKMoveInstanceAction(orgin,destination,inst,startFrame,startFrame+frames);
		
		
		actions.add(a1);
		
		
		return startFrame+frames;
	}
	
	private CKAssetInstance getInstance(int iID) {
		// TODO Auto-generated method stub
		return null;
	}*/



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
