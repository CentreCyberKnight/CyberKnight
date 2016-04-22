package ckEditor.Artifact;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKArtifactFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetButton;
import ckEditor.DataPickers.CKArtifactPicker;
import ckEditor.DataPickers.CKAssetPicker;
import ckEditor.treegui.BookList;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKBook;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class ArtifactPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel art = new JLabel("Electric Sword");
	private JLabel Paragraph = new JLabel("<html>This sword was given to you by the guardian of the lightning shrine.  Shrine guardians used this sword to safely enter the inner sanctum.</html>");
	
	//Change to true if you want to creat and edit artifacts, change to false if you do not
	
	private JLabel Name = new JLabel("Name: ");
	private JTextField name = new JTextField("String goes here");
	private JLabel Backstory = new JLabel("Backstory: ");
	private JTextArea backstory = new JTextArea("String goes here");
	private CKBook abilities = new CKBook();
	private CKBook Limits = new CKBook();
	private BookList Requirements = new BookList();
	private JLabel Rechargerate = new JLabel("Recharge rate: ");
	private JTextField RechargeRate = new JTextField("int goes here");
	private CKTreeGui tree;
	JPanel bottom1;
	private CKTreeGui tree2;
	JPanel right;
	private CKTreeGui tree3;
	private JScrollPane scrollWindow4;
	private JScrollPane scrollWindow2;
	private JScrollPane scrollWindow3;
	private JPanel bottom2 = new JPanel();
	
	private CKArtifactFactory Artfactory = CKArtifactFactory.getInstance();
	CKArtifactPicker artsInXML = new CKArtifactPicker(Artfactory.getAllAssets());
	
	private CKAssetButton artIcon  = new CKAssetButton();
	private CKArtifact currentArtifact;
	private boolean editable;
	
	public ArtifactPanel(CKArtifact X, boolean iseditable)
		{
			editable = iseditable;
			currentArtifact = X;
			this.setLayout(new GridLayout(3,1));

			//Editing JPanel ArtifactInfo
			JPanel Preferences = new JPanel();
			Preferences.setLayout(new FlowLayout(FlowLayout.LEFT));
			JPanel InfoPara = new JPanel();
			InfoPara.setLayout(new GridLayout(2,1,5,5));
			JPanel bottomholder = new JPanel();
			JPanel SpellLists = new JPanel();
			JPanel Properties = new JPanel();	
			
			//Editing Preferences JPanel
			CKAssetButton pref1  = new CKAssetButton();
			pref1.setIcon(X.getIconId());
			pref1.addActionListener(new Listener(X,0));
			
			/*CKAssetButton pref2 = new CKAssetButton();
			pref2.setIcon(X[1].getIconId());
			pref2.addActionListener(new Listener(X[1],1));*/
			
			
			//Adding elements to JPanel Preferences
			Preferences.add(pref1);
			//Preferences.add(pref2);
			
			//Editing InfoPara JPanel
			art.setFont(new Font("Serif", Font.BOLD, 30));
			
			//Adding elements to JPanel InfoPara
			InfoPara.add(art);
			InfoPara.add(Paragraph);
			
			//Adding elements to JPanel bottomholder
			bottomholder.add(SpellLists);
			bottomholder.add(Properties);
			
			//Adding elements to the JPanel ArtifactInfo
			this.add(Preferences);
			this.add(InfoPara);
			this.add(bottomholder);
			
			if(editable==true)
			{
				this.removeAll();
				this.repaint();
				this.revalidate();
				JPanel top = new JPanel();
				top.setLayout(new BorderLayout());
				JPanel scroll = new JPanel();
				JPanel buttons = new JPanel();
				buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
				
				scroll.setLayout(new GridLayout(4,1));
				JButton NewArtifact = new JButton("Create New");
				NewArtifact.addActionListener(new NewArtifact());
				
				artIcon.setPreferredSize(new Dimension(50,50));
				artIcon.setIcon(X.getIconId());
				artIcon.addActionListener(new IconListener());
				
				buttons.add(artIcon);
				buttons.add(NewArtifact);
				
				//Setting all textfield values
				name.setText(""+currentArtifact.getName());
				backstory.setText(""+currentArtifact.getBackstory());
				abilities = currentArtifact.getAbilities();
				Limits = currentArtifact.getLimits();
				Requirements = new BookList(currentArtifact.getRequirementsIter());
				RechargeRate.setText(Integer.toString(currentArtifact.getRechargeRate()));
				
				JPanel nameHolder = new JPanel();
				nameHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
				nameHolder.add(Name);
				nameHolder.add(name);
				scroll.add(nameHolder);
				
				JPanel backstoryHolder = new JPanel();
				backstoryHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
				backstoryHolder.add(Backstory);
				backstory.setColumns(30);
		        backstory.setLineWrap(true);
		        backstory.setRows(5);
		        backstory.setWrapStyleWord(true);
		        backstory.setEditable(true);
				backstoryHolder.add(backstory);
				scroll.add(backstoryHolder);
				
				JPanel iconHolder = new JPanel();
				iconHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
				scroll.add(iconHolder);
				
				JPanel rechargeHolder = new JPanel();
				rechargeHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
				rechargeHolder.add(Rechargerate);
				rechargeHolder.add(RechargeRate);
				scroll.add(rechargeHolder);
				
				JScrollPane scrollWindow1 = new JScrollPane(scroll,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				top.add(buttons, BorderLayout.NORTH);
				top.add(scrollWindow1, BorderLayout.CENTER);
				
				
				
				bottom2.setLayout(new BorderLayout());
				bottom1 = new JPanel();
				bottom1.setLayout(new BorderLayout());
				tree = new CKTreeGui(Limits);
				bottom1.add(tree, BorderLayout.NORTH);
				tree2 = new CKTreeGui(Requirements);
				scrollWindow4 = new JScrollPane(tree2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				bottom2.add(new JLabel("Requirements"), BorderLayout.NORTH);
				bottom2.add(scrollWindow4, BorderLayout.CENTER);
				
				JPanel left = new JPanel();
				left.setLayout(new BorderLayout());
				right = new JPanel();
				right.setLayout(new BorderLayout());
				right.setBorder(BorderFactory.createLineBorder(Color.black));
				left.setBorder(BorderFactory.createLineBorder(Color.black));
				
				JPanel middle = new JPanel();
				middle.setLayout(new BorderLayout());
				tree3 = new CKTreeGui(abilities);
				scrollWindow2 = new JScrollPane(tree3,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollWindow3 = new JScrollPane(bottom1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				left.add(new JLabel("Limits"), BorderLayout.NORTH);
				left.add(scrollWindow3, BorderLayout.EAST);
				right.add(new JLabel("Abilities"),BorderLayout.NORTH);
				right.add(scrollWindow2, BorderLayout.WEST);
				middle.add(left,BorderLayout.EAST);
				middle.add(right,BorderLayout.WEST);
				this.add(top);
				this.add(middle);
				this.add(bottom2);
			}
			
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			Preferences.setBorder(BorderFactory.createLineBorder(Color.black));
			//MKB FIXME??? not sure what this is? add(ArtifactInfo);
		}
	
	public String getName()
	{
		return name.getText();
	}
	
	public String getBackstory()
	{
		return backstory.getText();
	}
	
	public String getAID()
	{
		return artIcon.getAsset().getAID();
	}
	
	public CKBook getAbilities()
	{
		return abilities;
	}
	
	public CKBook getLimits()
	{
		return Limits;
	}
	
	public BookList getRequirements()
	{
		return Requirements;
	}
	
	public int getRechargeRate()
	{
		return Integer.parseInt(RechargeRate.getText());
	}
	
	public void setCurrentArtifact(CKArtifact X)
	{
		currentArtifact = X;
		artIcon.setIcon(currentArtifact.getIconId());
		name.setText(""+currentArtifact.getName());
		backstory.setText(""+currentArtifact.getBackstory());
		abilities = currentArtifact.getAbilities();
		Limits = currentArtifact.getLimits();
		Requirements = new BookList(currentArtifact.getRequirementsIter());
		RechargeRate.setText(Integer.toString(currentArtifact.getRechargeRate()));
		tree = new CKTreeGui(Limits);
		tree2 = new CKTreeGui(Limits);
		tree3 = new CKTreeGui(abilities);
		bottom1.removeAll();
		bottom1.add(tree, BorderLayout.NORTH);
		bottom1.repaint();
		bottom1.revalidate();
		
		scrollWindow4 = new JScrollPane(tree2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bottom2.removeAll();
		bottom2.add(new JLabel("Requirements"), BorderLayout.NORTH);
		bottom2.add(scrollWindow4, BorderLayout.CENTER);
		bottom2.repaint();
		bottom2.revalidate();
		
		scrollWindow2 = new JScrollPane(tree3,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		right.removeAll();
		right.add(new JLabel("Abilities"),BorderLayout.NORTH);
		right.add(scrollWindow2, BorderLayout.WEST);
		right.repaint();
		right.revalidate();

	}
	
	class Listener implements ActionListener
	{	
		CKArtifact artifact;
		int position;
		
		Listener(CKArtifact X, int pos)
		{
			artifact = X;
			position = pos;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			art.setText(artifact.getName());
			Paragraph.setText(artifact.getBackstory());
		}
	}

	class NewArtifact implements ActionListener
	{	
		public void actionPerformed(ActionEvent e)
		{
			name.setText("String goes here");
			backstory.setText("String goes here");
			RechargeRate.setText("int goes here");
		}
	}

	class IconListener implements ActionListener
	{
		CKGraphicsAssetFactoryXML factory;
		
		public void actionPerformed(ActionEvent e)
		{
			JFrame popUpView = new JFrame();
			factory = (CKGraphicsAssetFactoryXML) CKGraphicsAssetFactoryXML.getInstance();
			CKAssetPicker p = new CKAssetPicker(factory.getFilteredGraphicsAssets("icon"));
			p.addSelectedListener(new PopUpAssetViewer());
			popUpView.add(p);
			popUpView.pack();
			popUpView.setVisible(true);
		}
	}

	class PopUpAssetViewer implements CKEntitySelectedListener<CKGraphicsAsset>
	{
		@Override
		public void entitySelected(CKGraphicsAsset asset)
		{
			CKAssetViewer view = new CKAssetViewer(10,asset,new Dimension(400,400),true);
			artIcon.setIcon(view.getAsset());
			//set currentArtifact to the artifact associated with whatever icon was just selected
		}
	}
	
	public static void main(String args[])
	{
		JFrame frame = new JFrame();
		CKArtifact boots = CKArtifactFactory.getInstance().getAsset("combatBoots");
		
		frame.add(new ArtifactPanel(boots,true));
		frame.setVisible(true);
		frame.setSize(1100, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	
	}
}
