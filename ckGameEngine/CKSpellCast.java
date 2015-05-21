package ckGameEngine;

import ckCommonUtils.CKPosition;
import ckGameEngine.actions.CKGameActionListener;
import ckGameEngine.actions.CKGameActionListenerInterface;

public class CKSpellCast implements Cloneable
{

	private CKAbstractGridItem target;
	private CKAbstractGridItem source;
	private String chapter;
	private CKSpellResult result=new CKSpellResult();

	private String page;
	private int cp;
	private String key;

	private CKPosition redirect = null;

	/**
	 * SpellCast is a message sent into the system to request that the target
	 * perform an action.
	 * 
	 * @param target
	 *            - who/what is the target of the spell
	 * @param source
	 *            - who cast the spell
	 * @param chap
	 * @param page
	 * @param cp
	 * @param key
	 */
	public CKSpellCast(CKAbstractGridItem target, CKAbstractGridItem source,
			String chap, String page, int cp, String key)
	{
		this.target = target;
		this.source = source;
		this.chapter = chap;
		this.page = page;
		this.cp = cp;
		this.key = key;
	}

	public CKSpellCast lateBindCopy(CKAbstractGridItem lateTarget,
			CKAbstractGridItem lateSource)
	{

		CKSpellCast copy = new CKSpellCast(target, source, chapter, page, cp,
				key);
		if (target == null)
		{
			copy.target = lateTarget;
		}
		if (source == null)
		{
			copy.source = lateSource;
		}
		return copy;

	}

	public CKSpellResult getResult()
	{
		return result;
	}

	public void setResult(CKSpellResult result)
	{
		this.result = result;
	}
	
	public void addResult(CKAbstractGridItem t,String action,String result)
	{
		this.result.addResult(t, action, result);		
	}

	/**
	 * @return the redirect
	 */
	public CKPosition getRedirect()
	{
		return redirect;
	}

	/**
	 * @param redirect the redirect to set
	 */
	public void setRedirect(CKPosition redirect)
	{
		this.redirect = redirect;
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}




	/**
	 * @param key the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}




	/**
	 * @return the target
	 */
	public CKPosition getTargetPosition()
	{
		return target.getPos();
	}
	
	public CKAbstractGridItem getItemTarget()
	{
		return target;		
	}

	
	public CKGridActor getActorTarget()
	{
		if(target instanceof CKGridActor)
		{
			return (CKGridActor) target;
		}
		else
		{
			return null;
		}
	}
	

/*	public PC getPCTarget()
	{
		PlaceHolder holder = CKGameObjectsFacade.getQuest().getGrid().getPositionFromList(target).getPlaceHolder();
		if(holder == null) { return null; }
		else return (PC) holder;
	}
*/

	/**
	 * @param target the target to set
	 */
	public void setTarget(CKAbstractGridItem target)
	{
		this.target = target;
	}




	/**
	 * @return the source
	 */
	public CKAbstractGridItem getSource()
	{
		return source;
	}

	public CKGridActor getPCSource()
	{
		return (CKGridActor) source;
	}



	/**
	 * @param source the source to set
	 */
	public void setSource(CKAbstractGridItem source)
	{
		this.source = source;
	}




	/**
	 * @return the chapter
	 */
	public String getChapter()
	{
		return chapter;
	}




	/**
	 * @param chapter the chapter to set
	 */
	public void setChapter(String chapter)
	{
		this.chapter = chapter;
	}




	/**
	 * @return the page
	 */
	public String getPage()
	{
		return page;
	}




	/**
	 * @param page the page to set
	 */
	public void setPage(String page)
	{
		this.page = page;
	}




	/**
	 * @return the cp
	 */
	public int getCp()
	{
		return cp;
	}




	/**
	 * @param cp the cp to set
	 */
	public void setCp(int cp)
	{
		this.cp = cp;
	}


	public void castSpell()
	{
		CKGameActionListener listener = new CKGameActionListener();
		castSpell(listener, key);
	}
	
	public void castSpell(CKGameActionListenerInterface boss, String key)
	{
		//FIXME will need to handle world filters 
	/*	if(redirect ==null)
		{
			CKGameObjectsFacade.getQuest().applyWorldFilters(boss,this);
		}
		*/
		/*
		 * This is now handled be CKGridItemSet...without the need for intervention...
		 * if(target instanceof CKAreaPositions)
		{
			CKGameObjectsFacade.targetSpell(boss,this);
			return;
		}*/
		System.err.println("Casting:"+toString());

		CKAbstractGridItem item = getItemTarget();
		
		if(item == null) // FIXME This should never happen now
		{
			CKGameObjectsFacade.targetSpell(boss,this);
		}		
		else //goto Pc to filter and resolve this spell
		{
			item.targetSpell(boss,this);
		}

	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return source.getName()+" casts "+ chapter+":"+page+" at "+target.getName()+" for "+cp;
		
	}


	

	
	
	
	
	
}
