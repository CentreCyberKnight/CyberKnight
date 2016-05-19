package ckPythonInterpreterTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
// File: SimpleEmbedded.java
import org.python.util.PythonInterpreter;

public class CKJthonTest1 
{

	public static void main(String[] args) throws PyException, IOException
		    {
		        BufferedReader terminal;
		        PythonInterpreter interp;
		        terminal = new BufferedReader(new InputStreamReader(System.in));
		        System.out.println ("Hello");
		        interp = new PythonInterpreter();
		        interp.exec("import sys");
		        interp.exec("print sys");
		        interp.set("a", new PyInteger(42));
		        interp.exec("print a");
		        interp.exec("x = 2+2");
		        PyObject x = interp.get("x");
		        System.out.println("x: " + x);
		        PyObject localvars = interp.getLocals();
		        interp.set("localvars", localvars);
		        String codeString = "";
		        String prompt = ">> ";
		        while (true)
		        {
		            System.out.print (prompt);
		            try
		            {
		                codeString = terminal.readLine();
		                if (codeString.equals("exit"))
		                {
		                    System.exit(0);
		                    break;
		                }
		                interp.exec(codeString);
		            }
		            catch (IOException e)
		            {
		                e.printStackTrace();
		            }
		        }
		        System.out.println("Goodbye");
		    }
		}
