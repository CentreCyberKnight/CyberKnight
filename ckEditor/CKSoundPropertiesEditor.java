package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKURL;
import ckDatabase.XMLDirectories;
import ckSound.CKSound;


public class CKSoundPropertiesEditor 
extends CKXMLAssetPropertiesEditor<CKSound> 
implements 	ChangeListener,DocumentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5101467117708619570L;
	JTextField name,url_source,contributer,license,description,filename;
	JButton fileBrowse;
	//JButton playSound;
	//JButton stopSound;
	//JSlider slider;
	CKSound.SoundControls controls;
	
	CKSound asset;

	float volume = .5f;

	
	public CKSoundPropertiesEditor(CKSound asset, boolean editable)
	{
		this.asset=asset;
	
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	
		add(new JLabel("name"));
		name = new JTextField(asset.getName());
		name.setEditable(editable);
		add(name);

		add(new JLabel("description"));
		description = new JTextField(asset.getDescription());
		description.setEditable(editable);
		add(description);

		
		add(new JLabel("contibuter"));
		contributer= new JTextField(asset.getContributer());
		contributer.setEditable(editable);
		add(contributer);


		add(new JLabel("license"));
		license = new JTextField(asset.getLicense());
		license.setEditable(editable);
		add(license);

		add(new JLabel("url_source"));
		url_source = new JTextField(asset.getUrl_source());
		url_source.setEditable(editable);
		add(url_source);

		add(new JLabel("filename"));
		filename = new JTextField(asset.getFilename());
		filename.setEditable(false);
		add(filename);

		fileBrowse = new JButton("Choose File");
		fileBrowse.addActionListener(new ChooseFile());
		add(fileBrowse);
		
		controls = new CKSound.SoundControls(asset); 
		add(controls);
				
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
				String imgbase = new CKURL(XMLDirectories.SOUND_DIR+XMLDirectories.ASSET_STORAGE_DIR).getURL().getFile();
				 fc= new JFileChooser(imgbase);
			} catch (MalformedURLException e1)
			{
				System.err.println(e1);
				fc = new JFileChooser();
			}

	    	int returnVal = fc.showOpenDialog(CKSoundPropertiesEditor.this);

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
	
	
private Vector<ChangeListener> listeners = new Vector<ChangeListener>();

public void addChangeListener(ChangeListener l)
{
	listeners.add(l);
}




@Override
public void storeState()
{	
	stateChanged(null);
}


@Override
public void stateChanged(ChangeEvent e)
{		
	
	asset.setName(name.getText());
	asset.setUrl_source(url_source.getText());
	asset.setContributer(contributer.getText());
	asset.setLicense(license.getText());
	asset.setDescription(description.getText());
	asset.setFilename(filename.getText());
	asset.setPreferredVolume(controls.getVolume());

			
	for(ChangeListener l:listeners)
	{
		l.stateChanged(new ChangeEvent(this));
	}
	
}



/**
 * @param args
 */
public static void main(String[] args)
{
	JFrame frame = new JFrame();
	//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
	CKSound sound = new CKSound();
	CKSoundPropertiesEditor view=new CKSoundPropertiesEditor(sound,true);
	frame.add(view);
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	
}



@Override
public CKSound getAsset()
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

	

}
