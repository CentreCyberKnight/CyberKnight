package testing;

import static ckCommonUtils.CKPropertyStrings.P_END_TURN;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.AimDescriptionFactory;
import ckGameEngine.AimDescription;
import ckGameEngine.CKAbstractGridItem;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItem;
import ckGameEngine.DescisionGrid;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.DescisionGrid.DecisionNode;
import ckGameEngine.Direction;


public class testDescisionGrid
{
	static CKGrid grid;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		//grid=CKGridFactory.getInstance().getAsset("Fred");
		grid = new CKGrid(10,15);
		
		for (int i = 1; i < 10; i++)
			for (int j = 1; j < 15; j++)
			{
				CKAbstractGridItem land = new CKGridItem();
				land.setAssetID("blue");
				land.setMoveCost(1);
				grid.setPosition(land, i, j);
			}
		
		CKAbstractGridItem dude1 = new CKGridItem();
		dude1.setMoveCost(100);
		dude1.setItemHeight(3);
		grid.setPosition(dude1, 5, 5);
		
		CKAbstractGridItem dude2 = new CKGridItem();
		dude2.setMoveCost(100);
		dude2.setItemHeight(3);
		grid.setPosition(dude2, 5, 7);
		
		
		
	}

	//@Test
	public void testNotMoving()
	{
		System.out.println("TestNotMoving\n");
		DescisionGrid dgrid = new DescisionGrid(grid);
		//need Collection of Target positions
		CKPosition[] targets = {new CKPosition(5,5),new CKPosition(5,6)};
		//and array of CharacterActionDescriptions
		
		
		BiFunction<CharacterActionDescription,DecisionNode,double[]> swingFunction 
		=(cad,node)->
		{
			CKPosition pos = node.position;
			//double x = node.position.getX();
			//double y = node.position.getY();
			
			
			AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
			AimDescription aim = factory.getAsset(cad.targetType);
			CKPosition [] offsets = aim.getOffsets(node.direction);
			//Set<CKPosition> poses = Stream.of(offsets).map(e->pos.add(e)).collect(Collectors.toSet());
			long hits = targetHitStream(pos,targets,offsets).count();
			
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-2)*hits;
			}
			return utils;
		};
		
		int [] costs = {3,20};
		CharacterActionDescription swing = 
				new CharacterActionDescription("Swing", CKPropertyStrings.P_SWIPE,
				costs, false, true, false, 0, swingFunction,null);			
		
				
		BiFunction<CharacterActionDescription,DecisionNode,double[]> farDistanceFunction 
		=(cad,node)->
		{//only one target, so no need to check how many you hit!
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-3);
			}
			return utils;
		};
		
		int [] fcosts = {5,20};
		CharacterActionDescription far = 
				new CharacterActionDescription("FAR", CKPropertyStrings.P_SHORT_TARGET,
				fcosts, false, true, false, 0, farDistanceFunction,null);			
		
		
		CharacterActionDescription [] actions = {swing,far};
		
		dgrid.updateGrid(Arrays.asList(targets), Arrays.asList(actions));
		
		dgrid.generateNodeValues(12,false);
		
		dgrid.PrettyPrintNodeValues(Direction.SOUTHEAST);
		//dgrid.PrettyPrintNodeValues(Direction.SOUTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHEAST);
		
		
		
	}
	
	public static Stream<CKPosition> targetHitStream( CKPosition origin, 
				CKPosition[] targets, CKPosition[] offsets)
	{
		return Stream.of(offsets).map(e->origin.add(e))
				.filter(e->
				{
					for(CKPosition p:targets)
					{
						if(p.almostEqual(e, .001))
						{return true;}
					}
					return false;
		
				});
		
	}
	
	public static void getCmds(GridNode node)//,CKPosition destination)
	{
		//need to do the last thing first!
		GridNode parent = node.getParentNode(); 
		if(parent!=null) { getCmds(parent); }
		//now do my action
		String action = node.getAction();
		int cp = node.getActionCost();
		

		System.err.println("MoveTO "+action+" "+cp+":"+node.getPos()+node.getDir());
		if(action.equals("") || action.equals(P_END_TURN))
		{
			return; //we are done
		}
		//		CKSpellCast cast = new CKSpellCast(getCharacter(), getCharacter(),
	//			CH_MOVE, action, cp, "");
		//cast.castSpell();
	}
	
	
	
	//@Test
	public void testNotMovingFriendly()
	{
		System.out.println("TestNotMovingFriendly\n");
		DescisionGrid dgrid = new DescisionGrid(grid);
		//need Collection of Target positions
		CKPosition[] targets = {new CKPosition(5,5),new CKPosition(5,6),new CKPosition(5,7)};
		CKPosition[] friendlyTargets = {new CKPosition(5,6)};
		CKPosition[] foeTargets = {new CKPosition(5,5),new CKPosition(5,7)};
		
		//and array of CharacterActionDescriptions
		
		
		BiFunction<CharacterActionDescription,DecisionNode,double[]> swingFunction 
		=(cad,node)->
		{
			CKPosition pos = node.position;
			//double x = node.position.getX();
			//double y = node.position.getY();
			
			
			AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
			AimDescription aim = factory.getAsset(cad.targetType);
			CKPosition [] offsets = aim.getOffsets(node.direction);
			
			long goodHits = targetHitStream(pos,foeTargets,offsets).count();
			long badHits = targetHitStream(pos,friendlyTargets,offsets).count();
			
			
			
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-2)*(goodHits-1.5*badHits);
			}
			return utils;
		};
		
		int [] costs = {3,20};
		CharacterActionDescription swing = 
				new CharacterActionDescription("Swing", CKPropertyStrings.P_SWIPE,
				costs, false, true, false, 0, swingFunction,null);			
		
				
		BiFunction<CharacterActionDescription,DecisionNode,double[]> farDistanceFunction 
		=(cad,node)->
		{//only one target, so no need to check how many you hit!
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-3);
			}
			return utils;
		};
		
		int [] fcosts = {5,20};
		CharacterActionDescription far = 
				new CharacterActionDescription("FAR", CKPropertyStrings.P_SHORT_TARGET,
				fcosts, false, true, false, 0, farDistanceFunction,null);			
		
		
		CharacterActionDescription [] actions = {swing,far};
		
		dgrid.updateGrid(Arrays.asList(targets), Arrays.asList(actions));
		
		dgrid.generateNodeValues(12,false);
		
		dgrid.PrettyPrintNodeValues(Direction.SOUTHEAST);
		//dgrid.PrettyPrintNodeValues(Direction.SOUTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHEAST);
		
		
		
	}
	
	
	@Test
	public void testMoveAndShoot()
	{
		System.out.println("TestMovingFriendly\n");
		DescisionGrid dgrid = new DescisionGrid(grid);
		//need Collection of Target positions
		CKPosition[] targets = {new CKPosition(5,5),new CKPosition(5,7)};
		CKPosition[] friendlyTargets = {};//new CKPosition(5,6)};
		CKPosition[] foeTargets = {new CKPosition(5,5),new CKPosition(5,7)};
		
		//and array of CharacterActionDescriptions
		
		
		BiFunction<CharacterActionDescription,DecisionNode,double[]> swingFunction 
		=(cad,node)->
		{
			CKPosition pos = node.position;
			//double x = node.position.getX();
			//double y = node.position.getY();
			
			
			AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
			AimDescription aim = factory.getAsset(cad.targetType);
			CKPosition [] offsets = aim.getOffsets(node.direction);
			
			long goodHits = targetHitStream(pos,foeTargets,offsets).count();
			long badHits = targetHitStream(pos,friendlyTargets,offsets).count();
			
			
			
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-2)*(goodHits-1.5*badHits);
			}
			return utils;
		};
		
		int [] costs = {3,20};
		CharacterActionDescription swing = 
				new CharacterActionDescription("Swing", CKPropertyStrings.P_SWIPE,
				costs, true, true, false, 0, swingFunction,null);			
		
				
		BiFunction<CharacterActionDescription,DecisionNode,double[]> farDistanceFunction 
		=(cad,node)->
		{//only one target, so no need to check how many you hit!
			double []utils=new double[cad.costs.length];
			for(int i=0;i<utils.length;i++)
			{
				utils[i] = (cad.costs[i]-3);
			}
			return utils;
		};
		
		int [] fcosts = {5,20};
		CharacterActionDescription far = 
				new CharacterActionDescription("FAR", CKPropertyStrings.P_SHORT_TARGET,
				//fcosts, false, true, false, 0, farDistanceFunction);			
				fcosts, true, true, false, 0, farDistanceFunction,null);			
		
		
		CharacterActionDescription [] actions = {swing,far};
		//CharacterActionDescription [] actions = {far};
		
		dgrid.updateGrid(Arrays.asList(targets), Arrays.asList(actions));
		
		
		//now calcualte movement stuff
		
		
		CKGridActor baby = new CKGridActor("babySprite",Direction.NORTHWEST);
		baby.setPos(new CKPosition(5,4));
		
		GridNode [][][][] movement= grid.allPositionsReachable(baby, 22, 1);
		
		//dgrid.generateNodeValues(12,false);
		dgrid.generateNodeValues(movement, 22);
		
		
		//System.out.println("Values for "+Direction.SOUTHEAST);
		//dgrid.PrettyPrintNodeSummary(Direction.SOUTHEAST);
		//System.out.println("Values for "+Direction.SOUTHWEST);
		//dgrid.PrettyPrintNodeSummary(Direction.SOUTHWEST);
		System.out.println("Values for "+Direction.NORTHWEST);
		dgrid.PrettyPrintNodeSummary(Direction.NORTHWEST);
		//System.out.println("Values for "+Direction.NORTHEAST);
		//dgrid.PrettyPrintNodeSummary(Direction.NORTHEAST);
		
		/*
		System.out.println("Values for "+Direction.SOUTHEAST);
		dgrid.PrettyPrintNodeValues(Direction.SOUTHEAST);
		System.out.println("Values for "+Direction.SOUTHWEST);
		dgrid.PrettyPrintNodeValues(Direction.SOUTHWEST);
		*/System.out.println("Values for "+Direction.NORTHWEST);
		dgrid.PrettyPrintNodeValues(Direction.NORTHWEST);
		dgrid.PrettyPrintNodeActions(Direction.NORTHWEST);
		
		/*System.out.println("Values for "+Direction.NORTHEAST);
		dgrid.PrettyPrintNodeValues(Direction.NORTHEAST);
		*/
		
		getCmds(movement[5][5][Direction.NORTHEAST.ordinal()][0]);
		
		
	}

}
