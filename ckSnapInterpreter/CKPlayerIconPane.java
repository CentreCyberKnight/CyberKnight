package ckSnapInterpreter;

import netscape.javascript.JSException;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKTeam;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class CKPlayerIconPane extends HBox  {
	
    public CKPlayerIconPane(CKData data) {
    	
    	this.setPrefSize(350, 170);
    	//this.setStyle("-fx-background-color: rgb(0, 20, 28)");
    	//this.setOpacity(0.2);

		setIcons(data);
	}

	//Icons of all the Characters available that the user can select
public void setIcons(CKData data) {

	//CKDrawerTab icons = new CKDrawerTab(CharacterIcons, DrawerSides.LEFT, 0.0, 0.0, 350.0, 170.0, "ckSnapInterpreter/headshot.png");
	
		CKGridActor[] players = data.getTeam().getCharacters();
		System.out.println("This team is named " + data.getTeam().toString() + " and has " + data.getTeam().getCharacters().length + " players."); //team.tostring
		int pIndex= 0;
		for (CKGridActor p : players ) 
			if(p != null) {
				pIndex ++;
				try {
    				System.out.println(pIndex + ": " + p.getName() + " with the assetId: " + p.getAssetID());
					Button b = new Button(p.getAssetID(), new ImageView(p.getFXPortrait()));
					b.setOnAction(e -> {	
						data.setPlayer(p);
						
//						setAllArtifactsNodes();
//						ControlSpells.getChildren().clear();
					//	setControlSpells();
//						setPlayerNodes();
//						setStats();
//						setArtifactNodes();
					//take these out when observers are added
					});
    				this.getChildren().add(b);
    				this.setAlignment(Pos.CENTER);
				}
				catch (NullPointerException n) {
					System.out.println("Character Icon asset was not found" + n);
				}	
				
			}
		//return this;
	}
}