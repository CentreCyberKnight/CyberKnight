package ckSnapInterpreter;


import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class controlPanel extends Application // implements EventHandler<Action
												// Event>
{


	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("A Control Panel");

		GridPane grid = new GridPane();
		grid.setHgap(30);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));

		CKArtifactFactory aFactory = CKArtifactFactory.getInstance();
		CKArtifact combatBoots = aFactory.getAsset("combatBoots");
		CKArtifact coolSpoon = aFactory.getAsset("coolSpoon");

		
		
		Button a1 = new Button();
		a1.setText("Combat Boots");
		
		Button a2 = new Button();
		a2.setText("Spoon");
		
		/*
		Button a3 = new Button();
		a3.setText("Wind Wand");
		
		Button a4 = new Button();
		a4.setText("Fire Wand");
		
		Button a5 = new Button();
		a5.setText("Execute Script");
		*/
		
		TextField a6 = new TextField();

		a1.setOnAction(e -> {CKArtifactModel.setArtifact(combatBoots);});
		a2.setOnAction(e -> {CKArtifactModel.setArtifact(coolSpoon);});	
		
		//CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.readAssetFromXMLDirectory("boots");
		//System.out.println(asset);
		
		
		WebView browser = new WebView();
        CKGameObjectsFacade.setWebEngine(browser.getEngine());
        WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		
		// Display a local webpage
        
		webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		jsobj.setMember("javaProcess", a6);
		jsobj.setMember("java", new CKjsDebugger());
		/*
		a1.setOnAction(e -> {webEngine.executeScript("ide.domino('Boot', new List(['combatBoot.png', 'leftArrow.png', 'rightArrow.png']))");});
		a2.setOnAction(e -> {webEngine.executeScript("ide.domino('Wand', new List(['wandIcon.png', 'sparkles.png', 'vase_overTile.png', 'target.png']))");});
		a3.setOnAction( e -> {webEngine.executeScript("ide.hideBlock({Shock : true, Storm : true, Revive : true})");});
		a4.setOnAction( e -> {webEngine.executeScript("ide.hideBlock({Inferno: true, Ignite: true, Fusion: true, FireEat: true})");});
		a5.setOnAction( e -> {webEngine.executeScript("ide.fire('Boot', 1)");});
		*/
		
		grid.add(a1, 0, 0);
		grid.add(a2, 0, 1);
		/*
		grid.add(a3, 0, 2);
		grid.add(a5, 0, 3);
		*/
		grid.add(a6, 0, 4);
		grid.add(browser, 2, 0, 4, 5);

		Scene scene = new Scene(grid, 1000, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
