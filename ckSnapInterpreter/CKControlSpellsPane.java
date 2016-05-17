package ckSnapInterpreter;

import static ckGameEngine.CKGameObjectsFacade.getQuest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import netscape.javascript.JSObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import ckDatabase.CKConnection;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpell;
import ckGameEngine.Quest;
import ckPythonInterpreter.CKPlayerObjectsFacade;

public class CKControlSpellsPane extends HBox {

	
	public CKControlSpellsPane(CKDataModel data) {

    	this.setPrefSize(400, 150);
    	this.setPadding(new Insets(15, 12, 15, 12));
    	this.setSpacing(10);
    	//ControlSpells.setStyle("-fx-background-color: rgb(0, 20, 28)");
    	this.setOpacity(0.0);
    	//this.setTranslateX(350);
    	//this.setTranslateY(420);
    	
    	
    	
		data.registerArtifactObserver((artifact) -> 
		{
			setControlSpells(data);
		
		});
    	setControlSpells(data);
	}
	

	public void setControlSpells(CKDataModel data) 
	{
		
		if(data.getArtifact()==null)
		{return;}
		
		try {
			//System.out.println(data.getArtifact().getName() + " is equipped with " + data.getArtifact().spellCount() + " spells");
    		int aIndex = 0;
    		this.getChildren().clear();

    		for (Iterator<CKSpell> spells = data.getArtifact().getSpells(); spells.hasNext(); ) {
    			CKSpell s = spells.next();
    			if(s != null) {
    				aIndex ++;
    				System.out.println(aIndex + ": " + s.getIconID());
					Button b = new Button(s.getIconID(), new ImageView(s.getFXImage()));
					b.setContentDisplay(ContentDisplay.TOP);
					b.setOnAction(new EventHandler<ActionEvent>() {
		    			@Override public void handle (ActionEvent e) {
		    				
		    				//if data.getPlayer == getCurrentPlayer we are OK.
		    				//set by turn controller.
		    				if (CKGameObjectsFacade.getCurrentPlayer() != data.getPlayer())
		    				{
		    					return; //this button should not work for you....		    					
		    				}

		    				
		    				WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
		    	    		JSObject jsobj = (JSObject) webEngine.executeScript("window");
		    	    		
		    	    		//store the snap configuration to disk
		    				 File f = new File(CKConnection.getCKSettingsDirectory(),"snapConfig.xml");
		    				 XMLStore xml = new XMLStore(f);
			    	    		
			   				 jsobj.setMember("exportXML", xml);
			   				 webEngine.executeScript("ide.ckExportXML()");
			   				 
		    				 /*try(  PrintWriter out = new PrintWriter(f)  ){
		    					    out.println(xml );
		    					} catch (FileNotFoundException e1)
								{
									e1.printStackTrace();
								}
*/
		    				 
		    				 
		    				 
		    				CKPlayerObjectsFacade.setArtifact(data.getArtifact());
		    				CKGameObjectsFacade.setCurrentPlayer(data.getPlayer());
		    				CKPlayerObjectsFacade.calcCPTurnLimit();

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

	static public class XMLStore
	{
		File toF;
		
		public XMLStore(File f)
		{
			toF = f;			
		}
		
		public void store(String xml)
		{
			
			try(PrintWriter out = new PrintWriter(toF)  )
			{
			    out.println(xml );
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			
		
		}
	}
	}
	
