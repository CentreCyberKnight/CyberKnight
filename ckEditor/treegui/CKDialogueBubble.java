package ckEditor.treegui;

//Haven't finished yet
import javafx.scene.text.Font;

import javax.swing.tree.MutableTreeNode;

import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.assets.CKTextAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;

public class CKDialogueBubble extends CKActorEffect{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String description;
	
	public CKDialogueBubble()
	{
		this("Dialogue Bubble");
	}
	
	//Hard code right now, later, want the users to be able to decide the concept of dialogue
	public CKDialogueBubble(String description)
	{
		this.description=description;
		
	}
	
	@Override
	public int doActorEffect(CKSpellCast cast, boolean source, int startTime)
	{
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		int tid=engine.startTransaction(true);
		if (source){
			if((cast.getSource()) instanceof CKGridActor){
				CKGridActor target=(CKGridActor)cast.getSource();
				CKPosition pos=target.getPos();
				CKPosition added=new CKPosition(0,0,3,0);
				CKPosition newpos=pos.add(added);
				CKTextAsset text=new CKTextAsset("Hi",new Font(40),javafx.scene.paint.Color.RED);
				//text.getStyleClass().add()
				int IID=engine.createUniqueInstance(tid, text, newpos, startTime, CKGraphicsLayer.SPRITE_LAYER);
				try {
					engine.destroy(tid,IID,startTime+90);
				} catch (BadInstanceIDError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return startTime+90;
	}
	
	@Override
	public Object clone(){
		CKDialogueBubble bubble=new CKDialogueBubble(description);
		bubble.children.clear();
		for(Object n: this.children)
		{
			bubble.add((MutableTreeNode) ((CKGUINode) n).clone());
		}
		return bubble;
	}
	
}