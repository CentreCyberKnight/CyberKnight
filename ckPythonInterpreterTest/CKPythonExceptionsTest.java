package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKUniqueEditor;
import jsyntaxpane.DefaultSyntaxKit;


public class CKPythonExceptionsTest 
{
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JButton runButton;
	
	
	
	 public CKPythonExceptionsTest() 
	 {
	        JFrame f = new JFrame("Party23");//CKSytaxPaneTest.class.getName());
	        final Container c = f.getContentPane();
	        c.setLayout(new BorderLayout());
	        System.out.println("should be worlking");
	        editor = CKUniqueEditor.getUniqueEditor();
	        c.add(editor.getScrollablePane());
	        editor.setText("print x");
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
	        

	    }
	
	 
	 	private class ButtonHandler implements ActionListener
	 	{
	 		public void actionPerformed(ActionEvent event)
	 		{
	 			
	 			//get code from editor
	 			String code =editor.getText();
	 			//System.out.println("got to here with the code "+code);
	 			String new_code=editor.HLcode(code);
	 			//send code to console to run
	 			console.runNewCode(new_code);
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
	            	new CKPythonExceptionsTest();//.setVisible(true);
	        	}
	    	}
	    	);
			

		}

}
