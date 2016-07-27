package ckDatabase;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.stream.Collectors;

import ckCommonUtils.CKURL;
import ckCommonUtils.CKXMLAsset;

/**A CK GraphicsAsset fFactory that uses XML files store data
 * 
 * @author Michael K. Bradshaw
 *
 */
abstract public class CKXMLFactory<T extends CKXMLAsset<T>> 
{


	
/*
 * 	private static CKXMLFactory factory= null;
 * 
 *
 

	
	public static CKXMLFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKXMLFactory();
		}
		return factory;
	}
*/

	
	private HashMap<String,T> assetMap = null;
	private HashMap<String,Vector<String> > usageMap=null;
	private boolean shouldReload=false; 
		
	protected CKXMLFactory()
	{
		super();
		assetMap = new HashMap<String,T>();
	}


	
	public void clearCache()
	{
		assetMap.clear();
		
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract String getBaseDir();
	
	/**
	 * @return the shouldReload
	 * @return is a boolean
	 */
	public boolean isShouldReload()
	{
		return shouldReload;
	}

	/**
	 * @param shouldReload: the shouldReload to set
	 */
	public void setShouldReload(boolean shouldReload)
	{
		this.shouldReload = shouldReload;
	}

	/**
	 * creates random instance of the correct type
	 * @return a new instance of T
	 */
	public abstract T getAssetInstance();
	
	
	/**
	 *  Name generator for created assets, names in the format: asset + number string+ .<!-- -->xml
	 * @return: a unique name for an asset upon success, "OOPS" for failure
	 */
	public String generateUniqueAssetName()
	{
		try
		{
			String path = new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR).getURL().getFile();
			File uniqueFile = File.createTempFile("asset", ".xml", new File(path));
			String name =uniqueFile.getName(); 
			return name.substring(0, name.lastIndexOf(".xml"));
		} catch (IOException e)
		{
		
			e.printStackTrace();
		}
		return "OOPS";

	}

	
	/**
	 * Adds the asset to the xml directory in CK Data.
	 * @param asset
	 */
	public void writeAssetToXMLDirectory(T asset)
	{
		try
		{
			if(asset.getAID().length()==0)
			{
				asset.setAID(generateUniqueAssetName());
			}
			CKURL u = new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR+asset.getAID()+".xml");
			asset.writeToStream(u.getOutputStream());
		} catch (IOException e)
		{
		
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 
	 * @param assetID
	 * @return node representing the asset owning the param
	 */
	public T readAssetFromXMLDirectory(String assetID)
	{
		try
		{			
			CKURL u = new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR+assetID+".xml");
			XMLDecoder d = new XMLDecoder(u.getInputStream());
			
			@SuppressWarnings("unchecked")
			T node = (T) d.readObject();			
			d.close();
			return node;
		} catch (IOException e)
		{
			System.err.println("Unable to read asset"+assetID);
			e.printStackTrace();
		}
		return null;
	}
	
	
	public  T getAsset(String assetID)
	{
		return getAsset(assetID,shouldReload);
	}
	
	/**
	 * Used to get either a local asset or one from the directory.
	 * @param assetID
	 * @param forceReload
	 * @return asset The asset sought 
	 */
	public  T getAsset(String assetID,boolean forceReload)
	{
		//check it if exists
		
		T asset= assetMap.get(assetID);
		if(asset ==null || forceReload)
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

	
	
	
	public Vector<T> getAllAssetsVectored()
	{
		/*
			File folder;
			Vector<T> vec=new Vector<T>();
			
			try
			{
				folder = new File (new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR).getURL().getFile());
			
				for (File f : folder.listFiles())
				{
					
					String filename =f.getName().replaceFirst("[.][^.]+$","");
					//System.err.print("Reading file"+f);
					vec.add(getAsset(filename));
					//System.err.println("Done Reading file");
					
				}
			}	catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			return vec;
		*/
		try
		{
			CKURL url = new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR);
			return url.listFiles().filter(e->e.endsWith(".xml"))
			.map(e->getAsset(e.substring(0, e.length()-4)))
			.collect(Collectors.toCollection(Vector<T>::new));
			
			
		} catch (MalformedURLException e)
		{
			
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public Iterator<T> getAllAssets()
	{

		Vector<T> vec= getAllAssetsVectored();	
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

				File [] folder = new CKURL(getBaseDir()+XMLDirectories.ASSET_USAGE_DIR).getDirectoryFiles(".xml");		
				for (File f : folder)
				{
					//read in portraits
					String name = getBaseDir()+XMLDirectories.ASSET_USAGE_DIR + f.getName();
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
		
	/**
	 * stores usage in the base directory
	 * @param t: a string of non-zero, non-negative length
	 */
	protected void  storeUsage(String t)
	{
		if(t.length()<=0){
			return;
		}
		readUsages();
	
		CKURL u;
		try
		{
			u = new CKURL(getBaseDir()+XMLDirectories.ASSET_USAGE_DIR+t+".xml");
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
	
	/**
	 * Uses the param to create a usage map and collect only the assets with that usage. 
	 * @param filter
	 * @return asset iterator comprised of only the assets that have the usage specified by the filter
	 */
	public Iterator<T> getFilteredGraphicsAssets(String filter)
	{
		if(filter==null ) { return getAllAssets(); }

		readUsages();
	
		Vector<String> vec = usageMap.get(filter);
		if(vec==null){ return getAllAssets(); }
		
		Vector<T> ret = new Vector<T>();
		for(String s: vec) 
		{
			ret.add(getAsset(s));
		}
		return ret.iterator();		
	}

	/**
	 * Same as getFilteredGraphicsAssets, except returns all assets that match any of the filters
	 * @param filters
	 * @return Iterator of assets
	 */
	public Iterator<T> getFilteredAssets(String[] filters)
	{
		if(filters.length==0)
		{	
			return getAllAssets();
		}
		if(filters.length ==1)
		{
			return getFilteredGraphicsAssets(filters[0]);
		}
		readUsages();
		Iterator<T> iter = getAllAssets();
		ArrayList<T>  vec = new ArrayList<T>();
	
		while(iter.hasNext())
		{
			T asset = iter.next();
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


	
	/**
	 * Adds a usage to the usage map
	 * @param t name of the usage to be added
	 * @return void, but updates the usage map
	 */
	public void makeUsageType(String t)
	{
		if(t.length()<=0){
			return;
		}
		readUsages();
		if(usageMap.get(t)==null)
		{
			usageMap.put(t,new Vector<String>());
		}
		storeUsage(t);
		
	}
	
	/**
	 * Removes a usage from the usage map
	 * @param t name of the usage to be removed
	 * @return void, but updates the usage map
	 */
	public void removeUsageType(String t)
	{
		readUsages();
		usageMap.remove(t);
		storeUsage(t);		
	}
	
	/**
	 * Links an asset to a particular usage 
	 * @param assetID id number of teh asset
	 * @param type the usage to link it to
	 */
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
		
	public void assignUsageTypeToAssset(T asset, String t)
	{
			assignUsageTypeToAsset(asset.getAID(),t);
	}
	
	/**
	 * unlinks a usage from an asset
	 * @param assetID id of the asset to be operated on
	 * @param type the usage to be removed
	 */
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

	public void unassignUsageTypeToAssset(T asset, String t)
	{
			unassignUsageTypeToAsset(asset.getAID(),t);
	}
	

	/**
	 * Create iterator of all usages attached to an asset
	 * @return String iterator of object usage map
	 */
	public Iterator<String> getAllUsages()
	{
		readUsages();
		return usageMap.keySet().iterator();
	}
	
	/**
	 * 
	 * @param assetID asset to check
	 * @param name usage to search for
	 * @return True if the usage is in the asset usage map, false otherwise
	 */
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
	
	public void assetDelete(T asset){
		try
		{
			CKURL u = new CKURL(getBaseDir()+XMLDirectories.ASSET_DIR+asset.getAID()+".xml");
			String path = u.getFileName();
			if(path.charAt(2) == ':'){
				path = path.substring(3, path.length());
			}
			Files.delete(Paths.get(path));
		} catch (IOException e)
		{
		
			e.printStackTrace();
		}
	}
	

}
