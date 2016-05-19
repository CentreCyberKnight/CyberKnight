package ckGraphicsEngine;

public abstract class CKSceneMouseListener 
implements CKGraphicMouseInterface
{
	private CKGraphicsSceneInterface scene;
	
	CKSceneMouseListener(CKGraphicsSceneInterface s)
	{
		scene=s;
	}
	
	final public CKGraphicsSceneInterface getScene()
	{ return scene;}
	
	
}
