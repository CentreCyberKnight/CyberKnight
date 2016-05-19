package ckEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckGraphicsEngine.assets.CKCompositeAsset;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKImageAsset;
import ckGraphicsEngine.assets.CKLayeredAsset;
import ckGraphicsEngine.assets.CKRegulatedAsset;
import ckGraphicsEngine.assets.CKSharedAsset;
import ckGraphicsEngine.assets.CKSpriteAsset;

/**
 * @author bradshaw
 *
 */
public class CKAssetEditor extends JPanel implements
		CKEntitySelectedListener<CKGraphicsAsset>
{
	
	private static final long serialVersionUID = 5514897689888215428L;
	
	CKGraphicsAsset asset;
	CKAssetEditorPane pane;
	JPanel bpane;
	JLabel filename;
	
	public CKAssetEditor(CKGraphicsAsset asset)
	{
		super();
		setLayout(new BorderLayout());
		
		
		bpane = new JPanel();
		JButton save = new JButton("Save");
		JButton saveCopy = new JButton("Save as Copy");
		JButton load = new JButton("Load");	
		JButton newButton = new JButton("New Asset");
		
		bpane.add(save);
		save.addActionListener(new SaveAsset());
		
		bpane.add(saveCopy);
		saveCopy.addActionListener(new SaveAsNewAsset());
		
		bpane.add(load);
		load.addActionListener(new LoadAsset());
		
		bpane.add(newButton);
		newButton.addActionListener(new NewAsset());
		
		filename=new JLabel();
		setFilename(asset.getAID());
		bpane.add(filename);
		
		setAsset(asset);
		
		
//
		
	}

	public void setAsset(CKGraphicsAsset asset2)
	{
		querySave();
		removeAll();
		pane = CKAssetEditor.assetEditorFactory(asset2);
		setFilename(asset2.getAID());
		add(pane);
		add(bpane,BorderLayout.NORTH);
		revalidate();
		
	}

	
	private void setFilename(String id)
	{
		filename.setText("GRAPHICS/ASSETS/"+id);
	}
	
	class SaveAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(pane!=null)
			{
				pane.saveAsset();
				setFilename(pane.getAsset().getAID());
				
			}
			
		}		
	}
	
	class SaveAsNewAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(pane!=null)
			{
				pane.saveAsNewAsset();
				setFilename(pane.getAsset().getAID());
			}
			
		}		
	}
	
	class LoadAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKFilteredAssetPicker picker = new CKFilteredAssetPicker();		
			picker.addSelectedListener(new PickerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
			
		}
		
		
		
	}
	
	class PickerListener implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(CKGraphicsAsset a)
		{
			CKAssetEditor.this.entitySelected(a);
	
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	
	
	
	class NewAsset implements ActionListener
	{
		
		
		JRadioButton image = new JRadioButton("Image Asset");
		JRadioButton regulated = new JRadioButton("Regulated Asset");
		JRadioButton layered = new JRadioButton("Layered Asset");
		JRadioButton sprite = new JRadioButton("Sprite Asset");
		JRadioButton shared = new JRadioButton("Shared Asset");
		JButton choose = new JButton("Choose");

		
		public NewAsset()
		{
			ButtonGroup g=new ButtonGroup();
			g.add(image);
			g.add(regulated);
			g.add(layered);
			g.add(sprite);
			g.add(shared);
			
			
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			JPanel p = new JPanel();
			
			p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
			image.setSelected(true);
			p.add(image);
			p.add(regulated);
			p.add(layered);
			p.add(sprite);
			p.add(shared);
			p.add(choose);
			choose.addActionListener(new NewAssetPicked(this,frame));
			
			
			
			
			
			frame.add(p);
			frame.pack();
			frame.setVisible(true);
			
		}
		
	}
	
	
	class NewAssetPicked implements ActionListener
	{
		NewAsset n;
		JFrame f;
		
		public NewAssetPicked(NewAsset n,JFrame f)
		{
			this.n=n;
			this.f=f;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKGraphicsAsset asset=null;
			
			if(n.image.isSelected())
			{
				asset=new CKImageAsset();
			}
			else if(n.regulated.isSelected())
			{
				asset=new CKRegulatedAsset();
			}
			else if(n.layered.isSelected())
			{
				asset=new CKLayeredAsset();
			}
			
			if(n.sprite.isSelected())
			{
				asset=new CKSpriteAsset();
			}
			
			if(n.shared.isSelected())
			{
				asset=new CKSharedAsset();
			}

			
			
			CKAssetEditor.this.entitySelected(asset);
			
			f.setVisible(false); //you can't see me!
			f.dispose(); //Destroy the JFrame object
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * prompts the user if they want to save before leaving present asset.
	 * Will save if user requests it.
	 * 
	 * @return true if the command was canceled, false otherwise.
	 */
	private boolean querySave()
	{
		if(pane ==null) { return false; }
		//put up a do you want to save?
		int n = JOptionPane.showConfirmDialog(
			    null,
			    "Before loading, do you want to save this asset",
			    "Or cancel the load",
			    JOptionPane.YES_NO_CANCEL_OPTION);
		
		
		if(n ==JOptionPane.CANCEL_OPTION)
		{
			return true;
		}
		if(n == JOptionPane.YES_OPTION)
		{
			pane.saveAsset();
		}
		
		return false;
	}


	public static CKAssetEditorPane assetEditorFactory(CKGraphicsAsset a)
	{
		
		if (a instanceof CKImageAsset)
		{
			return new CKImageAssetEditorPane((CKImageAsset) a);
		}
		else if(a instanceof CKRegulatedAsset)
		{
			return new CKRegulatedAssetEditorPane((CKRegulatedAsset) a);
		}
		else if (a instanceof CKCompositeAsset)
		{
			return new CKCompositeAssetEditor((CKCompositeAsset) a);
		}
		else if (a instanceof CKSharedAsset)
		{
			return new CKSharedAssetEditorPane((CKSharedAsset) a);
		}
		else // a instanceof CKNullAsset)
		{
				//return new CKAssetEditorPane(a);
				return new CKAssetEditorPane(a){ /**
					 * 
					 */
					private static final long serialVersionUID = 6151816455693982325L;

				public void storeState(){} };
		}

	}

	@Override
	public void entitySelected(CKGraphicsAsset entity)
	{
		
		setAsset(entity);
	}
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		JFrame frame = new JFrame();
		CKGraphicsAsset water=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("fridgeFloor");		
		
		
		CKAssetEditor view=new CKAssetEditor(water);
		//CKImageAssetEditorPane view=new CKImageAssetEditorPane(new CKImageAsset());
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

}
