package ckEditor.DataPickers;

import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGridItemFactory;
import ckEditor.CKGridItemViewer;
import ckGameEngine.CKGridItem;

public class CKGridItemPicker extends CKPicker<CKGridItem>
{

	
	private static final long serialVersionUID = 5910885867951321575L;


	public CKGridItemPicker(Iterator<CKGridItem> iter)
	{
		super(iter);
	}

	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(CKGridItem asset)
	{
		// TODO Auto-generated method stub
		//FIXME--need to view a grid item....
		return new CKGridItemViewer(20,asset ,null,false );
	}


	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		CKGridItemFactory factory = CKGridItemFactory.getInstance(); 
		
		CKGridItemPicker picker = new CKGridItemPicker(factory.getAllAssets());
		picker.addSelectedListener(new PopUpGridItemViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class PopUpGridItemViewer implements CKEntitySelectedListener<CKGridItem>
{

	
	public PopUpGridItemViewer() {}
	
	@Override
	public void entitySelected(CKGridItem asset)
	{
		JFrame frame = new JFrame();

	//	CKAssetViewer view = new CKAssetViewer(10,asset ,new Dimension(400,400),true );

		//frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
