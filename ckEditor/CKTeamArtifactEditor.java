package ckEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKArtifactFactory;
import ckEditor.Artifact.window;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKDeltaBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKTeam;
import ckPythonInterpreter.CKTeamView;


public class CKTeamArtifactEditor extends JPanel implements ChangeListener
{
	
	
	//  bar
	//  artifact | stats | actor-selection
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8339155704904587263L;

	private CKTeam team;
	
	private HashMap<String,Vector<CKArtifact>> collections=new HashMap<String,Vector<CKArtifact>>();
	private String [] aTypes = {"shoes","mouth","weapon","armor","misc"};
	
	
	private ArtifactPropertiesEditor editor;
	
	private JButton equipButton=new JButton("try it");
	
	
	public CKTeamArtifactEditor(CKTeam team)
	{
		super(new BorderLayout());
		this.team=team;
		
		Vector<CKArtifact> vec = team.getArtifacts();
		if(vec.size()==0)
		{
			editor = new ArtifactPropertiesEditor(CKArtifactFactory.getInstance().getAssetInstance() ,false);			
		}
		else
		{
			editor = new ArtifactPropertiesEditor(vec.get(0) ,false);
		}
		
		add(editor,BorderLayout.CENTER);
		createSelectionBar();
		setEquipButton();
		setMyListeners();
		
		
		
		
	}
	
	
	protected void setMyListeners()
	{
		
		
		CKTeamView view = CKGameObjectsFacade.getArtifactController();
		if(view!=null)
		{
			view.addChangeListener(this);
		}			
	}
	
	
	
	protected void createSelectionBar()
	{
		
		
		
		//first create all of the data
		CKArtifactFactory aFactory = CKArtifactFactory.getInstance();
		
		
		for(CKArtifact art: team.getArtifacts())
		{
			for(String usage:aTypes)
			{
				if(aFactory.hasUsage(art.getAID(),usage ))
				{
					Vector<CKArtifact> vec = collections.get(usage);
					if(vec == null) 
					{
						vec = new Vector<CKArtifact>(); 
						collections.put(usage, vec);
						
					}
					
					vec.add(art);					
				}				
			}
		}
			
		//now we have all of the collections set, create buttons
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bar.add(new JLabel("please choose"));
		Set<String> keys = collections.keySet();
		
		for(String k:keys)
		{
			System.out.println(k);
			Vector<CKArtifact> artifacts = collections.get(k);
			CKAssetButton but = new CKAssetButton();//artifacts.get(0).getIconId());
			but.setPreferredSize(new Dimension(50,50));
			but.setVerticalAlignment(SwingConstants.BOTTOM);
			but.setIcon(artifacts.get(0).getIconId());
			//but.setText(k);
			but.setToolTipText(k);
			but.addActionListener(new ArtifactClassPopupListener(artifacts));
			//need to add the listener
			
			bar.add(but);
	
				
		}
		
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header,BoxLayout.X_AXIS));
		equipButton.setPreferredSize(new Dimension(100,50));
		header.add(equipButton);
		//header.add(Box.createRigidArea(new Dimension(100,0)));
		header.add(Box.createGlue());
		header.add(bar);
		
		add(header,BorderLayout.PAGE_START);
		
		
		
		
	}
	
	class ArtifactClassPopupListener implements ActionListener
	{
		Vector<CKArtifact> vec;
		
		
		public ArtifactClassPopupListener(Vector<CKArtifact> v)
		{
			vec =v;
		}
		
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();

			CKXMLAssetPicker<CKArtifact> picker = new CKXMLAssetPicker<CKArtifact>(vec.iterator() );
					
			picker.addSelectedListener(new ArtifactListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}

	}

	class ArtifactListener implements
			CKEntitySelectedListener<CKArtifact>
	{
		JFrame frame;

		public ArtifactListener(JFrame f)
		{
			frame = f;
		}

		@Override
		public void entitySelected(CKArtifact a)
		{ 
			setArtifact(a);
			
			
			frame.setVisible(false); // you can't see me!
			frame.dispose(); // Destroy the JFrame object
		}

	}
	
	
	public void setArtifact(CKArtifact a)
	{
		remove(editor);
		if(a ==null)
		{
			a = CKArtifactFactory.getInstance().getAssetInstance();
		}
		editor = new ArtifactPropertiesEditor(a, false);
		add(editor,BorderLayout.CENTER);

		revalidate();
		stateChanged(null);
	}
	
	
	
	
	
	public static void main(String [] args)
	{
		CKTeam team = new CKTeam();
		
		Vector<CKArtifact> vec = CKArtifactFactory.getInstance().getAllAssetsVectored();
		team.setArtifacts(vec);
		
		
		JFrame frame = new JFrame();

		
		frame.add(new CKTeamArtifactEditor(team));
		
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		
	}



	@Override
	public void stateChanged(ChangeEvent e)
	{ //selected actor has changed!
		System.err.println("actor has changed!!");
		setEquipButton();
	}
	

	public CKGridActor getActiveActor()
	{
		CKTeamView view = CKGameObjectsFacade.getArtifactController();
		if(view!=null)
		{
			return view.getSelectedActor();
		}		
		return null;
	}
	
	public void sendDeltaBook(CKDeltaBook book)
	{
		CKTeamView view = CKGameObjectsFacade.getArtifactController();
		if(view!=null)
		{
			view.getSelectedView().statsChanged(book);
		}		

	}
	
	
	ActionListener equipButtonListener;
	
	public void setEquipButton()
	{
		
		equipButton.removeActionListener(equipButtonListener);
		final CKGridActor actor = getActiveActor();
		final CKArtifact artifact = editor.getAsset();
		
		
		if(actor == null || artifact==null ||
				! actor.canEquip(artifact))
		{

			equipButton.setText("Can't Equip");
			equipButton.setEnabled(false);
		}
		else if (team.getCharacter(artifact.getEquippedBy())==actor)
		{
			equipButton.setText("Unequip");
			equipButton.setEnabled(true);
			equipButtonListener = new ActionListener()
			{	
				@Override
				public void actionPerformed(ActionEvent e)
				{
//					team.unequipArtifact(artifact);
					actor.unequipArtifact(artifact);
					setEquipButton();
				}
				
			};
			
			equipButton.addActionListener(equipButtonListener);
		}
		else //can equip
		{
			equipButton.setText("Equip");
			equipButton.setEnabled(true);
			//calc book
			CKDeltaBook book = team.calcDeltaBook(actor,artifact);
			sendDeltaBook(book);
			
			equipButtonListener = new ActionListener()
			{	
				@Override
				public void actionPerformed(ActionEvent e)
				{
					actor.equipArtifact(artifact);
					//team.equipArtifact(artifact,actor.getName());
					setEquipButton();
				}
				
			};
			
			equipButton.addActionListener(equipButtonListener);

			
			
		}
			
		
	}
	
	
	
	
	
	

}
