package ckGraphicsEngine;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;

import org.junit.Test;

public class TestCKDialogMessage
{

	@Test
	public final void testCreateDiffRect()
	{
		Rectangle bigger = new Rectangle(5,10,45,90);
		
		Rectangle R1 = new Rectangle(5,10,15,90);
		Rectangle R1Answer = new Rectangle(20,10,30,90);
		assertEquals("shave off Left",R1Answer,CKDialogMessage.createDiffRect(bigger, R1));
		
		Rectangle R2 = new Rectangle(5,10,45,30);
		Rectangle R2Answer = new Rectangle(5,40,45,60);
		assertEquals("shave off Top",R2Answer,CKDialogMessage.createDiffRect(bigger, R2));
		
		
		assertEquals("shave off Right",R1,CKDialogMessage.createDiffRect(bigger, R1Answer));
		

		assertEquals("shave off Bottom",R2,CKDialogMessage.createDiffRect(bigger, R2Answer));
		
		
		Rectangle p1big = new Rectangle(10,400,580,190);
		Rectangle p1small = new Rectangle(10,400,30,190);
		Rectangle p1sol =  new Rectangle(40,400,550,190);
		assertEquals("bug found",p1sol,CKDialogMessage.createDiffRect(p1big,p1small));
		
		/*
		Rectangle Bigger = new Rectangle(5,10,45,90);
		
		Rectangle Bigger = new Rectangle(5,10,45,90);
		*/
		
		
	}

}
