package ckCommonUtils;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ckGraphicsEngine.FX2dGraphicsEngine;


public class TransparentEffect
{
	protected FX2dGraphicsEngine engine;
	protected CKPosition origin;
	protected int radius;
	protected Scene scene;
	
	public TransparentEffect(FX2dGraphicsEngine engine,CKPosition origin, int radius, Scene scene)
	{
		this.engine=engine;	
		this.origin=origin;
		this.radius=radius;
		this.scene=scene;
	}
	
	public Group DrawOnScene(){
		Group root=new Group();
		Circle c=new Circle(radius,Color.web("white",0.2));
		//Not the right position, need to come back later
		c.setCenterX(origin.getX());
		c.setCenterY(origin.getY());
		c.setStrokeType(StrokeType.OUTSIDE);
		c.setStroke(Color.web("white", 0.1));
        c.setStrokeWidth(10);
        c.setEffect(new BoxBlur(5, 5, 3));
        //c.radiusProperty().bind(scene.widthProperty());;
        //c.setEffect(new Glow());
        //c.centerXProperty().bind(scene.widthProperty().divide(2));
        //c.centerYProperty().bind(scene.heightProperty().divide(2));
        Rectangle background=new Rectangle(scene.getWidth(), scene.getHeight(), Color.BLACK);
        background.widthProperty().bind(scene.widthProperty());
        background.heightProperty().bind(scene.heightProperty());
        
        /*//create the clip
        Rectangle clip=new Rectangle(200,200);
        background.setClip(clip);*/
        
        Group newgroup=new Group(background,c);
        Group blendmodegroup=new Group(newgroup,engine);
        engine.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendmodegroup);
		return root;
	}
}