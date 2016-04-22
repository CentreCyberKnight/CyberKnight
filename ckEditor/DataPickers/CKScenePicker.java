package ckEditor.DataPickers;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKSceneFactory;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.CKSceneViewer;

public class CKScenePicker extends CKPicker<CKGraphicsSceneInterface>
{

	
	private static final long serialVersionUID = 1362042434830939572L;

	public CKScenePicker(Iterator<CKGraphicsSceneInterface> iterator)
	{
		super(iterator);
	}
	
	
	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(CKGraphicsSceneInterface scene)
	{
			BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(scene, 200,200);
			JComponent panel = new JPanel();
			panel.add(new JLabel(new ImageIcon(img)));
			return panel;
		}
			
	
	
	
	
	
	
	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		CKSceneFactory factory = CKSceneFactory.getInstance(); 
		
		CKScenePicker picker = new CKScenePicker(factory.getAllAssets());
		picker.addSelectedListener(new PopUpSceneViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class PopUpSceneViewer implements CKEntitySelectedListener<CKGraphicsSceneInterface>
{

	
	public PopUpSceneViewer() {}
	
	@Override
	public void entitySelected(CKGraphicsSceneInterface scene)
	{
		JFrame frame = new JFrame();

		
		CKSceneViewer view = new CKSceneViewer(scene ,30);

		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
