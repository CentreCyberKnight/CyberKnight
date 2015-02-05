package ckTrigger;

public enum TriggerResult {

	
	SATISFIED("SATISFIED"), 
	UNSATISFIED("UNSATISFIED"), 
	SATISFIED_ONCE("RUN_ONCE"), 
	SATISFIED_END_QUEST("END_QUEST"),
	SATISFIED_END_LOOP("END_TRIGGER_LOOP"),
	INIT_ONLY("INIT_ONLY");
	
	
	private final String resultName;
		
	
	TriggerResult(String name)
	{
		this.resultName=name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() 
	{
		return this.resultName;
	}


	/**
	 * gets the triggerResult that has the string representation of name, UNSATISFIED otherwise
	 * @param name string representing the triggerResult
	 * @return triggerResult for the string
	 */
	static public TriggerResult getResults(String name)
	{
		for (TriggerResult t:TriggerResult.values())
		{
			if(t.toString().equals(name))
			{
				return t;
			}
		}
		return TriggerResult.UNSATISFIED;
	}
	
	public static String[] getResultNames()
	{
		String[] names = new String[TriggerResult.values().length];
		int i =0;
		for (TriggerResult t:TriggerResult.values())
		{
			names[i]=t.toString();
			i++;
		}
		return names;
	}
}
