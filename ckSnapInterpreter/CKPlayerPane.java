package ckSnapInterpreter;


import static ckCommonUtils.CKPropertyStrings.MAX_CP;
import javafx.application.Platform;
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
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKStatsChangeListener;

public class CKPlayerPane extends GridPane implements CKStatsChangeListener
{

	CKGridActor current = null;
	CKDataModel data = null;

	public CKPlayerPane(CKDataModel data)
	{
		this.data = data;
		// PlayerDescriptionWindow = new GridPane();
		this.setPadding(new Insets(5));
		this.setHgap(2);
		this.setVgap(2);
		this.setAlignment(Pos.CENTER);
		this.setPrefSize(350, 300);
		this.setStyle("-fx-background-color: rgba(217, 210, 240,.8)");
		// this.setOpacity(0.5);
		data.registerPlayerObserver((player) -> {
			try
			{
				setPlayerNodes(false);
			} catch (JSException e)
			{
				System.out.println(e.getMessage());
			}
		});
		
		// setPlayerNodes(data);
		// CKDrawerTab player = new CKDrawerTab(PlayerDescriptionWindow,
		// DrawerSides.LEFT, 0.0, 170.0, 350.0, 300.0,
		// "ckSnapInterpreter/silhouette.png");
		// player.setOpenSize(40.0, 100.0);

	}

	public void setPlayerNodes(boolean force)
	{

		CKGridActor actor = data.getPlayer();

		if (current == actor && ! force)
		{
			return;
		}
		
		if (current!=actor)
		{
			if(current != null)
			{
				current.removeListener(this);
			}
			actor.addListener(this);
		}

		Rectangle imageRect = new Rectangle(180, 250);
		this.getChildren().clear();
		this.add(imageRect, 0, 0, 2, 5);
		
		current=actor;
		if (actor == null)
		{
			return;
		}
		imageRect.setFill(new ImagePattern(data.getPlayer().getFXImage()));

		Label playername = new Label(data.getPlayer().getName());
		playername.setTextFill(Color.BLACK);
		playername.setFont(new Font("Comic Sans MS", 30));
		this.add(playername, 2, 3, 2, 1);
		int cyberpts = current.getCyberPoints();
		Label cpText = new Label("CP:   ");

		cpText.setTextFill(Color.BLACK);
		cpText.setFont(new Font("Courier New", 20));

		Label cp = new Label();// "CyberPoints: " + Integer.toString(cyberpts));
		cp.textProperty().set("" + cyberpts); // bind(data.getPlayer().getCyberPointsProperty().asString());

		cp.setTextFill(Color.BLACK);
		cp.setFont(new Font("Courier New", 20));

		Label cpMax = new Label("/"
				+ data.getPlayer().getAbilities().getChapter(MAX_CP).getValue());

		cpMax.setTextFill(Color.BLACK);
		cpMax.setFont(new Font("Courier New", 20));

		HBox cpPane = new HBox();
		cpPane.getChildren().addAll(cpText, cp, cpMax);

		this.add(cpPane, 2, 4, 3, 1);
	}

	// /these will be called from non FX threads..
	@Override
	public void equippedChanged()
	{
		redraw(true);
	}

	@Override
	public void statsChanged(CKBook stats)
	{
		redraw(true);
	}

	@Override
	public void cpChanged(int cp)
	{
		redraw(true);
	}

	public void redraw(boolean force)
	{
		if (Platform.isFxApplicationThread())
		{
			this.setPlayerNodes(force);
		} else
		{
			Platform.runLater(new Runnable()
			{

				@Override
				public void run()
				{
					setPlayerNodes(force);
				}
			}

			);

		}

	}

}
