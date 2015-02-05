package ckGameEngine;

import java.util.Iterator;

public abstract class Event implements Comparable<Event> {
	
	double priority;
	
	abstract void doEvent();
	
	public Event(double d)
	{
		this.priority=d;
	}
	
	public abstract Iterator<Event> getNewEvents();
/*	{
		//iterator
		//default just return this event in iterator format? 
		return 
	}
	*/
	
	public double getPriority()
	{
		return priority;
	}
	
	

	@Override
	public String toString() {
		return "Event [priority=" + priority + ", toString()="
				+ super.toString() + "]";
	}

	public int compareTo(Event that) {
		if(this.getPriority()==that.getPriority())
			return 0;
		else if(this.getPriority()>that.getPriority())
			return 1;
		return -1;
	}

}
