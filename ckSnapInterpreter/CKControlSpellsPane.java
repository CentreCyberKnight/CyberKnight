package ckSnapInterpreter;

import java.util.Iterator;

import netscape.javascript.JSObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpell;
import ckGameEngine.CKTeam;

public class CKControlSpellsPane extends HBox {

	
	public CKControlSpellsPane(CKData data) {

    	this.setPrefSize(400, 150);
    	this.setPadding(new Insets(15, 12, 15, 12));
    	this.setSpacing(10);
    	//ControlSpells.setStyle("-fx-background-color: rgb(0, 20, 28)");
    	this.setOpacity(0.0);
    	this.setTranslateX(350);
    	this.setTranslateY(570);
		data.registerArtifactObserver((artifact) -> 
		{
			setControlSpells(data);
		
		});
    	setControlSpells(data);
	}
	

	public void setControlSpells(CKData data) {
		try {
			System.out.println(data.getArtifact().getName() + " is equipped with " + data.getArtifact().spellCount() + " spells");
    		int aIndex = 0;
    		this.getChildren().clear();

    		for (Iterator<CKSpell> spells = data.getArtifact().getSpells(); spells.hasNext(); ) {
    			CKSpell s = spells.next();
    			if(s != null) {
    				aIndex ++;
    				System.out.println(aIndex + ": " + s.getIconID());
					Button b = new Button(s.getIconID(), new ImageView(s.getFXImage()));
					
					b.setOnAction(new EventHandler<ActionEvent>() {
		    			@Override public void handle (ActionEvent e) {
		    				WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		    	    		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		    	    		jsobj.setMember("artifactName", data.getArtifact().getName());
		    	    		jsobj.setMember("spellName", s.getName());
		    	    		jsobj.setMember("java", new CKjsDebugger());
		    	    		webEngine.executeScript("ide.executeScript()");
		    			}
		    		});
					
    				this.getChildren().add(b);
    				this.setAlignment(Pos.CENTER_LEFT);
    			}
    		}
		}
		catch (NullPointerException n) {
			System.out.println("This graphics asset was not found");
		}
	}

	
	
}