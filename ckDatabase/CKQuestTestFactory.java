package ckDatabase;
import ckEditor.treegui.CKQuestTest;

/**A CKTriggerFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKQuestTestFactory extends  CKXMLFactory<CKQuestTest>
	{
	private CKQuestTestFactory()
	{
		setShouldReload(true);
	}
		@Override
		public String getBaseDir()
		{
		
			return XMLDirectories.QUEST_TEST_DIR;
		}

		
		private static CKQuestTestFactory factory= null;
		 
		
		 

		public static CKQuestTestFactory getInstance()
		{
			if(factory==null)
			{
				factory = new CKQuestTestFactory();
			}
			return factory;
		}	
		
		
		
				
		public static void main(String [] args)
		{
			
		}



		


		@Override
		public CKQuestTest getAssetInstance()
		{
			return new CKQuestTest();
		}
		
			
}
