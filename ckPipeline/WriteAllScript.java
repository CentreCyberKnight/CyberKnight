package ckPipeline;

import java.util.*;
import java.io.*;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class WriteAllScript {
	private ArrayList<String> characters;
	private String dir,base;
	private ArrayList<String> defs=new ArrayList<String>();	//the default actions
	private ArrayList<Action> actions;
	private String output;
	private String[] directions;
	
	public WriteAllScript(ArrayList<String> chars, String bass){
		//This writes the DAZscript for rendering all actions of a single character
		characters = chars;
		for (int ch = 0; ch < chars.size(); ch ++)
		{
			String name = chars.get(ch);
			characters.set(ch, name.substring(0,name.length()-4));
		}
		
		dir="Scripts";
		base=bass;
		base=base.replace("\\", "\\\\");
		actions=new ArrayList<Action>();
		directions=new String[]{"'LF'","'RF'","'LB'","'RB'"};
		getDefaults();
		
		output="var scrip=new DzScript() \n";
		output=output+"scrip.loadFromFile('"+base+"Scripts\\\\myScript.dsa') \n";
		for (int ch = 0; ch < characters.size(); ch ++)
		{
			String name = characters.get(ch);
			getCharacter(name);
		}
		output = output + "scrip.call('quitDaz',[''])";
		
		save();
	}
	public void getDefaults(){
		//This gets the default actions and their files
		Boolean file=true;
		Path direc = Paths.get(base);
		Path p=direc;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(direc)) {
		    for (Path fil: stream) {
		    	if(fil.toFile().isDirectory() && fil.getFileName().toString().equals("Texts")){
		    		p=fil;
		    		//
		    		System.out.println(fil.getFileName().toString());
		    		//
		    	}}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}	
		try{
			Scanner scan=new Scanner(new FileReader(new File(p.toFile(),"default.txt")));
			while(scan.hasNextLine()){
				defs.add(scan.nextLine());
			}
			scan.close();
			file=true;
		}
		catch(FileNotFoundException e){
			file=false;
			//
			System.out.println("FILENOTFOUND");
			//
		}
		if (file){
			//This adds the defaults
			for (int x=0;x<defs.size();x++){
				String act=defs.get(x);
				String[] acts=act.split(";");
				Action action=new Action(acts[0],acts[1]);
				actions.add(action);
				
				//
				System.out.println("Action name: " + action.getName() + ": " + acts[0] + " " + acts[1]);
				//
			}
		}
	}
	public void getCharacter(String character){
		//This gets the file for the character itself, and if there are any actions that have different files here, they are changed
		//This calls write to update the output
		Boolean file=true;
		ArrayList<String> chDefs=defs;
		ArrayList<Action> chActions = actions;
		Path direc = Paths.get(base);
		Path p=direc;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(direc)) {
		    for (Path fil: stream) {
		    	if(fil.toFile().isDirectory() && fil.getFileName().toString().equals("Texts")){
		    		p=fil;
		    		//System.out.println(file.getFileName().toString());
		    	}}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}	
		try{
			//This tries to scan the default file, if there is none it returns false
			Scanner scan=new Scanner(new FileReader(new File(p.toFile(),character+".txt")));
			while(scan.hasNextLine()){
				chDefs.add(scan.nextLine());
			}
			scan.close();
			file=true;
		}
		catch(FileNotFoundException e){
			file=false;
			//System.out.println("FILENOTFOUND");
		}
		if (file){
			//If there is a file, this adds the already set defaults into the menu correctly
			for (int x=0;x<chDefs.size();x++){
				String act=chDefs.get(x);
				String[] acts=act.split(";");
				if (hasAction(chActions, acts[0])){
					Action actionen=findAction(chActions, acts[0]);
					actionen.setFile(acts[1]);
				}
				//System.out.println(action.getCharacter());
				//System.out.println(action.getFile());
			}
		}
		
		write(chActions, character);
	}
	public boolean hasAction(ArrayList<Action> chActions, String namen){
		//This makes sure that there is an action by this name
		for (Action act:chActions){
			if(act.getName().equals(namen)){
				return true;
			}
		}
		return false;
	}
	public Action findAction(ArrayList<Action> chActions, String namen){
		//This gives the action by that name, or null if there is no such action
		for (Action act:chActions){
			if(act.getName().equals(namen)){
				return act;
			}
		}
		return null;
	}
	public void write(ArrayList<Action> chActions, String namen){
		//This updates the output with given character's actions
		//This saves what needs to be written to the script in one string
		for(Action act:chActions){
			//for(String d:directions){
				//String line="scrip.call('loadPreset',['"+namen+"', '"+act.getName()+"', '"+act.getFile()+"', "+d+", '"+base+"']) \n";
				String line="scrip.call('loadPreset',['"+namen+"', '"+act.getName()+"', '"+act.getFile()+"', 'LF', '"+base+"']) \n";	
				output=output+line;
			//}
		}
	}
	public void save(){
		//This actually writes the text of the code to the .dsa file
		try {
			 
 			File file = new File(base+"/"+dir,"allScript.dsa");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(output);
			bw.close();
 
			//System.out.println("Done");
 
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		//This just tests it with bleh, a random made up character
		File f=new File("Characters");
		String base=f.getAbsolutePath();
		Path direc = Paths.get(base);
		ArrayList<String> chars=new ArrayList<String>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(direc)) {
		    for (Path file: stream) {
		    	if(file.toFile().toString().endsWith(".duf")){
		    		chars.add(file.getFileName().toString());
		    		//System.out.println(file.toString()+"\\");
		    	}}
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		}
		for (String ch:chars){
			//WriteScript w=new WriteScript(ch);
		}
	}
}
