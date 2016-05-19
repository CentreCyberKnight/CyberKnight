package ckSnapInterpreter;

import ckGameEngine.CKGridActor;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CKPlayerIconPane extends HBox  {
	
    public CKPlayerIconPane(CKDataModel data) {
    	
    	this.setPrefSize(350, 170);
    	//this.setStyle("-fx-background-color: rgb(0, 20, 28)");
    	//this.setOpacity(0.2);

		setIcons(data);
	}

	//Icons of all the Characters available that the user can select
public void setIcons(CKDataModel data) {

	//CKDrawerTab icons = new CKDrawerTab(CharacterIcons, DrawerSides.LEFT, 0.0, 0.0, 350.0, 170.0, "ckSnapInterpreter/headshot.png");
	
		CKGridActor[] players = data.getTeam().getCharacters();
		System.out.println("This team is named " + data.getTeam().toString() + " and has " + data.getTeam().getCharacters().length + " players."); //team.tostring
		Effect effect = new DropShadow();//new Glow(.8);

		for (CKGridActor p : players ) 
			if(p != null) {
				
				try {
    				
					Button b = new Button(p.getName(), new ImageView(p.getFXPortrait()));
					b.setContentDisplay(ContentDisplay.TOP);
					b.setOnAction(e -> { data.setPlayer(p); });
					
					data.getActivePlayerProperty()
						.registerObserver(
							e->{
								if(e==p)
								{
									b.setEffect(effect);
									data.setPlayer(p);
									System.out.println("I was picked"+e);
								}
								else
								{
									b.setEffect(null);
								}
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