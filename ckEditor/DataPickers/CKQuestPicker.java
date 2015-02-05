package ckEditor.DataPickers;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKSceneFactory;
import ckDatabase.CKQuestFactory;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.QuestData;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;

@Deprecated
public class CKQuestPicker extends CKPicker<QuestData> //JScrollPane
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8227301326915020704L;





	private CKQuestPicker(Iterator<QuestData> iter)
	{
		super(iter);
		
	}
	/* (non-Javadoc)
	 * @see ckGraphics.src.CKPicker#assetPanelView(java.lang.Object)
	 */
	@Override
	public JComponent assetPanelView(QuestData quest)
	{
		CKSceneFactory factory = CKSceneFactory.getInstance();
		BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(factory.getAsset(quest.getSceneID())  , 200,200);
		JComponent panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(new ImageIcon(img)));
		panel.add(new JLabel(quest.getName()+"   "+quest.getQid()),BorderLayout.SOUTH);
		return panel;
	}

	
	
	
	
	public static void main (String [] argv)
	{
		
		JFrame frame = new JFrame();
		CKQuestFactory factory = CKQuestFactory.getInstance(); 
		
		CKQuestPicker picker = new CKQuestPicker(factory.getAllAssets());
		picker.addSelectedListener(new PopUpQuestViewer());
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
}

class PopUpQuestViewer implements CKEntitySelectedListener<QuestData>
{

	
	public PopUpQuestViewer() {}
	
	@Override
	public void entitySelected(QuestData quest)
	{
		JFrame frame = new JFrame();

		
		//CKQuestViewer view = new CKSceneViewer(quest ,30);
		CKTreeGui tree = new CKTreeGui(quest);
		
		frame.add(tree);
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
