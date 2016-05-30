package ckEditor;

import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKBook;
import ckGameEngine.CKTeam;

public class CKTeamPropertiesEditor extends CKXMLAssetPropertiesEditor<CKTeam> implements ChangeListener,DocumentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;

	CKTeam asset;
	
	JTextField name;
	JTextArea functions;
	
	
	
	
	
	CKTreeGui   myAbilities;
	CKTreeGui   myStory;
	
	//FIXME add artifacts
	//show/add characters-names?
	//equip characters


		
	
	public CKTeamPropertiesEditor(CKTeam asset,boolean editable)
	{
		this.asset=asset;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		add(new JLabel("name"));
		name = new JTextField(asset.getName());
		name.setEditable(editable);
		add(name);
		
		add(new JLabel("Functions"));
		functions = new JTextArea(asset.getFunctions(),10,30);
		functions.setEditable(editable);
		add(functions);
		
		add(new JLabel("Abilities"));
		
		myAbilities = new CKTreeGui(this.asset.getAbilities(),editable);
		add(myAbilities);
		
		add(new JLabel("Story Record"));
		myStory = new CKTreeGui(asset.getStory(),editable);
		add(myStory);
				
	}

	private Vector<ChangeListener> listeners = new Vector<ChangeListener>();
	
	public void addChangeListener(ChangeListener l)
	{
		listeners.add(l);
	}
	
	


	@Override
	public void storeState()
	{
		myAbilities.stopEditing();
		myStory.stopEditing();
		
		privateStoreState();
	}
	
	
	
	private void privateStoreState()
	{	
		asset.setName(name.getText());
		asset.setFunctions(functions.getText());
		asset.setAbilities((CKBook) myAbilities.getRoot());
		asset.setStory((CKBook) myStory.getRoot());
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e)
	{		
		privateStoreState();
	
				
		for(ChangeListener l:listeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
		
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		//CKSharedAsset water=(CKSharedAsset) CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("HeroSW");
		CKTeam team = new CKTeam();
		CKTeamPropertiesEditor view=new CKTeamPropertiesEditor(team,true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}



	@Override
	public CKTeam getAsset()
	{
		return asset;
		
	}




	@Override
	public void insertUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}




	@Override
	public void removeUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}




	@Override
	public void changedUpdate(DocumentEvent e)
	{
		stateChanged(null);
	}

}
