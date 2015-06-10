package ckCommonUtils;

public class InterpolationTools
{

	
	
	public static double calcPercentBetween(double start,double pos, double end)
	{
		return (pos-start)/(end-start);
	}
	
	public static double calcLinearIterpolation(double percent,double start, double end)
	{
		double a = (1-percent)*start;
		double b = percent*end; 
		return a+b; 
		
	}
}
