package ckDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

import static ckCommonUtils.CKPropertyStrings.*;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.P_ILLUMINATE;
import static ckCommonUtils.CKPropertyStrings.P_TALK;

/**A CKTriggerFactory that uses a DB to solve the problems.
 * 
 * 
 * @author Michael K. Bradshaw
 *
 */
public class CKTriggerFactory extends  CKXMLFactory<CKTrigger>
	{

		@Override
		public String getBaseDir()
		{
		
			return XMLDirectories.TRIGGERS_DIR;
		}

		
		private static CKTriggerFactory factory= null;
		 
		
		 

		public static CKTriggerFactory getInstance()
		{
			if(factory==null)
			{
				factory = new CKTriggerFactory();
			}
			return factory;
		}	
		
		
		
		private static void createTestDB()
		{
		
			CKTriggerFactory factory = CKTriggerFactory.getInstance();
			
			CKTrigger tNull = new CKTrigger();
			tNull.setAID("NULL");
			factory.writeAssetToXMLDirectory(tNull);
			
			
			Satisfies s = new SpellSatisfies( CH_VOICE  ,P_TALK , 1,NumericalCostType.TRUE);
			CKGameAction a =null;
			try
			{
								
				String output = new Scanner(new File("DialogTest.xml")).useDelimiter("\\Z").next();
				System.out.println("here is my XML"+output);
				a= new CKDialogAction(output);
				
				
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
				a = new CKSimpleGUIAction( "Mom","Didn't Work!!" );
			}

			
			
			CKTrigger trig = new CKTrigger(s,a,TriggerResult.SATISFIED_END_LOOP);
			trig.setAID("talks_to_Mom");
			factory.writeAssetToXMLDirectory(trig);
			//triggers.add(trig);
			

			//mark
			Satisfies ms2 = new SpellSatisfies( CH_MARK ,P_ANY, 0,NumericalCostType.TRUE);
			CKGameAction ma2 =new CKMarkGridActor();
			trig = new CKTrigger(ms2,ma2);
			trig.setAID("MARK");
			factory.writeAssetToXMLDirectory(trig);

			
			
			//illuminate
			Satisfies s2 = new SpellSatisfies( CH_FIRE  ,P_ILLUMINATE , 1,NumericalCostType.TRUE);
			CKGameAction a2 =new CKSpellAction();
			trig = new CKTrigger(s2,a2);
			trig.setAID("Illuminate");
			factory.writeAssetToXMLDirectory(trig);

			
			
		}
		
		
		public static void main(String [] args)
		{
			createTestDB();
		}



		public CKSharedTrigger getSharedAsset(String string)
		{
			return new CKSharedTrigger(string);
		}



		@Override
		public CKTrigger getAssetInstance()
		{
			return new CKTrigger();
		}
		
			
}
