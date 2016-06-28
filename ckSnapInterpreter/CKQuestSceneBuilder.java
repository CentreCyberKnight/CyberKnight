package ckSnapInterpreter;

import ckDatabase.CKQuestFactory;
import ckDatabase.CKTeamFactory;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckSnapInterpreter.CKDrawerTab;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class CKQuestSceneBuilder  
{
	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	 
	
	public CKDataModel data;
	private Quest quest;
	private Scene scene;

	
	public CKQuestSceneBuilder (String questID)
	{
	
		CKTeamFactory.getInstance().clearCache();
		QuestData q  = CKQuestFactory.getInstance().getAsset(questID);
		init(q);
	}
	
	public CKQuestSceneBuilder(QuestData q)
	{
		
		init(q);
	}
	
	
	private void init(QuestData q)
	{
		quest = new Quest(q);
		data = new CKDataModel(q);
	}
	
	public	Scene getAndStartScene()
		{
		
		SplitPane split =new SplitPane();
    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
  
    	
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
    	CKDrawerTab allArtifactsTab = new CKDrawerTab(allArtifacts, 
    			DrawerSides.LEFT, 0.0, 470.0, 190.0, 350.0,
    			//DrawerSides.BOTTOM,350.0, 500.0, 400.0, 120.0,
    			"ckSnapInterpreter/sword.png"); 
    	
    	allArtifactsTab.openDrawer(true);
    	
//    	allArtifactsTab.translateXProperty().bind(menuPane.widthProperty().divide(2) );
//    	allArtifactsTab.layoutYProperty().bind(menuPane.heightProperty().subtract(120) );
    	
    	controls.layoutXProperty().bind(allArtifactsTab.layoutXProperty().add(70));
//		controls.layoutYProperty().bind(allArtifactsTab.layoutYProperty().subtract(140));
		controls.layoutYProperty().bind(allArtifactsTab.layoutYProperty());
	
    	
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
    	
    	Button endWin = new Button("End/Win");
    	endWin.setOnAction(e->CKGameObjectsFacade.getGameCompletionListener().endGame(3,quest.getQuestData().getAID()));
    	endWin.relocate(425, 0);
    	
    	Button endLose = new Button("End/Lose");
    	endLose.setOnAction(e->CKGameObjectsFacade.getGameCompletionListener().endGame(-1,quest.getQuestData().getAID()));
    	endLose.relocate(425, 50);

    	menuPane.getChildren().addAll(iconsTab, playerTab, artifactTab,
										//abilitiesTab,
    									allArtifactsTab, 
										//statsTab,
										controls,
										startTurn,endTurn,
										endWin,endLose
				);

		split.getItems().addAll(menuPane,snap);
		split.setDividerPosition(0, .75);

	
//		Scene scene = new Scene(menuPane,1500,820);
		scene = new Scene(split,1100,650);
		
	
	
	
	
	
	    //now to add the game Thread....
	    Thread T = new gameThread();
		T.start();
		return scene;
	    
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
    
    
    

	
    

	class gameThread extends Thread
	 {
		
		public gameThread()
		{
			this.setDaemon(true);
		}
		
		 public void run()
		 {
			 
						 
			 try
			 {
				 quest.gameLoop();
				 //MKB do call back here to report that the level is done.
			 }
			 catch (Exception e)
			 {
				System.out.println(e);
			 }
		
		 }
}

}


