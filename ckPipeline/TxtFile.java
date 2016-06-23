package ckPipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class TxtFile {
	private int width,height,fps,numFrames;
	private ArrayList<String> actions,actions2;
	private String name,outp,bass;
	private Path base,folder;
	private String[] directionen;
	
	public TxtFile(Path p,Path bas){
		setBasics();
		actions2=new ArrayList<String>();
		directionen=new String[]{"NW","SE","NE","SW"};
		base=bas;
		folder=p;
		bass=base.toString().replace("\\","/");
		
		outp=p.getFileName().toString();
		outp=outp+";"+bass+";";
		
		actions=actions(p);
		numFrames=numFrames(p);
		
		outp=outp+numFrames+";";
		outp=outp+width+";"+height+";"+fps+"@";
		
		for(int y=0;y<actions2.size();y++){
			outp=outp+actions2.get(y);
			//System.out.println(actions2.get(y));
		}
		outp=outp.substring(0,outp.length()-1);
		File output=new File(p.toFile(),p.getFileName().toString()+".txt");
		try{
			PrintWriter fw=new PrintWriter(new FileOutputStream(output,false));
			fw.write(outp);
			fw.close();
		}	
		catch(FileNotFoundException e){
		}
		name=output.getAbsolutePath().replace("\\", "/");
	}
	public void setBasics(){
		height=96;
		width=48;
		fps=10;
	}
	public ArrayList<String> actions(Path p){
		Path txts=p;
		Path bass=p.getParent().getParent();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(bass)) {
		    for (Path file: stream) {
		    	if(file.toFile().isDirectory()&&file.getFileName().toString().equals("Texts")){
		    		txts=file;
		    		//System.out.println("YES");
		    		//System.out.println(file.toString());
		    	}}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		ArrayList<String> defs=new ArrayList<String>();
		try{
			//This tries to scan the default file, if there is none it returns false
			Scanner scan=new Scanner(new FileReader(new File(txts.toFile(),"default.txt")));
			while(scan.hasNextLine()){
				//This saves each line in a string to be read later
				defs.add(scan.nextLine());
			}
			scan.close();
		}
		catch(FileNotFoundException e){
			System.out.println("FILENOTFOUND");
		}
		ArrayList<String> out=new ArrayList<String>();
		for (int x=0;x<defs.size();x++){
			//This goes through all the lines in the default, and creates actions from them
			String act=defs.get(x);
			String[] acts=act.split(";");
			String actSet="";
			for(String direct:directionen){
				actSet=acts[0]+"_"+direct+",";
				out.add(actSet);
			}

		}
		return out;
	}
	public String getName(){
		return name;
	}
	public Path getBase(){
		return base;
	}
	public Path getFolder(){
		return folder;
	}
	public int numFrames(Path p){
		File dir=p.toFile();
		String[] tot = dir.list(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".png");
		    }
		});
		for(String act:actions){
			//System.out.println(act);
			int acc=0;
			for(int f=0;f<tot.length;f++){
				if(tot[f].contains(act.substring(0,act.length()-1))){
					acc++;
				}
			}
			act=act+acc+";";
			actions2.add(act);
		}
		return tot.length;
	}

}
