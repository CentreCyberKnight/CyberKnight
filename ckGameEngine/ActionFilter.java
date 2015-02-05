package ckGameEngine;

import java.util.ArrayList;

public class ActionFilter {

	public ArrayList<String> Actions;
	public boolean ListProhibited;
	
	public ActionFilter(boolean isAllowed, ArrayList<String> actions)
	{
		ListProhibited = isAllowed;
		Actions = actions;
	}
	
	public boolean ActionIsAllowed(String action)
	{
		if (ListProhibited)
		{
			if (Actions.contains(action))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			if (Actions.contains(action))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
