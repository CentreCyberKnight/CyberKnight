package ckPythonInterpreterTest;

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
import static ckCommonUtils.CKPropertyStrings.MAX_CP;
import static ckCommonUtils.CKPropertyStrings.P_ARMOR;
import static ckCommonUtils.CKPropertyStrings.P_FORWARD;
import static ckCommonUtils.CKPropertyStrings.P_IGNITE;
import static ckCommonUtils.CKPropertyStrings.P_LEFT;
import static ckCommonUtils.CKPropertyStrings.P_OFFHAND_WEAPON;
import static ckCommonUtils.CKPropertyStrings.P_RIGHT;
import static ckCommonUtils.CKPropertyStrings.P_SHOES;
import static ckCommonUtils.CKPropertyStrings.P_SING;
import static ckCommonUtils.CKPropertyStrings.P_SWORD;
import static ckCommonUtils.CKPropertyStrings.SPEED;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKThreadCompletedListener;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGridActorFactory;
import ckDatabase.CKTeamFactory;
import ckEditor.CKTeamArtifactEditor;
import ckEditor.treegui.ActorNode;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKTeamView;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSnapInterpreter.SwingFXWebView;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;
import javafx.embed.swing.JFXPanel;

public class CKArtifactQuestRunner implements DocumentListener 
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
	 public CKArtifactQuestRunner(Quest q) 
	 {
		 this(q,true);
	 }
	
	 public CKArtifactQuestRunner(Quest q,boolean newThread) 
	 {
		 quest = q;
		 frame = new JFrame("CyberKnight Level 1");//CKSytaxPaneTest.class.getName());
		 final Container c = frame.getContentPane();
		 c.setLayout(new BorderLayout());
		 
		 
		 JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		 
		 
		 
		 leftPanel = new JPanel();
		leftPanel.setLayout(new CardLayout());
		// leftPanel.add(createEditorPane(),TEXT_EDITOR);
		 artifactController =createArtifactControllerPane(); 
		 leftPanel.add(artifactController,ARTIFACT_CONTROLLER);
		 //leftPanel.add(artifactController,BorderLayout.CENTER);
		 CardLayout cl = (CardLayout)(leftPanel.getLayout());
		 cl.show(leftPanel, ARTIFACT_CONTROLLER);
		
		 
		 
		 split.setLeftComponent(leftPanel);
		 split.setRightComponent(createTabPane());
		 c.add(split,BorderLayout.CENTER);
		 c.doLayout();    	

		 frame.setSize(1000, 600);
		 frame.setVisible(true);
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		// String cmd = "move('left',1)\n";				 
		 
		 //editor.setText(cmd);
		 if(newThread)
		 {
		 Thread T = new gameThread();
		 T.start();
		 }
		 else
		 {
			 quest.gameLoop();			 
		 }


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
		 Vector<CKGridActor> actors = quest.getActorsFromTeam(quest.getTeam());
		//CKTeam homeTeam = CKTeamFactory.getInstance().getAsset(quest.getTeam());
		CKTeamView art = new CKTeamView(actors);  
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
	 
	 
	
	 private JComponent getScenePanel(int sceneId)
	 {
		 //DOn't need to call since quest is already initialized
		 //return new CKSceneViewer(scene,sceneId);
		 //quest = CKGameObjectsFacade.getQuest();
		 //quest.creation(sceneId, frame);
		 CKGameObjectsFacade.setQuest(quest);
/*		 JFXPanel panel = new JFXPanel();
		 HBox root = new HBox();
		 root.getChildren().add(CKGameObjectsFacade.getEngine());
		 Scene scene = new Scene(root);
		 panel.setScene(scene);
		 return panel;
		 */
		 
		 return CKGameObjectsFacade.getJPanelEngine();
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
		 

		 CKTeam team = CKTeamFactory.getInstance().getAsset(quest.getTeam());
		 CKGameObjectsFacade.setArtifactEditor(new CKTeamArtifactEditor(team));
		 tab.addTab("Artifacts",CKGameObjectsFacade.getArtifactEditor());
		 tab.setMnemonicAt(3, KeyEvent.VK_F4);
		 
		 tab.addTab("Functions",createEditorPane());
		 tab.setMnemonicAt(4, KeyEvent.VK_F5);
		 
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
	 
	 
	 
	 //FIXME this is no longer needed here....but removing breaks the program.
	 private JFXPanel createEditorPane()
	 {
		 //taken from SwingFXWebView main
		 //creates a frame in the gui 
		 JFrame frame = new JFrame();           
         frame.getContentPane().add(new SwingFXWebView());            
         frame.setMinimumSize(new Dimension(500, 600));  
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
         frame.setVisible(true);
         //adds snaps to the frame
		 return SwingFXWebView.main(); 		 
	 }
	 
	 

	 
	 	@SuppressWarnings("unused")
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
	 			CKGameObjectsFacade.getQuest().notifyOfInput();
				
			}
	 		
	 	}
	 	
	
	 	
	public static void addActorData(QuestData q)
	{
		
		 CKArtifactFactory aFactory = CKArtifactFactory.getInstance();
		
		//make a team:)
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 1);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		teamplay.addChapter(new CKChapter(MAX_CP,10));
		teamplay.addChapter(new CKChapter(SPEED,5));
		
		CKTeam team = new CKTeam("heroes");
		team.addToAbilties(teamplay);
		String functions =
				"def moveAndTurn(right):\n"+
				"	if(right):\n" +
				"		move('right', 1)\n" +
				"	else:\n" +
				"		move('left', 1)\n" +
				"	move('forward',1)\n";
						
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

		CKArtifact combatBoots = aFactory.getAsset("combatBoots");
		dad.equipArtifact(P_SHOES,combatBoots);

		CKArtifact coolSword = aFactory.getAsset("coolSword");
		CKArtifact coolSpoon = aFactory.getAsset("coolSpoon");
		
		CKGridActor mom = new CKGridActor("momSprite",Direction.NORTHEAST);
		mom.setName("Mom");
		mom.setTeam(team);
		
		
		
		dChap = new CKChapter(CH_MOVE,1);
		dChap2 = new CKChapter(CH_VOICE,3,P_SING);
		
		String [] pos = { P_SHOES,P_OFFHAND_WEAPON} ;
		dChap3 = new CKChapter(CH_EQUIP_SLOTS,0,pos);
		dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		mom.addAbilities(dBook);
		
		
		CKArtifact balletShoes = aFactory.getAsset("balletShoes");
		
		mom.equipArtifact(P_SHOES,balletShoes);
				
		
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHWEST);
		baby.setName("Baby");
		baby.setTeam(team);
	
		baby.addAbilities(new CKBook("abil",CH_EQUIP_SLOTS,0,P_SHOES));
		
		CKArtifact babyShoes = aFactory.getAsset("bareFeet");
		baby.equipArtifact(P_SHOES, babyShoes);

		
		
		//add characters
		
		//team.addCharacter(mom);
		//team.addCharacter(baby);
		
		//add artifacts
		team.addArtifact(combatBoots);
		team.addArtifact(coolSword);
		team.addArtifact(coolSpoon);
		team.addArtifact(balletShoes);
		team.addArtifact(babyShoes);
		
		team.setName("ArtifactTest");
		team.setAID("ArtifactTest");
		CKTeamFactory.getInstance().writeAssetToXMLDirectory(team);
		
		dad.setAID("ArtifactTestDad");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(dad);
		
		mom.setAID("ArtifactTestMom");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(mom);
		
		baby.setAID("ArtifactTestBaby");
		CKGridActorFactory.getInstance().writeAssetToXMLDirectory(baby);
		
		
		//add to quest
		ActorNode babyActor =	new ActorNode("ArtifactTestBaby",Direction.NORTHWEST, 
				new CKPosition(9,0,0,0),new CKTriggerList());
		babyActor.setControllerID("ARTIFACT");
		ActorNode momActor =	new ActorNode("ArtifactTestMom",Direction.NORTHEAST, 
				new CKPosition(9,7,0,0),new CKTriggerList());
		momActor.setControllerID("ARTIFACT");
		ActorNode dadActor =	new ActorNode("ArtifactTestDad",Direction.NORTHEAST, 
				new CKPosition(5,9,0,0),new CKTriggerList());
		//dadActor.
		
		
		q.addActor(babyActor,"ArtifactTest");
		q.addActor(momActor,"ArtifactTest");
		q.addActor(dadActor,"ArtifactTest");
		q.setTeam("ArtifactTest");
	}
	
	
	public static Quest createTestQuest()
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
	/*	q.addTrigger(new CKTrigger(new TrueSatisfies(), 
				new CKSimpleGUIAction("Dad","Lets race to get to the fridge"),
				TriggerResult.INIT_ONLY));
		*/
		//Win actions
		q.addTrigger(new CKTrigger(winSatisfies1,
				new CKSimpleGUIAction("Baby","GAGA!!"),TriggerResult.SATISFIED_END_QUEST));	
		q.addTrigger(new CKTrigger(winSatisfies2,
				new CKSimpleGUIAction("Mom","Looks like I won.  Who wants brownies?"),TriggerResult.SATISFIED_END_QUEST));	
		q.addTrigger(new CKTrigger(winSatisfies3,
				new CKSimpleGUIAction("Dad","SPOOON!!"),TriggerResult.SATISFIED_END_QUEST));	
	addActorData(q);
	
	return new Quest(q);
	}
	public static void main(String[] args)
	{
		new CKArtifactQuestRunner(createTestQuest());
		
	}
	
	
	public void storeFunctions()
	{
		 CKTeam team = CKTeamFactory.getInstance().getAsset(quest.getTeam());
		 team.setFunctions(editor.getText());
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0)
	{
		storeFunctions();

	}

	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		storeFunctions();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0)
	{
		storeFunctions();
		
	}
}
