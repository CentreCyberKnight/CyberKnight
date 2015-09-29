package ckEditor.treegui;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.Quest;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKCasterEffect extends CKGUINode
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3310695576060802428L;


	public CKCasterEffect()
	{
		addIT(new CKGraphicsAssetPickerNode());
		this.setChildOrderLocked(true);
		this.setChildRemoveable(false);
	}
	
	
	/**
	 * provides the graphics for the act of casting the spell.
	 * 
	 *  needs to return the instance of the effect.
	 * 
	 * 
	 * @param startingPos
	 * @return the id of the casting effect's instance, -1 if there is no effect to remove later.
	 */
	public int doCasterGraphics(CKPosition startingPos)
	{
		int sourceEffect=-1;
		try
		{
			FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
			Quest quest = CKGameObjectsFacade.getQuest();
			sourceEffect = engine.createInstance(0,getGraphic().getAID(),
													startingPos,quest.getStartTime(), 
					CKGraphicsLayer.REARHIGHLIGHT_LAYER);
		} catch (LoadAssetError e)
		{
			e.printStackTrace();
		}
		return sourceEffect;
		
	}
	
	
	
	
	
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		if(newChild instanceof CKGraphicsAssetPickerNode)
		{
			remove(0); 
			super.insert(newChild,0);
		}
			
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#clone()
	 */
	@Override
	public Object clone()
	{
		CKCasterEffect effect = new CKCasterEffect();
		effect.add(this.getGraphic());
		return effect;
	}

	/**
	 * @return the action
	 */
	public CKGraphicsAssetPickerNode getGraphic()
	{
		return ( (CKGraphicsAssetPickerNode) this.getChildAt(0));
	}
	
	public void setGraphic(CKGraphicsAssetPickerNode node)
	{	
		add(node);
	}
	
	public String toString()
	{
		return "Caster Effect";
	}
	
	
	public static void main(String [] s)
	{
		CKStartSoundEffectNode effect = new CKStartSoundEffectNode();
		CKGraphicsAssetPickerNode ga= new CKGraphicsAssetPickerNode();
		ga.setAID("FRED");
		effect.add(ga);
		//effect.setName("I've been set");
		
		PipedInputStream pipeIn = new PipedInputStream();
		PipedOutputStream pipeOut;
		try
		{
			pipeOut = new PipedOutputStream(pipeIn);
			effect.writeToStream(pipeOut);
			effect.writeToStream(System.out);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		
		
		@SuppressWarnings("resource")
		XMLDecoder d = new XMLDecoder(pipeIn);
		CKStartSoundEffectNode e2 = (CKStartSoundEffectNode) d.readObject();
		//System.out.println("stored name" +e2.getName());
		
		
	}
	
}
