package ckSound;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.sceneAction.CKSceneAction;

public class CKSoundStopAction extends CKSceneAction{

	CKSound cks;
	

	public CKSoundStopAction(int stime, CKSound cks) {
		super(stime);
		// TODO Auto-generated constructor stub
		
		
		this.cks = cks;
		
	}

	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame) {
		// TODO Auto-generated method stub
		
		if((cks.isRunning() == true ) && frame >= endTime)
		{
			
			cks.stop();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
