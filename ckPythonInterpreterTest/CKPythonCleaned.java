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
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKUniqueEditor;


public class CKPythonCleaned 
{
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JButton runButton;
	JTabbedPane tabpane;
	
	
	 public CKPythonCleaned() 
	 {
	        JFrame f = new JFrame("Ausome editor of power!");//CKSytaxPaneTest.class.getName());
	        final Container c = f.getContentPane();
	        c.setLayout(new BorderLayout());
	        
	        
	        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	        split.setRightComponent(createEditorPane());
	        split.setLeftComponent(createTabPane());
	        //c.add(createEditorPane(),BorderLayout.CENTER);
	        
	        c.add(split,BorderLayout.CENTER);
	    	c.doLayout();
	                
	        f.setSize(800, 600);
	        f.setVisible(true);
	        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        
	        
	        String cmd =
	        	"from ckPythonInterpreter.CKUniqueEditor import *\n" +
	        	//"import ckPythonInterpreter.CKUniqueEditor\n" +
	        	"ed=getUniqueEditor()\n" +
	        	"ed.setText('It Worked')\n";
	        editor.setText(cmd);
	        	
	        	
	        

	    }
	
	 private JTabbedPane createTabPane() 
	 {
		 JTabbedPane tab = new JTabbedPane();
		 tab.setPreferredSize(new Dimension(410,600));
		 
		 tab.addTab("Console", createConsolePane());
		 tab.setMnemonicAt(0, KeyEvent.VK_F1);
		 
		 tab.addTab("scribbles",createURLPage("http://vault.hanover.edu/~bradshaw/"));
		 tab.setMnemonicAt(1, KeyEvent.VK_F2);
		 
		 tab.addTab("documentation",createURLPage("http://docs.python.org/"));
		 tab.setMnemonicAt(1, KeyEvent.VK_F3);
		
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

		 //create unique editor
		 editor = CKUniqueEditor.getUniqueEditor();
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
	 			console.runNewCode(code);
	 		}
	 		
	 	}

		/**
		 * @param args
		 */
		public static void main(String[] args) {
			
			java.awt.EventQueue.invokeLater(new Runnable() 
	    	{

	        @Override
	        	public void run() 
	        	{
	            	new CKPythonCleaned();//.setVisible(true);
	        	}
	    	}
	    	);
			

		}

}
