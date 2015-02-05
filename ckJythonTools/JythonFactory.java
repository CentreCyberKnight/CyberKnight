package ckJythonTools;

import java.io.IOException;
import java.net.URL;

import org.python.util.PythonInterpreter;

/*
 * Code was shamelessly taken from the web
 * 
 * http://wiki.python.org/jython/JythonMonthly/Articles/September2006/1
 * 
 * author - Josh Juneau
 * 
 * 
 * This code enables us to create Java objects from code written in Python
 */

public class JythonFactory
{
	   private static JythonFactory instance = null;
	    
	   private JythonFactory(){};
	   
	   public synchronized static JythonFactory getInstance(){
	        if(instance == null){
	            instance = new JythonFactory();
	        }
	        
	        return instance;
	              
	    }
	    	
	
	
	   public static Object getJythonObject(String interfaceName,
               String pathToJythonModule){
		   
		   return JythonFactory.getJythonObject(interfaceName, pathToJythonModule,null);
	   }
	   
	   public static Object getJythonObjectFromResource(String interfaceName,
               String pathToJythonModule)
	   {
		
		   	//TODO make this more robust
			URL url=getInstance().getClass().getResource("/"+pathToJythonModule);
			
		   
		   
		   return JythonFactory.getJythonObject(interfaceName, pathToJythonModule,url);
	   }
	   
	  private static Object getJythonObject(String interfaceName,
               String pathToJythonModule,URL url){
	       //first run the code that holds the object       
	       Object javaInt = null;
	       PythonInterpreter interpreter = new PythonInterpreter();
	       
	       if(url!=null)
	       {
	    	   try {
				interpreter.execfile(url.openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
	       else
	       {
	    	   interpreter.execfile(pathToJythonModule);
	    	   
	       }
	       //interpreter.execfile(filename);
	       
	       //here we run the code to create an object
	       //WARNING class name must be same as the file name
	       String tempName = pathToJythonModule.substring(pathToJythonModule.lastIndexOf("/")+1);
	       tempName = tempName.substring(0, tempName.indexOf("."));
	       System.out.println(tempName);
	       String instanceName = tempName.toLowerCase();
	       String javaClassName = tempName.substring(0,1).toUpperCase() +
	                           tempName.substring(1);
	       String objectDef = "=" + javaClassName + "()";
	       //here we get the object out of the interpreter
	       interpreter.exec(instanceName + objectDef);
	        try {
	           @SuppressWarnings("rawtypes")
			Class JavaInterface = Class.forName(interfaceName);
	           javaInt = 
	                interpreter.get(instanceName).__tojava__(JavaInterface);
	        } catch (ClassNotFoundException ex) {
	      
	        
	            ex.printStackTrace();  // Add logging here
	        }
	       return javaInt;
	   }
	}

