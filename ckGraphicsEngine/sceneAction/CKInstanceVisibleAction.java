package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKAssetInstance;

public class CKInstanceVisibleAction extends CKSceneAction
{
	CKAssetInstance inst;
	boolean visible;
	
	
	public CKInstanceVisibleAction(CKAssetInstance inst,boolean visible,int stime)
	{
		super(stime);
		this.inst=inst;
		this.visible=visible;
	}
	


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		if(startTime <=frame && frame<=endTime)
		{
			inst.setVisible(visible);
		}
	}
	
}
