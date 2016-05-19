package ckPipeline;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ckCommonUtils.CKProperties;
public class Start extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7122613110426253640L;
	//This is the menu shown when selecting a character
	private String character,base;
	private JComboBox<String> chars;
	private String[] charList;
	private GridBagLayout layout;
	private JTextPane instructions, subtitle;
	private GridBagConstraints top,mid,box;
	
	
	public Start(){
		base=CKProperties.getValue("Pipeline_Path");
		setup();
	}
	public void setup(){
		//This sets the panel
		//this.setBackground(Color.white);
		layout=new GridBagLayout();
		this.setLayout(layout);
		setConstraints();
		setupChars();
		setupText();
		setupBox();
	}
	public void setConstraints(){
		//This sets how each object is set up in the panel
		top = new GridBagConstraints();
		top.gridy=1;
		top.weightx=1;
		top.insets=new Insets(0,0,200,0);
		mid=new GridBagConstraints();
		mid.gridy=2;
		mid.weightx=1;
		mid.insets=new Insets(0,0,50,0);
		box=new GridBagConstraints();
		box.gridy=3;
	}
	public void setupChars(){
		//This gets the list of options of characters to choose, from the folder characters
		File dir=new File(base,"Characters");
		charList = dir.list(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".duf");
		    }
		});
	}
	public void setupText(){
		//This sets up each of the text panes
		setInstructions();
		setSubtitle();
	}
	public void setInstructions(){
		//This sets the instructions at the top to be bold and large, and centered
		instructions=new JTextPane();
		//Makes the white space not visible
		instructions.setOpaque(false);
		Font bigger=new Font("SERIF",Font.BOLD,30);
		instructions.setFont(bigger);
		instructions.setText("Select a Character to edit it's actions");
		StyledDocument doc = instructions.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		instructions.setEditable(false);
		this.add(instructions,top);
	}
	public void setSubtitle(){
		//This sets the text that heads the box to be centered
		subtitle=new JTextPane();
		//Makes the white space not visible
		subtitle.setOpaque(false);
		StyledDocument doc1 = subtitle.getStyledDocument();
		SimpleAttributeSet center1 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center1, StyleConstants.ALIGN_CENTER);
		doc1.setParagraphAttributes(0, doc1.getLength(), center1, false);
		subtitle.setText("Character:");
		subtitle.setEditable(false);
		this.add(subtitle,mid);
	}
	public void setupBox(){
		//Sets up the dropDown box for selecting characters
		chars=new JComboBox(charList);
		chars.addActionListener(new ActionListener(){
			//Sets what happens when a character is selected in the box
			public void actionPerformed(ActionEvent e){
		        String charac = (String) chars.getSelectedItem();
		        setCharacter(charac);
		        //System.out.println(charac);
			}
		});
		this.add(chars,box);
		setCharacter((String)chars.getSelectedItem());
	}
	public void setCharacter(String c){
		character=c;
	}
	public String getCharacter(){
		return character;
	}
}
