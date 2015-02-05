package testing;

import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.CKQuestFactory;
import ckEditor.CKXMLAssetEditor;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKAssetFalseCmd;
import ckPythonInterpreterTest.CKArtifactQuestRunner;
import ckSatisfies.ActorCPSatisfies;
import ckSatisfies.AndSatisfies;
import ckSatisfies.NotSatisfies;
import ckSatisfies.NumericalCostType;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.SpellSatisfies;
import ckSatisfies.TurnsTakenSatisfies;
import ckTrigger.CKTrigger;

public class TestPythonMoveForward
{

	QuestData quest;
	@Before
	public void setUp() throws Exception
	{
		quest = CKQuestFactory.getInstance().getAsset("asset69633757502583801");
		
	}

	public AndSatisfies createActorSatisfies(String actor, int t, boolean beforeTurn)
	{
		String when;
		if(beforeTurn) { when=CKPropertyStrings.P_START_TURN; }
		else					{ when=CKPropertyStrings.P_END_TURN; }
		
		Satisfies before = new SpellSatisfies(CKPropertyStrings.CH_WORLD,
				when , 0,NumericalCostType.TRUE);		
		
		Satisfies time = new TurnsTakenSatisfies(actor,t);
		return new AndSatisfies(time,before);
	}
	
	public String timeSpaceMessage(int t,boolean beforeTurn)
	{
		if(beforeTurn) { return " before  time "+t; }
		else 				{ return  " after  time "+t; }
		
	}
	
	public boolean isBefore(String b) { return b.compareToIgnoreCase("BEFORE")==0; }
	
	public void assertActorAt(String actor,int x,int y,int z,int t,boolean beforeTurn)
	{
		Satisfies and = createActorSatisfies(actor,t,beforeTurn);
		
		CKPosition pt = new CKPosition(x,y,z);
		Satisfies pos = new PositionReachedSatisfies(actor,pt);
				
		Satisfies notPos = new NotSatisfies(pos);
		and.add(notPos);
		
		String mess = "Actor is not at "+pt+timeSpaceMessage(t,beforeTurn);
		
		CKAssetFalseCmd bad = new CKAssetFalseCmd(mess);
		CKTrigger trigger = new CKTrigger(and,bad);
		trigger.setName(mess);
		//quest.addTrigger(trigger);
		
		quest.getActor(actor).addTrigger(trigger);
		
		
	}
	
	public void assertActorHasCP(String actor,NumericalCostType op, 
													int cp,int t,boolean beforeTurn)
	{
		Satisfies cpSat = new ActorCPSatisfies(actor,cp,op);
		Satisfies notCP = new NotSatisfies(cpSat);

		Satisfies and = createActorSatisfies(actor,t,beforeTurn);
		and.add(notCP);
		String message = "Actor cp not "+op+" "+cp+timeSpaceMessage(t,beforeTurn);
		
		CKAssetFalseCmd bad = new CKAssetFalseCmd(message);
		CKTrigger trigger = new CKTrigger(and,bad);
		trigger.setName(message);
		//quest.addTrigger(trigger);
		quest.getActor(actor).addTrigger(trigger);
	}

	
	public void generateCPTest(String cmd)
	{
		// [HERO CP EQUALS 10 before t3]
//		System.out.println("CMD:"+cmd);
		Scanner s = new Scanner(cmd);
		s.findInLine("(\\w+) (\\w+) (\\w+) (\\d+) (\\w+) t(\\d+)");
		MatchResult result=s.match();
		s.close();

		
		
/*		for (int i=1; i<=result.groupCount(); i++)
	         System.out.println(""+i+":"+result.group(i));
	*/
		
		
		assertActorHasCP(	result.group(1),
				NumericalCostType.getTypes(result.group(3)),	
				Integer.parseInt(result.group(4)),	
				Integer.parseInt(result.group(6)),
				isBefore(result.group(5)));	

	}


	
	
	public void generateATTest(String cmd)
	{
//		System.out.println("CMD:"+cmd);
		Scanner s = new Scanner(cmd);
		s.findInLine("(\\w+) (\\w+) (\\d+),(\\d+),(\\d+) (\\w+) t(\\d+)");
		MatchResult result=s.match();
		s.close();

/*		for (int i=1; i<=result.groupCount(); i++)
	         System.out.println(""+i+":"+result.group(i));
	*/
		assertActorAt(	result.group(1),
				Integer.parseInt(result.group(3)),	
				Integer.parseInt(result.group(4)),	
				Integer.parseInt(result.group(5)),	
				Integer.parseInt(result.group(7)),
				isBefore(result.group(6)));	
	}

	
	
	public void generateTest(String cmd)
	{
		System.out.println("CMD:"+cmd);
		Scanner s = new Scanner(cmd);
		s.findInLine("(\\w+) (\\w+) ");
		MatchResult result=s.match();		
		s.close();
		/*for (int i=1; i<=result.groupCount(); i++)
	         System.out.println(""+i+":"+result.group(i));
		*/
		
		String val = result.group(2).toUpperCase();
		switch(val)
		{
			case "AT":
				generateATTest(cmd);
				break;
			case "CP":
				generateCPTest(cmd);
				break;
			default:
				System.err.println("generate Test: Unknown command "+ val);
				
		}
		

		
	}

	public void generateActorTest(String text)
	{
		
		int startLine = text.indexOf("---", 0);
		while(startLine !=-1)
		{
			int endLine = text.indexOf('\n',startLine);
			if(endLine==-1) { endLine = text.length()-1; }
			//check for command
			int startCmd = text.indexOf('[',startLine);
			int endCmd = text.indexOf(']',startLine);
			if(startCmd != -1 && startCmd<endLine &&
					endCmd != -1 && endCmd<=endLine)
			{
				generateTest(text.substring(startCmd+1,endCmd));
			}
			
			startLine = text.indexOf("---", endLine+1);			
		}
	
	}
	
	
	
	
	
	/****invoking methods****/
	
	public void runQuest() 
	{
		CKGameObjectsFacade.unitTest=false;
		new CKArtifactQuestRunner(new Quest(quest),false);
	}
	
	public void checkQuest()
	{
		CKGameObjectsFacade.unitTest=true;
		Quest q = new Quest(quest);
		//no need to hold back on the framerate-there is no visual.
		CKGameObjectsFacade.getEngine().setFps(30000);
		CKGameObjectsFacade.getEngine().startGame();
		q.gameLoop();
		
	}
	
	public void seeQuest ()
	{
		JFrame frame = new JFrame();
		CKXMLAssetEditor<QuestData,CKQuestFactory> view=
				new CKXMLAssetEditor<QuestData,CKQuestFactory>
					(quest,CKQuestFactory.getInstance());
		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Scanner a = new Scanner(System.in);
		String s = a.nextLine();
		System.out.println(s);
		a.close();
	
	}
	
	
	
	@Test
	public void testMoveScript()
	{
		String text = 
				"----[HERO at 0,0,0 before t0]---\n" +
				"----[HERO CP EQUALS 100 before t0]---\n" +
				"----[HERO CP EQUALS 99 after t0]---\n" +
				"move('forward',1)\n" +
				"----[HERO at 0,1,0 before t1]---\n" +
				"----[HERO CP EQUALS 99 before t1]---\n" +
				"move('forward',1)\n" +
				"----[HERO at 0,2,0 before t2]---\n" +
				"----[HERO CP EQUALS 98 before t2]---\n" +
				"move('forward',1)\n" +
				"----[HERO at 0,2,0 before t3]---\n";
		
		String guiText =
				"----test loop 1----\n" +
				"choiceB" +
				"----test loop 2----\n" +
				"choiceB" +
				"----test loop 3----\n" +
				"choiceA"+
				"---completes level---\n" +
				"nice Job"+
				"---end---\n";
		
		CKGameObjectsFacade.getEngine().loadDialogDebugScript(guiText, 60);
		
		
		
		
		quest.getActor("HERO").setScriptController(text);
		generateActorTest(text);
		/*
		assertActorAt("HERO",0,0,0,0);
		assertActorAt("HERO",0,1,0,1);
		assertActorAt("HERO",0,2,0,2);
		assertActorAt("HERO",0,2,0,3);
		*/
		
		
		//seeQuest();
		//runQuest();
		checkQuest();
		
		
		
		
		
	
	
	}
	
	
	

}
