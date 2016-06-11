package ckGameEngine;

import static ckCommonUtils.CKPropertyStrings.CH_ACCURACY;
import static ckCommonUtils.CKPropertyStrings.CH_ATTACK_BONUS;
import static ckCommonUtils.CKPropertyStrings.CH_DEFENSE;
import static ckCommonUtils.CKPropertyStrings.CH_DEFENSE_EFFECTIVENESS;
import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.CH_EVADE;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.CP_PER_ROUND;
import static ckCommonUtils.CKPropertyStrings.MAX_CP;
import static ckCommonUtils.CKPropertyStrings.MAX_DAMAGE;
import static ckCommonUtils.CKPropertyStrings.P_BOLT;
import static ckCommonUtils.CKPropertyStrings.P_END_TURN;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import static ckCommonUtils.CKPropertyStrings.P_SLASH;
import static ckCommonUtils.CKPropertyStrings.P_TALK_CLICK;
import static ckCommonUtils.CKPropertyStrings.RECHARGE_CP;
import static ckCommonUtils.CKPropertyStrings.SPEED;
import static ckGameEngine.CKGameObjectsFacade.getQuest;

import java.util.Vector;
import java.util.stream.Collectors;

import ckCommonUtils.BasicCommand;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKQuestFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.treegui.ActorNode;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.DescisionGrid.CharacterActionReport;
import ckGameEngine.DescisionGrid.DecisionNode;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckPythonInterpreter.CKEditorPCController;
import ckPythonInterpreter.CKPlayerObjectsFacade;
import ckSatisfies.DeadSatisfies;
import ckSatisfies.Satisfies;
import ckSnapInterpreter.CKArtifactQuestRunner;
import ckSnapInterpreter.CKQuestRunner;
import ckSnapInterpreter.CKSpellObject;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;

public class ActorAIController extends ActorController
{
	Vector<CharacterActionDescription> actions = new Vector<>();

	public ActorAIController()
	{
		this(null);
	}
	
	public ActorAIController(CKGridActor pc)
	{
		super(pc);
	}

	public void add(CharacterActionDescription cad)
	{
		actions.add(cad);	
	}
	

	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#takeMyTurn()
	 */
	@Override
	protected void takeMyTurn()
	{
		CKGameObjectsFacade.setCurrentPlayer(getActor());
		CKPlayerObjectsFacade.calcCPTurnLimit();

		Quest w = getQuest();

		System.out.println("thinking....");
		//calculate movements
		CKBook book = getActor().getAbilities();
		GridNode [][][][] movement= 
				w.getGrid().allPositionsReachable(getActor(),
						book.getChapter(CH_MOVE).getValue() , 1);
		System.out.println("reachable....");
		//calculate possible actions
		DescisionGrid dgrid = new DescisionGrid(w.getGrid());
		
		dgrid.updateGrid(w.getActors().stream()
				.map(CKGridActor::getPos)
				.collect(Collectors.toList()), actions);
		
		dgrid.generateNodeValues(movement,
				book.getChapter(CP_PER_ROUND).getValue());
		
		//need to find maximal thing to do...
		
		System.out.println("doing....");
		//calculate actions to take here....
		DecisionNode decision = dgrid.getHighestUtilityNode();
		GridNode destination = movement[(int) decision.position.getX()]
				[(int) decision.position.getY()]
				[decision.direction.ordinal()][0];
		
		w.startTransaction();
		
		
		//FIXME we are only evaluating move then act.
		
		//move first
		System.out.println("Moving to "+(int) decision.position.getX()+" "+
				(int) decision.position.getY());
		
		
		CKEditorPCController.moveTo(destination);
		//now act!
		CharacterActionReport car = decision.cmap.getOrDefault(0, null);
		//DecisionNode origin = car.origin;
		System.out.println("decision"+car.toString());
		car.doAction(car.evaluateCPConsumed(decision.cpAvailible, decision.hasMoved));
		
		
		

/*			String code = "from ckPythonInterpreter.CKEditorPCController import * \n\n"+
							getActor().getTeam().getFunctions()+"\n"+
							iterator.next();

			getConsole().runNewCode(code);

			getConsole().waitForCompletion();
*/
		//Take action here
		
		
		w.endTransaction();

		CKGameObjectsFacade.setCurrentPlayer(null);
		//main thread will run through here.
	}

	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.PCTurnController#getInitialTurnEvent()
	 */
	@Override
	public Event getInitialTurnEvent(double start)
	{
		
		
		return new ActorEvent(getActor(),getActor().calcTimeToNextTurn(start));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String ret ="AIController for ";
		if(getActor()==null)
		{ret = ret+ "Null";}
		else
		{
			ret = ret+ getActor().getName();
		}
		return ret;
	}

	public static void getCmds(GridNode node)//,CKPosition destination)
	{
		//need to do the last thing first!
		GridNode parent = node.getParentNode(); 
		if(parent!=null) { getCmds(parent); }
		//now do my action
		String action = node.getAction();
		//int cp = node.getActionCost();
		

		//System.err.println("MoveTO "+action+" "+cp+":"+node.getPos()+node.getDir());
		if(action.equals("") || action.equals(P_END_TURN))
		{
			return; //we are done
		}
		//		CKSpellCast cast = new CKSpellCast(getCharacter(), getCharacter(),
	//			CH_MOVE, action, cp, "");
		//cast.castSpell();
	}
	
	
	
	
	//testing actions
	
	public static class SwingCommand implements BasicCommand
	{

		@Override
		public void doAction(CKPosition target, int cp)
		{
			CKEditorPCController.earth(CKPropertyStrings.P_SLASH, cp, target);
			CKEditorPCController.voice(P_TALK_CLICK, 0, target, "Your slash has done me in!");

			/*CKSpellObject spell = new CKSpellObject("ta");
			

			spell.spell(CH_EARTH, CKPropertyStrings.P_SLASH, cp, target,"");
			spell.spell(CH_VOICE,P_TALK_CLICK, 0, target, "Your slash has done me in!");
*/
		}		
	}
	
	public static class FireCommand implements BasicCommand
	{

		@Override
		public void doAction(CKPosition target, int cp)
		{
			CKEditorPCController.fire(CKPropertyStrings.P_BOLT, cp, target);
			CKEditorPCController.voice(P_TALK_CLICK, 0, target, "I was hit by fire!!");
	/*		CKSpellObject spell = new CKSpellObject("ta");
			
			spell.spell(CKPropertyStrings.CH_FIRE,
					CKPropertyStrings.P_BOLT, cp, target,"");
			spell.spell(CKPropertyStrings.CH_VOICE,P_TALK_CLICK, 0, target, "I was hit by fire!!");
			*/
		}		
	}
	
	
	
	public static void main(String [] args)
	{
		
		
		
		
		
		//make a quest...
		QuestData q = new QuestData(5);
		q.setAID("KITCHEN_AI_TEST");
		q.setSceneID("Kitchen");
				

		Satisfies winSatisfies2 = new DeadSatisfies("Dad");
		
		
		//Win actions
		q.addTrigger(new CKTrigger(winSatisfies2,
				new CKSimpleGUIAction("Baby","GAGA!!"),TriggerResult.SATISFIED_END_QUEST));	
		
	//addActorData(q); -- finish adding this together so I can watch the game :)
	

		
		
		
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHWEST);
		//baby.setPos(new CKPosition(5,4));

		//need charaxcter abilities to store 
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 5);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		teamplay.addChapter(new CKChapter(MAX_CP,60));
		teamplay.addChapter(new CKChapter(SPEED,5));
		teamplay.addChapter(new CKChapter(CP_PER_ROUND,20));
		teamplay.addChapter(new CKChapter(RECHARGE_CP,5));
		CKChapter fire = new CKChapter(CH_FIRE,20);
		fire.addPage(P_BOLT);
		CKChapter earth = new CKChapter(CH_EARTH,20);
		earth.addPage(P_SLASH);
		teamplay.addChapter(fire);
		teamplay.addChapter(earth);
		
		teamplay.addChapter(new CKChapter(CH_DEFENSE,0));
		teamplay.addChapter(new CKChapter(CH_EVADE,0));
		teamplay.addChapter(new CKChapter(CH_ATTACK_BONUS,0));
		teamplay.addChapter(new CKChapter(MAX_DAMAGE,40));
		teamplay.addChapter(new CKChapter(CH_DEFENSE_EFFECTIVENESS,70));
		teamplay.addChapter(new CKChapter(CH_ACCURACY,0));
		teamplay.addChapter(new CKChapter(CH_VOICE,0,P_TALK_CLICK));
		
		
		System.out.println(teamplay.treeString());
		CKTeam AI = new CKTeam("AI");
		AI.addToAbilties(teamplay);
		AI.addCharacter(baby);
		baby.setTeam(AI);
		AI.setName("AITEST");
		AI.setAID("AITEST");
		CKTeamFactory.getInstance().writeAssetToXMLDirectory(AI);
		

		CKGridActor dad = new CKGridActor("heroSprite",Direction.NORTHEAST);
		dad.setName("Dad");
		

		CKTeam dummies = new CKTeam("Dummy");
		dummies.addToAbilties(teamplay);
		dummies.addCharacter(dad);
		dad.setTeam(dummies);	
		dummies.setName("AITEST_DUMMY");
		dummies.setAID("AITEST_DUMMY");
		
		
		
		
		
		
		CKTeamFactory.getInstance().writeAssetToXMLDirectory(dummies);		
		
		dad.setAID("AITEST_DAD");
		baby.setAID("AITEST_BABY");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(baby);
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(dad);
		
		
		//add to quest
		ActorNode babyActor =	new ActorNode("AITEST_BABY",Direction.NORTHWEST, 
						new CKPosition(5,4,0,0),new CKTriggerList());
		babyActor.setControllerID("AITEST");
		ActorNode dadActor =	new ActorNode("AITEST_DAD",Direction.NORTHEAST, 
						new CKPosition(5,9,0,0),new CKTriggerList());
		dadActor.setControllerID("NULL");

		q.addActor(babyActor, "AITEST");
		q.addActor(dadActor,"AITEST_DUMMY");
		
		//Quest Q = new Quest(q);
		
		//new CKArtifactQuestRunner(Q);
		
		
		CKQuestFactory.getInstance().writeAssetToXMLDirectory(q);
    	String assetName = q.getAID();
    	
    	
		new Thread() {
	        @Override
	        public void run() {
	        	
	            javafx.application.Application.launch(CKQuestRunner.class,assetName);
	        }
	    }.start();
		
		
		
		
		
		
	}
	
}
