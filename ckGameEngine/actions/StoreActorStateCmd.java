package ckGameEngine.actions;

import ckCommonUtils.CKPosition;
import ckCommonUtils.LogListener;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;

public class StoreActorStateCmd extends CKGameAction
{

	
	private static final long serialVersionUID = -5665989944913504078L;
	
	boolean generateCommands = false;
	boolean beforeTurn = true;
	private LogListener listener;
	int verbosity;
	
	public StoreActorStateCmd(LogListener l, boolean generateCommands,boolean beforeTurn,int verbosity)
	{
		listener = l;
		this.generateCommands=generateCommands;
		this.beforeTurn=beforeTurn;
		this.verbosity = verbosity;
	}
	
	
	
	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		CKGridActor actor = cast.getActorTarget();
		String cmdB = "[";
		String cmdE = "]";
		
		if(!generateCommands) { cmdB = "";  cmdE="";}
		
		CKPosition pos = actor.getPos();
		
		//
		String report = "----"+cmdB+actor.getName()+" at "+(int)pos.getX()+","+(int)pos.getY()+","+(int)pos.getZ();
		if(generateCommands && beforeTurn) { report+=" before " ;}
		else if(generateCommands && ! beforeTurn) { report+=" after ";}
		report+="t"+actor.getTurnNumber()+cmdE+"---\n";
		
		String CP = "---"+cmdB+actor.getName()+" CP EQUALS "+actor.getCyberPoints();
		if(generateCommands && beforeTurn) { CP+=" before " ;}
		else if(generateCommands && ! beforeTurn) { CP+=" after ";}
		CP+="t"+actor.getTurnNumber()+cmdE+"---\n";
		
		String face = "---"+cmdB+actor.getName()+" FACES "+actor.getDirection();
		if(generateCommands && beforeTurn) {  face+=" before " ;}
		else if(generateCommands && ! beforeTurn) { face+=" after ";}
		face+="t"+actor.getTurnNumber()+cmdE+"---\n";
		
		listener.addText(report+CP+face);
//		"----[HERO at 0,0,0 before t0]---\n" +
//		"----[HERO CP EQUALS 100 before t0]---\n" +
		
		L.actionCompleted(this);
	}

}
