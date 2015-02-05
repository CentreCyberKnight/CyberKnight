package ckPythonInterpreterTest;

import ckPythonInterpreter.CKPythonSyntaxParser;

public class CKPythonCSDTest 

{

	public static void runTest(String s)
	{
		CKPythonSyntaxParser parser = new CKPythonSyntaxParser();
		System.out.printf("%s",s);
		parser.printCSDStack(parser.ParseCSD(s));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String t1 = "def fcn(n):\n  print \"Hello\"";
		runTest(t1);
		
		String t2 = "def fcn(n):\n   if(n<2):\n      return 1\n";
		t2+="   return fcn(n-1)+fcn(n-2)\n";
		runTest(t2);
	
		String t3="def perm(l):\n";
		t3+="       # Compute the list of all permutations of l\n";
		t3+="   if len(l) <= 1:\n";
		t3+="                 return [l]\n";
		t3+="   r = []\n";
		t3+="   for i in range(len(l)):\n";
		t3+="         s = l[:i] + l[i+1:]\n";
		t3+="         p = perm(s)\n";
		t3+="         for x in p:\n";
		t3+="          r.append(l[i:i+1] + x)\n";
		t3+="   return r\n";
		
		runTest(t3);
		


		
		
	 }

}
