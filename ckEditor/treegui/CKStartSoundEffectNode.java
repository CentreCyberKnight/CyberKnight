package ckEditor.treegui;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.tree.MutableTreeNode;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.Quest;
import ckGraphicsEngine.FX2dGraphicsEngine;

public class CKStartSoundEffectNode extends CKGUINode
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2983643302369174714L;


	public CKStartSoundEffectNode()
	{
		addIT(new CKSoundPickerNode());
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
	public int startSoundEffects()
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		Quest quest = CKGameObjectsFacade.getQuest();
		int ssid = engine.createSoundInstance(0, getSound().getAID());
		engine.playSound(quest.getStartTime(), ssid);
		return ssid;
		
	}
	
	
	
	
	
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#insert(javax.swing.tree.MutableTreeNode, int)
	 */
	@Override
	public void insert(MutableTreeNode newChild, int childIndex)
	{//this must be setup properly in the constructor for this to work later.
		//will ignore childIndex
		if(newChild instanceof CKSoundPickerNode)
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
		CKStartSoundEffectNode effect = new CKStartSoundEffectNode();
		effect.add(this.getSound());
		return effect;
	}

	/**
	 * @return the action
	 */
	public CKSoundPickerNode getSound()
	{
		return ( (CKSoundPickerNode) this.getChildAt(0));
	}
	
	public void setSound(CKSoundPickerNode node)
	{	
		add(node);
	}
	
	public String toString()
	{
		return "Start Sound Effect";
	}
	
	
	public static void main(String [] s)
	{
		CKCasterEffect effect = new CKCasterEffect();
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
		CKCasterEffect e2 = (CKCasterEffect) d.readObject();
		//System.out.println("stored name" +e2.getName());
		
		
	}
	
}
