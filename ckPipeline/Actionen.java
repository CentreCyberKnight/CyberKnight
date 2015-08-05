package ckPipeline;
import java.util.*;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.event.*;
import java.awt.*;
public class Actionen extends JPanel{
	//This is the Action and its graphical components
	private String actName,file;
	protected JButton remove;
	protected JComboBox<String> fileBox;
	protected JTextPane nameText;
	protected String[] bvhs;
	protected GridBagLayout layout;
	protected GridBagConstraints con;
	protected Boolean isDif;
	protected JCheckBox change;
	
	public Actionen(String namen, String fileNamen, String[] bvhData){
		actName=namen;
		file=fileNamen;
		bvhs=bvhData;
		isDif=false;
		setup();
	}
	public void setup(){
		//This sets everything up so it looks nice
		con= new GridBagConstraints();
		con.insets=new Insets(5,10,5,10);
		layout=new GridBagLayout();
		this.setLayout(layout);
		setupName();
		setupFileBox();
		setupCheckBox();
		this.add(change,con);
		this.add(nameText, con);
		this.add(fileBox,con);
	}
	public void setupFileBox(){
		//This sets up the dropDown menu to choose actions
		fileBox=new JComboBox<String>(bvhs);
		fileBox.setSelectedItem(file);
		fileBox.addActionListener(new ActionListener(){
			//This sets what happens when the menu is clicked
			public void actionPerformed(ActionEvent e){
		        String actionen = (String) fileBox.getSelectedItem();
		        setFile(actionen);
		        //System.out.println(actionen);
			}
		});
		//When first created, it is set to not visible, because the check box is not checked
		fileBox.setVisible(false);
	}
	public void setupName(){
		//This sets up the name field
		nameText=new JTextPane();
		StyledDocument doc = nameText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		nameText.setEditable(false);
		nameText.setText(actName);
		//nameText.setOpaque(false);
		nameText.setPreferredSize(new Dimension(100,30));
	}
	public void setupCheckBox(){
		//This sets up the check box
		change=new JCheckBox("Editable");
		//Originally it is not selected, showing it is left default
		change.setSelected(false);
		change.addActionListener(new ActionListener(){
			//If clicked, it changes the dropDown to editable/uneditable, and changes if the class has been edited
			public void actionPerformed(ActionEvent e){
				setEditable(!isDif);
			}
		});
	}
	public String getFile(){
		return file;
	}
	public void setEditable(Boolean selected){
		//This just makes the dropDown visible/invisible, sets the boolean, and sets the checkbox correctly
		fileBox.setVisible(selected);
		isDif=selected;
		change.setSelected(selected);
	}
	public void saveFile(){
		//This saves the currently selected file
		file=(String) fileBox.getSelectedItem();
	}
	public void setFile(String fName){
		file=fName;
	}
	public void selectFile(String fName){
		file=fName;
		fileBox.setSelectedItem(file);
	}
	public void setFileBox(){
		//This sets the selected file to whatever the file is supposed to be
		fileBox.setSelectedItem(file);
	}
	public String getactName(){
		return actName;
	}
	public boolean isDif(){
		//This checks if the action is editable
		return isDif;
	}
	
}
