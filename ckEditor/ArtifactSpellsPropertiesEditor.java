package ckEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKTabIconPane;
import ckCommonUtils.RequestNewComponentListener;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKAssetPicker;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKSpell;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class ArtifactSpellsPropertiesEditor extends JPanel implements RequestNewComponentListener,ChangeListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5233601691987910348L;
	CKTabIconPane tabs;
	CKArtifact artifact;
	
	
	public ArtifactSpellsPropertiesEditor(CKArtifact art, boolean editable)
	{
		artifact = art;
		setPreferredSize(new Dimension(500,300));
		tabs = new CKTabIconPane(this,editable,editable);
		
		//now add panels for each spell.
		Iterator<CKSpell> iter = art.getSpells();
		while(iter.hasNext())
		{
			CKSpell s = iter.next();
			SpellPanel sp = new SpellPanel(s);
			tabs.addTab(sp,sp.getIcon(),s.getName());
			
		}
		
		setLayout(new BorderLayout());
		add(tabs,BorderLayout.CENTER);
		
		
		
	}

	@Override
	public Component requestNewComponent()
	{
		CKSpell s = new CKSpell();
		artifact.addSpell(s);
		return new SpellPanel(s);
	}

	@Override
	public Icon requestNewIconforComponent(Component comp)
	{
		SpellPanel sp = (SpellPanel) comp;
		return sp.getIcon();
	}

	@Override
	public String requestNewDescriptionForComponent(Component comp)
	{
		SpellPanel sp = (SpellPanel) comp;
		return sp.getSpell().getName();
		
	}

	@Override
	public void notifyComponentRemoved(Component comp)
	{
		SpellPanel sp = (SpellPanel) comp;
		artifact.removeSpell(sp.getSpell());
		

	}
	
	/**
	 * Icon or title changed
	 * @param p
	 */
	public void iconChanged(SpellPanel p)
	{
		tabs.updateTab(p, p.getIcon(), p.getSpell().getName());
	}

	public void storeState()
	{
		//no need to do anything, yet.  All actions are immediatly stored
	}
	
	
	Vector<ChangeListener> listeners=new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	
	public void stateChanged(ChangeEvent e)
	{
		for(ChangeListener l:listeners)
		{
			l.stateChanged(e);
		}
	}
	
		
		
	public class SpellPanel extends JPanel implements DocumentListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3153410040138512139L;


		CKSpell spell;
		

		private JTextField title = new JTextField();		
		private JTextArea func = new JTextArea(5,15);
		private JTextField desc = new JTextField();
		private JScrollPane funcScrolls;

		private JButton newIcon = new JButton("Pick New Spell Icon");
		
		
		
		public SpellPanel(CKSpell spell)
		{
			setSpell(spell);
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			
			initComponents();
			
			add(title);
			add(desc);	
			//add(func);
			add(funcScrolls);

			newIcon.addActionListener(new IconListener(this));
			add(newIcon);
			
		}
		
		public void initComponents()
		{
			//add labels
			TitledBorder titleB;
			titleB = BorderFactory.createTitledBorder("Name");
			title.setBorder(titleB);
			
			
			
			
			
			titleB = BorderFactory.createTitledBorder("Description");
			desc.setBorder(titleB);
			
			
			//scrollpane
			
			funcScrolls = new JScrollPane(func);
			titleB = BorderFactory.createTitledBorder("Code");
			funcScrolls.setBorder(titleB);
			
			//add listeners
			title.getDocument().addDocumentListener(this);
			func.getDocument().addDocumentListener(this);
			desc.getDocument().addDocumentListener(this);
			
		}
		
		public CKSpell getSpell()
		{
			return spell;
		}
		
		public void setSpell(CKSpell spell)
		{
			this.spell = spell;
			title.setText(spell.getName());
			func.setText(spell.getFunctionCall());
			desc.setText(spell.getDescription());
			
		}
		
		public Icon getIcon()
		{
			 return new ImageIcon(CKGraphicsPreviewGenerator.createAssetPreview(
					 	CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(spell.getIconID()),
					 	0,0,50,50) );
		}
	
		
		

		public void storeState()
		{
			
			if(title.getText().compareTo(spell.getName())!=0)
			{
				spell.setName(title.getText());
				iconChanged(this);
			}
			spell.setFunctionCall(func.getText());
			spell.setDescription(desc.getText());
			stateChanged(new ChangeEvent(this));
			
		}
		
		@Override
		public void insertUpdate(DocumentEvent e)
		{
			storeState();
			
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			storeState();
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			storeState();
		}



		
		
		
		
	}
	
	
	

		class IconListener implements ActionListener
		{
			CKGraphicsAssetFactoryXML factory;
			SpellPanel sPanel;
			
			IconListener(SpellPanel panel)
			{
				this.sPanel = panel;
			}
			
			public void actionPerformed(ActionEvent e)
			{
				JFrame popUpView = new JFrame();
				factory = (CKGraphicsAssetFactoryXML) CKGraphicsAssetFactoryXML.getInstance();
				CKAssetPicker p = new CKAssetPicker(factory.getFilteredGraphicsAssets("icon"));
				p.addSelectedListener(new PopUpAssetViewer(sPanel,popUpView));
				popUpView.add(p);
				popUpView.pack();
				popUpView.setVisible(true);
			}
		}

		class PopUpAssetViewer implements CKEntitySelectedListener<CKGraphicsAsset>
		{
			SpellPanel sPanel;
			JFrame frame;
			
			PopUpAssetViewer(SpellPanel sPanel,JFrame f)
			{
				this.sPanel = sPanel;
				frame = f;
			}
			
			@Override
			public void entitySelected(CKGraphicsAsset asset)
			{
				sPanel.getSpell().setIconID(asset.getAID());
				iconChanged(sPanel);
				stateChanged(new ChangeEvent(this));
				frame.dispose();
				//set currentArtifact to the artifact associated with whatever icon was just selected
			}
		}

	

}
