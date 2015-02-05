package ckGameEngine;

public class PCUnitTest {
/*	static //static Grid grid = new Grid(6,6);
	Quest world = new Quest();
	//PC testPC = new PC(world);

	//CK2dGraphicsEngine engine = new CK2dGraphicsEngine(30,5);
	CK2dGraphicsEngine engine = null;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		//grid=new Grid(6,6);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		
		
		for(int i=0; i<grid.getWidth(); i++)
		{
			for(int j=0; j<grid.getHeight(); j++)
			{
				if(world.getGrid().getPositionFromList(i, j).getPlaceHolder()!=null)
				{
					world.getGrid().getPositionFromList(i, j).setPlaceHolder(null);
				}
			}
		}
		
		
	}

	@Before
	public void setUp() throws Exception 
	{
		JFrame frame = new JFrame();
		Quest.creation(1);
		frame.add(CKGameObjectsFacade.getEngine());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		world.startTransaction();

		//PC enemy = new PC();
		//enemy.checkOutPosition(world.getGrid().getPositionFromList(3,3));
	}

	@After
	public void tearDown() throws Exception 
	{
		world.endTransaction();
		
		for(int i=0; i<grid.getWidth(); i++)
		{
			for(int j=0; j<grid.getHeight(); j++)
			{
				if(world.getGrid().getPositionFromList(i, j).getPlaceHolder()!=null)
				{
					world.getGrid().getPositionFromList(i, j).setPlaceHolder(null);
				}
			}
		}
		
	}

	@Test
	public void testPC() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(1, 1),world);
		if(testPC.getSpecies()!=0 || testPC.getActionPoints()!=100 || testPC.getDirection()!=Direction.NORTHEAST)
		{
			System.out.println("Does this happen?");
			fail("The testPC believes it's species is " + testPC.getSpecies() + " that it it has " + testPC.getActionPoints()
					+ " action points, and is facing direction " + testPC.getDirection());
		}
	}


	@Test
	public void testPCCKPosition()  
	{
		//world.creation(1);
		//world.startTransaction();
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 3), "heroSprite");
		if(world.getGrid().getPositionFromList(2, 3).getPlaceHolder()==null)
		{
			fail("the Location was not checked out from the grid properly");
		}
		if(testPC.getPosition()==null)
		{
			fail("the testPC has not proparly set it's possition to it's possition");
		}
	}

	@Test
	public void testPCIntIntIntCKPosition()  
	{
		PC testPC = new PC("momSprite",Direction.NORTHEAST,world.getGrid().getPositionFromList(2, 3));
		if( testPC.getCyberPoints()!=1 || testPC.getDirection()!=Direction.NORTHEAST)
		{
			fail("The testPC believes "+ " that it it has " + testPC.getCyberPoints()
					+ " action points, and is facing direction " + testPC.getDirection());
		}
		if(world.getGrid().getPositionFromList(2, 3).getPlaceHolder()==null)
		{
			fail("the Location was not checked out from the grid properly");
		}
		if(testPC.getPosition()==null)
		{
			fail("the testPC has not proparly set it's possition to it's possition");
		}
	}

	

	@Test
	public void testSetActionPoints() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 3),"heroSprite");
		testPC.setCyberPoints(200);
		if(testPC.getCyberPoints()!=200)
		{
			fail("Action Points were not set correctly");
		}
	}

	@Test
	public void testSetDirection() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 3),"heroSprite");
		testPC.setDirection(Direction.NORTHWEST);
		if(testPC.getDirection()!=Direction.NORTHWEST)
		{
			fail("direction not set correctly");
		}
	}
	


	@Test
	public void testMove() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(1, 1),"heroSprite");
		testPC.setDirection(Direction.SOUTHEAST);
		System.out.println(testPC.getDirection());
		testPC.move(1);
		
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(2, 1))
		{
			fail("moved to different position than expected");
		}
		testPC.setDirection(Direction.NORTHEAST);
		System.out.println(testPC.getDirection());
		System.out.println("HEY! LISTEN!");
		//TODO error alert! 
		testPC.move(1);
		
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(2, 0))
		{
			fail("moved to different position than expected");
		}
		testPC.setDirection(Direction.NORTHWEST);
		System.out.println(testPC.getDirection());
		testPC.move(1);
		
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(1,0))
		{
			fail("moved to different position than expected");
		}
		testPC.setDirection(Direction.SOUTHWEST);
		System.out.println(testPC.getDirection());
		testPC.move(1);
		
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(1, 1))
		{
			fail("moved to different position than expected");
		}
	}

	@Test
	public void testMoveInt() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(1, 1),"heroSprite");
		testPC.setDirection(Direction.SOUTHWEST);
		testPC.move(3);
		System.out.println("About to check int int");
		if(!(testPC.getPosition().equals(world.getGrid().getPositionFromList(1, 4))))
		{
			fail("failing");
			System.out.println("Oh it failed");
		}
	}

	@Test
	public void testMoveCKPosition() 
	{
		testPC.checkOutPosition(world.getGrid().getPositionFromList(3, 3));
		testPC.move(world.getGrid().getPositionFromList(1, 1));
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(1, 1))
		{
			fail("failing");
		}
	}
	
	@Test
	public void testMoveDirectionInt()
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 3),"heroSprite");
		testPC.move(Direction.NORTHEAST, 2);
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(2, 1))
		{
			fail("Did not work! Oh noooooooooo");
		}
		testPC.move(Direction.SOUTHWEST, 4);
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(2, 5))
		{
			fail("Failllll");
		}
		testPC.move(Direction.NORTHWEST, 1);
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(1, 5))
		{
			fail("then there was failing");
		}
		testPC.move(Direction.SOUTHEAST, 3);
		if(testPC.getPosition()!=world.getGrid().getPositionFromList(4, 5))
		{
			fail("So close! But it Faillllled");
		}
	}
	
//	@Test
//	public void testMoveInt() {
//		testPC.checkOutPosition(world.getGrid().getPositionFromList(2, 2));
//		boolean testReturn = testPC.move(3);
//		if(!(testPC.getPosition().equals(world.getGrid().getPositionFromList(1, 2))) && testReturn==true)
//		{
//			fail("Oh man it is broken. sighhhh");
//		}
//	}

	@Test
	public void testGetNextPosition() {
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 2),"heroSprite");
		testPC.setDirection(Direction.NORTHEAST);
		if(testPC.getNextPosition()!=world.getGrid().getPositionFromList(2,1))
		{
			fail("oh man it is broken. which means everything is broken");
		}
	}

	@Test
	public void testTurn() {
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 2),"heroSprite");
		testPC.turn(world.getGrid().getPositionFromList(2, 1));
		if(testPC.getDirection()!=Direction.NORTHEAST)
		{
			fail("not facing correct way");
		}
	}

	@Test
	public void testUseActionPoints() 
	{
		PC testPC = new PC(world.getGrid().getPositionFromList(2, 2),"heroSprite");
		int ap = testPC.getCyberPoints();
		testPC.move(1);
		if(testPC.getCyberPoints()!=ap-1)
		{
			fail("Action poitns are messed up, or this needs updated");
		}	
	}

	@Test
	public void testCheckOutPosition() throws Exception 
	{
		//Grid grid = new Grid(4,4);
		PC tester = new PC(world);
		tester.checkOutPosition(world.getGrid().getPositionFromList(1,1));
		if(tester.getPosition()!=world.getGrid().getPositionFromList(1,1))
		{
			fail("not checking out position correctly");
		}
	}

	@Test
	public void testCheckInPosition() {
		testPC.checkOutPosition(world.getGrid().getPositionFromList(3, 2));
		testPC.checkInPosition();
		if(testPC.getPosition()!=null)
		{
			fail("not returning positions correctly");
		}
	}
*/
}
