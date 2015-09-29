package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_EARTH;
import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;
import static ckCommonUtils.CKPropertyStrings.CH_FIRE;
import static ckCommonUtils.CKPropertyStrings.CH_MOVE;
import static ckCommonUtils.CKPropertyStrings.CH_VOICE;
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

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.SwingUtilities;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.treegui.ActorNode;
import ckEditor.treegui.BookList;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;
import ckGameEngine.Direction;
import ckGameEngine.Quest;
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.FXAssetViewer;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSnapInterpreter.CKDrawerTab;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;
import ckTrigger.TriggerResult;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CKUI extends Application 
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
	public CKData data;
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
    	BorderPane border = new BorderPane();
    	border.setPrefSize(200,200);
    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
  
    	populateModel();

   //CKGameObjectsFacade.setQuest(quest);
  		 //	swingNode.setContent(CKGameObjectsFacade.getEngine());
    	CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("hero");
    	// need to slip this in.  FX2dGraphicsEngine engine = new FX2dGraphicsEngine();
	//	FXAssetViewer view=new FXAssetViewer(1,A1,new Dimension(700,800),true);
		FXAssetViewer view=new FXAssetViewer(1,A1,new Dimension(1500,820),true);
//		view.maxWidth(Double.MAX_VALUE);
//    	view.maxHeight(Double.MAX_VALUE);

    	//menuPane.getChildren().add(view);

		view.widthProperty().bind(menuPane.widthProperty());
		view.heightProperty().bind(menuPane.heightProperty());
    	menuPane.getChildren().add(view);


    	
    	
    	CKPlayerPane player = new CKPlayerPane(data);
    	CKDrawerTab playerTab = new CKDrawerTab(player, DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0, "ckSnapInterpreter/silhouette.png");
    	playerTab.setOpenSize(40.0, 100.0);
    	
    	CKPlayerIconPane icons = new CKPlayerIconPane(data);
    	CKDrawerTab iconsTab = new CKDrawerTab(icons, DrawerSides.LEFT, 0.0, 0.0, 350.0, 170.0, "ckSnapInterpreter/headshot.png");
    	
    	CKArtifactPane artifact = new CKArtifactPane(data);
    	CKDrawerTab artifactTab = new CKDrawerTab(artifact, DrawerSides.TOP, 350.0, 0.0, 400.0, 300.0, "ckSnapInterpreter/arrow.png");
    	
    	CKSnapPane snap = new CKSnapPane(data); //Snap Pane
    	CKDrawerTab snapTab = new CKDrawerTab(snap, DrawerSides.RIGHT, 750.0, 0.0, 690.0, 820.0, "ckSnapInterpreter/text.png");
    	
    	CKControlSpellsPane controls = new CKControlSpellsPane(data);
    	CKAllArtifactsPane allArtifacts = new CKAllArtifactsPane(data, controls);
    	CKDrawerTab allArtifactsTab = new CKDrawerTab(allArtifacts, DrawerSides.BOTTOM, 350.0, 720.0, 400.0, 100.0, "ckSnapInterpreter/sword.png"); 
    	
    	CKAddedAbilitiesPane abilities = new CKAddedAbilitiesPane(data);
    	CKDrawerTab abilitiesTab = new CKDrawerTab(abilities, DrawerSides.RIGHT, 350.0, 300.0, 400.0, 295.0, "ckSnapInterpreter/arrow.png");
    	
    	CKPlayerStatsPane stats = new CKPlayerStatsPane(data);
    	CKDrawerTab statsTab = new CKDrawerTab(stats, DrawerSides.LEFT, 0.0, 470.0, 350.0, 350.0, "ckSnapInterpreter/text.png");


		menuPane.getChildren().addAll(iconsTab, playerTab, artifactTab, abilitiesTab, snapTab, allArtifactsTab, statsTab);

		

	  //  Scene scene = new Scene(menuPane,700,720);
	    //Scene scene = new Scene(menuPane,1500,820);
		
//		border.setCenter(menuPane);
//		border.setRight(Snap());
		
		Scene scene = new Scene(menuPane,1500,820);
		
	    primaryStage.setTitle("Test Drawer Tabs");
	    primaryStage.setScene(scene);
	    primaryStage.show();
    }
    

	public void populateModel() {	


		//make a team:)
		CKBook teamplay = new CKBook();
		CKChapter chap = new CKChapter(CH_MOVE, 1);
		chap.addPage(new CKPage(P_FORWARD));
		chap.addPage(new CKPage(P_LEFT));
		chap.addPage(new CKPage(P_RIGHT));
		teamplay.addChapter(chap);
		
		team = new CKTeam("heroes");
		team.addToAbilties(teamplay);
		
		//add some characters!
		CKGridActor dad = new CKGridActor("heroSprite",Direction.NORTHEAST);
		dad.setName("Dad");
		
		CKChapter dChap = new CKChapter(CH_MOVE,2);
		CKChapter dChap2 = new CKChapter(CH_FIRE,2,P_IGNITE);
		CKChapter dChap3 = new CKChapter(CH_EQUIP_SLOTS,0);
		dChap3.addPage(new CKPage(P_SHOES));
		dChap3.addPage(new CKPage(P_SWORD));
		dChap3.addPage(new CKPage(P_ARMOR));
		CKBook dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		dad.addAbilities(dBook);
		
		//team.addCharacter(dad);
		dad.setTeam(team);
		
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
		spell = new CKSpell("LeftTurn","turns left", "move('left',1)","left" +"Arrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("UTurn","turns around", "move('left',2)","uTurn");
		combatBoots.addSpell(spell);
		
		
		
		//sword of awesome
		CKBook L2 = new CKBook("Limits",CH_EARTH,5,P_SLASH);
		CKBook A2 = new CKBook("abilties",CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON);
		CKBook [] R2 = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_OFFHAND_WEAPON),
									 new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SWORD) };		
		CKArtifact coolSword = new CKArtifact("Dual Strike","Adds a bonus","axe",A2,L2,new BookList(R2),1);
		//spells?
		CKSpell slash= new CKSpell("Slash","cuts enemy by 2","physical('slash',2)","uTurn");
		coolSword.addSpell(slash);
		
		
		CKGridActor mom = new CKGridActor("momSprite",Direction.NORTHEAST);
		mom.setName("Mom");
		//dad.equipArtifact(combatBoots);
		dad.equipArtifact(coolSword);
		
		dChap = new CKChapter(CH_MOVE,1);
		dChap2 = new CKChapter(CH_VOICE,3,P_SING);
		String [] pos = { P_SHOES,P_OFFHAND_WEAPON} ;
		dChap3 = new CKChapter(CH_EQUIP_SLOTS,0,pos);
		dBook = new CKBook();
		dBook.addChapter(dChap);
		dBook.addChapter(dChap2);
		dBook.addChapter(dChap3);
		mom.addAbilities(dBook);
		
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHEAST);
		baby.setName("Baby");


		baby.addAbilities(new CKBook("abil",CH_EQUIP_SLOTS,0,P_SHOES));
		
		//team.addCharacter(mom);
		//team.addCharacter(baby);
		//already did team.addCharacter(dad);
		
		//add artifacts
		team.addArtifact(combatBoots);
		team.addArtifact(coolSword);
		
	    //actors = new Vector<>();
	    //actors.add(dad);
	    //dad.setTeam(team);
	    //actors.add(mom);
	    mom.setTeam(team);
	   // actors.add(baby);
	    baby.setTeam(team);
	    //mom.equipArtifact(P_SHOES, combatBoots);
	    mom.equipArtifact(combatBoots);
	    
		//add to quest
		ActorNode babyActor =	new ActorNode("ArtifactTestBaby",Direction.NORTHWEST, 
				new CKPosition(9,0,0,0),new CKTriggerList());
		babyActor.setControllerID("NULL");
		ActorNode momActor =	new ActorNode("ArtifactTestMom",Direction.NORTHEAST, 
				new CKPosition(9,7,0,0),new CKTriggerList());
		momActor.setControllerID("NULL");
		ActorNode dadActor =	new ActorNode("ArtifactTestDad",Direction.NORTHEAST, 
				new CKPosition(5,9,0,0),new CKTriggerList());
		dadActor.setControllerID("NULL");
		
	    
	    
//		q2.addActor(babyActor,"ArtifactTest");
//		q2.addActor(momActor,"ArtifactTest");
//		q2.addActor(dadActor,"ArtifactTest");
//		q2.setTeam("ArtifactTest");
	    
	    data = new CKData(mom, combatBoots, spell);
	    data.setTeam(team);
	   // data.setPlayer(mom);
	    data.setArtifact(combatBoots);
//	    data.setSpell(spell);
	   // CKGameObjectsFacade.getEngine();

    }
    

	class gameThread extends Thread
	 {
		 public void run()
		 {
			 quest.gameLoop();
		 }
}
    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//            	JPanel j = CKGameObjectsFacade.getEngine();
//            	j.setPreferredSize(new Dimension(600,600));
        		CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("hero");	
        		//CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("hero");	
        		//CKGameObjectsFacade.setQuest(quest);
        		CKAssetViewer view=new CKAssetViewer(1,A1,null,true);
        		view.setPreferredSize(new Dimension(600,600));
            	/*JPanel j2 = new JPanel();
            	j2.setPreferredSize(new Dimension(600,600));
            	j2.setLayout(new BorderLayout());
            	j2.add(view, BorderLayout.CENTER);
            	j2.add(new JButton("hi my name is tony"), BorderLayout.NORTH);*/
        		
        		
        		//CKGameObjectsFacade.setQuest(quest);
       		 //	swingNode.setContent(CKGameObjectsFacade.getEngine());
            	swingNode.setContent(view);
            	
            	
            	//j2.setMinimumSize(new Dimension(500, 500));
            	//j.setPreferredSize(new Dimension(4000,4000));
            	//quest.gameLoop();
            	

//              		CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("hero");	
//              		CKAssetViewer view=new CKAssetViewer(1,A1,null,true);
//              		view.setPreferredSize(new Dimension(600,600));
//                  	JPanel j2 = new JPanel();
//                  	j2.setPreferredSize(new Dimension(600,600));
//                  	j2.setLayout(new BorderLayout());
//                  	j2.add(view, BorderLayout.CENTER);
//                  	j2.add(new JButton("hi my name is tony"), BorderLayout.NORTH);
//                  	swingNode.setContent(j2);           	
            }
        });
    
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
	
	//populateModel(q);
	return new Quest(q);
	}

    
    
    
    	
    	//Icons of all the Characters available that the user can select
	public CKDrawerTab Icons() {
    	CharacterIcons = new HBox();
    	CharacterIcons.setPrefSize(350, 170);
    	//CharacterIcons.setStyle("-fx-background-color: rgb(0, 20, 28)");
    	//CharacterIcons.setOpacity(0.2);
    	CKDrawerTab icons = new CKDrawerTab(CharacterIcons, DrawerSides.LEFT, 0.0, 0.0, 350.0, 170.0, "ckSnapInterpreter/headshot.png");
    	
    		CKGridActor[] players = team.getCharacters();
    		System.out.println("This team is named " + team.toString() + " and has " + team.getCharacters().length + " players."); //team.tostring
    		int pIndex= 0;
    		for (CKGridActor p : players ) 
    			if(p != null) {
    				pIndex ++;
    				try {
    				//	CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getPortrait(p.getAssetID());
	    				System.out.println(pIndex + ": " + p.getName() + " with the assetId: " + p.getAssetID());
						//Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
						Button b = new Button(p.getAssetID(), new ImageView(p.getFXPortrait()));
						b.setOnAction(e -> {	
							data.setPlayer(p);
							
//							setAllArtifactsNodes();
//							ControlSpells.getChildren().clear();
//						//	setControlSpells();
//							setPlayerNodes();
//							setStats();
//							setArtifactNodes();
						//take these out when observers are added
						});
	    				CharacterIcons.getChildren().add(b);
	    				CharacterIcons.setAlignment(Pos.CENTER);
    				}
    				catch (NullPointerException n) {
    					System.out.println("Character Icon asset was not found" + n);
    				}	
    				
		}
    	return icons;
	}
    	
    	
    	//Description and Points of the current player
    public CKDrawerTab Player() {	
		PlayerDescriptionWindow = new GridPane();
		PlayerDescriptionWindow.setPadding(new Insets(5));
		PlayerDescriptionWindow.setHgap(2);
		PlayerDescriptionWindow.setVgap(2);
		PlayerDescriptionWindow.setAlignment(Pos.CENTER);
		PlayerDescriptionWindow.setPrefSize(350, 300);
    	PlayerDescriptionWindow.setStyle("-fx-background-color: rgb(217, 210, 240)");
		PlayerDescriptionWindow.setOpacity(0.5);
    	setPlayerNodes();
    	CKDrawerTab player = new CKDrawerTab(PlayerDescriptionWindow, DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0, "ckSnapInterpreter/silhouette.png");
    	player.setOpenSize(40.0, 100.0);
    	return player;
    }
    
    public void setPlayerNodes() {
    	Rectangle imageRect = new Rectangle(180, 250);
    	imageRect.setFill(new ImagePattern(data.getPlayer().getFXImage()));
    	PlayerDescriptionWindow.getChildren().clear();
    	PlayerDescriptionWindow.add(imageRect, 0, 0, 2, 5);
     	//PlayerDescriptionWindow.setValignment(imageRect, VPos.CENTER);
     	Label playername = new Label(data.getPlayer().getName());
     	playername.setTextFill(Color.BLACK);
     	playername.setFont(new Font("Comic Sans MS", 30));
     	PlayerDescriptionWindow.add(playername, 2, 3, 2, 1);
     	int cyberpts = data.getPlayer().getCyberPoints();
     	Label cp = new Label("CyberPoints: " + Integer.toString(cyberpts));
     	cp.setTextFill(Color.BLACK);
     	cp.setFont(new Font("Courier New", 20));
//    	data.registerArtifactObserver((artifact) -> 
//    	{ 
//    	try {
//    		
//    	}
//    	catch (JSException e) {
//    		System.out.println(e.getMessage());
//    	}
//    	});
     	PlayerDescriptionWindow.add(cp, 2, 4, 3, 1);
    }
    	
    	//Player Statistics
    	public CKDrawerTab Stats() {
    		PlayerStatsWindow = new GridPane();
    		PlayerStatsWindow.setPrefSize(400, 300);
    		PlayerStatsWindow.setPadding(new Insets(5));
    		PlayerStatsWindow.setHgap(2);
    		PlayerStatsWindow.setVgap(2);
    		PlayerStatsWindow.setAlignment(Pos.CENTER);
    		PlayerStatsWindow.setPrefSize(350, 350);
    		PlayerStatsWindow.setStyle("-fx-background-color: rgb(217, 210, 240)");
    		PlayerStatsWindow.setOpacity(0.5);
	    	setStats();
    		CKDrawerTab stats = new CKDrawerTab(PlayerStatsWindow, DrawerSides.LEFT, 0.0, 470.0, 350.0, 350.0, "ckSnapInterpreter/text.png");
	    	return stats;
    	}
    	
    	
        public void setStats() {
        	PlayerStatsWindow.getChildren().clear();
        	Label title = new Label(data.getPlayer().getName() + "'s Stats");
         	title.setTextFill(Color.BLACK);
         	title.setFont(new Font("Comic Sans MS", 30));
         	title.setAlignment(Pos.TOP_CENTER);
    	PlayerStatsWindow.add(title, 0, 0, 5, 1);
    	PlayerStatsWindow.setAlignment(Pos.TOP_CENTER);
    	VBox skills = new VBox();  
    	VBox skillsPts = new VBox();
    	int aIndex= 0;
		for (Iterator<CKChapter> abilities = data.getPlayer().getAbilities().getChapters(); abilities.hasNext();) {
			CKChapter c = abilities.next();
			if( c != null) {
				aIndex ++;
				Label l = new Label(c.getName());
				l.setFont(new Font("Comic Sans MS", 15));
				Label value = new Label(Integer.toString((c.getValue())));
				value.setFont(new Font("Comic Sans MS", 15));
				System.out.println("stat: " + c.getName() + " has been printed");
				skills.getChildren().add(l);
				skillsPts.getChildren().add(value);
			}
		}
//    	data.registerArtifactObserver((artifact) -> 
//    	{ 
//    	try {
//    		
//    	}
//    	catch (JSException e) {
//    		System.out.println(e.getMessage());
//    	}
//    	});
    	
     	PlayerStatsWindow.add(skills, 0, 1, 2, 1);
     	PlayerStatsWindow.add(skillsPts, 2, 1, 2, 1);
    }
    	
    	//Snap!
    	public Node Snap() {
			BrowserWindow = new WebView();
			CKGameObjectsFacade.setWebEngine(BrowserWindow.getEngine());
			WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
			BrowserWindow.setPrefSize(690, 820);
			webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
			JSObject jsobj = (JSObject) webEngine.executeScript("window");
			jsobj.setMember("javaMove", new CKSpellObject("move"));
			jsobj.setMember("jsDebug", new CKjsDebugger());

	    	//CKDrawerTab snap = new CKDrawerTab(BrowserWindow, DrawerSides.RIGHT, 750.0, 0.0, 690.0, 820.0, "ckSnapInterpreter/text.png");
	    	
	    	data.registerArtifactObserver((artifact) -> 
	    	{ 
	    	try {
			jsobj.setMember("artifact", artifact);
			webEngine.executeScript("ide.setCyberSnap()");
	    	}
	    	catch (JSException e) {
	    		System.out.println(e.getMessage());
	    	}
	    	});
	    	
	    	return BrowserWindow;
	    	//return snap;
    	}
	    	
	    	
    	//The selected artifact's enlarged image and description
    	public CKDrawerTab Artifact() {
	    	ArtifactDescriptionWindow = new GridPane();
	    	ArtifactDescriptionWindow.setPrefSize(400, 300);
	    	ArtifactDescriptionWindow.setPadding(new Insets(5));
	    	ArtifactDescriptionWindow.setHgap(2);
	    	ArtifactDescriptionWindow.setVgap(2);
	    	ArtifactDescriptionWindow.setAlignment(Pos.CENTER);
	    	ArtifactDescriptionWindow.setPrefSize(350, 300);
	    	ArtifactDescriptionWindow.setStyle("-fx-background-color: rgb(217, 210, 240)");
	    	ArtifactDescriptionWindow.setOpacity(0.5);
	    	setArtifactNodes();
	    	CKDrawerTab artifact = new CKDrawerTab(ArtifactDescriptionWindow, DrawerSides.TOP, 350.0, 0.0, 400.0, 300.0, "ckSnapInterpreter/arrow.png");
	    	return artifact;
    	}

    	
    	public void setArtifactNodes() {
    		ArtifactDescriptionWindow.getChildren().clear();
    		//guardian if statement to ensure no null pointer exception
    		if (data.getArtifact() == null) {
    			return;
    		}
        	Label title = new Label(data.getArtifact().getName());
         	title.setTextFill(Color.BLACK);
         	title.setFont(new Font("Comic Sans MS", 30));
         	title.setAlignment(Pos.TOP_CENTER);
         	ArtifactDescriptionWindow.add(title, 0, 0, 5, 1);
        	HBox hnodes = new HBox();
        	Rectangle imageRect = new Rectangle(160, 160);
        	imageRect.setFill(new ImagePattern(data.getArtifact().getFXImage()));
        	String story = data.getArtifact().getBackstory();
        	StringBuilder sb = new StringBuilder(story);
        	int i = 0;
        	while ((i = sb.indexOf(" ", i + 8)) != -1) {
        	    sb.replace(i, i + 1, "\n");
        	}
			Label l = new Label(sb.toString());
			l.setFont(new Font("Courier New", 18));
			l.setPadding(new Insets(10));
			System.out.println("Artifact: " + data.getArtifact().getName() + "'s details have been created");
			hnodes.getChildren().addAll(imageRect, l);
			hnodes.setAlignment(Pos.CENTER);
			
			
    		ArtifactDescriptionWindow.add(hnodes, 0, 1, 2, 1);
    		ArtifactDescriptionWindow.setAlignment(Pos.CENTER);
    	}
    	
    	
    	
    	//The selected artifact's enlarged image and description
    	public CKDrawerTab AddedAbilities() {
	    	AddedAbilitiesWindow = new VBox();
	    	AddedAbilitiesWindow.setPrefSize(400, 295);
	    	AddedAbilitiesWindow.setPadding(new Insets(5));
	    	AddedAbilitiesWindow.setPadding(new Insets(15, 12, 15, 12));
	    	AddedAbilitiesWindow.setSpacing(10);
	    	AddedAbilitiesWindow.setStyle("-fx-background-color: rgb(217, 210, 240)");
	    	AddedAbilitiesWindow.setOpacity(.7);
//	    	AddedAbilitiesWindow.setTranslateX(350);
//	    	AddedAbilitiesWindow.setTranslateY(300);
	    	setAddedAbilityNodes();
	    	CKDrawerTab abilities = new CKDrawerTab(AddedAbilitiesWindow, DrawerSides.RIGHT, 350.0, 300.0, 400.0, 295.0, "ckSnapInterpreter/arrow.png");

			return abilities;
    	}
    	
    	
    	//export blocks here
    	public void setAddedAbilityNodes() {
    		AddedAbilitiesWindow.getChildren().clear();
        	Label title = new Label("Added Abilities");
         	title.setTextFill(Color.BLACK);
         	title.setFont(new Font("Comic Sans MS", 30));
         	title.setAlignment(Pos.TOP_CENTER);
         	AddedAbilitiesWindow.getChildren().add(title);
        	HBox hnodes = new HBox();
        	Rectangle imageRect = new Rectangle(160, 160);
        	imageRect.setFill(new ImagePattern(data.getArtifact().getFXImage()));
			System.out.println("Artifact: " + data.getArtifact().getName() + "'s details have been created");
			hnodes.getChildren().addAll(imageRect);
			hnodes.setAlignment(Pos.CENTER);
        	VBox addedAbs = new VBox();
        	int aIndex= 0;
    		for (Iterator<CKChapter> abilities = data.getPlayer().getAbilities().getChapters(); abilities.hasNext();) {
    			CKChapter c = abilities.next();
    			if( c != null) {
    				aIndex ++;
					Label l = new Label(c.getName());
					l.setFont(new Font("Comic Sans MS", 15));
					Label value = new Label(Integer.toString((c.getValue())));
					value.setFont(new Font("Comic Sans MS", 15));
					System.out.println("stat: " + c.getName() + " has been printed");
					addedAbs.getChildren().add(l);
    			}
    		}
    		
			
			AddedAbilitiesWindow.getChildren().addAll(addedAbs);
			AddedAbilitiesWindow.setAlignment(Pos.CENTER);
    	}
    	
    	

    	public void setAllArtifactsNodes() {
			try {
				data.setArtifact(null);
	    		Vector<CKArtifact> arts = team.getArtifacts(data.getPlayer().getName());
	    		System.out.println(data.getPlayer().getName() + " is equipped with " + arts.size() + " artifacts");
	    		int aIndex = 0;
	    		ArtifactSelectionWindow.getChildren().clear();
	    		for (CKArtifact a : arts) 
	    			if(a != null) {
	    				aIndex ++;
	    				System.out.println(aIndex + ": " + a.getIconId());
						Button b = new Button(a.getAID(), new ImageView(a.getFXImage()));
						b.setOnMouseEntered(e -> {	
							setControlSpells();
							data.setArtifact(a);
							setArtifactNodes();
							ControlSpells.setOpacity(1);
	
				    		System.out.println("Mouse has entered this artifact node");
							ControlSpells.setOnMouseEntered(o -> {
								ControlSpells.setOpacity(1);
								System.out.println("Mouse has entered the Control Spells node");
							});
							ControlSpells.setOnMouseExited(m -> {	
								ControlSpells.setOpacity(0.0);
							});
						});
						b.setOnMouseExited(e -> {
							//ControlSpells.getChildren().clear();
							ControlSpells.setOpacity(0.0);
						});
						
	    				ArtifactSelectionWindow.getChildren().add(b);
	    				ArtifactSelectionWindow.setAlignment(Pos.CENTER_LEFT);
	    			}
	    		}
			catch (NullPointerException n) {
				System.out.println("This graphics asset was not found");
			}
    	}

    	
    	
    	public CKDrawerTab AllArtifacts() {
	    	//All artifacts available for the current player
    		
	    	ArtifactSelectionWindow = new HBox();
	    	ArtifactSelectionWindow.setPrefSize(400, 100);
//	    	ArtifactSelectionWindow.setPadding(new Insets(15, 12, 15, 12));
//	    	ArtifactSelectionWindow.setSpacing(10);
//	    	ArtifactSelectionWindow.setStyle("-fx-background-color: rgb(0, 20, 28)");
//	    	ArtifactSelectionWindow.setOpacity(0.2);
	    	
	    	CKDrawerTab allArtifacts = new CKDrawerTab(ArtifactSelectionWindow, DrawerSides.BOTTOM, 350.0, 720.0, 400.0, 100.0, "ckSnapInterpreter/sword.png");    	

	    	setAllArtifactsNodes();
	    	return allArtifacts;
    	}
    	
    	
    	public HBox ControlSpells() {
	    	//Controls
	    	ControlSpells = new HBox();
	    	ControlSpells.setPrefSize(400, 150);
	    	ControlSpells.setPadding(new Insets(15, 12, 15, 12));
	    	ControlSpells.setSpacing(10);
	    	//ControlSpells.setStyle("-fx-background-color: rgb(0, 20, 28)");
	    	ControlSpells.setOpacity(0.0);
	    	ControlSpells.setTranslateX(350);
	    	ControlSpells.setTranslateY(570);
	    	setControlSpells();
	    	return ControlSpells;
    	}
    	
    	
    	public void setControlSpells() {
			try {
				System.out.println(data.getArtifact().getName() + " is equipped with " + data.getArtifact().spellCount() + " spells");
	    		int aIndex = 0;
	    		ControlSpells.getChildren().clear();
//	        	data.registerArtifactObserver((artifact) -> {
//	        		
//	        	}

	    		for (Iterator<CKSpell> spells = data.getArtifact().getSpells(); spells.hasNext(); ) {
	    			CKSpell s = spells.next();
	    			if(s != null) {
	    				aIndex ++;
	    				System.out.println(aIndex + ": " + s.getIconID());
						Button b = new Button(s.getIconID(), new ImageView(s.getFXImage()));
	    				ControlSpells.getChildren().add(b);
	    				ControlSpells.setAlignment(Pos.CENTER_LEFT);
	    			}
	    		}
			}
			catch (NullPointerException n) {
				System.out.println("This graphics asset was not found");
			}
    	}


 
    public static void main(String[] args) {
    	
        launch(args);
    }
}


