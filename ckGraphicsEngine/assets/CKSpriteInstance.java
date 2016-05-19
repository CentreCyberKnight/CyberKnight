package ckGraphicsEngine.assets;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import javafx.scene.canvas.GraphicsContext;
import ckCommonUtils.CKPosition;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.UnknownAnimationError;

public class CKSpriteInstance extends CKAssetInstance  
{
	protected int presentRow;
	
		
public CKSpriteInstance(CKPosition pos,CKSpriteAsset a,int IID )
{
	super(pos,a,IID);
	presentRow=0;
}


	
public void drawToGraphics (Graphics g,int frame,
		ImageObserver observer,CKCoordinateTranslator translator)
{
	drawToGraphics (g,frame,presentRow,observer,translator);
}

	
	
/* (non-Javadoc)
 * @see ckGraphicsEngine.assets.CKAssetInstance#drawToGraphics(javafx.scene.canvas.GraphicsContext, int, java.awt.image.ImageObserver, ckGraphicsEngine.CKCoordinateTranslator)
 */
@Override
public void drawToGraphics(GraphicsContext g, int frame,
		ImageObserver observer, CKCoordinateTranslator translator)
{
	drawToGraphics (g,frame,presentRow,observer,translator);
}



public void setAnimation(String animation) throws UnknownAnimationError 
{
	presentRow = ((CKSpriteAsset) asset).getRowIndex(animation);
}



public int getRowIndex(String animation) throws UnknownAnimationError 
{
	return ((CKSpriteAsset)asset).getRowIndex(animation);
}

public int getAnimationLength(String animation) throws UnknownAnimationError
{	
	return ((CKSpriteAsset)asset).getAnimationLength(animation);
}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
