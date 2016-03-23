package ckSnapInterpreter;

import java.util.Iterator;

import netscape.javascript.JSException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import ckGameEngine.CKChapter;
import ckGameEngine.CKTeam;

public class CKAddedAbilitiesPane extends VBox {
	
   	//The selected artifact's enlarged image and description
	public CKAddedAbilitiesPane(CKDataModel data) {
    	//AddedAbilitiesWindow = new VBox();
    	this.setPrefSize(400, 295);
    	this.setPadding(new Insets(5));
    	this.setPadding(new Insets(15, 12, 15, 12));
    	this.setSpacing(10);
    	this.setStyle("-fx-background-color: rgb(217, 210, 240)");
    	this.setOpacity(.7);
//    	AddedAbilitiesWindow.setTranslateX(350);
//    	AddedAbilitiesWindow.setTranslateY(300);
    	data.registerArtifactObserver((artifact) -> 
    	{ 
	    	try {
	    		setAddedAbilityNodes(data);
	    	}
	    	catch (JSException e) {
	    		System.out.println(e.getMessage());
	    	}
    	});
    	//setAddedAbilityNodes(data);
//    	CKDrawerTab abilities = new CKDrawerTab(this, DrawerSides.RIGHT, 350.0, 300.0, 400.0, 295.0, "ckSnapInterpreter/arrow.png");
    	
	}
	
	
	//export blocks here
	public void setAddedAbilityNodes(CKDataModel data) {
		this.getChildren().clear();
    	Label title = new Label("Added Abilities");
     	title.setTextFill(Color.BLACK);
     	title.setFont(new Font("Comic Sans MS", 30));
     	title.setAlignment(Pos.TOP_CENTER);
     	this.getChildren().add(title);
    	HBox hnodes = new HBox();
    	Rectangle imageRect = new Rectangle(160, 160);
    	
    	if(data.getArtifact()==null){
    		System.out.println("artifact is null");}
    	//System.out.println(data.getPlayer().getName());

//   ADDED PROTECTION FOR NULL ARTIFACT
    	
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
				//System.out.println("stat: " + c.getName() + " has been printed");
				addedAbs.getChildren().add(l);
			}
		}

		
		
		this.getChildren().addAll(addedAbs);
		this.setAlignment(Pos.CENTER);
	}
}