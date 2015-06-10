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
			long hits = Stream.of(offsets).map(e->pos.add(e))
					.filter(e->
					{
						for(CKPosition p:targets)
						{
							if(p.almostEqual(e, .001))
							{return true;}
						}
						return false;
				
					}).count();
			
			
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
		CharacterActionDescription [] actions = {swing};
				
		
		
		dgrid.updateGrid(Arrays.asList(targets), actions);
		
		dgrid.generateNodeValues(12,false);
		
		dgrid.PrettyPrintNodeValues(Direction.SOUTHEAST);
		dgrid.PrettyPrintNodeValues(Direction.SOUTHWEST);
		dgrid.PrettyPrintNodeValues(Direction.NORTHWEST);
		dgrid.PrettyPrintNodeValues(Direction.NORTHEAST);
		
		
		
	}

}
