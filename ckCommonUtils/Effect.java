package ckCommonUtils;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Effect extends Application{
	@Override
	   public void start(final Stage stage) throws Exception
	   {
	      Group rootGroup = new Group();
	      Scene scene =new Scene(rootGroup, 800, 400);
	      
	      Circle[] circles=new Circle[5];
	      Circle c1=new Circle();
	      Circle c2=new Circle();
	      Circle c3=new Circle();
	      Circle c4=new Circle();
	      Circle c5=new Circle();
	      circles[0]=c1;
	      circles[1]=c2;
	      circles[2]=c3;
	      circles[3]=c4;
	      circles[4]=c5;
	      int level=0;
	 
	      for (int i=9;i>4;i--){
		      circles[i-5].setCenterX(400);
		      circles[i-5].setCenterY(200);
		      circles[i-5].setRadius(50*i*.1);
		      circles[i-5].setFill(Color.TOMATO);
	      
		      Glow glow = new Glow(0.1*level);
		      level=level+1;
		      circles[i-5].setEffect(glow);
	      
		      rootGroup.getChildren().add(circles[i-5]);}

	      stage.setScene(scene);
	      stage.show();
	   }
	   public static void main(final String[] arguments)
	   {
	      Application.launch(arguments);
	   }
	}
