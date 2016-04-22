package ckPipeline;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class Interface2 extends JFrame{
	//This is the actual GUI Window
	private JPanel content,bottom;
	private JButton finish,add;
	private Menu2 menu;
	private Start select;
	private GridBagConstraints menCon;
	private boolean charSelected;
	private JScrollPane scroll;
	
	public Interface2(){
		super("Character Defaults");
		setup();
	}
	public void setup(){
		//This sets everything up the first time it is opened
		charSelected=false;
		menCon=new GridBagConstraints();
		menCon.fill=GridBagConstraints.BOTH;
		menCon.gridy=0;
		menCon.weighty=.8;
		menCon.weightx=.8;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,600);
		content=new JPanel(new GridBagLayout());
		//The start menu is created upon startup
		startMenu();
		this.setVisible(true);
	}
	public void startMenu(){
		//This creates the starting menu, for choosing a character
		//Content is remade each time to make sure everything shows up correctly
		content=new JPanel(new GridBagLayout());
		//content.setBackground(Color.WHITE);
		select=new Start();
		content.add(select,menCon);
		bottomMenu();
		this.setContentPane(content);
	}
	public void charMenu(String charName){
		//This creates the menu for editing the actions of a character
		//Content is recreated each time keep everything showing up correctly
		content=new JPanel(new GridBagLayout());
		menu=new Menu2(charName);
		content.add(menu,menCon);
		scroll=new JScrollPane(menu);
		content.add(scroll,menCon);
		bottomMenu();
		this.setContentPane(content);
		
	}
	public void bottomMenu(){
		//This creates the bottom menu with the next button
		bottom=new JPanel(new GridBagLayout());
		buttonSetup();
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridheight=1;
		c.weightx=1;
		c.anchor=GridBagConstraints.PAGE_END;
		content.add(bottom,c);
	}
	public void buttonSetup(){
		//This sets up the button for the bottom menu
		finish=new JButton();
		finish.setPreferredSize(new Dimension(150,50));
		finish.setText("Next");
		finish.addActionListener(new ActionListener(){
			//This sets what happens when the button is clicked
			public void actionPerformed(ActionEvent e){
				changeState();
			}
		});
		finish.setEnabled(true);
		bottom.add(finish);
	}
	public void changeState(){
		//This is called whenever the next button is clicked
		if(!charSelected){
			//This is if you are in the select a character menu, it saves your selection and loads up the next menu
			String ch=select.getCharacter();
			//content.remove(select);
			charMenu(ch);
			charSelected=true;
		}
		else{
			//This is if you are in the edit actions menu, it saves the options as they currently are, and then goes to the select a character menu
			save();
			startMenu();
			charSelected=false;
		}
		content.updateUI();
	}
	public void save(){
		//This saves the text file, for when finished
		menu.save();
	}
	
public static void main(String[] args){
	//This opens the GUI
	Interface2 i=new Interface2();
	//System.out.println("Working");
}
}