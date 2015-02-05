package ckPythonInterpreter;

import static ckCommonUtils.CKPropertyStrings.CH_EQUIP_SLOTS;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.text.EditorKit;

import jsyntaxpane.DefaultSyntaxKit;

import ckCommonUtils.DisabledPanel;
import ckCommonUtils.EquipmentComparator;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetButton;
import ckEditor.CKAssetLabel;
import ckGameEngine.CKArtifact;
import ckGameEngine.CKChapter;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGridActor;
import ckGameEngine.ActorController;
import ckGameEngine.CKPage;

public class CKCharacterConfigView extends JPanel
{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1186600715945823576L;
	
	
	CKGridActor character;
	JPanel input;
	DisabledPanel dPanel;
	public final static String TEXT_EDITOR="text editor";
	public final static String ARTIFACT_CONTROLLER="Artifact Controller";
	
	JButton runButton;
	 CKPythonEditorPane editor;	
	
	public CKCharacterConfigView(CKGridActor character)
	{
		super(new BorderLayout());
		this.character = character;
		
		/*JLabel name = new JLabel("<html><h2>"+character.getName()+"</h2></html>"); 
		add(name,BorderLayout.PAGE_START);
		*/
		
		add(new CKCharacterShortView(character),BorderLayout.NORTH);
		//do left hand side..
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		
		//left.add(new CKAssetLabel(getPortrait()),BorderLayout.WEST  );
		
		
		Vector<String> positions = getOrderedList();
		for(String pos: positions)
		{
			CKAssetButton artifactButton  = new CKAssetButton();
			//artifactButton.setAlignmentY(alignment);
			artifactButton.setPreferredSize(new Dimension(64,64));
			CKArtifact art = character.getArtifact(pos);
			if(art == null)
			{
				artifactButton.setIcon("equipment");
				artifactButton.setEnabled(false);
			}
			else
			{
				artifactButton.setIcon(art.getIconId());
			}
			
			left.add(artifactButton);
		}
		add(left,BorderLayout.LINE_START);
		add(character.getAbilities().getXMLAssetViewer(),BorderLayout.CENTER);
	}
	
	public Vector<String> getOrderedList()
	{
		CKChapter chap = this.character.getAbilities().getChapter(CH_EQUIP_SLOTS);
		Vector<String> list = new Vector<String>();
		Iterator<CKPage> iter = chap.getPages();
		while(iter.hasNext())
		{
			list.add( iter.next().getName());
		}
		Collections.sort(list,EquipmentComparator.getComparator());
		return list;
	}

	protected String getPortrait()
	{
	CKGraphicsAssetFactory factory =  CKGraphicsAssetFactoryXML.getInstance(); 
	return factory.getPortrait(character.getAssetID()).getAID();
			
	}
	
}
