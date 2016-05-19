package ckEditor.DataPickers;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKAssetPicker extends CKPicker<CKGraphicsAsset>
{

	
	private static final long serialVersionUID = 5910885867951321575L;


	public CKAssetPicker(Iterator<CKGraphicsAsset> iter)
	{
		super(iter);
	}

	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(CKGraphicsAsset asset)
	{
		// TODO Auto-generated method stub
		return new CKAssetViewer(20,asset ,null,false );
	}


	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
		
		CKAssetPicker picker = new CKAssetPicker(factory.getAllGraphicsAssets());
		picker.addSelectedListener(new PopUpAssetViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class PopUpAssetViewer implements CKEntitySelectedListener<CKGraphicsAsset>
{

	
	public PopUpAssetViewer() {}
	
	@Override
	public void entitySelected(CKGraphicsAsset asset)
	{
		JFrame frame = new JFrame();

		CKAssetViewer view = new CKAssetViewer(10,asset ,new Dimension(400,400),true );

		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
