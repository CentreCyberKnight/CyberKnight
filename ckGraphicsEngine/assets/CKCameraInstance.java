package ckGraphicsEngine.assets;

import java.awt.Point;

import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;

public class CKCameraInstance extends CKAssetInstance
{
	
	private CKCoordinateTranslator trans;
	private boolean On;
	
	
	public CKCameraInstance(CKPosition pos,CKCoordinateTranslator t)
	{
		super(pos, CKNullAsset.getNullAsset(), -10);
		trans=t;
		On=true;
	}

	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.assets.CKAssetInstance#moveBy(double, double, double)
	 */
	@Override
	public void moveBy(double x, double y, double z)
	{
		super.moveBy(x, y, z);
		Point p = new Point((int)getPosition().getX(),(int)getPosition().getY());
		if(trans.confineToMapCoords(p))
		{
			//System.out.println("backing out!!");
			super.moveBy(-x,-y,-z);
		}
	}



	public void turnOffCamera()
	{
		On=false;
	}
	
	public void turnOnCamera()
	{
		On=true;
	}
	
	
	public void recenterCamera()
	{
		if(On)
		{
			//where is the camera?
			Point p = trans.convertMapToScreen(getPosition());
			//	where is the centre of the the screen?
			int dx = trans.getScreenWidth()/2 - p.x;
			int dy = trans.getScreenHeight()/2 - p.y;
			trans.shiftWorldOffset(dx, dy);
		}
		
	}
	
	
}
