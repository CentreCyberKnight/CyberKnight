package ckSnapInterpreter;

import static ckCommonUtils.CKPropertyStrings.MAX_CP;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import netscape.javascript.JSException;

public class CKPlayerPane extends GridPane {

	public CKPlayerPane(CKData data) {
		//PlayerDescriptionWindow = new GridPane();
		this.setPadding(new Insets(5));
		this.setHgap(2);
		this.setVgap(2);
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(350, 300);
		this.setStyle("-fx-background-color: rgba(217, 210, 240,.8)");
		//this.setOpacity(0.5);
    	data.registerPlayerObserver((player) ->
    	{
    		try {
    			setPlayerNodes(data);
    		}
    		catch (JSException e) {
    			System.out.println(e.getMessage());
    		}
    	});
		//setPlayerNodes(data);
//		CKDrawerTab player = new CKDrawerTab(PlayerDescriptionWindow, DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0, "ckSnapInterpreter/silhouette.png");
//		player.setOpenSize(40.0, 100.0);
		
	}

public void setPlayerNodes(CKData data) {
	Rectangle imageRect = new Rectangle(180, 250);
	imageRect.setFill(new ImagePattern(data.getPlayer().getFXImage()));
	this.getChildren().clear();
	this.add(imageRect, 0, 0, 2, 5);
 	//PlayerDescriptionWindow.setValignment(imageRect, VPos.CENTER);
 	Label playername = new Label(data.getPlayer().getName());
 	playername.setTextFill(Color.BLACK);
 	playername.setFont(new Font("Comic Sans MS", 30));
 	this.add(playername, 2, 3, 2, 1);
 	int cyberpts = data.getPlayer().getCyberPoints();
 	Label cpText = new Label("CP:   ");

 	cpText.setTextFill(Color.BLACK);
 	cpText.setFont(new Font("Courier New", 20));
 	
 	Label cp = new Label();//"CyberPoints: " + Integer.toString(cyberpts));
 	cp.textProperty().bind(data.getPlayer().getCyberPointsProperty().asString());

 	cp.setTextFill(Color.BLACK);
 	cp.setFont(new Font("Courier New", 20));
 	
 	
 	Label cpMax = new Label("/"+data.getPlayer().getAbilities().getChapter(MAX_CP).getValue());

 	cpMax.setTextFill(Color.BLACK);
 	cpMax.setFont(new Font("Courier New", 20));

 	
 	HBox cpPane = new HBox();
 	cpPane.getChildren().addAll(cpText,cp,cpMax);
 	
 	this.add(cpPane, 2, 4, 3, 1);
	}

}