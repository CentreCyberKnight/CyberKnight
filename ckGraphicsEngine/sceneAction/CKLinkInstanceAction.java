package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.CKGraphicsEngine.RelationalLinkType;
import ckGraphicsEngine.assets.CKAssetInstance;

public class CKLinkInstanceAction extends CKSceneAction
{
	CKAssetInstance child;
	CKAssetInstance parent;
	RelationalLinkType type;
	
	
	public CKLinkInstanceAction(CKAssetInstance child,CKAssetInstance parent,
			RelationalLinkType type,int stime)
	{
		super(stime);
		this.child=child;
		this.parent=parent;
		this.type=type;
	}
	
	/**
	 * This constructor is in place to remove a link.
	 * @param child instance to have link removed from
	 * @param stime time to start this action
	 */
	public CKLinkInstanceAction(CKAssetInstance child,int stime)
	{	
		this(child,null,RelationalLinkType.NONE,stime);
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
			child.setParent(parent,type);
		}
	}
	
}
