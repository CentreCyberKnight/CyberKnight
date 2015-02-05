package ckEditor.DataPickers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsLayerFactory;
import ckDatabase.CKGraphicsLayerFactoryXML;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckGraphicsEngine.layers.CKLayerViewer;

@Deprecated
public class CKLayerPicker extends CKPicker<CKGraphicsLayer>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5197174440952513946L;


	private CKLayerPicker(Iterator<CKGraphicsLayer> iter)
	{
		super(iter);
	
	}
	
	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(CKGraphicsLayer asset)
	{
			BufferedImage img = CKGraphicsPreviewGenerator.createLayerPreview(asset, 200, 200);
			JComponent panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(new JLabel(new ImageIcon(img)));
			panel.add(new JLabel(asset.getDescription(),JLabel.CENTER)
								,BorderLayout.NORTH);
			return panel;
	}

	
	
	
	
	
	
	
	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		CKGraphicsLayerFactory factory = CKGraphicsLayerFactoryXML.getInstance();
		
		
		CKLayerPicker picker = new CKLayerPicker(factory.getAllGraphicsLayers());
		picker.addSelectedListener(new PopUpLayerViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class PopUpLayerViewer implements CKEntitySelectedListener<CKGraphicsLayer>
{

	
	public PopUpLayerViewer() {}
	
	@Override
	public void entitySelected(CKGraphicsLayer layer)
	{
		JFrame frame = new JFrame();

		CKLayerViewer view = new CKLayerViewer(10,layer ,new Dimension(400,400));

		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
