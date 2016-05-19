package ckEditor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

import ckCommonUtils.CKURL;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckDatabase.XMLDirectories;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.TileType;

public class CKImageAssetPropertiesEditor extends JPanel implements ChangeListener,ActionListener,DocumentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8072899742639301497L;
	CKImageAsset asset;
	JTextField name;
	JTextField filename;
	JTextField contributer;
	JTextField license;
	JTextField url_source;
	
	
	
	
	JButton fileBrowse;
	SpinnerNumberModel rowSpin;
	SpinnerNumberModel frameSpin;
	JLabel imageWidth;
	JLabel imageHeight;
	JComboBox<TileType> tileType;
		
	//FIXME-type?
	
	CKImageAssetPropertiesEditor(CKImageAsset asset)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(new JLabel("Description"));
		name = new JTextField(asset.getDescription());
		name.getDocument().addDocumentListener(this);
		add(name);
		
		
		add(new JLabel("Contributer"));
		contributer= new JTextField(asset.getContributer());
		contributer.getDocument().addDocumentListener(this);
		add(contributer);
		
		add(new JLabel("Liscence"));
		license = new JTextField(asset.getLicense());
		license.getDocument().addDocumentListener(this);
		add(license);
		
		add(new JLabel("URL Source"));
		url_source = new JTextField(asset.getUrl_source());
		url_source.getDocument().addDocumentListener(this);
		add(url_source);
		
				
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		filename = new JTextField(asset.getFilename());
		p.add(filename);
		fileBrowse = new JButton("Browse");
		fileBrowse.addActionListener(new ChooseFile());
		p.add(fileBrowse);
		add(p);
		
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new FlowLayout());
		spinnerPanel.add(new JLabel("Rows"));
		rowSpin = new SpinnerNumberModel(asset.getRows(), 1,1000 ,1);
		JSpinner spinR = new JSpinner(rowSpin);
		spinR.addChangeListener(this);
		spinnerPanel.add(spinR);
		
		spinnerPanel.add(new JLabel("Frame"));
		frameSpin = new SpinnerNumberModel(asset.getFrames(), 1,1000,1);
		JSpinner spinF = new JSpinner(frameSpin);
		spinF.addChangeListener(this);
		spinnerPanel.add(spinF);

		add(spinnerPanel);
		
		JPanel dim = new JPanel();
		dim.setLayout(new FlowLayout() );
		imageHeight=new JLabel("Height:"+asset.getHeight()+"px");
		dim.add(imageHeight);
		imageWidth=new JLabel("Width:"+asset.getWidth()+"px");
		dim.add(imageWidth);
		add(dim);
		
		tileType = new JComboBox<TileType>(TileType.values());
		tileType.addActionListener(this);
		tileType.setSelectedItem(asset.getDrawType());
		add(tileType);
		
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
		asset.setContributer(contributer.getText());
		asset.setLicense(license.getText());
		asset.setUrl_source(url_source.getText());
		
		
		
		
		asset.setRows(rowSpin.getNumber().intValue());
		asset.setFrames(frameSpin.getNumber().intValue());
		asset.setDrawType((TileType) tileType.getSelectedItem());
		
		
		
		imageWidth.setText("Width:"+asset.getWidth()+"px");
		imageHeight.setText("Height:"+asset.getHeight()+"px");
				
		for(ChangeListener l:listeners)
		{
			l.stateChanged(new ChangeEvent(this));
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

	
	
	class ChooseFile implements ActionListener
	{
	
	public void actionPerformed(ActionEvent e)
	{
	    //Handle open button action.
	    if (e.getSource() == fileBrowse) 
	    {
	    	JFileChooser fc;

	    	try
			{
				String imgbase = new CKURL(XMLDirectories.GRAPHIC_ASSET_IMAGE_DIR).getURL().getFile();
				 fc= new JFileChooser(imgbase);
			} catch (MalformedURLException e1)
			{
				System.err.println(e1);
				fc = new JFileChooser();
			}

	    	int returnVal = fc.showOpenDialog(CKImageAssetPropertiesEditor.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) 
	        {
	            File file = fc.getSelectedFile();
	            String base;
				try
				{
					base = new CKURL("").getURL().getFile();
				} catch (MalformedURLException e1)
				{
					e1.printStackTrace();
					return;
				}
	            //now calculate the relative path...
	            String relative = new File(base).toURI().relativize(file.toURI()).getPath();
	            asset.setFilename(relative);
	            filename.setText(relative);
	            stateChanged(null);
	            
	        } 
	    }
	}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		CKImageAsset water=(CKImageAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("Hero");		
		CKImageAssetPropertiesEditor view=new CKImageAssetPropertiesEditor(water);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		stateChanged(null);
		
	}

	
	//FIXME - need a detect when the asset changes and alter the spinners accordingly. 



	
}
