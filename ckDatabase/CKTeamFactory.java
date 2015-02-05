package ckDatabase;
import ckGameEngine.CKTeam;

/**A CKTriggerFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKTeamFactory extends  CKXMLFactory<CKTeam>
	{

		@Override
		public String getBaseDir()
		{//TODO this should actually go into the game save data...
		
			return XMLDirectories.TEAM_DIR;
		}

		
		private static CKTeamFactory factory= null;
		 
		
		 

		public static CKTeamFactory getInstance()
		{
			if(factory==null)
			{
				factory = new CKTeamFactory();
			}
			return factory;
		}	
		
		
		
		private static void createTestDB()
		{
		
			CKTeamFactory factory = CKTeamFactory.getInstance();
			
			CKTeam board = new CKTeam("board");
			factory.writeAssetToXMLDirectory(board);
			CKTeam nature = new CKTeam("nature");
			factory.writeAssetToXMLDirectory(nature);
			CKTeam player = new CKTeam("player");
			factory.writeAssetToXMLDirectory(player);

			
			
		}
		
		
		public static void main(String [] args)
		{
			createTestDB();
		}



	


		@Override
		public CKTeam getAssetInstance()
		{
			return new CKTeam();
		}
		
			
}
