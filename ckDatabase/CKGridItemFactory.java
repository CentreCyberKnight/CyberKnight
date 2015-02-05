package ckDatabase;

import ckGameEngine.CKGridItem;


public class CKGridItemFactory extends CKXMLFactory<CKGridItem>
{

	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.GRID_ITEMS_DIR;
	}

	
	private static CKGridItemFactory factory= null;
	 
	
	 

	public static CKGridItemFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKGridItemFactory();
		}
		return factory;
	}	
	
	
	
	private static void createTestDB()
	{
		
		CKGridItemFactory factory = CKGridItemFactory.getInstance();
		
		factory.makeUsageType("outdoor");
		factory.makeUsageType("indoor");
		
		
		
	}
	
	
	public static void main(String [] args)
	{
		createTestDB();
	}



	@Override
	public CKGridItem getAssetInstance()
	{
		return new CKGridItem();
	}
	
	
	

}
