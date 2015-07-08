package ckPythonInterpreter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKThreadCompletedListener;
import ckEditor.CKArtifactShortView;
import ckEditor.CKAssetButton;
import ckEditor.CKTeamArtifactEditor;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKStatsChangeListener;
import ckGameEngine.CKTeam;

public class CKEquipArtifactView extends JPanel implements CKStatsChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8641278173497994437L;
	private String pos;
	private CKGridActor character;
	private CKArtifact artifact;
	private JPanel emptyPanel;
	private CKArtifactShortView artifactPanel;
	//private boolean enabled;


	private String state;
	private final static String EMPTY = "EMPTY_VIEW";
	private final static String USED = "USED_VIEW";
	
	public CKEquipArtifactView(String s, CKGridActor character)
	{
		artifact=null;
		pos=s;
		this.character = character;
		setLayout(new CardLayout());
		setBorder( BorderFactory.createRaisedBevelBorder());
		
		//need two panels here
		createEmptyPanel();
		artifactPanel = new CKArtifactShortView();
		add(emptyPanel,EMPTY);
		add(artifactPanel,USED);		
		setState();
		
		//set listeners
		artifactPanel.getEquipButton().addActionListener(new UnequipListener());
		artifactPanel.getArtifactButton().addActionListener(new ShowArtifactListener());
		emptyPanel.addMouseListener(new EquipListener());
		//character.addListener(this); revisit if this is too slow.
		for(int i =0;i<CKArtifactShortView.MAX_SPELLS;i++)
		{
			artifactPanel.getSpellButton(i).addActionListener(new RunSpell(i));
		}
		
	}

	public void setState()
	{
		CKArtifact art = character.getArtifact(pos);
		
		if(art==null)
		{
	
			state = EMPTY;
		}
		else //need to set the artifact buttons
		{
			state=USED;
			artifactPanel.setArtifact(art);
			artifactPanel.getEquipButton().addActionListener(new CKCharacterView.ArtifactViewListener(art));
			
		}
		artifact = art;
		CardLayout cl = (CardLayout)(getLayout());
	    cl.show(this, state);
		
	}
	
	
	
	protected void createEmptyPanel()
	{
		emptyPanel = new JPanel();
		emptyPanel.setLayout(new BoxLayout(emptyPanel,BoxLayout.X_AXIS));
		CKAssetButton artifactButton  = new CKAssetButton();
		float alignment = BOTTOM_ALIGNMENT;
	    artifactButton.setAlignmentY(alignment);
		artifactButton.setPreferredSize(new Dimension(64,64));
		artifactButton.setIcon("equipment");
		artifactButton.setEnabled(false);
		emptyPanel.add(artifactButton);
		JLabel lab = new JLabel("<html><h2>"+pos+"</h2> <br />Click to Equip </html>");
		lab.setAlignmentY(alignment);
		emptyPanel.add(lab);
		emptyPanel.add(Box.createHorizontalGlue());
	}
	

	class RunSpell implements ActionListener
	{
		int pos;
		
		public RunSpell(int pos)
		{
			this.pos = pos;
//			spell=s;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKGridActor pc = (CKGridActor) CKGameObjectsFacade.getCurrentPlayer();
			System.out.println("YOU CLICKED A SPELL");
			if(pc != null && character == pc)
			{
				String code = "from ckPythonInterpreter.CKEditorPCController import * \n\n"+
					character.getTeam().getFunctions()+"\n"+
					artifact.getSpell(pos).getFunctionCall()+"\n";
				CKPlayerObjectsFacade.setArtifact(artifact);
				//FIXME-need to log the artifact getting set..
	 			character.getTurnController().fireLogEvent(	artifact.getSpell(pos).getFunctionCall()+"\n");
				CKGameObjectsFacade.runSpell(code,new ArtifactThreadFinishes());
			}
			
		}
		
	}
	
 	public class ArtifactThreadFinishes implements CKThreadCompletedListener
 	{

		@Override
		public  void threadFinishes(boolean error)
		{
			CKPlayerObjectsFacade.setArtifact(null);
			CKGameObjectsFacade.disableArtifactInput();
 			CKGameObjectsFacade.getQuest().notifyOfInput();
			
		}
 	}
	
	class UnequipListener implements ActionListener
	{		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKArtifact art = character.getArtifact(pos);
			if(art != null)
			{
				character.unequipArtifact(art);
				setState();
			}
		}
		
	}
	
	class EquipListener extends MouseAdapter
	{	
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent arg0)
		{
			JFrame frame = new JFrame();
						
			CKTeam team = character.getTeam();
			
			CKXMLAssetPicker<CKArtifact> picker = new CKXMLAssetPicker<CKArtifact> (team.equipIter(character,pos));
			
			picker.addSelectedListener(new SelectArtifactListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);

		
		
		
		}
		
	}
	
	class SelectArtifactListener implements CKEntitySelectedListener<CKArtifact>
	{
		JFrame p;
		public SelectArtifactListener(JFrame p)
		{
			this.p = p;
		}
		
		@Override
		public void entitySelected(CKArtifact entity)
		{
			p.setVisible(false); //you can't see me!
			p.dispose(); //Destroy the JFrame object
			character.equipArtifact(pos, entity);
			setState();			
		}
	}

	
	
	
	
	class ShowArtifactListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKTeamArtifactEditor editor = CKGameObjectsFacade.getArtifactEditor();
			if(editor!=null)
			{
				editor.setArtifact(artifact);				
			}			
		}
		
		
		
	}
	
	
	
	
	

	@Override
	public void equippedChanged()
	{
		setState();
		
	}

	@Override
	public void statsChanged(CKBook stats)
	{
		// do nothing		
	}

	@Override
	public void cpChanged(int cp)
	{
		
		
	}
}
