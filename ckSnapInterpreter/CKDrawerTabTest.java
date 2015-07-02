package ckSnapInterpreter;

import ckSnapInterpreter.CKDrawerTab;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CKDrawerTabTest extends Application 
{

	 
    @Override
    public void start(Stage primaryStage) {

    	Pane menuPane = new Pane();
    	menuPane.setPrefSize(200, 200);
    		     
    	
    	Rectangle rectangle = new Rectangle(180,180);
    	rectangle.setFill(Color.RED);
    	CKDrawerTab d1 = new CKDrawerTab(rectangle, DrawerSides.LEFT, 0.0, 0.0, 250.0, 200.0, "ckSnapInterpreter/headshot.png");
    	
    	
    	Rectangle r3 = new Rectangle(180, 180);
    	r3.setFill(Color.YELLOW);
    	CKDrawerTab d3 = new CKDrawerTab(r3, DrawerSides.LEFT, 0.0, 300.0, 200.0, 200.0, "ckSnapInterpreter/silhouette.png");
    	d3.setOpenSize(40.0, 100.0);
    	
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		webEngine.load(getClass().getResource("CyberSnap/snap.html").toExternalForm());
    	CKDrawerTab d4 = new CKDrawerTab(browser, DrawerSides.RIGHT, 600.0, 0.0, 400.0, 200.0, "ckSnapInterpreter/text.png");
    	 
    	Rectangle r5 = new Rectangle(180,180);
    	r5.setFill(Color.ORANGE);
    	CKDrawerTab d5 = new CKDrawerTab(r5, DrawerSides.TOP, 300.0, 0.0, 200.0, 200.0, "ckSnapInterpreter/arrow.png");
    
    	Rectangle r6 = new Rectangle(180,180);
    	r6.setFill(Color.PURPLE);
    	CKDrawerTab d6 = new CKDrawerTab(r6, DrawerSides.BOTTOM, 300.0, 600.0, 200.0, 200.0, "ckSnapInterpreter/sword.png");
    	
    	menuPane.getChildren().addAll(d1, d3, d4, d5, d6);
        
        
        Scene scene = new Scene(menuPane,800,800, Color.AQUAMARINE);
        primaryStage.setTitle("Test Drawer Tabs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}




    