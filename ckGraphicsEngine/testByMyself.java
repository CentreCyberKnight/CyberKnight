package ckGraphicsEngine;

import ckCommonUtils.*;
/*import ckGraphicsEngine.assets.CKFadeAsset;
import ckGraphicsEngine.assets.CKTextAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;*/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
import javafx.stage.Stage;

public class testByMyself extends Application{
	@Override
	public void start(Stage stage)
	{
		/*FX2dGraphicsEngine engine=new FX2dGraphicsEngine();
		engine.resize(600,600);
		engine.loadScene("Kitchen");
		try{
			System.out.println("Start");
			int tid=engine.startTransaction(true);
			CKTextAsset text1=new CKTextAsset("Help",new Font(40),javafx.scene.paint.Color.BLUE);//A normal text with color
			CKTextAsset text2=new CKTextAsset("10",new Font(40),javafx.scene.paint.Color.RED);//A text needs to move up and fade in 1 second
			CKFadeAsset fade=new CKFadeAsset(text2, 30,100);
			CKPosition pos1=new CKPosition(1,8,5,0);
			CKPosition pos2=new CKPosition(8,1,5,0);
			CKPosition pos3=new CKPosition(8,1,9,0);
			int start=0;
			engine.createUniqueInstance(tid, text1, pos1, 0, CKGraphicsLayer.SPRITE_LAYER);
			int spriteId=engine.createUniqueInstance(tid, fade, pos2,1, CKGraphicsLayer.SPRITE_LAYER);
			engine.move(tid,spriteId,start+30,pos1,pos3,15);
			System.out.println("Should be ended");
			engine.endTransaction(0, false);}
		catch(BadInstanceIDError e){System.out.println("Hahah");}

		HBox root = new HBox();
		Scene scene = new Scene(root,600,600);
		stage.setScene(scene);

		engine.maxWidth(Double.MAX_VALUE);
		engine.maxHeight(Double.MAX_VALUE);
		engine.widthProperty().bind(root.widthProperty());
		engine.heightProperty().bind(root.heightProperty());
		root.getChildren().addAll(engine);
		stage.setTitle("Tutorial");
		stage.show();*/
		
		FX2dGraphicsEngine engine=new FX2dGraphicsEngine();
		engine.loadScene("Kitchen");
		int tid=engine.startTransaction(true);
		CKPosition pos1=new CKPosition(8,1,5,0);
		CKPosition pos2=new CKPosition(1.0,8.0,5.0,0);
		CKPosition pos3=new CKPosition(4,8,0,0);
		CKPosition pos4=new CKPosition(8,4,0,0);
		CKPosition pos5=new CKPosition(3,3,0,0);
		CKPosition pos6=new CKPosition(7,7,0,0);
		
		FadeText.Fadetext("10",pos1,1,Color.RED,engine,tid);
		FadeText.Fadetext("30",pos2,50,Color.BLUE,engine,tid);
		FadeText.specialEffect("illuminate", pos3,pos4, 1, engine, tid);
		FadeText.specialEffect("Swirl", pos5,pos6, 180, engine, tid);
		engine.endTransaction(tid, false);
		HBox root = new HBox();
		Scene scene = new Scene(root,600,600);
		stage.setScene(scene);
		engine.maxWidth(Double.MAX_VALUE);
		engine.maxHeight(Double.MAX_VALUE);
		engine.widthProperty().bind(root.widthProperty());
		engine.heightProperty().bind(root.heightProperty());
		root.getChildren().addAll(engine);
		stage.setTitle("Tutorial");
		stage.show();
	}


}