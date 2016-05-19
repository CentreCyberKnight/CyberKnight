package ckEditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.CKGridItemFactory;
import ckDatabase.CKXMLFactory;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckGameEngine.CKGridItem;


/**
 * @author bradshaw
 *
 */
public class CKXMLAssetEditor<T extends CKXMLAsset<T>,F extends CKXMLFactory<T>> extends JPanel implements
		CKEntitySelectedListener<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6686618565658306394L;
	//T asset; - not used
	F factory;
	CKXMLAssetEditorPane<T,F> pane;
	JPanel bpane;
	
	/**
	 * Creates generic window for the editor
	 * @param asset
	 * @param factory
	 */
	public CKXMLAssetEditor(T asset,F factory)
	{
		super();
		setLayout(new BorderLayout());
		
		
		bpane = new JPanel();
		JButton save = new JButton("Save");
		JButton saveCopy = new JButton("Save as Copy");
		JButton load = new JButton("Load");
		JButton newAsset = new JButton("New Asset");
		JButton delAsset = new JButton("Delete Asset");
		bpane.add(save);
		save.addActionListener(new SaveAsset());
		
		bpane.add(saveCopy);
		saveCopy.addActionListener(new SaveAsNewAsset());
		
		bpane.add(load);
		load.addActionListener(new LoadAsset());

		bpane.add(newAsset);
		newAsset.addActionListener(new CreateNewAsset());
		
		bpane.add(delAsset);
		delAsset.addActionListener(new DelAsset());
		
		//now handle data model
		this.factory = factory;
		setAsset(asset);
		
		
		
		
//
		
	}

	/**
	 * customizes the window to fit the type of asset being worked with
	 * @param asset2
	 */
	public void setAsset(T asset2)
	{
		setAsset(asset2, false);
		
	}
	
	
	/**
	 * customizes the window to fit the type of asset being worked with
	 * also indicates whether the user should be prompted to save before
	 * loading a new asset
	 * @param asset2
	 * @param skipSave should querySave be skipped
	 */
	public void setAsset(T asset2, boolean skipSave){
		if(skipSave == false){
			querySave();
		}
		removeAll();
		pane = new CKXMLAssetEditorPane<T,F>(asset2,factory);
		add(pane);
		add(bpane,BorderLayout.NORTH);
		revalidate();

	}

	/**
	 * Overwrites the current loaded asset with changes made
	 *
	 */
	class SaveAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(pane!=null)
			{
				pane.saveAsset();
			}
			
		}		
	}
	
	/**
	 * Creates a new asset
	 *
	 */
	class SaveAsNewAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(pane!=null)
			{
				pane.saveAsNewAsset();
			}
			
		}		
	}
	
	
	/**
	 * Loads an asset
	 *
	 */
	class LoadAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFrame frame = new JFrame();
			CKXMLFilteredAssetPicker<T,F> picker = new CKXMLFilteredAssetPicker<T, F>(factory);		
			picker.addSelectedListener(new PickerListener(frame));
			frame.add(picker);
			frame.pack();
			frame.setVisible(true);
			
		}
		
		
		
	}
	
	/**
	 * Listener used to close and dispose of windows
	 *
	 */
	class PickerListener implements CKEntitySelectedListener<T>
	{
		JFrame frame;
		public PickerListener(JFrame f) {frame=f;}
		
		@Override
		public void entitySelected(T a)
		{
			CKXMLAssetEditor.this.entitySelected(a);
	
			frame.setVisible(false); //you can't see me!
			frame.dispose(); //Destroy the JFrame object
		}

	}
	
	
	
	class CreateNewAsset implements ActionListener
	{

		
		@Override
		public void actionPerformed(ActionEvent e)
		{
				
			CKXMLAssetEditor.this.entitySelected(factory.getAssetInstance());
			
			
		}		
	}
	
	class DelAsset implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			queryDelete();
			
		}		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * prompts the user if they want to save before leaving present asset.
	 * Will save if user requests it.
	 * 
	 * @return true if the command was cancelled, false otherwise.
	 */
	private boolean querySave()
	{
		if(pane ==null) { return false; }
		//put up a do you want to save?
		int n = JOptionPane.showConfirmDialog(
			    null,
			    "Before loading, do you want to save this asset",
			    "Or cancel the load",
			    JOptionPane.YES_NO_CANCEL_OPTION);
		
		
		if(n ==JOptionPane.CANCEL_OPTION)
		{
			return true;
		}
		if(n == JOptionPane.YES_OPTION)
		{
			pane.saveAsset();
		}
		
		return false;
	}

	private boolean queryDelete()
	{
		if(pane ==null) { return false; }
		//put up a do you want to save?
		int n = JOptionPane.showConfirmDialog(
			    null,
			    "Are you sure you would like to delete this asset?", "or cancel?",
			    JOptionPane.YES_NO_CANCEL_OPTION);
		
		
		if(n ==JOptionPane.CANCEL_OPTION)
		{
			return true;
		}
		if(n == JOptionPane.YES_OPTION)
		{
			pane.delAsset();
			CKXMLAssetEditor.this.setAsset(factory.getAssetInstance(), true);
		}
		
		return false;
	}

	

	@Override
	public void entitySelected(T entity)
	{
		
		setAsset(entity);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		JFrame frame = new JFrame();
		//gridItem
		CKXMLAssetEditor<CKGridItem,CKGridItemFactory> view=
				new CKXMLAssetEditor<CKGridItem,CKGridItemFactory>(new CKGridItem(),CKGridItemFactory.getInstance());
		
		//gridActor
		/*CKXMLAssetEditor<CKGridItem,CKGridActorFactory> view=
				new CKXMLAssetEditor<CKGridItem,CKGridActorFactory>(new CKGridActor(),CKGridActorFactory.getInstance());
*/
		//CKTrigger
		/*CKXMLAssetEditor<CKTrigger,CKTriggerFactory> view=
				new CKXMLAssetEditor<CKTrigger,CKTriggerFactory>(new CKTrigger(),CKTriggerFactory.getInstance());
		*/
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	

}
