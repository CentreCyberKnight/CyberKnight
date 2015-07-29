package ckGameEngine;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckCommonUtils.CKXMLAsset;
import ckCommonUtils.LogListener;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckGraphicsEngine.BadInstanceIDError;
import ckGameEngine.CKGameObjectsFacade;

public abstract  class ActorController implements CKXMLAsset<ActorController>
{
	final public static String NO_CONTROL = "No Control"; 
	final public static String TEXT_CONTROL = "Text Control"; 
	final public static String ARTIFACT_CONTROL = "Artifact Control";
	final public static String BOTH_CONTROL = "Both Control";
	final public static String SNAP_CONTROL = "Snap! Control";
	
	protected String permissions = NO_CONTROL;
	private CKGridActor actor;
	private Vector<LogListener> logListeners;
	private Vector<LogListener> aimLogListeners;
	private Vector<CKTurnListener> turnListeners;
	private String AID="";
	private String name = "";
	
	
	

	public ActorController() { this(null); }
	
	public ActorController(CKGridActor pc) { actor=pc; }
	
	public CKGridActor getActor() { return actor;}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return name;
	}


	/**
	 * @param actor the actor to set
	 */
	public void setActor(CKGridActor actor)
	{
		this.actor = actor;
	}
	
	public void addTurnListener(CKTurnListener l)
	{
		if(turnListeners==null) {turnListeners=new Vector<CKTurnListener>();}
		turnListeners.add(l);
	}
	
	public void removeTurnListener(CKTurnListener l)
	{
		if(turnListeners==null || l==null) {return;}

		turnListeners.remove(l);
		
	}
	
	public void fireTurnEvent(CKSpellCast cast)
	{
		if(turnListeners==null) { return; }
		
		Iterator<CKTurnListener> iter = turnListeners.iterator();
		while(iter.hasNext())
		{
			if(iter.next().turnEvent(cast))
			{
				iter.remove();
			}
		}
		
	}
	
	
	public void addListener(LogListener l)
	{
		if(logListeners==null) { logListeners=new Vector<LogListener>();}
		logListeners.add(l);
		
	}
	
	public void fireLogEvent(String s)
	{
		if(logListeners==null) { return; }
		for(LogListener L:logListeners)
		{
			L.addText(s);
		}
	}

	Iterator<CKPosition> aimIterator = null;
	public void loadAimText(String s)
	{

		final String xml = "<?xml";
		Vector<CKPosition> vec = new Vector<CKPosition>();
		int starting = 0;
		int next = s.indexOf(xml,1);
		if(next==-1) { next = s.length();}

		try
		{
			while (starting<s.length())
			{
				ByteArrayInputStream input = new ByteArrayInputStream(s.substring(starting, next).getBytes());
				System.out.println(s.substring(starting,next)+":"+starting+"-"+next);
				XMLDecoder e = new XMLDecoder(input);
				CKPosition pos = (CKPosition) e.readObject();
				vec.add(pos);
				e.close();
				System.out.println("Aim Pos:"+pos+":"+starting+"-"+next);
				starting=next;
				next = s.indexOf(xml, next+1);
				if(next==-1) { next = s.length();}
			}
		}//only way out...
		catch (ArrayIndexOutOfBoundsException bound) {}
		finally
		{
			aimIterator = vec.iterator();	
		}
		
	}
	
	/**
	 * 
	 * @return the next aim stored in a script.  null if no script exists
	 */
	public CKPosition useStoredAim()
	{
		if(aimIterator==null || !aimIterator.hasNext()) { return null; }
		
		return aimIterator.next();		
	}
	
	public void addAimListener(LogListener l)
	{
		if(aimLogListeners==null) { aimLogListeners=new Vector<LogListener>();}
		aimLogListeners.add(l);
		
	}
	
	public void fireAimLogEvent(CKPosition pos)
	{
		if(aimLogListeners==null) { return; }

		//have to actually work now...
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(b);
		e.writeObject(pos);
		e.close();		
		String s = b.toString();
		
		for(LogListener L:aimLogListeners)
		{
			L.addText(s);
		}
	}

	/**
	 * @return the permissions
	 */
	public String getPermissions()
	{
		return permissions;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(String permissions)
	{
		this.permissions = permissions;
	}

	public void focusCharacter()
	{
		Quest w = CKGameObjectsFacade.getQuest();	
		w.startTransaction();
		try
		{
			/*CKGameObjectsFacade.getEngine().cameraFollowInstance(w.gettId(),
			getCharacter().getInstanceID(), 
			w.getStartTime(), 30);
			*/
			
			
			CKGameObjectsFacade.getEngine().cameraPointAt(w.gettId(),
					getActor().getPos(),w.getStartTime(), 30);
			
			
		} 
		catch (BadInstanceIDError e) { e.printStackTrace(); } 
		w.endTransaction();
	}
	
	/**
	 * Instructs the Pc object how to take a turn
	 * 
	 */
	public final void takeTurn()
	{
		
		System.out.println(actor.getName()+" takes turn with"+ this.toString());
		//CKGameObjects
		focusCharacter();
		
	
		CKSpellCast precast = new CKSpellCast(actor,actor,CKPropertyStrings.CH_WORLD,
												CKPropertyStrings.P_START_TURN,0,"");
		precast.castSpell();
		this.fireTurnEvent(precast);

		
		takeMyTurn();	
	
		
		CKSpellCast postcast = new CKSpellCast(actor,actor,CKPropertyStrings.CH_WORLD,
												CKPropertyStrings.P_END_TURN,0,"");
		postcast.castSpell();
		this.fireTurnEvent(postcast);
		
		getActor().setTurnsTaken(getActor().getTurnsTaken()+1);
		
		

	}
	
	/**
	 * subclasses must override this method to take their turn, called from takeTurn(); 
	 */
	protected abstract void takeMyTurn();
	
	/**
	 * Casts spell to trigger that the item has been loaded.
	 */
	public void onLoad()
	{
		CKSpellCast cast = new CKSpellCast(actor,actor,CKPropertyStrings.CH_WORLD,CKPropertyStrings.P_ON_LOAD,0,"");
		cast.castSpell();
	}
	
	public void onRemove()
	{
		CKSpellCast cast = new CKSpellCast(actor,actor,CKPropertyStrings.CH_WORLD,CKPropertyStrings.P_ON_DEATH,0,"");
		cast.castSpell();	
	}
	
	/**
	 * Creates an initial Event for the AbstractGridItem in this quest.  As events occur they will
	 * create further events.
	 * @param start the starting time that the actor enters the game.
	 * 
	 * @return the Initial event, or null if there is no event.
	 */
	public Event getInitialTurnEvent(double start) { return null; }

	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.writeObject(this);
		e.close();		
		
	}

	@Override
	public String getAID()
	{
		return AID;
	}

	@Override
	public void setAID(String a)
	{
		AID=a;
	}

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	@Override
	public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
	{
		return new JLabel(this.toString());
	}

	@Override
	public <B extends CKXMLAsset<B>> CKXMLAssetPropertiesEditor<B> getXMLPropertiesEditor()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
}