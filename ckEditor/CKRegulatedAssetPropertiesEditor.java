package ckEditor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKRegulatedAsset;

public class CKRegulatedAssetPropertiesEditor extends JPanel implements ChangeListener,ActionListener,DocumentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;
	CKRegulatedAsset asset;
	JTextField name;
	JTextField fromName;
	JButton pickerBrowse;
	SpinnerNumberModel rateSpin;
	JLabel imageHeight;
	JLabel imageWidth;
	
	CKRegulatedAssetPropertiesEditor(CKRegulatedAsset asset)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(new JLabel("Description"));
		name = new JTextField(asset.getDescription());
		name.getDocument().addDocumentListener(this);
		add(name);
		fromName = new JTextField(asset.getAssetID());
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(fromName);
		pickerBrowse = new JButton("Pick Asset");
		pickerBrowse.addActionListener(this);
		p.add(pickerBrowse);
		add(p);
		
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new FlowLayout());
		spinnerPanel.add(new JLabel("Rate"));
		
		rateSpin = new SpinnerNumberModel(asset.getMaxRate(), 1,120 ,1);
		JSpinner spinR = new JSpinner(rateSpin);
		spinR.addChangeListener(this);
		spinnerPanel.add(spinR);

		add(spinnerPanel);
		
		JPanel dim = new JPanel();
		dim.setLayout(new FlowLayout() );
		imageHeight=new JLabel("Height:"+asset.getHeight(0)+"px");
		dim.add(imageHeight);
		imageWidth=new JLabel("Width:"+asset.getWidth(0)+"px");
		dim.add(imageWidth);
		add(dim);		
	}

	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{		
		asset.setDescription(name.getText());
		asset.setAssetID(fromName.getText());
		asset.setMaxRate(rateSpin.getNumber().intValue());
		
		imageWidth.setText("Width:"+asset.getWidth(0)+"px");
		imageHeight.setText("Height:"+asset.getHeight(0)+"px");
				
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
		CKRegulatedAsset water=(CKRegulatedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("SlowSwirl");		
		CKRegulatedAssetPropertiesEditor view=new CKRegulatedAssetPropertiesEditor(water);
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
			fromName.setText(a.getAID());
			stateChanged(null);
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

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
	
}
