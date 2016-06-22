package ckDatabase;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKURL;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.layers.CKLayerViewer;
import ckGraphicsEngine.layers.CKStaticMatrixLayer;
import ckGraphicsEngine.layers.CKTiledLayer;

/**A CK GraphicsAsset fFactory that uses XML files to store Layers
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKGraphicsLayerFactoryXML implements CKGraphicsLayerFactory
{
	
	private static CKGraphicsLayerFactoryXML instance;
	
	private CKGraphicsLayerFactoryXML()
	{
		
	}
	
	public static CKGraphicsLayerFactoryXML getInstance()
	{
		if(instance==null)
		{
			instance = new CKGraphicsLayerFactoryXML();
		}
		return instance;
	}
	
	public static String generateUniqueLayerName()
	{
		try
		{
			String path = new CKURL(XMLDirectories.GRAPHIC_LAYERS_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("layer", "", new File(path));
			return uniqueFile.getName();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OOPS";

	}
	

	public static void writeLayerToXMLDirectory(CKGraphicsLayer layer)
	{
		try
		{
			if(layer.getLid().length()==0)
			{
				layer.setLid(generateUniqueLayerName());
			}
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_LAYERS_DIR+layer.getLid()+".xml");
			layer.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static CKGraphicsLayer readLayerFromXMLDirectory(String layerID)
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_LAYERS_DIR+layerID+".xml");
			return CKGraphicsLayer.readFromStream(u.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the layer stored in the database with id lid.
	 * Layers are not unique, so there is no need to keep them unique.
	 * @param lid
	 * @return
	 */
	public CKGraphicsLayer getGraphicsLayer(String lid)
	{
		return readLayerFromXMLDirectory(lid);
	}
	
	
	public static void createTestDB()
	{
		
			
		CKGraphicsAssetFactory afactory = CKGraphicsAssetFactoryXML.getInstance();
		//layer 2
			
		int rows=10;
		int cols=10;
		CKGraphicsLayer layer=
					new CKStaticMatrixLayer("test1","Meadow",rows,cols,CKGraphicsLayer.GROUND_LAYER);
			
		for (int i=0;i<rows;i++)
			{
				for(int j=0;j<cols;j++)
				{
					CKPosition pos=new CKPosition(i,j,0,0);
					if((i+1)*(j+1) %2 ==0)
						{
						layer.addAsset(pos,afactory.getGraphicsAsset("bCliff"));
						}
					else
					{
						layer.addAsset(pos,afactory.getGraphicsAsset("gCliff"));
					}
				}
			}
			layer.addAsset(new CKPosition(0,0,0,0),afactory.getGraphicsAsset("fridgeFloor"));
			writeLayerToXMLDirectory(layer);
			//CKGrpahicsLayerFactoryXML layer
			//System.out.println("storing 3rd layer");
		//layer 3
			//asset will be numbered 15 for now
			

			
			CKTiledLayer layer2 = new CKTiledLayer(CKGraphicsLayer.BACKGROUND1_LAYER,
					"StoneBackdrop","Stone backdrop",
					CKTiledLayer.OffsetType.CAMERA_FOLLOW);
			
			layer2.addAsset(new CKPosition(0,0,0,0), afactory.getGraphicsAsset("stone"));
			writeLayerToXMLDirectory(layer2);
				

			//layer 5
			CKTiledLayer layer4 = new CKTiledLayer(CKGraphicsLayer.BACKGROUND1_LAYER,
					"lightClouds","Cloud Forground",
					CKTiledLayer.OffsetType.DRIFT_W_CAMERA);
			
			layer4.addAsset(new CKPosition(2,1,0,0), afactory.getGraphicsAsset("lightClouds"));
			writeLayerToXMLDirectory(layer4);
/*
			//layer 5
			CKTiledLayer layer5 = new CKTiledLayer(CKGraphicsLayer.BACKGROUND1_LAYER,"cloud backdrop",
					CKTiledLayer.OffsetType.DRIFT_W_CAMERA);
			
			layer5.addAsset(new CKPosition(2,1,0,0), afactory.getGraphicsAsset(32));
			layer5.storeLayerToDB(stmt);
*/
			//layer 6 - kitchen
			CKTiledLayer layer6 = new CKTiledLayer(CKGraphicsLayer.BACKGROUND1_LAYER,
					"asset6849515026589853026","black background",
					CKTiledLayer.OffsetType.CAMERA_FOLLOW);
			
			layer6.addAsset(new CKPosition(0,0,0,0), afactory.getGraphicsAsset("asset6849515026589853026"));
			writeLayerToXMLDirectory(layer6);
			
			
			int krows = 10;
			int kcols=krows;
			CKStaticMatrixLayer kitchen = 
					new CKStaticMatrixLayer("Kitchen","Mom's Kitchen",krows,kcols,CKGraphicsLayer.GROUND_LAYER);
					
			for (int i=0;i<rows;i++)
			{	for(int j=0;j<cols;j++)
				{
					CKPosition ckp=new CKPosition(i,j,0,0);
					kitchen.addAsset(ckp,afactory.getGraphicsAsset("kFloor") );
				}				
			}
			/*
			for (int i=0; i<rows;i++)
			{
				kitchen.addAsset(new CKPosition(i,0,0,0), afactory.getGraphicsAsset(36) );
				kitchen.addAsset(new CKPosition(0,i,0,0), afactory.getGraphicsAsset(36) );
			}
			kitchen.addAsset(new CKPosition(0,4,0,0), afactory.getGraphicsAsset(38));
			kitchen.addAsset(new CKPosition(4,0,0,0), afactory.getGraphicsAsset(40));
			*/
			writeLayerToXMLDirectory(kitchen);
		
	}
	
	public static void main(String[] args)
	{
		
		createTestDB();
		
		JFrame frame = new JFrame();
		
		CKLayerViewer view=new CKLayerViewer(30,
				(new CKGraphicsLayerFactoryXML()).getGraphicsLayer("Kitchen"),
				new Dimension(256,256),new Dimension(5,5));
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		
		
		   frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	});
		
		
		
		
		
	}

	
	
	
		
	
	
	public Iterator<CKGraphicsLayer> getAllGraphicsLayers()
	{
		File folder;
		Vector<CKGraphicsLayer> vec=new Vector<CKGraphicsLayer>();
		
		try
		{
			folder = new File (new CKURL(XMLDirectories.GRAPHIC_LAYERS_DIR).getURL().getFile());
		
			for (File f : folder.listFiles())
			{
				vec.add(getGraphicsLayer(f.getName().replaceFirst("[.][^.]+$","") )  );
			}
		}	catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	
		return vec.iterator();
		
	
	}
	
	
	
	
	
	
	
	
	
}
