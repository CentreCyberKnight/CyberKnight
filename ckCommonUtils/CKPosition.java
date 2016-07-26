package ckCommonUtils;

import java.awt.Point;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.python.modules.math;

import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.layers.CKGraphicsLayer;

/**
 * Provides a Position description with X,Y,Z and depth data.
 * Position data can be used to determine the order in which things are drawn to the screen.
 * When objects have similar positions, depth will be used to determine which should be drawn first.
 * Larger depth values are closer to the observer than negative values. 
 * @author Michael K. Bradshaw
 *
 */
public class CKPosition implements Comparable<CKPosition>,Cloneable
{ 
	private double x;
	private double y;
	private double z;
	private int depth;
	
	public CKPosition(double x,double y,double z,int depth)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.depth=depth;
	}
	
	public CKPosition()
	{
		this(0,0,0,0);
	}
	
	public CKPosition(double x,double y)
	{
		this(x,y,0,0);
	}
	
	
	public CKPosition(double x,double y,double z)
	{
		this(x,y,z,0);
	}

	public CKPosition(CKPosition pos)
	{
		this(pos.x,pos.y,pos.z,pos.depth);
	}

	public static double planarDistance(CKPosition p1, CKPosition p2) {
		double dx = p1.x-p2.x;
		double dy = p1.y-p2.y;
		double dz = p1.z-p2.z;
		return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)+Math.pow(dz,2));	
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int compareTo(CKPosition arg0) {
		
		
		//sort by y's. then depth, then x, then z
		double diffs[]={arg0.getY()-getY(),
				getDepth()-arg0.getDepth(), //count depth differently
				arg0.getX()-getX(),
				arg0.getZ()-getZ()};
				
	
		for( double d: diffs)
		{
			if(d!=0)
			{
				if(d<0){ return -1; }
				else {return 1;}
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object that) {
		if ( this == that ) {return true;}
		if ( !(that instanceof CKPosition) ) {return false;}
		
		CKPosition pos = (CKPosition)that;
		return(this.compareTo(pos)==0);
	}

	@Override
	public String toString() {
		String s="Position("+x+","+y+","+z+","+depth+")";
		return s;
	}
	
	public String getType(){
		return "CKPosition";
	}

	/**
	 * Interpolates the Position that is frac % between P1 and P2
	 * @param sPos - starting position
	 * @param ePos - ending position
	 * @param frac number between 0 and 1 that indicates how much the 
	 * @return Point that is frac between P1 and P2
	 */
	public static CKPosition interpolate(CKPosition sPos, CKPosition ePos, float frac) {
		return new CKPosition(
				(ePos.x-sPos.x)*frac+sPos.x,
				(ePos.y-sPos.y)*frac+sPos.y,
				(ePos.z-sPos.z)*frac+sPos.z,
				(int) ((ePos.depth-sPos.depth)*frac+sPos.depth));
	}

	//added method
	//quadratic easing in - accelerating from zero velocity 
	//Copyright © 2001 Robert Penner All rights reserved.
	public static CKPosition easingininterpolate(CKPosition sPos,CKPosition ePos, float frac){
		return new CKPosition(
				(ePos.x-sPos.x)*frac*frac+sPos.x,
				(ePos.y-sPos.y)*frac*frac+sPos.y,
				(ePos.z-sPos.z)*frac*frac+sPos.z,
				(int) ((ePos.depth-sPos.depth)*frac+sPos.depth));
	}
	
	//quadratic easing out - decelerating to zero velocity 
	//Copyright © 2001 Robert Penner All rights reserved.
	public static CKPosition easingoutinterpolate(CKPosition sPos,CKPosition ePos,float frac){
		return new CKPosition(
				(ePos.x-sPos.x)*(2*frac-frac*frac)+sPos.x,
				(ePos.y-sPos.y)*(2*frac-frac*frac)+sPos.y,
				(ePos.z-sPos.z)*(2*frac-frac*frac)+sPos.z,
				(int) ((ePos.depth-sPos.depth)*frac+sPos.depth));
	}
	
	
	public Object clone() {
	    try
	    {
	        return super.clone();
	    }
	    catch (CloneNotSupportedException e)
	    {
	
	        throw new InternalError(e.toString());
	    }
	}

	
	/**
	  * Nomalizes the position values for x,y,z. so that the distance will be 1.
	  * @return the magnitude of the original vector (x,y,z)
	  */
	public double normalize() {
		 double dist = Math.sqrt(x*x+y*y+z*z);
		 x=x/dist;
		 y=y/dist;
		 z=z/dist;
		 return dist;
	 }

	public final CKPosition scaleVector(double scale) {
		 
		 return new CKPosition(x*scale,y*scale,z*scale,0);	 
	 }

	public final CKPosition getDifferenceVector(CKPosition last) {
		 return new CKPosition(last.x-x,last.y-y,last.z-z,0);
		 
		 
	 }

	public void round() {
		 x=(double)Math.round(x);
		 y=(double)Math.round(y);
		 z=(double)Math.round(z);
	 }

	public boolean almostEqual(CKPosition pos, double tol) {
		 //sort by y's. then depth, then x, then z
		 double diffs[]={Math.abs(pos.getY()-getY()),
				 Math.abs(pos.getX()-getX()),
				 Math.abs(pos.getZ()-getZ())};
					
		
			for( double d: diffs)
			{
				if(d>tol)
				{
					return false;
				}
			}
			return true;
		 
	
	 }

	public CKPosition add(CKPosition origin)
	{
		return new CKPosition(x+origin.x,y+origin.y,
				z+origin.z,depth+origin.depth);
	}

	public CKPosition add(Point other)
	{
		return new CKPosition(x+other.x,y+other.y,
				z,depth);

	}
	
	public CKPosition rotate(double radians)
	{
		double xr = x*Math.cos(radians) - y*Math.sin(radians);
		double yr = x*Math.sin(radians) - y*math.cos(radians);
		return new CKPosition(xr,yr,z,depth);
	}
	public static void main(String[] args){
		Application.launch(Test.class,args);
	}
	
	public static class Test extends Application{
	
		@Override
		public void start(Stage stage){
			FX2dGraphicsEngine engine=new FX2dGraphicsEngine();
			engine.resize(600,600);
			engine.loadScene("Kitchen");
			CKPosition spos1=new CKPosition(7,9,0,0);
			CKPosition epos1=new CKPosition(7,9,15,0);
			CKPosition spos2=new CKPosition(9,7,0,0);
			CKPosition epos2=new CKPosition(9,7,15,0);
			CKPosition spos3=new CKPosition(8,8,0,0);
			CKPosition epos3=new CKPosition(8,8,15,0);
			try{
				
				int tid=engine.startTransaction(true);
				engine.loadAsset(tid, "illuminate");
				engine.loadAsset(tid, "Swirl");
				engine.loadAsset(tid, "SlowSwirl");
				int spriteID1=engine.createInstance(tid, "illuminate", spos1,0, CKGraphicsLayer.SPRITE_LAYER);
				engine.move(tid, spriteID1, 60, spos1, epos1, 5);
				int spriteID2=engine.createInstance(tid, "Swirl", spos2, 0, CKGraphicsLayer.SPRITE_LAYER);
				engine.move(tid, spriteID2, 60, spos2, epos2, 5,CKPosition::easingininterpolate);
				int spriteID3=engine.createInstance(tid, "SlowSwirl", spos3, 0, CKGraphicsLayer.SPRITE_LAYER);
				engine.move(tid, spriteID3, 60, spos3, epos3, 5, CKPosition::easingoutinterpolate);
				engine.endTransaction(0, false);}
			catch (BadInstanceIDError e){}
			catch (LoadAssetError e){}
	
			HBox root = new HBox();
			Scene scene = new Scene(root,600,600);
			stage.setScene(scene);
	
			engine.maxWidth(Double.MAX_VALUE);
			engine.maxHeight(Double.MAX_VALUE);
			engine.widthProperty().bind(root.widthProperty());
			engine.heightProperty().bind(root.heightProperty());
			root.getChildren().addAll(engine);
			stage.setTitle("Tutorial1");
			stage.show();

		
		}
	
	}

}