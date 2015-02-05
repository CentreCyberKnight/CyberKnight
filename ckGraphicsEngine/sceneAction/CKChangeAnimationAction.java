package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.UnknownAnimationError;
import ckGraphicsEngine.assets.CKSpriteInstance;

public class CKChangeAnimationAction extends CKSceneAction
{
	CKSpriteInstance instance;
	String animation;
	
	public CKChangeAnimationAction(CKSpriteInstance instance,
			String animation,int stime)
	{
		super(stime);
		this.instance=instance;
		this.animation=animation;
	}
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.sceneAction.CKSceneAction#performAction(ckGraphicsEngine.CKGraphicsScene, int)
	 */
	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		if(startTime <=frame && frame<=endTime)
		{
		
			try
			{
				instance.setAnimation(animation);
			} catch (UnknownAnimationError e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
