package ckSnapInterpreter;

import java.util.Vector;

import netscape.javascript.JSException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKTeam;

public class CKAllArtifactsPane extends HBox {
	
	public CKAllArtifactsPane(CKData data, CKControlSpellsPane controls) {

		this.setPrefSize(400, 100);
	//	ArtifactSelectionWindow.setPadding(new Insets(15, 12, 15, 12));
	//	ArtifactSelectionWindow.setSpacing(10);
	//	ArtifactSelectionWindow.setStyle("-fx-background-color: rgb(0, 20, 28)");
	//	ArtifactSelectionWindow.setOpacity(0.2);
		
		//CKDrawerTab allArtifacts = new CKDrawerTab(this, DrawerSides.BOTTOM, 350.0, 720.0, 400.0, 100.0, "ckSnapInterpreter/sword.png");    	
    	data.registerPlayerObserver((player) ->
    	{
    		try {
    			setAllArtifactsNodes(data, controls);
    			System.out.println(data.getPlayer().getName());
    			//System.out.println(data.getArtifact().getName());
    		}
    		catch (JSException e) {
    			System.out.println(e.getMessage());
    		}
    	});
		//setAllArtifactsNodes(data, controls);
	}

	public void setAllArtifactsNodes(CKData data, CKControlSpellsPane controls) {

		System.out.println("try statement" + data.getPlayer());
		// data.setArtifact(null);
		Vector<CKArtifact> arts = data.getPlayer().getTeam()
				.getArtifacts(data.getPlayer().getName());
		System.out.println(data.getPlayer().getName() + " is equipped with "
				+ arts.size() + " artifacts");
		int aIndex = 0;
		this.getChildren().clear();
		for (CKArtifact a : arts)
		{
			if (a != null) {
				aIndex++;
				System.out.println(aIndex + ": " + a.getIconId());
				Button b = new Button(a.getAID(), new ImageView(a.getFXImage()));

				b.setOnMouseEntered(e -> {
					// setControlSpells();
					data.setArtifact(a);
					// setArtifactNodes();
					controls.setOpacity(1);

					System.out.println("Mouse has entered this artifact node");
					controls.setOnMouseEntered(o -> {
						controls.setOpacity(1);
						System.out
								.println("Mouse has entered the Control Spells node");
					});
					controls.setOnMouseExited(m -> {
						controls.setOpacity(0.0);
					});
				});
				b.setOnMouseExited(e -> {
					controls.setOpacity(0.0);
				});

				this.getChildren().add(b);
				this.setAlignment(Pos.CENTER_LEFT);
			}				
    	}
	}
}
    	
   