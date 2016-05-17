package ckDatabase;
import static ckCommonUtils.CKDatabaseTools.DBAT;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;

import ckCommonUtils.CKURL;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.CKLayeredAsset;
import ckGraphicsEngine.assets.CKNullAsset;
import ckGraphicsEngine.assets.CKRegulatedAsset;
import ckGraphicsEngine.assets.CKSharedAsset;
import ckGraphicsEngine.assets.CKSpriteAsset;
import ckGraphicsEngine.assets.TileType;

/**A CK GraphicsAsset fFactory that uses XML files store data
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKGraphicsAssetFactoryXML extends CKGraphicsAssetFactory
{

	/*static private String AT_TYPE = "graphics_asset_type";
	
	private static Connection connection;
	public static String DBAT = CKDatabaseTools.DBAT;
	public static String DBAUT="graphics_asset_usage_type";
	public static String DBAU="graphics_asset_usage";
	public static String DBAP="graphics_asset_portrait";

	//FIXME this should use a singleton pattern?
*/
	private static HashMap<String,CKGraphicsAsset> assetMap = null;
	private static HashMap<String,String> portraitMap=null;
	private static HashMap<String,Vector<String> > usageMap=null;
	
	private static CKGraphicsAssetFactoryXML factory= null;
	
	public static CKGraphicsAssetFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKGraphicsAssetFactoryXML();
		}
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	private CKGraphicsAssetFactoryXML()
	{
		super();
		assetMap = new HashMap<String,CKGraphicsAsset>();
		
		//read in portraits
		CKURL u;
		try
		{
			u = new CKURL(XMLDirectories.GRAPHIC_PORTRAITS);
		
			XMLDecoder d = new XMLDecoder(u.getInputStream());
			portraitMap = (HashMap<String,String>) d.readObject();
			d.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			System.err.println("Portrait File Missing" );
			portraitMap = new HashMap<String,String>();
		}
		
	}

	public static String generateUniqueAssetName()
	{
		try
		{
			String path = new CKURL(XMLDirectories.GRAPHIC_ASSET_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("asset", ".xml", new File(path));
			String name =uniqueFile.getName(); 
			return name.substring(0, name.lastIndexOf(".xml"));
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OOPS";

	}
	public static void writeAssetToXMLDirectory(CKGraphicsAsset asset)
	{
		try
		{
			if(asset.getAID().length()==0)
			{
				asset.setAID(generateUniqueAssetName());
			}
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_ASSET_DIR+asset.getAID()+".xml");
			asset.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static CKGraphicsAsset readAssetFromXMLDirectory(String assetID)
	{
		try
		{
			CKURL u = new CKURL(XMLDirectories.GRAPHIC_ASSET_DIR+assetID+".xml");
			return CKGraphicsAsset.readFromStream(u.getInputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public  CKGraphicsAsset getGraphicsAsset(String assetID)
	{
		//check it if exists
		CKGraphicsAsset asset= assetMap.get(assetID);
		if(asset ==null)
		{
			//need to get it from the database now.
			asset=readAssetFromXMLDirectory(assetID);
			
			if(asset!=null)  //store results into hash table to speed up work 
			{
				assetMap.put(assetID,asset);
			}
			else          {      System.err.println(assetID+"not Found"); }
		}		
		return asset;
	}

	/**
	 * Returns the portrait assigned to the assent listed.  If not portrait exists
	 * return the asset instead.
	 * @param assetID
	 * @return
	 */
	public CKGraphicsAsset getPortrait(String assetID)
	{
		String newAsset = portraitMap.get(assetID);
		if(newAsset ==null)
		{
			return this.getGraphicsAsset(assetID);
		}
		else
		{
			return this.getGraphicsAsset(newAsset);
		}
	}
	
	public CKGraphicsAsset getPortrait(CKGraphicsAsset asset)
	{
		return getPortrait(asset.getAID());
	}
		
	protected void storePortraits()
	{
		CKURL u;
		try
		{
			u = new CKURL(XMLDirectories.GRAPHIC_PORTRAITS);
		
			XMLEncoder d = new XMLEncoder(u.getOutputStream());
			d.writeObject(portraitMap);
			d.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void assignPortrait(String assetID,String portraitID)
	{
		portraitMap.put(assetID, portraitID);
		storePortraits();
	}
	
	public void assignPortrait(CKGraphicsAsset asset, CKGraphicsAsset portrait)
	{
		assignPortrait(asset.getAID(),portrait.getAID());
	}
	
	/**
	 * Removes any portrait assignment for this asset
	 * @param asset
	 */
	public void unassignPortrait(CKGraphicsAsset asset)
	{
		String id  = asset.getAID();
		assignPortrait(id,id);
	}
	
	
	
	public Iterator<CKGraphicsAsset> getAllGraphicsAssets()
	{
		File folder;
		Vector<CKGraphicsAsset> vec=new Vector<CKGraphicsAsset>();
		
		try
		{
			folder = new File (new CKURL(XMLDirectories.GRAPHIC_ASSET_DIR).getURL().getFile());
		
			for (File f : folder.listFiles())
			{
				vec.add(getGraphicsAsset(f.getName().replaceFirst("[.][^.]+$","")));
			}
		}	catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	
		return vec.iterator();
	}
		

			
	/**
	 * Initializes the usage map for assets.  This should only be called when the accessing files,
	 * Not web pages or JAR files.
	 */
	@SuppressWarnings("unchecked")
	protected void readUsages()
	{
		if(usageMap==null)
		{
			usageMap = new HashMap<String,Vector<String>>();
			try
			{

				File folder;
				folder = new File (new CKURL(XMLDirectories.GRAPHIC_ASSET_USAGE_DIR).getURL().getFile());
			
				for (File f : folder.listFiles())
				{
					//read in portraits
					String name = XMLDirectories.GRAPHIC_ASSET_USAGE_DIR + f.getName();
					XMLDecoder d = new XMLDecoder(new CKURL(name).getInputStream());
					usageMap.put(f.getName().replaceFirst("[.][^.]+$", ""), (Vector<String>) d.readObject() );
					d.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}		
	}
		

	protected void  storeUsage(String t)
	{
		readUsages();
	
		CKURL u;
		try
		{
			u = new CKURL(XMLDirectories.GRAPHIC_ASSET_USAGE_DIR+t+".xml");
		} catch (MalformedURLException e1)
		{
			e1.printStackTrace();
			return;
		}
		
		Vector<String> vec = usageMap.get(t);
	
		if(vec == null)
		{
			File f = new File(u.getURL().getFile());
			f.delete();			
		}
		else
		{
			try
			{
				XMLEncoder d = new XMLEncoder(u.getOutputStream());
				d.writeObject(usageMap.get(t));
				d.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	

	public Iterator<CKGraphicsAsset> getFilteredGraphicsAssets(String filter)
	{
		if(filter==null ) { return getAllGraphicsAssets(); }

		readUsages();
	
		Vector<String> vec = usageMap.get(filter);
		if(vec==null){ return getAllGraphicsAssets(); }
		
		Vector<CKGraphicsAsset> ret = new Vector<CKGraphicsAsset>();
		for(String s: vec) 
		{
			ret.add(getGraphicsAsset(s));
		}
		return ret.iterator();		
	}


	@Override
	public Iterator<CKGraphicsAsset> getFilteredGraphicsAssets(String[] filters)
	{
		if(filters.length==0)
		{
			return this.getAllGraphicsAssets();
		}
		if(filters.length ==1)
		{
			return getFilteredGraphicsAssets(filters[0]);
		}
		readUsages();
		Iterator<CKGraphicsAsset> iter = getAllGraphicsAssets();
		ArrayList<CKGraphicsAsset>  vec = new ArrayList<CKGraphicsAsset>();
	
		while(iter.hasNext())
		{
			CKGraphicsAsset asset = iter.next();
			for(String s:filters)
			{
				if(this.hasUsage(asset.getAID(),s))
				{
					vec.add(asset);
					break;
				}
			}
		}
		
		return vec.iterator();
	}


	
	
	public void makeUsageType(String t)
	{
		readUsages();
		if(usageMap.get(t)==null)
		{
			usageMap.put(t,new Vector<String>());
		}
		storeUsage(t);
		
	}
	
	public void removeUsageType(String t)
	{
		readUsages();
		usageMap.remove(t);
		storeUsage(t);		
	}
	
	public void assignUsageTypeToAsset(String assetID, String type)
	{
		readUsages();
		if(hasUsage(assetID,type))
		{
			return;
		}
		Vector<String> vec = usageMap.get(type);
		if(vec==null)
		{
			vec = new Vector<String>();
			usageMap.put(type, vec);
		}
		vec.add(assetID);
		storeUsage(type);
	}
		
	public void assignUsageTypeToAssset(CKGraphicsAsset asset, String t)
	{
			assignUsageTypeToAsset(asset.getAID(),t);
	}
	

	public void unassignUsageTypeToAsset(String assetID, String type)
	{
		readUsages();
		Vector<String> vec = usageMap.get(type);
		if(vec!=null)
		{
			vec.remove(assetID);
			storeUsage(type);
		}
	}		

	public void unassignUsageTypeToAssset(CKGraphicsAsset asset, String t)
	{
			assignUsageTypeToAsset(asset.getAID(),t);
	}
	

	@Override
	public Iterator<String> getAllGraphicsUsages()
	{
		readUsages();
		return usageMap.keySet().iterator();
	}
	

	public boolean hasUsage(String assetID, String name)
	{
		readUsages();
		Vector<String> vec = usageMap.get(name);
		if(vec!=null)
		{
			return vec.contains(assetID);
		}
		return false;
	}		
	

	public static String getAssetDescription(int aid, Statement stmt) throws SQLException
	{
		String Q = "SELECT asset_description FROM "+DBAT+" WHERE asset_id="+aid+";";
		ResultSet set=stmt.executeQuery(Q);
		if(set.next())
		{
			return set.getString("asset_description");
		}
		return null;
		
	}
	
	
	
	public static void setAssetDescription(int aid, String description,Statement stmt) throws SQLException
	{
		String Q = "UPDATE "+DBAT+ " SET asset_description=\'"+description+"\' WHERE asset_id="+aid+";";
		stmt.executeUpdate(Q);
		
	}
	
	
	
	
	public void createQuickSprite(String base,String filename,int w,int h,int frames,int rows)
	{
		CKImageAsset A1 = new CKImageAsset(base,base,w,h,frames,rows,TileType.SPRITE,
				  XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+filename);
		writeAssetToXMLDirectory(A1);
			//2
		
			CKSharedAsset a2a = new CKSharedAsset(base +"SW",base+" SW", base,0);
			writeAssetToXMLDirectory(a2a);
			//8
			CKSharedAsset a2b = new CKSharedAsset(base+"NW",base+" NW", base,1);
			writeAssetToXMLDirectory(a2b);
			//9
			CKSharedAsset a2c = new CKSharedAsset(base+"NE",base+" NE", base,2);
			writeAssetToXMLDirectory(a2c);
			//10
			CKSharedAsset a2d = new CKSharedAsset(base+"SE",base+" SE", base,3);
			writeAssetToXMLDirectory(a2d);
		
			CKSpriteAsset heroS = new CKSpriteAsset(base+"Sprite",base+" Sprite");
			heroS.addAnimation("SOUTHWEST", a2a);
			heroS.addAnimation("NORTHWEST", a2b);
			heroS.addAnimation("NORTHEAST", a2c);
			heroS.addAnimation("SOUTHEAST", a2d);
			writeAssetToXMLDirectory(heroS);
			factory.assignUsageTypeToAsset(base+"Sprite","sprite");
		
	}
		
	public static void createTestDB()
	{
		CKGraphicsAssetFactoryXML factory = new CKGraphicsAssetFactoryXML();
		String floor = "floor";
		String sprite="sprite";
		String portrait = "portrait";
		String icon="icon";
		//String highlight="highlight";
		String SpellEffect ="spell_effect";
		String over ="over";
			
			//1
			/*CKImageAsset(stmt,"person",
			32,64,6,4,TileType.SPRITE,
			  "images/sprites_map_claudius.png");
			  */
		//NULL ASSET
		writeAssetToXMLDirectory(CKNullAsset.getNullAsset());
			//a1
		CKImageAsset A1 = new CKImageAsset("Hero", "person",48,96,6,4,TileType.SPRITE,
				  XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"big_person.png");
		writeAssetToXMLDirectory(A1);
			System.out.println("a1");
			//2
			CKSharedAsset a2a = new CKSharedAsset("heroSW","Hero SW", "Hero",0);
			writeAssetToXMLDirectory(a2a);
			System.out.println("a2");
			//8
			CKSharedAsset a2b = new CKSharedAsset("heroNW","Hero NW", "Hero",1);
			writeAssetToXMLDirectory(a2b);
			//9
			CKSharedAsset a2c = new CKSharedAsset("heroNE","Hero NE", "Hero",2);
			writeAssetToXMLDirectory(a2c);
			//10
			CKSharedAsset a2d = new CKSharedAsset("heroSE","Hero SE", "Hero",3);
			writeAssetToXMLDirectory(a2d);
		
			CKSpriteAsset heroS = new CKSpriteAsset("heroSprite","Hero Sprite");
			heroS.addAnimation("SOUTHWEST", a2a);
			heroS.addAnimation("NORTHWEST", a2b);
			heroS.addAnimation("NORTHEAST", a2c);
			heroS.addAnimation("SOUTHEAST", a2d);
			writeAssetToXMLDirectory(heroS);
			factory.assignUsageTypeToAsset("heroSprite",sprite);

			//43 dad portrait
			CKImageAsset dpor = new CKImageAsset("HeroPortrait","dad",100,131,1,1,TileType.BASE,
					  XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"dad_portrait.jpg");
			writeAssetToXMLDirectory(dpor);
			factory.assignUsageTypeToAsset(dpor.getAID(),portrait);
			factory.assignPortrait("heroSprite",dpor.getAID());

			
			//19 baby sheet
			factory.createQuickSprite("baby","babysheet3.png",64,128,4,4);
			
			//41 baby portrait
			CKImageAsset bpor = new CKImageAsset("babyPortrait","baby ",100,120,1,1,TileType.BASE,
					  XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"baby_portrait.JPG");
			writeAssetToXMLDirectory(bpor);
			factory.assignUsageTypeToAsset(bpor.getAID(),portrait);
			factory.assignPortrait("babySprite",bpor.getAID());
			
			
			
			//26 mom sheet
			factory.createQuickSprite("mom","momsheet2.png",48,96,1,4);

			
			//42 mom portrait
			CKImageAsset mpor = new CKImageAsset("momPortrait","mom ",100,150,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"mom_portrait.jpg");
			writeAssetToXMLDirectory(mpor);
			factory.assignUsageTypeToAsset(mpor.getAID(),portrait);
			factory.assignPortrait("momSprite",mpor.getAID());

			
			
			
			//4
			CKImageAsset blue = new CKImageAsset("blue","blue field",64,32,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"highlight_baseTile.png");
			writeAssetToXMLDirectory(blue);
			factory.assignUsageTypeToAsset("blue",floor);
			
			
			//5			
			CKImageAsset cf = new CKImageAsset("cliffFace","Cliff",64,80,1,1,TileType.SUB,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"wall_subTile.png");
			writeAssetToXMLDirectory(cf);

			//6 layered
			
			CKLayeredAsset bCliff = new CKLayeredAsset("bCliff","Cliff Face");
			bCliff.addAsset(blue);
			bCliff.addAsset(cf);
			writeAssetToXMLDirectory(bCliff);
			factory.assignUsageTypeToAsset("bCliff",floor);

			
			//13			
			CKImageAsset green = new CKImageAsset("green","green field",64,32,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"grass_baseTile.png");
			writeAssetToXMLDirectory(green);
			factory.assignUsageTypeToAsset("green",floor);

			//14 layered
			CKLayeredAsset gCliff = new CKLayeredAsset("gCliff","green Cliff Face");
			gCliff.addAsset(green);
			gCliff.addAsset(cf);
			writeAssetToXMLDirectory(gCliff);
			factory.assignUsageTypeToAsset("gCliff",floor);
		
			
			//33 tiled floor
			CKImageAsset KT =new  CKImageAsset("KTile","KitchenTile",64,32,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"kitchen.png");
			writeAssetToXMLDirectory(KT);
			factory.assignUsageTypeToAsset("KTile", floor);
			//34 combined floor
			CKLayeredAsset KT2 = new CKLayeredAsset("kFloor","Thick Kitchen Floor");
			KT2.addAsset(cf);
			KT2.addAsset(KT);
			writeAssetToXMLDirectory(KT2);
			factory.assignUsageTypeToAsset(KT2.getAID(),floor);
			//39 fridge
			CKImageAsset fridge=new CKImageAsset("fridge","fridge ",64,100,1,1,TileType.OVER,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"fridge.png");
			writeAssetToXMLDirectory(fridge);
			//40 fridge floor
			CKLayeredAsset fridgeFloor = new CKLayeredAsset("fridgeFloor","Fridge on Floor");
			fridgeFloor.addAsset(fridge);
			fridgeFloor.addAsset(KT2);
			writeAssetToXMLDirectory(fridgeFloor);
			factory.assignUsageTypeToAsset(fridgeFloor.getAID(),floor);
			
			//16 stone background
			CKImageAsset stone = new CKImageAsset("stone", "stone", 128, 128, 1, 1,TileType.BASE, 
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"stoneBackdrop.png");
			writeAssetToXMLDirectory(stone);
			//17 cloud foreground
			CKImageAsset dark = new CKImageAsset("darkClouds", "darkClouds", 512, 512, 1, 1,TileType.BASE, 
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"darkClouds.png");
			writeAssetToXMLDirectory(dark);
			//32cloud foreground
			CKImageAsset light = new CKImageAsset("lightClouds", "LightClouds", 256,256, 1, 1,TileType.BASE, 
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"LightClouds.png");
			writeAssetToXMLDirectory(light);
			//7
			CKImageAsset swirl = new CKImageAsset("Swirl","Swirl", 3,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"swirl.png");
			writeAssetToXMLDirectory(swirl);
			//15 slow spin
			CKRegulatedAsset slowswirl = new CKRegulatedAsset("SlowSwirl","SlowSwirl","Swirl",10);
			writeAssetToXMLDirectory(slowswirl);
			
//			CKRegulatedAsset.insertXMLAsset(stmt,"10 fps", 10, 7);
			
			
			
			
			
			/*			//35 counter
			CKImageAsset(stmt,"counter",64,64,1,1,TileType.OVER,
					  "images/counter.png");
			factory.assignUsageTypeToAsset(35,floor);
			//36
			int[]counter={35,34};
			CKLayeredAsset.insertXMLAsset(stmt,"Kitchen Floor Counter", counter);
			factory.assignUsageTypeToAsset(36,floor);
			//37 stove
			CKImageAsset(stmt,"stove ",64,70,1,1,TileType.OVER,
					  "images/stove2.png");
			factory.assignUsageTypeToAsset(37,floor);
			//38
			int[] stove={37,34};
			CKLayeredAsset.insertXMLAsset(stmt,"Kitchen Floor Stove", stove);
			factory.assignUsageTypeToAsset(38,floor);
			
			
			
			
			/*
			
			
		//11 test sprites
			//12 test tiles
			int [] tids ={7,6};
			double [] heights={1,0};
			CKTileAsset.insertXMLAsset(stmt,"together", tids, heights);
			factory.assignUsageTypeToAsset(12,floor);
			//15 slow spin
			CKRegulatedAsset.insertXMLAsset(stmt,"10 fps", 10, 7);
*/
			//44 boot icon
			CKImageAsset iconA = new CKImageAsset("boots","combat boot",64,59,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"combatBoot.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//45 up arrow
			iconA = new CKImageAsset("upArrow","up arrow",40,48,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"uparrow.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//46 left arrow icon
			iconA = new CKImageAsset("leftArrow","left arrow",45,48,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"leftArrow.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//47 right arrow icon
			iconA = new CKImageAsset("rightArrow","right arrow",45,48,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"rightArrow.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//48 uturn arrow icon
			iconA = new CKImageAsset("uTurn","u turn",48,41,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"Uturn.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//49 equipment icon
			iconA = new CKImageAsset("equipment","equipment",64,64,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"equipmentIcon.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//50 axe icon
			iconA = new CKImageAsset("axe","axe icon",64,56,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"axeIcon.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//51 wand icon
			iconA = new CKImageAsset("wand","wand icon",62,64,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"wandIcon.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//52 ballet shoe icon
			iconA = new CKImageAsset("balletShoes","ballet shoes",43,64,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"balletshoes.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//53 bare feet icon
			iconA = new CKImageAsset("bareFeet","bare feet",49,64,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"barefeet.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//54 wooden spoon icon
			iconA = new CKImageAsset("woodenSpoon","Wooden Spoon",14,64,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"spoon.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//55 sparkles icon
			iconA = new CKImageAsset("sparkles","Sparkles",42,48,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"sparkles.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			//56 mouth icon
			iconA = new CKImageAsset("lips","Lips",64,26,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"lips.png");
			writeAssetToXMLDirectory(iconA);
			factory.assignUsageTypeToAsset(iconA.getAID(),icon);
			
			
			//57 rear highlight
			CKImageAsset A = new CKImageAsset("yellowRH","Yellow Rear Highlight",64,32,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"yellow_backHighlightTile.png");
			writeAssetToXMLDirectory(A);
			//58 front highlight
			CKImageAsset A2 = new CKImageAsset("yellowFH","Yellow Front Highlight",64,32,1,1,TileType.BASE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"yellow_frontHighlightTile.png");
			writeAssetToXMLDirectory(A2);
			//59 yellow orb
			CKImageAsset A3 = new CKImageAsset("orb","Yellow Orb",36,36,4,1,TileType.SPRITE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"orb_sprite.png");
			writeAssetToXMLDirectory(A3);
			//60 yellow orb2
			CKImageAsset A4 = new CKImageAsset("orb2","Yellow Orb Outlined",36,36,4,1,TileType.SPRITE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"orb2_sprite.png");
			writeAssetToXMLDirectory(A4); 
			//61 target
			CKImageAsset A5 = new CKImageAsset("dummy","dummy",24,53,1,1,TileType.SPRITE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"target.png");
			writeAssetToXMLDirectory(A5);
			factory.assignUsageTypeToAsset(A5.getAID(),sprite);
			//62 illuminate 
			CKImageAsset A6 = new CKImageAsset("illuminate","Illuminate effect",36,36,2,1,TileType.SPRITE,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"illuminate_sprite.png");
			writeAssetToXMLDirectory(A6);
			factory.assignUsageTypeToAsset(A6.getAID(),SpellEffect); 
			
			//pineBlock
			
			CKImageAsset PB = new CKImageAsset("pineBlock","height one block",36,64,1,1,TileType.OVER,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"pineBox.png");
			writeAssetToXMLDirectory(PB);
			factory.assignUsageTypeToAsset(PB.getAID(),over); 

			
			//stoneBlock
			
			CKImageAsset SB = new CKImageAsset("stoneBlock","height two block",36,96,1,1,TileType.OVER,
					XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR+"stoneblock.png");
			writeAssetToXMLDirectory(SB);
			factory.assignUsageTypeToAsset(SB.getAID(),over); 

		
		
			
			
		
	}
	
	public static void main(String[] args)
	{
		
		
		createTestDB();
		
		
		
		CKGraphicsAsset person = (new CKGraphicsAssetFactoryXML()).getGraphicsAsset("babySprite");
		
		JFrame frame = new JFrame();
		CKAssetViewer view=new CKAssetViewer(30,person,new Dimension(256,256));
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//Thread animator = new Thread(test);
		//animator.start();
		   frame.addWindowListener(new WindowAdapter(){
	           public void windowClosing(WindowEvent e)
	           	{
	               	System.exit(0);
	           	}
	       	});
		
		
		
		
		
		
		
		System.out.println("YEA!");
	}

}
