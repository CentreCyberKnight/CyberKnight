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


public class CKQuestRunner extends Application 
{
	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	 
	
	public CKDataModel data;
	private Quest quest;

	

	
	@Override
	public void init() throws Exception
	{
	
		super.init();
		
		Parameters param = getParameters();
		for(String p:param.getRaw())
		{
			System.out.println("Param:"+p);
			
			
		}
		String asset = param.getRaw().get(0);
		CKTeamFactory.getInstance().clearCache();
		QuestData q  = CKQuestFactory.getInstance().getAsset(asset);
		quest = new Quest(q);
		
		data = new CKDataModel(q);
	}
	
	

    @Override
    public void start(Stage primaryStage) {

    	/*String asset = "asset7364953977011982560";
    	
    	QuestData q  = CKQuestFactory.getInstance().getAsset(asset);
		quest = new Quest(q);
	*/
    	
    	
    	//The main pane that all DrawerTab nodes are added onto
    	//BorderPane border = new BorderPane();
    	//border.setPrefSize(200,200);
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
			 }
			 catch (Exception e)
			 {
				System.out.println(e);
			 }
		
		 }
}
   

    
    
  
 
    public static void main(String[] args)
    {
    	
    	String input[] = {"asset7364953977011982560"};
    	
        launch(input);
    	//launch(args);
    }
}


