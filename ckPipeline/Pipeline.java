package ckPipeline;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ckCommonUtils.CKProperties;
import ckGraphicsEngine.assets.CKSpritesheetAsset;
public class Pipeline {
	public static void main(String[] args){
		String bass=CKProperties.getValue("Pipeline_Path");
		String daz=CKProperties.getValue("DAZ_Path");
		Runtime rt = Runtime.getRuntime();
		
		//store all the characters in a list
		ArrayList<String> chars=new ArrayList<String>();
		File f=new File(bass,"Characters");
		System.out.println(daz);
		String base=f.getAbsolutePath();
		//base=base.replace("\\", "\\\\");
		Path dir = Paths.get(base);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    String name;
			for (Path file: stream) {
		    	name = file.getFileName().toString();
		    	if (name.contains(".duf"))
		    	{
		    		chars.add(file.getFileName().toString());
		    		//System.out.println("added");
		    	}
		    	/*
		    	System.out.println("Character path: ");
		    	System.out.println(name.contains(".duf"));
		    	System.out.println(file.getFileName().toString());
		    	System.out.println(file.toString()+"\\");
		    	*/
		    }
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		
		//
		for (String name: chars)
		{
			System.out.println(name);
		}
		System.out.println("number of character: " + chars.size());
		//
		
		//write a script for all the characters
		/*for(int z=0;z<chars.size();z++){
			WriteScript makeScript=new WriteScript(chars.get(z),bass);
		}*/
		
		//write the scripts of all the characters and actions in one script file
		//WriteAllScript makeScript=
		new WriteAllScript(chars,bass);
		
		//run the scripts for all the characters
		/*for(int zz=0;zz<chars.size();zz++){
			String ch=chars.get(zz).substring(0,chars.get(zz).length()-4)+".dsa";
			File ab=new File(bass,"Scripts/"+ch);
			String absol=ab.getAbsolutePath();
			String inp=daz+" "+absol;
			System.out.println("inp: " + inp);
			try{
				Process pr=rt.exec(inp);
				try{
					int n=pr.waitFor();
				}
				catch(InterruptedException i){
					System.err.println(i);
				}
				
				//
				System.out.println("end of rendering");
				//
				
			}
			catch(IOException r){
				System.err.println(r);
				System.out.println("DAZ");
			}
		}*/
		
		//run the script of all the characters and actions in one script file
		File ab=new File(bass,"Scripts/allScript.dsa");
		String absol=ab.getAbsolutePath();
		String inp=daz+" "+absol;
		System.out.println("inp: " + inp);
		try{
			Process pr=rt.exec(inp);
			try{
				pr.waitFor();
			}
			catch(InterruptedException i){
				System.err.println(i);
			}
			
			//
			System.out.println("end of rendering");
			//
			
		}
		catch(IOException r){
			System.err.println(r);
			System.out.println("DAZ");
		}
		
		//write the text file for Chad's codes
		
		//
		System.out.println("Start the spritesheet");
		//
		
		Write w=new Write(bass);
		ArrayList<TxtFile> files=w.getFiles();
		for(TxtFile fi:files){
			//System.out.println(f.getFolder());
			
			//Create an empty file called "done"
			File fil=new File(fi.getFolder().toFile(),"done.txt");
			
			//File filen=new File(fi.getFolder().toFile(),"mobster.txt");
			if(!fil.exists()){
				try{
					
					//
					System.out.println("parameter of the CKSpritesheetAsset in Pipeline class" + fi.getName());
					//
					
					CKSpritesheetAsset t=new CKSpritesheetAsset(fi.getName());
					t.pipeline();
				}
				catch(FileNotFoundException e){
					System.out.println("FILENOTFOUND");
				}
				try{
					fil.createNewFile();
				}
				catch(IOException e){
				}
			}
		}
	}
}
