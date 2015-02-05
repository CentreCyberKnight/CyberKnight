package ckEditor.Artifact;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import ckDatabase.CKArtifactFactory;
import ckGameEngine.CKArtifact;

public class window extends JFrame
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6537317522783685963L;

	private boolean editable = true;
	
	private ArtifactPanel ArtifactInfo;
	private SpellsPanel SpellInfo;
	
	private JButton icon1 = new JButton();
	private JButton icon2 = new JButton();
	
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private int xSize = ((int) tk.getScreenSize().getWidth());
	private int ySize = ((int) tk.getScreenSize().getHeight());
	private JPanel BottomContent = new JPanel();
	private JPanel BottomLeft = new JPanel();
	private JPanel BottomRight = new JPanel();
	private JPanel CommandLine = new JPanel();
	private JPanel GamingWindow = new JPanel();
	private JPanel rightContent = new JPanel();
	private JPanel leftContent = new JPanel();
	
	private JPanel tabs = new JPanel();
	private JPanel contents = new JPanel();
	private JButton expand = new JButton("Expand");
	
	private ActionListener act1 = new secondListener();
	private ActionListener act2 = new thirdListener();
	
	private CKArtifactFactory Artfactory = CKArtifactFactory.getInstance();
	
	private CKArtifact currentArtifact;
	private JButton save = new JButton("Save");
	
	public window()
	{
		currentArtifact = Artfactory.readAssetFromXMLDirectory("balletShoes");
		//Setting the layout for the window
		setLayout(new BorderLayout());
		
		//Creating/editing the left and right content windows
		leftContent.setLayout(new GridLayout(2,1));
		rightContent.setLayout(new GridLayout(2,1));
		//rightContent.setPreferredSize(new Dimension((int)(Math.round(xSize*.30)),(ySize)));
		
		//Top Content Holder
		JPanel TopContent = new JPanel();
		TopContent.setLayout(new GridLayout(2,0,10,10));
		
		//Bottom Content Holder
		BottomContent.setLayout(new BorderLayout());
		BottomLeft.setLayout(new BorderLayout());
		BottomLeft.setPreferredSize(new Dimension((int)(Math.round(xSize*.35)),(ySize)));
		BottomRight.setLayout(new BorderLayout());
		BottomRight.setPreferredSize(new Dimension((int)(Math.round(xSize*.65)),(ySize)));
		
		
		
		//Row 1, Column 1 of JPanel TopContent
		JPanel IconHolder = new JPanel();
		IconHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
		ImageIcon image1 = new ImageIcon("images/Electric_Sword.png");
		ImageIcon image2 = new ImageIcon("images/Electric_Slash.png");

		icon1.setIcon(image1);
		icon1.addActionListener(new Listener(1));
		icon2.setIcon(image2);
		icon2.addActionListener(new Listener(2));

		IconHolder.add(icon1);
		IconHolder.add(icon2);
		
		String artAID;
		if(isEditable()==true)
		{
			Vector<CKArtifact> ArtifactStorage = new Vector<CKArtifact>();
			Iterator<CKArtifact> ArtIter = Artfactory.getAllAssets();
			JComboBox artDropDown = new JComboBox();
			artDropDown.removeAllItems();
			
			while(ArtIter.hasNext())
			{
				ArtifactStorage.add(ArtIter.next());
			}
			
			for(CKArtifact artifact:ArtifactStorage)
			{
				artAID = artifact.getAID();
				artDropDown.addItem(artAID);
			}
			artDropDown.addActionListener(new DropListener());
			
			save.addActionListener(new SaveListener());
			
			IconHolder.add(artDropDown);
			IconHolder.add(save);
		}

		GamingWindow.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//Adding elements to the TopContent JPanel
		TopContent.add(IconHolder);
		TopContent.add(GamingWindow);

		
		
		
		
/*		//Creating the Ballet Shoes artifact
		String[] pages = {P_FORWARD,P_LEFT,P_RIGHT};
		CKBook limitsballet = new CKBook();
		String[] pagesb = {P_FORWARD,P_LEFT,P_RIGHT};
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pagesb ));
		
		CKBook ABS = new CKBook("shoe abilities",SPEED ,2);
		limitsballet.addChapter(new CKChapter(CH_MOVE,3,pages ));
		//"Requirements", CH_EQUIP_SLOTS,0,P_SHOES
		BookList reqsballer = new BookList();
		CKArtifact balletShoes = new CKArtifact("Ballet Shoes","Worn by all the premier dancers!!","balletShoes", ABS,limitsballet,reqsballer,2);
		
		//Creating and adding spells to the ballet shoes artifact
		CKSpell spell;
		spell = new CKSpell("Forward","moves forward", "move('forward',1)","upArrow",1, "1");
		balletShoes.addSpell(spell);
		spell = new CKSpell("LeftTurn","turns left", "move('left',1)","leftArrow",1, "1");
		balletShoes.addSpell(spell);
		spell = new CKSpell("Right","turns right", "move('left',3)","rightArrow",1, "3");
		balletShoes.addSpell(spell);*/
		
		
		
		//Row2, Column 1 content (JPanel BottomContent)
		ArtifactInfo = new ArtifactPanel(currentArtifact, isEditable());
		SpellInfo = new SpellsPanel(currentArtifact, isEditable());
		CommandLine.setLayout(new GridLayout(2,1,5,5));
		
		//Editing the CommandLine JPanel
		tabs.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Editing the tabs Jpanel
		ImageIcon PrefSrc3 = new ImageIcon("images/PlaceHolder.jpg");
		JLabel tab1 = new JLabel();
		tab1.setIcon(PrefSrc3);
		JLabel tab2 = new JLabel();
		tab2.setIcon(PrefSrc3);
		JLabel tab3 = new JLabel();
		tab3.setIcon(PrefSrc3);
		
		//Adding the elements to the tab 
		tabs.add(tab1);
		tabs.add(tab2);
		tabs.add(tab3);
		
		expand.addActionListener(act1);
		tabs.add(expand);
		
		//Editing the contents JPanel
		
		/*
		//The command line will go here
		*/
		
		//Adding the elements to the CommandLine JPanel
		contents.setBackground(Color.BLUE);
		CommandLine.add(tabs);
		CommandLine.add(contents);

		//Adding elements to the BottomContent JPanel
		ArtifactInfo.setPreferredSize(new Dimension((int)(Math.round(xSize*.35)),(ySize)));
		SpellInfo.setPreferredSize(new Dimension((int)(Math.round(xSize*.25)),(ySize)));
		CommandLine.setPreferredSize(new Dimension((int)(Math.round(xSize*.40)),(ySize)));
		
		BottomLeft.add(ArtifactInfo, BorderLayout.EAST);
		BottomRight.add(SpellInfo, BorderLayout.WEST);
		BottomRight.add(CommandLine, BorderLayout.EAST);

		BottomContent.add(BottomLeft, BorderLayout.CENTER);
		BottomContent.add(BottomRight, BorderLayout.EAST);
		
		//Borders
		TopContent.setBorder(BorderFactory.createLineBorder(Color.black));
		CommandLine.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Adding elements to the JFrame
		leftContent.add(TopContent);
		leftContent.add(BottomContent);
		
		add(leftContent, BorderLayout.WEST);
	}

	public boolean isEditable()
	{
		return editable;
	}
	
	class Listener implements ActionListener
	{
		JPopupMenu dropmenu = new JPopupMenu();
		JButton item;
		
		public Listener(int x)
		{
			ImageIcon src = new ImageIcon("images/PlaceHolder.jpg");
			JMenuItem item1 = new JMenuItem();
			item1.setIcon(src);
			JMenuItem item2 = new JMenuItem();
			item2.setIcon(src);
			JMenuItem item3 = new JMenuItem();
			item3.setIcon(src);
			JMenuItem item4 = new JMenuItem();
			item4.setIcon(src);
			JMenuItem item5 = new JMenuItem();
			item5.setIcon(src);
			JMenuItem item6 = new JMenuItem();
			item6.setIcon(src);
			JMenuItem item7 = new JMenuItem();
			item7.setIcon(src);
			JMenuItem item8 = new JMenuItem();
			item8.setIcon(src);
			dropmenu.setLayout(new GridLayout(3,3));
			dropmenu.add(item1);
			dropmenu.add(item2);
			dropmenu.add(item3);
			dropmenu.add(item4);
			dropmenu.add(item5);
			dropmenu.add(item6);
			dropmenu.add(item7);
			dropmenu.add(item8);
			if(x==1)
			{
				item = icon1;
			}
			else
			{
				item = icon2;
			}
		}

		public void actionPerformed(ActionEvent e)
		{
			dropmenu.show(item, 120, 80);
		}
	}
	
	class SaveListener implements ActionListener
	{	
		//JLabel[] labels = {VarName,Name,Backstory,Icon,Abilities,Rechargerate};
		//JTextField[] textfields = {varName,name,backstory,icon,abilities,RechargeRate};
		public void actionPerformed(ActionEvent e)
		{
			//Editing/saving artifact associated with the frame, dont create new artifact
			
			/*temp = new CKArtifact(name.getText(),backstory.getText(),icon.getText(),abilities,Limits,Requirements,Integer.parseInt(RechargeRate.getText()));
			temp.setAID(name.getText());*/
			currentArtifact.setAID("");
			currentArtifact.setAbilties(ArtifactInfo.getAbilities());
			currentArtifact.setName(ArtifactInfo.getName());
			currentArtifact.setBackstory(ArtifactInfo.getBackstory());
			currentArtifact.setIconId(ArtifactInfo.getAID());
			currentArtifact.setLimits(ArtifactInfo.getLimits());
			currentArtifact.setName(ArtifactInfo.getName());
			currentArtifact.setRechargeRate(ArtifactInfo.getRechargeRate());
			currentArtifact.setSpells(SpellInfo.getSpells());
			CKArtifactFactory factory = CKArtifactFactory.getInstance();
			factory.writeAssetToXMLDirectory(currentArtifact);
			System.out.println("CKArtifact "+ArtifactInfo.getName()+"was written to XML");
		}
	}
	
	class secondListener implements ActionListener
	{		
		public void actionPerformed(ActionEvent e)
		{
			expand.setText("Collapse");
			expand.removeActionListener(act1);
			expand.addActionListener(act2);
			add(rightContent,BorderLayout.EAST);
			rightContent.setPreferredSize(new Dimension((int)(Math.round(xSize*.40)),(ySize)));
			rightContent.add(tabs);
			rightContent.add(contents);
			rightContent.repaint();
			rightContent.revalidate();
			BottomRight.remove(CommandLine);
			BottomRight.setPreferredSize(new Dimension((int)(Math.round(xSize*.25)),(ySize)));
			//leftContent.setPreferredSize(new Dimension((int)(Math.round(xSize*.70)),(ySize)));
		}
	}

	class thirdListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			expand.setText("Expand");
			expand.removeActionListener(act2);
			expand.addActionListener(act1);
			rightContent.remove(tabs);
			rightContent.remove(contents);
			remove(rightContent);
			CommandLine.add(tabs);
			CommandLine.add(contents);
			CommandLine.setPreferredSize(new Dimension((int)(Math.round(xSize*.35)),(ySize)));
			BottomRight.setPreferredSize(new Dimension((int)(Math.round(xSize*.65)),(ySize)));
			CommandLine.setPreferredSize(new Dimension((int)(Math.round(xSize*.40)),(ySize)));
			BottomRight.add(CommandLine, BorderLayout.EAST);
			BottomContent.repaint();
			BottomContent.revalidate();
		}
	}
	
	class DropListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JComboBox temp = (JComboBox)e.getSource();
			String Selection = (String)temp.getSelectedItem();
			currentArtifact = Artfactory.readAssetFromXMLDirectory(Selection);
			System.out.println(Selection);
			ArtifactInfo.setCurrentArtifact(currentArtifact);
			SpellInfo.setSpellIter(currentArtifact.getSpells());
		}
	}

	public static void main(String args[])
	{
		window frame = new window();
		frame.setVisible(true);
		frame.setSize(1100, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
}
