package ckDatabase;

/**
 * 
 * @author Michael K. Bradshaw
 *
 */
@Deprecated
public class CKGraphicsSceneFactoryXML //implements CKSceneFactory
{/*
	private static CKGraphicsSceneFactoryXML instance;

	private CKGraphicsSceneFactoryXML()
	{
	}
	
	public static CKGraphicsSceneFactoryXML getInstance()
	{
		if(instance==null)
		{
			instance = new CKGraphicsSceneFactoryXML();
		}
		return instance;
	}
		

	public static String generateUniqueSceneName()
	{
		try
		{
			String path = new CKURL(XMLDirectories.GRAPHIC_SCENES_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("scene", "", new File(path));
			return uniqueFile.getName();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OOPS";

	}
	

	public static void writeSceneToXMLDirectory(CKGraphicsScene scene)
	{
		try
		{
			if(scene.getAID().length()==0)
			{
				scene.setAID(generateUniqueSceneName());
			}
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_SCENES_DIR+scene.getAID()+".xml");
			scene.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static CKGraphicsSceneInterface readSceneFromXMLDirectory(String sceneID)
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_SCENES_DIR+sceneID+".xml");
			return CKGraphicsScene.readFromStream(u.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Returns the layer stored in the database with id lid.
	 * Layers are not unique, so there is no need to keep them unique.
	 * @param aid
	 * @return
	 */
	/*public CKGraphicsSceneInterface getGraphicsScene(String sid)
	{
		return readSceneFromXMLDirectory(sid);
	}

	
	public Iterator<CKGraphicsSceneInterface> getAllGraphicsScenes()
	{
		File folder;
		Vector<CKGraphicsSceneInterface> vec=new Vector<CKGraphicsSceneInterface>();
		
		try
		{
			folder = new File (new CKURL(XMLDirectories.GRAPHIC_SCENES_DIR).getURL().getFile());
		
			for (File f : folder.listFiles())
			{
				vec.add(getGraphicsScene(f.getName().replaceFirst("[.][^.]+$","") )  );
			}
		}	catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	
		return vec.iterator();
		
	
	}

	
	
	/*
	 * Make test database
	 */
	
	

	/*
	public static void createTestDB()
	{
		
		CKGraphicsLayerFactory lfactory = 	CKGraphicsLayerFactoryXML.getInstance();			

		int rows=10;
		int cols=10;
		CKGraphicsScene scene = new CKGraphicsScene("test1","example",
				"asset486111934992328588");
		
		//scene.addLayer(lfactory.getGraphicsLayer("test1"));
		scene.addLayer(lfactory.getGraphicsLayer("StoneBackdrop"));
		//id 1
		writeSceneToXMLDirectory(scene);
			
			
						
		//id 3
		CKGraphicsScene scene3 = new CKGraphicsScene("Kitchen","Kitchen","asset2189318077150772264");
		//scene3.addLayer(lfactory.getGraphicsLayer("Kitchen"));
		scene3.addLayer(lfactory.getGraphicsLayer("lightClouds"));
		//id 3
		writeSceneToXMLDirectory(scene3);
	
	}
	
	
	public static void main(String[] args)
	{
		
		//createTestDB();
			
		
		JFrame frame = new JFrame();
		
		CKGraphicsSceneInterface scene = 
				CKSceneFactory.getInstance().getAsset("test1");
		CKSceneViewer panel = new CKSceneViewer(scene, 1);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		   frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	});
			
		System.out.println("YEA!");
	}

	*/
	
	
	
}
