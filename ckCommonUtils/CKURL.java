package ckCommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import ckDatabase.CKCampaignNodeFactory;
import ckDatabase.CKConnection;

public class CKURL
{
	private URL url = null;
	private static boolean setAuth=false;
	
	public CKURL(String filename) throws MalformedURLException
	{	//setAuth=true; //TODO get rid of this eventually
		if( ! setAuth )
		{
			Authenticator.setDefault(new MyAuth());
			setAuth=true;
		}
		String base = CKProperties.getValue("RESOURCEPATH");
		if (base == null)
		{//look in the same jar file...
			URL url = this.getClass().getProtectionDomain()
					.getCodeSource().getLocation();
			String jarPath="";
			try
			{
				jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			base ="file:"+jarPath;
			if(base.endsWith(".jar"))
			{
				base="jar:"+base+"!/";
			}
			base+="CK_DATA/CK_DATA/";

		}
		else if(base.compareTo("__BASE_DIR__")==0)
		{
			base = (new File(System.getProperty("user.dir")))
					.getParentFile().toURI().toURL().toString();
			//base = (new File(System.getProperty("user.dir"))).toURI().toURL().toString();
			base+="CK_DATA/CK_DATA/";
		}
		else if (base.compareTo("__DEFAULT_RESOURCE__")==0)
		{
			base = CKConnection.getCKSettingsDirectory().toURI().toURL().toString();
			base = "jar:"+base+"CKResources.jar!/";
		}
		url = new URL(base+filename);
		//System.out.println("URL is: "+url);
	}
	
	public InputStream getInputStream() throws IOException
	{
		//System.out.println(url);
		return url.openStream();
	}
	

	public  OutputStream getOutputStream() throws IOException
	{
		//System.out.println(url);
		String protocol =url.getProtocol(); 
		if( protocol.compareToIgnoreCase("file")==0)
		{  //file URL cannot be written to-convert to fileoutputstream first
			File f = new File(url.getFile());
			f.createNewFile(); //only runs if file does not exist. 
			
			//System.out.println("file: "+url.getFile());
			
			return new FileOutputStream(url.getFile());
		}
		else if( protocol.compareToIgnoreCase("http")==0)
		{
			//System.out.println("http outputstream");
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			uc.setRequestMethod("PUT");
			return uc.getOutputStream();
			
		}
		
		//System.out.println("default outputstream");
		URLConnection uc = url.openConnection();
		uc.setDoOutput(true);
		return uc.getOutputStream();
		
	}
	
	public URL getURL()
	{
		return url;	
	}

	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return url.toString();
	}
	
	public String getFileName()
	{
		String decoded = "";
		try {
			decoded = URLDecoder.decode(getURL().getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decoded;
	}
	
	@Deprecated //use listFiles instead
	public File[] getDirectoryFiles(final String docType)
	{
		File folder = new File(this.getFileName());
		File[] files = folder.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.endsWith(docType);
				}
			});
		if (files==null) { files = new File[0]; } //empty but avoids other code having to check.
		return files;
	}
	
	
	public Stream<String> listFiles()
	{
		String protocol =url.getProtocol(); 
		if( protocol.compareToIgnoreCase("file")==0)
		{  
			File folder = new File(url.getFile());
			return Arrays.stream(folder.listFiles())
			.map(f->f.getName());
		}
		else if (protocol.compareToIgnoreCase("jar")==0)
		{
			try
			{
				JarURLConnection conn = (JarURLConnection) url.openConnection();
				String directory = conn.getEntryName();
				int dirLen = directory.length();
				JarFile jf = conn.getJarFile();
				return jf.stream()
						.filter(e->e.getName().startsWith(directory) &&
								e.getName().length()>dirLen)
					.map(e->e.getName().substring(dirLen));
			} catch (IOException e)
			{
				System.err.println("Unable to look at jar directory: "+url);
				e.printStackTrace();
				return null;
			}
			
			
		}
		else { return null; }
		
	}

	public static void copyURL(CKURL from, CKURL to)
	{
		try
		{
			InputStream in = from.getInputStream();
			OutputStream out = to.getOutputStream();
			
			int bytes=0;
			byte [] data=new byte[2<<14];
			bytes=in.read(data);
			while(bytes!=-1)
			{
				System.out.println("I read "+bytes+" bytes");
				out.write(data, 0, bytes);
				bytes=in.read(data);				
			}
			in.close();
			out.flush();
			out.close();
		
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		
		
	}
	
	
	public static void main(String [] args) throws MalformedURLException
	{
		/*try
		{*/
			//network to file
			//CKProperties.setValue("RESOURCEPATH","jar:http://grim.hanover.edu/CK/CKResources.jar!/");
			//CKProperties.setValue("RESOURCEPATH","__DEFAULT_RESOURCE__");
			//CKURL url = new CKURL("images/babysheet.png");
			//String path = "jar:file:/C:/Users/michael.bradshaw/Downloads/ckGame.jar!/CK_DATA/CK_DATA/"
			//		+ "GAME/CAMPAIGN_NODES/ASSETS/";
			
			String path = "jar:file:/C:/Users/michael.bradshaw/Downloads/ckGame.jar!/CK_DATA/CK_DATA/";

			CKProperties.setValue("RESOURCEPATH",path);
			
			
			//CKURL url = new CKURL("");
			//url.listFiles().forEachOrdered(e->System.out.println("file:"+e));
			
			
			CKCampaignNodeFactory.getInstance().getAllAssetsVectored();
			
			
/*			CKURL url = new CKURL("asset1980675959004506348.xml");
	*/
			/*
			System.out.println(url);
//			CKConnection.updateLocalFile(new File(CKConnection.getCKSettingsDirectory() ,"baby.png"),
	//				url.getURL());
			System.out.println("done");
		
			InputStream in = url.getInputStream();
			
			byte[] buffer = new byte[1024];
			int len = in.read(buffer);
			while (len != -1) {
			    System.out.write(buffer, 0, len);
			    len = in.read(buffer);
			}
			*/
			//file to network
			//file one
			/*
			CKProperties.setValue("RESOURCEPATH","file:");
			CKURL local = new CKURL(CKConnection.getCKSettingsDirectory()+File.separator+"bradshaw.jpg");
			System.out.println("local:"+local);
			
			CKProperties.setValue("RESOURCEPATH","ftp://cyberknight:resources@grim.hanover.edu/images/");
			CKURL network = new CKURL("bradshaw2.jpg");
			System.out.println("network:"+network);
			copyURL(local,network);
		*/
			
			//test it
			/*System.out.println("test http outputstream");
			HttpURLConnection uc;
			try
			{
				uc = (HttpURLConnection) network.getURL().openConnection();
			
			uc.setDoOutput(true);
			uc.setRequestMethod("PUT");
			OutputStream writer = uc.getOutputStream();
			writer.write("please work out".getBytes());
			writer.close();
			int code = uc.getResponseCode();
			System.out.println("Code is "+code+" protocol "+uc.getRequestMethod());
			/*InputStream in = uc.getInputStream();
			byte [] data = new byte[2<<12];
			int bytes = in.read(data);
			while(bytes != -1)
			{
				for(int i=0;i<bytes;i++)
				{
					System.out.print(data[i]);
				}
				
			}
			in.close();
			*/
			//uc.disconnect();
			
			//configure the server!! http://www.php.net/manual/en/features.file-upload.put-method.php
			/*} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			
		/*	
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		*/
		
		
		
		
	}
	
	
	
	
	

}

class MyAuth extends Authenticator
{

	/* (non-Javadoc)
	 * @see java.net.Authenticator#getPasswordAuthentication()
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		System.out.println("Authentication occurs"+ CKProperties.getValue("USERNAME")+
				CKProperties.getValue("PASSWORD"));
		return new PasswordAuthentication(CKProperties.getValue("USERNAME"),
				CKProperties.getValue("PASSWORD").toCharArray());
	}	
}








