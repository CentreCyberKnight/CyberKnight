package ckGameEngine;

import java.awt.image.BufferedImage;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.ArtifactPropertiesEditor;
import ckEditor.CKArtifactShortView;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckEditor.treegui.BookList;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import static ckCommonUtils.CKPropertyStrings.*;

public class CKArtifact implements CKXMLAsset<CKArtifact> 
{
	String AID = "";
	String name;
	String backstory;
	String iconId;
	Image fximage;
	String snapImage;
	
	Vector<CKSpell> spells;	
	
	CKBook abilties;
	CKBook limits;
	BookList requirements;
	
	int rechargeRate;
	String equippedBy="";
	String equippedAt="";
	
	
	
	
	public CKArtifact(String name,String backstory,String icon,
			CKBook abilities,CKBook limits,BookList requirements,int rechargeRate)
	{
		this.name= name;
		this.backstory=backstory;
		this.iconId=icon;
		this.spells=new Vector<CKSpell>();
		this.fximage = null;
		
		this.abilties=abilities;
		this.limits=limits;
		this.requirements = requirements;
		
/*		for(BookList b:requirements)
		{
			this.requirements.add(b);
		}
*/
		this.rechargeRate=rechargeRate;
	}

	public CKArtifact(String name,String backstory,String icon,
			CKBook abilities,CKBook limits,CKBook requirements,int rechargeRate)
	{
		/*this.name= name;
		this.backstory=backstory;
		this.iconId=icon;
		this.spells=new Vector<CKSpell>();
		
		this.abilties=abilities;
		this.limits=limits;
		this.requirements = new Vector<CKBook>();*/
		
		this(name,backstory,icon,abilities,limits,new BookList(requirements),rechargeRate);
		
	}
	public CKArtifact(String name,String backstory,String icon,
			CKBook abilities,CKBook limits,CKBook [] requirements,int rechargeRate)
	{
		
		this(name,backstory,icon,abilities,limits,new BookList(requirements),rechargeRate);
		
	}
	
	public CKArtifact(String name)
	{
		this(name,"","equipment",new CKBook(),new CKBook(),new BookList(),1);
	}
	

	public CKArtifact()
	{
		this("","","equipment",new CKBook(),new CKBook(),new BookList(),1);
	}
	
	
	public void addRequirementSet(CKBook req)
	{
		requirements.add(req);
	}

	

	
		
	/**
	 * @return the requirements
	 */
	public BookList getRequirements()
	{
		return requirements;
	}
	
	/**
	 * @param requirements the requirements to set
	 */
	public void setRequirements(BookList requirements)
	{
		this.requirements = requirements;
	}
	

	public CKBook getLimits()
	{
		return limits;
	}

	public void addSpell(CKSpell spell)
	{
		spells.add(spell);
		//TODO notify artifact change?
	}
	
	public void removeSpell(CKSpell spell)
	{
		spells.remove(spell);
	}
	
	public int spellCount()
	{
		return spells.size();
	}
	
	public void addRequirements(CKBook req)
	{
		requirements.add(req);
	}
	
	public Iterator<CKSpell> getSpells()
	{
		return spells.iterator();
	}
	
	public void setSpells(Vector<CKSpell> vect)
	{
		this.spells = vect;
	}
	
	
	
	public void turnPasses()
	{
		//TODO something?
	}

	public CKBook getAbilities()
	{
		return abilties;
	}


	/**
	 * @return the requirements
	 */
	public Iterator<CKBook> getRequirementsIter()
	{
		return requirements.iterator();
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
	 * @return the iconId
	 */
	public String getIconId()
	{
		return iconId;
	}


	/**
	 * @param iconId the iconId to set
	 */
	public void setIconId(String iconId)
	{
		this.iconId = iconId;
	}
	
	
	
	
	public Image getFXImage() 
	{
		if(this.fximage != null) {	
			return fximage;
		}
		else {
			try {
				System.out.println("The image for " + iconId + " was not found. It is now being created.");	
    			CKGraphicsAsset asset = CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(iconId);
    			Image image = CKGraphicsPreviewGenerator.createAssetPreviewFX(asset, 0, 0, 80, 90);
    			this.fximage = image;
			}
			catch (NullPointerException n) {
				System.out.println("The asset for " + iconId + " was not found." );
			}
			return fximage;	
		}
	}
	
	//returns a base64 string of the artifact image
	public String getSnapImage()
		{
		if(this.snapImage != null && this.fximage != null) {	
			return this.snapImage;
		}
		else {
			try {
				getFXImage();
    			BufferedImage bImage = SwingFXUtils.fromFXImage(this.fximage, null);
    			ByteArrayOutputStream s = new ByteArrayOutputStream();
    			try {
					ImageIO.write(bImage, "png", s);
					} 
    			catch (IOException e) {
					e.printStackTrace();
					}
    			this.snapImage  = Base64.encode(s.toByteArray());
			}
			catch (NullPointerException n) {
				System.out.println("The asset for " + iconId + " was not found." );
			}
			return this.snapImage;	
		}
	}
	
	//returns an array of images converted to base64
	//the images are for the spells
	public String[] getSpellImageArray()
	{
		 String[] spellImages = new String[spells.size()];
		 for (int i = 0; i < spells.size(); i++) {
			 spellImages[i] = spells.get(i).getSnapImage();
		 }
		 return spellImages;
	}
	
	//returns an array of spell names
	public String[] getSpellNamesArray()
	{
		 String[] spellNames = new String[spells.size()];
		 for (int i = 0; i < spells.size(); i++) {
			 spellNames[i] = spells.get(i).getName();
		 }
		 return spellNames;
	}

	
	/**
	 * @return the abilties
	 */
	public CKBook getAbilties()
	{
		return abilties;
	}


	/**
	 * @param abilties the abilties to set
	 */
	public void setAbilties(CKBook abilties)
	{
		this.abilties = abilties;
	}


	/**
	 * @return the rechargeRate
	 */
	public int getRechargeRate()
	{
		return rechargeRate;
	}


	/**
	 * @param rechargeRate the rechargeRate to set
	 */
	public void setRechargeRate(int rechargeRate)
	{
		this.rechargeRate = rechargeRate;
	}


	/**
	 * @param limits the limits to set
	 */
	public void setLimits(CKBook limits)
	{
		this.limits = limits;
	}


	/**
	 * @return the equippedBy
	 */
	public String getEquippedBy()
	{
		return equippedBy;
	}

	/**
	 * @param equippedBy the equippedBy to set
	 */
	public void setEquippedBy(String equippedBy)
	{
		this.equippedBy = equippedBy;
	}

	/**
	 * @return the equippedAt
	 */
	public String getEquippedAt()
	{
		return equippedAt;
	}

	/**
	 * @param equippedAt the equippedAt to set
	 */
	public void setEquippedAt(String equippedAt)
	{
		this.equippedAt = equippedAt;
	}

	

	public boolean usesPosition(String pos)
	{
		for(CKBook b:requirements)
		{
			//System.out.println(name+" looking for "+pos+"\n"+b.treeString());
			if( b.hasPage(CH_EQUIP_SLOTS,pos)) {return true;}
		}
		//System.out.println("no..");
		return false;
			
	}

	public CKSpell getSpell(int pos)
	{
		
		return spells.get(pos);
	}

	public CKBook getUsageLimits()
	{
		if(equippedBy.length()==0)
		{
			return limits;
		}
		else
		{
			CKGridActor act = CKGameObjectsFacade.getQuest().getActor(equippedBy);
			if(act==null)
			{
				return limits;
			}
			return CKBook.minBounds(limits,act.getAbilities());
		}
	}

	static class ArtifactPersistenceDelegate extends DefaultPersistenceDelegate
	{
	    protected void initialize(Class type, Object oldInstance,
	                              Object newInstance, Encoder out) 
	    {
	        super.initialize(type, oldInstance,  newInstance, out);

	        CKArtifact art = (CKArtifact) oldInstance;

	        Iterator<CKSpell> iter = art.getSpells();
	        while(iter.hasNext())
	        {
	        	CKSpell inst = iter.next();
	            out.writeStatement(new java.beans.Statement(oldInstance,
	                              "addSpell", // Could also use "addElement" here.
	                              new Object[]{inst}) );
	        }
	    }
	}
	
	
	
	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		e.setPersistenceDelegate(getClass(), new ArtifactPersistenceDelegate());
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
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		switch(v)
		{
		case STATIC:
		case INTERACTIVE:
			CKArtifactShortView view = new CKArtifactShortView();
			view.setArtifact(this);
			return view;
		case EDITABLE:
			return new ArtifactPropertiesEditor(this, true);
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CKXMLAssetPropertiesEditor<CKArtifact> getXMLPropertiesEditor()
	{
		return new ArtifactPropertiesEditor(this, true);
	}
	

	
	public static void main(String[] args)
	{
		CKBook limits = new CKBook();
		String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		limits.addChapter(new CKChapter(CH_MOVE,2,pages ) );
		CKBook abilities=new CKBook("Abilties",CH_VOICE,1,P_TALK);
		CKBook []reqs = {new CKBook("Requirements", CH_EQUIP_SLOTS,0,P_SHOES) };
		CKArtifact combatBoots = new CKArtifact("Combat Boots","Given to you by your grandmother",
				"boots", abilities,limits,new BookList(reqs),2);
		
		//need some spells
		CKSpell spell = new CKSpell("Go Forth", "moves forward by 2",
				"move('forward',2)", "upArrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("LeftTurn", "turns left and moves forward",
				"moveAndTurn(False)", "leftArrow");
		combatBoots.addSpell(spell);
		spell = new CKSpell("UTurn", "turns around", "move('left',2)", "uTurn");
		combatBoots.addSpell(spell);		
			
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		combatBoots.writeToStream(baos);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		System.out.println(new String( baos.toByteArray()));
		
		XMLDecoder xmldecoder = new XMLDecoder(bais);
		//CKArtifact NewBoots = (CKArtifact) xmldecoder.readObject();
		
		
	xmldecoder.close();
		
	}
	
}
