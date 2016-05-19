package ckGameEngine;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;

import ckCommonUtils.CKPropertyStrings;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKTeamFactory;
import ckEditor.CKTeamPropertiesEditor;
import ckEditor.CKXMLAssetPropertiesEditor;

public class CKTeam implements CKXMLAsset<CKTeam>
{

	
	String AID="";
	String name="";
	String functions=""; //FIXME right now just store it as a big string.
	Vector<CKGridActor> characters = new Vector<CKGridActor>();
	Vector<CKArtifact> artifacts = new Vector<CKArtifact>();
	CKBook story = new CKBook();
	CKBook abilities = new CKBook();
	HashMap<String,Vector<CKArtifact>> equipMap=new HashMap<String,Vector<CKArtifact>>();
	
	
	/*public CKTeam(String n,String f,Vector<CKGridActor> c,
			Vector<CKArtifact> a,CKBook story,CKBook ab)
	{
		this.name = n;
		this.functions=f;
		this.characters=new Vector<CKGridActor>();
		if(c != null)
		{
			for (CKGridActor ch:c)
			{
				this.addCharacter(ch);
			}
		}
		this.artifacts=a;
		this.story = story;
		this.abilities = ab;
		
		
	}*/
	
	private static CKTeam nullTeam;
	public static CKTeam getNullTeam()
	{
		if(nullTeam == null) { nullTeam = new CKTeam("Team Null"); }
		return nullTeam;
	}
	
	public CKTeam()
	{
		this.name="Unnamed";
	}
	
	public CKTeam(String name)
	{
		this.name = name;
	}
	
	
	
	public void  addToAbilties(CKBook b)
	{
		this.abilities = CKBook.addBooks(this.abilities, b);
	}
	
	public void addCharacter(CKGridActor pc)
	{
		characters.add(pc);
		//pc.setTeam(this);
		
	}
	
	public CKGridActor[] getCharacters()
	{
		return characters.toArray(new CKGridActor[0]);
	}
	
	
	public CKGridActor getCharacter(String name)
	{
		for(CKGridActor a:characters)
		{
			if(a.getName().compareTo(name)==0)
			{
				return a;
			}
		}
		return null;
	}
	
	
	/**
	 * @return the equipMap
	 */
	public HashMap<String, Vector<CKArtifact>> getEquipMap()
	{
		return equipMap;
	}

	/**
	 * @param equipMap the equipMap to set
	 */
	public void setEquipMap(HashMap<String, Vector<CKArtifact>> equipMap)
	{
		this.equipMap = equipMap;
	}

	public CKBook getAbilities()
	{
		//System.out.println(abilities.treeString());
		return abilities;
	}
	
	
	public CKBook getAbilities(String name)
	{
		
		Vector<CKArtifact>equippedList = equipMap.get(name);
		if(equippedList==null) { return getAbilities(); }
		ArrayList <CKBook> books = new ArrayList<CKBook>();
		
		books.add(abilities);
		for( CKArtifact a : equippedList)
		{
			if(a != null) 	{		books.add(a.getAbilities());  }
		}
		
		return CKBook.addBooks(books.iterator());
		
	}
	
	public Vector<CKArtifact> getArtifacts(String name)
	{
		Vector<CKArtifact> vec = equipMap.get(name);
		if(vec==null) { return new Vector<CKArtifact>(); }

		return vec;
	}
	
	public void equipArtifact(CKArtifact art,String owner, String pos)
	{
		equipArtifact(art,owner,pos,true);		
	}
	
	public void equipArtifact(CKArtifact art,String owner, String pos,boolean notify)
	{
		String actor = art.getEquippedBy();
		if(actor.length()>0)
		{
			unequipArtifact(art,notify);
		}
		art.setEquippedBy(owner);
		art.setEquippedAt(pos);
		
		Vector<CKArtifact> vec = equipMap.get(owner);
		if(vec==null) 
		{
			vec=new Vector<CKArtifact>();
			equipMap.put(owner, vec);
		}
		vec.add(art);
		
	}
	
	/**
	 * This will attempt to find the right position to equip the artifact
	 * If there are no over laps (more than one possible place) this should be ok.
	 * @param art
	 * @param owner
	 */
	public String findArtifactPosition(CKArtifact art,String owner)
	{
		CKGridActor actor = getCharacter(owner);
		
		Iterator<CKPage> pages=actor.getAbilities().getChapter(CKPropertyStrings.CH_EQUIP_SLOTS).getPages();
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
					return name;
				}
			}			
		}		
		
		return "";
	}
	
	
	
	

	public void unequipArtifact(CKArtifact art)
	{
		unequipArtifact(art,true);
	}
	public void unequipArtifact(CKArtifact art,boolean notify)
	{
		String owner = art.getEquippedBy();
		if(owner !="")
		{
				
			Vector<CKArtifact> vec = equipMap.get(owner);
		
			if(vec != null)
			{
				vec.remove(art);
			}
			art.setEquippedBy("");
			if(notify)
			{
				this.getCharacter(owner).AbilityChanges();
			}
			
		}
		
		//should notify actor if possible
		
	}

	public Iterator<CKArtifact> equipIter(CKGridActor character, String pos)
	{
		ArrayList<CKArtifact> good = new ArrayList<CKArtifact>();
		
		for(CKArtifact a:artifacts)
		{
			if(a.usesPosition(pos) && character.canEquip(a))
			{
				good.add(a);
			}
		}
		return good.iterator();		
	}

	public void addArtifact(CKArtifact combatBoots)
	{
		artifacts.add(combatBoots);
		
	}

	public String getFunctions()
	{
		return functions;
	}

	public void setFunctions(String fun)
	{
		functions = fun;		
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
	 * @return the artifacts
	 */
	public Vector<CKArtifact> getArtifacts()
	{
		return artifacts;
	}

	/**
	 * @param artifacts the artifacts to set
	 */
	public void setArtifacts(Vector<CKArtifact> artifacts)
	{
		this.artifacts = artifacts;
	}

	/**
	 * @return the story
	 */
	public CKBook getStory()
	{
		return story;
	}

	/**
	 * @param story the story to set
	 */
	public void setStory(CKBook story)
	{
		this.story = story;
	}

	/**
	 * @param abilities the abilities to set
	 */
	public void setAbilities(CKBook abilities)
	{
		this.abilities = abilities;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.setPersistenceDelegate(CKArtifact.class, new CKArtifact.ArtifactPersistenceDelegate());
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
		AID =a;
		
	}

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
		
	}

	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		if(v==ViewEnum.STATIC)
		{
			return new JLabel(name);
		}
		else
		{		
			return new CKTeamPropertiesEditor(this,false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKTeam> getXMLPropertiesEditor()
	{
		return new CKTeamPropertiesEditor(this,true);
	}
	
	public static void main(String [] args)
	{
		
		CKTeam team = CKTeamFactory.getInstance().getAsset("ArtifactTest");
		System.out.println(team);
	}

	public CKDeltaBook calcDeltaBook(CKGridActor actor, CKArtifact artifact)
	{
		CKBook before = actor.getAbilities();
		
		String pos = findArtifactPosition(artifact,actor.getName());
		//clear old growth
		
		//artifact in same position
		CKArtifact oldPositionArtifact = actor.getArtifact(pos);
		if(oldPositionArtifact!=null)
		{
			this.unequipArtifact(oldPositionArtifact,false);
		}
		//person that presently owns artifact
		String oldOwner = artifact.getEquippedBy(); 
		String oldPos   = artifact.getEquippedAt();

		//ok equip it- this should avoid calling status updates...
		
		//equipArtifact(art,owner,
		this.equipArtifact(artifact, actor.getName(),
					findArtifactPosition(artifact,actor.getName()),false);
		CKBook after = actor.getAbilities();
		
		//now clean up.
		if(oldOwner.length()>0)
		{
			this.equipArtifact(artifact,oldOwner,oldPos,false);
		}
		if(oldPositionArtifact!=null)
		{
			this.equipArtifact(oldPositionArtifact, actor.getName(),pos,false);
		}
		
		return new CKDeltaBook(before,after);
		
		
	}

	
	
	
}
