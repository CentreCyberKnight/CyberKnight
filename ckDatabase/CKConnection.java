package ckDatabase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.jar.JarFile;

import ckCommonUtils.CKProperties;

public class CKConnection
{

	private static boolean checked=false; 


	public static Connection getConnection(String db) 
	{
		updateCKResources();
	        try
			{
				Class.forName("org.h2.Driver");
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}


        	try
			{
			 if(db.compareTo("__DEFAULT_DB__")==0)
		        {
					db = CKConnection.getCKSettingsDirectory().toURI().toURL().toString();
					db = db+"cyberknight";
		        }
			} catch (MalformedURLException e)
			{
				
				e.printStackTrace();
			}
        	

			Connection conn;		        
	        
			try
			{
				
				conn = DriverManager.getConnection("jdbc:h2:"+db, "sa", "");
			} catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
	       return conn;
	        
	}
	
	public static Connection getConnection()
	{
		//return getConnection("~/test");
		return getConnection(CKProperties.getValue("GAMEDBPATH"));
	}
	
	
	public static void updateLocalFile(File local,URL resource)
	{
		updateLocalFile(local,resource,false);
	}
	
	public static void updateLocalFile(File local,URL resource,boolean forceupdate)
	{

		try
		{//look at local file
			long modified = local.lastModified();
			//setup if modified on connection
			URLConnection remote = resource.openConnection();
			remote.setIfModifiedSince(modified);		
			remote.connect();
			
			if(remote.getLastModified()>modified || forceupdate
			||! local.exists())  //then read in the file from the URL
			{
			    int length = remote.getContentLength();
			    if (length == -1) {   throw new IOException("Remote file is empty. aborting"); }
			    InputStream in = new BufferedInputStream(remote.getInputStream());
			    
			    byte[] data = new byte[length];
			    int offset = 0;
			    while (offset < length)
			    {
			      int bytesRead =  in.read(data, offset, data.length - offset);
			      if (bytesRead == -1)
			        { break; }
			      offset += bytesRead;
			    }
			    in.close();
			    
			    if (offset != length)
			    {  throw new IOException("Only read " + offset + " bytes; Expected " + length + " bytes");}

			    FileOutputStream out = new FileOutputStream(local);
			    out.write(data);
			    out.flush();
			    out.close();
				
			}
			else
			{
				System.out.println("Local file:"+local + " is uptodate, no need to download");
			}
		
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		
		
	}
	
	 
	
	public final static String productionDB = "cyberknight.h2.db";
	public final static String productionAssets = "CKResources.jar";
	public final static String repositoryURL = "http:vault.hanover.edu/~bradshaw/CK/";
	
	
	/**
	 * checks for updated CK resources and downloads them if they are newer than the local copies
	 * Presently doing each file separately, might want to do the entire directory in one go? 
	 */
	public static void updateCKResources()
	{
		if(checked) { return; }
		checked = true;
		String update = CKProperties.getValue("UPDATE");
		if(update.compareToIgnoreCase("NEVER")==0) { return; }
		
		boolean forceupdate =false; 
		if(update.compareToIgnoreCase("ALWAYS")==0) { forceupdate =true; }
		
		String repositoryURL = CKProperties.getValue("REPOSITORY");
		try
		{
			updateLocalFile(new File (getCKSettingsDirectory() ,productionDB),
					new URL(repositoryURL+productionDB) ,forceupdate);
			updateLocalFile(new File (getCKSettingsDirectory() ,productionAssets),
				new URL(repositoryURL+productionAssets),forceupdate );
		} catch (MalformedURLException e)
		{
			System.err.println("Unable to check for new CK Resources");
		}
	}
	
private final static String settingsDir = ".cyberknight";

	public static File getCKResourceDirectory()
	{
		return new File(getCKSettingsDirectory(),"resources");
		
	}
	public static File getCKSettingsDirectory() 
	{
	    String userHome = System.getProperty("user.home");
	    if(userHome == null)     {  throw new IllegalStateException("user.home==null");    }
	    
	    File settingsDirectory = new File(userHome,settingsDir);
	    if(!settingsDirectory.exists()) 
	    {
	        if(!settingsDirectory.mkdir()) 
	        {
	            throw new IllegalStateException(settingsDirectory.toString());
	        }
	    }
	    return settingsDirectory;
	}
	
	
	public static Connection getProductionConnection()
	{
		updateCKResources();
			
		return getConnection("\\~"+settingsDir+"\\"+productionDB);		
	}
	
	public static void printResultSet(ResultSet set) throws SQLException
	{
		ResultSetMetaData md = set.getMetaData();
		int count = md.getColumnCount();
		for (int i=1; i<=count; i++)
		{
			System.out.print(md.getColumnLabel(i)+" ");
	    }
		System.out.print("\n");
		while (set.next())
		{
			for (int i=1; i<=count; i++) 
			{
				System.out.print(set.getString(i)+" ");
			}
			System.out.print("\n");
		}
			
	}

	public static InputStream getJarFileInput(String jar, String file) throws IOException
	{
		
			JarFile jarFile = new JarFile(jar);
			InputStream in = jarFile.getInputStream( jarFile.getEntry(file));
			return in;
		

	}
	
	
	
	
	public static void main(String[] args)
	{
	/*
	try
	{
				
		updateLocalFile(new File (getCKSettingsDirectory() ,"bradshaw.jpg"),
									new URL("http://vault.hanover.edu/~bradshaw/bradshawm.jpg") );
	} catch (MalformedURLException e)
	{
		e.printStackTrace();
	}	
		
	}*/
		Connection conn = CKConnection.getConnection("tcp://grim.hanover.edu/cyberknight");
		
		//create
		String q="DROP TABLE IF EXISTS TEST;" +
				" CREATE TABLE TEST(id INTEGER,name VARCHAR(255));"+
		"INSERT INTO TEST VALUES(1,'alice');"+
		"INSERT INTO TEST VALUES(2,'bob');"+
		"INSERT INTO TEST VALUES(3,'cathy');";
			
		
		Statement stmt=null;;
		try
		{
			stmt = conn.createStatement();
		} catch (SQLException e)
		{
		
			e.printStackTrace();
			return;
		}
		//first we write
		try
		{
			stmt.executeUpdate(q);
			String q2 = "SELECT * FROM TEST;";
			ResultSet set=stmt.executeQuery(q2);
			while(set.next())
			{
				int id = set.getInt("id");
				String name = set.getString("name");
				System.out.println("Line id"+id+" name is"+name);
			}
		
		} catch (SQLException e)
		{
			
			e.printStackTrace();
			return;
		}
		
		System.out.println("All clear!");
		
	}
	
	
}
