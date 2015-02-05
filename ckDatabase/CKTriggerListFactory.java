package ckDatabase;
import ckTrigger.CKTriggerList;

/**A CKTriggerFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKTriggerListFactory extends  CKXMLFactory<CKTriggerList>
	{

		@Override
		public String getBaseDir()
		{
		
			return XMLDirectories.TRIGGERLIST_DIR;
		}

		
		private static CKTriggerListFactory factory= null;
		 
		
		 

		public static CKTriggerListFactory getInstance()
		{
			if(factory==null)
			{
				factory = new CKTriggerListFactory();
			}
			return factory;
		}	
		
		
		
		private static void createTestDB()
		{
			
			CKTriggerList list = new CKTriggerList();
			CKTriggerFactory fact = CKTriggerFactory.getInstance();
			list.add(fact.getSharedAsset("Illuminate"));
			list.setAID("globalDefault");
			CKTriggerListFactory.getInstance().writeAssetToXMLDirectory(list);
			
			
			CKTriggerList listNull = new CKTriggerList();
			listNull.setAID("NULL");
			CKTriggerListFactory.getInstance().writeAssetToXMLDirectory(listNull);
			
			CKTriggerList baseline = new CKTriggerList();
			baseline.add(fact.getSharedAsset("MARK"));
			
			baseline.setAID("baseline");
			CKTriggerListFactory.getInstance().writeAssetToXMLDirectory(baseline);
			
			
		}
		
		
		public static void main(String [] args)
		{
			
		}



		@Override
		public CKTriggerList getAssetInstance()
		{
			return new CKTriggerList();
		}
		
			
}
