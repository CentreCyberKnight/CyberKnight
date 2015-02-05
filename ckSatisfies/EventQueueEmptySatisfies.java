package ckSatisfies;

import java.util.PriorityQueue;

import ckGameEngine.CKSpellCast;
import ckGameEngine.Event;

public class EventQueueEmptySatisfies extends Satisfies {

	private static final long serialVersionUID = -6803604369307065744L;
	PriorityQueue<Event> eQueue;
	
	public EventQueueEmptySatisfies(PriorityQueue<Event> eventQueue)
	{
		eQueue = eventQueue;
	}
	
	
	
	public Object clone()
	{
		
		return new EventQueueEmptySatisfies(eQueue);
	}



	@Override
	public boolean isSatisfied(CKSpellCast cast) 
	{
		return eQueue.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "EventQueueEmptySatisfies";
	}

}
