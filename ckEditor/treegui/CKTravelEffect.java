package ckEditor.treegui;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JFrame;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKTravelEffect extends CKGUINode
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3310695576060802428L;
	private static final int SPEED = 0;
	private static final int GRAPHICS_ASSET = 1;
	private static final int SOUND_ASSET = 2;

	
	
	private String description;
	
	
	public CKTravelEffect()
	{
		this("Travel Description");
	}
	public CKTravelEffect(String description)
	{
		this.description = description;
		add(new CKNumberPickerNode("Speed"));
		add(new CKGraphicsAssetPickerNode("Graphics Asset"));
		add(new CKSoundPickerNode("Sound Asset"));
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	
	public int doTravelEffect(CKPosition startingPos, CKPosition endingPos,int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		//Quest quest = CKGameObjectsFacade.getQuest();		
		
		int travelTime=startTime;
		try
		{
			
			String aid = getGraphic().getAID();
			if(aid.compareTo("null")!=0)
			{
				int travel1Effect = engine.createInstance(0, aid,
						startingPos,startTime, 
					CKGraphicsLayer.FRONTHIGHLIGHT_LAYER);
			//do visuals
			travelTime = engine.move(0,travel1Effect, startTime,//quest.getStartTime()+45,
					startingPos, endingPos,getSpeed());

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
	

	
	
	
	
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	//@Override
	//public void insert(MutableTreeNode newChild, int childIndex)
//	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		/*if(newChild instanceof CKGraphicsAssetPickerNode)
		{
			remove(0); 
			super.insert(newChild,0);
		}*/
			
//	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CKTravelEffect effect = new CKTravelEffect(description);
		effect.setSpeed(getSpeed());
		effect.setGraphic((CKGraphicsAssetPickerNode) this.getGraphic().clone());
		effect.setSound((CKSoundPickerNode) this.getSound().clone());
		return effect;
	}

	/**
	 * @return the action
	 */
	public CKGraphicsAssetPickerNode getGraphic()
	{
		return ( (CKGraphicsAssetPickerNode) this.getChildAt(GRAPHICS_ASSET));
	}
	
	public void setGraphic(CKGraphicsAssetPickerNode node)
	{	
		remove(GRAPHICS_ASSET);
		insert(node,GRAPHICS_ASSET);
	}
	
	
	public CKSoundPickerNode getSound()
	{
		return ( (CKSoundPickerNode) this.getChildAt(SOUND_ASSET));
	}
	
	public void setSound(CKSoundPickerNode node)
	{	
		remove(SOUND_ASSET);
		insert(node,SOUND_ASSET);
	}
	
	
	public int getSpeed()
	{
		return ((CKNumberPickerNode) this.getChildAt(SPEED)).getNumber();
	}
	
	public void setSpeed(int speed)
	{
		((CKNumberPickerNode) this.getChildAt(SPEED)).setNumber(speed);
	}
	
	
	public String toString()
	{
		return description;
	}
	
	public static void main(String [] s)
	{
		CKTravelEffect travel = new CKTravelEffect();
		//CKSoundPickerNode effect = new CKSoundPickerNode();
		//effect.setAID("BOB");
		//CKGraphicsAssetPickerNode ga= new CKGraphicsAssetPickerNode();
		//ga.setAID("FRED");
		
		//travel.setGraphic(ga);
		//travel.setSound(effect);
		
		
		//effect.setName("I've been set");
		
		PipedInputStream pipeIn = new PipedInputStream();
		PipedOutputStream pipeOut;
		try
		{
			pipeOut = new PipedOutputStream(pipeIn);
			travel.writeToStream(pipeOut);
			travel.writeToStream(System.out);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		
		
		@SuppressWarnings("resource")
		XMLDecoder d = new XMLDecoder(pipeIn);
		CKTravelEffect e2 = (CKTravelEffect) d.readObject();
		//System.out.println("stored name" +e2.getName());
		
		
		JFrame frame = new JFrame("CyberKnight Actor Editor");
		CKGuiRoot root = new CKGuiRoot();
		
		//CKActorEffect effect2 = new CKActorEffect();
		root.add(e2);
		frame.add(new CKTreeGui(root));
		//frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}
