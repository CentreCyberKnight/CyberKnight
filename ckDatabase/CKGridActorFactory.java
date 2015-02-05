package ckDatabase;

import javax.swing.JOptionPane;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridActorOverLay;
import ckGameEngine.CKGridItem;

/*
 * Even though this is a factory for CKgridActors, it will return CGridActors
 * 
 * 
 */
public class CKGridActorFactory extends CKXMLFactory<CKGridItem>
{
	
	public CKGridActorFactory()
	{
		this.setShouldReload(true);
	}

	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.GRID_ACTORS_DIR;
	}

	
	private static CKGridActorFactory factory= null;
	 
	
	 

	public static CKGridActorFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKGridActorFactory();
		}
		return factory;
	}	
	
	
	
	private static void createTestDB()
	{
		
		CKGridActorFactory factory = CKGridActorFactory.getInstance();
		
		factory.makeUsageType("sliders");
		factory.makeUsageType("characters");
		
		
		
	}
	
	
	public static void main(String [] args)
	{
		createTestDB();
	}



	@Override
	public CKGridItem getAssetInstance()
	{
		
		Object[] possibilities = {"Actor", "OverLay"};
		String s = (String)JOptionPane.showInputDialog(
		                    null,
		                    "What kind of class",
		                    "Customized Dialog",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    "Actor");

		/*//If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
		    setLabel("Green eggs and... " + s + "!");
		    return;
		*/
		if(s.compareTo("Actor")==0)
		{
			return new CKGridActor();			
		}
		else //overlay
		{
			return new CKGridActorOverLay();
		}
		

	}
	
	

}
