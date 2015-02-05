package ckGameEngine;

import java.util.Vector;

import javax.swing.Icon;

import ckEditor.CKSpellListener;

public class CKSpell
{
	String name;
	String description;
	String functionCall;
	String iconID;
	
	
	int timeTilRecharge;
	Vector<CKSpellListener> listeners;
	
	public CKSpell()
	{
		this("","","","sparkles");
	}
	
	public CKSpell(String name,String description,String fcn, String iconID)
	{
		this.name=name;
		this.description=description;
		this.functionCall=fcn;
		this.iconID=iconID;
		
		listeners=new Vector<CKSpellListener>();
		timeTilRecharge = 0;
	}
	
	public void turnPasses()
	{
		if(timeTilRecharge >0)
		{
			timeTilRecharge--;
			notifyRechargeChange();
		}		
	}
	
	/*
	public void runSpell()
	{
		//need to run the spell...
		
		
		
		//
		timeTilRecharge = rechargeTime;
		notifyRechargeChange();
	}
	*/
	
	//FIXME do I need these?
	public void addListener(CKSpellListener lis)
	{
		listeners.add(lis);
	}
	
	public void removeListener(CKSpellListener lis)
	{
		listeners.remove(lis);
	}
	
	
	protected void notifyRechargeChange()
	{
		for (CKSpellListener lis: listeners)
		{
			lis.rechargeChanged();
		}
	}
	
	protected void notifySpellChanged()
	{
		for (CKSpellListener lis: listeners)
		{
			lis.spellChanged();
		}
		
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
	 * @return the functionCall
	 */
	public String getFunctionCall()
	{
		return functionCall;
	}

	/**
	 * @param functionCall the functionCall to set
	 */
	public void setFunctionCall(String functionCall)
	{
		this.functionCall = functionCall;
	}

	/**
	 * @return the iconID
	 */
	public String getIconID()
	{
		return iconID;
	}

	/**
	 * @param iconID the iconID to set
	 */
	public void setIconID(String iconID)
	{
		this.iconID = iconID;
	}

	
	
	
	/**
	 * @return the timeTilRecharge
	 */
	public int getTimeTilRecharge()
	{
		return timeTilRecharge;
	}

	/**
	 * @param timeTilRecharge the timeTilRecharge to set
	 */
	public void setTimeTilRecharge(int timeTilRecharge)
	{
		this.timeTilRecharge = timeTilRecharge;
	}
	
	
}
