package ckSatisfies;

public enum NumericalCostType {

	
	EQ("EQUALS"),
	NEQ("NOT_EQUALS"), 	
	GT("GREATER_THAN"), 
	GTE("GREATER_THAN_EQUAL"), 
	LT("LESS_THAN"), 
	LTE("LESS_THAN_EQUAL"), 
	TRUE("RETURN_TRUE"),
	FALSE("RETURN_FALSE");
	
	
	
	private final String resultName;
		
	
	NumericalCostType(String name)
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
	 * gets the NumericalCostType that has the string representation of name, FALSE otherwise
	 * @param name string representing the triggerResult
	 * @return triggerResult for the string
	 */
	static public NumericalCostType getTypes(String name)
	{
		for (NumericalCostType t:NumericalCostType.values())
		{
			if(t.toString().equals(name))
			{
				return t;
			}
		}
		return NumericalCostType.FALSE;
	}
	
	public static String[] getResultNames()
	{
		String[] names = new String[NumericalCostType.values().length];
		int i =0;
		for (NumericalCostType t:NumericalCostType.values())
		{
			names[i]=t.toString();
			i++;
		}
		return names;
	}
	
	final public static boolean evaluate(float a,NumericalCostType t ,float b)
	{
		switch(t)
		{
		case EQ:
			return a == b;
		case NEQ:
			return a != b;
		case GT:
			return a > b;
		case GTE:
			return a>=b;
		case LT:
			return a<b;
		case LTE:
			return a<=b;
		case TRUE:
			return true;
		case FALSE:
			return false;
				
		}		
		return false;
	}
	
}
