package ckDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

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
		
		
		
		
		public List<CampaignNode> getCampaign(String s)
		{
			//get all nodes
			return CKCampaignNodeFactory.getInstance()
			.getAllAssetsVectored()
			.stream()
			.filter(n->n.getCampaign().compareTo(s)==0)
			.collect(Collectors.toList());
		}
		
			
}
