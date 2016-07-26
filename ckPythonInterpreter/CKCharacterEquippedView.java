package ckPythonInterpreter;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckCommonUtils.DisabledPanel;
import ckGameEngine.ActorController;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;

public class CKCharacterEquippedView extends JPanel
{
	
	private static final long serialVersionUID = -1006727793845218574L;
	CKGridActor character;
	JPanel input;
	DisabledPanel dPanel;
	public final static String TEXT_EDITOR="text editor";
	public final static String ARTIFACT_CONTROLLER="Artifact Controller";
	
	JButton runButton;
	 CKPythonEditorPane editor;	
	
	public CKCharacterEquippedView(CKGridActor character)
	{
		this.character = character;
		String permissions = character.getTurnController().getPermissions();
		
		//setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		add(new CKCharacterShortView(this.character),BorderLayout.NORTH);
		
		input = new JPanel(new CardLayout());
		JButton toText = new JButton("Switch to Text");
		toText.addActionListener(new ShowTextView());
		JButton toArtifacts = new JButton("Switch to Artifact");
		toArtifacts.addActionListener(new ShowArtifactView());
		
		
		JPanel tight = new JPanel();
		tight.setLayout(new BorderLayout());
		tight.add(new CKEquippedView(this.character),BorderLayout.NORTH);
		if(ActorController.BOTH_CONTROL.compareTo(permissions)==0)
		{
			tight.add(toText,BorderLayout.PAGE_END);
		}
		input.add(tight,ARTIFACT_CONTROLLER);
		
		JPanel text = new JPanel(new BorderLayout());
		text.add(createEditorPane(), BorderLayout.CENTER);
		if(ActorController.BOTH_CONTROL.compareTo(permissions)==0	)
		{
			text.add(toArtifacts,BorderLayout.PAGE_END);
		}
		input.add(text,TEXT_EDITOR);
		
		
		
		//should base this off of type of controller...
		CardLayout cl = (CardLayout)(input.getLayout());
		/*PCTurnController =  
		switch(character.getTurnControllerID())
		{
		
		}*/
		if(permissions.compareTo(ActorController.TEXT_CONTROL)==0)
		{
			cl.show(input, TEXT_EDITOR);
		}
		else 
		{
			cl.show(input, ARTIFACT_CONTROLLER);			
		}
		// cl.show(input, TEXT_EDITOR);
		 dPanel = new DisabledPanel(input);
		 add(dPanel);
		 dPanel.setEnabled(false);
		
		
		
//		add(Box.createRigidArea(new Dimension(0,1)));
	}
	
	class ShowArtifactView implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			CardLayout cl = (CardLayout)(input.getLayout());
			 cl.show(input, ARTIFACT_CONTROLLER);
		}
		
	}
	
	class ShowTextView implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			CardLayout cl = (CardLayout)(input.getLayout());
			 cl.show(input,TEXT_EDITOR);
		}
		
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
	
	public void enableCharacter(boolean enable)
	{
		dPanel.setEnabled(enable);
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
 	/*
 	private class consoleThreadFinishes implements CKThreadCompletedListener
 	{

		@Override
		public  void threadFinishes(boolean error)
		{
			CKGameObjectsFacade.disableTextInput();
// 			CKGameObjectsFacade.getQuest().notifyOfInput();
			
		}
 		
 	}*/
 	

	
	
}
