package ckEditor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKTriggerListFactory;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGridItem;
import ckGameEngine.Direction;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTriggerList;

public class CKGridItemPropertiesEditor extends CKXMLAssetPropertiesEditor<CKGridItem> 
implements ChangeListener,ActionListener,DocumentListener,ItemListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;

	CKGridItem asset;
	
	JTextField name;
	JTextField description;
	
	JTextField assetName;
	JButton pickerBrowse;
	
	SpinnerNumberModel heightSpin;
	SpinnerNumberModel weightSpin;
	SpinnerNumberModel strengthSpin;
	SpinnerNumberModel moveCostSpin;
	SpinnerNumberModel slideCostSpin;
	JComboBox<Direction> lowSide;
	
	
	//dropdown for the SharedTriggers
	JComboBox<CKTriggerList> sharedTrigger;
	CKTreeGui   myTriggers;
	
	JLabel imageHeight;
	JLabel imageWidth;
	
	
	public CKGridItemPropertiesEditor(CKGridItem asset)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		add(new JLabel("Name"));
		name = new JTextField(asset.getName());
		name.getDocument().addDocumentListener(this);
		add(name);
		
		add(new JLabel("Description"));
		description = new JTextField(asset.getDescription());
		description.getDocument().addDocumentListener(this);
		add(description);
		
		
		
		assetName = new JTextField(asset.getAssetID());
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(assetName);
		pickerBrowse = new JButton("Pick Asset");
		pickerBrowse.addActionListener(this);
		p.add(pickerBrowse);
		add(p);

		JPanel spinnerPanel1 = new JPanel();
		spinnerPanel1.setLayout(new FlowLayout());
		spinnerPanel1.add(new JLabel("Height"));
		heightSpin = new SpinnerNumberModel(asset.getItemHeight(), -100,100,1);
		JSpinner spinH = new JSpinner(heightSpin);
		spinH.addChangeListener(this);
		spinnerPanel1.add(spinH);

		spinnerPanel1.add(new JLabel("Weight"));
		weightSpin = new SpinnerNumberModel(asset.getItemWeight(),-100,100,1);
		JSpinner spinW = new JSpinner(weightSpin);
		spinW.addChangeListener(this);
		spinnerPanel1.add(spinW);

		spinnerPanel1.add(new JLabel("Strength"));
		strengthSpin = new SpinnerNumberModel(asset.getItemStrength(), 0,100,1);
		JSpinner spinS = new JSpinner(strengthSpin);
		spinS.addChangeListener(this);
		spinnerPanel1.add(spinS);

		add(spinnerPanel1);
		
		

		JPanel spinnerPanel2 = new JPanel();
		spinnerPanel2.setLayout(new FlowLayout());
		spinnerPanel2.add(new JLabel("Move"));
		moveCostSpin = new SpinnerNumberModel(Math.min(asset.getMoveCost(),100), -100,100,1);
		JSpinner spinM = new JSpinner(moveCostSpin);
		spinM.addChangeListener(this);
		spinnerPanel2.add(spinM);

		spinnerPanel2.add(new JLabel("Slide"));
		slideCostSpin = new SpinnerNumberModel(Math.min(asset.getSlideCost(),100),-100,100,1);
		JSpinner spinSlide = new JSpinner(slideCostSpin);
		spinSlide.addChangeListener(this);
		spinnerPanel2.add(spinSlide);

		
		spinnerPanel2.add(new JLabel("Low"));		
		lowSide = new JComboBox<Direction>(Direction.values());
		lowSide.setSelectedItem(asset.getLowSide());
		lowSide.addItemListener(this);
		spinnerPanel2.add(lowSide);
		
		
		add(spinnerPanel2);
		
		add(new JLabel("Shared Triggers"));
		sharedTrigger = new JComboBox<CKTriggerList>(CKTriggerListFactory.getInstance().getAllAssetsVectored()); 
		add(sharedTrigger);
		myTriggers = new CKTreeGui(asset.getPersonalTriggers());
		add(myTriggers);
		
		
		
		
		
		
		JPanel dim = new JPanel();
		dim.setLayout(new FlowLayout() );
		imageHeight=new JLabel("Height:"+asset.getAsset().getHeight(0)+"px");
		dim.add(imageHeight);
		imageWidth=new JLabel("Width:"+asset.getAsset().getWidth(0)+"px");
		dim.add(imageWidth);
		add(dim);		
	}

	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	


	@Override
	public void storeState()
	{
		myTriggers.stopEditing();
		
		privateStoreState();
		
	}
	
	private void privateStoreState()
	{
		asset.setName(name.getText());
		asset.setDescription(description.getText());
		asset.setAssetID(assetName.getText());
		asset.setItemHeight(heightSpin.getNumber().intValue());
		asset.setItemWeight(weightSpin.getNumber().intValue());
		asset.setItemStrength(strengthSpin.getNumber().intValue());
		asset.setMoveCost(moveCostSpin.getNumber().intValue());
		asset.setSlideCost(slideCostSpin.getNumber().intValue());
		asset.setLowSide((Direction)lowSide.getSelectedItem());
		asset.setPersonalTriggers((CKTriggerList) myTriggers.getRoot());
		
		
		CKTriggerList tl = (CKTriggerList) sharedTrigger.getSelectedItem();
		asset.setSharedTriggers(new CKSharedTriggerList(tl.getAID()));
		
				
		imageWidth.setText("Width:"+asset.getAsset().getWidth(0)+"px");
		imageHeight.setText("Height:"+asset.getAsset().getHeight(0)+"px");
		
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e)
	{		
		privateStoreState();
				
		for(ChangeListener l:listeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
		
	}

	
	public void actionPerformed(ActionEvent e)
	{
		
		JFrame frame = new JFrame();
		CKFilteredAssetPicker picker = new CKFilteredAssetPicker();		
		picker.addSelectedListener(new PickerListener(frame));
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
		CKGridItem water = new CKGridItem();
		CKGridItemPropertiesEditor view=new CKGridItemPropertiesEditor(water);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}

	class PickerListener implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGraphicsAsset a)
		{
			assetName.setText(a.getAID());
			stateChanged(null);
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}

	@Override
	public CKGridItem getAsset()
	{
		return asset;
		
	}




	@Override
	public void insertUpdate(DocumentEvent e)
	{
		stateChanged(null);	
	}




	@Override
	public void removeUpdate(DocumentEvent e)
	{
		stateChanged(null);	
	}




	@Override
	public void changedUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}




	@Override
	public void itemStateChanged(ItemEvent e)
	{
		stateChanged(null);
		
	}


	
}
