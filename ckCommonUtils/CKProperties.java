package ckCommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import ckDatabase.CKConnection;

public class CKProperties
{

	private static Properties prop=null;
	private static String baseFile = "default.config";
	private static String clientFile= CKConnection.getCKSettingsDirectory()+File.separator+"CK.config";
	
	private static synchronized void storeProperties(Properties p,String location)
	{
		try
		{
			p.store(new FileOutputStream(location),"CyberKnight Configuration File");
		} catch (FileNotFoundException e)
		{
			
			e.printStackTrace();
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
		
	}
	
	private static synchronized void getProperties()
	{
		if (prop == null)
		{
			//first get default
			//eventually this will be described within a jar file...
			Properties base= new Properties();
			try
			{
				base.load(new FileInputStream(baseFile));
			} catch (FileNotFoundException e)
			{
				storeProperties(base,baseFile);
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			
			//then get clients
			prop = new Properties(base);
			
			try
			{
				prop.load(new FileInputStream(clientFile));
			} catch (FileNotFoundException e)
			{
				storeProperties(prop,clientFile);
				System.out.println("creating client CK configuration file");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String getValue(String key)
	{
		getProperties();
		return prop.getProperty(key);
	}
	
	public static void setValue(String key,String value)
	{
		getProperties();
		prop.setProperty(key, value);
	}
	
	public static void storeProperties()
	{
		storeProperties(prop,clientFile);
	}
	
	public static void main(String [] args)
	{
		//getProperties();
		System.out.println(getValue("RESOURCESERVER"));
		//setValue("ROSE","RED");
		System.out.println(getValue("ROSE"));
		System.out.println(clientFile);
		
		
		//	URL L = new URL("file","", baseFile);
		
		
	}
	
	
}
