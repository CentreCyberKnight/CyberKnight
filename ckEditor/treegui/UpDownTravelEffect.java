package ckEditor.treegui;

import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.assets.CKFadeAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class UpDownTravelEffect extends CKTravelEffect
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	public UpDownTravelEffect(){
		super();
	}
	
	public UpDownTravelEffect(String description){
		super(description);
	}



	public int doTravelEffect(CKPosition startingPos, CKPosition endingPos,int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();	
		
		CKPosition added=new CKPosition(0,0,5,0);
		CKPosition epos=startingPos.add(added);
		int travelTime=startTime;
		try
		{
			
			String aid = getGraphic().getAID();
			if(aid.compareTo("null")!=0)
			{
				int travel1Effect = engine.createInstance(0, aid, startingPos,startTime, CKGraphicsLayer.FRONTHIGHLIGHT_LAYER);
				//do visuals
				travelTime = engine.move(0,travel1Effect, startTime, startingPos, epos,getSpeed());

				engine.destroy(0,travel1Effect,travelTime);
				int speed = getSpeed();
				System.out.println(speed);
			}			
			//do the sounds
			aid = getSound().getAID();
			if(aid.length()>0)
			{
				int ssid = engine.createSoundInstance(0, aid);
				engine.playSound(startTime,travelTime, ssid);
			}
			
			
		} catch (LoadAssetError e)
		{	
			e.printStackTrace();
		} catch (BadInstanceIDError e)
		{	
			e.printStackTrace();
		}
	
		return travelTime;
	}
	
}