package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.AimDescriptionFactory;
import ckDatabase.CKGridFactory;
import ckGameEngine.AimDescription;
import ckGameEngine.CKGrid;
import ckGameEngine.DescisionGrid;
import ckGameEngine.Direction;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.DescisionGrid.DecisionNode;
import ckGameEngine.Grid;
import static ckCommonUtils.CKPropertyStrings.*;


public class testDescisionGrid
{
	static CKGrid grid;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		//grid=CKGridFactory.getInstance().getAsset("Fred");
		grid = new CKGrid(10,15);
		
	}

	@Test
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
			double x = node.position.getX();
			double y = node.position.getY();
			
			
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
				costs, false, true, false, 0, swingFunction);			
		
				
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
				fcosts, false, true, false, 0, farDistanceFunction);			
		
		
		CharacterActionDescription [] actions = {swing,far};
		
		dgrid.updateGrid(Arrays.asList(targets), actions);
		
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
	
	
	
	@Test
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
			double x = node.position.getX();
			double y = node.position.getY();
			
			
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
				costs, false, true, false, 0, swingFunction);			
		
				
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
				fcosts, false, true, false, 0, farDistanceFunction);			
		
		
		CharacterActionDescription [] actions = {swing,far};
		
		dgrid.updateGrid(Arrays.asList(targets), actions);
		
		dgrid.generateNodeValues(12,false);
		
		dgrid.PrettyPrintNodeValues(Direction.SOUTHEAST);
		//dgrid.PrettyPrintNodeValues(Direction.SOUTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHWEST);
		//dgrid.PrettyPrintNodeValues(Direction.NORTHEAST);
		
		
		
	}

}
