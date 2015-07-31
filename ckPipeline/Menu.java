package ckPipeline;
import javax.swing.*;
import ckCommonUtils.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.event.*;
public class Menu extends JPanel {
	//This is the menu which houses the actions which are being created
	private ArrayList<Opts> actions;
	private GridBagLayout layout;
	private String[] bvhs;
	private ArrayList<JButton> buttons;
	private GridBagConstraints actCons,butCons;
	private String base;
	public Menu(){
		base=CKProperties.getValue("Pipeline_Path");
		actions= new ArrayList<Opts>();
		setup();
	}
	public void setup(){
		//This sets everything so that it will show up correctly in rows
		actCons=new GridBagConstraints();
		actCons.weightx=1;
		actCons.gridx=0;
		actCons.anchor=GridBagConstraints.LINE_START;
		actCons.fill=GridBagConstraints.HORIZONTAL;
		butCons=new GridBagConstraints();
		butCons.anchor=GridBagConstraints.LINE_END;
		butCons.insets=new Insets(0,0,0,20);
		layout=new GridBagLayout();
		this.setLayout(layout);
		buttons=new ArrayList<JButton>();
		getBVHs();
		checkFile();
		//showItems();
		//System.out.println(actions.get(1).isVisible());
	}
	public void getBVHs(){
		//This gets the list of actions that can be chosen
		File dir=new File(base,"actions");
		bvhs = dir.list(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".gfa");
		    }
		});
	}
	public void checkFile(){
		//This checks the default file
		Boolean file=true;
		File f=new File(base,"Texts");
		ArrayList<String> defs=new ArrayList<String>();
		try{
			//This tries to scan the default file, if there is none it returns false
			Scanner scan=new Scanner(new FileReader(new File(f,"default.txt")));
			while(scan.hasNextLine()){
				//This saves each line in a string to be read later
				defs.add(scan.nextLine());
			}
			scan.close();
			file=true;
		}
		catch(FileNotFoundException e){
			file=false;
			System.out.println("FILENOTFOUND");
		}
		if (file){
			//If there is a file, this adds the already set defaults into the menu correctly
			for (int x=0;x<defs.size();x++){
				//This goes through all the lines in the default, and creates actions from them
				String act=defs.get(x);
				String[] acts=act.split(";");
				Opts action=new Opts(acts[0],acts[1],bvhs);
				addAction(action);
				//System.out.println(action.getCharacter());
				//System.out.println(action.getFile());
			}
		}
	}
	public void addAction(){
		//This adds a blank action
		Opts a= new Opts(bvhs);
		actions.add(a);
		actCons.gridy=actions.lastIndexOf(a);
		this.add(a,actCons);
		setupButton();
	}
	
	public void addAction(Opts a){
		//This adds a non-blank action
		actions.add(a);
		actCons.gridy=actions.lastIndexOf(a);
		this.add(a,actCons);
		setupButton();
	}
	public void removeAction(int a){
		//This removes the nth action from the list
		this.remove(actions.get(a));
		this.remove(buttons.get(a));
		actions.remove(a);
		buttons.remove(a);
		this.updateUI();
	}
	public void setupButton(){
		//This sets up the remove button, which will be shown next to the option
		JButton remove=new JButton();
		remove.setPreferredSize(new Dimension(80,30));
		remove.setText("Remove");
		buttons.add(remove);
		butCons.gridy=buttons.lastIndexOf(remove);
		this.add(remove,butCons);
		remove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton j=(JButton)e.getSource();
				int a=buttons.lastIndexOf(j);
				removeAction(a);
			}
		});
	}
	public void save(){
		//This saves everything to the default.txt file
		File direct=new File(base,"Texts");
		File outp=new File(direct,"default.txt");
		try{
			PrintWriter fw=new PrintWriter(new FileOutputStream(outp,false));
			for(Opts act: actions){
				//These save the info
				act.saveActName();
				act.saveFile();
				//This is the string actually saved for each action
				String out=act.getactName()+";"+act.getFile()+"\r\n";
				fw.write(out);
			}
			fw.close();
		}	
		catch(FileNotFoundException e){
		}
	}
}
