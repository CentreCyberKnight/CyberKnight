package ckEditor;

import javax.swing.JFrame;

import ckCommonUtils.CKEntitySelectedListener;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckGraphicsEngine.assets.CKCompositeAsset;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.CKRegulatedAsset;
import ckGraphicsEngine.assets.CKSharedAsset;

public class CKAssetEditorFactory
{

	public static CKAssetEditorPane getEditor(CKGraphicsAsset asset)
	{
		
		if( asset instanceof CKImageAsset)
		{
			return new CKImageAssetEditorPane((CKImageAsset) asset);
		}
		else if (asset instanceof CKRegulatedAsset)
		{
			return new CKRegulatedAssetEditorPane((CKRegulatedAsset) asset);
		}
		else if(asset instanceof CKSharedAsset)
		{
			return new CKSharedAssetEditorPane((CKSharedAsset)asset );
		}
		else if(asset instanceof CKCompositeAsset)
		{
			return new CKCompositeAssetEditor((CKCompositeAsset) asset);
		}
		else
		{
			return null;
		}
		
	}
	
	
	/**
	 * 
	 */
	
	public static void chooseAsset(CKEntitySelectedListener<CKAssetEditorPane> listener)
	{
		JFrame frame = new JFrame();

		CKFilteredAssetPicker filter = new CKFilteredAssetPicker();
		filter.addSelectedListener(new AssetWaiter(listener));
		

		frame.add(filter);
		frame.pack();
		frame.setVisible(true);
	
		
		
		
	}
	
	static class AssetWaiter implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		CKEntitySelectedListener<CKAssetEditorPane> listener;
		
		public AssetWaiter(CKEntitySelectedListener<CKAssetEditorPane> listener)
		{
			this.listener = listener;
		}
		
		@Override
		public void entitySelected(CKGraphicsAsset entity)
		{
			listener.entitySelected(CKAssetEditorFactory.getEditor(entity));
		}
		
	}
	
}
