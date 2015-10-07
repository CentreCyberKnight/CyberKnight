package ckSnapInterpreter;

import java.util.Iterator;

import netscape.javascript.JSException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import ckGameEngine.CKChapter;
import ckGameEngine.CKTeam;

public class CKPlayerStatsPane extends GridPane {
	
	
	public CKPlayerStatsPane(CKData data) {
		
		//PlayerStatsWindow = new GridPane();
		this.setPrefSize(400, 300);
		this.setPadding(new Insets(5));
		this.setHgap(2);
		this.setVgap(2);
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(350, 350);
		this.setStyle("-fx-background-color: rgb(217, 210, 240)");
		this.setOpacity(0.5);
    	data.registerPlayerObserver((player) ->
    	{
    		try {
    			setStats(data);
    		}
    		catch (JSException e) {
    			System.out.println(e.getMessage());
    		}
    	});
		//setStats(data);
//		CKDrawerTab stats = new CKDrawerTab(PlayerStatsWindow, DrawerSides.LEFT, 0.0, 470.0, 350.0, 350.0, "ckSnapInterpreter/text.png");

	}
	
	
    public void setStats(CKData data) {
    	this.getChildren().clear();
    	if(data.getPlayer() == null) {
    		return;
    	}
    	Label title = new Label(data.getPlayer().getName() + "'s Stats");
     	title.setTextFill(Color.BLACK);
     	title.setFont(new Font("Comic Sans MS", 30));
	    title.setAlignment(Pos.TOP_CENTER);
		this.add(title, 0, 0, 5, 1);
		this.setAlignment(Pos.TOP_CENTER);
		VBox skills = new VBox();  
		VBox skillsPts = new VBox();
		int aIndex= 0;
		for (Iterator<CKChapter> abilities = data.getPlayer().getAbilities().getChapters(); abilities.hasNext();) {
			CKChapter c = abilities.next();
			if( c != null) {
				aIndex ++;
				Label l = new Label(c.getName());
				l.setFont(new Font("Comic Sans MS", 15));
				Label value = new Label(Integer.toString((c.getValue())));
				value.setFont(new Font("Comic Sans MS", 15));
				System.out.println(aIndex + ". Stat: " + c.getName() + " has been printed");
				
				//skills.getChildren().add(l);
				skillsPts.getChildren().add(value);
			}
		}
		skills.getChildren().add(this.createStatIcons("fireicon.png"));
//		skills.getChildren().add(this.createStatIcons("ckSnapInterpreter/watericon.png"));
//		skills.getChildren().add(this.createStatIcons("ckSnapInterpreter/earthicon.png"));
		//skills.getChildren().add(this.createStatIcons("ckSnapInterpreter/windicon.png"));
		
	 	this.add(skills, 0, 1, 2, 1);
	 	this.add(skillsPts, 2, 1, 2, 1);
	}
    
    public Rectangle createStatIcons(String iconimage) {
    	Image image = new Image(iconimage);
    	Rectangle icon = new Rectangle(0, 0);
    	//open.relocate(x, y);
    	icon.setFill(new ImagePattern(image));
    	icon.setOpacity(0.5);
    	icon.setWidth(50);
    	icon.setHeight(50);
    	return icon;
    	
    }

}