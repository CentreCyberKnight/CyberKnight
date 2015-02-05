package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import ckDatabase.CKQuestTestFactory;
import ckEditor.treegui.CKQuestTest;

@RunWith(Parameterized.class)
public class TestQuestCases
{

	CKQuestTest test;
	
	public TestQuestCases(CKQuestTest t,String name,String aid)
	{
		test = t;
	}
	
	
	@Parameters(name= "{index}: {1} in {2}")
	public static Collection<Object[]> data()
	{
		
		ArrayList<Object[]> out = new ArrayList<Object[]>();
		for(CKQuestTest t:CKQuestTestFactory.getInstance().getAllAssetsVectored())
		{
			Object [] a = {t,t.getName(),t.getAID()};
			out.add(a);
		}
		return out;

	}
	
	
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test()
	{
		System.err.println(test.getName());
		test.runBackgroundTest();
		//test.runVewingTest();
		//fail("Not yet implemented");
	}

}
