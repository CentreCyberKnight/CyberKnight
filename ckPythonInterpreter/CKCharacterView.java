package ckPythonInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckCommonUtils.DisabledPanel;
import ckCommonUtils.EquipmentComparator;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetButton;
import ckEditor.CKAssetLabel;
import ckEditor.CKBookView;
import ckEditor.CKTeamArtifactEditor;
import ckGameEngine.ActorController;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKPage;
import ckGameEngine.CKStatsChangeListener;



public class CKCharacterView extends JPanel implements CKStatsChangeListener
{

	private static final long serialVersionUID = -5611795151955932240L;
	CKGridActor character;
	
	public final static String TEXT_CONTROLLER="Text Controller";
	public final static String ARTIFACT_CONTROLLER="Artifact Controller";
	public final static String SHOW_STATS="Stat Veiwer";
	
	String state = TEXT_CONTROLLER;
	String lastState = SHOW_STATS;
	
	
	JLabel 	cpLabel = new JLabel();
	JButton showStatsButton = new JButton();
	
	JPanel 	display = new JPanel(new CardLayout());
	DisabledPanel artDPanel;
	DisabledPanel textDPanel;
	JPanel statsView;
	
	JButton runButton;
	CKPythonEditorPane editor;	
	
	CKBookView bookStats = new CKBookView(new CKBook());
	
	
	
	public CKCharacterView(CKGridActor character2)
	{
		this.character = character2;
		character.addListener(this);
		
		String permissions = character.getTurnController().getPermissions();
		lastState = SHOW_STATS;
		if(ActorController.TEXT_CONTROL.compareTo(permissions)==0)
		{
			state = TEXT_CONTROLLER;
		}
		else if (ActorController.ARTIFACT_CONTROL.compareTo(permissions)==0 ||
				ActorController.BOTH_CONTROL.compareTo(permissions)==0)
		{
			state = ARTIFACT_CONTROLLER;
		}
		else //special case!!
		{
			state = SHOW_STATS;
		}
		
		
		initPane();
		stateChanged(null);
		doMyLayout();
		

		
		
		
		/*
		CKBookView comp = new CKBookView(character2.getAbilities());
		//JComponent comp = character2.getAbilities().getXMLAssetViewer();
		character2.addListener(comp);
		comp.setPreferredSize(new Dimension(200,125));
		add(comp,BorderLayout.CENTER);		 
		*/
		
		
		//set button
		toggleStatsView();
		toggleStatsView(); //back to original state
		
	}
	
	
	public void initPane()
	{
		showStatsButton.addActionListener(new ToggleStatsListener());
		statsChanged(character.getAbilities());
		
	}
	
	
	
	public void stateChanged(Event e)
	{
				
		//header
		cpLabel.setText("<html><h1>CP: "+character.getCyberPoints()+"</h1></html>");
		

		
		
		
		
		
	}
	
	protected void doMyLayout()
	{
		removeAll();
		setLayout(new BorderLayout());
		layoutHeader();
		layoutDisplay();
		
		
		
	}
	
	
	protected void layoutHeader()
	{
		
		JPanel header = new JPanel(new BorderLayout());
		
		
		//setPreferredSize(new Dimension(250,175));
		header.add(new CKAssetLabel(getPortrait()),BorderLayout.WEST  );
		
		JLabel name = new JLabel("<html><h2>"+character.getName()+"</h2></html>");
		name.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		header.add(name,BorderLayout.NORTH);
		
		JPanel quickStats= new JPanel(new BorderLayout());
		
		
		quickStats.add(cpLabel,BorderLayout.CENTER);
		
		
		
		quickStats.add(showStatsButton,BorderLayout.SOUTH);
		
		header.add(quickStats,BorderLayout.CENTER);
		
		
		add(header,BorderLayout.NORTH);
		
	}
	
	
	public void layoutDisplay()
	{
		String permissions = character.getTurnController().getPermissions();
		//display
		
		
		//Artifact Display
		JPanel tight = new JPanel();
		tight.setLayout(new BorderLayout());
		tight.add(new CKEquippedView(this.character),BorderLayout.NORTH);
		if(ActorController.BOTH_CONTROL.compareTo(permissions)==0)
		{
			JButton toText = new JButton("Switch to Text");
			toText.addActionListener(new ShowTextView());
			tight.add(toText,BorderLayout.PAGE_END);
		}
		artDPanel = new DisabledPanel(tight);
		display.add(artDPanel,ARTIFACT_CONTROLLER);
		
		//Text Display
		JPanel text = new JPanel(new BorderLayout());
		text.add(createEditorPane(), BorderLayout.CENTER);
		if(ActorController.BOTH_CONTROL.compareTo(permissions)==0	)
		{
			JButton toArtifacts = new JButton("Switch to Artifact");
			toArtifacts.addActionListener(new ShowArtifactView());
			text.add(toArtifacts,BorderLayout.PAGE_END);
		}
		textDPanel = new DisabledPanel(text);
		display.add(textDPanel,TEXT_CONTROLLER);
		
		//AddStats
		statsView = createStatsView();
		display.add(statsView,SHOW_STATS);
		
		add(display,BorderLayout.CENTER);
		
		
		
		
	}
	
	protected JPanel createStatsView()
	{
		JPanel stats = new JPanel(new BorderLayout(2,2));
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		
//		left.
		
		
		left.setBorder(new EmptyBorder(11,2,11,2));

		Vector<String> positions = getOrderedList();
		//left.add(Box.createRigidArea(new Dimension(1,11)));
		for (String pos : positions)
		{
			CKAssetButton artifactButton = new CKAssetButton();
			// artifactButton.setAlignmentY(alignment);
			artifactButton.setPreferredSize(new Dimension(64, 64));
			CKArtifact art = character.getArtifact(pos);
			if (art == null)
			{
				artifactButton.setIcon("equipment");
				artifactButton.setEnabled(false);
			} else
			{
				artifactButton.setIcon(art.getIconId());
				artifactButton.addActionListener(new ArtifactViewListener(art));
			}

			left.add(artifactButton);
			left.add(Box.createRigidArea(new Dimension(1,7)));

		}
		stats.add(left, BorderLayout.LINE_START);
		stats.add(bookStats,BorderLayout.CENTER);
		
		/*stats.add(character.getAbilities().getXMLAssetViewer(),
				BorderLayout.CENTER);
				*/
		return stats;
	}
	
	
	
	public void enableCharacter(boolean enable)
	{
		//artDPanel.setEnabled(enable);
		//textDPanel.setEnabled(enable);
	}
	
	
	
	class ShowArtifactView implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			CardLayout cl = (CardLayout)(display.getLayout());
			lastState = state;
			state = ARTIFACT_CONTROLLER;
			cl.show(display, state);

		}
		
	}
	
	class ShowTextView implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			CardLayout cl = (CardLayout)(display.getLayout());
			lastState = state;
			state= TEXT_CONTROLLER;
			 cl.show(display,state);
		}
		
	}
	
	
	
	public void toggleStatsView()
	{
		CardLayout cl = (CardLayout)(display.getLayout());
		
	
		if(state == lastState && state == SHOW_STATS) //special case-no controller
		{
			showStatsButton.setText("Hide Stats");
			showStatsButton.setEnabled(false);
		}
		if(state==SHOW_STATS)
		{
			cl.show(display,lastState);
			state = lastState;
			lastState = SHOW_STATS;
			showStatsButton.setText("Full Stats");
		}
		else
		{
			lastState= state;
			state=SHOW_STATS;
			cl.show(display,state);
			showStatsButton.setText("Hide Stats");
		}
	}
	
	class ToggleStatsListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			toggleStatsView();
		}
		
	}
	
	
	
	
	
	protected String getPortrait()
	{
	CKGraphicsAssetFactory factory =  CKGraphicsAssetFactoryXML.getInstance(); 
	return factory.getPortrait(character.getAssetID()).getAID();
			
	}

	 private JPanel createEditorPane()
	 {
		 JPanel pane=new JPanel();
		 pane.setLayout(new BorderLayout());
		// pane.setPreferredSize(new Dimension(300,300));

		 //create unique editor
		 //editor = CKGameObjectsFacade.getUniqueEditor();
		
		 editor = new CKPythonEditorPane();
		 
		 pane.add(editor.getScrollablePane());
	           		    
		 //TOOL BARS
		 //toolbar is part of the editor kit--awesome!
		 JToolBar jToolBar1 = new javax.swing.JToolBar();
		 jToolBar1.setRollover(true);
		 jToolBar1.setFocusable(false);
		 	
		 EditorKit kit = editor.getEditorKit();
		 if (kit instanceof DefaultSyntaxKit) 
		 {
			 DefaultSyntaxKit defaultSyntaxKit = (DefaultSyntaxKit) kit;
			 defaultSyntaxKit.addToolBarActions(editor, jToolBar1);
		 }	
		 jToolBar1.validate();
		 pane.add(jToolBar1, BorderLayout.PAGE_START);
		 
	
	    runButton = new JButton("Run Code");
//	    CKGameObjectsFacade.setRunButton(runButton);
	    
//	    CKGameObjectsFacade.disableTextInput();	
	    
	    ButtonHandler handler = new ButtonHandler();
	    runButton.addActionListener(handler);
	    pane.add(runButton,BorderLayout.PAGE_END);
	        
		 
		editor.setText("move('forward',1)\n" +
				"pc = aim('front',5) \n" +
				"earth('shove',1,pc)");
	    return pane;
	       
		 
	 }
	 
	 private class ButtonHandler implements ActionListener
	 	{
	 		public void actionPerformed(ActionEvent event)
	 		{
	 			String code = "from ckPythonInterpreter.CKEditorPCController import * \n\n"+
						character.getTeam().getFunctions()+"\n"+
	 					editor.getText();
	 			character.getTurnController().fireLogEvent(editor.getText()+'\n');
				CKGameObjectsFacade.runSpell(code,null);//,new consoleThreadFinishes());
	 		}
	 		
	 	}
	 
	 
	 public Vector<String> getOrderedList()
		{
			CKChapter chap = this.character.getAbilities().getChapter(CH_EQUIP_SLOTS);
			Vector<String> list = new Vector<String>();
			Iterator<CKPage> iter = chap.getPages();
			while(iter.hasNext())
			{
				list.add( iter.next().getName());
			}
			Collections.sort(list,EquipmentComparator.getComparator());
			return list;
		}
	
	
	 
	 
	 
	 
	static class ArtifactViewListener implements ActionListener
	{

		
		CKArtifact art;
		
		public ArtifactViewListener(CKArtifact a)	
		{
			art=a;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			CKTeamArtifactEditor editor = CKGameObjectsFacade.getArtifactEditor();
			if(editor!=null)
			{
				editor.setArtifact(art);				
			}			
		}
		
		
		
		
		
	}
	 
	 
	 
	@Override
	public void equippedChanged()
	{
		// 
		display.remove(statsView);
		statsView = createStatsView();
		display.add(statsView,SHOW_STATS);
		
		//call twice to reset
		toggleStatsView();
		toggleStatsView();
		
	}

	@Override
	public void statsChanged(CKBook newStats)
	{
		bookStats.setBook(newStats);
		
	}


	public CKGridActor getActor()
	{
		return character;
	}


	@Override
	public void cpChanged(int cp)
	{
		stateChanged(null);
	}

}
