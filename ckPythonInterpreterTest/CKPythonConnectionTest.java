package ckPythonInterpreterTest;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKUniqueEditor;


public class CKPythonConnectionTest 
{
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JButton runButton;
	
	
	
	 public CKPythonConnectionTest() 
	 {
	        JFrame f = new JFrame("Party23");//CKSytaxPaneTest.class.getName());
	        final Container c = f.getContentPane();
	        c.setLayout(new BorderLayout());
	        System.out.println("should be worlking");
	        //editor = new CKPythonEditorPane();
	        editor = CKUniqueEditor.getUniqueEditor();
	        c.add(editor.getScrollablePane());
	        
	        //TOOL BARS
	        //toolbar is part of the editor kit--awesome!
	        JToolBar jToolBar1 = new javax.swing.JToolBar();
	        jToolBar1.setRollover(true);
	        jToolBar1.setFocusable(false);
	             
	        EditorKit kit = editor.getEditorKit();
			if (kit instanceof DefaultSyntaxKit) {
				DefaultSyntaxKit defaultSyntaxKit = (DefaultSyntaxKit) kit;
				defaultSyntaxKit.addToolBarActions(editor, jToolBar1);
			}
			jToolBar1.validate();
			c.add(jToolBar1, BorderLayout.PAGE_START);
			
	        
	        
	      //create console for output?
			console = new CKPythonConsoleExtended();
	        console.setPreferredSize(new Dimension(400,600));
	        //console.runTest();
	        
	        //Need a way to link console and editor
	        c.add(console,BorderLayout.LINE_START);
	        runButton = new JButton("Run Code");
	        ButtonHandler handler = new ButtonHandler();
	        runButton.addActionListener(handler);
	        c.add(runButton,BorderLayout.PAGE_END);
	        
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
	            	new CKPythonConnectionTest();//.setVisible(true);
	        	}
	    	}
	    	);
			

		}

}
