package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKTransparentAsset;
import ckGraphicsEngine.assets.TransAsset;

public class CKFadeAction extends CKSceneAction{
	private boolean FadeOut;
	
	TransAsset trans;
	
	public CKFadeAction(TransAsset asset,int stime,int etime,boolean FadeOut){
		super(stime,etime);
		
		this.FadeOut=FadeOut;
		trans= asset;
		
		
	}

	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame) {
		double percent=0;
		if(frame<startTime&&FadeOut)
		{
			percent=1;
		}
		else if (frame < endTime)
		{

			if (FadeOut){
				percent = 1-(frame-startTime)/(double)(endTime-startTime); }
			else{
				percent=(frame-startTime)/(double)(endTime-startTime);
			}
			
		}
		else{
			if(!FadeOut){
				
				percent=1;
			}
				
			}
		
		trans.setPercent(percent);
	}
	
	
}