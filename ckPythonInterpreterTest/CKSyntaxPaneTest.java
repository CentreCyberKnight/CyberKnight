package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.syntaxkits.PythonSyntaxKit;


public class CKSyntaxPaneTest 
{

    public static void main(String[] args)
    	{
        java.awt.EventQueue.invokeLater(new Runnable() 
        	{

            @Override
            	public void run() 
            	{
                	new CKSyntaxPaneTest();//.setVisible(true);
            	}
        	}
        	);
    	}

    public CKSyntaxPaneTest() {
        JFrame f = new JFrame("Party");//CKSytaxPaneTest.class.getName());
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        //DefaultSyntaxKit.initKit();

        final JEditorPane codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        //codeEditor.setContentType("text/python");
        codeEditor.setEditorKit(new PythonSyntaxKit());
        //codeEditor.setText("public static void main(String[] args) {\n}");
        
        //toolbar is part of the editor kit--ausome!
        JToolBar jToolBar1 = new javax.swing.JToolBar();
        jToolBar1.setRollover(true);
        jToolBar1.setFocusable(false);
             
        EditorKit kit = codeEditor.getEditorKit();
		if (kit instanceof DefaultSyntaxKit) {
			DefaultSyntaxKit defaultSyntaxKit = (DefaultSyntaxKit) kit;
			defaultSyntaxKit.addToolBarActions(codeEditor, jToolBar1);
		}
		jToolBar1.validate();
		c.add(jToolBar1, BorderLayout.PAGE_START);
		
		 
        c.add(scrPane, BorderLayout.CENTER);
        //c.add(codeEditor, BorderLayout.CENTER);
        
        //create console for output?
        JTextPane tpane = new JTextPane();
        c.add(tpane,BorderLayout.LINE_START);
        c.doLayout();
        
        
        
        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
	}
	


