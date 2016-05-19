package ckSnapInterpreter;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

//import ckGui.CKDrawerTabTest;

public class CKDrawerTab extends Pane
{
	Rectangle openR;
	Rectangle close;
	Rectangle clip;
	Node contents;
	DrawerSides side;
	Double x;
	Double y;
	Double width;
	Double height;
	Image iconimage;


	public static String rectColor = "rgb(0,20,28)";
	public static Double rectOpacity = 0.2;
	
	
	TranslateTransition slideOut;
	TranslateTransition slideIn;

	TranslateTransition closeSlideOut;
	TranslateTransition closeSlideIn;

	FadeTransition ftHide;
	FadeTransition ftShow;

	PauseTransition pause;
	SequentialTransition seqT;

	// nodes instead of just image
	public CKDrawerTab(Node contents, DrawerSides side, Double x, Double y,
			Double w, Double h, String icon)
	{
		this.contents = contents;
		this.side = side;
		this.iconimage = new Image(icon);
		// this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,
		// null, null)));
		// this.setOpacity(0.8);
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.relocate(x, y);
		this.setPrefSize(width, height);
		clip = new Rectangle(0, 0, w, h);

		createOpen(iconimage);
		this.getChildren().addAll(contents);
		createClose();
		setPos();
		this.setClip(clip);
		this.setPickOnBounds(false);
		setContents();

	}

	// should resize the content node but doesn't work
	public void setContents()
	{
		// contents.widthProperty().bind(this.prefWidthProperty());
		// contents.heightProperty().bind(this.prefHeightProperty());
		// contents.resize(width, height);
		// contents.relocate(width/4, height/4);

	}

	public void setPos()
	{
		switch (side)
		{
		case TOP:
			openR.relocate((width / 2) - (openR.getWidth() / 2), 0);
			contents.relocate(0.0, -height);
			close.relocate(0.0, -close.getHeight());
			this.setSlides(0.0, height);
			break;
		case BOTTOM:
			openR.relocate((width / 2) - (openR.getWidth() / 2),
					height - openR.getHeight());
			contents.relocate(0.0, height);
			// close.relocate(width - 30, height);
			close.relocate(0.0, height);
			this.setSlides(0.0, -height);
			break;
		case LEFT:
			openR.relocate(0, 0);
			contents.relocate(-width, 0);
			// contents.relocate(-((this.width/2)+ (this.width/4)),
			// this.height/4);
			close.relocate(-close.getWidth(), 0);
			this.setSlides(width, 0.0);
			// System.out.println("setPos");
			break;
		case RIGHT:
			openR.relocate(width - openR.getWidth(), 0);
			contents.relocate(width, 0);
			close.relocate(width, 0);
			this.setSlides(-width, 0.0);
			break;
		default:
			break;
		}
	}

	// add interpolator...bouncy
	public void setSlides(Double xMove, Double yMove)
	{
		// this.xMove = xMove;
		// this.yMove = yMove;

		slideOut = new TranslateTransition(Duration.seconds(.5), contents);
		slideIn = new TranslateTransition(Duration.seconds(.5), contents);


		slideOut.setByX(xMove);
		slideOut.setByY(yMove);
		slideOut.setInterpolator(Interpolator.EASE_OUT);

		slideIn.setByX(-xMove);
		slideIn.setByY(-yMove);
		slideIn.setInterpolator(Interpolator.EASE_IN);
		
		
		
		closeSlideOut = new TranslateTransition(Duration.seconds(.5), close);
		closeSlideIn = new TranslateTransition(Duration.seconds(.5), close);

		closeSlideIn.setByX(-xMove);
		closeSlideIn.setByY(-yMove);
		closeSlideIn.setInterpolator(Interpolator.EASE_IN);

		closeSlideOut.setByX(xMove);
		closeSlideOut.setByY(yMove);
		closeSlideOut.setInterpolator(Interpolator.EASE_OUT);
		
		
		
		ftHide = new FadeTransition(Duration.seconds(0.1), openR);
		ftShow = new FadeTransition(Duration.seconds(0.6), openR);

		ftHide.setToValue(0.0);
		ftShow.setToValue(0.5);

		
		pause = new PauseTransition(Duration.seconds(0.8));
		seqT = new SequentialTransition(pause, ftShow);


		openR.setOnMouseClicked(e -> {
			openDrawer(true);
		});

		close.setOnMouseClicked(o -> {
			openDrawer(false);
		});
	}

	

	public void openDrawer(boolean open)
	{
		/*
		 * TranslateTransition slideOut = new TranslateTransition(
		 * Duration.seconds(.5), contents); TranslateTransition slideIn = new
		 * TranslateTransition(Duration.seconds(.5), contents);
		 * 
		 * TranslateTransition closeSlideOut = new TranslateTransition(
		 * Duration.seconds(.5), close); TranslateTransition closeSlideIn = new
		 * TranslateTransition( Duration.seconds(.5), close);
		 * 
		 * FadeTransition ftHide = new FadeTransition(Duration.seconds(0.1),
		 * openR); FadeTransition ftShow = new
		 * FadeTransition(Duration.seconds(0.6), openR);
		 * 
		 * PauseTransition pause = new PauseTransition(Duration.seconds(0.8));
		 * SequentialTransition seqT = new SequentialTransition(pause, ftShow);
		 */
		if (open)
		{
			slideOut.play();
			ftHide.play();
			closeSlideOut.play();
		} else
		{
			slideIn.play();
			seqT.play();
			closeSlideIn.play();
		}
	}

	public void createOpen(Image iconimage)
	{
		this.openR = new Rectangle(0, 0, Color.web(rectColor, rectOpacity));
		// open.relocate(x, y);
		openR.setFill(new ImagePattern(iconimage));
		// open.setOpacity(0.5);
		openR.setWidth(50);
		openR.setHeight(50);

		getChildren().add(openR);
	}

	public void setOpenSize(Double w, Double h)
	{
		openR.setWidth(w);
		openR.setHeight(h);
	}

	public Rectangle getOpen()
	{
		return openR;
	}

	public void createClose()
	{
		this.close = new Rectangle(0, 0, Color.web(rectColor, rectOpacity));
		// close.relocate(x, y);
		close.setWidth(20);
		close.setHeight(20);
		close.setFill(Color.web(rectColor));
		close.setOpacity(0.5);

		getChildren().add(close);
	}

}
