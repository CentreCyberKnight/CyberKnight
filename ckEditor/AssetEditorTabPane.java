package ckEditor;

import ckCommonUtils.CKTabPane;

public class AssetEditorTabPane extends CKTabPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8072564284998711122L;
	ColorChooser chooser;
	
	public AssetEditorTabPane(ColorChooser c)
	{
		super();
		chooser=c;
	}
	
	public void addEditorTab()
	{
		this.addTab(new ImageAssetEditor(chooser), "");
	}
	
	
}
