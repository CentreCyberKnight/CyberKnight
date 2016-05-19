package ckPythonInterpreter;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.CKAssetLabel;
import ckGameEngine.CKGridActor;

public class CKCharacterShortView extends JPanel
{

	private static final long serialVersionUID = -5611795151955932240L;
	CKGridActor character;
	JLabel cpLabel;
	
	public CKCharacterShortView(CKGridActor character2)
	{
		this.character = character2;
		setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(250,175));
		add(new CKAssetLabel(getPortrait()),BorderLayout.WEST  );
		
		JLabel name = new JLabel("<html><h2>"+character2.getName()+"</h2></html>");
		name.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		add(name,BorderLayout.NORTH);
		
		JPanel quickStats = new JPanel(new BorderLayout());
		
		cpLabel = new JLabel("<html><h1>CP: "+character2.getCyberPoints()+"</h1></html>");
		quickStats.add(cpLabel,BorderLayout.CENTER);
		
		quickStats.add(new JButton("Full Stats"),BorderLayout.SOUTH);
		
		add(quickStats,BorderLayout.CENTER);
		
		/*
		CKBookView comp = new CKBookView(character2.getAbilities());
		//JComponent comp = character2.getAbilities().getXMLAssetViewer();
		character2.addListener(comp);
		comp.setPreferredSize(new Dimension(200,125));
		add(comp,BorderLayout.CENTER);		 
		*/
		
		
	}

	protected String getPortrait()
	{
	CKGraphicsAssetFactory factory =  CKGraphicsAssetFactoryXML.getInstance(); 
	return factory.getPortrait(character.getAssetID()).getAID();
			
	}

}
