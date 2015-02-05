package ckEditor.DataPickers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKXMLFactory;

public class CKXMLAssetUsagePicker
<T extends CKXMLAsset<T>,F extends CKXMLFactory<T>> extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9098189865182961408L;
	ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
	T myAsset;
	F factory;
	JTextField text;
	boolean column;
	
	
	
	public CKXMLAssetUsagePicker(F fact)
	{
		this(null,fact,false);
	}
	
	
	public CKXMLAssetUsagePicker(T asset,F fact)
	{
		this(asset,fact,false);
	}

	public CKXMLAssetUsagePicker(T asset,F fact,boolean column)
	{
	
		myAsset=asset;
		factory = fact;
		this.column = column;
		loadUsages();
	}	
	
	protected void loadUsages()
	{
		Iterator<String> usages = factory.getAllUsages();

		this.removeAll();
//		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());

		String aName= "";
		if(myAsset != null) { aName = myAsset.getAID(); }
		JPanel bPanel = new JPanel();
		if(column)
		{
			bPanel.setLayout(new BoxLayout(bPanel,BoxLayout.Y_AXIS));
		}
		else
		{
			bPanel.setLayout(new FlowLayout());
		}
		
		while(usages.hasNext())
		{
			String name = usages.next();
			JCheckBox box = new JCheckBox(name);
			if(factory.hasUsage(aName,name))
			{
				box.setSelected(true);
			}
			boxes.add(box);
			//if(column) { box.setAlignmentX(LEFT_ALIGNMENT); }
			bPanel.add(box);
		}

		ScrollPane scroll = new ScrollPane();
		
		scroll.add(bPanel);
		scroll.setPreferredSize(new Dimension(100,200));
		add(scroll);
		JPanel nPanel=new JPanel();
		if(column)
		{
			nPanel.setLayout(new BoxLayout(nPanel,BoxLayout.Y_AXIS)); 
		}
		else
		{
			nPanel=new JPanel(new FlowLayout());
		}
		text=new JTextField();
		text.setColumns(15);
		//if(column) { text.setAlignmentX(RIGHT_ALIGNMENT); }
		nPanel.add(text);
		JButton button = new JButton("New Usage");
		button.addActionListener(this);
		//if(column) { button.setAlignmentX(RIGHT_ALIGNMENT); }
		nPanel.add(button);
		//if(column) { nPanel.setAlignmentX(LEFT_ALIGNMENT); }
		add(nPanel,BorderLayout.PAGE_END);
		this.revalidate();
		
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
		for(JCheckBox b:boxes)
		{

			if(b.isSelected())
			{
				factory.assignUsageTypeToAssset(myAsset, b.getText());
			}
			else
			{
				factory.unassignUsageTypeToAssset(myAsset, b.getText());
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(text.getText().length()<=0){
			return;
		}
		factory.makeUsageType(text.getText());
		loadUsages();
	}
	
}
