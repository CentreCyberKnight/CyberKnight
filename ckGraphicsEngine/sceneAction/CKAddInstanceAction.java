package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKAssetInstance;

public class CKAddInstanceAction extends CKSceneAction
{
	CKAssetInstance instance;
	int layerDepth;
	
	
	public CKAddInstanceAction(CKAssetInstance inst,int depth,
			int stime)
	{
		super(stime);
		instance=inst;
		layerDepth = depth;
	}


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		if(startTime <=frame && frame<=endTime)
		{
			//This should work since it is called by the scene
			scene.addInstanceToLayer(instance, layerDepth);
			instance.setVisible(true);
		}
	}
	
}
