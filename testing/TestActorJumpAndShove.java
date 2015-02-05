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
import ckGameEngine.actions.CKShoveActorCmd;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKTrigger;

public class TestActorJumpAndShove
{
	CKGrid grid;
	CKGridActor actor1;
	CKGridActor actor2;
	
	@Before
	public void setUp() throws Exception
	{
		CKGameObjectsFacade.unitTest=true;
		CKGameObjectsFacade.getEngine().loadScene("TEST_ACTOR_JUMP_AND_SHOVE");
		//grid = CKGridFactory.getInstance().getAsset("TEST_JUMP_OVER",true);
		grid = CKGameObjectsFacade.getGrid();
		actor1=new CKGridActor("babySprite", Direction.NORTHWEST);
		SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);		
		CKTrigger trigger = new CKTrigger(mSat,new CKMoveActorCmd());
		actor1.addTrigger(trigger);
		actor1.setItemHeight(2);
		actor1.setItemWeight(10);
		//grid.addToPosition(actor1,1,1);
		actor2=new CKGridActor("babySprite", Direction.NORTHWEST);
		SpellSatisfies mSat2 = new SpellSatisfies(CKPropertyStrings.CH_EARTH, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);
		 CKTrigger trigger2 = new CKTrigger(mSat2, new CKShoveActorCmd());
		 actor2.addTrigger(trigger2);
		 actor2.setItemHeight(2);
		 actor2.setItemWeight(5);
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
	
	
	@Test //works
	public void testShove()
	{
		grid.addToPosition(actor1, 9, 0);
		grid.addToPosition(actor2, 8, 0);
		assertActorAt(actor1, 9, 0, 0);
		assertActorAt(actor2, 8, 0, 0);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,10,"");
		shove.castSpell();
		assertActorAt(actor2,7,0,0);
	}
	
	@Test
	public void testShoveOff()
	{
		grid.addToPosition(actor1, 4, 1);
		grid.addToPosition(actor2, 4, 2);
		assertActorAt(actor1,4,1,3);
		assertActorAt(actor2,4,2,3); 
		actor1.setDirection(Direction.SOUTHWEST);
		actor2.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,120,"");
		shove.castSpell();
		assertActorAt(actor2,4,4,0); //Will not be shoved off
		
	}
	
	//@Test
	public void SERampTest()
	{
		grid.addToPosition(actor1, 1, 5);
		grid.addToPosition(actor2, 2, 5);
		assertActorAt(actor1, 1,5,0);
		assertActorAt(actor2, 2,5,0);
		actor1.setDirection(Direction.SOUTHEAST);
		actor2.setDirection(Direction.SOUTHEAST);
		assertDirection(actor1, Direction.SOUTHEAST);
		assertDirection(actor2, Direction.SOUTHEAST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,20,"");
		shove.castSpell();
		assertActorAt(actor2, 4,5,2); //cannot shove up ramps
	}
	
	@Test
	public void SWRampTest()
	{
		grid.addToPosition(actor1, 7, 3);
		grid.addToPosition(actor2, 7, 4);
		assertActorAt(actor1, 7,3,0);
		assertActorAt(actor2, 7,4,0);
		actor1.setDirection(Direction.SOUTHWEST);
		actor2.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		assertDirection(actor2, Direction.SOUTHWEST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,10,"");
		shove.castSpell();
		assertActorAt(actor2, 7,5,1); //cannot shove up ramps
	}
	
	@Test  //works
	public void SWRampBackwardTest()
	{
		grid.addToPosition(actor1, 6, 8);
		grid.addToPosition(actor2, 6, 7);
		assertActorAt(actor1, 6,8,0);
		assertActorAt(actor2, 6,7,0);
		actor1.setDirection(Direction.NORTHEAST);
		actor2.setDirection(Direction.NORTHEAST);
		assertDirection(actor1, Direction.NORTHEAST);
		assertDirection(actor2, Direction.NORTHEAST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,100,"");
		shove.castSpell();
		assertActorAt(actor2, 6,7,0); //And you can't~
	}
	
	@Test //works
	public void TileTest()
	{
		grid.addToPosition(actor1, 6, 9);
		grid.addToPosition(actor2, 5, 9);
		assertActorAt(actor1, 6,9,0);
		assertActorAt(actor2, 5,9,0);
		actor1.setDirection(Direction.NORTHWEST);
		actor2.setDirection(Direction.NORTHWEST);
		assertDirection(actor1, Direction.NORTHWEST);
		assertDirection(actor2, Direction.NORTHWEST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,1,"");
		shove.castSpell();
		assertActorAt(actor2, 4,9,0); 
		//Reading Negative slide as positive, sliding past the first tile still calculates the weight into cost of movement
	}
	
	@Test //works
	public void DownRamp()
	{
		grid.addToPosition(actor1, 9, 5);
		grid.addToPosition(actor2, 9, 6);
		assertActorAt(actor1, 9,5,2);
		assertActorAt(actor2, 9,6,2);
		actor1.setDirection(Direction.SOUTHWEST);
		actor2.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		assertDirection(actor2, Direction.SOUTHWEST);
		CKSpellCast shove = new CKSpellCast(actor2,actor1,CH_EARTH,P_SHOVE,5,"");
		shove.castSpell();
		assertActorAt(actor2, 9,7,1); //cannot shove up ramps
	}
	
	
	///*
	@Test //works
	public void testJump()
	{
		//Jumping down works, jumping up does not
		grid.addToPosition(actor1, 1, 1);
		assertActorAt(actor1, 1, 1, 0);
		actor1.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		CKSpellCast jump = new CKSpellCast(actor1, actor1,CH_MOVE, P_JUMP_UP, 90,"");
		jump.castSpell();
		assertActorAt(actor1,1,2,1);
	}
	
	//@Test //works
	public void testJumpOver()
	{
		//Jumping down works, jumping up does not
		grid.addToPosition(actor1, 7, 0);
		assertActorAt(actor1, 7, 0, 0);
		//actor1.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.NORTHWEST);
		CKSpellCast jump = new CKSpellCast(actor1, actor1,CH_MOVE, P_JUMP_OVER, 6,"");
		jump.castSpell();
		assertActorAt(actor1,5,0,0);
	}
	
	
	//@Test //works
	public void testJumpOverWall()
	{
		
		grid.addToPosition(actor1, 1, 1);
		assertActorAt(actor1, 1, 1, 0);
		actor1.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		CKSpellCast jump = new CKSpellCast(actor1, actor1,CH_MOVE, P_JUMP_OVER, 6,"");
		jump.castSpell();
		assertActorAt(actor1,1,3,0);
	}
	
	//@Test //works
	public void testJumpOverOffWall()
	{
		//Jumping down works, jumping up does not
		grid.addToPosition(actor1, 1, 2);
		assertActorAt(actor1, 1, 2, 1);
		actor1.setDirection(Direction.SOUTHWEST);
		assertDirection(actor1, Direction.SOUTHWEST);
		CKSpellCast jump = new CKSpellCast(actor1, actor1,CH_MOVE, P_JUMP_OVER, 4,"");
		jump.castSpell();
		assertActorAt(actor1,1,3,0);
	}
	
	
	//@Test //works
	public void testJumpOverAcrossMap()
	{
		//Jumping down works, jumping up does not
		grid.addToPosition(actor1, 9, 9);
		assertActorAt(actor1, 9, 9, 0);
		actor1.setDirection(Direction.NORTHWEST);
		assertDirection(actor1, Direction.NORTHWEST);
		CKSpellCast jump = new CKSpellCast(actor1, actor1,CH_MOVE, P_JUMP_OVER, 1000,"");
		jump.castSpell();
		assertActorAt(actor1,0,9,0);
	}
	//*/
	
}