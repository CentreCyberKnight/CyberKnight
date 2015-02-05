package ckGameEngine;

import static org.junit.Assert.*;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import ckCommonUtils.CKPosition;
import ckGameEngine.actions.CKGameAction;
import ckGameEngine.actions.CKReactiveSpellAction;
import ckSatisfies.NumericalCostType;
import ckSatisfies.SpellSatisfies;
import ckTrigger.CKTrigger;
import static ckCommonUtils.CKPropertyStrings.*;

public class CKGridTest
{

	CKGrid grid;
	CKGridActor actor1;
	CKGridActor block;
	CKGridActor cardBoardBox;
	
	String step1 = "Stepped ON 5,8";
	String crush1 = "Crushed Cardboardbox";
	
	
	public void assertActorAt(CKAbstractGridItem actor,int x,int y,int z)
	{
		System.out.println("Actor is at:"+ actor.getPos());
		assertEquals(x,(int)actor.getPos().getX());
		assertEquals(y,(int)actor.getPos().getY());
		assertEquals(z,(int)actor.getPos().getZ());

	}
	
	public void assertDirection(CKGridActor actor, Direction d){
		 assertEquals(actor.getDirection(), d);
	}
	
	@Before
	public void setUp() throws Exception
	{
		CKGameObjectsFacade.unitTest=true;
		
		grid = new CKGrid(10,10);
		//now to place some tiles.
		
		
		
		for(int i =2;i<=8;i++)
			for(int j =2;j<=8;j++)
			{
				CKAbstractGridItem land= new CKGridItem();
				land.setAssetID("blue");
				land.setMoveCost(1);
				grid.setPosition(land, i, j);				
			}
		
		
		for(int i =4;i<=6;i++)
			for(int j =4;j<=6;j++)
			{
				CKAbstractGridItem block= new CKGridItem();
				block.setAssetID("pineBlock");
				block.setMoveCost(2);
				block.setItemHeight(1);
				grid.addToPosition(block, i,j);
			}
		
		CKAbstractGridItem bigBlock=new CKGridItem();
		bigBlock.setAssetID("stoneBlock");
		bigBlock.setMoveCost(1);
		bigBlock.setItemHeight(2);
		grid.addToPosition(bigBlock,5, 5);
		
		//build actor
		actor1=new CKGridActor("babySprite", Direction.NORTHEAST);
		actor1.setName("baby");
		actor1.setItemHeight(2);
		actor1.setItemWeight(10);
		grid.addToPosition(actor1,6,8);
		
		//cardboardbox
		/*cardBoardBox = new CKGridActor("dummy",Direction.NORTHEAST);
		cardBoardBox.setItemStrength(1);
		cardBoardBox.setItemWeight(1);
		SpellSatisfies sat1 = new SpellSatisfies(CH_ON_CRUSH,P_ANY,0,NumericalCostType.TRUE);
		
		CKSpellCast castCrush = new CKSpellCast(null,null,CH_MARK,crush1,3,"4,8");
		CKGameAction crushOnAction = new CKReactiveSpellAction(castCrush);
		
		//CkGameAction 
		
		add when I can see it!!
		
		//CKTrigger trig = new CKTrigger(sat1,crushOnAction);
		
		*/
		
		
		
		
		
		block = new CKGridActor("dummy",Direction.NORTHEAST);
		grid.addToPosition(block,6,7);
		
		
		SpellSatisfies sat = new SpellSatisfies(CH_WORLD,P_ON_STEP,0,NumericalCostType.TRUE);
		
		CKSpellCast cast = new CKSpellCast(null,null,CH_MARK,step1,3,"5,8");
		CKGameAction stepOnAction = new CKReactiveSpellAction(cast);
		
		CKTrigger trig = new CKTrigger(sat,stepOnAction);
		
		grid.getTopPosition(new CKPosition(5,8)).addTrigger(trig);
		grid.getTopPosition(new CKPosition(5,8)).setName("marking tile");
		
		
		
	}

	@Test
	public final void testPlacements()
	{
		assertEquals(3,grid.getPosition(5, 5).getTotalHeight() );
		assertEquals(1,grid.getPosition(5, 5).getFinalMoveCost() );
		assertEquals(1,grid.getPosition(4, 6).getTotalHeight() );
		assertEquals(2,grid.getPosition(4, 6).getFinalMoveCost() );
		assertEquals(0,grid.getPosition(2, 8).getTotalHeight() );
		assertEquals(1,grid.getPosition(2, 8).getFinalMoveCost() );
		assertEquals(0,grid.getPosition(5, 5).getItemHeight() ); //on bottom
		
		CKPosition pos = grid.getTopPosition(new CKPosition(5, 5)).getPos();
		
		assertEquals(5,(int)pos.getX());
		assertEquals(5,(int)pos.getY());
		assertEquals(1,(int)pos.getZ());//height of bottom of thinger.
		
		pos = grid.getTopPosition(new CKPosition(4, 6)).getPos();
		
		assertEquals(4,(int)pos.getX());
		assertEquals(6,(int)pos.getY());
		assertEquals(0,(int)pos.getZ());//height of bottom of thinger.
		
		
	
	}

	@Test
	public final void testMoves()
	{
		
		
		assertEquals(0,grid.getPosition(5, 8).getTotalHeight() ); 
		assertEquals(false,actor1.hasChapter(step1));
		
		grid.move(actor1, new CKPosition(5,8,0,0) );
		assertActorAt(actor1,5,8,0);
		//check for mark
		assertEquals(true,actor1.hasChapter(step1));
	
	}

	
	//public void duplicatePassOverlays
	
	@Test
	public final void testOverlays()
	{
		CKAbstractGridItem item = grid.getTopPosition(new CKPosition(5,5));
		
		assertEquals(1,grid.getPosition(5, 5).getFinalMoveCost() );
		assertEquals(2,grid.getPosition(4, 6).getFinalMoveCost() );
		
		CKGridActorOverLay lay1 = new CKGridActorOverLay();
		lay1.setMoveCost(-1);
		assertEquals(-1,lay1.getMoveCost());
		grid.addToPosition(lay1, 5, 5);
		assertEquals(0,lay1.getMoveCost());
		
		assertEquals(0,grid.getPosition(5, 5).getFinalMoveCost() );
		lay1.setMoveCost(10);
		assertEquals(10,lay1.getMoveCost());
		
		assertEquals(10,grid.getPosition(5, 5).getFinalMoveCost() );
		assertEquals(1,item.getMoveCost() );
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		grid.writeToStream(baos);
		
			
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		XMLDecoder e = new XMLDecoder(bais);
		//e.setPersistenceDelegate(getClass(), new ArtifactPersistenceDelegate());
		CKGrid grid2 = (CKGrid) e.readObject();
		e.close();	
		
		CKGridActorOverLay lay2 = (CKGridActorOverLay)
				grid2.getTopPosition(5,5);
		
		
		assertEquals(10,grid.getPosition(5, 5).getFinalMoveCost() );
		assertEquals(2,grid.getPosition(4, 6).getFinalMoveCost() );
		
		

		assertActorAt(lay2,5,5,1);
		assertActorAt(grid2.getTopPosition(5,5).getPrev(),5,5,1);
		
		
		//check that things added afterwards will work.
		CKGridItem topper = new CKGridItem();
		topper.setItemHeight(3);
		
		grid2.addToPosition(topper,5,5);
		
		
		assertActorAt(topper,5,5,3);
		assertEquals(6,grid2.getTopPosition(5,5).getTotalHeight());
		assertEquals(6,grid2.getPosition(5,5).getTotalHeight());
/*		assertEquals(0,grid.getPosition(5, 5).getPosFinalMoveCost() );
		lay1.setMoveCost(10);
		assertEquals(10,lay1.getMoveCost());
		
		assertEquals(10,grid.getPosition(5, 5).getFinalMoveCost() );
		assertEquals(1,item.getMoveCost() );
	*/	
	
		

		
				
	}


}
