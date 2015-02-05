package ckGameEngine;

import java.util.Iterator;

public class EndEvent extends Event {

	public EndEvent(int priority)
	{
		super(priority);
	}
	@Override
	void doEvent() {
		//so this will end the game, cause a break? Just by exsisting, doesn't need to do anything
	}

	@Override
	public String toString() {
		return "EndEvent [priority=" + priority + ", toString()="
				+ super.toString() + "]";
	}
	@Override
	public Iterator<Event> getNewEvents() {
		//TODO how do you end the world? Or maybe this doesn't need to add a new event, a satisfies can just look for end world
		/*ArrayList<Event> al = new ArrayList<Event>(); 
		al.add(new EndEvent());
		return al.iterator(); */
		return null;
	}

	
}
