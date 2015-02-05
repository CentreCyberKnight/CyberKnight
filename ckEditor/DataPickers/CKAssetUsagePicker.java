package ckEditor.DataPickers;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKAssetUsagePicker extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9098189865182961408L;
	ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
	CKGraphicsAsset myAsset;
	
	
	public CKAssetUsagePicker()
	{
		this(null);
	}
	
	
	public CKAssetUsagePicker(CKGraphicsAsset asset)
	{
		myAsset=asset;
		setLayout(new FlowLayout());
		CKGraphicsAssetFactoryXML fact =(CKGraphicsAssetFactoryXML)
				CKGraphicsAssetFactoryXML.getInstance(); 
		String aName= "";
		if(asset != null) { aName = asset.getAID(); }
		
		Iterator<String> usages = fact.getAllGraphicsUsages();
		while(usages.hasNext())
		{
			String name = usages.next();
			JCheckBox box = new JCheckBox(name);
			if(fact.hasUsage(aName,name))
			{
				box.setSelected(true);
			}
			boxes.add(box);
			add(box);
		}
		
	}	
	
	public String[] getSelected()
	{
		ArrayList<String> L = new ArrayList<String>();
		for(JCheckBox b:boxes)
		{
			if(b.isSelected())
			{
				L.add(b.getText());
			}			
		}
		
		String[] s = new String[L.size()];
		L.toArray(s);
		return s;
	}
	
	public void saveUsages()
	{
		CKGraphicsAssetFactoryXML fact =(CKGraphicsAssetFactoryXML)
				CKGraphicsAssetFactoryXML.getInstance(); 
		
		for(JCheckBox b:boxes)
		{
			if(b.isSelected())
			{
				fact.assignUsageTypeToAssset(myAsset, b.getText());
			}
			else
			{
				fact.unassignUsageTypeToAsset(myAsset.getAID(), b.getText());
			}
		}		
	}
	
}
