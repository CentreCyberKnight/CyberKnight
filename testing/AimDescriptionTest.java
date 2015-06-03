package testing;

import static org.junit.Assert.*;


import org.junit.BeforeClass;
import org.junit.Test;

import ckCommonUtils.CKPosition;
import ckDatabase.AimDescriptionFactory;
import ckGameEngine.AimDescription;
import ckGameEngine.Direction;
import static ckCommonUtils.CKPropertyStrings.*;

public class AimDescriptionTest
{

	static AimDescription description1;
	static AimDescription description2;

	static AimDescription swipe;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		CKPosition [] array = {new CKPosition(3,0),new CKPosition(3,1)};
		description1 = new AimDescription(0,array,Direction.SOUTHEAST,true,0,0);
		description2 = AimDescriptionFactory.getInstance().getAsset(P_FRONT);
		swipe = AimDescriptionFactory.getInstance().getAsset(P_SWIPE);
	}

	@Test
	public void testGetOffsetsDirection()
	{
		CKPosition [] array = description1.getOffsets(Direction.SOUTHEAST);
		assertTrue(array[0].almostEqual(new CKPosition(3,0), .001));
		//System.out.println(array[1]);
		assertTrue(array[1].almostEqual(new CKPosition(3,1), .001));

		array = description1.getOffsets(Direction.SOUTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(0,3), .001));
		assertTrue(array[1].almostEqual(new CKPosition(-1,3), .001));

		array = description1.getOffsets(Direction.NORTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(-3,0), .001));
		assertTrue(array[1].almostEqual(new CKPosition(-3,1), .001));
		
		array = description1.getOffsets(Direction.NORTHEAST);
		System.out.println(array[0]);

		assertTrue(array[0].almostEqual(new CKPosition(0,-3), .001));
		assertTrue(array[1].almostEqual(new CKPosition(1,-3), .001));

	}
	
	@Test
	public void testFront()
	{
		CKPosition [] array = description2.getOffsets(Direction.SOUTHEAST);
		System.out.println(description2);
		assertTrue(array[0].almostEqual(new CKPosition(1,0), .001));

		array = description2.getOffsets(Direction.SOUTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(0,1), .001));

		array = description2.getOffsets(Direction.NORTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(-1,0), .001));
		
		array = description2.getOffsets(Direction.NORTHEAST);

		assertTrue(array[0].almostEqual(new CKPosition(0,-1), .001));
	}

	
	
	@Test
	public void testSwipe()
	{
		CKPosition [] array = swipe.getOffsets(Direction.SOUTHEAST);
		assertTrue(array[0].almostEqual(new CKPosition(1,0), .001));
		//System.out.println(array[1]);
		assertTrue(array[1].almostEqual(new CKPosition(1,1), .001));
		assertTrue(array[2].almostEqual(new CKPosition(1,-1), .001));

		array = swipe.getOffsets(Direction.SOUTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(0,1), .001));
		assertTrue(array[1].almostEqual(new CKPosition(-1,1), .001));
		assertTrue(array[2].almostEqual(new CKPosition(1,1), .001));

		array = swipe.getOffsets(Direction.NORTHWEST);
		assertTrue(array[0].almostEqual(new CKPosition(-1,0), .001));
		assertTrue(array[1].almostEqual(new CKPosition(-1,1), .001));
		assertTrue(array[2].almostEqual(new CKPosition(-1,-1), .001));
		
		array = swipe.getOffsets(Direction.NORTHEAST);
		System.out.println(array[0]);

		assertTrue(array[0].almostEqual(new CKPosition(0,-1), .001));
		assertTrue(array[1].almostEqual(new CKPosition(1,-1), .001));
		assertTrue(array[2].almostEqual(new CKPosition(-1,-1), .001));

	}
	
	

}
