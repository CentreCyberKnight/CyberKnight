package ckGameEngine;


///MAKE COPY IS CURRENTLY MAKING 

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import ckCommonUtils.CKPosition;
import ckCommonUtils.CKPropertyStrings;
import ckDatabase.CKActorControllerFactory;
import ckEditor.CKGridActorPropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.ActorNode;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.UnknownAnimationError;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckGraphicsEngine.assets.CKNullAsset;
import ckGraphicsEngine.assets.CKSpriteAsset;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckPythonInterpreter.CKPlayerObjectsFacade;
import ckTrigger.CKSharedTriggerList;

public class CKGridActor extends CKGridItem
{

	
	

	

	private Direction direction;	
	private CKBook coreAbilities = new CKBook();
	private CKBook questAbilities = new CKBook();
	private CKTeam team = CKTeam.getNullTeam();
	
	
	ActorController turnController;// = new ActorArtifactController(this,ActorController.BOTH_CONTROL);
	String controllerID;// = "BOTH";
	
	private int instanceID=-100;
	private int cyberPoints=100;
	private int turnsTaken = 0;
	
	
	
	public CKGridActor()
	{
		this("null",Direction.NORTHEAST);
	}
	


	public CKGridActor(String assetID,Direction direction)
	{
		setAssetID(assetID); 
		this.direction = direction;
		setControllerID("BOTH");		
	}
	
	
	private int cpConsumedLastRound = 0;
	public void setCPConsumedLastRound(int consumed) 
	{
		cpConsumedLastRound = consumed;
	}
	
	public double calcTimeToNextTurn(double presentTime)
	{
		int speed = Math.max(1,getAbilities().getChapter(CKPropertyStrings.SPEED).getValue());
		
		double offset = (CKPropertyStrings.SPEED_NUMERATOR+cpConsumedLastRound)
				/speed;
		
		return presentTime+offset;	
	}
	
	/**
	 * @return the controllerID
	 */
	public String getControllerID()
	{
		return controllerID;
	}



	/**
	 * @param controllerID the controllerID to set
	 */
	public void setControllerID(String controllerID)
	{
		this.controllerID = controllerID;
		this.turnController = CKActorControllerFactory.getInstance().getAsset(controllerID);
		turnController.setActor(this);
		
	}



	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#onAdd()
	 */
	@Override
	public void onAdd()
	{
		if(CKGameObjectsFacade.unitTest) {return; } //not in game, running tests
		CK2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
		if(engine==null) {return; }
		try
		{
			if(this.instanceID<0)
			{
				engine.loadAsset(0,getAssetID());
				this.instanceID = engine.createInstance(0, getAssetID(),getPos(),0,
						CKGraphicsLayer.SPRITE_LAYER);
			}
			else
			{
				engine.moveTo(0, instanceID, 0, getPos(),0);
			}
			if(getAsset() instanceof CKSpriteAsset)
			{
				engine.setAnimation(0, instanceID, this.direction.toString(),0);
			}
		} catch (LoadAssetError e)
		{
			e.printStackTrace();
		} catch (BadInstanceIDError e)
		{
		
			e.printStackTrace();
		} catch (UnknownAnimationError e)
		{
		
			e.printStackTrace();
		}
		
	}


	
	/* (non-Javadoc)
	 * @see ckGameEngine.CKAbstractGridItem#onRemove()
	 */
	@Override
	public void onRemove()
	{
		//just to be careful
		prev=null;
		next=null;
		
		if(CKGameObjectsFacade.unitTest) {return; } //not in game, running tests
		CK2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
		if(engine==null) {return; }
		try
		{
			if(this.instanceID>0)
			{
				
				engine.destroy(0, this.instanceID, 0);
				this.instanceID=-100;
			}
		} catch (BadInstanceIDError e)
		{
			e.printStackTrace();
		}
	}



	/**
	 * @return the instanceID
	 */
	public int getInstanceID()
	{
		return instanceID;
	}



	/**
	 * @param instanceID the instanceID to set
	 */
	public void setInstanceID(int instanceID)
	{
		this.instanceID = instanceID;
	}


	
	public int  drawMove(CKPosition start,CKPosition end,int time) 
	{

		CK2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
		int endTime = time;
		try
		{
			endTime = engine.move(0,instanceID,time,start,end,10);
		} catch (BadInstanceIDError e)
		{
			e.printStackTrace();
		}
		if(next != null)
		{
			((CKGridActor) next).drawMove(start, end, time);
		}
		return endTime;
	}
	
	
	@Override
	public CKGraphicsAsset getTerrainAsset()
	{
		return CKNullAsset.getNullAsset();
	}
	
	
	/**
	 * adds a page to the actors abilities
	 */
	
	public void addPage(String chapter, int val, String page)
	{
		questAbilities.addChapter(new CKChapter(chapter,val,page));
	}

	public void addChapter(String chapter, int val)
	{
		questAbilities.addChapter(new CKChapter(chapter,val));
	}

	public boolean hasChapter(String chapter)
	{
		return getAbilities().hasChapter(chapter);
	}



	public ActorController getTurnController()
	{
		return turnController;
	}


/*
	public void setTurnController(ActorController cont)
	{
		turnController = cont;
	}
*/
	public void setScriptController(String text)
	{
		//turnControllerId = 5;
		//assert(ActorNode.controllerTypes[turnControllerId].compareTo("Debug")==0);
		turnController = new ActorScriptController(this,text);		
	}


	/**
	 * @return the direction
	 */
	public Direction getDirection()
	{
		return direction;
	}



	/**
	 * @param direction the direction to set
	 */
	
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}
	/*
		try{
			engine.setAnimation(tId, instanceId, this.direction.toString(), quest.getStartTime());
		} catch (BadInstanceIDError e) {
			e.printStackTrace();
		} catch (UnknownAnimationError e) {
			e.printStackTrace();
		}
		quest.setStartTime(quest.getStartTime()+30);
	}
*/	
	public void turnLeft()
	{
		setDirection(this.direction.getLeftNeightbor());
	}
	
	public void turnRight()
	{
		setDirection(this.direction.getRightNeightbor());
	}

	/**
	 * @return the team
	 */
	public CKTeam getTeam()
	{
		return team;
	}



	/**
	 * @param team the team to set
	 */
	public void setTeam(CKTeam team)
	{
		this.team = team;
		team.addCharacter(this);
	}



	/**
	 * @return the cyberPoints
	 */
	public int getCyberPoints()
	{
		return cyberPoints;
	}



	/**
	 * @param cyberPoints the cyberPoints to set
	 */
	public void setCyberPoints(int cyberPoints)
	{
		this.cyberPoints = cyberPoints;
		CKBook  book = getAbilities();
		int m = book.getChapter(CKPropertyStrings.MAX_CP).getValue();
		if(m < this.cyberPoints)
		{
			this.cyberPoints=m;
		}
		this.notifyListenerCP(cyberPoints);	
	}



	/**
	 * @return the abilities
	 */
	public CKBook getAbilities()
	{
		ArrayList <CKBook> books = new ArrayList<CKBook>();
		books.add(getTeam().getAbilities(name)); 
		books.add(coreAbilities);
		books.add(questAbilities);
		
		return CKBook.addBooks(books.iterator());
	}



	

	public void addAbilities(CKBook b)
	{
		if(b != null) { CKBook.addToBook(questAbilities,b); }
	}
	
	
	

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getXMLPropertiesEditor()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKGridItem> getXMLPropertiesEditor()
	{
		return new CKGridActorPropertiesEditor(this);
	}



	public CKPosition getNextPosition()
	{
		
		return getDirection().getAhead(getPos(), CKGameObjectsFacade.getQuest().getGrid());
	}



	public boolean isDead()
	{
		return cyberPoints<0;
	}

	
	
	public int getTurnNumber()
	{
		return turnsTaken;
	}



	/**
	 * @return the coreAbilities
	 */
	public CKBook getCoreAbilities()
	{
		return coreAbilities;
	}



	/**
	 * @param coreAbilities the coreAbilities to set
	 */
	public void setCoreAbilities(CKBook coreAbilities)
	{
		this.coreAbilities = coreAbilities;
	}



	/**
	 * @return the questAbilities
	 */
	public CKBook getQuestAbilities()
	{
		return questAbilities;
	}



	/**
	 * @param questAbilities the questAbilities to set
	 */
	public void setQuestAbilities(CKBook questAbilities)
	{
		this.questAbilities = questAbilities;
	}



	/**
	 * @return the turnsTaken
	 */
	public int getTurnsTaken()
	{
		return turnsTaken;
	}



	/**
	 * @param turnsTaken the turnsTaken to set
	 */
	public void setTurnsTaken(int turnsTaken)
	{
		this.turnsTaken = turnsTaken;
	}

	
	
	/*******artifact methods*****/
	
	public CKArtifact getArtifact(String pos)
	{
		Vector<CKArtifact> arts= team.getArtifacts(name);
		if(arts==null) {return null; }
		for(CKArtifact art:arts)
		{	
			if(art.equippedAt.compareTo(pos)==0)
			{
				return art;
			}
		}
		return null;
	}
	
	
	public void equipArtifact(CKArtifact art)
	{
		
		Iterator<CKPage> pages=getAbilities().getChapter(CKPropertyStrings.CH_EQUIP_SLOTS).getPages();
		while(pages.hasNext())
		{
			String name = pages.next().getName();
			Iterator<CKBook> iter = art.getRequirementsIter();
			while(iter.hasNext())
			{
				CKPage p = iter.next().getChapter(CKPropertyStrings.CH_EQUIP_SLOTS)
						.getPage(name);
				if(p!=null)
				{
					equipArtifact(name,art);
					return;
				}
			}			
		}		
		
		
	}
	
	
	
	
	
	public void equipArtifact(String pos,CKArtifact artifact)
	{
		/* handled by team
		 CKGridActor oldowner = artifact.getEquippedBy();
		if(oldowner != null)  { oldowner.unequipArtifact(artifact); }
      */
		CKArtifact old = getArtifact(pos);
		if(old!=null)
		{
			this.unequipArtifact(old);
		}
		
		if(this.canEquip(artifact,pos))
		{
			team.equipArtifact(artifact, name,pos);
			AbilityChanges();
		}
		else
		{
			//need to put out an error message
		}
	}

	public void unequipArtifact(CKArtifact artifact)
	{
		team.unequipArtifact(artifact);

		/*artifact.setEquippedBy(null);
		Set<Entry<String, CKArtifact>> s = equippedList.entrySet();
		for (Map.Entry<String,CKArtifact> e: s)
		{
			if( e.getValue() == artifact)
			{
				equippedList.remove(e.getKey());
				break;
			}
		}*/
		AbilityChanges();
	}
	
	public boolean canEquip(CKArtifact artifact)
	{
		CKBook ability = getAbilities();
		return ability.meetsRequirements(artifact.getRequirementsIter()) ;
	}
	
	public boolean canEquip(CKArtifact artifact,String pos)
	{
		CKBook ability = getAbilities();
		return (ability.hasPage(CH_EQUIP_SLOTS, pos) && 
				ability.meetsRequirements(artifact.getRequirementsIter())) ;
	}
	
	public void AbilityChanges()
	{
		for(CKArtifact art: team.getArtifacts(name))
		{
			if(! canEquip(art))
			{
				unequipArtifact(art); 
				return;                     //recursive call back to ability Changes, must quit here
			}
		}
		notifyListenersEquipped();
		notifyListenerStats(getAbilities());
		//TODO might want to notify listeentrs here
	}
	
	
	


	
	Vector<CKStatsChangeListener> listeners=new Vector<CKStatsChangeListener>();
	
	private void notifyListenersEquipped()
	{
		for (CKStatsChangeListener l: listeners)
		{
			l.equippedChanged();
		}
	}
	
	private void notifyListenerStats(CKBook b)
	{
		for (CKStatsChangeListener l: listeners)
		{
			l.statsChanged(b);
		}
	}
	
	private void notifyListenerCP(int cp)
	{
		for (CKStatsChangeListener l: listeners)
		{
			l.cpChanged(cp);
		}
	}
	
	
	public void addListener(CKStatsChangeListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(CKStatsChangeListener l)
	{
		listeners.remove(l);
	}
	@Override 
	protected CKAbstractGridItem makeCopy(CKAbstractGridItem item)
	{
		super.makeCopy(item);
		CKGridActor I = (CKGridActor) item;
		//I.setInstanceID(-1);
	//	I.setControllerID(controllerID);
		I.setCoreAbilities((CKBook) coreAbilities.clone());
		I.setCPConsumedLastRound(cpConsumedLastRound);
		I.setCyberPoints(cyberPoints);
		I.setDirection(direction);
		I.setQuestAbilities((CKBook)questAbilities.clone());
		//I.setScriptController(null); Simon: Set Script Controller requires a string, but the TurnController turns it into a TurnController, which the method can't take. What should I do?
		I.setTeam(team);
		I.setTurnsTaken(turnsTaken);
		
		return I;
		
	}
	@Override
	public CKAbstractGridItem makeCopy()
	{
		return makeCopy(new CKGridActor());		
	}
	
	
	


	

	
}
