package ckGameEngine;

import ckDatabase.CKGridItemFactory;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckGraphicsEngine.assets.CKGraphicsAsset;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.TriggerResult;

public class CKSharedGridItem extends CKAbstractGridItem
{
	
	
	
	



	String sharedGI="null";
	
	
	public CKSharedGridItem()
	{
		
	}
	
	public CKSharedGridItem(String gi)
	{
		sharedGI = gi;
	}
	
	
	public static void main(String [] args)
	{
		CKSharedGridItem item = new CKSharedGridItem("asset4010021008016557691");
		CKAbstractGridItem sh = item.getShared();
		
		System.out.println("here"+sh);
		
	}
	
	
	
	/**
	 * @return the sharedGI
	 */
	public String getSharedGI()
	{
		return sharedGI;
	}




	/**
	 * @param sharedGI the sharedGI to set
	 */
	public void setSharedGI(String sharedGI)
	{
		this.sharedGI = sharedGI;
	}

	public CKGridItem getShared()
	{
		return CKGridItemFactory.getInstance().getAsset(sharedGI);
	}


	@Override
	public void setSharedTriggers(CKSharedTriggerList l)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public CKSharedTriggerList getSharedTriggers()
	{
		return getShared().getSharedTriggers();
	}

	@Override
	public CKGraphicsAsset getTerrainAsset()
	{
		return getShared().getTerrainAsset();
	}

	@Override
	public int getItemHeight()
	{
		return getShared().getItemHeight();
	}

	@Override
	public void setItemHeight(int itemHeight)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public int getItemWeight()
	{
		return getShared().getItemWeight();
	}

	@Override
	public void setItemWeight(int itemWeight)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public int getItemStrength()
	{
		return getShared().getItemStrength();
	}

	@Override
	public void setItemStrength(int itemStrength)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public int getMoveCost()
	{
		return getShared().getMoveCost();
	}

	@Override
	public void setMoveCost(int moveCost)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public int getSlideCost()
	{
		return getShared().getSlideCost();
	}

	@Override
	public void setSlideCost(int slideCost)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public Direction getLowSide()
	{
		return getShared().getLowSide();
	}

	@Override
	public void setLowSide(Direction lowSide)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public String getDescription()
	{
		return getShared().getDescription();
	}

	@Override
	public void setDescription(String description)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public String getName()
	{
		return getShared().getName();
	}

	@Override
	public void setName(String name)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public String getAssetID()
	{
		return getShared().getAssetID();
	}

	@Override
	public void setAssetID(String assetID)
	{
		System.err.println("Cannot set values on shared resource");
	}

	@Override
	public CKGraphicsAsset getAsset()
	{
		return getShared().getAsset();
	}




	@Override
	public CKAbstractGridItem makeCopy()
	{
		CKSharedGridItem item = new CKSharedGridItem(sharedGI);
		
		return super.makeCopy(item);
	}
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.CKAbstractGridItem#targetSpell(ckGameEngine.actions.CKGameActionListenerInterface, ckGameEngine.CKSpellCast)
	 */
	@Override
	public void targetSpell(CKGameActionListenerInterface boss, CKSpellCast cast)
	{
			TriggerResult result = personalTriggers.doTriggers( 
					boss,false, cast);
			
			if(result !=TriggerResult.UNSATISFIED) { return;}
		
			this.getShared().targetSpell(boss, cast);
	}


}
