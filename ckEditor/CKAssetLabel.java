package ckEditor;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKAssetLabel extends JLabel
{
	
	private static final long serialVersionUID = 5518735224049387491L;
	CKGraphicsAsset asset;
	
	//TODO would like to use this directly with graphics asset to handle animations...
	public CKAssetLabel(String string)
	{
		this(CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(string) );		
	}
	
	public CKAssetLabel(CKGraphicsAsset asset)
	{
		super();
		this.asset = asset;
		setIcon( new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(
				asset, 0,0,96,96) ) );
	}
	
}
