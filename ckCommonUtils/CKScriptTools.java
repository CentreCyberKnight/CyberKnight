package ckCommonUtils;

import java.util.Scanner;
import java.util.Vector;
import java.util.regex.MatchResult;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ckDatabase.CKQuestFactory;
import ckEditor.CKXMLAssetEditor;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKAssetFalseCmd;
import ckGameEngine.actions.StoreActorStateCmd;
import ckPythonInterpreterTest.CKArtifactQuestRunner;
import ckSatisfies.ActorCPSatisfies;
import ckSatisfies.ActorDirectionSatisfies;
import ckSatisfies.AndSatisfies;
import ckSatisfies.NotSatisfies;
import ckSatisfies.NumericalCostType;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.SpellSatisfies;
import ckSatisfies.TurnsTakenSatisfies;
import ckTrigger.CKTrigger;

public class CKScriptTools
{

	public static Vector<String> pullOutActions(String commentedScript)
	{
		Vector<String> snippets = new Vector<String>();
		
		int lastIndex = 0; 
		int nextIndex = commentedScript.indexOf("---", lastIndex);
		while(nextIndex!=-1)
		{
			int len = nextIndex-lastIndex;
			if(len>0)
			{
				snippets.add(commentedScript.substring(lastIndex, nextIndex));
			}
			//now to reposition the lastIndex to end of line
			lastIndex = commentedScript.indexOf('\n',nextIndex);
			if(lastIndex == -1) { break;} //file ends without newline.
			lastIndex++; //move it past the newline.
			nextIndex = commentedScript.indexOf("---", lastIndex);		
		}
		//might be a last snippet here
		
		if(lastIndex!=-1 && lastIndex < commentedScript.length()-1)
		{
			String last = commentedScript.substring(lastIndex);
			//just auto add a \n in case they forgot
			snippets.add(last+'\n');
		}
		
		return snippets;
		
	}
	

	public static AndSatisfies createActorSatisfies(String actor, int t, boolean beforeTurn)
	{
		String when;
		if(beforeTurn) { when=CKPropertyStrings.P_START_TURN; }
		else					{ when=CKPropertyStrings.P_END_TURN; }
		
		Satisfies before = new SpellSatisfies(CKPropertyStrings.CH_WORLD,
				when , 0,NumericalCostType.TRUE);		
		
		Satisfies time = new TurnsTakenSatisfies(actor,t);
		return new AndSatisfies(time,before);
	}
	
	public static void recordActorStats(QuestData quest, String actor,LogListener l, boolean beforeTurn)
	{
		String when;
		if(beforeTurn) { when=CKPropertyStrings.P_START_TURN; }
		else					{ when=CKPropertyStrings.P_END_TURN; }
		
		Satisfies before = new SpellSatisfies(CKPropertyStrings.CH_WORLD,
				when , 0,NumericalCostType.TRUE);		
		
		StoreActorStateCmd store = new StoreActorStateCmd(l,true, beforeTurn, 10);
		CKTrigger trigger = new CKTrigger(before,store);
		trigger.setName("Store Actor Data ");
		//quest.addTrigger(trigger);
		
		quest.getActor(actor).addTrigger(trigger);
		
		
	}
	
	public static String timeSpaceMessage(int t,boolean beforeTurn)
	{
		if(beforeTurn) { return " before  time "+t; }
		else 				{ return  " after  time "+t; }
		
	}
	
	public static boolean isBefore(String b) { return b.compareToIgnoreCase("BEFORE")==0; }

	public static void assertActorAt(QuestData quest, String actor,int x,int y,int z,int t,boolean beforeTurn)
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
	

	public static void assertActorFaces(QuestData quest, String actor,Direction direction,int t,boolean beforeTurn)
	{
		Satisfies and = createActorSatisfies(actor,t,beforeTurn);
		
		
		Satisfies pos = new ActorDirectionSatisfies(actor,direction);
				
		Satisfies notPos = new NotSatisfies(pos);
		and.add(notPos);
		
		String mess = "Actor is not facing "+direction+timeSpaceMessage(t,beforeTurn);
		
		CKAssetFalseCmd bad = new CKAssetFalseCmd(mess);
		CKTrigger trigger = new CKTrigger(and,bad);
		trigger.setName(mess);
		
		quest.getActor(actor).addTrigger(trigger);
		
		
	}
	
	public static void assertActorHasCP(QuestData quest, String actor,NumericalCostType op, 
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

	
	public static void generateDirectionTest(QuestData quest, String cmd)
	{
		
		// [HERO FACES DIRECTION before t2]
		Scanner s = new Scanner(cmd);
		s.findInLine("(\\w+) (\\w+) (\\w+) (\\w+) t(\\d+)");
		MatchResult result=s.match();
		s.close();

		
		
/*		for (int i=1; i<=result.groupCount(); i++)
	         System.out.println(""+i+":"+result.group(i));
	*/
		
		
		assertActorFaces(	quest,
				result.group(1),
				Direction.getDirection(result.group(3)),	
				Integer.parseInt(result.group(5)),
				isBefore(result.group(4)));	

		
		
	}
	
	public static void generateCPTest(QuestData quest,String cmd)
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
		
		
		assertActorHasCP(	quest,
				result.group(1),
				NumericalCostType.getTypes(result.group(3)),	
				Integer.parseInt(result.group(4)),	
				Integer.parseInt(result.group(6)),
				isBefore(result.group(5)));	

	}


	
	
	public static void generateATTest(QuestData quest, String cmd)
	{
//		System.out.println("CMD:"+cmd);
		Scanner s = new Scanner(cmd);
		s.findInLine("(\\w+) (\\w+) (\\d+),(\\d+),(\\d+) (\\w+) t(\\d+)");
		MatchResult result=s.match();
		s.close();

/*		for (int i=1; i<=result.groupCount(); i++)
	         System.out.println(""+i+":"+result.group(i));
	*/
		assertActorAt(quest,
				result.group(1),
				Integer.parseInt(result.group(3)),	
				Integer.parseInt(result.group(4)),	
				Integer.parseInt(result.group(5)),	
				Integer.parseInt(result.group(7)),
				isBefore(result.group(6)));	
	}

	
	
	public static void generateTest(QuestData quest, String cmd)
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
				generateATTest(quest,cmd);
				break;
			case "CP":
				generateCPTest(quest,cmd);
				break;
			case "FACES":
				generateDirectionTest(quest,cmd);
				break;

			default:
				System.err.println("generate Test: Unknown command "+ val);
				
		}
		

		
	}

	public static void generateActorTest(QuestData quest, String text)
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
				generateTest(quest,text.substring(startCmd+1,endCmd));
			}
			
			startLine = text.indexOf("---", endLine+1);			
		}
	
	}
	
	
	
	
	
	/****invoking methods****/
	
	public static void runQuestInThread(QuestData quest) 
	{
		CKGameObjectsFacade.unitTest=false;
		new CKArtifactQuestRunner(new Quest(quest),false);
	}

	public static void runQuest(QuestData quest) 
	{
		CKGameObjectsFacade.unitTest=false;
		new CKArtifactQuestRunner(new Quest(quest));
	}

	public static void checkQuest(QuestData quest)
	{
		CKGameObjectsFacade.unitTest=true;
		Quest q = new Quest(quest);
		//no need to hold back on the framerate-there is no visual.
		CKGameObjectsFacade.getEngine().setFps(30000);
		CKGameObjectsFacade.getEngine().startGame();
		q.gameLoop();
		
	}
	
	public static void seeQuest (QuestData quest)
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
	
	
	
	
}
