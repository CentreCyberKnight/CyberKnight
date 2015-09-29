package ckEditor.treegui;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JFrame;
import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKActorEffect extends CKGUINode
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3310695576060802428L;

	private static final int DURATION = 0;
	private static final int BG_ASSET = 1;
	private static final int FG_ASSET = 2;
	private static final int SOUND_ASSET = 3;
	
	
//	private int duration;
	private String description;
	

	
	public CKActorEffect()
	{
		this("Actor Effect");
	}
	
	public CKActorEffect(String description)
	{
		this.description = description;
		add(new CKNumberPickerNode("Duration"));
		add(new CKGraphicsAssetPickerNode("Background"));
		add(new CKGraphicsAssetPickerNode("Foreground"));
		add(new CKSoundPickerNode("Sound"));
		
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}
	
	
	public int doActorEffect(CKSpellCast cast,boolean source,int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		//Quest quest = CKGameObjectsFacade.getQuest();		
		
		int endTime = startTime+getDuration();
		CKPosition position = cast.getTargetPosition();
		if(source)
		{
			position = cast.getSource().getPos();
		}
		
		try
		{
			String aid = getBGGraphic().getAID();
			if(aid.compareTo("null")!=0)
			{
				int bgEffect = engine.createInstance(0, aid,
						position,startTime, 
						CKGraphicsLayer.REARHIGHLIGHT_LAYER);
				engine.destroy(0, bgEffect, endTime);
			}
			aid = getFGGraphic().getAID();
			if(aid.compareTo("null")!=0)
			{
				int fgEffect = engine.createInstance(0, aid,
					position,startTime, 
					CKGraphicsLayer.FRONTHIGHLIGHT_LAYER);
				engine.destroy(0, fgEffect, endTime);
			}
			//do the sounds
			
			aid = getSound().getAID();
			if(aid.length()>0)
			{
				int ssid = engine.createSoundInstance(0,aid);
				engine.playSound(startTime,endTime, ssid);
			}
			
			
		} catch (LoadAssetError e)
		{	
			e.printStackTrace();
		} catch (BadInstanceIDError e)
		{	
			e.printStackTrace();
		}
	
		return endTime;
		
	}
	

	
	
	
	
	

	

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CKActorEffect action = new CKActorEffect(description);
		action.children.clear();
		for(Object n :this.children)
		{
			action.add((MutableTreeNode) ((CKGUINode) n).clone());			
		}
		return action;

	}

	/**
	 * @return the action
	 */
	public CKGraphicsAssetPickerNode getBGGraphic()
	{
		return ( (CKGraphicsAssetPickerNode) this.getChildAt(BG_ASSET));
	}
	
	public void setBGGraphic(CKGraphicsAssetPickerNode node)
	{	
		remove(BG_ASSET);
		insert(node,BG_ASSET);
	}
	
	
	

	/**
	 * @return the action
	 */
	public CKGraphicsAssetPickerNode getFGGraphic()
	{
		return ( (CKGraphicsAssetPickerNode) this.getChildAt(FG_ASSET));
	}
	
	public void setFGGraphic(CKGraphicsAssetPickerNode node)
	{	
		remove(FG_ASSET);
		insert(node,FG_ASSET);
	}
	
	
	/**
	 * @return the action
	 */
	public CKSoundPickerNode getSound()
	{
		return ( (CKSoundPickerNode) this.getChildAt(SOUND_ASSET));
	}
	
	public void setSound(CKSoundPickerNode node)
	{	
		remove(SOUND_ASSET);
		insert(node,SOUND_ASSET);
	}
	
	
	
	/**
	 * @return the duration
	 */
	public int getDuration()
	{
		return ((CKNumberPickerNode) this.getChildAt(DURATION)).getNumber();
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration)
	{
		((CKNumberPickerNode) this.getChildAt(DURATION)).setNumber(duration);
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

	public String toString()
	{
		return description;
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String [] s)
	{
		CKActorEffect travel = new CKActorEffect();
		CKSoundPickerNode effect = new CKSoundPickerNode();
		effect.setAID("BOB");
		CKGraphicsAssetPickerNode ga= new CKGraphicsAssetPickerNode();
		ga.setAID("FRED");
		travel.setDuration(10);
		
		travel.setBGGraphic(ga);
		travel.setSound(effect);
		
		
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
		CKActorEffect e2 = (CKActorEffect) d.readObject();
		//System.out.println("stored name" +e2.getName());
		
		
		
		JFrame frame = new JFrame("CyberKnight Actor Editor");
		CKGuiRoot root = new CKGuiRoot();
		
		CKActorEffect effect2 = new CKActorEffect();
		root.add(effect2);
		frame.add(new CKTreeGui(root));
		//frame.add(new CKGameActionBuilder());
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
		
	}
	
}
