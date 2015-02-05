package ckEditor;

import org.jdesktop.swingx.MultiSplitPane;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKGraphicsAsset;

abstract public class CKAssetEditorPane extends MultiSplitPane 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CKGraphicsAsset asset;
	
	public CKAssetEditorPane(CKGraphicsAsset asset)
	{
		this.asset=asset;
	}
	
	
	public void saveAsset()
	{
		
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(asset);
		storeState();
	}
	
	public void saveAsNewAsset()
	{
		
		asset.setAID("");
		CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(asset);
		storeState();
	}
	
	public CKGraphicsAsset getAsset()
	{
		return asset;
	}

	abstract public void storeState();
	
	

}
