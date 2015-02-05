package ckEditor.Artifact;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetButton;
import ckEditor.DataPickers.CKAssetPicker;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKSpell;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class SpellsPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel content = new JPanel();
	private JLabel func = new JLabel();
	private JTextField func2 = new JTextField();
	private JLabel desc = new JLabel();
	private JTextField desc2 = new JTextField();
	private JLabel title = new JLabel();
	private JTextField title2 = new JTextField();
	private boolean editable = false;
	private JButton edit = new JButton("edit");
	private ActionListener act1 = new editListener();
	private CKSpell currentSpell;
	private ActionListener act2 = new saveListener(currentSpell);
	
	private JPanel spells;
	
	private Iterator<CKSpell> spellIter;
	private JButton addSpell = new JButton("Add Spell");
	private JButton removeSpell = new JButton("Remove Last Spell");


	private Vector<CKSpell> spellStorage = new Vector<CKSpell>();
	private Vector<CKAssetButton> buttonStorage = new Vector<CKAssetButton>();
	
	public SpellsPanel(CKArtifact X, boolean bool)
	{
		editable = bool;
		
		this.setLayout(new GridLayout(2,1,5,5));
		spellIter = X.getSpells();
		transferSpells(spellIter);

		//Editing JPanel SpellInfo
		spells = new JPanel();
		spells.setLayout(new FlowLayout(FlowLayout.LEFT));
		content.setLayout(new GridLayout(3,1,5,5));
		
		//Editing spells JPanel
		addSpells(spellStorage);
		
		//Adding elements to the spells JPanel
		edit.addActionListener(act1);
		spells.add(edit);
		
		//Editing the content JPanel
		title.setFont(new Font("Serif", Font.BOLD, 30));
		func.setFont(new Font("Serif", Font.PLAIN, 20));
		
		content.add(title);
		content.add(func);
		content.add(desc);
		
		//Adding elements to the SpellInfo JPanel
		this.add(spells);
		this.add(content);
		if(editable==true)
		{
			addSpell.addActionListener(new AddSpellListener());
			removeSpell.addActionListener(new RemoveSpellListener());
			spells.add(addSpell);
			spells.add(removeSpell);
		}
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		spells.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	//public CKSpell(String name,String description,String fcn, String iconID,int rechargeTime,String costHint)
	
	public void transferSpells(Iterator<CKSpell> IT)
	{
		spellStorage.removeAllElements();
		while(IT.hasNext())
		{
			spellStorage.add(IT.next());
		}
	}
	
	public void addSpells(Vector<CKSpell> Spells)
	{
		spells.removeAll();
		for(CKSpell spell:Spells)
		{
			CKAssetButton s1  = new CKAssetButton();
			s1.setPreferredSize(new Dimension(30,30));
			s1.setIcon(spell.getIconID());
			s1.addActionListener(new Listener(spell));
			s1.addActionListener(new IconListener(s1));
			spells.add(s1);
		}
	}
	
	public Vector<CKSpell> getEditedSpells()
	{
		return spellStorage;
	}
	
	public void setSpellIter(Iterator<CKSpell> X)
	{
		spellIter = X;
		transferSpells(X);
		addSpells(spellStorage);
		spells.add(edit);
		spells.add(addSpell);
		spells.revalidate();
		spells.repaint();
	}
	
	public Vector<CKSpell> getSpells()
	{
		return spellStorage;
	}
	
	public void EnableButton()
	{
		for(CKAssetButton button:buttonStorage)
		{
			button.setEnabled(true);
		}
	}
	
	class Listener implements ActionListener
	{	
		CKSpell artifactSpell;
		
		Listener(CKSpell X)
		{
			artifactSpell = X;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			currentSpell = artifactSpell;
			title.setText(artifactSpell.getName());
			func.setText(artifactSpell.getFunctionCall());
			desc.setText(artifactSpell.getDescription());
			act2 = new saveListener(currentSpell);
		}
	}
	
	class editListener implements ActionListener
	{
		String str1;
		String str2;
		String str3;
		
		public void actionPerformed(ActionEvent e)
		{
			for(CKAssetButton button:buttonStorage)
			{
				button.setEnabled(false);
			}
			
			str1 = func.getText();
			str2 = desc.getText();
			str3 = title.getText();
			
			func2.setText(str1);
			desc2.setText(str2);
			title2.setText(str3);
			
			content.removeAll();
			content.add(title2);
			content.add(func2);
			content.add(desc2);
			content.repaint();
			content.revalidate();
			
			EnableButton();
			
			edit.setText("save");
			edit.removeActionListener(act1);
			edit.addActionListener(act2);
		}
	}
		
		class saveListener implements ActionListener
		{
			String str1;
			String str2;
			String str3;
			CKSpell artSpell;
			
			public saveListener (CKSpell X)
			{
				artSpell = X;
			}

			public void actionPerformed(ActionEvent e)
			{
				str1 = func2.getText();
				str2 = desc2.getText();
				str3 = title2.getText();
				
				func.setText(str1);
				desc.setText(str2);
				title.setText(str3);
				
				artSpell.setName(str3);
				artSpell.setDescription(str2);
				artSpell.setFunctionCall(str1);
				
				content.removeAll();
				content.add(title);
				content.add(func);
				content.add(desc);
				
				edit.setText("edit");
				edit.removeActionListener(act2);
				edit.addActionListener(act1);
				
				for(CKAssetButton button:buttonStorage)
				{
					button.setEnabled(true);
				}
				content.repaint();
				content.revalidate();
			}
		}
		
		
		class AddSpellListener implements ActionListener
		{
			CKSpell newSpell = new CKSpell("New Spell","New Spell","NewSpell()","upArrow");
			
			public void actionPerformed(ActionEvent e)
			{
				spellStorage.add(newSpell);
				addSpells(spellStorage);
				spells.add(edit);
				spells.add(addSpell);
				spells.add(removeSpell);
				spells.repaint();
				spells.revalidate();
			}
		}
		
		class RemoveSpellListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				int size = spellStorage.size();
				spellStorage.remove(size-1);
				spells.removeAll();
				addSpells(spellStorage);
				spells.add(edit);
				spells.add(addSpell);
				spells.add(removeSpell);
				spells.repaint();
				spells.revalidate();
			}
		}


		class IconListener implements ActionListener
		{
			CKGraphicsAssetFactoryXML factory;
			CKAssetButton bttn;
			
			IconListener(CKAssetButton arg)
			{
				bttn = arg;
			}
			
			public void actionPerformed(ActionEvent e)
			{
				JFrame popUpView = new JFrame();
				factory = (CKGraphicsAssetFactoryXML) CKGraphicsAssetFactoryXML.getInstance();
				CKAssetPicker p = new CKAssetPicker(factory.getFilteredGraphicsAssets("icon"));
				p.addSelectedListener(new PopUpAssetViewer(bttn));
				popUpView.add(p);
				popUpView.pack();
				popUpView.setVisible(true);
			}
		}

		class PopUpAssetViewer implements CKEntitySelectedListener<CKGraphicsAsset>
		{
			CKAssetButton bttn;
			PopUpAssetViewer(CKAssetButton arg)
			{
				bttn = arg;
			}
			
			@Override
			public void entitySelected(CKGraphicsAsset asset)
			{
				CKAssetViewer view = new CKAssetViewer(10,asset,new Dimension(400,400),true);
				bttn.setIcon(view.getAsset());
				//set currentArtifact to the artifact associated with whatever icon was just selected
			}
		}
	
	public static void main(String args[])
	{
		//
	}
}
