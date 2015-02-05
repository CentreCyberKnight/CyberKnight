package ckEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ckEditor.Utilities.*;
public class Housing extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5677746846893909805L;

	ColorChooser chooser;
	JMenuBar menuBar;
	AssetEditorTabPane tabPane;
	

	public Housing()
	{
		int inset=50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(inset, inset,
	              screenSize.width  - inset*2,
	              screenSize.height - inset*2);
	    this.addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e)
	        	{
	            	System.exit(0);
	        	}
	    	}
		);
	    
	    //init components
	    
	    chooser=new ColorChooser();
	    
	    tabPane=new AssetEditorTabPane(chooser);
	    initMenuBar();
	    //event=new EventHandler(tabPane,chooser);
	   
	    //addComponents
	    this.setJMenuBar(menuBar);
	    
	    add(tabPane,BorderLayout.CENTER);
	    add(chooser,BorderLayout.EAST);
	    
	    setVisible(true);
	}
	
	//TODO
	//menu bar -- very basic menu Bar
	//handel menu bar actions
	
	private void initMenuBar()
	{
		menuBar=new JMenuBar();
		
		//init file
		JMenu fileMenu = addMenu(menuBar,"File");
		addMenuItem(fileMenu,"openImage","Open Image");
		addMenuItem(fileMenu,"saveImage","Save Image");
		addMenuItem(fileMenu,"closeWindow","Close");
		addMenuItem(fileMenu,"newImage","New Image");
		
		
		//init edit
		
		//init add
		JMenu addMenu = addMenu(menuBar,"Add");
		addMenuItem(addMenu,"NewColorSelectionPane","New Color Selection Pane");
		addMenuItem(addMenu,"NewAsset","New Asset");
		addMenuItem(addMenu,"NewEditorTab","New Editor Tab");
		
		//init img?
		
		//init options
		JMenu optionMenu = addMenu(menuBar,"Options");
		JMenu background = new JMenu("Change Background");
		optionMenu.add(background);
		addMenuItem(background,"Cblack","Black");
		addMenuItem(background,"Cgray","Gray");
		addMenuItem(background,"Cwhite","White");
		addMenuItem(background,"Igrid1","grid1");
		addMenuItem(background,"Igrid2","grid2");
		addMenuItem(background,"Igrid3","grid3");
		addMenuItem(background,"Igrid4","grid4");
		addMenuItem(background,"Igrid5","grid5");

		//init help
		JMenu helpMenu = addMenu(menuBar,"Help");
		addMenuItem(helpMenu,"help","THERE IS NO HELP!");
	}
	
	public JMenu addMenu(JMenuBar menuBar, String text)
	{
		JMenu x=new JMenu(text);
		menuBar.add(x);
		return x;
	}
	
	/*method to assist in adding menu items to menus
	 * @param menu --menu to add item to
	 * @param command --action command for event handeling
	 * @param text --text to appear in menu
	 * @return none
	 */
	public void addMenuItem(JMenu menu, String command, String text)
	{
		JMenuItem item = new JMenuItem(text);
		item.setActionCommand(command);
		item.addActionListener(this);
		menu.add(item);
	}
	
	public void addOptionMenuItem(JMenu menu, String command, String text)
	{
		JMenuItem item = new JMenuItem(text);
		item.setActionCommand(command);
		item.addActionListener(new ActionListener()
			{@Override
				public void actionPerformed(ActionEvent e) 
				{
					String s=e.getActionCommand();
					if (s.charAt(0)=='I')//its an image
					{
						Image bg_image=Util.getImage("Cyber Knight/ckEditor/images/tools/"+s+".png");
						//tabPane.getSelectedComponent().setBackground(bg_image,null);
					}
					else
					{
						//tabPane.getSelectedComponent().setBackground(null,Color.BLACK);//TODO
					}
				}			
			});
		menu.add(item);
	}

	@Override
	public void actionPerformed(ActionEvent evt) 
	{
		tabPane.addEditorTab();
		
	}
	
}
