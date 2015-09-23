package ckSnapInterpreter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKTeam;

public class CKSnapPane extends Pane {

	WebView BrowserWindow;
	
	public CKSnapPane(CKData data) {
		
		this.setPrefSize(690, 820);
		this.setPadding(new Insets(5));
		//this.setStyle("-fx-background-color: rgb(217, 210, 240)");
		//this.setOpacity(0.5);
		this.getChildren().add(getSnap(data));
	}
	
	//getters and setters of WebView
	
	
	public Node getSnap(CKData data) {

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
	}
	
}

