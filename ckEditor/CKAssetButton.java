package ckEditor;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKNullAsset;

public class CKAssetButton extends JButton
{
	
	private static final long serialVersionUID = -8340961432656912495L;
	CKGraphicsAsset asset;
	
	public CKAssetButton()
	{
		this("axe");
	}
	//TODO would like to use this directly with graphics asset to handle animations...
	public CKAssetButton(String assetID)
	{
		this( CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID) );		
	}
	
	public CKAssetButton(CKGraphicsAsset asset)
	{
		super();
		this.asset = asset;
		setIcon( asset );
	}

	public void setIcon(CKGraphicsAsset asset)
	{
		this.asset = asset;
		if(asset != CKNullAsset.getNullAsset())
		{
			Dimension d = getPreferredSize();
			int min = Math.min(d.height, d.width)-5;
			super.setIcon( new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(
					asset, 0,0,min,min)) );
		}
	}

	public void setIcon(String assetID)
	{
		setIcon(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID) );
	}
	public CKGraphicsAsset getAsset()
	{
		return asset;
	}
	
}
