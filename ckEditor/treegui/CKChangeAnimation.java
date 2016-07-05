package ckEditor.treegui;

import javax.swing.tree.MutableTreeNode;

import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.UnknownAnimationError;

public class CKChangeAnimation extends CKActorEffect{
	
	private String description;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CKChangeAnimation(){
		super();}
	
	public CKChangeAnimation(String description){
		super(description);
		this.description=description;
	}
	
	//need to rewrite later, right now just change to push and change back to walk
	@Override
	public int doActorEffect(CKSpellCast cast,boolean source,int startTime){
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		int tid=engine.startTransaction(true);
		String direction;
		int Duration=0;
		if(source)
		{
			if ((cast.getSource()) instanceof CKGridActor){
				CKGridActor target=(CKGridActor)cast.getSource();
				direction=target.getDirection().toString();
				String dir=direction.substring(direction.length()-2);
				System.out.println("my direction is: "+dir);
				String Animation="pushBall_"+dir;
				try {
					//System.out.println("AAAAAAANNNNNNNNNNIIIIIIIMMMMMMMEEEEEE: "+Animation);
					engine.setAnimation(tid, target.getInstanceID(), Animation, startTime);
					Duration=engine.getInstanceAnimationLength(tid, target.getInstanceID(), Animation);
					//System.out.println("DDDDDDDDDDuuuuuuuuuuuuurrrrrrrrrrraaaaaaaaaaattttttttion is: "+Duration);
					System.out.println(direction);
					engine.setAnimation(tid, target.getInstanceID(), direction, startTime+Duration);
					//System.out.println("WWWWWWWWAAAAAAAAAAAAAAALLLLLLLLLLLLLLLKKKKKKKKKKKKKKK");
				} catch (BadInstanceIDError | UnknownAnimationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		engine.startTransaction(false);
		
		return startTime+Duration;
	}
	
	@Override
	public Object clone()
	{
		CKChangeAnimation action = new CKChangeAnimation(description);
		action.children.clear();
		for(Object n :this.children)
		{
			action.add((MutableTreeNode) ((CKGUINode) n).clone());			
		}
		return action;

	}
	
}