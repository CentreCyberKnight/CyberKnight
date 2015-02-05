package ckPythonInterpreter;


import javax.swing.JTextPane;
//import javax.swing.SwingUtilities;
import javax.swing.text.Document;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

//import org.omg.CORBA_2_3.portable.InputStream;
import org.python.util.InteractiveConsole;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;


public class CKPythonConsolePane extends JTextPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6759331096850111850L;
	PipedInputStream paneIn;
	PipedOutputStream paneOut;
	InteractiveConsole console;
	
	public CKPythonConsolePane()
	{
		console = new InteractiveConsole();
		
		try {
		//link pane's output to the input of the console.
		paneOut = new PipedOutputStream();
		console.setIn(new PipedInputStream(paneOut));
		
		//link pane's input to the output of the console
		paneIn =new  PipedInputStream();
		console.setOut(new PipedOutputStream (paneIn));
		//not sure if this would work...console.setErr(new PipedOutputStream (paneIn));
		}
		catch (IOException e)
		{}
		
		//need to create listener to control what the user types...
		addKeyListener(new consoleListener(paneOut));
		//need to create a thread to listen on the stream and write its output to the screen
		
		updateTextThread T = new updateTextThread(paneIn,(JTextPane)this);
		//SwingUtilities.invokeLater(T);
		T.start();
		//console.interact();
		runTest();
		
	}
	class test1 extends Thread
	{
		String cmd;
		InteractiveConsole console;
		public test1(InteractiveConsole co,String c)
		{
			cmd = c;
			console=co;
		}
		
		public void run()
		{
			console.interact();
			//console.exec(cmd);
		}
		
	}
	
	public void runTest()
	{
		String p= "name=raw_input('what is your name')\n";
		p+="print 'Hello',name\n";
		//console.exec(p);
		test1 t = new test1(console,p);
		t.start();
			
		
	}
	class codeRunner extends Thread
	{
		String source;
		InteractiveConsole console;
		public codeRunner(InteractiveConsole con,
				String s)
		{
			source=s;
			console=con;
			
		}
		public void run()
		{
			System.out.printf("Got to thread");
			//console.runsource(source);
			console.exec(source);
			System.out.printf("after thread");
		}
		
	}
	public void runNewCode(String source)
	{
		//console.resetbuffer();
	//	console.runsource(source);
		//console.exec(source);
		codeRunner runner=new codeRunner(console,source);
		runner.start();
		
	}
	
	//connects input and output to the console
	
	class consoleListener extends KeyAdapter
	{
		OutputStream out;
		public consoleListener(OutputStream output)
		{
			out = output;
		}

	
		@Override
		public void keyTyped(KeyEvent key)
		{
			try
			{
				out.write(key.getKeyChar());
			}
			catch(IOException e) {}
			
		}
		
	}//end of console listener class
	
	class updateTextThread extends Thread
	{
		PipedInputStream in;
		JTextPane pane;
		
		public updateTextThread(PipedInputStream paneIn,JTextPane jpane)
		{
			super();
			in=paneIn;
			pane=jpane;
		}
		
		@Override
		public void run()
		{
			int len = 128;
			byte byteArray[]=new byte[len];
			int retrieved=0;
			
			System.out.println("pre loop");
			try
			{
				retrieved = in.read(byteArray);
				while(retrieved>0)
				{
					System.out.println("once through the loop");
							
					Document doc=pane.getDocument();
					doc.insertString(doc.getLength(),new String(byteArray, 0, retrieved),null);
					pane.setCaretPosition(pane.getDocument().getLength());
					
					retrieved = in.read(byteArray);
				
				}
				
			}
			catch(Exception e)
			{
				
				
			}
			
			
			
		}//end of run
		 	
	
	}//end of updatetext thread
	
	
}
