package ckEditor;

import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGridActorFactory;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKTeam;


public class CKTeamPropertiesEditorV2 extends CKXMLAssetPropertiesEditor<CKTeam> implements ChangeListener,DocumentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;

	CKTeam asset;
	
	JTextField name;
	JTextArea functions;
	
	
	
	
	
	CKTreeGui   myAbilities;
	CKTreeGui   myStory;
	
	//FIXME add artifacts
	//show/add characters-names?
	//equip characters


		
	
	public CKTeamPropertiesEditorV2(CKTeam asset,boolean editable)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		add(left);
		
		

		
		left.add(new JLabel("name"));
		name = new JTextField(asset.getName());
		name.setEditable(editable);
		left.add(name);
		
		left.add(new JLabel("Functions"));
		functions = new JTextArea(asset.getFunctions(),10,30);
		functions.setEditable(editable);
		left.add(functions);
		
		left.add(new JLabel("Abilities"));
		
		myAbilities = new CKTreeGui(this.asset.getAbilities(),editable);
		left.add(myAbilities);
		
		left.add(new JLabel("Story Record"));
		myStory = new CKTreeGui(asset.getStory(),editable);
		left.add(myStory);
				
		
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right,BoxLayout.Y_AXIS));
		
		JButton addArtifactButton = new JButton("Add Artifact");
		addArtifactButton.addActionListener( 
				e->{
					JFrame frame = new JFrame();
					CKArtifactFactory factory =CKArtifactFactory.getInstance(); 
					
					CKXMLAssetPicker<CKArtifact> picker = 
							new CKXMLAssetPicker<CKArtifact>(factory.getAllAssets());
					picker.addSelectedListener(
							new CKEntitySelectedListener<CKArtifact>()
							{

								@Override
								public void entitySelected(CKArtifact entity)
								{
									addArtifact(entity,right);
									asset.addArtifact(entity);
									right.revalidate();
									right.repaint();
								}
								
							});
							//new SceneListener(frame));
					frame.add(picker);
					frame.pack();
					frame.setVisible(true);
				}
				);
		
		right.add(addArtifactButton);
		
		
		
		for(CKArtifact art:asset.getArtifacts())
		{
			addArtifact(art,right);
		}
		add(right);
		
		
	}
	
	
	private void addArtifact(CKArtifact art,JPanel contain)
	{
		
		JPanel pane = new JPanel(new FlowLayout());
		CKArtifactShortView view = (CKArtifactShortView) art.getXMLAssetViewer();
		pane.add(view);
	
		JTextField box = new JTextField(art.getEquippedBy());

		box.setColumns(20);
		box.getDocument().addDocumentListener(new DocumentListener()
				{
					
					public void change()
					{
						String value = box.getText();
						if(value.equals("") )
						{
							asset.unequipArtifact(art);
						}
						else
						{
							//not one of the better ways to do this...
							//FIXME
							asset.equipArtifact(art,value,"hand");
						}
						stateChanged(null);
					}

					@Override
					public void changedUpdate(DocumentEvent arg0)
					{
						change();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0)
					{
						change();
						
					}

					@Override
					public void removeUpdate(DocumentEvent arg0)
					{
						change();
						
					}
				});
		contain.add(box);
		
		
		
		contain.add(pane);
		view.getEquipButton().addActionListener(
				e->{
					asset.removeArtifact(art);
					stateChanged(null);
					contain.remove(pane);
					contain.validate();
					contain.repaint();
				}
				);
		stateChanged(null);
	}
	

	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	


	@Override
	public void storeState()
	{
		myAbilities.stopEditing();
		myStory.stopEditing();
		
		privateStoreState();
	}
	
	
	
	private void privateStoreState()
	{	
		asset.setName(name.getText());
		asset.setFunctions(functions.getText());
		asset.setAbilities((CKBook) myAbilities.getRoot());
		asset.setStory((CKBook) myStory.getRoot());
		System.out.println("Storing Team");
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

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
		CKTeam team = new CKTeam();
		
		team.addCharacter((CKGridActor) CKGridActorFactory.getInstance().getAsset("SpellTestDad"));
		CKArtifact art = CKArtifactFactory.getInstance().getAsset("bareFeet");
		team.addArtifact(art);
		team.equipArtifact(art, "Dad", "shoes");
		
		
		
		
		
		CKTeamPropertiesEditorV2 view=new CKTeamPropertiesEditorV2(team,true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}



	@Override
	public CKTeam getAsset()
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
