package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.assets.CKAssetInstance;


public class CKRemoveInstanceAction extends CKSceneAction
{
	
	CKAssetInstance asset;

	
	
	public CKRemoveInstanceAction(CKAssetInstance a,int stime)
	{
		super(stime);
		asset=a;
	}


	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		if(startTime <=frame && frame<=endTime)
		{
			System.out.print("Getting rid of instance");
			if(scene.removeInstanceFromScene(asset))
				{
				System.out.println("Success");
				}
			else
			{
				System.out.println("Fail");
			}
		}
	}
	
}
