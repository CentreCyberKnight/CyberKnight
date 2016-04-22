package ckGameEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CKBookTest
{

	CKBook S1;
	
	CKChapter A;
	CKChapter A1;
	CKChapter B;
	CKChapter B1;
	
	CKChapter C;
	
	
	@Before
	public void setUp() throws Exception
	{
		A=new CKChapter("Levels",1);
		A.addPage(new CKPage("level1"));
		A1=new CKChapter("Levels",1);
		A1.addPage(new CKPage("level2"));

		B=new CKChapter("Move",3);
		B.addPage(new CKPage("forward"));
		B.addPage(new CKPage("turn_left"));
		B.addPage(new CKPage("turn right"));
		
		C=new CKChapter("Fire",0);
		S1 = new CKBook();
		S1.addChapter(A);
		S1.addChapter(B);
	}

	
	@Test
	public final void testAddAttribute()
	{
		//added in the setup...
		assertTrue("Move should be in place",S1.getChapter("Move").equals(B));
		assertTrue("Levels should be in n place",S1.getChapter("Levels").equals(A));
		assertTrue("Fire should not be in place",!C.equals(S1.getChapter("Fire")));
		S1.addChapter(C);
		assertTrue("Fire should not be in place",C.equals(S1.getChapter("Fire")));
		
	//	fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetChapter()
	{
		//tested pretty well here...
		testAddAttribute();
	}

	@Test
	public final void testRemoveChapter()
	{
		assertTrue("Move is present",B.equals(S1.getChapter("Move")));
		S1.removeChapter("Move");
		assertTrue("Move should not be in place",!B.equals(S1.getChapter("Move")));
		
	}

	@Test
	public final void testHasChapter()
	{
		assertTrue("Move is present",S1.hasChapter("Move"));
		S1.removeChapter("Move");
		assertTrue("Move should not be in place",!S1.hasChapter("Move"));
	
	}

	@Test
	public final void testHasPage()
	{
		assertTrue("can't find forward",B.hasPage("forward"));
		assertTrue("can't find left_turn",B.hasPage("turn_left"));
		assertTrue("should not find feed",! B.hasPage("feed"));
		
		assertTrue("can't find forward in set", S1.hasPage("Move","forward"));
		assertTrue("should not find feed",! S1.hasPage("Move","feed"));
		assertTrue("should not find mope",! S1.hasPage("Mope","forward"));
		
	}

	@Test
	public final void testAddSetsCKAttributeSetCKAttributeSet()
	{
		CKBook S2 = new CKBook();
		S2.addChapter(A1);
		CKBook S3 = CKBook.addBooks(S1, S2);
		assertTrue("missing level1 ", S3.hasPage("Levels","level1"));
		assertTrue("missing level2 ", S3.hasPage("Levels","level2"));
		assertEquals("Level is not 2", 2, S3.getChapter("Levels").getValue());
		
		//need to test the addition without addition + dups

		CKBook S10 = new CKBook();
		S10.addChapter(B);
		
		CKBook S11 = new CKBook();
		CKChapter b1=new CKChapter("Move",0);
		b1.addPage(new CKPage("forward"));
		b1.addPage(new CKPage("spin"));
		S11.addChapter(b1);
		
		CKBook S12 = CKBook.addBooks(S10, S11);
		assertTrue("missing level1 ", S12.hasPage("Move","spin"));
		assertTrue("missing level2 ", S12.hasPage("Move","forward"));
		assertEquals("Level is not 2", 3, S12.getChapter("Move").getValue());

		System.out.println(S12.treeString());
		
	}

	
	
	@Test
	public final void testAddChapter()
	{
		CKBook b = new CKBook("target");
		b.addChapter(A);
		assertTrue("Does not have levels",b.hasChapter("levels"));
		assertEquals("Is not at Level 1",b.getChapter("levels").getValue(),1);
		assertTrue("Does not have level1 page",b.hasPage("levels", "level1"));
		b.addChapter(A1);
		assertTrue("Does not have levels",b.hasChapter("levels"));
		assertEquals("Is not at Level 2",b.getChapter("levels").getValue(),2);
		assertTrue("Does not have level2 page",b.hasPage("levels", "level2"));
		assertTrue("Does not have level1 page",b.hasPage("levels", "level1"));

		
		
		
		
		
	}

	@Test
	public final void testTreeString()
	{

		CKBook S10 = new CKBook();
		S10.addChapter(A);
		S10.addChapter(B);
		//System.out.println(S10.getChapter("Levels"));
		//System.out.println(S10.hasChapter("Levels"));
		
		CKBook S11 = new CKBook();
		CKChapter b1=new CKChapter("Move",0);
		b1.addPage(new CKPage("forward"));
		b1.addPage(new CKPage("spin"));
		S11.addChapter(b1);
		
		CKBook S12 = CKBook.addBooks(S10, S11);
		
		String s =	"CKBook\n\n"+
				"Levels   1\n"+
				"    level1\n"+
				"Move   3\n"+
				"    forward\n"+
				"    turn right\n"+
				"    spin\n"+
				"    turn_left\n";


		
		System.out.println(S12.treeString());
		
		assertTrue(S12.treeString().compareTo(s)==0 );
		
		
		
		//fail("Not yet implemented"); // TODO
	}

	
	private final void notEqualsTest()
	{
		assertTrue("A should not equal A1l",!A1.equals(A));
		
	}
	@Test
	public final void testEqualsObject()
	{
		notEqualsTest();		
		
		CKChapter a=new CKChapter("Levels",1);
		a.addPage(new CKPage("level1"));
		assertEquals("A not equal",A,a);
		
		CKChapter a1=new CKChapter("Levels",1);
		a1.addPage(new CKPage("level2"));
		assertEquals("A1 not equal",A1,a1);
		
		CKChapter b=new CKChapter("Move",3);
		b.addPage(new CKPage("turn right"));
		b.addPage(new CKPage("turn_left"));
		b.addPage(new CKPage("forward"));
		//test reverse ordering
		assertEquals("B not equal",B,b);
		
		CKChapter c=new CKChapter("Fire",0);
		CKBook s1 = new CKBook();
		s1.addChapter(A);
		s1.addChapter(B);
		assertEquals("Sets are equal",s1,S1);
	}

	

}
