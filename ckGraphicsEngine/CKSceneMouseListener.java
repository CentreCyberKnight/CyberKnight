package ckGraphicsEngine;

import java.awt.event.MouseAdapter;

public class CKSceneMouseListener extends MouseAdapter
{
	private CKGraphicsSceneInterface scene;
	
	CKSceneMouseListener(CKGraphicsSceneInterface s)
	{
		scene=s;
	}
	
	final public CKGraphicsSceneInterface getScene()
	{ return scene;}
	
	
}
