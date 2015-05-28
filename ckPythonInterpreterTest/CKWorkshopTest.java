package ckPythonInterpreterTest;

import static ckCommonUtils.CKPropertyStrings.*;

import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.P_ARMOR;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_IGNITE;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_OFFHAND_WEAPON;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;
import static ckCommonUtils.CKPropertyStrings.P_SING;
import static ckCommonUtils.CKPropertyStrings.P_SLASH;
import static ckCommonUtils.CKPropertyStrings.P_SWORD;
import static ckCommonUtils.CKPropertyStrings.P_TALK;

import java.awt.BorderLayout;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKThreadCompletedListener;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.treegui.ActorNode;
import ckEditor.treegui.BookList;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKTeamView;
import ckSatisfies.NumericalCostType;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKSharedTrigger;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.CKTriggerNode;
import ckTrigger.TriggerResult;

public class CKWorkshopTest 
{ 
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JPanel leftPanel;
	CKTeamView artifactController=null;
	JButton runButton;
	JTabbedPane tabpane;
	JFrame frame;
	Quest quest;
	
	public static String TEXT_EDITOR="text editor";
	public static String ARTIFACT_CONTROLLER="Artifact Controller";

	/*public static void TEXT_EDITOR="text editor";
	public static void TEXT_EDITOR="text editor";
	*/
	
	 public CKWorkshopTest(Quest q) 
	 {
		 quest = q;
		 frame = new JFrame("CyberKnight Level 1");//CKSytaxPaneTest.class.getName());
		 final Container c = frame.getContentPane();
		 c.setLayout(new BorderLayout());
		 
		 
		 JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		 split.setLeftComponent(createTabPane());
		 
		 
		 
		 leftPanel = new JPanel();
		 leftPanel.setLayout(new CardLayout());
		 leftPanel.add(createEditorPane(),TEXT_EDITOR);
		 artifactController =createArtifactControllerPane(); 
		 leftPanel.add(artifactController,ARTIFACT_CONTROLLER);
		 CardLayout cl = (CardLayout)(leftPanel.getLayout());
		 cl.show(leftPanel, ARTIFACT_CONTROLLER);
		
		
		 
		 split.setRightComponent(leftPanel);
		 c.add(split,BorderLayout.CENTER);
		 c.doLayout();    	

		 frame.setSize(1000, 600);
		 frame.setVisible(true);
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		 String cmd = "move('left',1)\n";				 
		 
		 editor.setText(cmd);
		 Thread T = new gameThread();
		 T.start();

	    }

	 public void switchToCharacterArtifacts(String name)
	 {
		
		 CardLayout cl = (CardLayout)(leftPanel.getLayout());
		 cl.show(leftPanel, ARTIFACT_CONTROLLER);
		 artifactController.gotoTab(name);
	 }
	 
	 public void enableArtifactController(String name,boolean enable)
	 {
		 artifactController.enableCharacter(name,enable);
	 }
	 
	 
	 private CKTeamView createArtifactControllerPane()
	{
		 CKTeamView art = new CKTeamView(quest.getActorsFromTeam(quest.getTeam()));  
	
		 CKGameObjectsFacade.setArtifactController(art);
		 return art;
	}


	class gameThread extends Thread
	 {
		 public void run()
		 {
			 quest.gameLoop();
		 }
 }
	 
	 
	
	 private JPanel getScenePanel(int sceneId)
	 {
		 //DOn't need to call since quest is already initialized
		 //return new CKSceneViewer(scene,sceneId);
		 //quest = CKGameObjectsFacade.getQuest();
		 //quest.creation(sceneId, frame);
		 CKGameObjectsFacade.setQuest(quest);
		 return CKGameObjectsFacade.getEngine();
	 }

	 
	
	 
	 private JTabbedPane createTabPane() 
	 {
		 JTabbedPane tab = new JTabbedPane();
		 tab.setPreferredSize(new Dimension(600,600));
		 
		 tab.addTab("Console", createConsolePane());
		 tab.setMnemonicAt(0, KeyEvent.VK_F1);
		 
		 tab.addTab("game",getScenePanel(3));		 
		 tab.setMnemonicAt(1, KeyEvent.VK_F2);
		 
		 tab.addTab("documentation",createURLPage("http://docs.python.org/"));
		 tab.setMnemonicAt(2, KeyEvent.VK_F3);

		/* tab.addTab("tree",new CKTreeGui(quest));		 
		 tab.setMnemonicAt(3, KeyEvent.VK_F4);
*/
		 
		 
		 
		 
		 tab.setSelectedIndex(1);
		 
		 return tab;
	}

	 
	 
	 private JComponent wrapInScrolls(JComponent j)
	 {
		 JScrollPane scroll = new JScrollPane(j);
		 scroll.setVerticalScrollBarPolicy(
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	     return scroll;
		 
	 }
	 
	 private JComponent createURLPage(String url)
	 {
		 
		 JEditorPane editorPane = new JEditorPane();
		 editorPane.setEditable(false);
		 try {
			 java.net.URL data = new URL(url);
			 editorPane.setPage(data);
		     } catch (IOException e) {
		         System.err.println("Attempted to read a bad URL: " + url);
		     }
		     
		 return wrapInScrolls(editorPane);
		 
	 }
	 
	private JComponent createConsolePane()
	 {
		 
			console = new CKPythonConsoleExtended();
			console.setPreferredSize(new Dimension(400,600));
			CKGameObjectsFacade.setConsole(console);
	        return wrapInScrolls(console);
	        
	 }
	 
	 
	 
	 
	 private JPanel createEditorPane()
	 {
		 JPanel pane=new JPanel();
		 pane.setLayout(new BorderLayout());
		 pane.setPreferredSize(new Dimension(300,600));

		 //create unique editor
		 editor = CKGameObjectsFacade.getUniqueEditor();
		
		 pane.add(editor.getScrollablePane());
	           		    
		 //TOOL BARS
		 //toolbar is part of the editor kit--awesome!
		 JToolBar jToolBar1 = new javax.swing.JToolBar();
		 jToolBar1.setRollover(true);
		 jToolBar1.setFocusable(false);
		 	
		 EditorKit kit = editor.getEditorKit();
		 if (kit instanceof DefaultSyntaxKit) 
		 {
			 DefaultSyntaxKit defaultSyntaxKit = (DefaultSyntaxKit) kit;
			 defaultSyntaxKit.addToolBarActions(editor, jToolBar1);
		 }	
		 jToolBar1.validate();
		 pane.add(jToolBar1, BorderLayout.PAGE_START);
		 
	
	    runButton = new JButton("Run Code");
	    /*CKGameObjectsFacade.setRunButton(runButton);
	    
	    CKGameObjectsFacade.disableTextInput();	
	    */
	    ButtonHandler handler = new ButtonHandler();
	    runButton.addActionListener(handler);
	    pane.add(runButton,BorderLayout.PAGE_END);
	        
		 
		 
	    return pane;
	       
		 
	 }
	 
	 
	 
	 
	 	private class ButtonHandler implements ActionListener
	 	{
	 		public void actionPerformed(ActionEvent event)
	 		{
	 			
	 			//get code from editor
	 			String code =editor.getText();
	 			
	 			console.runNewCode("from ckPythonInterpreter.CKEditorPCController import * \n"+code,new consoleThreadFinishes());
	 			
	 			
	 			
	 		}
	 		
	 	}
	 	
	 	private class consoleThreadFinishes implements CKThreadCompletedListener
	 	{

			@Override
			public  void threadFinishes(boolean error)
			{
				CKGameObjectsFacade.disableTextInput();
//	 			CKGameObjectsFacade.getQuest().notifyOfInput();
				
			}
	 		
	 	}
	 	
	
	 	
	public static void addActorData(QuestData q)
	{
		//make a team:)
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 1);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		
		CKChapter chap2 = new CKChapter(CH_VOICE, 4);
		chap2.addPage(new CKPage(P_TALK));
		chap2.addPage(new CKPage(P_HACK));
		teamplay.add(chap2);

		CKChapter chap3 = new CKChapter(CH_AIM, 7);
		chap3.addPage(new CKPage(P_TARGET));
		chap3.addPage(new CKPage(P_STAR));
		
		teamplay.add(chap3);

		CKChapter chap4 = new CKChapter(CH_FIRE, 4);
		chap4.addPage(new CKPage(P_ILLUMINATE));
		teamplay.add(chap4);
		
		CKChapter chap5 = new CKChapter(CP_PER_ROUND,5);
		teamplay.add(chap5);
		
		teamplay.addChapter(new CKChapter(SPEED,1));
		teamplay.addChapter(new CKChapter(RECHARGE_CP,10));
		teamplay.addChapter(new CKChapter(MAX_CP,100));
		teamplay.addChapter(new CKChapter(CH_EVADE,0));
		teamplay.addChapter(new CKChapter(CH_ACCURACY,0));
		teamplay.addChapter(new CKChapter(CH_DEFENSE,0));
		teamplay.addChapter(new CKChapter(CH_DEFENSE_EFFECTIVENESS,70));
		teamplay.addChapter(new CKChapter(MAX_DAMAGE,40));
		teamplay.addChapter(new CKChapter(CH_ATTACK_BONUS,0));
		
		
		

		
		
		
		CKTeam team = new CKTeam("heroes");
		team.addToAbilties(teamplay);
		String functions =
				"def moveAndTurn(right):\n"+
				"	if(right):\n" +
				"		move('right', 1)\n" +
				"	else:\n" +
				"		move('left', 1)\n" +
				"	move('forward',1)\n\n"+
				"def talkTo():\n"+
				"      pc = aim('target',5)\n" +
				"      voice('talk',1,pc)\n\n" +
				"def lightUp():\n"+
				"      pc = aim('target',5)\n"+
				"      fire('"+P_ILLUMINATE+"',1,pc)\n\n"+
				"def lightAll():\n" +
				"      pc = aim('star',5)\n" +
				"      fire('"+P_ILLUMINATE+"',2,pc)\n\n";
		System.out.println(functions);
		team.setFunctions(functions);
		
		//add some characters!
		CKGridActor dad = new CKGridActor("heroSprite",Direction.NORTHEAST);
		dad.setName("Dad");
		dad.setTeam(team);
		
		
		CKChapter dChap = new CKChapter(CH_MOVE,2);
		CKChapter dChap2 = new CKChapter(CH_FIRE,2,P_IGNITE);
		CKChapter dChap3 = new CKChapter(CH_EQUIP_SLOTS,0);
		dChap3.addPage(new CKPage(P_SHOES));
		dChap3.addPage(new CKPage(P_SWORD));
		dChap3.addPage(new CKPage(P_ARMOR));
		dChap3.addPage(new CKPage(P_MOUTH));
		System.out.println(""+dChap+dChap2+dChap3);
		CKBook dBook = new CKBook("dad's book");
		
		dBook.addChapter(dChap);
		System.out.println("DAD0"+dBook);
		
		dBook.addChapter(dChap2);
		System.out.println("DAD1"+dBook);
		
		dBook.addChapter(dChap3);
		System.out.println("DAD2"+dBook);
		
		dad.addAbilities(dBook);
		System.out.println("DAD3"+dBook);
		
		team.addCharacter(dad);
		//combat boots
		CKBook limits = new CKBook();
		String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		limits.addChapter(new CKChapter(CH_MOVE,2,pages ) );
		CKBook abilities=new CKBook("Abilties",CH_VOICE,1,P_TALK);
		CKBook []reqs = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact combatBoots = new CKArtifact("Combat Boots","Given to you by your grandmother",
				"boots", abilities,limits,new BookList(reqs),2);
		
		//need some spells
				CKSpell spell = new CKSpell("Go Forth","moves forward by 2", "move('forward',2)","upArrow");
				combatBoots.addSpell(spell);
				spell = new CKSpell("LeftTurn","turns left and moves forward", "moveAndTurn(False)","leftArrow");
				combatBoots.addSpell(spell);
				spell = new CKSpell("UTurn","turns around", "move('left',2)","uTurn");
				combatBoots.addSpell(spell);
				
				dad.equipArtifact(P_SHOES,combatBoots);
				
		//sword of awesome
		CKBook L2 = new CKBook("Limits",CH_EARTH,5,P_SLASH);
		CKBook A2 = new CKBook("abilties",CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON);
		CKBook [] R2 = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON),
									 new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SWORD) };		
		CKArtifact coolSword = new CKArtifact("Dual Strike","Adds a bonus","axe",A2,L2,new BookList(R2),1);
		//spells?
		CKSpell slash= new CKSpell("Slash","cuts enemy by 2","physical('slash',2)","uTurn");
		coolSword.addSpell(slash);
		
		
		//spoon of awesome
		CKBook SL1 = new CKBook("Limits",CH_EARTH,5,P_BASH);
		CKBook SA2 = new CKBook("abilties",CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON);
		CKBook [] SR2 = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON) };		
		CKArtifact coolSpoon = new CKArtifact("Mom's Spoon","Home Loving","woodenSpoon",SL1,SA2,SR2,1);
		//spells?
		CKSpell bash = new CKSpell("Bash","hits enemy by 2","physical('bash',2)","sparkles");
		coolSpoon.addSpell(bash);

		
		//mouth of talking
		CKBook ML1 = new CKBook("mouth limits");
		ML1.addChapter(new CKChapter(CH_VOICE,3,P_TALK ) );
		ML1.addChapter(new CKChapter(CH_FIRE,3,P_ILLUMINATE) );
		String[] aims = {P_TARGET,P_STAR};
		ML1.addChapter(new CKChapter(CH_AIM,10,aims));
		System.out.println(ML1.treeString());
		//CKBook ML1 = new CKBook("Limits",CH_VOICE,5,P_TALK);
		CKBook MA2 = new CKBook("abilties",CH_VOICE,1,P_TALK);
		CKBook [] MR2 = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_MOUTH) };		
		CKArtifact mouth = new CKArtifact("Mouth","talking aaway","lips",MA2,ML1,MR2,1);
		//spells?
		
		
		
		CKSpell talk = new CKSpell("Talk","Speaks to person in front","talkTo()","sparkles");
		mouth.addSpell(talk);
		CKSpell light = new CKSpell("Light","illuminating","lightUp()","sparkles");
		mouth.addSpell(light);
		CKSpell light2 = new CKSpell("Light Everythin","illuminating","lightAll()","uTurn");
		mouth.addSpell(light2);
		
		//CKArtifact mouth = CKArtifactFactory.getInstance().readAssetFromXMLDirectory("motorMouth");
		
		dad.equipArtifact(P_MOUTH,mouth);

		
		CKGridActor mom = new CKGridActor("momSprite",Direction.NORTHEAST);
		mom.setName("Mom");
		mom.setTeam(team);
		
		
		//CKTriggerList triggers = new CKTriggerList();
		Satisfies s = new SpellSatisfies( CH_VOICE  ,P_TALK , 1,NumericalCostType.TRUE);
		CKGameAction a =null;
		/*
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
		*/
		a = new CKSimpleGUIAction( "Mom","What did you want?" );
		
		
		CKTriggerNode trig = new CKTrigger(s,a,TriggerResult.SATISFIED_END_LOOP);
		//triggers.add(trig);
		
		//illuminate
		//CKTriggerListNode worldTriggers = new CKTriggerList();
/*		Satisfies s2 = new SpellSatisfies( CH_FIRE  ,P_ILLUMINATE , 1,NumericalCostType.TRUE);
		CKGameAction a2 =new CKSpellAction();*/
		
		//worldTriggers.add(new Trigger(s2,a2));
		//worldTriggers.add(new CKSharedTrigger("Illuminate"));
		//CKGameObjectsFacade.setSpells(worldTriggers);
		
		mom.addTrigger(trig);
		mom.addTrigger(new CKSharedTrigger("Illuminate"));
		
		CKTriggerList worldSpells = new CKTriggerList("world Spells");
		worldSpells.add(new CKSharedTrigger("Illuminate"));
		CKGameObjectsFacade.setSpells(worldSpells);
		
		
		
		dChap = new CKChapter(CH_MOVE,1);
		dChap2 = new CKChapter(CH_VOICE,3,P_SING);
		
		String [] pos = { P_SHOES,P_OFFHAND_WEAPON} ;
		dChap3 = new CKChapter(CH_EQUIP_SLOTS,0,pos);
		dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		mom.addAbilities(dBook);
		
		
		CKBook limitsballet = new CKBook();
		String[] pagesb = {P_FORWARD,P_LEFT,P_RIGHT};
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pagesb ) );
		
		
		//String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		CKBook ABS = new CKBook("shoe abilities",SPEED ,2);
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pages ) );
		CKBook []reqsballer = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact balletShoes = new CKArtifact("Ballet Shoes","Worn by all the premier dancers!!",
				"balletShoes", ABS,limitsballet,reqsballer,2);
		
		//need some spells
		spell = new CKSpell("Forward","moves forward", "move('forward',1)","upArrow");
		balletShoes.addSpell(spell);
		spell = new CKSpell("LeftTurn","turns left", "move('left',1)","leftArrow");
		balletShoes.addSpell(spell);
		spell = new CKSpell("Right","turns right", "move('left',3)","rightArrow");
		balletShoes.addSpell(spell);
		
		//CKArtifact balletShoes = CKArtifactFactory.getInstance().readAssetFromXMLDirectory("balletShoes");
		mom.equipArtifact(P_SHOES,balletShoes);
				
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHWEST);
		baby.setName("Baby");
		baby.setTeam(team);
		
		CKBook limitsshoes = new CKBook();
		
		limitsshoes.addChapter(new CKChapter(CH_MOVE,1,pages ) );
		CKBook []reqsShoes = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact babyShoes = new CKArtifact("Bare Feet","When you can't afford shoes",
				"bareFeet", new CKBook(),limitsshoes,reqsShoes,2);


		baby.addAbilities(new CKBook("abil",CH_EQUIP_SLOTS,0,P_SHOES));

		baby.equipArtifact(P_SHOES, babyShoes);

		//need some spells
		spell = new CKSpell("Forward","moves forward", "move('forward',1)","upArrow");
		babyShoes.addSpell(spell);
		
		
		//add characters
		
		team.addCharacter(mom);
		//team.addCharacter(baby);
		
		//add artifacts
		team.addArtifact(combatBoots);
		team.addArtifact(coolSword);
		team.addArtifact(coolSpoon);
		team.addArtifact(balletShoes);
		team.addArtifact(babyShoes);
		team.addArtifact(mouth);
		
		team.setName("SpellTest");
		team.setAID("SpellTest");
		CKTeamFactory.getInstance().writeAssetToXMLDirectory(team);
		
		dad.setAID("SpellTestDad");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(dad);
		
		mom.setAID("SpellTestMom");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(mom);
		
		//add to quest
		
		//ActorNode babyActor =	new ActorNode("Baby",25,Direction.NORTHWEST, 
		//		new CKPosition(9,0,0,0),2,baby,new TriggerList());
		
		ActorNode momActor =	new ActorNode("SpellTestMom",Direction.NORTHEAST, 
				new CKPosition(9,7,0,0),new CKTriggerList());
		ActorNode dadActor =	new ActorNode("SpellTestDad",Direction.NORTHEAST, 
				new CKPosition(9,8,0),new CKTriggerList());
		/*
		ActorNode momActor =	new ActorNode("Mom","momSprite",Direction.NORTHEAST, 
				new CKPosition(9,7,0,0),2,mom,triggers);
		ActorNode dadActor =	new ActorNode("Dad","heroSprite",Direction.NORTHEAST, 
				new CKPosition(9,8,0,0),2,dad,new CKTriggerList());
		//q.addActor(babyActor);
*/
		
		q.addActor(momActor,"SpellTest");
		q.addActor(dadActor,"SpellTest");
		q.setTeam("SpellTest");
	}
	
	
	public static Quest createTestQuest()
	{
		//CKGrid grid = new CKGrid(10,10);
		QuestData q = new QuestData(5);
		q.setSceneID("Kitchen");
				
		Satisfies winSatisfies2 = new PositionReachedSatisfies("Mom", new CKPosition(0,0));
		Satisfies winSatisfies3 = new PositionReachedSatisfies("Dad", new CKPosition(0, 0));
		
		//start action

		q.addTrigger(new CKTrigger(winSatisfies2,
				new CKSimpleGUIAction("Mom","Looks like I won.  Who wants brownies?"),TriggerResult.SATISFIED_END_QUEST));	
		q.addTrigger(new CKTrigger(winSatisfies3,
				new CKSimpleGUIAction("Dad","SPOOON!!"),TriggerResult.SATISFIED_END_QUEST));	
	addActorData(q);
	
	return new Quest(q);
	}
	public static void main(String[] args)
	{
		new CKWorkshopTest(createTestQuest());
		
	}
}
