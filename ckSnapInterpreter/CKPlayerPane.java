package ckSnapInterpreter;

import netscape.javascript.JSException;
import ckGameEngine.CKTeam;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class CKPlayerPane extends GridPane {

	public CKPlayerPane(CKData data) {
		//PlayerDescriptionWindow = new GridPane();
		this.setPadding(new Insets(5));
		this.setHgap(2);
		this.setVgap(2);
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(350, 300);
		this.setStyle("-fx-background-color: rgb(217, 210, 240)");
		this.setOpacity(0.5);
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
 	Label cp = new Label("CyberPoints: " + Integer.toString(cyberpts));
 	cp.setTextFill(Color.BLACK);
 	cp.setFont(new Font("Courier New", 20));

 	this.add(cp, 2, 4, 3, 1);
	}

}