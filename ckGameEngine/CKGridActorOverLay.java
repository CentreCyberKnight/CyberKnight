package ckGameEngine;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;

import ckCommonUtils.CKPosition;
import ckEditor.CKXMLAssetPropertiesEditor;
import ckGameEngine.actions.CKGameActionListenerInterface;
import ckTrigger.CKSharedTriggerList;
import ckTrigger.TriggerResult;

public class CKGridActorOverLay extends CKGridActor
{
	
	
	
	
	
	public CKGridActorOverLay()
	{
		this.setSharedTriggers(new CKSharedTriggerList("NULL"));
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
	
		result = getSharedTriggers().doTriggers(boss, false, cast);
	
		if(prev != null)
		{
			prev.targetSpell(boss,cast);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getMoveCost()
	 */
	@Override
	public int getMoveCost()
	{
		if(prev==null)
		{
			return super.getMoveCost();
		}
		else
		{
			return prev.getMoveCost()+super.getMoveCost();
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#setMoveCost(int)
	 */
	@Override
	public void setMoveCost(int moveCost)
	{
		if(prev==null)
		{
			super.setMoveCost(moveCost);
		}
		else
		{
			super.setMoveCost(moveCost-prev.getMoveCost());
		}
		
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getItemHeight()
	 */
	@Override
	public int getItemHeight()
	{
		if(prev==null)
		{
			return super.getItemHeight();
		}
		else
		{
			return prev.getItemHeight()+super.getItemHeight();
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#setItemHeight(int)
	 */
	@Override
	public void setItemHeight(int itemHeight)
	{
		if(prev==null)
		{
			super.setItemHeight(itemHeight);
		}
		else
		{
			super.setItemHeight(itemHeight-prev.getItemHeight());
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getItemWeight()
	 */
	@Override
	public int getItemWeight()
	{
		if(prev==null)
		{
			return super.getItemWeight();
		}
		else
		{
			return prev.getItemWeight()+super.getItemWeight();
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#setItemWeight(int)
	 */
	@Override
	public void setItemWeight(int itemWeight)
	{
		if(prev==null)
		{
			super.setItemWeight(itemWeight);
		}
		else
		{
			super.setItemWeight(itemWeight-prev.getItemWeight());
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getItemStrength()
	 */
	@Override
	public int getItemStrength()
	{
		
		if(prev==null)
		{
			return super.getItemStrength();
		}
		else
		{
			return prev.getItemStrength()+super.getItemStrength();
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#setItemStrength(int)
	 */
	@Override
	public void setItemStrength(int itemStrength)
	{
		if(prev==null)
		{
			super.setItemStrength(itemStrength);
		}
		else
		{
			super.setItemStrength(itemStrength-prev.getItemStrength());
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getDescription()
	 */
	@Override
	public String getDescription()
	{
		
		if(prev==null)
		{
			return super.getDescription();
		}
		else
		{
			return prev.getDescription()+" is "+super.getMoveCost();
		}
	}


	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getName()
	 */
	@Override
	public String getName()
	{
		if(prev==null)
		{
			return super.getName();
		}
		else
		{
			return super.getName()+ prev.getName();
		}
	}


	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#getSlideCost()
	 */
	@Override
	public int getSlideCost()
	{
		if(prev==null)
		{
			return super.getSlideCost();
		}
		else
		{
			return prev.getSlideCost()+super.getSlideCost();
		}
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.CKGridItem#setSlideCost(int)
	 */
	@Override
	public void setSlideCost(int slideCost)
	{
		if(prev==null)
		{
			super.setSlideCost(slideCost);
		}
		else
		{
			super.setSlideCost(slideCost-prev.getSlideCost());
		}
	}

	
	
	
	
	
	/* (non-Javadoc)
	 * @see ckGameEngine.CKAbstractGridItem#getPos()
	 */
	@Override
	public CKPosition getPos()
	{
		if(prev==null)
		{ 	return super.getPos();  }
		else
		{
			return prev.getPos();
		}
	}
	
	//leave setPos as normal
	

	

	@SuppressWarnings("unchecked")
	@Override
	public CKXMLAssetPropertiesEditor<CKGridItem> getXMLPropertiesEditor()
	{
		// TODO Auto-generated method stub
		return super.getXMLPropertiesEditor();
	}
	
	public static class GridActorOverLayPersistenceDelegate extends DefaultPersistenceDelegate
	{
	    protected void initialize(Class<?> type, Object oldInstance,
	                              Object newInstance, Encoder out) 
	    {
	        super.initialize(type, oldInstance,  newInstance, out);

	        CKGridActorOverLay oldOverlay = (CKGridActorOverLay) oldInstance;
	        //should create a duplicate output to override the previous one
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setMoveCost", 
                    new Object[]{oldOverlay.moveCost}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setItemHeight", 
                    new Object[]{oldOverlay.itemHeight}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setItemWeight", 
                    new Object[]{oldOverlay.itemWeight}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setItemStrength", 
                    new Object[]{oldOverlay.itemStrength}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setDescription", 
                    new Object[]{oldOverlay.description}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setName", 
                    new Object[]{oldOverlay.name}) );
	        out.writeStatement(new java.beans.Statement(oldInstance,
                    "setMoveCost", 
                    new Object[]{oldOverlay.moveCost}) );
	        
	        
	       
	    }
	}
	
	@Override
	public CKAbstractGridItem makeCopy()
	{
		return makeCopy(new CKGridActorOverLay());		
	}
	
}
