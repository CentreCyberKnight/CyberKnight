/**
 * 
 */
package ckCommonUtils;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * @author michael.bradshaw
 *
 */
public class StreamOperators
{
	

	public static void forXY(int xMax, int yMax, BiIntConsumer fcn)
	{
		forXY(0, xMax, 0, yMax, fcn);
	}

	public static void forXY(int xMin, int xMax, int yMin, int yMax,
			BiIntConsumer fcn)
	{
		IntStream.range(xMin, xMax).forEach(
				x -> IntStream.range(yMin, yMax).forEach(y -> fcn.apply(x, y)));
	}

	public static void forXYFiltered(int xMin, int xMax, int yMin, int yMax,
			IntPredicate xFilter, IntPredicate yFilter, BiIntConsumer fcn)
	{
		IntStream
				.range(xMin, xMax)
				.filter(xFilter)
				.forEach(
						x -> IntStream.range(yMin, yMax).filter(yFilter)
								.forEach(y -> fcn.apply(x, y)));
	}

	public static void forXYFiltered(int xMin, int xMax, int yMin, int yMax,
			BiIntPredicate xyFilter,
			BiIntConsumer fcn)
	{
		IntStream
				.range(xMin, xMax)//.boxed()
				.forEach(
						x -> IntStream.range(yMin, yMax)//.boxed()
						.filter(y->xyFilter.test(x,y))
						.forEach(y -> fcn.apply(x, y)));
	}
	
	
	public static void forXYFiltered(int xMin, int xMax, int yMin, int yMax,
			int width,int height,BiIntConsumer fcn)
			{
				int myXMin = xMin>0?xMin:0;
				int myYMin = yMin>0?yMin:0;
				int myXMax = xMax<width?xMax:width;
				int myYMax = yMax<width?yMax:height;
				
				forXY(myXMin,myXMax,myYMin,myYMax,fcn);		
			}
	
	
	public static void forXYFiltered(int xMin, int xMax, int yMin, int yMax,
			int width,int height,BiIntPredicate xyFilter,
			BiIntConsumer fcn)
			{
				int myXMin = xMin>0?xMin:0;
				int myYMin = yMin>0?yMin:0;
				int myXMax = xMax<width?xMax:width;
				int myYMax = yMax<width?yMax:height;
				
				forXYFiltered(myXMin,myXMax,myYMin,myYMax,xyFilter,fcn);		
			}
	
	
	
	
	
	public static BiIntPredicate DonutTest(int x,int y,int min,int max)
//	int width,int height)
	{
		return (px,py)->
		{
			int dx = px-x;
			int dy = py-y;
			double distance =Math.sqrt(dx*dx + dy*dy);
			return (distance>=min && distance <=max);							
		};
	}
	
	public static BiIntPredicate DonutTestWithBounds(int x,int y,int min,int max,
	int width,int height)
	{
		return (px,py)->
		{
			int dx = px-x;
			int dy = py-y;
			double distance =Math.sqrt(dx*dx + dy*dy);
			return px>=0 && px<width && 
					py>=0 && py<height && 
					distance>=min && distance <=max;							
		};
	}
				
	
	public static void forXYBoundedDonut(int x, int y, int min, int max,
			int width,int height,BiIntConsumer fcn)
			{
				forXYFiltered(x-max,x+max,y-max,y+min,width,height,
						DonutTest(x,y,min,max),fcn);		
			}
	
	
	public static void main(String[] s)
	{
		forXY(5, 5, (x, y) -> {
			System.out.println("" + x + "," + y);
		});
		
		
		forXYFiltered(-5,5,-5,5,
				(x)->x%2==0,
				(y)->y%2==1,
				(x,y)->System.out.println(""+x+","+y));
		
		forXYFiltered(-5,15,-5,25,
				5,7,
				(x,y)->System.out.println("F"+x+","+y));
		
		forXYFiltered(0,10,0,20,(x,y)->{return (x+y)<10;},
				(x,y)->System.out.println("F"+x+","+y));
		
		forXYFiltered(-10,10,-20,20,StreamOperators.DonutTestWithBounds(2, 3, 2, 5,5,6),
				(x,y)->System.out.println("D"+x+","+y));
		
		
		
	}

}
