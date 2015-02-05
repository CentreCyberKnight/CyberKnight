package testing;

import static org.junit.Assert.*;
import static ckCommonUtils.CKPropertyStrings.*;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Direction;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKMarkGridActor;
import ckGameEngine.actions.CKMoveActorCmd;
import ckGameEngine.actions.CKReactiveSpellAction;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTrigger;

public class TestActorJumpOver
{
	CKGrid grid;
	CKGridActor actor1;
	
	@Before
	public void setUp() throws Exception
	{
		CKGameObjectsFacade.unitTest=true;
		CKGameObjectsFacade.getEngine().loadScene("TEST_JUMP_OVER");
		//grid = CKGridFactory.getInstance().getAsset("TEST_JUMP_OVER",true);
		grid = CKGameObjectsFacade.getGrid();
		actor1=new CKGridActor("babySprite", Direction.SOUTHWEST);
		SpellSatisfies mSat = new SpellSatisfies(CKPropertyStrings.CH_MOVE, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);		
		CKTrigger trigger = new CKTrigger(mSat,new CKMoveActorCmd());
		actor1.addTrigger(trigger);
		mSat = new SpellSatisfies(CKPropertyStrings.CH_MARK, CKPropertyStrings.P_ANY, 0, NumericalCostType.TRUE);		
		trigger = new CKTrigger(mSat,new CKMarkGridActor());
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
		grid.addToPosition(actor1,0,0);
		actor1.setDirection(Direction.SOUTHEAST);
		assertActorAt(actor1,0,0,0);
		for(int i =0;i<4;i++)
		{
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,0,0,0);
		}
		
		//4 should do it...
		CKSpellCast jump1 = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,4,"");
		
		
		jump1.castSpell();
		assertActorAt(actor1,1,0,0);
		
		
		for(int i =4;i<6;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,1,0,0);
		}
		for(int i =6;i<10;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,2,0,0);
		}
		for(int i =10;i<18;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,3,0,0);
		}
		for(int i =18;i<34;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,4,0,0);
		}		
		for(int i =34;i<66;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,5,0,0);
		}		
		
		for(int i =66;i<130;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,0);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,6,0,0);
		}		
		//test jump off edge
		grid.addToPosition(actor1,0,0);
		actor1.setDirection(Direction.SOUTHEAST);			
		jump1 = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,400,"");
		
		jump1.castSpell();
		assertActorAt(actor1,6,0,0);//there is no 7,0
	}
	
	@Test
	public void testOver()
	{
		
		String step1 = "You SHOULD NOT LAND HERE";
		String step2 = "THANKS!";
		//poison the grid
		for (int i = 1;i<6;i++)
		{
			SpellSatisfies sat = new SpellSatisfies(CH_WORLD,P_ON_STEP,0,NumericalCostType.TRUE);
			
			CKSpellCast cast = new CKSpellCast(null,null,CH_MARK,step1,3,""+i+",1");
			CKGameAction stepOnAction = new CKReactiveSpellAction(cast);
			
			CKTrigger trig = new CKTrigger(sat,stepOnAction);
			
			grid.getTopPosition(new CKPosition(i,1)).addTrigger(trig);
		}
		//put a response on the target to ensure that triggers are working.
		SpellSatisfies sat = new SpellSatisfies(CH_WORLD,P_ON_STEP,0,NumericalCostType.TRUE);
		
		CKSpellCast cast = new CKSpellCast(null,null,CH_MARK,step2,3,"6,1");
		CKGameAction stepOnAction = new CKReactiveSpellAction(cast);
		
		CKTrigger trig = new CKTrigger(sat,stepOnAction);
		
		grid.getTopPosition(new CKPosition(6,1)).addTrigger(trig);
		System.out.println("HI:"+grid.getTopPosition(new CKPosition(6,1)).getPersonalTriggers());
		
		

		grid.addToPosition(actor1,0,1);
		actor1.setDirection(Direction.SOUTHEAST);			
		CKSpellCast jump1 = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,300,"");
		
		jump1.castSpell();
		assertActorAt(actor1,6,1,0);//there is no 7,1
		
		//now check for marks
		
		assertEquals(false,actor1.hasChapter(step1));
		assertEquals(true,actor1.hasChapter(step2));
		
		
	}
	
	
	@Test
	public void testObstruction()
	{
		grid.addToPosition(actor1,0,5);
		actor1.setDirection(Direction.SOUTHEAST);
		assertActorAt(actor1,0,5,0);
		
		CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,10,"");
		jump.castSpell();
		assertActorAt(actor1,1,5,0);
		
		
		//land in unlandable??
		grid.addToPosition(actor1,0,4);
		actor1.setDirection(Direction.SOUTHEAST);
		assertActorAt(actor1,0,4,0);
		
		jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,10,"");
		jump.castSpell();
		assertActorAt(actor1,1,4,0);		
		
	}
	
	@Test
	public void testOverPit()
	{
		
		for(int i =1;i<10;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,6);
			actor1.setDirection(Direction.SOUTHEAST);	
			assertActorAt(actor1,0,6,1);
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,0,6,1);
		}

		
		for(int i =10;i<18;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,6);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			System.out.println(grid.getPosition(new CKPosition(3,6)).getTotalHeight());
			
			assertActorAt(actor1,3,6,1);
		}
		
		
		for(int i =18;i<34;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,6);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,4,6,0);
		}
		
		for(int i =34;i<133;i++)
		{
			//System.out.println("CP is "+i);
			grid.addToPosition(actor1,0,6);
			actor1.setDirection(Direction.SOUTHEAST);			
			
			CKSpellCast jump = new CKSpellCast(actor1,actor1,CH_MOVE,P_JUMP_OVER,i,"");
			jump.castSpell();
			assertActorAt(actor1,4,6,0);
		}
		

	}
	
}
