package ckEditor;

import javax.swing.JPanel;

import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKImageAsset;

public class AssetFactory 
{
	

	
	public static void addAssetToEditor(CKImageAsset i, Editor e)
	{
		e.setImageAsset(i);
	}
	
	public static CKImageAsset getImageAssetFromDB(String aid)
	{
		CKGraphicsAssetFactory fact=CKGraphicsAssetFactoryXML.getInstance();
		CKImageAsset ia=(CKImageAsset) fact.getGraphicsAsset(aid);
		return ia;
	}
	
	public static JPanel createImageAssetEditor(String aid, ColorChooser c)
	{
		CKGraphicsAssetFactory fact=CKGraphicsAssetFactoryXML.getInstance();
		CKImageAsset ia=(CKImageAsset) fact.getGraphicsAsset(aid);
		ImageAssetEditor iae=new ImageAssetEditor(c);
		iae.getEditor().setImageAsset(ia);
		return iae;
	}
	
}
