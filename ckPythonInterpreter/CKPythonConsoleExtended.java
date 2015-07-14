package ckPythonInterpreter;





import java.io.IOException;
import java.io.Reader;
//import java.lang.Math;






import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jconsole.JConsole;
import ckCommonUtils.CKThreadCompletedListener;
import ckGameEngine.CKGameObjectsFacade;
import ckPythonInterpreter.CKUniqueEditor;
import ckPythonInterpreter.CKPythonDebuggerInterface;
import netscape.javascript.JSObject;

import org.python.core.PyException;
import org.python.util.InteractiveConsole;
import org.python.core.PySystemState;
import org.python.core.ThreadState;






public class CKPythonConsoleExtended extends JConsole 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4494183487499399490L;
	private String prompt=">>>";
	private int timeout_milli=1000;
	private InteractiveConsole console;
	private codeRunner execThread;
	private SwitchedIO inIO;
	private Thread inputThread;
	
	
	public CKPythonConsoleExtended()
	{
		inIO = new SwitchedIO(super.getInputStream());
		inputThread = new Thread(inIO);
		inputThread.setDaemon(true);
		inputThread.start();
		
		resetConsole();
		execThread=null;
		appendText(prompt);
		//EngineThread t = new EngineThread(console);
		//t.start();
	}
	
	public synchronized void waitForCompletion()
	{
			try { 	wait(); 	}
			catch (InterruptedException e) {	}
	}
	
	public synchronized void codeCompletes()
	{
		notify();
	}
	
	
	@Override
	public Reader getInputStream()
	{
		return inIO;		
	}
	
	
	private void resetConsole()
	{
		stopRunningCode();  //will stop thread if running
		
		
		console = new InteractiveConsole();
		
		console.setIn(this.getInputStream());
		console.setOut(getOutputStream());
		console.setErr(getErrStream());
	}

	
	public void appendText(String s)
	{
		StringBuilder text = new StringBuilder(getText());
		text.append(s);
		setText(text.toString());
	}
	
	//no longer used-eventually remove
	class EngineThread extends Thread
	{
		InteractiveConsole console;
		
		public EngineThread(InteractiveConsole co)
		{
//			cmd = c;
			console=co;
		}
		
		public void run()
		{
			console.interact();
		}
	}

	/**
	 * IO reader that uses another IO readerstream for input.
	 * input while there is a thread running is input.
	 * input while a thread is not running is an action that should be executed.
	 * 
	 * 
	 */
	class SwitchedIO extends Reader implements Runnable
	{
		private Reader _in;
		private StringBuilder stream;  //used to store btes while waiting.
		
		public SwitchedIO(Reader in)
		{
			_in=in;
			stream=new StringBuilder();
		}
		
		@Override
		public void close() throws IOException
		{
			_in.close();
			stream=null;
		}

		@Override
		public void reset() throws IOException
		{
			privateReset();
		}
		
		private synchronized void privateReset()
		{
			stream.setLength(0);			
		}
		
		@Override
		/**
		 * constantly reads from the _in member to keep string buffer full or make commands
		 * 
		 * 
		 */
		public void run()
		{
			int str_len=100;
			char str_buf[]=new char[str_len];
			int read_len=0;
			StringBuilder mystream=new StringBuilder();

			while(true)
			{
				//get data from member _in
				try
				{
					read_len =_in.read(str_buf,0,str_len);
				} catch (IOException e)
				{
					// i think this will not get called by the interrupt in stop thread
					e.printStackTrace();
					//it will keep running...
				}
				//if it has been interrupted, time to stop
				if(read_len>0)
				{
					mystream.append(str_buf,0,read_len);
				}
				//check for newline
				//System.out.printf("present buffer %s:",mystream.toString());						
				int index = mystream.indexOf("\n");
				if(index!=-1)
				{//get the cmd
					String cmd = mystream.substring(0,index+1); //include the newline
					mystream.delete(0,index+1); //include the newline
					
					if(isRunning())
					{//this is input for my boss, add to stream for reads
						//System.out.printf("%s:passing string\n",cmd);
						addData(cmd);
					}	
					else
					{//cmd for input to program
						//System.out.printf("%s:executing string\n",cmd);
						runNewCode(cmd);
					}
				}
			}//end while
		}//end run fcn

		/**Adds a string to the stream and wakes any threads that are waiting for the data.
		 * 
		 *
		 */
		private synchronized void addData(String cmd)
		{
			stream.append(cmd);
			//System.out.printf("Notifying all threads\n");
			notifyAll();
		}

		/**Will return immediately if there is data to read.
		 * Otherwise will wait for the new data to appear.
		 * 
		 * 
		 */
		private synchronized int waitForNewData() throws IOException
		{
			int length = stream.length();
			if(length == 0)
			{
				try
				{
					//System.out.printf("waiting.../n");
					wait();
				}		
				catch (InterruptedException e)
				{
					//System.out.printf("Interrupted.../n");
					length = stream.length();
					if(length ==0)
					{ //not a notification for new data
						throw(new IOException("SwitchedIO:Read interurrupted"));
					}
				}
			}
			//exits function if there is new data
			return length;
		}

		private synchronized char getChar()
		{
			char ret= stream.charAt(0);
			// delete it from the buffer
			stream.deleteCharAt(0);
			return ret;
		}
		
				
		@Override
		public int read(char[] cbuf, int off, int len) throws IOException
		{
			System.out.printf("inside of my read fcn");
			
			int count = 0;
			boolean doneReading = false;
			 
			for (int i = off; i < off + len && !doneReading; i++)
			{
				// determine if we have a character we can read
				waitForNewData();
				// get the character and remove from buffer
				cbuf[i] = getChar();
				count++;
				if (cbuf[i] == '\n')
				{
					doneReading = true;
				}
			}
			return count;
		}				
	}//end class
		


	
	public class codeRunner extends Thread
	{
		protected String source;
		protected InteractiveConsole console;
		protected CKThreadCompletedListener listener;
		
		public codeRunner(InteractiveConsole con,
				String s)
		{
			this(con,s,null);
		}
		
		
		
		public codeRunner(InteractiveConsole con, String s,
				CKThreadCompletedListener listener)
		{
			source=s;
			console=con;
			this.listener=listener;
		}

		
		


		/**
		 * closes the present execution of the console and allows another thread to run 
		 * 
		 */
		public void closeRunner()
		{
			console.interrupt(new ThreadState(this, new PySystemState() ));
			//not sure if this will do it.
		}
		
		boolean error = false;
		public void run()
		{

			//System.out.printf("Got to thread");
			//console.runsource(source);
			try
			{
				//trying to run CyberSnap
				WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
				JSObject jsobj = (JSObject) webEngine.executeScript("window");
				jsobj.setMember("completionListener", this);
				System.out.println("in thread");
				webEngine.executeScript("setTimeout(ide.fireTEST(), 0)");
				//console.exec(source);
			}
			catch(Exception e)
			{ //should be interrupt by another thread
				System.out.println("Not a PYException?");
				closeRunner();
				error=true;
			}

			waitForSnap();			
		}
		
		volatile boolean done = false;
		public synchronized void waitForSnap()
		{
			while(!done) //need this in case snap completes before we can wait for it.
			{	try {wait();}
				catch (InterruptedException e) {}
			}
			//now wrap it up.
			System.out.printf("after thread");
			codeCompletes();
			if(listener!=null)
			{
				listener.threadFinishes(error);
			}		
		}
		
		public synchronized void snapCompletes()
		{
			done=true;
			notify();
		}
	}
	
	
	public static String wrapCode(String s)
	{
		String header = "from ckPythonInterpreter.CKPlayerObjectsFacade import * \ntry:\n ";
		String footer="\nexcept Exception as e:\n\tdisplayPythonException(e)";
		String lines[] = s.split("\\r?\\n");
		for(String l: lines)
		{
			header+="\t"+l;
		}
		return header+footer;
	}
	
	class exceptionRunner extends codeRunner
	{
		
		
		
		public exceptionRunner(InteractiveConsole con, String s,
				CKThreadCompletedListener listener)
		{
			super(con, wrapCode(s), listener);
			
		}
		
	}
	
	
	class debugRunner extends codeRunner
	{

		public debugRunner(InteractiveConsole con, String s)
		{
			super(con, s);
		}
		
		
		public void run()
		{
			//System.out.printf("Got to thread");
			//console.runsource(source);
			try
			{
				//need to create the stuff here...
				
				CKPythonDebuggerInterface debugger=CKUniqueEditor.getDebuggerInstance();
				debugger.storeProgram(source);
				CKUniqueEditor.storeInitializedDebugger(debugger);
				String code=
					"from ckPythonInterpreter.CKUniqueEditor import *\n"+
					"debug= getInitializedDebugger()\n"+
					"debug.runProgram()\n";
				
				
				
				
				console.exec(code);
			}
			catch(PyException  e )
			{//problem with the python code
				//System.out.println("I throw a PYException?");
				e.printStackTrace();
			}
			catch(Exception e)
			{ //should be interrupt by another thread
				closeRunner();				
			}
			appendText(prompt);
			//System.out.printf("after thread");
		}
		
		
		
		
	}
	
	public boolean isRunning()
	{
		return (execThread !=null &&
				execThread.getState() != Thread.State.TERMINATED);
	}
		
	public void runNewCode(String source,
			CKThreadCompletedListener listener)
	{
		
		requestFocusInWindow(); 
		console.resetbuffer();
		execThread=new codeRunner(console,source,listener);
		execThread.setDaemon(true);
		
		//execThread.start();
		Platform.runLater(execThread);
	}

	
	public void runNewCode(String source)
	{
		
		//if(execThread!=null)
		//{
		//stopRunningCode();
		//}
		requestFocusInWindow(); 
		console.resetbuffer();
		//need to add a newline to clear the console.
		//addText("\n");
		//((ConsoleInputStream) getInputStream()).addText("\n");
		execThread=new codeRunner(console,source);
		execThread.setDaemon(true);
		execThread.start();
		
	}
	
	public void runDebuggingCode(String source)
	{
		requestFocusInWindow(); 
		console.resetbuffer();
		
		execThread=new debugRunner(console,source);
		execThread.setDaemon(true);
		execThread.start();
		
	}
	
	
	/**Tries to stop currently running process.  This should work if the process is blocked on IO
	 *  however, it might not work if there is a computationally intensive thread running.
	 */
	public void stopRunningCode()
	{
		System.out.print("Entering stop running code");
		if(isRunning())
			//execThread !=null &&
			//	execThread.getState() != Thread.State.TERMINATED)
		{			
			//wait to see if it will complete on its own.
			try 
			{ 
				execThread.join(timeout_milli);

			
				//need to get more aggressive
				if(isRunning())//execThread.getState() != Thread.State.TERMINATED)
				{//this will not work if there is computationally intensive loop
					execThread.interrupt(); //this crashes the python...but will allow us to work
					Thread.sleep(timeout_milli); //let the python crash, gracefully
					//execThread.join(); //this will block input as well
				}
			}
			//do nothing, I want to wake up anyway
			catch (Exception e)  {}
		}
		//should be safe now...
		execThread=null;
		System.out.println("Leaving stop running code");
	}

	
		
}

	