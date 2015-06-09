package ckCommonUtils;

public class InterpolationTools
{

	
	
	public static double calcPercentBetween(double start,double pos, double end)
	{
		return (pos-start)/(end-start);
	}
	
	public static double calcLinearIterpolation(double percent,double start, double end)
	{
		return (1-percent)*start + percent*end;
		
	}
}
