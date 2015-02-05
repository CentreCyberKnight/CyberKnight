package ckEditor;

import javax.swing.JFrame;
import org.jdesktop.swingx.MultiSplitLayout;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKAssetUsagePicker;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKImageAsset;

public class CKImageAssetEditorPane extends CKAssetEditorPane
{

	private static final long serialVersionUID = 3828622010319116084L;
	CKAssetViewer view;
	CKAssetCellViewer still;
	CKImageAssetPropertiesEditor ed;
	CKAssetUsagePicker pick;
	
	public CKImageAssetEditorPane(CKImageAsset asset)
	{
		super(asset);
		String layoutDef = 
			    "(ROW (LEAF name=left weight=1.0) (COLUMN top middle (LEAF name=bottom weight=1.0)))";
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		getMultiSplitLayout().setModel(modelRoot);
		
		view = new CKAssetViewer(8, asset,null);
		still = new CKAssetCellViewer(asset);
		pick = new CKAssetUsagePicker(asset);
		ed = new CKImageAssetPropertiesEditor(asset);
		ed.addChangeListener(still);
		
		add(view,"left");
		add(still,"bottom");
		add(pick,"middle");
		add(ed,"top");
			
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		JFrame frame = new JFrame();
		CKImageAsset water=(CKImageAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("green");		
		
		
		CKImageAssetEditorPane view=new CKImageAssetEditorPane(water);
		//CKImageAssetEditorPane view=new CKImageAssetEditorPane(new CKImageAsset());
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
		
		
	}




	@Override
	public void storeState()
	{
		pick.saveUsages();
		
	}

}
