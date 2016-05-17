package ckPipeline;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class Interface extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8717938164212919671L;
	//This is the actual window of the GUI
	JPanel content,bottom;
	JButton finish,add;
	Menu menu;
	private JScrollPane scroll;
	
	public Interface(){
		super("Default Actions");
		content=new JPanel(new GridBagLayout());
		setup();
	}
	public void setup(){
		//This sets up the window itself
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,600);
		startMenu();
		bottomMenu();
		this.setContentPane(content);
		this.setVisible(true);
	}
	public void startMenu(){
		//This creates the menu at the beginning
		menu=new Menu();
		//These make it so that it shows up correctly
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridy=0;
		c.weighty=.8;
		c.weightx=.8;
		content.add(menu,c);
		//This adds a scroll bar with the menu
		scroll=new JScrollPane(menu);
		content.add(scroll,c);
		
	}
	public void bottomMenu(){
		//This creates the bottom menu with the finish and add buttons
		bottom=new JPanel(new GridBagLayout());
		buttonSetup();
		//This makes sure the buttons are at the bottom of the page
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridheight=1;
		c.weightx=1;
		c.anchor=GridBagConstraints.PAGE_END;
		content.add(bottom,c);
	}
	public void buttonSetup(){
		//This sets up the buttons for the bottom menu
		add=new JButton();
		finish=new JButton();
		add.setPreferredSize(new Dimension(150,50));
		finish.setPreferredSize(new Dimension(150,50));
		add.setText("Add a Default");
		finish.setText("Finish");
		add.addActionListener(new ActionListener(){
			//This sets what happens when it is clicked, an item is added to the menu
			public void actionPerformed(ActionEvent e){
				addItem();
			}
		});
		finish.addActionListener(new ActionListener(){
			//This sets what happens when it is clicked, the defaults are saved to their current values and then closes the menu
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
		//This adds all of the buttons
		add.setEnabled(true);
		finish.setEnabled(true);
		GridBagConstraints but=new GridBagConstraints();
		but.insets=new Insets(0,0,0,50);
		bottom.add(add,but);
		bottom.add(finish);
	}
	public void addItem(){
		//This will add an option to the menu, then update the UI
		menu.addAction();
		menu.updateUI();
	}
	public void save(){
		//This saves the text file, for when finished
		menu.save();
		System.exit(0);
	}
	
public static void main(String[] args){
	Interface i=new Interface();
	//System.out.println("Working");
}
}