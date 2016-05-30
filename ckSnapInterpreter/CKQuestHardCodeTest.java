package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.*;

import ckCommonUtils.CKPosition;
import ckDatabase.CKQuestFactory;
import ckEditor.treegui.ActorNode;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKPage;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;


public class CKQuestHardCodeTest 
{
	
	
	
	public CKQuestHardCodeTest()
	{
		String assetName ="HARD_CODE_TEST"; 
    	QuestData questData = createTestQuest();
    	
    	questData.setAID(assetName);
    	CKQuestFactory.getInstance().writeAssetToXMLDirectory(questData);
    	
		new Thread() {
        @Override
        public void run() {
        	
            javafx.application.Application.launch(CKQuestRunner.class,assetName);
        }
    }.start();
	}


    
    

	public void populateModel(QuestData q) {	


		
		//add to quest
		ActorNode babyActor =	new ActorNode("ArtifactTestBaby",Direction.NORTHWEST, 
				new CKPosition(9,0,0,0),new CKTriggerList());
		babyActor.setControllerID("NULL");
		ActorNode momActor =	new ActorNode("ArtifactTestMom",Direction.NORTHEAST, 
				new CKPosition(5,8,0,0),new CKTriggerList());
		momActor.setControllerID("SNAP");
		ActorNode dadActor =	new ActorNode("ArtifactTestDad",Direction.NORTHEAST, 
				new CKPosition(5,9,0,0),new CKTriggerList());
		dadActor.setControllerID("SNAP");
		
	    
	    
		q.addActor(babyActor,"ArtifactTest");
		q.addActor(momActor,"ArtifactTest");
		q.addActor(dadActor,"ArtifactTest");
		
		q.setTeam("ArtifactTest");
	
		
		//make a team:)
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 10);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		teamplay.addChapter(new CKChapter("Fire",10,"bolt"));

		teamplay.addChapter(new CKChapter(MAX_CP,1000));
		teamplay.addChapter(new CKChapter(RECHARGE_CP,10));
		
		teamplay.addChapter(new CKChapter(CH_EVADE,0));
		teamplay.addChapter(new CKChapter(CH_ACCURACY,0));
		teamplay.addChapter(new CKChapter(CH_DEFENSE,0));
		teamplay.addChapter(new CKChapter(CH_DEFENSE_EFFECTIVENESS,70));
		teamplay.addChapter(new CKChapter(MAX_DAMAGE,40));
		teamplay.addChapter(new CKChapter(CH_ATTACK_BONUS,0));
		teamplay.addChapter(new CKChapter("Aim",10,"target"));
		teamplay.addChapter(new CKChapter("Aim",10,P_FRONT));
		teamplay.addChapter(new CKChapter("Aim",0,P_SHORT_TARGET));
		teamplay.addChapter(new CKChapter("Water",10,"rain"));
		//teamplay.addChapter(MAX-);
		
		CKTeam team = q.getActorsFromTeam("ArtifactTest").get(0).getTeam();                    //new CKTeam("heroes");
		team.addToAbilties(teamplay);
	
		
		team.getArtifacts("Dad").get(0).setLimits(null);
		team.getArtifacts("Mom").get(0).setLimits(null);
		
    
    }
    

	
    public QuestData createTestQuest()
	{
		//CKGrid grid = new CKGrid(10,10);
		QuestData q = new QuestData(5);
		q.setSceneID("Kitchen");
				
		Satisfies winSatisfies1 = new PositionReachedSatisfies("Baby", new CKPosition(0, 0));
		Satisfies winSatisfies2 = new PositionReachedSatisfies("Mom", new CKPosition(0,0));
		Satisfies winSatisfies3 = new PositionReachedSatisfies("Dad", new CKPosition(0, 0));
		
		//start action
/*		CKSequentialAction start = new CKSequentialAction();
		start.add()
	*/
		q.addTrigger(new CKTrigger(new TrueSatisfies(), 
				new CKSimpleGUIAction("Dad","Lets race to get to the fridge"),
				TriggerResult.INIT_ONLY));
		
		//Win actions
		q.addTrigger(new CKTrigger(winSatisfies1,
				new CKSimpleGUIAction("Baby","GAGA!!"),TriggerResult.SATISFIED_END_QUEST));	
		q.addTrigger(new CKTrigger(winSatisfies2,
				new CKSimpleGUIAction("Mom","Looks like I won.  Who wants brownies?"),TriggerResult.SATISFIED_END_QUEST));	
		q.addTrigger(new CKTrigger(winSatisfies3,
				new CKSimpleGUIAction("Dad","SPOOON!!"),TriggerResult.SATISFIED_END_QUEST));	
	/*
		q.addTrigger(new CKTrigger(new TrueSatisfies(),
				new CKSimpleGUIAction("Dad","SPOOON!!"),TriggerResult.SATISFIED_END_QUEST));	
	*/
		
		
	populateModel(q);
	return q;
	}

    
    
  
 
    public static void main(String[] args) {
    	
    	new CKQuestHardCodeTest();
    }
}


