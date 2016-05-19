package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.CKGridActor;
import ckGameEngine.Direction;
import ckGameEngine.actions.CKMoveActorCmd;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKTrigger;

public class FindYourPathTest
{
	CKGrid grid;
	CKGridActor actor1;

	@Before
	public void setUp() throws Exception
	{
		CKGameObjectsFacade.unitTest = true;
		CKGameObjectsFacade.getEngine().loadScene("TEST_MOVE_TO");
		grid = CKGameObjectsFacade.getGrid();
		actor1 = new CKGridActor("ArtifactTestDad", Direction.NORTHWEST);
		SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE,
				CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);
		CKTrigger trigger = new CKTrigger(mSat, new CKMoveActorCmd());
		actor1.addTrigger(trigger);
		actor1.setItemHeight(2);
		actor1.setItemWeight(10);
	}

	@Test //works
	public void testJumpOverAcrossMap()
	{
		// Jumping down works, jumping up does not
		grid.addToPosition(actor1, 0, 9);
		actor1.setDirection(Direction.NORTHEAST);
		GridNode[][][][] map = grid.allPositionsReachable(actor1, 4, 4);

		GridNode n = map[0][8][0][0];
		System.out.println(n.getAction());
		assertEquals(5, 5);

	}
	// */

}
