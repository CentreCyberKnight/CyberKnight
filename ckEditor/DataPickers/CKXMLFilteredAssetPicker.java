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
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKSceneFactory;
import ckDatabase.CKXMLFactory;
import ckGraphicsEngine.CKGraphicsSceneInterface;

public class CKXMLFilteredAssetPicker<T extends CKXMLAsset<T>,F extends CKXMLFactory<T>>
extends MultiSplitPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5772656368872658L;
	
	
	CKXMLAssetUsagePicker<T,F> filter;
	JButton reload;
	CKXMLAssetPicker<T> picker;
	
	F factory;
	
	public CKXMLFilteredAssetPicker(F fact)
	{
		factory = fact;
		String layoutDef = 
			    "(COLUMN top middle (LEAF name=bottom weight=1.0))";
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		getMultiSplitLayout().setModel(modelRoot);

		filter = new CKXMLAssetUsagePicker<T,F>(factory);
		
		JPanel mid=new JPanel();
		mid.setLayout(new FlowLayout());
		reload=new JButton("Filter");
		reload.addActionListener(new Reload());
		mid.add(new JLabel("Click to "));
		mid.add(reload);
		
		
		picker = new CKXMLAssetPicker<T>(factory.getAllAssets());
		
		
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
			Iterator<T>iter= factory.getFilteredAssets(filters);
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
		//	CKXMLFilteredAssetPicker<CKGridItem,CKGridItemFactory> picker =
		//			new CKXMLFilteredAssetPicker<CKGridItem, CKGridItemFactory>(CKGridItemFactory.getInstance());
		
		CKXMLFilteredAssetPicker<CKGraphicsSceneInterface,CKSceneFactory> picker =
				new CKXMLFilteredAssetPicker<CKGraphicsSceneInterface,CKSceneFactory>(CKSceneFactory.getInstance());

		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}




	public void addSelectedListener(CKEntitySelectedListener<T> listener)
	{
		this.picker.addSelectedListener(listener);
	}

}
