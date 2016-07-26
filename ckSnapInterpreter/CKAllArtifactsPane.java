package ckSnapInterpreter;

import java.util.Vector;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKStatsChangeListener;


public class CKAllArtifactsPane extends VBox implements CKStatsChangeListener
{

	private CKDataModel data;
	private CKControlSpellsPane controls;
	private CKGridActor current;
	
	public CKAllArtifactsPane(CKDataModel data, CKControlSpellsPane controls)
	{
		this.data = data;
		this.controls = controls;

		this.setPrefSize(400, 110);

		data.registerPlayerObserver((player) -> {
			setAllArtifactsNodes(false);
		});
		// setAllArtifactsNodes(data, controls);
	}

	public void setAllArtifactsNodes(boolean force) 
	{
		CKGridActor actor = data.getPlayer();
		
		if(current==actor && ! force) { return;}
		
		if (current != null) { current.removeListener(this); }
		
		current = actor;
		this.getChildren().clear();
		
		if(actor==null) { return; }
		
		current.addListener(this);
		
		
		Vector<CKArtifact> arts = current.getTeam()
				.getArtifacts(current.getName());

//		int aIndex = 0;

		if(arts.size() >0)
		{
			data.setArtifact(arts.get(0));
		}
		
		for (CKArtifact a : arts)
		{
			if (a != null)
			{
//				aIndex++;
//				System.out.println(aIndex + ": " + a.getIconId());
				Button b = new Button(a.getAID(), new ImageView(a.getFXImage()));
				b.setContentDisplay(ContentDisplay.TOP);
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

	@Override
	public void equippedChanged()
	{
		redraw(true);
		
	}

	@Override
	public void statsChanged(CKBook stats)
	{
		//no action!!
	}

	@Override
	public void cpChanged(int cp)
	{
		// no action!!
	}
	
	
	public void redraw(boolean force)
	{
		if (Platform.isFxApplicationThread())
		{
			this.setAllArtifactsNodes(force);
		} else
		{
			Platform.runLater(new Runnable()
			{

				@Override
				public void run()
				{
					setAllArtifactsNodes(force);
				}
			}

			);

		}
		
	
	}
}
