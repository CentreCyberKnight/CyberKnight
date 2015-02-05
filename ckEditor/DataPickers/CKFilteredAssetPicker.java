package ckEditor.DataPickers;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKFilteredAssetPicker extends MultiSplitPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CKAssetUsagePicker filter;
	JButton reload;
	CKAssetPicker picker;
	
	
	
	public CKFilteredAssetPicker()
	{
		String layoutDef = 
			    "(COLUMN top middle (LEAF name=bottom weight=1.0))";
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		getMultiSplitLayout().setModel(modelRoot);

		filter = new CKAssetUsagePicker();
		
		JPanel mid=new JPanel();
		mid.setLayout(new FlowLayout());
		reload=new JButton("Filter");
		reload.addActionListener(new Reload());
		mid.add(new JLabel("Click to "));
		mid.add(reload);
		
		picker = new CKAssetPicker(CKGraphicsAssetFactoryXML.getInstance().getAllGraphicsAssets());
		
		
		add(filter ,"top");
		add(mid,"middle");
		add(picker,"bottom");
			
	}

	class Reload implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			//1 get filters
			String[] filters = filter.getSelected();
			//2 get iterator
			Iterator<CKGraphicsAsset>iter= 
					CKGraphicsAssetFactoryXML.getInstance().getFilteredGraphicsAssets(filters);
			//3 restart theasset picket 
			picker.rePopulateEntities(iter);
		}
		
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		JFrame frame = new JFrame();
		CKFilteredAssetPicker picker = new CKFilteredAssetPicker();
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}




	public void addSelectedListener(CKEntitySelectedListener<CKGraphicsAsset> listener)
	{
		this.picker.addSelectedListener(listener);
	}

}
