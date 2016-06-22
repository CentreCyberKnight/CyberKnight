package ckCommonUtils;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.assets.CKFadeAsset;
import ckGraphicsEngine.assets.CKTextAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class FadeText{
	
	//Draw the text on the screen, make the text moves up, and the text fade eventually
	//the text will start to move after 1 second and it takes one second for the text to fade
	//String text- the text you want to be shown up on the screen
	//CKPosition spos- the starting position for the text
	//int starttime- when the text shows up on the screen
	//Color color- the color you want for the text
	//FX2dGraphicsEngine engine
	//int tid- transaction ID
	public static void Fadetext(String text, CKPosition spos,int starttime, Color color,FX2dGraphicsEngine engine,int tid)
	{
		CKTextAsset Text=new CKTextAsset(text,new Font(40),color);
		CKFadeAsset Fade=new CKFadeAsset(Text,starttime,starttime+60,false);
		try{
			CKPosition added=new CKPosition(0,0,6,0);
			CKPosition epos=spos.add(added);
			int spriteId=engine.createUniqueInstance(tid, Fade, spos,starttime, CKGraphicsLayer.SPRITE_LAYER);
			engine.move(tid,spriteId,starttime+30,spos,epos,15);
			}
		catch(BadInstanceIDError e){System.out.println("Bad Instance");}	
	}
	
	
	public static void specialEffect(String AID, CKPosition spos, CKPosition epos, int StartFrame, FX2dGraphicsEngine engine,int tid) 
	{
		try{
			engine.loadAsset(tid,AID);
			CKPosition added=new CKPosition(0,0,4,0);
			////////
			//int S=engine.createInstance(tid, AID, spos,StartFrame, CKGraphicsLayer.SPRITE_LAYER);
			//////////
			CKPosition spos1=spos.add(added);
			CKPosition epos1=epos.add(added);
			CKFadeAsset Fade1=new CKFadeAsset(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(AID),StartFrame+30,StartFrame+60);
			int spriteID1=engine.createUniqueInstance(tid, Fade1, spos,StartFrame, CKGraphicsLayer.SPRITE_LAYER);
			engine.move(tid, spriteID1, StartFrame, spos, spos1, 15);
			StartFrame=StartFrame+60;
			engine.destroy(tid, spriteID1, StartFrame);
			int spriteID2=engine.createInstance(tid, AID, epos1,StartFrame+30, CKGraphicsLayer.SPRITE_LAYER);
			engine.move(tid, spriteID2, StartFrame+30, epos1, epos, 15);
			StartFrame=StartFrame+100;
			engine.destroy(tid, spriteID2, StartFrame);
			CKFadeAsset Fade2=new CKFadeAsset(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(AID),StartFrame,StartFrame+30);
			engine.createUniqueInstance(tid, Fade2, epos, StartFrame, CKGraphicsLayer.SPRITE_LAYER);
		}
		catch(LoadAssetError e){System.out.println("LoadAssetError");}
		catch(BadInstanceIDError e){System.out.println("BadInstanceIDError");}
	}
	
}