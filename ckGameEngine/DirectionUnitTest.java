package ckGameEngine;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPosition;

public class DirectionUnitTest
{
	
	Direction ne;
	Direction nw;
	Direction se;
	Direction sw;
	Direction none;
	

	
	
	@Before
	public void setUp() throws Exception 
	{
		ne = Direction.NORTHEAST;
		nw = Direction.NORTHWEST;
		se = Direction.SOUTHEAST;
		sw = Direction.SOUTHWEST;
		none = Direction.NONE;
		
		
	}
	
	@Test
	public void testLeftNeighbors()
	{
		//System.out.println(-1 % 5);
		//System.out.println(ne.getLeftNeightbor()+"is not equal to "+nw);
		assertEquals(ne.getLeftNeightbor(),nw);
		assertEquals(nw.getLeftNeightbor(),sw);
		assertEquals(sw.getLeftNeightbor(),se);
		assertEquals(se.getLeftNeightbor(),ne);
		assertEquals(none.getLeftNeightbor(),none);

	}

	@Test
	public void testRighttNeighbors()
	{
		assertEquals(ne.getRightNeightbor(),se);
		assertEquals(nw.getRightNeightbor(),ne);
		assertEquals(sw.getRightNeightbor(),nw);
		assertEquals(se.getRightNeightbor(),sw);
		assertEquals(none.getRightNeightbor(),none);

	}
	
	
	
	@Test
	public void testGetDirectionTo()
	{
		CKPosition p0 = new CKPosition(0,0,0,0);
		CKPosition p1 = new CKPosition(1,1.5,0,0);
		CKPosition p2 = new CKPosition(1,.0001,0,0);
		//CKPosition p3 = new CKPosition(30,20,0,0);
		CKPosition p4 = new CKPosition(-12,10,0,0);
		CKPosition p5 = new CKPosition(-24,-34,0,0);
		
		assertEquals(sw,Direction.getDirectionTo(p0,p1));
		assertEquals(se,Direction.getDirectionTo(p0,p2));
		assertEquals(nw , Direction.getDirectionTo(p0,p4));
		assertEquals(ne , Direction.getDirectionTo(p0,p5));
	}

	@Test
	public void testAngleTo()
	{
		assertEquals(Direction.NORTHEAST.angleTo(Direction.SOUTHEAST),Math.PI/2.0,.001);
		assertEquals(Direction.NORTHEAST.angleTo(Direction.SOUTHWEST),Math.PI,.001);
		assertEquals(Direction.NORTHEAST.angleTo(Direction.NORTHWEST),-Math.PI/2.0,.001);
		assertEquals(Direction.NORTHWEST.angleTo(Direction.NORTHEAST),Math.PI/2.0,.001);
		assertEquals(Direction.SOUTHEAST.angleTo(Direction.SOUTHEAST),0,.001);
		assertEquals(Direction.NORTHWEST.angleTo(Direction.SOUTHEAST),Math.PI,.001);
		//assertEquals(Direction.NONE.angleTo(Direction.SOUTHEAST),Math.PI,.001);
	}
	
	
}
