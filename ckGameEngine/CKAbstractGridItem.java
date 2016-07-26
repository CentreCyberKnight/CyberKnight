package ckGameEngine;

import static ckCommonUtils.CKPropertyStrings.CH_WORLD;
import static ckCommonUtils.CKPropertyStrings.P_ON_CRUSH;
import static ckCommonUtils.CKPropertyStrings.P_ON_STEP;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

import javafx.scene.canvas.GraphicsContext;
import ckCommonUtils.CKPosition;
import ckEditor.treegui.CKGUINode;
import ckEditor.treegui.CKHiddenNode;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.CKTriggerList;
import ckTrigger.CKTriggerNode;
import ckTrigger.TriggerResult;

abstract public class CKAbstractGridItem 
{

	protected CKPosition pos = new CKPosition();
	protected CKTriggerList personalTriggers = new CKTriggerList();
	protected CKAbstractGridItem next = null;
	protected CKAbstractGridItem prev = null;
		
	/**
	 * This is needed to link triggers into the questData tree 
	 */
	CKHiddenNode hidden = new CKHiddenNode();
	
	
	

	public CKAbstractGridItem()
	{
		addTreeChild(personalTriggers);
	}

	
	public void setTreeParent(CKGUINode parent)
	{
		hidden.secretParent(parent);
	}
	
	public void addTreeChild(CKGUINode child)
	{
		hidden.addIT(child);
	}
	
	public abstract CKAbstractGridItem makeCopy();
	
	protected CKAbstractGridItem makeCopy(CKAbstractGridItem item)
	{
		item.setPos((CKPosition)pos.clone());
		item.setPersonalTriggers((CKTriggerList)personalTriggers.clone());
		item.next=null;
		item.prev=null;
		return item;
		
	}
	
	
	abstract public void setSharedTriggers(CKSharedTriggerList l);
	abstract public CKSharedTriggerList getSharedTriggers();
	
	
	
	
	public void addTrigger(CKTriggerNode trig)
	{
		personalTriggers.add(trig);
	}
	
	public void addTriggers(CKTriggerList trigs)
	{
		for (int i=0; i<trigs.getChildCount();i++)
		{
			CKTriggerNode trig = (CKTriggerNode) trigs.getChildAt(i);
			addTrigger(trig);
		}
	}

	public int getTotalWeight()
	{
		int rest = 0;
		if(next != null)
		{
			rest = next.getTotalWeight();
		}
		return getItemWeight()+rest;
	}

	/**
	 * Go through my list of triggers and if they do not get the job done,
	 * Go through the global triggers to see if there is anything solved.
	 * @param boss
	 * @param cast
	 */
	public void targetSpell(CKGameActionListenerInterface boss, CKSpellCast cast)
	{
		TriggerResult result = personalTriggers.doTriggers( 
				boss,false, cast);
		
		if(result !=TriggerResult.UNSATISFIED) { return;}
	
		result = getSharedTriggers().doTriggers(boss, false, cast);
		
	/*	if(result !=TriggerResult.UNSATISFIED) { return;}
		//go global?
		CKGameObjectsFacade.targetSpell(boss,cast);
		*/
	
	}

	abstract public CKGraphicsAsset getTerrainAsset();
	

	public void onStep(CKAbstractGridItem mover)
	{
		CKSpellCast cast = new CKSpellCast(this,mover,CH_WORLD,P_ON_STEP,
											mover.getItemWeight(),"");
		
		cast.castSpell();
	}

	public void onCrush(CKAbstractGridItem next2)
	{
		CKSpellCast cast = new CKSpellCast(this,next2,CH_WORLD,P_ON_CRUSH,
											next2.getItemWeight(),"");
		
		cast.castSpell();
	
	}

	public void onAdd()
	{
		if(next!=null)
		{
			addOn(next);
		}
		
		return; 
		
	}

	public void onRemove()
	{
		return;
	}
	
	/**
	 * performs the step action
	 * 
	 * @param mover
	 * @return
	 */
	public void stepOn(CKAbstractGridItem mover)
	{
		if(next != null && next != mover)
		{
			next.stepOn(mover);
		}
		else //end of recursion
		{
			if(mover.prev!=null)
			{
				mover.prev.next=null;
			}
			onStep(mover);
			next=mover;
			mover.prev=this;
			mover.pos.setX(this.pos.getX());
			mover.pos.setY(this.pos.getY());
			mover.pos.setZ(this.pos.getZ()+getItemHeight());
		}
		
			
		//handle crush here
		if(next.getTotalWeight()>this.getItemStrength())
		{
			onCrush(next);
		}
		
		
	}

	/**
	 * Adds CKAbstractGridItem to the stack without activating local abilities
	 * i.e. traps
	 * 
	 * @param mover
	 */
	public void addOn(CKAbstractGridItem mover)
	{
		if(next != null && next != mover)
		{
			next.addOn(mover);
		}
		else //end of recursion
		{
			double h =getItemHeight();
			if(mover.prev!=null)
			{
				mover.prev.next=null;
			}
			
			next=mover;
			mover.prev=this;
			mover.setPos((int)this.pos.getX(),(int)this.pos.getY(),
								(int)(this.pos.getZ()+h));
			mover.onAdd();//must have pos set first.

		}
		
			
		//handle crush here
		if(next.getTotalWeight()>this.getItemStrength())
		{
			onCrush(next);
		}
		
		
		
	}

	public CKPosition getPos()
	{
		
		return pos;
	}

	public CKAbstractGridItem getTop()
	{
		if(next==null)
		{
			return this;
		}
		else
		{
			return next.getTop();
		}
	}

	public int getTotalHeight()
	{
		if(next == null)
		{
			return (int) (pos.getZ()+getItemHeight());
		}
		return next.getTotalHeight();
	}

	public int getFinalMoveCost()
	{
		if(next == null)
		{
			return getMoveCost();
		}
		return next.getFinalMoveCost();
	
	
	
	}
	
	public int getFinalSlideCost()
	{
		if(next == null)
		{
			return getSlideCost();
		}
		return next.getFinalSlideCost();
	}

	
	

	public CKAbstractGridItem findItemWithAsset(String assetID)
	{
		if(this.getAssetID().compareTo(assetID)==0)
		{
			return this;
		}
		else if (next != null)
		{
			return next.findItemWithAsset(assetID);
		}
		else
		{
			return null;
		}
	}

	public void removeItemFromGrid(CKGrid grid)
	{
		if(prev !=null)//I'm not the bottom
		{
			prev.next = null;
		}
		else if(next !=null) //on bottom, clear out bottom, add new ones later
		{
			grid.setPosition(null, pos);
		}
		else if(grid.getPosition(pos) != this ) 
		{  //I'm not in the grid...
			//nothing to do
			
		}
		else //only item in the column.
		{
			grid.setPosition(new CKGridItem(),pos);
			onRemove();
			return;
		}
		
		if(next !=null)
		{	next.prev=null;
			grid.addToPosition(next, (int)next.getPos().getX(),(int)next.getPos().getY());
		}
		//addTo recursively re-adds each one in step...
		
		onRemove();
		
	}

	public void setPos(CKPosition pos)
	{
		setPos((int)pos.getX(),(int)pos.getY(),(int)pos.getZ());
	}

	public void setPos(int x, int y, int z)
	{
		pos.setX(x);
		pos.setY(y);
		pos.setZ(z);
	}

	/**
	 * @return the itemHeight
	 */
	abstract public int getItemHeight();
	
	/**
	 * @param itemHeight the itemHeight to set
	 */
	abstract public void setItemHeight(int itemHeight);
	
	/**
	 * @return the itemWeight
	 */
	abstract public int getItemWeight();
	
	/**
	 * @param itemWeight the itemWeight to set
	 */
	abstract public void setItemWeight(int itemWeight);


	/**
	 * @return the itemStrength
	 */
	abstract public int getItemStrength();


	/**
	 * @param itemStrength the itemStrength to set
	 */
	abstract public void setItemStrength(int itemStrength);


	/**
	 * @return the moveCost
	 */
	abstract public int getMoveCost();

	/**
	 * @param moveCost the moveCost to set
	 */
	abstract public void setMoveCost(int moveCost);
	
	/**
	 * @return the slideCost
	 */
	abstract public int getSlideCost();
	
	/**
	 * @param slideCost the slideCost to set
	 */
	abstract public void setSlideCost(int slideCost);

	/**
	 * @return the lowSide
	 */
	abstract public Direction getLowSide();

	/**
	 * @param lowSide the lowSide to set
	 */
	abstract public void setLowSide(Direction lowSide);
	
	/**
	 * @return the description
	 */
	abstract public String getDescription();

	/**
	 * @param description the description to set
	 */
	abstract public void setDescription(String description);

	
	/**
	 * @return the name
	 */
	abstract public String getName();


	/**
	 * @param name the name to set
	 */
	abstract public void setName(String name);


	/**
	 * @return the assetID
	 */
	abstract public String getAssetID();


	/**
	 * @param assetID the assetID to set
	 */
	abstract public void setAssetID(String assetID);

	public void drawItem(int frame, ImageObserver observer, CKCoordinateTranslator translator, Graphics g)
	{
		Point screenP = translator.convertMapToScreen(getPos());
		this.getTerrainAsset().drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
		if(next!=null)
		{
			next.drawItem(frame,observer,translator,g);
		}
		
	}
	
	public void drawItem(int frame, ImageObserver observer,
			CKCoordinateTranslator translator, GraphicsContext g)
	{
		Point screenP = translator.convertMapToScreen(getPos());
		this.getTerrainAsset().drawToGraphics(g, screenP.x, screenP.y,frame,0, observer);
		if(next!=null)
		{
			next.drawItem(frame,observer,translator,g);
		}
		
	}

	public void changeHeight(double heightDiff)
	{
		getPos().setZ(getPos().getZ()+heightDiff);
		if(next!=null)
		{
			next.changeHeight(heightDiff);
		}
		
	}

	abstract public CKGraphicsAsset getAsset();
	
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
		this.personalTriggers.removeFromParent();
		
		this.personalTriggers = personalTriggers;
		this.addTreeChild(this.personalTriggers);
	}

	/**
	 * @return the next
	 */
	public CKAbstractGridItem getNext()
	{
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(CKAbstractGridItem next)
	{
		this.next = next;
		next.prev = this;
	}

	public CKAbstractGridItem getPrev()
	{
		return this.prev;
	}
	
	


}