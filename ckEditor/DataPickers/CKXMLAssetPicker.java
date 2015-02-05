package ckEditor.DataPickers;

import java.util.Iterator;
import javax.swing.JComponent;
import ckCommonUtils.CKXMLAsset;

public class CKXMLAssetPicker<T extends CKXMLAsset<T>> extends CKPicker<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3260611283568905034L;

	public CKXMLAssetPicker(Iterator<T> iter)
	{
		super(iter);
	}

	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(T asset)
	{
		// TODO Auto-generated method stub
		//FIXME--need to view a grid item....
		return asset.getXMLAssetViewer();
	}
		
}
