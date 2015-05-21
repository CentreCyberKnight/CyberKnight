package ckGameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ckCommonUtils.CKPosition;

public class CKSpellResult
{
	private class Tuple
	{
		public CKAbstractGridItem target;
		public String action;
		public String result;
		
		public Tuple(CKAbstractGridItem target, String action, String result)
		{
			this.target = target;
			this.action = action;
			this.result = result;
		}
		
		
		/**
		 * @return the target
		 */
		public CKAbstractGridItem getTarget()
		{
			return target;
		}

		/**
		 * @param target the target to set
		 */
		public void setTarget(CKAbstractGridItem target)
		{
			this.target = target;
		}

		/**
		 * @return the action
		 */
		public String getAction()
		{
			return action;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(String action)
		{
			this.action = action;
		}

		/**
		 * @return the result
		 */
		public String getResult()
		{
			return result;
		}

		/**
		 * @param result the result to set
		 */
		public void setResult(String result)
		{
			this.result = result;
		}

		
		

	}
	
	private ArrayList<Tuple> results = new ArrayList<>();
		
	public CKSpellResult() {}
	
	public void addResult(CKAbstractGridItem target, String action, String result)
	{
			results.add(new Tuple(target,action,result));
	}
	
	public Map<String, List<String>> getResult(CKAbstractGridItem target)
	{
		
		/*HashMap<String,String> map = new HashMap<>();
		
		for(Tuple t:results)
		{
			if (t.target==target)
			{
				//TODO merge if already in map!!
				map.put(t.action, t.result);
			}
		}
		return map;*/
		
		return results.stream()
			.filter(t->t.target==target)
			.collect(Collectors.groupingBy(Tuple::getAction,
					Collectors.mapping(Tuple::getResult,Collectors.toList())));
	}
	
	public String getResult(CKAbstractGridItem target,String action)
	{
		String ret ="";
		for(Tuple t:results)
		{
			if(t.target==target && t.action.equals(action))
			{
				ret +=t.result;
			}
		}
		return ret;
	}

	
	
	
	
}
