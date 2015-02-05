package ckDatabase;

import ckGameEngine.CKGrid;


public class CKGridFactory extends CKXMLFactory<CKGrid>
{

	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.GRIDS_DIR;
	}

	
	private static CKGridFactory factory= null;
	 
	
	 

	public static CKGridFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKGridFactory();
		}
		return factory;
	}	
	
	
	
	private static void createTestDB()
	{
		
		CKGridFactory factory = CKGridFactory.getInstance();
		
		factory.makeUsageType("hometown");
		factory.makeUsageType("outdoor");
		factory.makeUsageType("indoor");
		
		
		
	}
	
	
	public static void main(String [] args)
	{
		createTestDB();
	}



	@Override
	public CKGrid getAssetInstance()
	{
		
		return new CKGrid();
	}
	
	
	

}
