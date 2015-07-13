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

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import ckCommonUtils.CKPosition;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetButton;
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
import ckGameEngine.QuestData;
import ckGameEngine.actions.CKSimpleGUIAction;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckPythonInterpreter.CKCharacterView;
import ckPythonInterpreter.CKTeamView;
import ckSatisfies.PositionReachedSatisfies;
import ckSatisfies.Satisfies;
import ckSatisfies.TrueSatisfies;
import ckSnapInterpreter.CKDrawerTab;
import ckTrigger.CKTrigger;
import ckTrigger.TriggerResult;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CKUI extends Application 
{
	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	 
	HBox ArtifactControls;
	HBox CharacterIcons;
	Rectangle PlayerStatsWindow;
	WebView BrowserWindow;
	Rectangle PlayerDescriptionWindow;
	Rectangle ArtifactDescriptionWindow;
	HBox ArtifactSelectionWindow;
	CKData data;
	QuestData q;
	CKTeam team;
	Vector<CKGridActor> actors;
	CKFXImageGallery images = new CKFXImageGallery();
	boolean found;
	
	
	FadeTransition ftShowControls = new FadeTransition(Duration.seconds(0.3), ArtifactControls);
	FadeTransition ftHideControls = new FadeTransition(Duration.seconds(0.3), ArtifactControls);
	PauseTransition p = new PauseTransition(Duration.seconds(0.3));
	SequentialTransition stControls = new SequentialTransition (p, ftShowControls);
	

	
    @Override
    public void start(Stage primaryStage) {

    	//The main pane that all DrawerTab nodes are added onto
    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
    	populateModel();
		//this adds all the CKDrawerTabs as the main pane's children
		menuPane.getChildren().addAll(Icons(), Player(), Snap(), AllArtifacts(), Artifact(), ArtifactControls(), Stats());
		
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
		
		//add them
		
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
	    
	    data = new CKData(mom, combatBoots, spell);
	    data.setTeam(team);
	   // data.setPlayer(mom);
//	    data.setArtifact(combatBoots);
//	    data.setSpell(spell);
		
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
    					CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getPortrait(p.getAssetID());
	    				System.out.println(pIndex + ": " + p.getName() + " with the assetId of " + p.getAssetID());
						Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
						images.add(new CKFXImage(image, p.getName()));
						Button b = new Button(p.getAssetID(), new ImageView(image));
						b.setOnAction(e -> {
							data.setPlayer(p);
							setArtifactNodes();
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
		PlayerDescriptionWindow = new Rectangle(350, 300);
    	PlayerDescriptionWindow.setFill(Color.web(rectColor));
    	PlayerDescriptionWindow.setOpacity(0.2);
    	CKDrawerTab player = new CKDrawerTab(PlayerDescriptionWindow, DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0, "ckSnapInterpreter/silhouette.png");
    	player.setOpenSize(40.0, 100.0);
    	return player;
    }
    	
    	//Player Statistics
    	public CKDrawerTab Stats() {
	    	PlayerStatsWindow = new Rectangle(350, 350);
	    	PlayerStatsWindow.setFill(Color.web(rectColor));
	    	PlayerStatsWindow.setOpacity(0.2);
	    	CKDrawerTab stats = new CKDrawerTab(PlayerStatsWindow, DrawerSides.LEFT, 0.0, 470.0, 350.0, 350.0, "ckSnapInterpreter/text.png");
	    	return stats;
    	}
    	
    	//Snap!
    	public CKDrawerTab Snap() {
			BrowserWindow = new WebView();
			WebEngine webEngine = BrowserWindow.getEngine();
			BrowserWindow.setPrefSize(690, 820);
			webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
	    	CKDrawerTab snap = new CKDrawerTab(BrowserWindow, DrawerSides.RIGHT, 750.0, 0.0, 690.0, 820.0, "ckSnapInterpreter/text.png");
	    	return snap;
    	}
	    	
	    	
    	//The selected artifact's enlarged image and description
    	public CKDrawerTab Artifact() {
	    	ArtifactDescriptionWindow = new Rectangle(400,300);
	    	ArtifactDescriptionWindow.setFill(Color.web(rectColor));
	    	ArtifactDescriptionWindow.setOpacity(0.2);
	    	CKDrawerTab artifact = new CKDrawerTab(ArtifactDescriptionWindow, DrawerSides.TOP, 350.0, 0.0, 400.0, 300.0, "ckSnapInterpreter/arrow.png");
	    	return artifact;
    	}
    	

    	
    	//returns a vector of fx images of players, artifacts, and spells
    	public CKFXImageGallery getFXImageGallery() {
    		return images;
    	}
    		
    	public void setArtifactNodes() {
			try {
	    		Vector<CKArtifact> arts = team.getArtifacts(data.getPlayer().getName());
	    		System.out.println(data.getPlayer().getName() + " is equipped with " + arts.size() + " artifacts");
	    		int aIndex = 0;
	    		for (CKArtifact a : arts) 
	    			if(a != null) {
	    				aIndex ++;
	    				CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(a.getIconId());
	    				System.out.println(aIndex + ": " + a.getIconId());
						Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
						images.add(new CKFXImage(image, a.getName()));
						Button b = new Button(a.getAID(), new ImageView(image));
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

	    	//hover enter effect
	    	ArtifactSelectionWindow.setOnMouseEntered(e -> {
	    		System.out.println("Mouse has entered this node");
	    		ftShowControls.setFromValue(0.0);
	    		ftShowControls.setToValue(0.5);
	    		stControls.play();
	    	});    	
	    	
//	    	if (ArtifactControls.isHover()) 
//	    	{
//	    		System.out.println("Mouse has entered Artifact Controls");
//	    		ftShowControls.setToValue(0.5);
//	    		stControls.play();
//	    	}
//	    	else 
//	        	ArtifactSelectionWindow.setOnMouseExited(e -> {
//	        		
//	        		System.out.println("Mouse has exited");
//	        		ftHideControls.setFromValue(0.5);
//	        		ftHideControls.setToValue(0.0);
//	        		ftHideControls.play();
//	        	});

    	//hover to make the controls stay
	    	setArtifactNodes();
	    	return allArtifacts;
    	}
    	
    	
    	public HBox ArtifactControls() {
	    	//Controls
	    	ArtifactControls = new HBox();
	    	ArtifactControls.setPrefSize(400, 150);
	    	ArtifactControls.setPadding(new Insets(15, 12, 15, 12));
	    	ArtifactControls.setSpacing(10);
	    	ArtifactControls.setStyle("-fx-background-color: rgb(0, 20, 28)");
	    	ArtifactControls.setOpacity(0.0);
	    	ArtifactControls.setTranslateX(350);
	    	ArtifactControls.setTranslateY(520);
	    	return ArtifactControls;
    	}
    	
//    	public void Transitions() {
//	    	//transitions for artifacts and controls
//	    	ftShowControls = new FadeTransition(Duration.seconds(0.3), ArtifactControls);
//	    	ftHideControls = new FadeTransition(Duration.seconds(0.3), ArtifactControls);
//	    	p = new PauseTransition(Duration.seconds(0.3));
//			stControls = new SequentialTransition (p, ftShowControls);
//	    	
//	    	//hover enter effect
//	    	ArtifactSelectionWindow.setOnMouseEntered(e -> {
//	    		System.out.println("Mouse has entered this node");
//	    		ftShowControls.setFromValue(0.0);
//	    		ftShowControls.setToValue(0.5);
//	    		stControls.play();
//	    	});
//	    	
//	    	
//	    	//if Artifact Controls are hovered, e-> asldjfslkd. else 
//	    	{
//	    	if (ArtifactControls.isHover()) 
//	    	{
//	    		System.out.println("Mouse has entered Artifact Controls");
//	    		ftShowControls.setToValue(0.5);
//	    		stControls.play();
//	    	}
//	    	
//	    	
//	    	else 
//	        	{ArtifactSelectionWindow.setOnMouseExited(e -> {
//	        		
//	        		System.out.println("Mouse has exited");
//	        		ftHideControls.setFromValue(0.5);
//	        		ftHideControls.setToValue(0.0);
//	        		ftHideControls.play();
//	        	});}
//	    	}
//    	}
//    	
		//hover exit effect

    	

 
    public static void main(String[] args) {
    	
        launch(args);
    }
}




    