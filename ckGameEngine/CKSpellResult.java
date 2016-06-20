package ckGameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class CKSpellResult
{
	
	
	public final static String DAMAGE="damage"; 
	
	 class Tuple
	{
		public CKAbstractGridItem target;
		public String action;
		public String resultType;
		public double result;
		private String resultDescription;
		
		public Tuple(CKAbstractGridItem target, String action, String resultType,
				double result,String resultDescription)
		{
			this.target = target;
			this.action = action;
			this.resultType = resultType;
			this.result = result;
			this.setResultDescription(resultDescription);
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
		public String getResultType()
		{
			return resultType;
		}

		/**
		 * @param result the result to set
		 */
		public void setResultType(String resultType)
		{
			this.resultType = resultType;
		}


		/**
		 * @return the result
		 */
		public double getResult()
		{
			return result;
		}


		/**
		 * @param result the result to set
		 */
		public void setResult(double result)
		{
			this.result = result;
		}


		public String getResultDescription()
		{
			return resultDescription;
		}


		public void setResultDescription(String resultDescription)
		{
			this.resultDescription = resultDescription;
		}

		
		

	}
	
	private ArrayList<Tuple> results = new ArrayList<>();
		
	public CKSpellResult() {}
	
	public void addResult(CKAbstractGridItem target, String action, String resultType,double result)
	{
			results.add(new Tuple(target,action,resultType,result,""));
	}
	
	public void addResult(CKAbstractGridItem target, String action, String resultType,double result,String resultString)
	{
			results.add(new Tuple(target,action,resultType,result,resultString));
	}

	
	public Stream<Tuple> actorStream()
	{
		return results.stream()
				.filter(t-> t.target instanceof CKGridActor);
	}

	public Stream<Tuple> actorStream(String name)
	{
		return actorStream().filter(t->t.getTarget().getName().equals(name));		
	}
	
	public Stream<Tuple> actorStream(CKAbstractGridItem item)
	{
		return results.stream().filter(t->t.getTarget()==item);		
	}
	

	/**
	 * Creates an actor stream of the members of a team
	 * @param team
	 * @return
	 */
	public Stream<Tuple> actorStream(CKTeam team,boolean onTeam)
	{
		return actorStream().filter(t->onTeam == (((CKGridActor)t.getTarget()).getTeam() == team));
	}
	
	
	

	/**
	 * Creates an actor stream of the members of a team
	 * @param team
	 * @return
	 */
	public Stream<Tuple> actorStream(CKTeam team)
	{
		return actorStream(team,true);
	}
	
	
	public DoubleStream resultsStream(String resultType)
	{
		return results.stream()
				.filter(r->r.resultType.equalsIgnoreCase(resultType))
				.mapToDouble(Tuple::getResult);
	}
	
	public DoubleStream resultsStream(String resultType,CKTeam team)
	{
		return actorStream(team)
				.filter(r->r.resultType.equalsIgnoreCase(resultType))
				.mapToDouble(Tuple::getResult);
	}
	
	public double sumResults(String resultType)
	{
		return resultsStream(resultType).sum();	
	}
	
	
	public double avgResults(String string, int depth)
	{
		return sumResults(string)/depth;
	}

	public double sumResults(String resultType,CKTeam team)
	{
		return resultsStream(resultType,team).sum();	
	}
	
	
	public double avgResults(String string, CKTeam team, int depth)
	{
		return sumResults(string,team)/depth;
	}

	
	public double avgResults(String resultType)
	{
		return resultsStream(resultType).average().getAsDouble();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Action, Results, for a target
	@Deprecated
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
					Collectors.mapping(Tuple::getResultType,Collectors.toList())));
	}
	
	@Deprecated
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
	
	public Set<String> allResultTypes()
	{
		return results.stream().collect(Collectors.mapping(Tuple::getResultType,Collectors.toSet()));
	}

	
	
	
	
}
