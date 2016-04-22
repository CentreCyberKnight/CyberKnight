package ckPythonInterpreter;

import ckJythonTools.JythonFactory;


public class CKUniqueAlgViz
{
	private static CKAlgViz algviz;
	private static CKPythonState pythonState;
	
		// Note that the constructor is private
		private CKUniqueAlgViz() { }
		
		public static CKAlgViz getUniqueAlgViz(int width)
		{
			if (algviz == null)
			{
				algviz = new CKAlgViz(width);
			}
			return algviz;
		}
	
		public static CKPythonState getUniquePythonState()
		{
			if(pythonState==null)
			{
				
				String path = "ckPythonInterpreter/CKPythonStateDecider.py";
				//System.out.println("path name is "+path);
				//URL filename=getUniqueAlgViz(400).getClass().getResource("/"+path);
				
				//JythonFactory jf = JythonFactory.getInstance();
				pythonState=(CKPythonState) 
				            JythonFactory.getJythonObjectFromResource("ckPythonInterpreter.CKPythonState", 
				            		path);
				            		//"ckPythonInterpreter/CKPythonStateDecider.py");
			
			}
			return pythonState;
		}
		
}
