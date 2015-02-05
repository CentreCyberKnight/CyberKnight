package ckEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jdesktop.swingx.MultiSplitLayout;


import ckCommonUtils.CKEntitySelectedListener;
import ckDatabase.CKGraphicsAssetFactory;
import ckDatabase.CKGraphicsAssetFactoryXML;
import ckEditor.DataPickers.CKAssetPicker;
import ckEditor.DataPickers.CKAssetUsagePicker;
import ckEditor.DataPickers.CKFilteredAssetPicker;
import ckGraphicsEngine.assets.CKAssetViewer;
import ckGraphicsEngine.assets.CKCompositeAsset;
import ckGraphicsEngine.assets.CKGraphicsAsset;

public class CKCompositeAssetEditor extends CKAssetEditorPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8502775289068923594L;
	private CKCompositeAsset asset;
	private CKGraphicsAssetFactory afactory;
	private EditorState state;
	private CKAssetPicker aggPicker;
	private CKCompositeAssetPropertiesEditor properties;
	private CKAssetUsagePicker usages;
	private CKAssetCellViewer cell;
	
		public CKCompositeAssetEditor(CKCompositeAsset asset)
					
		{
			super(asset);

			this.asset=asset;
			afactory = CKGraphicsAssetFactoryXML.getInstance();;
			
			String layoutDef = 
				    "(COLUMN (ROW (LEAF name=viewer weight=1.0) state (COLUMN  properties usages) )" +
				    "(ROW (LEAF name=parts weight=1.0 ) picker cell) )";
			
/*			String layoutDef = 
				    "(ROW (COLUMN (LEAF name=viewer weight=1.0) state (ROW  properties usages) )" +
				    "(COLUMN (LEAF name=parts weight=1.0 ) picker cell) )";
	*/		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
			getMultiSplitLayout().setModel(modelRoot);

			//create viewer for the asset
			JPanel assetPanel = new CKAssetViewer(10,asset,null,false);
			add(assetPanel,"viewer");
			
			//create state editor for adding,removing,ordering, and height actions
			JPanel stateController = new CKCompositeControllerPanel(asset);
			add(stateController,"state");
			
			//properties
			properties =new CKCompositeAssetPropertiesEditor(asset); 
			add(properties,"properties");
			
			//usages
			usages = new CKAssetUsagePicker(asset);
			add(usages,"usages");
			
			//create picker for the compositeAssets
			aggPicker = new CKAssetPicker(asset.iterator());
			aggPicker.addSelectedListener(new LayerListener());
			add(aggPicker,"parts");
			
			//create picker for the library of elements
			CKFilteredAssetPicker libraryPicker = new CKFilteredAssetPicker();
			libraryPicker.addSelectedListener(new AddAssetListener());
			add(libraryPicker,"picker");
			
			
			//cell viewer
			cell = new CKAssetCellViewer(asset);
			add(cell,"cell");
			properties.addChangeListener(cell);
			

		
			//TODO add menu items to load and save assets to database
	/*		
			
			JPanel topPane = new JPanel();
			topPane.setLayout(new GridLayout(1,2));
			topPane.add(assetPanel);
			topPane.add(stateController);

			JPanel bottomPane = new JPanel();
			bottomPane.setLayout(new GridLayout(1,2));
			
			
			bottomPane.add(aggPicker);
			bottomPane.add(libraryPicker);
			
			
			
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setAlignmentX(LEFT_ALIGNMENT);
			add(topPane);
			add(bottomPane);
*/			
			
		}
		
		class CKCompositeControllerPanel extends JPanel
		{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8480571957116700768L;

			public CKCompositeControllerPanel(CKCompositeAsset A)
			{
				
				setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				setAlignmentX(LEFT_ALIGNMENT);
				
				
				
				ButtonGroup group = new ButtonGroup();
			
				//add remove radio
				JRadioButton removeButton = new JRadioButton("Remove Picked Asset");
				removeButton.addItemListener(new RemoveAsset());
				group.add(removeButton);
				add(removeButton);

				
				if(A.supportsOrdering())
				{
					JRadioButton upButton = new JRadioButton("Move Picked Asset Up");
					upButton.addItemListener(new MoveUp());
					group.add(upButton);
					add(upButton);
					
					JRadioButton downButton = new JRadioButton("Move Picked Asset Down");
					downButton.addItemListener(new MoveDown());
					group.add(downButton);
					add(downButton);
				}
			

				if(A.supportsHeight())//reusing button names...
				{
					JRadioButton upButton = new JRadioButton("Increase Height of Picked Asset");
					upButton.addItemListener(new IncreaseHeight());
					group.add(upButton);
					add(upButton);
					
					JRadioButton downButton = new JRadioButton("Decrease Height of Picked Asset");
					downButton.addItemListener(new DecreaseHeight());
					group.add(downButton);
					add(downButton);
				}
			
				if(A.supportsNaming())
				{
					JRadioButton naming = new JRadioButton("Change Name of Picked Asset");
					naming.addItemListener(new Naming());
					group.add(naming);
					add(naming);
				}
				
				JButton saveButton = new JButton("Save Picked Asset");
				saveButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(asset);
					}
					
				});
				group.add(saveButton);
				add(saveButton);
				
				
			
			}
		}
		
		
		
		
		
		
		//adding asset listener for the librabry picker
		
		class AddAssetListener implements CKEntitySelectedListener<CKGraphicsAsset>
		{
			EditorState action;
			public AddAssetListener()
			{
				action=new AddAsset();
			}
			
			
			@Override
			public void entitySelected(CKGraphicsAsset newAsset)
			{
				action.doAction(newAsset);
				
			}

		}
	
		//adding asset listener for the layer picker
		
		class LayerListener implements CKEntitySelectedListener<CKGraphicsAsset>
		{	
			@Override
			public void entitySelected(CKGraphicsAsset newAsset)
			{
				state.doAction(newAsset);
				
			}

		}
		
		
		
		
		
		
		
		private abstract class EditorState implements ItemListener
		{
			abstract public void doAction(CKGraphicsAsset newAsset);

			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				state = this;
				
			}

		}

		private class MoveUp extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.moveUpAsset(newAsset);
				aggPicker.rePopulateEntities(asset.iterator());
			}
			
		}

		private class MoveDown extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.moveDownAsset(newAsset);				
				aggPicker.rePopulateEntities(asset.iterator());
			}
			
		}

		private class IncreaseHeight extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.addHeight(newAsset, 1);
				aggPicker.rePopulateEntities(asset.iterator());
			}

		}


		private class DecreaseHeight extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.addHeight(newAsset,-1);
				aggPicker.rePopulateEntities(asset.iterator());
			}
		}

		@SuppressWarnings("unused")
		private class SaveAsset extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				CKGraphicsAssetFactoryXML.writeAssetToXMLDirectory(asset);
				
				//aggPicker.rePopulateAssets(asset.iterator());
				//System.out.println("I am removing"+newAsset.getDescription());
			}
		}
		
		private class RemoveAsset extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.removeAsset(newAsset);
				aggPicker.rePopulateEntities(asset.iterator());
				System.out.println("I am removing"+newAsset.getDescription());
			}
		}


		private class AddAsset extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				asset.addAsset(newAsset);
				aggPicker.rePopulateEntities(asset.iterator());
				cell.stateChanged(null);
			}
		
		}
		
		private class Naming extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				String name = asset.getAssetName(newAsset);
				
				String newName = JOptionPane.showInputDialog("What would you like the new name to be",
	                     name);
				if(newName.length() > 0)
				{
					asset.renameAsset(newAsset, newName);
				}
			}
		
		}
		
		public void storeState()
		{
			usages.saveUsages();
		}

		
		public static void main(String[] args)
		{
			JFrame frame = new JFrame();
			CKGraphicsAssetFactory factory = CKGraphicsAssetFactoryXML.getInstance(); 
			
			CKCompositeAssetEditor ed =
				new CKCompositeAssetEditor(//new CKLayeredAsset("blank"),
						//new CKSpriteAsset("blank"),
						(CKCompositeAsset) factory.getGraphicsAsset("heroSprite")
								);
			frame.add(ed);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			
			
		}

		
}
