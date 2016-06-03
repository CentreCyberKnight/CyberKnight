package ckDatabase;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MARK;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.P_ANY;
import static ckCommonUtils.CKPropertyStrings.P_ILLUMINATE;
import static ckCommonUtils.CKPropertyStrings.P_TALK;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ckGameEngine.CampaignNode;
import ckGameEngine.actions.CKDialogAction;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKMarkGridActor;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckGameEngine.actions.CKSpellAction;
import ckSatisfies.NumericalCostType;
import ckSatisfies.Satisfies;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKSharedTrigger;
import ckTrigger.CKTrigger;
import ckTrigger.TriggerResult;

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
