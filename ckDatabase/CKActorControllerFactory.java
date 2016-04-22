package ckDatabase;

import ckCommonUtils.AIBasicCommand;
import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.ActorAIController;
import ckGameEngine.ActorArtifactController;
import ckGameEngine.ActorController;
import ckGameEngine.ActorNullController;
import ckGameEngine.ActorSnapController;
import ckGameEngine.ActorTurnController;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.OngoingEffectController;


public class CKActorControllerFactory extends CKXMLFactory<ActorController>
{
	
	public CKActorControllerFactory()
	{
		this.setShouldReload(true);
	}

	@Override
	public String getBaseDir()
	{
	
		return XMLDirectories.ACTOR_CONTROLLERS_DIR;
	}

	
	private static CKActorControllerFactory factory= null;
	 
	
	 

	public static CKActorControllerFactory getInstance()
	{
		if(factory==null)
		{
			factory = new CKActorControllerFactory();
		}
		return factory;
	}	
	
	
	
	private static void createTestDB()
	{
		
		CKActorControllerFactory factory = CKActorControllerFactory.getInstance();
		
		ActorArtifactController art = new ActorArtifactController();
		art.setPermissions(ActorController.TEXT_CONTROL);
		art.setName("Text Controller");
		art.setAID("TEXT");
		factory.writeAssetToXMLDirectory(art);
		
		
				
		art = new ActorArtifactController();
		art.setPermissions(ActorController.ARTIFACT_CONTROL);
		art.setName("Artifact Controller");
		art.setAID("ARTIFACT");
		factory.writeAssetToXMLDirectory(art);
		
		art = new ActorArtifactController();
		art.setPermissions(ActorController.BOTH_CONTROL);
		art.setName("Text and Artifact Controller");
		art.setAID("BOTH");
		factory.writeAssetToXMLDirectory(art);
		
		ActorSnapController sn = new ActorSnapController();
		sn.setPermissions(ActorController.SNAP_CONTROL);
		sn.setName("Snap Controller");
		sn.setAID("SNAP");
		factory.writeAssetToXMLDirectory(sn);
		
		OngoingEffectController ongoing = new OngoingEffectController();
		ongoing.setPermissions(ActorController.NO_CONTROL);
		ongoing.setName("Ongoing Effect Controller");
		ongoing.setAID("EFFECT");
		factory.writeAssetToXMLDirectory(ongoing);
		
		ActorTurnController turn = new ActorTurnController();
		turn.setPermissions(ActorController.NO_CONTROL);
		turn.setName("Turn Controller");
		turn.setAID("TURN");
		factory.writeAssetToXMLDirectory(turn);
		
		
		AITestController ai = new AITestController();
		ai.setPermissions(ActorController.NO_CONTROL);
		ai.setName("AI TEST Controller");
		ai.setAID("AITEST");
		factory.writeAssetToXMLDirectory(ai);
		
		
		
		
		
	}
	
	
	public static class AITestController extends ActorAIController
	{
		
		
		public AITestController()
		{
			super(null);
			
			AIBasicCommand Swing = new AIBasicCommand(new SwingCommand());
			int [] costs = {3,20};
			CharacterActionDescription swing = 
					new CharacterActionDescription("Swing", CKPropertyStrings.P_SWIPE,
					costs, true, true, false, 0,(cad,node)->{return Swing.doPrediction(cad, node);}, 
					Swing);			
			
			AIBasicCommand Far = new AIBasicCommand(new FireCommand());
			int [] fcosts = {5,10};
			CharacterActionDescription far = 
					new CharacterActionDescription("FAR", CKPropertyStrings.P_SHORT_TARGET,	
					fcosts, true, true, false, 0, (cad,node)->{return Far.doPrediction(cad,node);},Far);			
			
			add(swing);
			add(far);		
		}
		
		
		
		
		
		
	}
	
	
	
	
	public static void main(String [] args)
	{
		createTestDB();
	}



	@Override
	public ActorController getAssetInstance()
	{
		
		return new ActorNullController();
		

	}
	
	

}