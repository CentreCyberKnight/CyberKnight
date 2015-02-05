package ckGraphicsEngine.assets;

import ckCommonUtils.CKPosition;
/**
 * This Class extends the SpriteInstance by 
 * automatically changing the animation whenever the sprite's vector changes.
 * @author bradshaw
 *
 */
public class CKVectoredSpriteInstance extends CKSpriteInstance
{

	public CKVectoredSpriteInstance(CKPosition pos, CKSpriteAsset a, int IID)
	{
		super(pos, a, IID);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ckGraphicsEngine.CKAssetInstance#moveBy(double, double, double)
	 */
	@Override
	public void moveBy(double x, double y, double z)
	{
		int anim=presentRow/4;
		int offset=0;
		if(Math.abs(x)>Math.abs(y))
		{
			if(x>0) {offset=0;}
			else    {offset=2;}
		}
		else
		{
			if(y>0) {offset=1;}
			else    {offset=3;}
		}
		presentRow=anim+offset;	
		
		super.moveBy(x, y, z);
	}
	
	
	
	
	
	
	

}
