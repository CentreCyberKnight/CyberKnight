package ckEditor;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKGridItemFactory;
import ckDatabase.CKXMLFactory;
import ckEditor.DataPickers.CKXMLAssetUsagePicker;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGridItem;

public class CKXMLAssetEditorPane<T extends CKXMLAsset<T>,F extends CKXMLFactory<T> > extends MultiSplitPane implements ChangeListener
{
	
	private static final long serialVersionUID = -9000583358257433626L;
	
	
	protected T asset;
	private F afactory;
	//private CKGridItemPicker itemPicker;
	private CKXMLAssetPropertiesEditor<T> properties;
	private CKXMLAssetUsagePicker<T,F> usages;
	private JComponent assetPanel; 
	
	private JLabel idLabel;
		
	

	public CKXMLAssetEditorPane(T asset,F fact)
	{
		this.asset=asset;
		afactory = fact;
			
		//String layoutDef = 
//				"(ROW (COLUMN (LEAF name=viewer weight=1.0) usages) (COLUMN  properties )) ";

		String layoutDef = "(COLUMN  (ROW (COLUMN (LEAF name=viewer weight=1.0) usages ) properties ) assetid )";

//		String layoutDef = "(COLUMN  assetid (ROW (COLUMN (LEAF name=viewer weight=1.0) usages) properties ))";
//		String layoutDef = "(ROW (COLUMN (LEAF name=viewer weight=1.0) usages)properties)";


				
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		getMultiSplitLayout().setModel(modelRoot);

		
		idLabel =new JLabel("FileName: "+afactory.getBaseDir()+asset.getAID());
		idLabel.setPreferredSize(new Dimension(100,30));
		add(idLabel,"assetid");
		
		
		
			
		//properties
		properties =asset.getXMLPropertiesEditor();
		properties.addChangeListener(this);
		add(properties,"properties");
			
		//usages
		usages = new CKXMLAssetUsagePicker<T,F>(asset,fact,true);
		add(usages,"usages");
	
		//create viewer for the asset
		assetPanel = asset.getXMLAssetViewer(CKXMLAsset.ViewEnum.EDITABLE);
		if(showAssetPanel())
		{	//CKTreeGUI are already viewable and editable at the same time...
			add(assetPanel,"viewer");
		}
			
	}
	
	public boolean showAssetPanel()
	{
		if (properties.getClass().equals(assetPanel.getClass()))
		{
			return false;
			
		}
		if ((assetPanel instanceof CKTreeGui) && 
			(properties instanceof CKGUINodePropertiesEditor))
		{
			return false;
		}
		else
		{
			return true;
		}
		
		
	}
	
/*	protected void layoutPane()
	{
		String layoutDef = 

				"(COLUMN  (ROW (COLUMN viewer usages weight=1.0) properties ) assetid )";

				
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		getMultiSplitLayout().setModel(modelRoot);

		idLabel =new JLabel("FileName: "+afactory.getBaseDir()+asset.getAID()); 
		add(idLabel,"assetid");
		
		//create viewer for the asset
		JComponent assetPanel = asset.getXMLAssetViewer(CKXMLAsset.ViewEnum.EDITABLE);
		if(! (assetPanel instanceof CKTreeGui))
		{	//CKTreeGUI are already viewable and editable at the same time...
			add(assetPanel,"viewer");
		}
			
		//properties
		properties =asset.getXMLPropertiesEditor();
		properties.addChangeListener(this);
		add(properties,"properties");
			
		//usages
		usages = new CKXMLAssetUsagePicker<T,F>(asset,fact);
		add(usages,"usages");
	}
		*/
		
	
	public void saveAsset()
	{
		properties.storeState();//just to be sure it saved everything
		afactory.writeAssetToXMLDirectory(asset);
		usages.saveUsages(); //must make sure there is an asset ID.
		idLabel.setText("FileName: "+asset.getAID());

		//System.err.println("You've more written the database for grid item yet");
	}
	
	public void saveAsNewAsset()
	{
		asset.setAID("");
		saveAsset();
	}
	
	public void delAsset()
	{
		usages.removeAll(); //must make sure there is an asset ID.
		afactory.assetDelete(asset);
		

		//System.err.println("You've more written the database for grid item yet");
	}
	
	
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
		CKGridItem water = new CKGridItem();
		CKGridItemFactory.getInstance();
		JPanel view=new CKXMLAssetEditorPane<CKGridItem,CKGridItemFactory>(water,CKGridItemFactory.getInstance());
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}



	@Override
	public void stateChanged(ChangeEvent e)
	{
		if(showAssetPanel())
		{
			remove(assetPanel);
		}
		
		assetPanel = asset.getXMLAssetViewer(CKXMLAsset.ViewEnum.EDITABLE);
		if(showAssetPanel())
		{	//CKTreeGUI are already viewable and editable at the same time...
			add(assetPanel,"viewer");
			revalidate();
		}
		
	}
	
	

}
