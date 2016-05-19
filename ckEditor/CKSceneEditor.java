package ckEditor;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ckCommonUtils.CKEntitySelectedListener;
import ckCommonUtils.CKXMLAsset.ViewEnum;
import ckDatabase.CKGraphicsLayerFactory;
import ckEditor.DataPickers.CKXMLAssetPicker;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.CKGraphicsSceneInterface;
import ckGraphicsEngine.layers.CKGraphicsLayer;

@Deprecated
public class CKSceneEditor extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8502775289068923594L;
	private CKGraphicsSceneInterface scene;
	private CKGraphicsLayerFactory afactory;
	private EditorState state;
	private CKXMLAssetPicker<CKGraphicsLayer> layerPicker;
	private JLabel sceneLabel;
	
		public CKSceneEditor(CKGraphicsSceneInterface scene,
									  CKGraphicsLayerFactory factory)
		{
			this.scene=scene;
			afactory = factory;
			
			//create viewer for the scene
			BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(scene, 200, 200);
			sceneLabel = new JLabel(new ImageIcon(img));
			JComponent scenePanel = new JPanel();
			scenePanel.add(sceneLabel);
			
			//create picker for the layers in the scene
			layerPicker = new CKXMLAssetPicker<CKGraphicsLayer>(scene.layerIterator());
			layerPicker.addSelectedListener(new LayerListener());
			
			//create picker for the library of elements
			//TODO add a method to decrease the number of Layers shown
			CKXMLAssetPicker<CKGraphicsLayer> libraryPicker = new CKXMLAssetPicker<CKGraphicsLayer>(afactory.getAllGraphicsLayers());
			libraryPicker.addSelectedListener(new AddLayerListener());
			
			//create state editor for adding,removing,ordering, and height actions
			JPanel stateController = new CKSceneControllerPanel(scene);
			
			
			
			//TODO add menu items to load and save assets to database
			
			
			JPanel topPane = new JPanel();
			topPane.setLayout(new GridLayout(1,2));
			topPane.add(scenePanel);
			topPane.add(stateController);

			JPanel bottomPane = new JPanel();
			bottomPane.setLayout(new GridLayout(1,3));
			
			
			//bottomPane.add(layerPicker);
			bottomPane.add(scene.getXMLAssetViewer(ViewEnum.EDITABLE));
			bottomPane.add(libraryPicker);
			
			
			
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setAlignmentX(LEFT_ALIGNMENT);
			add(topPane);
			add(bottomPane);
			
			
		}
		
		private void reloadSceneLabel()
		{
			BufferedImage img = CKGraphicsPreviewGenerator.createScenePreview(scene, 200, 200);
			sceneLabel.setIcon(new ImageIcon(img));			
		}
		
		class CKSceneControllerPanel extends JPanel
		{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8480571957116700768L;

			public CKSceneControllerPanel(CKGraphicsSceneInterface A)
			{
				
				setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				setAlignmentX(LEFT_ALIGNMENT);
				
				
				
				ButtonGroup group = new ButtonGroup();
			
				//add remove radio
				JRadioButton removeButton = new JRadioButton("Remove Layer");
				removeButton.addItemListener(new RemoveLayer());
				group.add(removeButton);
				add(removeButton);

				
				//TODO I need to add an edit Layer button here
/*
				JRadioButton editButton = new JRadioButton("Edit Picked Layer");
				editButton.addItemListener(new AddLayer());
				group.add(editButton);
				add(editButton);
	*/				
			}
		}
		
		
		
		
		
		
		//adding asset listener for the library picker
		
		class AddLayerListener implements CKEntitySelectedListener<CKGraphicsLayer>
		{
			EditorState action;
			public AddLayerListener()
			{
				action=new AddLayer();
			}
			
			@Override
			public void entitySelected(CKGraphicsLayer layer)
			{
				action.doAction(layer);
				
			}

		}
		
		class LayerListener implements CKEntitySelectedListener<CKGraphicsLayer>
		{

			@Override
			public void entitySelected(CKGraphicsLayer layer)
			{
				state.doAction(layer);
			}
			
			
		}
	
		
		
		private abstract class EditorState implements ItemListener
		{
			abstract public void doAction(CKGraphicsLayer newLayer);

			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				state = this;
				
			}

		}



		


		private class RemoveLayer extends EditorState
		{

			@Override
			public void doAction(CKGraphicsLayer newLayer)
			{
				// TODO Auto-generated method stub
				scene.removeLayer(newLayer);
				layerPicker.rePopulateEntities(scene.layerIterator());
				reloadSceneLabel();
			
			}
		
		}	


		private class AddLayer extends EditorState
		{

			@Override
			public void doAction(CKGraphicsLayer newLayer)
			{
				// TODO Auto-generated method stub
				scene.addLayer(newLayer);
				layerPicker.rePopulateEntities(scene.layerIterator());
				reloadSceneLabel();				
			}
		
		}
	/*	
		private class Naming extends EditorState
		{

			@Override
			public void doAction(CKGraphicsAsset newAsset)
			{
				String name = asset.getAssetName(newAsset);
				
				String newName = JOptionPane.showInputDialog("What would you like the new name to be",
	                     name);
				asset.renameAsset(newAsset, newName);
			}

			@Override
			public void doAction(CKGraphicsLayer newLayer)
			{
				// TODO Auto-generated method stub
				
			}
		
		}
*/
		/*
		public static void main(String[] args)
		{
			JFrame frame = new JFrame();
			CKSceneFactory factory = CKSceneFactory.getInstance(); 
			CKGraphicsLayerFactory layerfactory = CKGraphicsLayerFactoryXML.getInstance(); 
			
			CKGraphicsSceneInterface scene = factory.getAsset("Kitchen");
			scene.setAID("");
			CKSceneEditor ed =
				new CKSceneEditor(scene,layerfactory);
			frame.add(ed);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			
			
		}*/
		
		
		
		
		
}
