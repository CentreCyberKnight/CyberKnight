package ckDatabase;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;

import ckCommonUtils.CKURL;

public class UsageManager
{
	/**
	 * Initializes the usage map for assets.  This should only be called when the accessing files,
	 * Not web pages or JAR files.
	 */
	
	private Map<String,Vector<String>> usageMap = null;
	private String baseDir;
	public UsageManager(String baseDir)
	{
		this.baseDir = baseDir;
	}
	
	
	protected void readUsages()
	{
		if(usageMap==null)
		{
			String basePath = baseDir+XMLDirectories.ASSET_USAGE_DIR; 
		
				try
				{
					usageMap = new CKURL(basePath)
					.listFiles()
					.filter(s->s.endsWith(".usage"))
					.map(e->e.substring(0, e.length()-6))
					.collect(Collectors.toMap(name->name,
							name->{

								Vector<String> vec = new Vector<String>();
								String path = basePath+ name+".usage";
						
									try
									{
										Scanner scan = new Scanner(new CKURL(path).getInputStream());
										while(scan.hasNextLine())
										{
											vec.add(scan.nextLine().trim());
										}
										scan.close();

									} catch (Exception e1)
									{
										System.err.println("Unable to read usage: "+path);
										e1.printStackTrace();
									}
							
									return vec;						
						}));
				} catch (MalformedURLException e)
				{
					System.err.println("Unable to read Directory of usages: "+baseDir);
					e.printStackTrace();
					
				}
		}
	}
		
	/**
	 * stores usage in the base directory
	 * @param t: a string of non-zero, non-negative length
	 */
	protected void storeUsage(String t)
	{
		if(t.length()<=0){
			return;
		}
		readUsages();
	
		CKURL u;
		try
		{
			u = new CKURL(baseDir+XMLDirectories.ASSET_USAGE_DIR+t+".usage");
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
			java.util.Collections.sort(vec);
			
			try
			{
				PrintWriter out = new PrintWriter(u.getOutputStream());
				vec.stream().forEachOrdered(n -> out.println(n));
				out.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
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
	
	
	public Vector<String> getUsageList(String name)
	{
		readUsages();
		
		Vector<String> vec = usageMap.get(name);
		if(vec!=null)
		{
			return new Vector<String>(vec);
		}
		return null;

		
		
	}
	
	


	
	
	
	
}
