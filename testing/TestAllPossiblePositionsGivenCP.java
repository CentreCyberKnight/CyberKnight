package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.Direction;
import ckGameEngine.actions.CKMoveActorCmd;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTrigger;

public class TestAllPossiblePositionsGivenCP
{
        CKGrid grid;
        CKGridActor actor1;
         
        @Before
        public void setUp() throws Exception
        {
                CKGameObjectsFacade.unitTest=true;
                CKGameObjectsFacade.getEngine().loadScene("asset7423026469327425066");
                grid = CKGameObjectsFacade.getGrid();
                actor1=new CKGridActor("babySprite", Direction.SOUTHWEST);
                SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);                 
                CKTrigger trigger = new CKTrigger(mSat,new CKMoveActorCmd());
                actor1.addTrigger(trigger);
                actor1.setSharedTriggers(new CKSharedTriggerList());
                actor1.setItemHeight(2);
                actor1.setItemWeight(10);
        }
         
        public void assertActorAt(CKGridActor actor,int x,int y,int z)
        {
                System.out.println("Actor is at:"+ actor.getPos());
                assertEquals(x,(int)actor.getPos().getX());
                assertEquals(y,(int)actor.getPos().getY());
                assertEquals(z,(int)actor.getPos().getZ());

        }
         
        public void assertLowestMoveCost(CKGridActor actor, Direction d, int expected) {
                int lowCost = grid.lowestMoveCost(actor, d).getCost();
                assertEquals(lowCost, expected);
        }
         
        @Test
        public void test()
        {
                grid.addToPosition(actor1,2,2);
                assertActorAt(actor1,2,2,0);
                 
                int expectedLowestMoveCost = 1; // need a real value here
                assertLowestMoveCost(actor1, Direction.NORTHEAST, expectedLowestMoveCost);
                 
                int maxCP = 1; // the max CP 
                int expectedNumOfPositions = 4; // expected number of reachable positions
                //CKAreaPositions positions = grid.allPositionsReachable(actor1, maxCP);
                //assertEquals(positions.getArea().length, expectedNumOfPositions);
                fail("This has not been rewritten!!");
//                grid.addToPosition(actor1,2,2);
//                assertActorAt(actor1,2,2,0);
//                maxCP = 2;
//                expectedNumOfPositions = 9;
//                positions = grid.allPositionsReachable(actor1, maxCP);
//                assertEquals(positions.getArea().length, expectedNumOfPositions);
//                
//                grid.addToPosition(actor1,2,2);
//                assertActorAt(actor1,2,2,0);
//                maxCP = 5;
//                expectedNumOfPositions = 9;
//                positions = grid.allPositionsReachable(actor1, maxCP);
//                assertEquals(positions.getArea().length, expectedNumOfPositions);
//                
//                grid.addToPosition(actor1,2,2);
//                assertActorAt(actor1,2,2,0);
//                maxCP = 202;
//                expectedNumOfPositions = 25;
//                positions = grid.allPositionsReachable(actor1, maxCP);
//                assertEquals(positions.getArea().length, expectedNumOfPositions);
//                
//                grid.addToPosition(actor1,2,2);
//                assertActorAt(actor1,2,2,0);
//                maxCP = 1000;
//                expectedNumOfPositions = 25;
//                positions = grid.allPositionsReachable(actor1, maxCP);
//                assertEquals(positions.getArea().length, expectedNumOfPositions);
        }
}
