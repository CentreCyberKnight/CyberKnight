package ckEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKAssetPicker;
import ckEditor.treegui.BookList;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class ArtifactPropertiesEditor extends CKXMLAssetPropertiesEditor<CKArtifact> 
	implements ActionListener,ChangeListener,DocumentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private CKAssetButton artIcon  = new CKAssetButton();
	private JTextField name = new JTextField("name goes here",10);
	private JTextArea backstory = new JTextArea(3,20);
	ArtifactSpellsPropertiesEditor spellEditor;
	
	
	private CKTreeGui abilityTree=new CKTreeGui(new CKBook());
	private CKTreeGui limitTree=new CKTreeGui(new CKBook());
	private CKTreeGui reqTree=new CKTreeGui(new BookList());;
	
	
	private CKArtifact artifact;
	public ArtifactPropertiesEditor(CKArtifact art, boolean isEditable)
		{
		
			//no other good place to init this
			spellEditor = new ArtifactSpellsPropertiesEditor(art,isEditable);	
		
			initComponents(isEditable);
			setArtifact(art);
			initComponentListeners(isEditable);
			layoutStuff();
			
		}
	
	protected void initComponents(boolean editable)
	{
		artIcon.setPreferredSize(new Dimension(50,50));
		
		if(editable)
		{
			artIcon.addActionListener(this);
			
		}

		name.setEditable(editable);
		//UIManager.getDefaults().getFont("font");
		name.setFont(new Font("Arial", Font.PLAIN, 18));
		
		backstory.setEditable(editable);
		backstory.setLineWrap(true);
		backstory.setWrapStyleWord(true);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Back Story");
		backstory.setBorder(title);

		
		
		if(! editable)
		{ //set to bookview already guinodes
			abilityTree=new CKBookView(new CKBook());
			limitTree=new CKBookView(new CKBook());			
			reqTree=new CKTreeGui(new BookList(),editable);
		}
		title = BorderFactory.createTitledBorder("Added Abilities");
		abilityTree.setBorder(title);
		
		title = BorderFactory.createTitledBorder("Ability Limits");
		limitTree.setBorder(title);
		
		title = BorderFactory.createTitledBorder("Meet one set of requirements");
		reqTree.setBorder(title);
		
	
	}
	
	
	protected void initComponentListeners(boolean editable)
	{
		

		name.getDocument().addDocumentListener(this);
		backstory.getDocument().addDocumentListener(this);
		
		abilityTree.addChangeListener(this);
		limitTree.addChangeListener(this);
		reqTree.addChangeListener(this);
		
		
		
	}
	
	protected void layoutStuff()
	{
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		JPanel topRight = new JPanel();
		topRight.setLayout(new BoxLayout(topRight,BoxLayout.Y_AXIS));
		topRight.setAlignmentX(LEFT_ALIGNMENT);
		JPanel topRightTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		
		topRightTop.add(artIcon);
		topRightTop.add(name);
		topRight.add(topRightTop);
		topRight.add(backstory);
		top.add(topRight);
		//TODO
		//JPanel placeholder = new JPanel();
		//placeholder.setBackground(Color.red);
		//placeholder.setPreferredSize(new Dimension(500,300));
		
		top.add(spellEditor);
		
		//now the bottom
		JPanel bottom = new JPanel(new GridLayout(1,3));
		bottom.add(abilityTree);
		bottom.add(limitTree);
		bottom.add(reqTree);
		
		setLayout(new BorderLayout());
		add(top,BorderLayout.PAGE_START);
		add(bottom,BorderLayout.CENTER);
		
		
		
		
		
	}
	
	
	
	
	
	public void setArtifact(CKArtifact art)
	{
		artifact = art;
		
		artIcon.setIcon(artifact.getIconId());
		name.setText(artifact.getName());
		backstory.setText(artifact.getBackstory());
		abilityTree.setRoot(artifact.getAbilities());
		limitTree.setRoot(artifact.getLimits());
		reqTree.setRoot(artifact.getRequirements());

	}
	

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFrame popUpView = new JFrame();
		CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance();
		CKAssetPicker p = new CKAssetPicker(
				factory.getFilteredGraphicsAssets("icon"));
		p.addSelectedListener(new PopUpAssetViewer(popUpView));
		popUpView.add(p);
		popUpView.pack();
		popUpView.setVisible(true);

	}

	class PopUpAssetViewer implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		JFrame frame;
		PopUpAssetViewer(JFrame f)
		{
			frame = f;
		}
		
		@Override
		public void entitySelected(CKGraphicsAsset asset)
		{
			CKAssetViewer view = new CKAssetViewer(10,asset,new Dimension(400,400),true);
			artIcon.setIcon(view.getAsset());
			stateChanged(new ChangeEvent(ArtifactPropertiesEditor.this));
			frame.dispose();
			//set currentArtifact to the artifact associated with whatever icon was just selected
		}
	}
	
	

	
	Vector<ChangeListener> cListeners = new Vector<ChangeListener>();
	@Override
	public void addChangeListener(ChangeListener l)
	{
		cListeners.add(l);		
	}

	public void stateChanged(ChangeEvent e)
	{
		privateStoreState();
		
		for(ChangeListener l:cListeners)
		{
			l.stateChanged(e);
		}
		
	}
	
	@Override
	public CKArtifact getAsset()
	{
		return artifact;
	}

	public void storeState()
	{
		abilityTree.stopEditing();
		limitTree.stopEditing();
		reqTree.stopEditing();
		
		
		privateStoreState();
	}
	
	private void privateStoreState()
	{
		artifact.setName(name.getText());
		artifact.setIconId(artIcon.getAsset().getAID());
		artifact.setBackstory(backstory.getText());
		artifact.setAbilties((CKBook) abilityTree.getRoot());
		artifact.setLimits((CKBook) limitTree.getRoot());
		artifact.setRequirements((BookList) reqTree.getRoot());
		
	}
	
	
	
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		CKArtifact boots = CKArtifactFactory.getInstance().getAsset("combatBoots");
		
		frame.add(new ArtifactPropertiesEditor(boots,true));
		frame.setVisible(true);
		frame.setSize(700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		stateChanged(new ChangeEvent(this));
		
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		stateChanged(new ChangeEvent(this));
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		stateChanged(new ChangeEvent(this));
	}

	
}
