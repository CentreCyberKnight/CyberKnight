package ckGameEngine;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Set;

import ckTrigger.CKTriggerListNode;

import static ckCommonUtils.CKPropertyStrings.*;

@Deprecated
public class CKCharacter
{
	//character data - true between quests
	String assetID;                        //picture
	String name;
	String backstory;
	CKBook abilities;
	Hashtable<String,CKArtifact >equippedList;
	CKTriggerListNode triggers;
	CKTeam team;
	
	
	private CKCharacter(String assetID,String name,String backstory, CKBook myAbilties)
	{
		this.assetID=assetID;
		this.name=name;
		this.backstory=backstory;
		this.abilities = CKAbilityFactory.characterAbilityFactory();
		addAbilities(myAbilties);
		equippedList = new Hashtable<String,CKArtifact>();
		team=CKTeam.getNullTeam();
		triggers = null;
	}
	
	private CKCharacter()
	{
		this("Hero");
	}
	
	private CKCharacter(String assetid)
	{
		this(assetid,"Hero");
	}
	
	private CKCharacter(String assetid,String name)
	{
		this(assetid,name,"",null);
	}
	
	public void addAbilities(CKBook b)
	{
		if(b != null) { CKBook.addToBook(abilities,b); }
	}
	
	
	public CKBook getAbilties()
	{
		//combine team, me, and CKArtifact abilities to get final ability
		//cache it eventually.
		ArrayList <CKBook> books = new ArrayList<CKBook>();
		books.add(team.getAbilities()); 
		books.add(abilities);
		//System.out.println("abilities"+abilities.treeString());
		for( CKArtifact a : equippedList.values())
		{
			if(a != null) 	{		books.add(a.getAbilities());  }
		}
		
		
		return CKBook.addBooks(books.iterator());
	}

	
	/**
	 * @return the triggers
	 */
	public CKTriggerListNode getTriggers()
	{
		return triggers;
	}

	/**
	 * @param triggers the triggers to set
	 */
	public void setTriggers(CKTriggerListNode triggers)
	{
		this.triggers = triggers;
	}

	public CKArtifact getArtifact(String pos)
	{
		if(equippedList.containsKey(pos))
		{
			return equippedList.get(pos);
		}
		return null;
	}
	
	 
	 /* has errors now, ignoring before mothballing
	
	public void equipArtifact(String pos,CKArtifact artifact)
	{
		CKCharacter oldowner = artifact.getEquippedBy();
		if(oldowner != null)  { oldowner.unequipArtifact(artifact); }
      
		if(equippedList.containsKey(pos))
		{
			this.unequipArtifact(equippedList.get(pos));
		}
		
		if(this.canEquip(artifact,pos))
		{
			artifact.setEquippedBy(this);
			equippedList.put(pos,artifact);
			AbilityChanges();
		}
		else
		{
			//need to put out an error message
		}
	}*/

	public void unequipArtifact(CKArtifact artifact)
	{
		artifact.setEquippedBy("");
		Set<Entry<String, CKArtifact>> s = equippedList.entrySet();
		for (Map.Entry<String,CKArtifact> e: s)
		{
			if( e.getValue() == artifact)
			{
				equippedList.remove(e.getKey());
				break;
			}
		}
		AbilityChanges();
	}
	
	public boolean canEquip(CKArtifact artifact)
	{
		CKBook ability = getAbilties();
		return ability.meetsRequirements(artifact.getRequirementsIter()) ;
	}
	
	public boolean canEquip(CKArtifact artifact,String pos)
	{
		CKBook ability = getAbilties();
		return (ability.hasPage(CH_EQUIP_SLOTS, pos) && 
				ability.meetsRequirements(artifact.getRequirementsIter())) ;
	}
	
	private void AbilityChanges()
	{
		for(CKArtifact art: equippedList.values())
		{
			if(! canEquip(art))
			{
				unequipArtifact(art); 
				return;                     //recursive call back to ability Changes, must quit here
			}
		}
		notifyListenersEquipped();
		notifyListenerStats(getAbilties());
		//TODO might want to notify listeentrs here
	}
	
	
	
	public String getAssetID()
	{
		return assetID;		
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
	}

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

	/**
	 * @return the backstory
	 */
	public String getBackstory()
	{
		return backstory;
	}

	/**
	 * @param backstory the backstory to set
	 */
	public void setBackstory(String backstory)
	{
		this.backstory = backstory;
	}

	/**
	 * @param assetID the assetID to set
	 */
	public void setAssetID(String assetID)
	{
		this.assetID = assetID;
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
	
	
	public void addListener(CKStatsChangeListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(CKStatsChangeListener l)
	{
		listeners.remove(l);
	}
	
	
}
