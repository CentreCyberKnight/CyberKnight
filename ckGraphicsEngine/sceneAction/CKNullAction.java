package ckGraphicsEngine.sceneAction;

import ckGraphicsEngine.CKGraphicsSceneInterface;

public class CKNullAction extends CKSceneAction
{

	public CKNullAction(int stime, int etime)
	{
		super(stime, etime);
		
	}

	@Override
	public void performAction(CKGraphicsSceneInterface scene, int frame)
	{
		//do nothing you are just here until your allotted time.

	}

}
