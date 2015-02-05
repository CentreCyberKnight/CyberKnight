package ckGameEngine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyInputHandler extends KeyAdapter 
{
	
	private Quest world;
	//private PC currentPC;
	
	public KeyInputHandler(Quest world)
	{
		System.out.println("Well, one thing works");
		this.world=world;
		//this.currentPC = world.getCurrentPC();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("KEYPRESSED");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("KEYRELEASED");
	}
	
	public void keyTyped(KeyEvent e)
	{
		System.out.println("KEY TYPED");
		if(!world.isInputOK())
		{
			return;
		}
		/*
		 * PC character = world.getCurrentPC();
		 
		if(character ==null )
		{
			return;			
		}
		Direction dir;
		if(e.getKeyChar()=='w')
			dir = Direction.NORTHEAST;
		else if(e.getKeyChar()=='s')
			dir = Direction.SOUTHWEST;
		else if(e.getKeyChar()=='a')
			dir = Direction.NORTHWEST;
		else if(e.getKeyChar()=='d')
			dir = Direction.SOUTHEAST;
		else
			return;
		world.startTransaction();
		System.out.println(dir);
		world.setStartTime(world.getCurrentPC().move(dir, 1));
		world.notifyOfInput();
		world.endTransaction();
		*/
		System.out.println("Umm I thought this class was in disuse so you'll need to fix this yourself");
		
	}
}
