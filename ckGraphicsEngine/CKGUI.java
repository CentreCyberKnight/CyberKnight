package ckGraphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import ckCommonUtils.CKScriptTools;
import ckCommonUtils.LogListener;

public class CKGUI
{
	
	Vector<CKDialogMessage> oldMessages = new Vector<CKDialogMessage>();
	Vector<CKDialogMessage> newMessages = new Vector<CKDialogMessage>();
	CKDialogMessage mess;
	
	public CKGUI() 
	{
	
	}
	
	Vector<String> scriptResponses = null;
	Iterator<String > scriptIterator=null;
	int frameWait = 0;
	int frameCountDown=0;
	
	public void loadSatisfyScript(String script,int frames)
	{
		scriptResponses = CKScriptTools.pullOutActions(script);
		scriptIterator = scriptResponses.iterator();
		frameWait=frames;
		frameCountDown=frames;
		
	}
	
	Vector<LogListener> logListeners;
	
	public void addListener(LogListener l)
	{
		if(logListeners==null) { logListeners=new Vector<LogListener>();}
		logListeners.add(l);
		
	}
	
	public void fireLogEvent(String s)
	{
		String text = s+"---Dialog Choice---\n";
		if(logListeners==null) { return; }
		for(LogListener L:logListeners)
		{
			L.addText(text);
		}
	}
	
	
	public void calcState()
	{
		
		if(mess == null && ! newMessages.isEmpty())
		{
			mess = newMessages.firstElement();
			newMessages.remove(0);
			oldMessages.add(mess);
		}
		
		if(scriptResponses!=null)
		{ 
			if(mess!=null)
			{
				frameCountDown--;
				if(frameCountDown<0 && scriptIterator.hasNext())
				{
					String text = scriptIterator.next();
					if(mess.shouldCloseFromScript(text) )
					{
						mess=null;
						this.fireLogEvent(text);
						frameCountDown = frameWait;
					}
					else
					{
						System.err.println("\n\nASSERT FAILS Dialog "+text+" should have been correct");
						System.exit(1);
						//Does not work as this is not the main threadfail("Dialog "+text+" should have been correct");
					}
				}
			}
			
			
			
		}
		
	}
	
	
	protected void drawSingleMessage(Graphics g,int width,int height)
	{
		int margin = 10;
		int col=226;
		g.setColor(new Color(col,col,col,222));
		Rectangle rect = new Rectangle(margin, height-200,
				width-(2*margin), 200-margin);
		//g.fillRect(rect.x,rect.y,rect.width,rect.height);
		g.fill3DRect(rect.x, rect.y, rect.width, rect.height, true);
		
		//pass this as a parameter to avoid a race condition!
		mess.drawMessage(g,rect, null);		
	
	}
	
	protected void drawSingleMessage(GraphicsContext g,int width,int height)
	{
		int margin = 10;
		int leftMargin = 200;
		int col=226;
		g.setFill(javafx.scene.paint.Color.rgb(col,col,col,.75));
		Rectangle rect = new Rectangle(leftMargin, height-200,
				width-margin-leftMargin, 200-margin);
		//g.fillRect(rect.x,rect.y,rect.width,rect.height);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		
		//pass this as a parameter to avoid a race condition!
		mess.drawMessage(g,rect, null);		
	
	}
	
	
	
	public synchronized void  drawOffScreenBuffer(Graphics g,int width,int height)
	{
		
		/*  handle in calc, remove next time you go through here.
		if(mess == null && ! newMessages.isEmpty())
		{
			mess = newMessages.firstElement();
			newMessages.remove(0);
			oldMessages.add(mess);
		}
		*/
		if(mess != null)
		{
			drawSingleMessage(g,width,height);
		}
		else
		{
			//do nothing
		}
	}
	
	public synchronized void  drawOffScreenBuffer(GraphicsContext g,int width,int height)
	{
		
		/*  handle in calc, remove next time you go through here.
		if(mess == null && ! newMessages.isEmpty())
		{
			mess = newMessages.firstElement();
			newMessages.remove(0);
			oldMessages.add(mess);
		}
		*/
		if(mess != null)
		{
			drawSingleMessage(g,width,height);
		}
		else
		{
			//do nothing
		}
	}
	
	
	
	/**
	 * Returns true if the mouse event is handled at this layer in the engine 
	 * @param e - the mouse event to be looked at (mouse up action?)
	 * @return true if the event was handled, false if it should be handled by a lower layer.
	 */
	public synchronized boolean handleMouseEvent(MouseEvent e)
	{
		if(scriptResponses!=null) return false;  //don't allow user to bypass script
		if (mess== null) return false;
		
		String response = mess.pickedByMouse(e);
		if(response!=null)
		{
			mess=null;
			e.consume();
			this.fireLogEvent(response);
			return true;
		}
		return false;		
	}
	
	public synchronized void addDialogMessage(CKDialogMessage m)
	{
		newMessages.add(m);
	}
	
	
	
}
