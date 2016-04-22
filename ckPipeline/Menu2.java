package ckPipeline;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import ckCommonUtils.CKProperties;
public class Menu2 extends JPanel {
	private ArrayList<Actionen> actions;
	private GridBagLayout layout;
	private String[] bvhs;
	private String charN, base;
	private GridBagConstraints actCons;
	public Menu2(String charName){
		//This is the Menu which holds all of the actions in a list
		base=CKProperties.getValue("Pipeline_Path");
		actions= new ArrayList<Actionen>();
		charN=charName.substring(0, charName.length()-4);
		setup();
	}
	public void setup(){
		//This sets up the menu
		//This is setting the base constraints for the actions
		actCons=new GridBagConstraints();
		actCons.weightx=1;
		actCons.gridx=0;
		actCons.anchor=GridBagConstraints.LINE_START;
		actCons.fill=GridBagConstraints.HORIZONTAL;
		layout=new GridBagLayout();
		this.setLayout(layout);
		getBVHs();
		checkDef();
		checkFile();
		//showItems();
		//System.out.println(actions.get(1).isVisible());
	}
	public void getBVHs(){
		//This gets all the possible actions from the directory they are in
		//This makes a directory object with the same name as the folder where the actions are
		File dir=new File(base,"Actions");
		//This makes an array of Strings which are all possible actions
		bvhs = dir.list(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        //This only returns files with the correct suffix
		    	return fileName.endsWith(".gfa");
		    }
		});
	}
	public void checkDef(){
		//This checks the default file to get the list of actions
		Boolean file=true;
		File f=new File(base,"Texts");
		//This is a list of all actions in the default
		ArrayList<String> defs=new ArrayList<String>();
		try{
			//This tries to scan the default file, if there is none it returns false
			Scanner scan=new Scanner(new FileReader(new File(f,"default.txt")));
			while(scan.hasNextLine()){
				//For each line in the default, this adds a string to the above list
				defs.add(scan.nextLine());
			}
			scan.close();
			//This says there was a file
			file=true;
		}
		catch(FileNotFoundException e){
			//If there is no default, nothing is added in
			file=false;
			System.out.println("FILENOTFOUND");
		}
		if (file){
			//If there is a file, this adds the already set defaults into the menu correctly
			for (int x=0;x<defs.size();x++){
				String act=defs.get(x);
				String[] acts=act.split(";");
				//This Creates an action with the given name, file and options
				Actionen action=new Actionen(acts[0],acts[1],bvhs);
				//This adds the action to the list of actions in the menu
				addAction(action);
				//System.out.println(action.getCharacter());
				//System.out.println(action.getFile());
			}
		}
	}
	public void checkFile(){
		//This checks if there is already a file written for this character, and gets info if there is
		Boolean file=true;
		File f=new File(base,"Texts");
		//This will list out all the actions in the file, if any
		ArrayList<String> defs=new ArrayList<String>();
		try{
			//This tries to scan the characters file, if there is none it returns false
			Scanner scan=new Scanner(new FileReader(new File(f,charN+".txt")));
			while(scan.hasNextLine()){
				//This adds an action to the list
				defs.add(scan.nextLine());
			}
			scan.close();
			file=true;
		}
		catch(FileNotFoundException e){
			//If no file, do not go through next step
			file=false;
			System.out.println("FILENOTFOUND");
		}
		if (file){
			//If there is a file, this adds the already set defaults into the menu correctly
			for (int x=0;x<defs.size();x++){
				String act=defs.get(x);
				String[] acts=act.split(";");
				//This looks to see if there is already an action by this name
				if (hasAction(acts[0])){
					//If there is one, this updates the value, and sets it to editable to show that it is customized already
					Actionen actionen=findAction(acts[0]);
					actionen.selectFile(acts[1]);
					actionen.setEditable(true);
				}
				//System.out.println(action.getCharacter());
				//System.out.println(action.getFile());
			}
		}
	}
	public boolean hasAction(String namen){
		//This checks if there is an action by that name in the list of actions in the menu
		for (Actionen act:actions){
			//Goes through the actions until it finds one with the same name
			if(act.getactName().equals(namen)){
				return true;
			}
		}
		return false;
	}
	public Actionen findAction(String namen){
		//This gets the action by that name, or null if there is none
		for (Actionen act:actions){
			//Goes through the actions until it finds the one by that name
			if(act.getactName().equals(namen)){
				return act;
			}
		}
		//Returns null if there is no such action, but all uses in the code are already dependent on there being an action
		return null;
	}
	public void addAction(Actionen a){
		//This adds an action to the list and then the panel
		actions.add(a);
		actCons.gridy=actions.lastIndexOf(a);
		this.add(a,actCons);
	}
	public ArrayList<Actionen> getActions(){
		//This returns the list of actions
		return actions;
	}
	public void save(){
		//This saves everything to a .txt file named after the character
		File f=new File(base,"Texts");
		File outp=new File(f,charN+".txt");
		try{
			//This writes to the outp file, and does not append, so will overwrite a previous file
			PrintWriter fw=new PrintWriter(new FileOutputStream(outp,false));
			//This goes through the actions
			for(Actionen act: actions){
				//This saves the file to whatever is currently selected
				act.saveFile();
				if(act.isDif()){
					//This checks to see if the action is different than the default, by looking if the box has been checked
					String out=act.getactName()+";"+act.getFile()+"\r\n";
					fw.write(out);
				}
			}
			fw.close();
		}	
		catch(FileNotFoundException e){
		}
	}
}
