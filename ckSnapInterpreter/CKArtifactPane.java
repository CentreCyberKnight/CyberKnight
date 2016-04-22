package ckSnapInterpreter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class CKArtifactPane extends GridPane {
	

	//The selected artifact's enlarged image and description
	public CKArtifactPane(CKData data) {
    	//ArtifactDescriptionWindow = new GridPane();
    	this.setPrefSize(400, 300);
    	this.setPadding(new Insets(5));
    	this.setHgap(2);
    	this.setVgap(2);
    	this.setAlignment(Pos.CENTER);
    	this.setPrefSize(350, 300);
    	this.setStyle("-fx-background-color: rgb(217, 210, 240)");
    	this.setOpacity(0.5);
		data.registerArtifactObserver((artifact) -> 
		{
			setArtifactNodes(data);
		
		});
    	setArtifactNodes(data);
//    	CKDrawerTab artifact = new CKDrawerTab(ArtifactDescriptionWindow, DrawerSides.TOP, 350.0, 0.0, 400.0, 300.0, "ckSnapInterpreter/arrow.png");
//    	return artifact;
	}

	
	public void setArtifactNodes(CKData data) {
		this.getChildren().clear();
		//guardian if statement to ensure no null pointer exception
		if (data.getArtifact() == null) {
			return;
		}
    	Label title = new Label(data.getArtifact().getName());
     	title.setTextFill(Color.BLACK);
     	title.setFont(new Font("Comic Sans MS", 30));
     	title.setAlignment(Pos.TOP_CENTER);
     	this.add(title, 0, 0, 5, 1);
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
		
//    	data.registerArtifactObserver((artifact) -> 
//    	{ 
//    	try {
//    		
//    	}
//    	catch (JSException e) {
//    		System.out.println(e.getMessage());
//    	}
//    	});
		
		this.add(hnodes, 0, 1, 2, 1);
		this.setAlignment(Pos.CENTER);
	}
}