package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.*;

import java.util.Vector;

import ckCommonUtils.CKPosition;
import ckEditor.treegui.ActorNode;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKSpellCast;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;
import ckSnapInterpreter.CKDrawerTab;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CKUITurns extends Application 
{
	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	 
	HBox ControlSpells;
	HBox CharacterIcons;
	GridPane PlayerStatsWindow;
	WebView BrowserWindow;
	GridPane PlayerDescriptionWindow;
	GridPane ArtifactDescriptionWindow;
	HBox ArtifactSelectionWindow;
	VBox AddedAbilitiesWindow;
	public CKDataModel data;
	QuestData q;
	CKTeam team;
	Vector<CKGridActor> actors;
	Pane pane;
	Quest quest;

	
	
	FadeTransition ftShowControls = new FadeTransition(Duration.seconds(0.3), ControlSpells);
	FadeTransition ftHideControls = new FadeTransition(Duration.seconds(0.3), ControlSpells);
	PauseTransition p = new PauseTransition(Duration.seconds(0.3));
	SequentialTransition stControls = new SequentialTransition (p, ftShowControls);



    @Override
    public void start(Stage primaryStage) {

    	//The main pane that all DrawerTab nodes are added onto
    	//BorderPane border = new BorderPane();
    	//border.setPrefSize(200,200);
    	SplitPane split =new SplitPane();
    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
  
    	//populateModel();
    	quest = createTestQuest();
    	CKGameObjectsFacade.setQuest(quest);
    	CKGameObjectsFacade.setDataModel(data);
    	
    	
 
		FX2dGraphicsEngine view = CKGameObjectsFacade.getEngine();

		view.widthProperty().bind(menuPane.widthProperty());
		view.heightProperty().bind(menuPane.heightProperty());
    	menuPane.getChildren().add(view);
	     


    	
    	
    	CKPlayerPane player = new CKPlayerPane(data);
    	CKDrawerTab playerTab = new CKDrawerTab(player, DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0, "ckSnapInterpreter/silhouette.png");
    	playerTab.setOpenSize(40.0, 100.0);
    	
    	CKPlayerIconPane icons = new CKPlayerIconPane(data);
    	CKDrawerTab iconsTab = new CKDrawerTab(icons, DrawerSides.LEFT, 0.0, 0.0, 350.0, 170.0, "ckSnapInterpreter/headshot.png");
    	iconsTab.openDrawer(true);

    	CKArtifactPane artifact = new CKArtifactPane(data);
    	CKDrawerTab artifactTab = new CKDrawerTab(artifact, DrawerSides.TOP, 350.0, 0.0, 400.0, 300.0, "ckSnapInterpreter/arrow.png");
    	
    	CKSnapPane snap = new CKSnapPane(data); //Snap Pane
    	//CKDrawerTab snapTab = new CKDrawerTab(snap, DrawerSides.RIGHT, 750.0, 0.0, 690.0, 820.0, "ckSnapInterpreter/text.png");
    	
    	CKControlSpellsPane controls = new CKControlSpellsPane(data);
    	CKAllArtifactsPane allArtifacts = new CKAllArtifactsPane(data, controls);
    	CKDrawerTab allArtifactsTab = new CKDrawerTab(allArtifacts, DrawerSides.BOTTOM, 
    			350.0, 500.0, 400.0, 120.0, "ckSnapInterpreter/sword.png"); 
    	
    	allArtifactsTab.openDrawer(true);
    	
//    	allArtifactsTab.translateXProperty().bind(menuPane.widthProperty().divide(2) );
    	allArtifactsTab.layoutYProperty().bind(menuPane.heightProperty().subtract(120) );
    	
    	controls.layoutXProperty().bind(allArtifactsTab.layoutXProperty());
		controls.layoutYProperty().bind(allArtifactsTab.layoutYProperty().subtract(140));
	
    	
    	CKAddedAbilitiesPane abilities = new CKAddedAbilitiesPane(data);
    	CKDrawerTab abilitiesTab = new CKDrawerTab(abilities, DrawerSides.RIGHT, 350.0, 300.0, 400.0, 295.0, "ckSnapInterpreter/arrow.png");
    	
    	CKPlayerStatsPane stats = new CKPlayerStatsPane(data);
    	CKDrawerTab statsTab = new CKDrawerTab(stats, DrawerSides.LEFT, 0.0, 470.0, 350.0, 350.0, "ckSnapInterpreter/text.png");


    	Button startTurn = new Button("StartTurn");
    	startTurn.setOnAction(e-> {castSpellOnCurrent("WORLD", "START TURN",1);} );
    	startTurn.relocate(350, 0);

    	Button endTurn = new Button("EndTurn");
    	endTurn.setOnAction(e-> {castSpellOnCurrent("WORLD", "END TURN",1);} );
    	endTurn.relocate(350, 50);

    	menuPane.getChildren().addAll(iconsTab, playerTab, artifactTab,
										abilitiesTab, allArtifactsTab, statsTab,controls,
										startTurn,endTurn
				);

		split.getItems().addAll(menuPane,snap);
		split.setDividerPosition(0, .75);

	
//		Scene scene = new Scene(menuPane,1500,820);
		Scene scene = new Scene(split,1100,650);
		
	    primaryStage.setTitle("Test Drawer Tabs");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    
	    
	    //now to add the game Thread....
	    Thread T = new gameThread();
		T.start();
	    //q.gameLoop();
    }
    
    
    
    public void castSpellOnCurrent(String chapter, String page,int CP)
    {
    	
    	CKGridActor actor = data.getPlayer();
    	if(actor == null) { return;}
    	
    	
    	CKSpellCast cast = new CKSpellCast(CKSpellObject.getItemAt(actor.getPos()),
				actor, chapter,page, CP, "");
    	//runs it on, not the FXThread!
    	Thread t = new Thread()
		{
    		public void run()
    		{	
    			cast.castSpell();
				}
		};
		t.start();
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
		
		team = q.getActorsFromTeam("ArtifactTest").get(0).getTeam();                    //new CKTeam("heroes");
		team.addToAbilties(teamplay);
	
		System.out.println(team.getAbilities().treeString());
		
		team.getArtifacts("Dad").get(0).setLimits(null);
		team.getArtifacts("Mom").get(0).setLimits(null);
		
    
		data = new CKDataModel(null,null,null);
	    data.setTeam(team);

    }
    

	class gameThread extends Thread
	 {
		 public void run()
		 {
			 System.out.println("STARTING GAME LOOP");

				 
						 
			 try
			 {
				 quest.gameLoop();
			 }
			 catch (Exception e)
			 {
				System.out.println(e);
			 }
			 
			 System.out.println("ENDING GAME LOOP");
		 }
}
    public Quest createTestQuest()
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
	return new Quest(q);
	}

    
    
  
 
    public static void main(String[] args) {
    	
        launch(args);
    }
}


