package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Container;

//import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
//import javax.swing.JTextPane;
//import javax.swing.JToolBar;
import javax.swing.WindowConstants;
//import javax.swing.text.EditorKit;

import ckPythonInterpreter.CKPythonConsolePane;

//import jsyntaxpane.DefaultSyntaxKit;
//import jsyntaxpane.syntaxkits.PythonSyntaxKit;


public class CKPythonConsolePaneTest 
{

    public static void main(String[] args)
    	{
        java.awt.EventQueue.invokeLater(new Runnable() 
        	{

            @Override
            	public void run() 
            	{
                	new CKPythonConsolePaneTest();//.setVisible(true);
                	
            	}
        	}
        	);
    	}

    public CKPythonConsolePaneTest() {
        JFrame f = new JFrame("Party");//CKSytaxPaneTest.class.getName());
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        //DefaultSyntaxKit.initKit();

        final CKPythonConsolePane console = new CKPythonConsolePane();
        //final JTextPane console = new JTextPane();
        JScrollPane scrPane = new JScrollPane(console);
		 
        c.add(scrPane, BorderLayout.CENTER);
        //c.add(codeEditor, BorderLayout.CENTER);
        


        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        console.runTest();

    }
	}
	


