package ckPipeline;
import ckGraphicsEngine.assets.CKSpritesheetAsset;
import java.util.*;
import java.io.*;
import ckCommonUtils.*;
import java.nio.file.*;
public class Pipeline {
	public static void main(String[] args){
		String bass=CKProperties.getValue("Pipeline_Path");
		String daz=CKProperties.getValue("DAZ_Path");
		Runtime rt = Runtime.getRuntime();
		ArrayList<String> chars=new ArrayList<String>();
		File f=new File(bass,"Characters");
		String base=f.getAbsolutePath();
		Path dir = Paths.get(base);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    for (Path file: stream) {
		    	chars.add(file.getFileName().toString());
		    	//System.out.println(file.toString()+"\\");
			}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		for(int z=0;z<chars.size();z++){
			WriteScript makeScript=new WriteScript(chars.get(z),bass);
		}
		
		for(int zz=0;zz<chars.size();zz++){
			String ch=chars.get(zz).substring(0,chars.get(zz).length()-4)+".dsa";
			File ab=new File(bass,"Scripts/"+ch);
			String absol=ab.getAbsolutePath();
			String inp=daz+" "+absol;
			try{
				Process pr=rt.exec(inp);
				try{
					int n=pr.waitFor();
				}
				catch(InterruptedException i){
				}
			}
			catch(IOException r){
				System.err.println(r);
				//System.out.println("DAZ");
			}
		}
		Write w=new Write(bass);
		ArrayList<TxtFile> files=w.getFiles();
		for(TxtFile fi:files){
			//System.out.println(f.getFolder());
			File fil=new File(fi.getFolder().toFile(),"done.txt");
			//File filen=new File(fi.getFolder().toFile(),"mobster.txt");
			if(!fil.exists()){
				try{
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
