package ckPythonInterpreter;


import ckJythonTools.JythonFactory;
import ckPythonInterpreter.CKPythonEditorPane;

public class CKUniqueEditor
{
	private static CKPythonEditorPane editor;
		// Note that the constructor is private
		private CKUniqueEditor() { }
		
		public static CKPythonEditorPane getUniqueEditor()
		{
			if (editor == null)
			{
				editor = new CKPythonEditorPane();
			}
			return editor;
		}
	
		private static CKPythonDebuggerInterface debug;
		
		
		public static void storeInitializedDebugger(CKPythonDebuggerInterface d)
		{
			debug=d;			
		}
		
		public static CKPythonDebuggerInterface getInitializedDebugger()
		{
			return debug;
		}
		
		
		public static CKPythonDebuggerInterface getDebuggerInstance()
		{
			
			String path = "ckPythonInterpreter/CKPythonDebugger.py";
			/*
			System.out.println("path name is "+path);
			URL filename=getUniqueEditor().getClass().getResource("/"+path);
			//System.out.println("unique location"+getUniqueEditor().getClass().getResource(path));
			System.out.println("filname is"+filename);
			//filename=path;
			*/
			CKPythonDebuggerInterface face=(CKPythonDebuggerInterface) 
			            JythonFactory.getJythonObjectFromResource("ckPythonInterpreter.CKPythonDebuggerInterface", 
	                               path);
		
			return face;	
		}
		
}
