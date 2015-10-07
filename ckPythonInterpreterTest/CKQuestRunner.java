package ckPythonInterpreterTest;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckCommonUtils.CKThreadCompletedListener;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.Quest;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.QuestData;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKPythonConsoleExtended;

public class CKQuestRunner 
{ 
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JButton runButton;
	JTabbedPane tabpane;
	JFrame frame;
	Quest quest;
	
	 public CKQuestRunner(QuestData q) 
	 {
		 quest = new Quest(q);
		 frame = new JFrame("CyberKnight Level 1");//CKSytaxPaneTest.class.getName());
		 final Container c = frame.getContentPane();
		 c.setLayout(new BorderLayout());
		 
		 
		 JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		 split.setLeftComponent(createTabPane());
		 
		 split.setRightComponent(createEditorPane());
		 
		 
		 c.add(split,BorderLayout.CENTER);
		 c.doLayout();    	

		 frame.setSize(1000, 600);
		 frame.setVisible(true);
		 //MKB called form other boss frame... frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		 String cmd = "from ckPythonInterpreter.CKPlayerObjectsFacade import * \n"+
		 "C = getCharacter()\n"+
		 "C.move('forward',1)\n";				 
		 editor.setText(cmd);
		 //System.out.println("World = " + world);

		 //world.gameLoop();
		 Thread T = new gameThread();
		 T.start();

	    }

	 
	 class gameThread extends Thread
	 {
		 public void run()
		 {
			 quest.gameLoop();
		 }
 }
	 
	 
	
	 private JPanel getScenePanel(int sceneId)
	 {
		 //DOn't need to call since quest is already initialized
		 //return new CKSceneViewer(scene,sceneId);
		 //quest = CKGameObjectsFacade.getQuest();
		 //quest.creation(sceneId, frame);
		 CKGameObjectsFacade.setQuest(quest);
		 return CKGameObjectsFacade.getJPanelEngine();
	 }

	 
	
	 
	 private JTabbedPane createTabPane() 
	 {
		 JTabbedPane tab = new JTabbedPane();
		 tab.setPreferredSize(new Dimension(600,600));
		 
		 tab.addTab("Console", createConsolePane());
		 tab.setMnemonicAt(0, KeyEvent.VK_F1);
		 
		 tab.addTab("game",getScenePanel(3));		 
		 tab.setMnemonicAt(1, KeyEvent.VK_F2);
		 
		 tab.addTab("documentation",createURLPage("http://docs.python.org/"));
		 tab.setMnemonicAt(2, KeyEvent.VK_F3);

		 tab.addTab("tree",new CKTreeGui(quest.getQuestData()));		 
		 tab.setMnemonicAt(3, KeyEvent.VK_F4);

		 
		 
		 
		 
		 tab.setSelectedIndex(1);
		 
		 return tab;
	}

	 
	 
	 private JComponent wrapInScrolls(JComponent j)
	 {
		 JScrollPane scroll = new JScrollPane(j);
		 scroll.setVerticalScrollBarPolicy(
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	     return scroll;
		 
	 }
	 
	 private JComponent createURLPage(String url)
	 {
		 
		 JEditorPane editorPane = new JEditorPane();
		 editorPane.setEditable(false);
		 try {
			 java.net.URL data = new URL(url);
			 editorPane.setPage(data);
		     } catch (IOException e) {
		         System.err.println("Attempted to read a bad URL: " + url);
		     }
		     
		 return wrapInScrolls(editorPane);
		 
	 }
	 
	private JComponent createConsolePane()
	 {
		 
			console = new CKPythonConsoleExtended();
			console.setPreferredSize(new Dimension(400,600));
	        return wrapInScrolls(console);
	        
	 }
	 
	 
	 
	 
	 private JPanel createEditorPane()
	 {
		 JPanel pane=new JPanel();
		 pane.setLayout(new BorderLayout());
		 pane.setPreferredSize(new Dimension(300,600));

		 //create unique editor
		 editor = CKGameObjectsFacade.getUniqueEditor();
		
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
	    CKGameObjectsFacade.setRunButton(runButton);
	    
	    CKGameObjectsFacade.disableTextInput();	
	    
	    ButtonHandler handler = new ButtonHandler();
	    runButton.addActionListener(handler);
	    pane.add(runButton,BorderLayout.PAGE_END);
	        
		 
		 
	    return pane;
	       
		 
	 }
	 
	 
	 
	 
	 	private class ButtonHandler implements ActionListener
	 	{
	 		public void actionPerformed(ActionEvent event)
	 		{
	 			
	 			//get code from editor
	 			String code =editor.getText();
	 			//System.out.println("got to here with the code "+code);
	 			//send code to console to run
	 			console.runNewCode(code,new consoleThreadFinishes());
	 			
	 			
	 			
	 		}
	 		
	 	}
	 	
	 	private class consoleThreadFinishes implements CKThreadCompletedListener
	 	{

			@Override
			public  void threadFinishes(boolean error)
			{
				CKGameObjectsFacade.disableTextInput();
	 			CKGameObjectsFacade.getQuest().notifyOfInput();
				
			}
	 		
	 	}
	 	
	 	/*
	 	public static void runInstance(Quest q)
	 	{
	 		
			java.awt.EventQueue.invokeLater(new MYStarter(q));			
	 		
	 	}
	 	
	 	
	 	class MYStarter implements Runnable
	 	{
	 		
	 		Quest q;
	 		public MYStarter(Quest q)
	 		{
	 			this.q = q;
	 		}

			@Override
			public void run()
			{
				new CKQuestRunner(q);
				
			}
	 		
	 		
	 		
	 		
	 	}
	 	
	 	*/
	 	
	 	

		/**
		 * @param args
		 */
	/*	public static void main(String[] args) {
			
			// World initalized in get Scene Panel probably DONE
			// need to somehow give world scene Panel DOne... I think? Just the scene righht?
			//Game loop and such may be able to be put into 
			//linked editor constructor (This does not work I don't think, causes program to lock up? Syncronization? )
			//world.gameLoop(); Maybe give send some variables that could be decoded in world or something? Think think think

			java.awt.EventQueue.invokeLater(new Runnable() 
	    	{

	        @Override
	        	public void run() 
	        	{
	        	
	            	new CKQuestRunner();//.setVisible(true);
	            	
	        	}
	    	}
	    	);
			

		}
*/
}
