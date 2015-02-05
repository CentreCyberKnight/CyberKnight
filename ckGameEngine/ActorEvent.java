package ckGameEngine;

import java.util.ArrayList;
import java.util.Iterator;

public class ActorEvent extends Event {

	CKGridActor pc;
	
	public ActorEvent(CKGridActor pc2, double d)
	{
		super(d);
		this.pc=pc2;	
		
	}
	
	public CKGridActor getPC()
	{
		return pc;
	}
	
	/*public void priorityIncrease(int increase)
	{
		this.increase=increase;
	}*/
	
	@Override
	void doEvent()
	{
		//pc.setTurnNumber(pc.getTurnNumber()+1); this should be handled with marks..
		pc.getTurnController().takeTurn();
//		cont.takeTurn();
	}
	
	
	@Override
	public String toString() {
		return "PCEvent [pc=" + pc + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public Iterator<Event> getNewEvents() 
	{
		ArrayList<Event> al = new ArrayList<Event>(); 
		//pc.setPriority(priority+increase);
		//TODO calculate priority as seen in PC.getPrioirity?
		al.add(new ActorEvent(pc, pc.calcTimeToNextTurn(priority)));
//		System.out.println("PC has new priority of "+priority+pc.getPriority());
		return al.iterator(); 
	}

}
