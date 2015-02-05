package jconsole.streams;

import java.io.IOException;
import java.io.Reader;

import jconsole.JConsole;

/**
 * Data written into this is data from the console
 * 
 * @author Andrew
 */
public class ConsoleInputStream extends Reader
{
	@SuppressWarnings("unused")
	private JConsole		console;
	private StringBuilder	stream;

	/**
	 * @param console
	 */
	public ConsoleInputStream(JConsole console)
	{
		this.console = console;
		stream = new StringBuilder();
	}

	/**Adds a string to the stream and wakes any threads that are waiting for the data.
	 * @param text
	 */
	public synchronized void addText(String text)
	{
		//addData(text);
		stream.append(text);
		//System.out.printf("Notifying all threads\n");
		notifyAll();
	}

	/**Adds a string to the stream and wakes any threads that are waiting for the data.
	 * 
	 *
	 */
/*	private synchronized void addData(String cmd)
	{
		stream.append(cmd);
		//System.out.printf("Notifying all threads\n");
		notifyAll();
	}
*/
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
	public synchronized void close() throws IOException
	{
		console = null;
		stream = null;
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

	
/*	
	@Override
	public int read(char[] buf, int off, int len) throws IOException
	{
		int count = 0;
		boolean doneReading = false;
		for (int i = off; i < off + len && !doneReading; i++)
		{
			// determine if we have a character we can read
			// we need the lock for stream
			int length = 0;
			while (length == 0)
			{
				// sleep this thread until there is something to read
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					//MKB proper behavior is to throw IOException as it is waiting on IO
					throw(new IOException("ConsoleInputStream:Read interurrupted"));
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				synchronized (stream)
				{
					length = stream.length();
				}
			}
			synchronized (stream)
			{
				// get the character
				buf[i] = stream.charAt(0);
				// delete it from the buffer
				stream.deleteCharAt(0);
				count++;
				if (buf[i] == '\n')
				{
					doneReading = true;
				}
			}
		}
		return count;
	}
*/
}
