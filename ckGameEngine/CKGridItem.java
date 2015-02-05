package ckGameEngine;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import javax.swing.JComponent;

import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKGridItemPropertiesEditor;
import ckEditor.CKGridItemViewer;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTriggerList;

public class CKGridItem extends CKAbstractGridItem implements CKXMLAsset<CKGridItem>
{

	int itemHeight=0;
	
	int itemWeight=0;
	/**
	 * how much weight can be placed on top of this item before it breaks.
	 */
	int itemStrength=100; 

	int moveCost=Integer.MAX_VALUE;
	int slideCost=5;
	Direction lowSide=Direction.NONE;
	
	
	String assetID="null";
	
	private String AID="";
	String name="";
	String description="";
	
	CKSharedTriggerList sharedTriggers = new CKSharedTriggerList("baseline"); 
	
	
	public CKGridItem()
	{
		this.addTreeChild(sharedTriggers);
	}
	/*
	public CKGridItem(CKPosition position)
	{
		setPos(position);
	}
*/

	public void setSharedTriggers(CKSharedTriggerList l)
	{
		sharedTriggers.removeFromParent();
		
		sharedTriggers=l;
		this.addTreeChild(sharedTriggers);
		
	}

	public CKGraphicsAsset getTerrainAsset()
	{
		return CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
	}

	

	/**
	 * @return the itemHeight
	 */
	public int getItemHeight()
	{
		return itemHeight;
	}

	/**
	 * @param itemHeight the itemHeight to set
	 */
	public void setItemHeight(int itemHeight)
	{
		this.itemHeight = itemHeight;
	}

	/**
	 * @return the itemWeight
	 */
	public int getItemWeight()
	{
		return itemWeight;
	}

	/**
	 * @param itemWeight the itemWeight to set
	 */
	public void setItemWeight(int itemWeight)
	{
		this.itemWeight = itemWeight;
	}

	/**
	 * @return the itemStrength
	 */
	public int getItemStrength()
	{
		return itemStrength;
	}

	/**
	 * @param itemStrength the itemStrength to set
	 */
	public void setItemStrength(int itemStrength)
	{
		this.itemStrength = itemStrength;
	}

	/**
	 * @return the moveCost
	 */
	public int getMoveCost()
	{
		return moveCost;
	}

	/**
	 * @param moveCost the moveCost to set
	 */
	public void setMoveCost(int moveCost)
	{
		this.moveCost = moveCost;
	}

	/**
	 * @return the slideCost
	 */
	public int getSlideCost()
	{
		return slideCost;
	}

	/**
	 * @param slideCost the slideCost to set
	 */
	public void setSlideCost(int slideCost)
	{
		this.slideCost = slideCost;
	}

	/**
	 * @return the lowSide
	 */
	public Direction getLowSide()
	{
		return lowSide;
	}

	/**
	 * @param lowSide the lowSide to set
	 */
	public void setLowSide(Direction lowSide)
	{
		this.lowSide = lowSide;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
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
	 * @return the assetID
	 */
	public String getAssetID()
	{
		return assetID;
	}

	/**
	 * @param assetID the assetID to set
	 */
	public void setAssetID(String assetID)
	{
		this.assetID = assetID;
	}
/*  done in super class
	public void drawItem(int frame, ImageObserver observer, CKCoordinateTranslator translator, Graphics g)
	{
		Point screenP = translator.convertMapToScreen(getPos());
		this.getTerrainAsset().drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
		if(next!=null)
		{
			next.drawItem(frame,observer,translator,g);
		}
		
	}
*/
	public void changeHeight(double heightDiff)
	{
		getPos().setZ(getPos().getZ()+heightDiff);
		if(next!=null)
		{
			next.changeHeight(heightDiff);
		}
		
	}

	public CKGraphicsAsset getAsset()
	{
		return CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset(assetID);
	}

	/**
	 * @return the personalTriggers
	 */
	public CKTriggerList getPersonalTriggers()
	{
		return personalTriggers;
	}

	/**
	 * @param personalTriggers the personalTriggers to set
	 */
	public void setPersonalTriggers(CKTriggerList personalTriggers)
	{
		this.personalTriggers = personalTriggers;
	}

	

	/**
	 * @return the sharedTriggers
	 */
	public CKSharedTriggerList getSharedTriggers()
	{
		return sharedTriggers;
	}

	

	@Override
	protected CKAbstractGridItem makeCopy(CKAbstractGridItem item)
	{
		super.makeCopy(item);
		CKGridItem I = (CKGridItem) item;
		I.setItemHeight(itemHeight);
		I.setItemWeight(itemWeight);
		I.setItemStrength(itemStrength);
		I.setMoveCost(moveCost);
		I.setSlideCost(slideCost);
		I.setLowSide(lowSide); //OK since direction is enumeration?
		I.setAssetID(assetID);
		I.setAID("");//should not have one for copy.
		I.setName("Copy of"+name);
		I.setDescription(description);
		I.setSharedTriggers(sharedTriggers);
		
		return I;
		
	}
	
	@Override
	public CKAbstractGridItem makeCopy()
	{
		return makeCopy(new CKGridItem());		
	}


	
	
	
	/*CKXMLAsset methods  */
	 
	 
	 
	


	public String getAID()
	{
		return AID;
	}


	public void setAID(String a)
	{
		AID=a;		
	}
	
	
	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(
				new BufferedOutputStream(out));
		//e.setPersistenceDelegate(getClass(), new ArtifactPersistenceDelegate());
		e.writeObject(this);
		e.close();		
	}

	
	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		return new CKGridItemViewer(20, this, null, false);

	}

	
	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}


	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKGridItem> getXMLPropertiesEditor()
	{
		
		return new CKGridItemPropertiesEditor(this);
	}

	
	

	
	
	
	
}
