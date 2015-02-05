package ckDatabase;

import ckGameEngine.CKGrid;
import ckGraphicsEngine.CKGraphicsScene;
import ckGraphicsEngine.CKGraphicsSceneInterface;

public class CKSceneFactory extends CKXMLFactory<CKGraphicsSceneInterface>
{

	@Override
	public String getBaseDir()
	{
		
		return XMLDirectories.GRAPHIC_SCENES_DIR;
	}

	@Override
	public CKGraphicsSceneInterface getAssetInstance()
	{
		//TODO put in popup to ask for right size!!!
		return new CKGraphicsScene("","", new CKGrid(10,10));
	}
	
	
	
	private static CKSceneFactory factory= null;
	 
	
	 

	public static CKSceneFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKSceneFactory();
		}
		return factory;
	}	
	
	/*
 public CKGraphicsSceneInterface getGraphicsScene(String aid);
 
 public Iterator<CKGraphicsSceneInterface> getAllGraphicsScenes();
 */
}
