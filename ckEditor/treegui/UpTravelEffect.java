package ckEditor.treegui;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class UpTravelEffect extends CKTravelEffect
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	public UpTravelEffect(){
		super();
	}
	
	public UpTravelEffect(String description){
		super(description);
	}


	@Override
	public int doTravelEffect(CKPosition startingPos, CKPosition endingPos,int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();	
		System.out.println("!!!!!!!MoveUP");
		CKPosition added=new CKPosition(0,0,20,0);
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
	
	
	@Override
	public Object clone()
	{
		UpTravelEffect effect = new UpTravelEffect(this.getDescription());
		effect.setSpeed(getSpeed());
		effect.setGraphic((CKGraphicsAssetPickerNode) this.getGraphic().clone());
		effect.setSound((CKSoundPickerNode) this.getSound().clone());
		return effect;
	}
	
}