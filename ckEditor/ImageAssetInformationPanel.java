package ckEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.TileType;

public class ImageAssetInformationPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4021261155525158097L;
	
	JComboBox imageTypes;
	JLabel assettype_label;
	JLabel imagetype_label;
	JLabel width_label;
	JLabel height_label;
	JLabel rows_label;
	JLabel frames_label;
	JLabel fname_label;
	JLabel description_label;
	JSpinner width_spinner;
	JSpinner height_spinner;
	JSpinner rows_spinner;
	JSpinner frames_spinner;
	JTextField fname_field;
	JTextField description_field;
	JButton autoname_button;
	JButton create_button;
	JButton cancel_button;
	String[] asset_type_names={"IMAGE","LAYERED","NULL","REGULATED","SHARED","SPRITE","TILE"};
	
	CKImageAsset asset;
	private int userChoice=2;
	public ImageAssetInformationPanel()
	{
		asset=null;
		setLayout(null);
		setPreferredSize(new Dimension(455,245));
		initComponents();
		addComponents();
		setVisible(true);
	}
	
////////////////////////////////
	//--BEgin inits --//
	////////////////////////////////
	
	private void initComponents()
	{
		initLabels();
		initSpinners();
		initButtons();
		initTextFields();
		initComboBoxes();	
	}
	
	private void initLabels()
	{
		assettype_label=new JLabel("Asset Type:");
		imagetype_label=new JLabel("Image Type:");
		width_label=new JLabel("Width:");
		height_label=new JLabel("Height:");
		rows_label=new JLabel("Rows:");
		frames_label=new JLabel("Frames:");
		fname_label=new JLabel("File Name:");
		description_label=new JLabel("Description:");
		
		assettype_label.setBounds(6, 26, 100, 16);
		imagetype_label.setBounds(194, 26, 100, 16);
		width_label.setBounds(6, 74, 60, 28);
		height_label.setBounds(168, 74, 60, 28);
		rows_label.setBounds(6, 114, 60, 27);
		frames_label.setBounds(168, 111, 60, 33);
		fname_label.setBounds(6, 193, 72, 16);
		description_label.setBounds(6, 163, 72, 16);
	}
	
	private void initSpinners()
	{
		width_spinner=new JSpinner();
		height_spinner=new JSpinner();
		rows_spinner=new JSpinner();
		frames_spinner=new JSpinner();
			
		width_spinner.setBounds(79, 74, 50, 30);
		height_spinner.setBounds(242, 74, 50, 30);
		rows_spinner.setBounds(79, 114, 50, 30);
		frames_spinner.setBounds(242, 114, 50, 30);
		
		width_spinner.setValue(64);
		height_spinner.setValue(32);
		rows_spinner.setValue(1);
		frames_spinner.setValue(1);
	}
	
	private void initButtons()
	{
		autoname_button=new JButton("Browse");
		create_button=new JButton("Create");
		cancel_button=new JButton("Cancel");
		
		autoname_button.setBounds(350,190,100,20);
		create_button.setBounds(125,220,100,20);	
		cancel_button.setBounds(235,220,100,20);
		
		autoname_button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					long timestamp=System.currentTimeMillis();
					fname_field.setText(timestamp+".png");			
				}			
			});
		
		create_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				String d=getAssetDescription();
				int w=getAssetWidth();
				int h=getAssetHeight();
				int r=getAssetRows();
				int f=getAssetFrames();
				TileType t=getImageType();
				//String name=getFileName();
				//CKImageAsset(String desc, int width,int height,int frames,int rows,TileType ty)
				
				
					asset = new CKImageAsset(d,w,h,f,r,t);//image
					//userChoice=1;
					//TODO AssetFactory.addAssetToEditor(asset, );
					close();
			}			
		});
		
		cancel_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{	
				userChoice=0;
				close();				
			}			
		});
	}
	
	private void initComboBoxes()
	{
		
		imageTypes=new JComboBox<TileType>(TileType.values());
		imageTypes.setSelectedItem(TileType.BASE);
		//imageTypes.setEnabled(false);
		imageTypes.setBounds(270, 22, 79, 27);
	}
	
	private void initTextFields()
	{
		fname_field=new JTextField();
		fname_field.setColumns(10);
		fname_field.setBounds(79, 187, 262, 28);
		
		description_field=new JTextField();
		description_field.setColumns(10);
		description_field.setBounds(79, 157, 262, 28);
	}
	
	private void addComponents()
	{
		add(imageTypes);
		add(assettype_label);
		add(imagetype_label);
		add(width_label);
		add(height_label);
		add(rows_label);
		add(frames_label);
		add(fname_label);
		add(description_label);
		add(width_spinner);
		add(height_spinner);
		add(rows_spinner);
		add(frames_spinner);
		add(fname_field);
		add(description_field);
		add(autoname_button);
		add(create_button);
		add(cancel_button);
	}
	
	private void close()
	{
		SwingUtilities.getWindowAncestor(this).dispose();
	}
	
	////////////////////////////////
	//--END inits --//
	////////////////////////////////
	
	/////////////////////////////
	//-- GETTERS AND SETTERS --//
	/////////////////////////////
	
	public int getAssetWidth()
	{
		return (Integer) width_spinner.getValue();
	}
	
	public int getAssetHeight()
	{
		return (Integer) height_spinner.getValue();
	}
	
	public int getAssetRows()
	{
		return (Integer) rows_spinner.getValue();
	}
	
	public int getAssetFrames()
	{
		return (Integer) frames_spinner.getValue();
	}
	
	public String getFileName()
	{
		return fname_field.getText();
	}
	
	public TileType getImageType()
	{
		return TileType.values()[imageTypes.getSelectedIndex()];
	}
	
	public String getAssetDescription()
	{
		return description_field.getText();
	}
	
	public CKImageAsset getImageAsset()
	{
		return asset;
	}
	
	public int getUserChoice()
	{
		return userChoice;
	}
	////////////////////////////////
	//--END GETTERS AND SETTERS --//
	////////////////////////////////
	/*public static void main(String[] args)
	{
		JDialog d=new JDialog();
		d.setResizable(false);
		d.setContentPane(new ImageAssetInformationPanel());
		d.pack();
		d.setVisible(true);
	}
	*/
}
