package testing;

import static org.junit.Assert.*;
import static ckCommonUtils.CKPropertyStrings.*;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;
import ckGameEngine.actions.CKMoveActorCmd;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTrigger;
import ckTrigger.CKTriggerList;

public class TestActorMovement
{
	CKGrid grid;
	CKGridActor actor1;
	
	@Before
	public void setUp() throws Exception
	{
		CKGameObjectsFacade.unitTest=true;
		CKGameObjectsFacade.getEngine().loadScene("TEST_ACTOR_MOVEMENT");
		grid = CKGameObjectsFacade.getGrid();
		actor1=new CKGridActor("babySprite", Direction.SOUTHWEST);
		SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);		
		CKTrigger trigger = new CKTrigger(mSat,new CKMoveActorCmd());
		actor1.addTrigger(trigger);
		actor1.setSharedTriggers(new CKSharedTriggerList());
		actor1.setItemHeight(2);
		actor1.setItemWeight(10);
		//grid.addToPosition(actor1,1,1);
		 
	}
	
	public void assertActorAt(CKGridActor actor,int x,int y,int z)
	{
		System.out.println("Actor is at:"+ actor.getPos());
		assertEquals(x,(int)actor.getPos().getX());
		assertEquals(y,(int)actor.getPos().getY());
		assertEquals(z,(int)actor.getPos().getZ());

	}
	
	public void assertDirection(CKGridActor actor, Direction d){
		 assertEquals(actor.getDirection(), d);
	}

	@Test
	public void test()
	{
		grid.addToPosition(actor1,1,1);
		assertActorAt(actor1,1,1,0);
		CKSpellCast move1 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,1,"");
		move1.castSpell();
		assertActorAt(actor1,1,2,0);
		CKSpellCast move2 = new CKSpellCast(actor1,actor1,CH_MOVE,P_LEFT,1,"");
		move2.castSpell();
		assertDirection(actor1, Direction.SOUTHEAST);
		CKSpellCast move3 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,1,"");
		move3.castSpell();
		assertActorAt(actor1,6,2,0);
		CKSpellCast move4 = new CKSpellCast(actor1,actor1,CH_MOVE,P_RIGHT,2,"");
		move4.castSpell();
		assertDirection(actor1,Direction.NORTHWEST);
		CKSpellCast move5 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,1,"");
		move5.castSpell();
		assertActorAt(actor1,1,2,0);
		CKSpellCast move6 = new CKSpellCast(actor1,actor1,CH_MOVE,P_LEFT,1,"");
		move6.castSpell();
		assertDirection(actor1, Direction.SOUTHWEST);
		CKSpellCast move7 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,1,"");
		move7.castSpell();
		assertActorAt(actor1,1,2,0);
		CKSpellCast move8 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,3,"");
		move8.castSpell();
		assertActorAt(actor1,1,3,1);
		CKSpellCast move9 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,3,"");
		move9.castSpell();
		assertActorAt(actor1,1,4,1);
		CKSpellCast move10 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,2,"");
		move10.castSpell();
		assertActorAt(actor1,1,5,1);
		CKSpellCast move11 = new CKSpellCast(actor1,actor1,CH_MOVE,P_LEFT,2,"");
		move11.castSpell();
		assertDirection(actor1, Direction.NORTHEAST);
		CKSpellCast move12 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,3,"");
		move12.castSpell();
		assertActorAt(actor1,1,3,1);
		CKSpellCast move13 = new CKSpellCast(actor1,actor1,CH_MOVE,P_RIGHT,1,"");
		move13.castSpell();
		assertDirection(actor1, Direction.SOUTHEAST);
		CKSpellCast move14 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,20,"");
		move14.castSpell();
		assertActorAt(actor1,2,3,0);
		move13.castSpell();
		assertDirection(actor1, Direction.SOUTHWEST);
		move10.castSpell();
		assertActorAt(actor1,2,4,0);
		move12.castSpell();
		assertActorAt(actor1,2,5,0);
	} 
	
	
	
	@Test
	public void testDown(){
		grid.addToPosition(actor1,1,4);
		CKSpellCast move11 = new CKSpellCast(actor1,actor1,CH_MOVE,P_LEFT,2,"");
		move11.castSpell();
		assertDirection(actor1, Direction.NORTHEAST);
		CKSpellCast move12 = new CKSpellCast(actor1,actor1,CH_MOVE,P_FORWARD,10,"");
		move12.castSpell();
		assertActorAt(actor1,1,0,0);
	}

}
