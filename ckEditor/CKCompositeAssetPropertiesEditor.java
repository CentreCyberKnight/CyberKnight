package ckEditor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckDatabase.CKGraphicsAssetFactoryXML;
import ckGraphicsEngine.assets.CKCompositeAsset;

public class CKCompositeAssetPropertiesEditor extends JPanel implements ChangeListener,ActionListener,DocumentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;
	CKCompositeAsset asset;
	JTextField description;
	JLabel imageHeight;
	JLabel imageWidth;
	
	CKCompositeAssetPropertiesEditor(CKCompositeAsset asset)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(new JLabel("Description"));
		description = new JTextField(asset.getDescription());
		description.getDocument().addDocumentListener(this);
		add(description);
		
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

		asset.setDescription(description.getText());
		
		imageWidth.setText("Width:"+asset.getWidth(0)+"px");
		imageHeight.setText("Height:"+asset.getHeight(0)+"px");
				
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

	
	
	
	
	
	public void actionPerformed(ActionEvent e)
	{

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		CKCompositeAsset water=(CKCompositeAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("babySprite");		
		CKCompositeAssetPropertiesEditor view=new CKCompositeAssetPropertiesEditor(water);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}
	
}
