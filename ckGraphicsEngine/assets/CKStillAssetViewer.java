package ckGraphicsEngine.assets;

import java.awt.Dimension;

public class CKStillAssetViewer extends CKAssetViewer
{

	private static final long serialVersionUID = 1817247736749882565L;

	public CKStillAssetViewer(double targetfps, CKGraphicsAsset a, Dimension D)
	{
		super(targetfps, a, D);
	}

	public CKStillAssetViewer(double targetfps, CKGraphicsAsset a, Dimension D,
			boolean extended)
	{
		super(targetfps, a, D, extended);
	}


	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGraphicsEngine.assets.CKAssetViewer#calcState()
	 */
	@Override
	public void calcState()
	{
		//do nothing, we don't want it to change
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
