package ckGraphicsEngine.sceneAction;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKGraphicsAsset;


public class CKAddAssetAction extends CKSceneAction
{
	
	CKPosition pos;
	CKGraphicsAsset asset;
	int layerDepth;
	
	
	public CKAddAssetAction(CKPosition p, CKGraphicsAsset a,int depth,
			int stime)
	{
		super(stime);
		asset=a;
		pos=(CKPosition) p.clone();
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
			scene.addAssetToLayer(pos,asset, layerDepth);
		}
	}
	
}
