package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.JEditorPane;
import javax.swing.JFrame;
//import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
//import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import ckPythonInterpreter.CKPythonEditorPane;
//import ckPythonInterpreterTest.CKPythonSuiteTest.ButtonHandler;

import jsyntaxpane.DefaultSyntaxKit;

public class CKPythonEditorPaneTest 
{
	CKPythonEditorPane editor;
	JButton runButton;
	JTextField numberfield;
	
    public static void main(String[] args)
    	{
        java.awt.EventQueue.invokeLater(new Runnable() 
        	{

            @Override
            	public void run() 
            	{
                	new CKPythonEditorPaneTest();//.setVisible(true);
            	}
        	}
        	);
    	}
    
 	private class ButtonHandler implements ActionListener
 	{
 		public void actionPerformed(ActionEvent event)
 		{
 			
 			//get code from editor
 			String code = numberfield.getText();
 			try
 			{
 				int num= Integer.parseInt(code.trim());
 				editor.RemoveHL();
 				editor.HiLight(num,num+1,Color.yellow);
 			}
 				/*try {
					System.out.printf("I tried");
 					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				//editor.RemoveHL();
 			}*/
 			catch (NumberFormatException nfe)
			{
 		
			}
 			System.out.println("got to here with the code "+code);
 			//send code to console to run				
 			}
 	}

    public CKPythonEditorPaneTest() {
        JFrame f = new JFrame("Party23");//CKSytaxPaneTest.class.getName());
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        System.out.println("should be working");
        editor = new CKPythonEditorPane();
        c.add(editor.getScrollablePane());
        
        //TOOL BARS
        //toolbar is part of the editor kit--ausome!
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
		
		runButton = new JButton("Run Code");
        ButtonHandler handler = new ButtonHandler();
        runButton.addActionListener(handler);
        c.add(runButton,BorderLayout.PAGE_END);
        
      //create console for output?
        numberfield= new JTextField();
        numberfield.setPreferredSize(new Dimension(200,400));
        c.add(numberfield,BorderLayout.LINE_START);
        c.doLayout();
                
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
