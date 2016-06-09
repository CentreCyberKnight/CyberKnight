package ckDatabase;
import ckGameEngine.CampaignNode;

/**A CKTriggerFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKCampaignNodeFactory extends  CKXMLFactory<CampaignNode>
	{

		@Override
		public String getBaseDir()
		{
		
			return XMLDirectories.CAMPAIGN_NODE_DIR;
		}

		
		private static CKCampaignNodeFactory factory= null;
		 
		
		 

		public static CKCampaignNodeFactory getInstance()
		{
			if(factory==null)
			{
				factory = new CKCampaignNodeFactory();
			}
			return factory;
		}	
		
		
		
	


		@Override
		public CampaignNode getAssetInstance()
		{
			return new CampaignNode();
		}
		
			
}
