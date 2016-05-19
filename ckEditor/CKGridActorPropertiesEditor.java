package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKActorControllerFactory;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.ActorController;
import ckGameEngine.CKBook;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKGridItem;
import ckGameEngine.Direction;

public class CKGridActorPropertiesEditor 
extends CKXMLAssetPropertiesEditor<CKGridItem> implements ChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979672104515483722L;

	CKGridActor asset;
	
	JComboBox<Direction> dirBox;
//	JComboBox<String> controllerBox;
	JTextField controllerName = new JTextField(20);
	
	
	CKTreeGui   myAbilities;
	

	
	CKGridItemPropertiesEditor itemEditor;
		
	
	public CKGridActorPropertiesEditor(CKGridItem asset)
	{
		this.asset=(CKGridActor) asset;
		
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		
		
		itemEditor = new CKGridItemPropertiesEditor(this.asset);
		itemEditor.addChangeListener(this);
		add(itemEditor);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		panel.add(new JLabel("TODO Team selectors"));
		
		panel.add(new JLabel("Direction"));
		dirBox = new JComboBox<Direction>(Direction.values());
		dirBox.setEditable(false);
		dirBox.setSelectedItem(this.asset.getDirection());
		panel.add(dirBox);
		
		//controllers
		JPanel cont = new JPanel();
		controllerName.setText(this.asset.getTurnController().getName());
		cont.add(controllerName);
		JButton controllerButton = new JButton("Pick Controller");
		controllerButton.addActionListener(new ControllerViewerPopupListener());
		cont.add(controllerButton);
		panel.add(cont);
		
		panel.add(new JLabel("Abilities"));
		
		
		//myAbilities = new CKTreeGui(this.asset.getAbilities());
		myAbilities = new CKTreeGui(this.asset.getCoreAbilities());
		panel.add(myAbilities);
		
		
		add(panel);
				
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
		
		privateStoreState();
	}
	
	private void privateStoreState()
	{	
		itemEditor.storeState();
		//stateChanged(null);
		asset.setDirection(dirBox.getItemAt(dirBox.getSelectedIndex()) );
		// controller set on button press--	asset.setTurnControllerID(new ActorNullController(asset));
		asset.setCoreAbilities((CKBook) myAbilities.getRoot());
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
		CKGridItem water = new CKGridActor();
		CKGridActorPropertiesEditor view=new CKGridActorPropertiesEditor(water);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
	}



	@Override
	public CKGridItem getAsset()
	{
		return asset;
		
	}
	
	

	class ControllerViewerPopupListener implements ActionListener
	{

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			
			CKXMLFilteredAssetPicker<ActorController,CKActorControllerFactory> picker = 
					new CKXMLFilteredAssetPicker<ActorController,CKActorControllerFactory>(CKActorControllerFactory.getInstance());
			picker.addSelectedListener(new ControllerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
		}
		
	}
	
	
	
	class ControllerListener implements CKEntitySelectedListener<ActorController>
	{
		JFrame frame;
		public ControllerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(ActorController a)
		{
			asset.setControllerID(a.getAID());
			controllerName.setText(a.getName());
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}

	
	

}
