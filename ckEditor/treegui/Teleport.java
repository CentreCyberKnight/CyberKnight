package ckEditor.treegui;

import ckCommonUtils.CKPosition;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.assets.CKFadeAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class Teleport extends CKTravelEffect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Teleport()
	{
		super();
	}
	
	public Teleport(String description)
	{
		super(description);
	}
	
	@Override
	public int doTravelEffect(CKPosition startingPos, CKPosition endingPos,int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();	
		System.out.println("Teleport!!!!!!!!");
		int travelTime=startTime;
		try
		{
			String aid = getGraphic().getAID();
			if(aid.compareTo("null")!=0)
			{
				CKFadeAsset Fade=new CKFadeAsset(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(aid),startTime+30,startTime+60);
				int travelEffect1 = engine.createUniqueInstance(0, Fade, startingPos,startTime, CKGraphicsLayer.FRONTHIGHLIGHT_LAYER);
				startTime=startTime+60;
				//do visuals
				//travelTime = engine.move(0,travel1Effect, startTime, startingPos, epos,getSpeed());

				engine.destroy(0,travelEffect1,startTime);
				startTime=startTime+20;
				
				engine.createInstance(0, aid, endingPos, startTime, CKGraphicsLayer.FRONTHIGHLIGHT_LAYER);
				travelTime=startTime;
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
	
	@Override
	public Object clone()
	{
		Teleport effect = new Teleport(this.getDescription());
		effect.setSpeed(getSpeed());
		effect.setGraphic((CKGraphicsAssetPickerNode) this.getGraphic().clone());
		effect.setSound((CKSoundPickerNode) this.getSound().clone());
		return effect;
	}
	
}