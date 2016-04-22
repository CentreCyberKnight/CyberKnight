package ckPipeline;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class Opts extends JPanel{
	//This is the Opts, and its graphical objects
	private String actName,file;
	protected JButton remove;
	protected JComboBox<String> fileBox;
	protected JTextField nameField;
	protected String[] bvhs;
	protected GridBagLayout layout;
	protected GridBagConstraints con;
	
	public Opts(String[] bvhData){
		//This creates a blank new Opts
		actName="";
		file="";
		bvhs=bvhData;
		setup();
	}
	public Opts(String namen, String fileNamen, String[] bvhData){
		//This creates an Opts with the given name and file, used when loading the previous default
		actName=namen;
		file=fileNamen;
		bvhs=bvhData;
		setup();
	}
	public void setup(){
		//This is the setting up of the panel the objects are shown on
		con= new GridBagConstraints();
		con.insets=new Insets(5,10,5,10);
		layout=new GridBagLayout();
		this.setLayout(layout);
		setupNameInput();
		setupFileBox();
	}
	public void setupFileBox(){
		//This sets up the dropDown box, with the options from the list of Optss
		fileBox=new JComboBox<String>(bvhs);
		//This sets the selected item to the file, if it is an option
		fileBox.setSelectedItem(file);
		fileBox.addActionListener(new ActionListener(){
			//This sets what happens when an option is selected
			public void actionPerformed(ActionEvent e){
				//This gets the selected option, then sets the file to the Opts
		        String Optsen = (String) fileBox.getSelectedItem();
		        setFile(Optsen);
		        //System.out.println(Optsen);
			}

		});
		this.add(fileBox,con);
	}
	public void setupNameInput(){
		//This sets up the name input box to name the Opts
		nameField=new JTextField();
		nameField.setText(actName);
		nameField.setPreferredSize(new Dimension(200,30));
		nameField.addKeyListener(new KeyAdapter(){
			//This makes sure a semi-colon is never typed
			public void keyTyped(KeyEvent e){
				char c=e.getKeyChar();
				//System.out.println(c);
				if(c==';'){
					java.awt.Toolkit.getDefaultToolkit().beep();
					e.consume();
				}
			}
		});
		this.add(nameField, con);
	}
	public String getFile(){
		return file;
	}
	public void saveFile(){
		//This saves the file to whatever is currently selected
		file=(String) fileBox.getSelectedItem();
	}
	public void saveActName(){
		//This saves the Opts Name to whatever is currently in the text box
		actName=nameField.getText();
	}
	public void setFile(String fName){
		file=fName;
	}
	public void setNameField(){
		//This sets the name to whatever it is supposed to be
		nameField.setText(actName);
	}
	public void setFileBox(){
		//This sets the selected file to whatever the file is supposed to be
		fileBox.setSelectedItem(file);
	}
	public String getactName(){
		return actName;
	}
	public void setactName(String namen){
		actName=namen;
	}
	
}
