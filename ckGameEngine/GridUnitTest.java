/**
 * 
 */
package ckGameEngine;

/**
 * @author Nathan
 *
 */
@Deprecated
public class GridUnitTest {
	
	Grid grid;
	PC testPC;
	Quest world;
//	
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
	/**
	 * @throws java.lang.Exception
	 */
/*	@Before
	public void setUp() throws Exception 
	{
		world = new Quest();
		
		testPC = new PC(world.getGrid().getPositionFromList(1, 1), "heroSprite");
		//testPC.setGrid(grid);
		//testPC.checkOutPosition();
		//grid.getPositionFromList(1, 1).setPlaceHolder(testPC);
	}
*/
	/**
	 * @throws java.lang.Exception
	 */
/*	@After
	public void tearDown() throws Exception {
		
		for(int i=0; i<grid.getWidth(); i++)
		{
			for(int j=0; j<grid.getHeight(); j++)
			{
				if(grid.getPositionFromList(i, j).getPlaceHolder()!=null)
				{
					grid.getPositionFromList(i, j).setPlaceHolder(null);
				}
			}
		}
				
	}
	
*/

	/**
	 * Test method for {@link ckGameEngine.Grid#Grid(int, int)}.
	 */
/*	@Test
	public void testGridIntInt() 
	{
		if(grid.getHeight()!=4 || grid.getWidth()!=4)
		{
		
			fail("Fail. The Height is " + grid.getHeight() + " and the width is " + grid.getWidth());
		}
	}
*/
	/**
	 * Test method for {@link ckGameEngine.Grid#Grid(int, int, int, int)}.
	 */
/*	@Test
	public void testGridIntIntIntInt() {
		for(int i=0; i<grid.getWidth(); i++)
		{
			for(int j=0; j<grid.getHeight(); j++)
			{
				if(grid.getPositionFromList(i, j).getZ()!=0)
				{
					fail("the height of tile with width " + i + " and height " + j + " is being read as " + 
							grid.getPositionFromList(i, j).getZ());

				}
				if(grid.getPositionFromList(i, j).getDepth()!=0)
				{
					fail("the height of tile with width " + i + " and height " + j + " is being read as " + 
							grid.getPositionFromList(i, j).getDepth());

				}
			}
		}
	}
*/
	/**
	 * Test method for {@link ckGameEngine.Grid#getPositionFromList(int, int)}.
	 */
/*	@Test
	public void testGetPositionFromList() 
	{
		if(grid.getPositionFromList(1, 1)==null)
		{
			fail("The getPositionFromList is failing to retrieve the position of the PC occupying locaiton 1,1");
		}
	}
*/
}
