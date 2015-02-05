package ckEditor;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckGameEngine.CKArtifact;
import ckGameEngine.CKSpell;
import ckGraphicsEngine.assets.CKNullAsset;

public class CKArtifactShortView extends JPanel
{
	
	private static final long serialVersionUID = 5332358119891402921L;
	CKAssetButton artifactButton;
	JLabel title;
	CKAssetButton[] spellButtons;
	public static int MAX_SPELLS = 4;
	JButton equipButton;
	
	public CKArtifactShortView()
	{
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		float alignment = BOTTOM_ALIGNMENT;
		
		artifactButton  = new CKAssetButton();
		artifactButton.setAlignmentY(alignment);
		artifactButton.setPreferredSize(new Dimension(64,64));
		add(artifactButton);
		add(Box.createRigidArea(new Dimension(2,0)));
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		title = new JLabel("");
		top.setAlignmentY(alignment);
		top.add(title);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		
		spellButtons = new CKAssetButton[MAX_SPELLS];
		for(int i=0;i<MAX_SPELLS;i++)
		{
			spellButtons[i] = new CKAssetButton();
			spellButtons[i].setAlignmentY(alignment);
		    spellButtons[i].setPreferredSize(new Dimension(32,32));
			bottom.add(spellButtons[i]);
		}
		bottom.add(Box.createHorizontalGlue());
		top.add(Box.createHorizontalGlue());
		
		equipButton= new JButton("E");
		equipButton.setAlignmentY(alignment);
		//eButton.setPreferredSize(new Dimension(32,32));
		equipButton.setToolTipText("<html><h3>Unequip Artifact</h3></html>");
		top.add(equipButton);
			
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		left.add(top);
		left.add(bottom);
		left.setAlignmentY(alignment);
		add(left);
		add(Box.createHorizontalGlue());
		
	}
	
	
	
	
	

	public void setArtifact(CKArtifact art)
	{
		artifactButton.setIcon(art.getIconId());
		artifactButton.setToolTipText("<html><h2>"+art.getName()+"</h2><br /><p>"+art.getBackstory()+"</p></html>");
		
		title.setText("<html><h2>"+art.getName()+"</h2></html>");
		Iterator<CKSpell> iter=art.getSpells();
		int pos=0;
		while(iter.hasNext() && pos < MAX_SPELLS)
		{
			CKSpell s= iter.next();
			CKAssetButton but = spellButtons[pos];
			pos++;
			
			but.setIcon(s.getIconID());
			but.setToolTipText("<html><h3>"+s.getName()+"</h3><br />"+s.getDescription()+"</html>");
			but.setVisible(true);
		}
		for( ;pos<MAX_SPELLS ;pos++ )
		{
			CKAssetButton but = spellButtons[pos];
			but.setIcon(CKNullAsset.getNullAsset());
			but.setText("");
			but.setToolTipText("");
			but.setVisible(false);
		}
			
	}

	/**
	 * @return the artifactButton
	 */
	public CKAssetButton getArtifactButton()
	{
		return artifactButton;
	}

	/**
	 * @return the spellButtons
	 */
	public CKAssetButton getSpellButton(int i)
	{
		return spellButtons[i];
	}

	/**
	 * @return the maxSpells
	 */
	public static int getMaxSpells()
	{
		return MAX_SPELLS;
	}

	/**
	 * @param maxSpells the maxSpells to set
	 */
	public static void setMaxSpells(int maxSpells)
	{
		CKArtifactShortView.MAX_SPELLS = maxSpells;
	}






	/**
	 * @return the equipButton
	 */
	public JButton getEquipButton()
	{
		return equipButton;
	}
	
	
}
