package ckPipeline;
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Write {
	
	private String base;
	private ArrayList<Path> chars;
	private ArrayList<TxtFile> txts;
	
	
	public Write(String bass){
		base=bass;
		chars=new ArrayList<Path>();
		getFolders();
		doCharacters();
		
		/*
		getFiles();
		for (TxtFile txt: txts) 
		{
    		System.out.println("Write class's doCharater and getFiles functions: " + txt.getName());
	    }
		*/
	}
	public void getFolders(){
		Path dir = Paths.get(base+"\\Output");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    for (Path file: stream) {
		    	if(file.toFile().isDirectory()){
		    		chars.add(file);
		    		
		    		/*
		    		System.out.println("Write class's getFolders function: " + file.toString()+"\\");
		    		*/
		    	}}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}	
	}
	public void doCharacters(){
		txts=new ArrayList<TxtFile>();
		for(Path ch:chars){
			TxtFile t=new TxtFile(ch,ch.getParent());
			txts.add(t);
		}
	}
	public ArrayList<TxtFile> getFiles(){
		return txts;
	}

	public static void main(String[] args){
		//Write w=new Write();
	}
}
