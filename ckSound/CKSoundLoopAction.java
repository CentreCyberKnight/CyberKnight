package ckSound;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.sceneAction.CKSceneAction;

public class CKSoundLoopAction extends CKSceneAction{
	
	CKSound cks;
	double vol;
	
	public CKSoundLoopAction(int stime, CKSound cks, double vol)
	{
		super(stime);
	
		this.cks = cks;
		this.vol = vol;
	}
	
	
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame) {
		// TODO Auto-generated method stub

		if(startTime <=frame)
		{
			cks.playAtPercent(vol);
			
			cks.loop();
			
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
